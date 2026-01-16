package models;

import view.Observer;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KanbanModel implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- CONSTANTES ---
    private static final String NOME_ARQUIVO = "kanban_db.ser";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // --- SINGLETON ---
    private static KanbanModel instanciaUnica;

    // --- DADOS PERSISTIDOS ---
    private List<UsuarioEntity> usuarios;
    private List<ConviteEntity> convites;
    private List<TeamEntity> times;

    // --- ESTADO DA SESSÃO ---
    private UsuarioEntity usuarioLogado;
    private TeamEntity timeSelecionado;
    private BoardEntity quadroSelecionado;
    private CardEntity cardSelecionado;
    private ColumnEntity colunaSelecionada;

    // --- OBSERVERS (TRANSIENT) ---
    private transient List<Observer> observers;

    // ==================================================================================
    // 1. SINGLETON E INICIALIZAÇÃO
    // ==================================================================================

    private KanbanModel() {
        this.usuarios = new ArrayList<>();
        this.times = new ArrayList<>();
        this.convites = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public static KanbanModel getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = carregarDados();
            if (instanciaUnica == null) {
                instanciaUnica = new KanbanModel();
            }
        }
        return instanciaUnica;
    }

    public static void resetInstancia() {
        instanciaUnica = null;
    }

    // ==================================================================================
    // 2. PADRÃO OBSERVER
    // ==================================================================================

    public void attachObserver(Observer observer) {
        if (this.observers == null) {
            this.observers = new ArrayList<>();
        }
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detachObserver(Observer observer) {
        if (this.observers != null) {
            observers.remove(observer);
        }
    }

    public void notifica() {
        if (this.observers != null) {
            for (Observer o : observers) {
                o.update();
            }
        }
    }

    // ==================================================================================
    // 3. PERSISTÊNCIA (SALVAR/CARREGAR)
    // ==================================================================================

    public void salvarDados() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            out.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar dados: " + e.getMessage(), e);
        }
    }

    private static KanbanModel carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            KanbanModel model = (KanbanModel) in.readObject();
            model.observers = new ArrayList<>();
            return model;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return null;
        }
    }

    public void limparDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists()) {
            arquivo.delete();
        }
        this.usuarios.clear();
        this.times.clear();
        this.convites.clear();
        this.usuarioLogado = null;
        this.timeSelecionado = null;
        this.quadroSelecionado = null;
        this.cardSelecionado = null;
        this.colunaSelecionada = null;
        salvarDados();
        notifica();
    }

    // ==================================================================================
    // 4. MÓDULO DE USUÁRIOS (CRUD & AUTENTICAÇÃO)
    // ==================================================================================

    public void autenticarUsuario(String email, String senha) {
        validarParametro(email, "Email");
        validarParametro(senha, "Senha");

        for (UsuarioEntity u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.checkPassword(senha)) {
                this.usuarioLogado = u;
                notifica();
                return;
            }
        }
        throw new IllegalStateException("Usuário ou senha inválidos.");
    }

    public void deslogarUsuario() {
        this.usuarioLogado = null;
        limparContexto();
        notifica();
    }

    public UsuarioEntity getUsuarioLogado() {
        return usuarioLogado;
    }

    public void adicionarUsuario(String nome, String email, String senha) {
        validarParametro(nome, "Nome");
        validarParametro(email, "Email");
        validarParametro(senha, "Senha");

        if (senha.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres.");
        }

        if (buscarUsuarioPorEmail(email) != null) {
            throw new IllegalArgumentException("Já existe um usuário com o email informado.");
        }

        int maiorId = 0;
        if (usuarios != null) {
            for (UsuarioEntity u : usuarios) {
                if (u == null) continue;

                if (u.getId() > maiorId) {
                    maiorId = u.getId();
                }
            }
        }
        int novoId = maiorId + 1;

        UsuarioEntity novo = new UsuarioEntity(novoId, nome, email, senha);
        usuarios.add(novo);
        salvarDados();
        notifica();
    }

    public void editarUsuario(int id, String novoNome, String novoEmail, String novaSenha) {
        UsuarioEntity usuario = buscarUsuarioPorId(id);

        if (novoEmail != null && !novoEmail.trim().isEmpty() && !novoEmail.equals(usuario.getEmail())) {
            if (buscarUsuarioPorEmail(novoEmail) != null) {
                throw new IllegalArgumentException("Email já em uso.");
            }
            usuario.setEmail(novoEmail);
        }

        if (novoNome != null && !novoNome.trim().isEmpty()) {
            usuario.setUsername(novoNome);
        }

        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            if (novaSenha.length() < 6) {
                throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres.");
            }
            usuario.setPassword(novaSenha);
        }

        salvarDados();
        notifica();
    }

    public void deletarUsuario(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        if (usuarioLogado != null && usuarioLogado.getId() == id) {
            throw new IllegalStateException("Não pode deletar o próprio usuário logado.");
        }

        UsuarioEntity usuario = buscarUsuarioPorId(id);

        // Remove usuário de todos os times
        for (TeamEntity time : times) {
            if (time.getMembers().contains(usuario)) {
                if (!time.getOwner().equals(usuario)) {
                    time.getMembers().remove(usuario);
                } else {
                    throw new IllegalStateException("Não é possível deletar o líder de um time. Transfira a liderança primeiro.");
                }
            }
        }

        usuarios.remove(usuario);
        salvarDados();
        notifica();
    }

    public String[] listarUsuarios() {
        if (usuarios.isEmpty()) {
            return new String[0];
        }

        return usuarios.stream()
                .sorted((u1, u2) -> Integer.compare(u1.getId(), u2.getId()))
                .map(u -> String.format("%d - %s (%s)", u.getId(), u.getUsername(), u.getEmail()))
                .toArray(String[]::new);
    }

    public String[] consultarUsuario(int id) {
        UsuarioEntity usuario = buscarUsuarioPorId(id);
        return new String[]{
                "ID: " + usuario.getId(),
                "Nome: " + usuario.getUsername(),
                "Email: " + usuario.getEmail()
        };
    }

    public UsuarioEntity buscarUsuarioPorId(int id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public UsuarioEntity buscarUsuarioPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // ==================================================================================
    // 5. MÓDULO DE TIMES (CRUD & CONVITES)
    // ==================================================================================

    public void selecionarTime(int idTime) {
        validarUsuarioLogado();

        TeamEntity time = buscarTimePorId(idTime);

        if (!time.getMembers().contains(usuarioLogado)) {
            throw new IllegalStateException("Você não é membro deste time.");
        }

        this.timeSelecionado = time;
        this.quadroSelecionado = null;
        this.cardSelecionado = null;
        this.colunaSelecionada = null;
        notifica();
    }

    public TeamEntity getTimeSelecionado() {
        return timeSelecionado;
    }

    public void criarTime(String nome) {
        validarUsuarioLogado();
        validarParametro(nome, "Nome");

        int maiorId = 0;
        if (times != null) {
            for (TeamEntity t : times) {
                if (t == null) continue;

                if (t.getId() > maiorId) {
                    maiorId = t.getId();
                }
            }
        }
        int novoId = maiorId + 1;

        TeamEntity novoTime = new TeamEntity(novoId, nome, "", usuarioLogado);
        times.add(novoTime);
        salvarDados();
        notifica();
    }

    public void editarTime(int id, String novoNome) {
        validarUsuarioLogado();
        validarParametro(novoNome, "Nome");

        TeamEntity time = buscarTimePorId(id);
        validarPermissaoLider(time);

        time.setName(novoNome);
        salvarDados();
        notifica();
    }

    public void deletarTime(int id) {
        validarUsuarioLogado();

        TeamEntity time = buscarTimePorId(id);
        validarPermissaoLider(time);

        times.remove(time);

        if (timeSelecionado != null && timeSelecionado.getId() == id) {
            limparContexto();
        }

        salvarDados();
        notifica();
    }

    public void transferirLideranca(int idTime, int idNovoLider) {
        validarUsuarioLogado();

        TeamEntity time = buscarTimePorId(idTime);
        validarPermissaoLider(time);

        UsuarioEntity novoLider = buscarUsuarioPorId(idNovoLider);

        if (!time.getMembers().contains(novoLider)) {
            throw new IllegalStateException("O novo líder deve ser membro do time.");
        }

        time.setOwner(novoLider);
        salvarDados();
        notifica();
    }

    public void removerMembroDoTime(int idTime, int idMembro) {
        validarUsuarioLogado();

        TeamEntity time = buscarTimePorId(idTime);
        validarPermissaoLider(time);

        UsuarioEntity membro = buscarUsuarioPorId(idMembro);
        time.removeMember(membro);

        salvarDados();
        notifica();
    }

    public String[] listarMeusTimes() {
        if (usuarioLogado == null) {
            return new String[0];
        }

        return times.stream()
                .filter(t -> t.getMembers().contains(usuarioLogado))
                .map(t -> String.format("[%d] %s (Membros: %d)",
                        t.getId(), t.getName(), t.getMembers().size()))
                .toArray(String[]::new);
    }

    public String[] listarTimes() {
        return times.stream()
                .map(t -> String.format("ID: %d | Time: %s | Líder: %s | Membros: %d",
                        t.getId(), t.getName(), t.getOwner().getUsername(), t.getMembers().size()))
                .toArray(String[]::new);
    }

    public String[] listarMembrosDoTime(int idTime) {
        TeamEntity time = buscarTimePorId(idTime);

        return time.getMembers().stream()
                .map(m -> String.format("%d - %s (%s)%s",
                        m.getId(), m.getUsername(), m.getEmail(),
                        m.equals(time.getOwner()) ? " [LÍDER]" : ""))
                .toArray(String[]::new);
    }

    public TeamEntity buscarTimePorId(int id) {
        return times.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Time não encontrado."));
    }

    // --- CONVITES ---

    public void enviarConvite(String emailDestinatario) {
        validarUsuarioLogado();
        validarParametro(emailDestinatario, "Email do destinatário");
        TeamEntity time = timeSelecionado;
        UsuarioEntity destinatario = buscarUsuarioPorEmail(emailDestinatario);

        if (destinatario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (time.getMembers().contains(destinatario)) {
            throw new IllegalStateException("Usuário já é membro deste time.");
        }

        // Verifica se já existe convite pendente
        boolean convitePendente = convites.stream()
                .anyMatch(c -> c.getDestinatarioEmail().equalsIgnoreCase(emailDestinatario)
                        && c.getIdTime() == timeSelecionado.getId()
                        && c.getStatus().equals("PENDENTE"));

        if (convitePendente) {
            throw new IllegalStateException("Já existe um convite pendente para este usuário.");
        }

        int idConvite = convites.size() + 1;
        ConviteEntity convite = new ConviteEntity(
                idConvite, usuarioLogado.getEmail(), emailDestinatario, timeSelecionado.getId(), time.getName()
        );

        convites.add(convite);
        salvarDados();
        notifica();
    }

    public String[] verificarMeusConvites() {
        if (usuarioLogado == null) {
            return new String[0];
        }

        return convites.stream()
                .filter(c -> c.getDestinatarioEmail().equalsIgnoreCase(usuarioLogado.getEmail())
                        && c.getStatus().equals("PENDENTE"))
                .map(c -> String.format("%d#%s#%s", c.getId(), c.getNomeTime(), c.getRemetenteEmail()))
                .toArray(String[]::new);
    }

    public void deletarConvite(int id) {
        convites.removeIf(c -> c.getId() == id);
        salvarDados();
        notifica();
    }

    public void responderConvite(int idConvite, boolean aceitou) {
        validarUsuarioLogado();

        ConviteEntity convite = buscarConvitePorId(idConvite);

        if (!convite.getDestinatarioEmail().equalsIgnoreCase(usuarioLogado.getEmail())) {
            throw new IllegalStateException("Este convite não é para você.");
        }

        if (!convite.getStatus().equals("PENDENTE")) {
            throw new IllegalStateException("Este convite já foi respondido.");
        }

        if (aceitou) {
            convite.setStatus("ACEITO");
            entrarNoTime(convite.getIdTime());
        } else {
            convite.setStatus("RECUSADO");
        }

        salvarDados();
        notifica();
    }

    public void entrarNoTime(int idTime) {
        validarUsuarioLogado();

        TeamEntity time = buscarTimePorId(idTime);

        if (time.getMembers().contains(usuarioLogado)) {
            return;
        }

        time.addMember(usuarioLogado);
        salvarDados();
        notifica();
    }

    public ConviteEntity buscarConvitePorId(int id) {
        return convites.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Convite não encontrado."));
    }

    // ==================================================================================
    // 6. MÓDULO DE QUADROS (BOARDS)
    // ==================================================================================

    public void selecionarQuadro(int idQuadro) {
        if(idQuadro == -1) { quadroSelecionado = null; return; }
        validarTimeSelecionado();

        BoardEntity quadro = buscarQuadroPorId(idQuadro);
        this.quadroSelecionado = quadro;
        this.cardSelecionado = null;
        this.colunaSelecionada = null;
        notifica();
    }

    public BoardEntity getQuadroSelecionado() {
        return quadroSelecionado;
    }

    public void criarQuadro(String nome) {
        validarTimeSelecionado();
        validarParametro(nome, "Nome");

        int novoId = timeSelecionado.getBoards().size() + 1;
        BoardEntity novoQuadro = new BoardEntity(novoId, nome);

        timeSelecionado.addBoard(novoQuadro);
        salvarDados();
        notifica();
    }

    public void editarQuadro(int id, String novoNome) {
        validarTimeSelecionado();
        validarParametro(novoNome, "Nome");

        BoardEntity quadro = buscarQuadroPorId(id);
        quadro.setNome(novoNome);

        salvarDados();
        notifica();
    }

    public void deletarQuadro(int id) {
        validarTimeSelecionado();

        BoardEntity quadro = buscarQuadroPorId(id);
        timeSelecionado.getBoards().remove(quadro);

        if (quadroSelecionado != null && quadroSelecionado.getId() == id) {
            quadroSelecionado = null;
            cardSelecionado = null;
            colunaSelecionada = null;
        }

        salvarDados();
        notifica();
    }

    public String[] listarQuadros() {
        if (timeSelecionado == null) {
            return new String[0];
        }

        return timeSelecionado.getBoards().stream()
                .map(b -> String.format("%d - %s (%d cards)", b.getId(), b.getNome(), b.getCards().size()))
                .toArray(String[]::new);
    }

    public String[] consultarQuadro(int id) {
        validarTimeSelecionado();

        BoardEntity quadro = buscarQuadroPorId(id);
        return new String[]{
                "ID: " + quadro.getId(),
                "Nome: " + quadro.getNome(),
                "Número de Cards: " + quadro.getCards().size(),
                "Número de Colunas: " + quadro.getColunas().size()
        };
    }

    public BoardEntity buscarQuadroPorId(int id) {
        if (timeSelecionado == null) {
            throw new IllegalStateException("Nenhum time selecionado.");
        }

        return timeSelecionado.getBoards().stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Quadro não encontrado."));
    }

    // ==================================================================================
    // 7. MÓDULO DE COLUNAS
    // ==================================================================================

    public void selecionarColuna(int idColuna) {
        validarQuadroSelecionado();

        ColumnEntity coluna = buscarColunaPorId(idColuna);
        this.colunaSelecionada = coluna;
        notifica();
    }

    public ColumnEntity getColunaSelecionada() {
        return colunaSelecionada;
    }

    public void criarColuna(String nomeColuna) {
        validarQuadroSelecionado();
        validarParametro(nomeColuna, "Nome da coluna");

        int maiorId = 0;
        if (quadroSelecionado != null) {
            for (ColumnEntity c : quadroSelecionado.getColunas()) {
                if (c == null) continue;

                if (c.getId() > maiorId) {
                    maiorId = c.getId();
                }
            }
        }
        int novoId = maiorId + 1;

        quadroSelecionado.adicionarColuna(novoId, nomeColuna.toUpperCase());
        salvarDados();
        notifica();
    }

    public void editarColuna(int idColuna, String novoNome) {
        validarQuadroSelecionado();
        validarParametro(novoNome, "Nome da coluna");

        ColumnEntity coluna = buscarColunaPorId(idColuna);

        // Atualiza o status de todos os cards da coluna antiga
        String nomeAntigo = coluna.getName();
        String nomeNovo = novoNome.toUpperCase();

        for (CardEntity card : quadroSelecionado.getCards()) {
            if (card.getStatus().equals(nomeAntigo)) {
                card.setStatus(nomeNovo);
            }
        }

        coluna.setName(nomeNovo);
        salvarDados();
        notifica();
    }

    public void deletarColuna(int idColuna) {
        validarQuadroSelecionado();

        if (quadroSelecionado.getColunas().size() <= 1) {
            throw new IllegalStateException("Não é possível deletar a última coluna.");
        }

        ColumnEntity coluna = buscarColunaPorId(idColuna);
        String nomeColuna = coluna.getName();

        // Move todos os cards para a primeira coluna
        String primeiraColuna = quadroSelecionado.getColunas().get(0).getName();
        for (CardEntity card : quadroSelecionado.getCards()) {
            if (card.getStatus().equals(nomeColuna)) {
                card.setStatus(primeiraColuna);
            }
        }

        quadroSelecionado.getColunas().remove(coluna);

        if (colunaSelecionada != null && colunaSelecionada.getId() == idColuna) {
            colunaSelecionada = null;
        }

        salvarDados();
        notifica();
    }

    public String[] listarColunas() {
        if (quadroSelecionado == null) {
            return new String[0];
        }

        return quadroSelecionado.getColunas().stream()
                .map(c -> {
                    int numCards = (int) quadroSelecionado.getCards().stream()
                            .filter(card -> card.getStatus().equals(c.getName()))
                            .count();
                    return String.format("%d - %s (%d cards)", c.getId(), c.getName(), numCards);
                })
                .toArray(String[]::new);
    }

    public ColumnEntity buscarColunaPorId(int id) {
        if (quadroSelecionado == null) {
            throw new IllegalStateException("Nenhum quadro selecionado.");
        }

        return quadroSelecionado.getColunas().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Coluna não encontrada."));
    }

    public String[] buscarColunaPorIdString(int id) {
        if (quadroSelecionado == null) {
            throw new IllegalStateException("Nenhum quadro selecionado.");
        }

        // 1. Busca a coluna
        ColumnEntity col = quadroSelecionado.getColunas().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Coluna não encontrada."));

        // 2. Busca os cards (assumindo que retorna uma List<String>)
        // Se retornar array, use List.of(listarCards...)
        List<String> cards = listarCardsPorColunaId(col.getId());

        // 3. Monta a lista final dinâmica
        List<String> resultado = new ArrayList<>();
        resultado.add(String.valueOf(col.getId()));             // Índice 0: ID
        resultado.add(col.getName() != null ? col.getName() : ""); // Índice 1: Nome
        resultado.addAll(cards);                                // Índice 2+: Cards

        // 4. Converte de volta para String[]
        return resultado.toArray(new String[0]);
    }
    // ==================================================================================
    // 8. MÓDULO DE CARDS (TAREFAS)
    // ==================================================================================

    public void selecionarCard(int idCard) {
        validarQuadroSelecionado();

        CardEntity card = buscarCardPorId(idCard);
        this.cardSelecionado = card;
        notifica();
    }

    public CardEntity getCardSelecionado() {
        return cardSelecionado;
    }

    public void criarCardColuna(int idColuna, String titulo, String descricao, CardPriority prioridade) {
        validarQuadroSelecionado();
        selecionarColuna(idColuna);

        validarParametro(titulo, "Título");

        int novoId = quadroSelecionado.getCards().size() + 1;

        CardEntity novo = new CardEntity(novoId, titulo, descricao, prioridade, colunaSelecionada.getName());
        novo.setAssignee(usuarioLogado);

        quadroSelecionado.addCard(novo);
        salvarDados();
        notifica();
    }


    public void criarCard(String titulo, String descricao, CardPriority prioridade) {
        validarQuadroSelecionado();
        validarParametro(titulo, "Título");

        String primeiraColuna = quadroSelecionado.getColunas().get(0).getName();
        int novoId = quadroSelecionado.getCards().size() + 1;

        CardEntity novo = new CardEntity(novoId, titulo, descricao, prioridade, primeiraColuna);
        novo.setAssignee(usuarioLogado);

        quadroSelecionado.addCard(novo);
        salvarDados();
        notifica();
    }

    public void editarCard(int id, String titulo, String descricao, CardPriority prioridade) {
        validarQuadroSelecionado();

        CardEntity card = buscarCardPorId(id);

        if (titulo != null && !titulo.trim().isEmpty()) {
            card.setTitle(titulo);
        }

        if (descricao != null && !descricao.trim().isEmpty()) {
            card.setDescription(descricao);
        }

        if (prioridade != null) {
            card.setPriority(prioridade);
        }

        salvarDados();
        notifica();
    }

    public void atribuirCard(int idCard, int idUsuario) {
        validarQuadroSelecionado();

        CardEntity card = buscarCardPorId(idCard);
        UsuarioEntity usuario = buscarUsuarioPorId(idUsuario);

        if (!timeSelecionado.getMembers().contains(usuario)) {
            throw new IllegalStateException("O usuário deve ser membro do time.");
        }

        card.setAssignee(usuario);
        salvarDados();
        notifica();
    }

    public void deletarCard(int id) {
        validarQuadroSelecionado();

        CardEntity card = buscarCardPorId(id);
        quadroSelecionado.removeCard(card);

        if (cardSelecionado != null && cardSelecionado.getId() == id) {
            cardSelecionado = null;
        }

        salvarDados();
        notifica();
    }

    public void moverCard(int idCard) {
        validarQuadroSelecionado();

        CardEntity card = buscarCardPorId(idCard);
        List<String> nomesColunas = quadroSelecionado.getColunas().stream()
                .map(ColumnEntity::getName)
                .collect(Collectors.toList());

        int indexAtual = nomesColunas.indexOf(card.getStatus());

        if (indexAtual == -1) {
            card.setStatus(nomesColunas.get(0));
        } else if (indexAtual < nomesColunas.size() - 1) {
            card.setStatus(nomesColunas.get(indexAtual + 1));
        } else {
            throw new IllegalStateException("O card já está na última etapa!");
        }

        salvarDados();
        notifica();
    }

    public void moverCardParaColuna(int idCard, int idColuna) {
        validarQuadroSelecionado();

        CardEntity card = buscarCardPorId(idCard);
        ColumnEntity coluna = buscarColunaPorId(idColuna);

        card.setStatus(coluna.getName().toUpperCase());
        salvarDados();
        notifica();
    }

    public String[] listarCard() {
        if (cardSelecionado == null) {
            return new String[0];
        }

        CardEntity card = cardSelecionado;
        return new String[]{
                String.valueOf(card.getId()),
                card.getTitle() != null ? card.getTitle() : "",
                card.getDescription() != null ? card.getDescription() : "",
                card.getStatus() != null ? card.getStatus() : "Sem status",
                card.getCreatedAt() != null ? card.getCreatedAt().format(DATE_FORMATTER) : "",
                card.getAssignee() != null ? card.getAssignee().getUsername() : "Não atribuído",
                card.getPriority() != null ? card.getPriority().toString() : "MEDIA"
        };
    }

    public String[] listarCardsPorColuna() {
        if (quadroSelecionado == null) {
            return new String[0];
        }

        List<String> relatorio = new ArrayList<>();

        for (ColumnEntity coluna : quadroSelecionado.getColunas()) {
            String nomeColuna = coluna.getName();
            relatorio.add("--- " + nomeColuna + " ---");

            List<CardEntity> cardsNaColuna = quadroSelecionado.getCards().stream()
                    .filter(c -> c.getStatus().equals(nomeColuna))
                    .sorted((c1, c2) -> Integer.compare(c1.getId(), c2.getId()))
                    .collect(Collectors.toList());

            if (cardsNaColuna.isEmpty()) {
                relatorio.add("Vazio");
            } else {
                for (CardEntity card : cardsNaColuna) {
                    relatorio.add(card.toString());
                }
            }

            relatorio.add("");
        }

        return relatorio.toArray(new String[0]);
    }

    public List<String> listarCardsPorColunaId(int colId) {
        // Retorna uma lista vazia
        if (quadroSelecionado == null) {
            return new ArrayList<>();
        }

        List<String> relatorio = new ArrayList<>();

        // Busca a coluna
        ColumnEntity coluna = quadroSelecionado.getColunas()
                .stream()
                .filter(c -> c.getId() == colId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Coluna não encontrada."));

        String nomeColuna = coluna.getName();

        // Busca os cards
        List<CardEntity> cardsNaColuna = quadroSelecionado.getCards().stream()
                .filter(c -> nomeColuna.equals(c.getStatus()))
                .sorted(Comparator.comparingInt(CardEntity::getId))
                .collect(Collectors.toList());

        if (cardsNaColuna.isEmpty()) {
            relatorio.add("Sem cards");
        } else {
            for (CardEntity card : cardsNaColuna) {
                relatorio.add(card.toString());
            }
        }

        relatorio.add("");
        return relatorio;
    }

    public String[] filtrarCardsPorPrioridade(CardPriority prioridade) {
        validarQuadroSelecionado();

        if (prioridade == null) {
            throw new IllegalArgumentException("Prioridade não pode ser nula.");
        }

        return quadroSelecionado.getCards().stream()
                .filter(c -> c.getPriority() == prioridade)
                .map(CardEntity::toString)
                .toArray(String[]::new);
    }

    public String[] filtrarCardsPorResponsavel(int idUsuario) {
        validarQuadroSelecionado();

        return quadroSelecionado.getCards().stream()
                .filter(c -> c.getAssignee() != null && c.getAssignee().getId() == idUsuario)
                .map(CardEntity::toString)
                .toArray(String[]::new);
    }

    public CardEntity buscarCardPorId(int id) {
        if (quadroSelecionado == null) {
            throw new IllegalStateException("Nenhum quadro selecionado.");
        }

        return quadroSelecionado.getCards().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Card não encontrado."));
    }

    // ==================================================================================
    // 9. MÉTODOS DE VALIDAÇÃO (DRY - Don't Repeat Yourself)
    // ==================================================================================

    private void validarUsuarioLogado() {
        if (usuarioLogado == null) {
            throw new IllegalStateException("Nenhum usuário está logado.");
        }
    }

    private void validarTimeSelecionado() {
        if (timeSelecionado == null) {
            throw new IllegalStateException("Nenhum time selecionado.");
        }
    }

    private void validarQuadroSelecionado() {
        if (quadroSelecionado == null) {
            throw new IllegalStateException("Nenhum quadro selecionado.");
        }
    }

    private void validarParametro(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " é obrigatório.");
        }
    }

    private void validarPermissaoLider(TeamEntity time) {
        if (!time.getOwner().equals(usuarioLogado)) {
            throw new IllegalStateException("Apenas o líder pode realizar esta ação.");
        }
    }

    private void limparContexto() {
        this.timeSelecionado = null;
        this.quadroSelecionado = null;
        this.cardSelecionado = null;
        this.colunaSelecionada = null;
    }

    // ==================================================================================
    // 10. MÉTODOS DE ESTATÍSTICAS E RELATÓRIOS
    // ==================================================================================

    public String[] gerarEstatisticasDoQuadro() {
        validarQuadroSelecionado();

        List<String> stats = new ArrayList<>();
        stats.add("=== ESTATÍSTICAS DO QUADRO: " + quadroSelecionado.getNome() + " ===");
        stats.add("");

        int totalCards = quadroSelecionado.getCards().size();
        stats.add("Total de Cards: " + totalCards);

        if (totalCards > 0) {
            stats.add("");
            stats.add("--- Por Prioridade ---");
            for (CardPriority p : CardPriority.values()) {
                long count = quadroSelecionado.getCards().stream()
                        .filter(c -> c.getPriority() == p)
                        .count();
                stats.add(p + ": " + count);
            }

            stats.add("");
            stats.add("--- Por Coluna ---");
            for (ColumnEntity col : quadroSelecionado.getColunas()) {
                long count = quadroSelecionado.getCards().stream()
                        .filter(c -> c.getStatus().equals(col.getName()))
                        .count();
                stats.add(col.getName() + ": " + count);
            }

            stats.add("");
            stats.add("--- Por Responsável ---");
            quadroSelecionado.getCards().stream()
                    .map(CardEntity::getAssignee)
                    .distinct()
                    .forEach(assignee -> {
                        String nome = assignee != null ? assignee.getUsername() : "Não atribuído";
                        long count = quadroSelecionado.getCards().stream()
                                .filter(c -> {
                                    if (assignee == null) return c.getAssignee() == null;
                                    return c.getAssignee() != null && c.getAssignee().equals(assignee);
                                })
                                .count();
                        stats.add(nome + ": " + count);
                    });
        }

        return stats.toArray(new String[0]);
    }

    public String[] gerarRelatorioCompleto() {
        List<String> relatorio = new ArrayList<>();

        relatorio.add("========================================");
        relatorio.add("      RELATÓRIO COMPLETO DO SISTEMA");
        relatorio.add("========================================");
        relatorio.add("");

        relatorio.add("Total de Usuários: " + usuarios.size());
        relatorio.add("Total de Times: " + times.size());
        relatorio.add("Total de Convites Pendentes: " +
                convites.stream().filter(c -> c.getStatus().equals("PENDENTE")).count());

        if (usuarioLogado != null) {
            relatorio.add("");
            relatorio.add("Usuário Logado: " + usuarioLogado.getUsername());

            long meusTimesCount = times.stream()
                    .filter(t -> t.getMembers().contains(usuarioLogado))
                    .count();
            relatorio.add("Meus Times: " + meusTimesCount);
        }

        return relatorio.toArray(new String[0]);
    }

    // ==================================================================================
    // 11. GETTERS ADICIONAIS
    // ==================================================================================

    public List<UsuarioEntity> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public List<TeamEntity> getTimes() {
        return new ArrayList<>(times);
    }

    public List<ConviteEntity> getConvites() {
        return new ArrayList<>(convites);
    }

    public boolean isUsuarioLogado() {
        return usuarioLogado != null;
    }

    public boolean isTimeSelecionado() {
        return timeSelecionado != null;
    }

    public boolean isQuadroSelecionado() {
        return quadroSelecionado != null;
    }
}