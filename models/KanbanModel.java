package models;

import view.Observer;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KanbanModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String NOME_ARQUIVO = "kanban_db.ser";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static KanbanModel instanciaUnica;

    // Dados
    private List<UsuarioEntity> usuarios;
    private List<ConviteEntity> convites;
    private List<TimeEntity> times;

    // Contexto da sessão atual
    private UsuarioEntity usuarioLogado;
    private TimeEntity timeSelecionado;
    private QuadroEntity quadroSelecionado;
    private CardEntity cardSelecionado;
    private ColunaEntity colunaSelecionada;

    private transient List<Observer> observers;

    private KanbanModel() {
        this.usuarios = new ArrayList<>();
        this.times = new ArrayList<>();
        this.convites = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    // Singleton com lazy loading básico
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

    // --- Observer ---

    public void attachObserver(Observer observer) {
        if (this.observers == null) this.observers = new ArrayList<>();
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detachObserver(Observer observer) {
        if (this.observers != null) observers.remove(observer);
    }

    public void notifica() {
        if (this.observers != null) {
            for (Observer o : observers) o.update();
        }
    }

    // --- Persistência ---

    public void salvarDados() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            out.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar: " + e.getMessage(), e);
        }
    }

    private static KanbanModel carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            KanbanModel model = (KanbanModel) in.readObject();
            model.observers = new ArrayList<>(); // Lista transient precisa ser recriada
            return model;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
            return null;
        }
    }

    public void limparDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists()) arquivo.delete();

        this.usuarios.clear();
        this.times.clear();
        this.convites.clear();
        limparContexto();
        this.usuarioLogado = null;

        salvarDados();
        notifica();
    }

    // --- Usuários ---

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

    public String[] getUsuarioLogadoString() {
        return new String[]{
                String.valueOf(usuarioLogado.getId()),
                usuarioLogado.getUsername(),
                usuarioLogado.getEmail(),
                usuarioLogado.getPasswordHash()
        };
    }

    public UsuarioEntity getUsuarioLogado() {
        return usuarioLogado;
    }

    public void adicionarUsuario(String nome, String email, String senha) {
        validarParametro(nome, "Nome");
        validarParametro(email, "Email");
        validarParametro(senha, "Senha");

        if (senha.length() < 6) throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres.");
        if (buscarUsuarioPorEmail(email) != null) throw new IllegalArgumentException("Email já existe.");

        // Simula auto-increment
        int maiorId = usuarios.stream()
                .filter(u -> u != null)
                .mapToInt(UsuarioEntity::getId)
                .max().orElse(0);

        usuarios.add(new UsuarioEntity(maiorId + 1, nome, email, senha));
        salvarDados();
        notifica();
    }

    public void editarUsuario(String novoNome, String novoEmail, String novaSenha) {
        UsuarioEntity usuario = usuarioLogado;

        if (novoEmail != null && !novoEmail.trim().isEmpty() && !novoEmail.equals(usuario.getEmail())) {
            if (buscarUsuarioPorEmail(novoEmail) != null) throw new IllegalArgumentException("Email em uso.");
            usuario.setEmail(novoEmail);
        }

        if (novoNome != null && !novoNome.trim().isEmpty()) usuario.setUsername(novoNome);

        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            if (novaSenha.length() < 6) throw new IllegalArgumentException("Senha curta demais.");
            usuario.setPassword(novaSenha);
        }

        salvarDados();
        notifica();
    }

    public void deletarUsuario(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        if (usuarioLogado != null && usuarioLogado.getId() == id) {
            throw new IllegalStateException("Não pode se auto-deletar.");
        }

        UsuarioEntity usuario = buscarUsuarioPorId(id);

        // Remove dos times, mas bloqueia se for Owner
        for (TimeEntity time : times) {
            if (time.getMembers().contains(usuario)) {
                if (time.getOwner().equals(usuario)) {
                    throw new IllegalStateException("Transfira a liderança dos times antes de deletar o usuário.");
                }
                time.getMembers().remove(usuario);
            }
        }

        usuarios.remove(usuario);
        salvarDados();
        notifica();
    }

    public String[] listarUsuarios() {
        if (usuarios.isEmpty()) return new String[0];
        return usuarios.stream()
                .sorted(Comparator.comparingInt(UsuarioEntity::getId))
                .map(u -> String.format("%d - %s (%s)", u.getId(), u.getUsername(), u.getEmail()))
                .toArray(String[]::new);
    }

    public String[] consultarUsuario(int id) {
        UsuarioEntity usuario = buscarUsuarioPorId(id);
        return new String[]{ "ID: " + usuario.getId(), "Nome: " + usuario.getUsername(), "Email: " + usuario.getEmail() };
    }

    public UsuarioEntity buscarUsuarioPorId(int id) {
        return usuarios.stream().filter(u -> u.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public UsuarioEntity buscarUsuarioPorEmail(String email) {
        return usuarios.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }

    // --- Times ---

    public void selecionarTime(int idTime) {
        if(idTime == -1) {
            limparContexto();
            return;
        }
        validarUsuarioLogado();

        TimeEntity time = buscarTimePorId(idTime);
        if (!time.getMembers().contains(usuarioLogado)) {
            throw new IllegalStateException("Acesso negado ao time.");
        }

        this.timeSelecionado = time;
        // Reseta filhos
        this.quadroSelecionado = null;
        this.cardSelecionado = null;
        this.colunaSelecionada = null;
        notifica();
    }

    public TimeEntity getTimeSelecionado() {
        return timeSelecionado;
    }

    public void criarTime(String nome) {
        validarUsuarioLogado();
        validarParametro(nome, "Nome");

        int maiorId = times.stream().filter(t -> t != null).mapToInt(TimeEntity::getId).max().orElse(0);

        times.add(new TimeEntity(maiorId + 1, nome, "", usuarioLogado));
        salvarDados();
        notifica();
    }

    public void editarTime(String novoNome) {
        validarUsuarioLogado();
        validarParametro(novoNome, "Nome");
        validarPermissaoLider(timeSelecionado);

        timeSelecionado.setName(novoNome);
        salvarDados();
        notifica();
    }

    public void deletarTime() {
        validarUsuarioLogado();
        validarPermissaoLider(timeSelecionado);

        times.remove(timeSelecionado);
        limparContexto();
        salvarDados();
        notifica();
    }

    public void transferirLideranca(int idTime, int idNovoLider) {
        validarUsuarioLogado();
        TimeEntity time = buscarTimePorId(idTime);
        validarPermissaoLider(time);

        UsuarioEntity novoLider = buscarUsuarioPorId(idNovoLider);
        if (!time.getMembers().contains(novoLider)) {
            throw new IllegalStateException("O novo líder precisa ser membro do time.");
        }

        time.setOwner(novoLider);
        salvarDados();
        notifica();
    }

    public void removerMembroDoTime(int idTime, int idMembro) {
        validarUsuarioLogado();
        TimeEntity time = buscarTimePorId(idTime);
        validarPermissaoLider(time);

        time.removeMember(buscarUsuarioPorId(idMembro));
        salvarDados();
        notifica();
    }

    public String[] listarMeusTimes() {
        if (usuarioLogado == null) return new String[0];
        return times.stream()
                .filter(t -> t.getMembers().contains(usuarioLogado))
                .map(t -> String.format("[%d] %s (Membros: %d)", t.getId(), t.getName(), t.getMembers().size()))
                .toArray(String[]::new);
    }

    public String[] listarTimes() {
        return times.stream()
                .map(t -> String.format("ID: %d | Time: %s | Líder: %s | Membros: %d",
                        t.getId(), t.getName(), t.getOwner().getUsername(), t.getMembers().size()))
                .toArray(String[]::new);
    }

    public String[] listarMembrosDoTime(int idTime) {
        TimeEntity time = buscarTimePorId(idTime);
        return time.getMembers().stream()
                .map(m -> String.format("%d - %s (%s)%s",
                        m.getId(), m.getUsername(), m.getEmail(),
                        m.equals(time.getOwner()) ? " [LÍDER]" : ""))
                .toArray(String[]::new);
    }

    public TimeEntity buscarTimePorId(int id) {
        return times.stream().filter(t -> t.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Time não encontrado."));
    }

    // --- Convites ---

    public void enviarConvite(String emailDestinatario) {
        validarUsuarioLogado();
        validarParametro(emailDestinatario, "Email");

        UsuarioEntity destinatario = buscarUsuarioPorEmail(emailDestinatario);
        if (destinatario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        if (timeSelecionado.getMembers().contains(destinatario)) throw new IllegalStateException("Usuário já está no time.");

        // Evita spam de convites
        boolean pendente = convites.stream()
                .anyMatch(c -> c.getDestinatarioEmail().equalsIgnoreCase(emailDestinatario)
                        && c.getIdTime() == timeSelecionado.getId()
                        && c.getStatus().equals("PENDENTE"));

        if (pendente) throw new IllegalStateException("Convite já enviado.");

        convites.add(new ConviteEntity(
                convites.size() + 1,
                usuarioLogado.getEmail(),
                emailDestinatario,
                timeSelecionado.getId(),
                timeSelecionado.getName()
        ));

        salvarDados();
        notifica();
    }

    public String[] verificarMeusConvites() {
        if (usuarioLogado == null) return new String[0];
        return convites.stream()
                .filter(c -> c.getDestinatarioEmail().equalsIgnoreCase(usuarioLogado.getEmail()) && c.getStatus().equals("PENDENTE"))
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

        if (!convite.getDestinatarioEmail().equalsIgnoreCase(usuarioLogado.getEmail())) throw new IllegalStateException("Convite inválido.");
        if (!convite.getStatus().equals("PENDENTE")) throw new IllegalStateException("Convite já respondido.");

        convite.setStatus(aceitou ? "ACEITO" : "RECUSADO");
        if (aceitou) entrarNoTime(convite.getIdTime());

        salvarDados();
        notifica();
    }

    public void entrarNoTime(int idTime) {
        validarUsuarioLogado();
        TimeEntity time = buscarTimePorId(idTime);
        if (!time.getMembers().contains(usuarioLogado)) {
            time.addMember(usuarioLogado);
            salvarDados();
            notifica();
        }
    }

    public ConviteEntity buscarConvitePorId(int id) {
        return convites.stream().filter(c -> c.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Convite não encontrado."));
    }

    // --- Quadros ---

    public void selecionarQuadro(int idQuadro) {
        if(idQuadro == -1) { quadroSelecionado = null; return; }
        validarTimeSelecionado();

        this.quadroSelecionado = buscarQuadroPorId(idQuadro);
        this.cardSelecionado = null;
        this.colunaSelecionada = null;
        notifica();
    }

    public QuadroEntity getQuadroSelecionado() { return quadroSelecionado; }

    public void criarQuadro(String nome) {
        validarTimeSelecionado();
        validarParametro(nome, "Nome");

        timeSelecionado.addBoard(new QuadroEntity(timeSelecionado.getBoards().size() + 1, nome));
        salvarDados();
        notifica();
    }

    public void editarQuadro(int id, String novoNome) {
        validarTimeSelecionado();
        validarParametro(novoNome, "Nome");

        buscarQuadroPorId(id).setNome(novoNome);
        salvarDados();
        notifica();
    }

    public void deletarQuadro() {
        validarTimeSelecionado();
        validarQuadroSelecionado();

        if (!timeSelecionado.getOwner().equals(usuarioLogado)) {
            throw new IllegalStateException("Apenas o líder pode deletar o quadro.");
        }
        timeSelecionado.removeBoard(quadroSelecionado);
        // --------------------------

        this.quadroSelecionado = null;
        this.cardSelecionado = null;
        this.colunaSelecionada = null;

        salvarDados();
        notifica();
    }

    public String[] listarQuadros() {
        if (timeSelecionado == null) return new String[0];
        return timeSelecionado.getBoards().stream()
                .map(b -> String.format("%d - %s (%d cards)", b.getId(), b.getNome(), b.getCards().size()))
                .toArray(String[]::new);
    }

    public String[] consultarQuadro(int id) {
        validarTimeSelecionado();
        QuadroEntity quadro = buscarQuadroPorId(id);
        return new String[]{
                "ID: " + quadro.getId(),
                "Nome: " + quadro.getNome(),
                "Cards: " + quadro.getCards().size(),
                "Colunas: " + quadro.getColunas().size()
        };
    }

    public QuadroEntity buscarQuadroPorId(int id) {
        if (timeSelecionado == null) throw new IllegalStateException("Time não selecionado.");
        return timeSelecionado.getBoards().stream().filter(b -> b.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Quadro não encontrado."));
    }

    // --- Colunas ---

    public void selecionarColuna(int idColuna) {
        validarQuadroSelecionado();
        this.colunaSelecionada = buscarColunaPorId(idColuna);
        notifica();
    }

    public ColunaEntity getColunaSelecionada() { return colunaSelecionada; }

    public void criarColuna(String nomeColuna) {
        validarQuadroSelecionado();
        validarParametro(nomeColuna, "Nome da coluna");

        int maiorId = quadroSelecionado.getColunas().stream()
                .filter(c -> c != null)
                .mapToInt(ColunaEntity::getId).max().orElse(0);

        quadroSelecionado.adicionarColuna(maiorId + 1, nomeColuna.toUpperCase());
        salvarDados();
        notifica();
    }

    public void editarColuna(int idColuna, String novoNome) {
        validarQuadroSelecionado();
        validarParametro(novoNome, "Nome da coluna");
        ColunaEntity coluna = buscarColunaPorId(idColuna);

        String nomeAntigo = coluna.getName();
        String nomeNovo = novoNome.toUpperCase();

        // Atualiza cards para não ficarem com status órfão
        for (CardEntity card : quadroSelecionado.getCards()) {
            if (card.getStatus().equals(nomeAntigo)) card.setStatus(nomeNovo);
        }

        coluna.setName(nomeNovo);
        salvarDados();
        notifica();
    }

    public void deletarColuna(int idColuna) {
        validarQuadroSelecionado();
        if (quadroSelecionado.getColunas().size() <= 1) throw new IllegalStateException("Deve haver ao menos uma coluna.");

        ColunaEntity coluna = buscarColunaPorId(idColuna);
        String nomeColuna = coluna.getName();
        String primeiraColuna = quadroSelecionado.getColunas().get(0).getName();

        for (CardEntity card : quadroSelecionado.getCards()) {
            if (card.getStatus().equals(nomeColuna)) card.setStatus(primeiraColuna);
        }

        quadroSelecionado.removeColuna(coluna);
        if (colunaSelecionada != null && colunaSelecionada.getId() == idColuna) colunaSelecionada = null;

        salvarDados();
        notifica();
    }

    public String[] listarColunas() {
        if (quadroSelecionado == null) return new String[0];
        return quadroSelecionado.getColunas().stream()
                .map(c -> {
                    long numCards = quadroSelecionado.getCards().stream().filter(card -> card.getStatus().equals(c.getName())).count();
                    return String.format("%d - %s (%d cards)", c.getId(), c.getName(), numCards);
                })
                .toArray(String[]::new);
    }

    public ColunaEntity buscarColunaPorId(int id) {
        if (quadroSelecionado == null) throw new IllegalStateException("Quadro não selecionado.");
        return quadroSelecionado.getColunas().stream().filter(c -> c.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Coluna não encontrada."));
    }

    public String[] buscarColunaPorIdString(int id) {
        if (quadroSelecionado == null) throw new IllegalStateException("Quadro não selecionado.");
        ColunaEntity col = buscarColunaPorId(id);

        List<String> resultado = new ArrayList<>();
        resultado.add(String.valueOf(col.getId()));
        resultado.add(col.getName() != null ? col.getName() : "");
        resultado.addAll(listarCardsPorColunaId(col.getId()));

        return resultado.toArray(new String[0]);
    }

    // --- Cards ---

    public void selecionarCard(int idCard) {
        validarQuadroSelecionado();
        this.cardSelecionado = buscarCardPorId(idCard);
        notifica();
    }

    public CardEntity getCardSelecionado() { return cardSelecionado; }

    public void criarCardColuna(int idColuna, String titulo, String descricao, CardPriority prioridade) {
        validarQuadroSelecionado();
        selecionarColuna(idColuna);
        validarParametro(titulo, "Título");

        CardEntity novo = new CardEntity(
                quadroSelecionado.getCards().size() + 1,
                titulo, descricao, prioridade,
                colunaSelecionada.getName()
        );
        novo.setAssignee(usuarioLogado);

        quadroSelecionado.addCard(novo);
        salvarDados();
        notifica();
    }

    public void criarCard(String titulo, String descricao, CardPriority prioridade) {
        validarQuadroSelecionado();
        validarParametro(titulo, "Título");

        CardEntity novo = new CardEntity(
                quadroSelecionado.getCards().size() + 1,
                titulo, descricao, prioridade,
                quadroSelecionado.getColunas().get(0).getName()
        );
        novo.setAssignee(usuarioLogado);

        quadroSelecionado.addCard(novo);
        salvarDados();
        notifica();
    }

    public void editarCard(int id, String titulo, String descricao, CardPriority prioridade) {
        validarQuadroSelecionado();
        CardEntity card = buscarCardPorId(id);

        if (titulo != null && !titulo.trim().isEmpty()) card.setTitle(titulo);
        if (descricao != null && !descricao.trim().isEmpty()) card.setDescription(descricao);
        if (prioridade != null) card.setPriority(prioridade);

        salvarDados();
        notifica();
    }

    public void atribuirCard(int idCard, int idUsuario) {
        validarQuadroSelecionado();
        CardEntity card = buscarCardPorId(idCard);
        UsuarioEntity usuario = buscarUsuarioPorId(idUsuario);

        if (!timeSelecionado.getMembers().contains(usuario)) throw new IllegalStateException("Usuário não é do time.");

        card.setAssignee(usuario);
        salvarDados();
        notifica();
    }

    public void deletarCard(int id) {
        validarQuadroSelecionado();
        quadroSelecionado.removeCard(buscarCardPorId(id));

        if (cardSelecionado != null && cardSelecionado.getId() == id) cardSelecionado = null;

        salvarDados();
        notifica();
    }

    public void moverCard(int idCard) {
        validarQuadroSelecionado();
        CardEntity card = buscarCardPorId(idCard);
        List<String> colunas = quadroSelecionado.getColunas().stream().map(ColunaEntity::getName).collect(Collectors.toList());

        int index = colunas.indexOf(card.getStatus());

        if (index == -1) card.setStatus(colunas.get(0));
        else if (index < colunas.size() - 1) card.setStatus(colunas.get(index + 1));
        else throw new IllegalStateException("Card já finalizado.");

        salvarDados();
        notifica();
    }

    public void moverCardParaColuna(int idCard, int idColuna) {
        validarQuadroSelecionado();
        CardEntity card = buscarCardPorId(idCard);
        card.setStatus(buscarColunaPorId(idColuna).getName().toUpperCase());
        salvarDados();
        notifica();
    }

    public String[] listarCard() {
        if (cardSelecionado == null) return new String[0];
        CardEntity c = cardSelecionado;
        return new String[]{
                String.valueOf(c.getId()),
                c.getTitle(),
                c.getDescription(),
                c.getStatus(),
                c.getCreatedAt() != null ? c.getCreatedAt().format(DATE_FORMATTER) : "",
                c.getAssignee() != null ? c.getAssignee().getUsername() : "Não atribuído",
                c.getPriority().toString()
        };
    }

    public String[] listarCardsPorColuna() {
        if (quadroSelecionado == null) return new String[0];
        List<String> relatorio = new ArrayList<>();

        for (ColunaEntity coluna : quadroSelecionado.getColunas()) {
            relatorio.add("--- (" + coluna.getId() + ") " +  coluna.getName() + " ---");

            List<CardEntity> cards = quadroSelecionado.getCards().stream()
                    .filter(c -> c.getStatus().equals(coluna.getName()))
                    .sorted(Comparator.comparingInt(CardEntity::getId))
                    .collect(Collectors.toList());

            if (cards.isEmpty()) relatorio.add("  Vazio");
            else for (CardEntity c : cards) relatorio.add(c.toString());

            relatorio.add("");
        }
        return relatorio.toArray(new String[0]);
    }

    public List<String> listarCardsPorColunaId(int colId) {
        if (quadroSelecionado == null) return new ArrayList<>();

        String nomeColuna = buscarColunaPorId(colId).getName();
        List<String> relatorio = new ArrayList<>();

        List<CardEntity> cards = quadroSelecionado.getCards().stream()
                .filter(c -> nomeColuna.equals(c.getStatus()))
                .sorted(Comparator.comparingInt(CardEntity::getId))
                .collect(Collectors.toList());

        if (cards.isEmpty()) relatorio.add("Sem cards");
        else for (CardEntity c : cards) relatorio.add(c.toString());

        relatorio.add("");
        return relatorio;
    }

    public String[] filtrarCardsPorPrioridade(CardPriority prioridade) {
        validarQuadroSelecionado();
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
        if (quadroSelecionado == null) throw new IllegalStateException("Quadro não selecionado.");
        return quadroSelecionado.getCards().stream().filter(c -> c.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Card não encontrado."));
    }

    // --- Helpers ---

    private void validarUsuarioLogado() {
        if (usuarioLogado == null) throw new IllegalStateException("Login necessário.");
    }

    private void validarTimeSelecionado() {
        if (timeSelecionado == null) throw new IllegalStateException("Selecione um time.");
    }

    private void validarQuadroSelecionado() {
        if (quadroSelecionado == null) throw new IllegalStateException("Selecione um quadro.");
    }

    private void validarParametro(String valor, String nome) {
        if (valor == null || valor.trim().isEmpty()) throw new IllegalArgumentException(nome + " é obrigatório.");
    }

    private void validarPermissaoLider(TimeEntity time) {
        if (!time.getOwner().equals(usuarioLogado)) throw new IllegalStateException("Requer permissão de líder.");
    }

    private void limparContexto() {
        this.timeSelecionado = null;
        this.quadroSelecionado = null;
        this.cardSelecionado = null;
        this.colunaSelecionada = null;
    }

    // --- Stats & Relatórios ---

    public String[] gerarEstatisticasDoQuadro() {
        validarQuadroSelecionado();
        List<String> stats = new ArrayList<>();
        stats.add("=== ESTATÍSTICAS: " + quadroSelecionado.getNome() + " ===");

        stats.add("\nPor Prioridade:");
        for (CardPriority p : CardPriority.values()) {
            stats.add(p + ": " + quadroSelecionado.getCards().stream().filter(c -> c.getPriority() == p).count());
        }

        stats.add("\nPor Coluna:");
        for (ColunaEntity col : quadroSelecionado.getColunas()) {
            stats.add(col.getName() + ": " + quadroSelecionado.getCards().stream().filter(c -> c.getStatus().equals(col.getName())).count());
        }

        return stats.toArray(new String[0]);
    }

    public String[] gerarRelatorioCompleto() {
        List<String> relatorio = new ArrayList<>();
        relatorio.add("=== RELATÓRIO DO SISTEMA ===");
        relatorio.add("Usuários: " + usuarios.size());
        relatorio.add("Times: " + times.size());
        relatorio.add("Convites Pendentes: " + convites.stream().filter(c -> c.getStatus().equals("PENDENTE")).count());

        if (usuarioLogado != null) {
            relatorio.add("Logado como: " + usuarioLogado.getUsername());
        }
        return relatorio.toArray(new String[0]);
    }

    // --- Getters de Cópia ---

    public List<UsuarioEntity> getUsuarios() { return new ArrayList<>(usuarios); }
    public List<TimeEntity> getTimes() { return new ArrayList<>(times); }
    public List<ConviteEntity> getConvites() { return new ArrayList<>(convites); }

    public boolean isUsuarioLogado() { return usuarioLogado != null; }
    public boolean isTimeSelecionado() { return timeSelecionado != null; }
    public boolean isQuadroSelecionado() { return quadroSelecionado != null; }
}