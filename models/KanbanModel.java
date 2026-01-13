package models;

import view.Observer;

import javax.smartcardio.Card;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KanbanModel implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- ATRIBUTOS DE ESTADO ---
    private static KanbanModel instanciaUnica;
    private static final String NOME_ARQUIVO = "kanban_db.ser";

    // Listas de Dados (O "Banco de Dados")
    private List<UsuarioEntity> usuarios;
    private List<ConviteEntity> convites;
    private List<TeamEntity> times;

    // Estado da Sessão Atual (Quem está logado e onde ele está navegando)
    private UsuarioEntity usuarioLogado;
    private TeamEntity timeSelecionado;    // Qual time está aberto?
    private BoardEntity quadroSelecionado; // Qual quadro está aberto?
    private CardEntity cardSelecionado;

    // Lista de observadores (transient = não salva no arquivo)
    private transient ArrayList<Observer> observers;

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
            instanciaUnica = carregarDados(); // Tenta carregar do disco

            if (instanciaUnica == null) {
                instanciaUnica = new KanbanModel(); // Se não existir, cria novo
                // Opcional: Criar admin padrão aqui se quiser
            }
        }
        return instanciaUnica;
    }

    // ==================================================================================
    // 2. PADRÃO OBSERVER
    // ==================================================================================

    public void attachObserver(Observer observer) {
        if (this.observers == null) {
            this.observers = new ArrayList<>();
        }
        if (observer != null) {
            observers.add(observer);
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
            // System.out.println("[Sistema] Dados salvos."); // Debug
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    private static KanbanModel carregarDados() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            KanbanModel model = (KanbanModel) in.readObject();
            model.observers = new ArrayList<>(); // Recria lista transient
            return model;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    // ==================================================================================
    // 4. MÓDULO DE USUÁRIOS (CRUD & AUTENTICAÇÃO)
    // ==================================================================================

    public void autenticarUsuario(String email, String senha){
        for (UsuarioEntity u : usuarios) {
            if (u.getEmail().equals(email) && u.checkPassword(senha)) {
                this.usuarioLogado = u;
                notifica();
                return;
            }
        }
        throw new IllegalStateException("Usuário ou senha inválidos.");
    }

    public void deslogarUsuario() {
        this.usuarioLogado = null;
        this.timeSelecionado = null;   // Limpa contexto
        this.quadroSelecionado = null; // Limpa contexto
        notifica();
    }

    public UsuarioEntity getUsuarioLogado() {
        return usuarioLogado;
    }

    public void adicionarUsuario(String nome, String email, String senha) {
        for (UsuarioEntity u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new RuntimeException("Já existe um usuário com o email informado.");
            }
        }

        if (nome == null || nome.isEmpty()) throw new RuntimeException("Nome obrigatório.");
        if (senha == null || senha.length() < 6) throw new RuntimeException("Senha deve ter no mínimo 6 caracteres.");

        // Gera ID simples (size + 1 não é perfeito pra deletar, mas serve pro trabalho)
        // Se quiser ID robusto, use um contador incremental salvo no model
        UsuarioEntity novo = new UsuarioEntity(nome, email, senha);
        usuarios.add(novo);
        salvarDados();
        notifica();
    }

    public void deletarUsuario(int id) {
        if (id <= 0) throw new RuntimeException("ID inválido.");

        UsuarioEntity alvo = null;
        for (UsuarioEntity u : usuarios) {
            if (u.getId() == id) {
                alvo = u;
                break;
            }
        }

        if (alvo == null) throw new RuntimeException("Usuário não encontrado.");
        if (usuarioLogado != null && usuarioLogado.getId() == id) {
            throw new RuntimeException("Não pode deletar o próprio usuário logado.");
        }

        usuarios.remove(alvo);
        salvarDados();
        notifica();
    }

    public void editarUsuario(int id, String novoNome, String novoEmail, String novaSenha) {
        UsuarioEntity alvo = null;
        for (UsuarioEntity u : usuarios) {
            if (u.getId() == id) {
                alvo = u;
                break;
            }
        }

        if (alvo == null) throw new RuntimeException("Usuário não encontrado.");

        if (novoEmail != null && !novoEmail.isEmpty() && !novoEmail.equals(alvo.getEmail())) {
            for (UsuarioEntity u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(novoEmail) && u.getId() != id) {
                    throw new RuntimeException("Email já em uso.");
                }
            }
            alvo.setEmail(novoEmail);
        }

        if (novoNome != null && !novoNome.isEmpty()) alvo.setUsername(novoNome);

        if (novaSenha != null && !novaSenha.isEmpty()) {
            if (novaSenha.length() < 6) throw new RuntimeException("Senha muito curta.");
            alvo.setPassword(novaSenha);
        }

        salvarDados();
        notifica();
    }

    public String[] listarUsuarios() {
        if (usuarios == null || usuarios.isEmpty()) return new String[0];

        usuarios.sort((u1, u2) -> Integer.compare(u1.getId(), u2.getId()));
        String[] listaFormatada = new String[usuarios.size()];

        for (int i = 0; i < usuarios.size(); i++) {
            UsuarioEntity u = usuarios.get(i);
            listaFormatada[i] = String.format("%d - %s (%s)", u.getId(), u.getUsername(), u.getEmail());
        }
        return listaFormatada;
    }

    public String[] consultarUsuario(int id) {
        for (UsuarioEntity u : usuarios) {
            if (u.getId() == id) {
                return new String[]{
                        "Nome: " + u.getUsername(),
                        "Email: " + u.getEmail(),
                        "ID: " + u.getId()
                };
            }
        }
        throw new RuntimeException("Usuário não encontrado.");
    }

    // ==================================================================================
    // 5. MÓDULO DE TIMES (CRUD & CONVITES)
    // ==================================================================================

    // --- Contexto de Time ---
    public void selecionarTime(int idTime) {
        TeamEntity timeEncontrado = null;
        for (TeamEntity t : times) {
            if (t.getId() == idTime) {
                timeEncontrado = t;
                break;
            }
        }

        if (timeEncontrado == null) throw new RuntimeException("Time não encontrado.");

        boolean isMembro = false;
        for(UsuarioEntity u : timeEncontrado.getMembers()) {
            if(u.getId() == usuarioLogado.getId()) {
                isMembro = true;
                break;
            }
        }

        if (!isMembro) throw new RuntimeException("Você não é membro deste time.");

        this.timeSelecionado = timeEncontrado;
        // Ao mudar de time, reseta o quadro selecionado para evitar inconsistência
        this.quadroSelecionado = null;
        this.cardSelecionado = null;
    }

    public TeamEntity getTimeSelecionado() {
        return timeSelecionado;
    }

    // --- CRUD ---

    public void criarTime(String nome) {
        if (nome == null || nome.isEmpty()) throw new RuntimeException("Nome obrigatório.");
        if (usuarioLogado == null) throw new RuntimeException("Ninguém logado.");

        // Novo ID (Simples)
        int novoId = times.size() + 1;
        TeamEntity novoTime = new TeamEntity(nome, "", usuarioLogado);

        novoTime.addMember(usuarioLogado); // Criador já é membro

        times.add(novoTime);
        salvarDados();
        notifica();
    }

    // --- EDITAR E DELETAR TIMES (Recuperados) ---

    public void editarTime(int id, String novoNome) {
        if (usuarioLogado == null) throw new RuntimeException("Ninguém logado.");
        if (novoNome == null || novoNome.isEmpty()) throw new RuntimeException("Nome vazio.");

        TeamEntity alvo = null;
        for (TeamEntity t : times) {
            if (t.getId() == id) {
                alvo = t;
                break;
            }
        }

        if (alvo == null) throw new RuntimeException("Time não encontrado.");

        // Validação de Segurança: Só o dono (criador) pode editar?
        if (!alvo.getOwner().getEmail().equals(usuarioLogado.getEmail())) {
            throw new RuntimeException("Apenas o líder pode editar o time.");
        }

        alvo.setName(novoNome);
        salvarDados();
        notifica();
    }

    public void deletarTime(int id) {
        if (usuarioLogado == null) throw new RuntimeException("Ninguém logado.");
        if (id <= 0) throw new RuntimeException("ID inválido.");

        TeamEntity alvo = null;
        for (TeamEntity t : times) {
            if (t.getId() == id) {
                alvo = t;
                break;
            }
        }

        if (alvo == null) throw new RuntimeException("Time não encontrado.");

        // Validação de Segurança:
        if (!alvo.getOwner().getEmail().equals(usuarioLogado.getEmail())) {
            throw new RuntimeException("Apenas o líder pode deletar o time.");
        }

        times.remove(alvo);

        // Se deletou o time que estava aberto, limpa a seleção
        if (timeSelecionado != null && timeSelecionado.getId() == id) {
            timeSelecionado = null;
            quadroSelecionado = null;
            cardSelecionado = null;
        }

        salvarDados();
        notifica();
    }

    public String[] listarMeusTimes() {
        if (usuarioLogado == null) return new String[0];

        ArrayList<String> meusTimes = new ArrayList<>();
        for (TeamEntity t : times) {
            boolean isMembro = false;
            for(UsuarioEntity membro : t.getMembers()) {
                if(membro.getId() == usuarioLogado.getId()) {
                    isMembro = true;
                    break;
                }
            }

            if (isMembro) {
                meusTimes.add(String.format("[%d] %s (Membros: %d)", t.getId(), t.getName(), t.getMembers().size()));
            }
        }
        return meusTimes.toArray(new String[0]);
    }

    // Lista TODOS os times (para Admin ou Debug)
    public String[] listarTimes() {
        String[] lista = new String[times.size()];
        for (int i = 0; i < times.size(); i++) {
            TeamEntity t = times.get(i);
            lista[i] = String.format("ID: %d | Time: %s | Membros: %d", t.getId(), t.getName(), t.getMembers().size());
        }
        return lista;
    }

    // --- Convites ---

    public void enviarConvite(String emailDestinatario, int idTime) {
        if (usuarioLogado == null) throw new RuntimeException("Ninguém logado.");

        TeamEntity time = null;
        for(TeamEntity t : times) {
            if(t.getId() == idTime) time = t;
        }
        if(time == null) throw new RuntimeException("Time não encontrado.");

        // Verifica se usuário existe
        boolean usuarioExiste = false;
        for(UsuarioEntity u : usuarios) {
            if(u.getEmail().equalsIgnoreCase(emailDestinatario)) usuarioExiste = true;
        }
        if(!usuarioExiste) throw new RuntimeException("Usuário não encontrado.");

        int idConvite = convites.size() + 1;
        ConviteEntity convite = new ConviteEntity(
                idConvite, usuarioLogado.getEmail(), emailDestinatario, idTime, time.getName()
        );

        convites.add(convite);
        salvarDados();
        notifica();
    }

    public String[] verificarMeusConvites() {
        if (usuarioLogado == null) return new String[0];

        List<String> meusConvites = new ArrayList<>();
        for (ConviteEntity c : convites) {
            if (c.getDestinatarioEmail().equalsIgnoreCase(usuarioLogado.getEmail())
                    && c.getStatus().equals("PENDENTE")) {
                meusConvites.add(c.getId() + "#" + c.getNomeTime() + "#" + c.getRemetenteEmail());
            }
        }
        return meusConvites.toArray(new String[0]);
    }

    public void responderConvite(int idConvite, boolean aceitou) {
        ConviteEntity alvo = null;
        for(ConviteEntity c : convites) {
            if(c.getId() == idConvite) alvo = c;
        }

        if(alvo == null) throw new RuntimeException("Convite não encontrado.");

        if(aceitou) {
            alvo.setStatus("ACEITO");
            entrarNoTime(alvo.getIdTime());
        } else {
            alvo.setStatus("RECUSADO");
        }
        salvarDados();
        notifica();
    }

    public void entrarNoTime(int idTime) {
        TeamEntity timeAlvo = null;
        for(TeamEntity t : times) {
            if(t.getId() == idTime) timeAlvo = t;
        }
        if (timeAlvo == null) throw new RuntimeException("Time não encontrado.");

        if (timeAlvo.getMembers().contains(usuarioLogado)) {
            // throw new RuntimeException("Você já está neste time."); // Opcional lançar erro
            return;
        }

        timeAlvo.addMember(usuarioLogado);
        salvarDados();
        notifica();
    }

    // ==================================================================================
    // 6. MÓDULO DE QUADROS (BOARDS)
    // ==================================================================================

    // --- Contexto de Quadro ---

    // Método que faltava para a tela de Cards funcionar!
    public void selecionarQuadro(int idQuadro) {
        if (timeSelecionado == null) throw new RuntimeException("Nenhum time selecionado.");

        BoardEntity alvo = null;
        for (BoardEntity b : timeSelecionado.getBoards()) {
            if (b.getId() == idQuadro) {
                alvo = b;
                break;
            }
        }

        if (alvo == null) throw new RuntimeException("Quadro não encontrado.");
        this.quadroSelecionado = alvo;
    }

    public BoardEntity getQuadroSelecionado() {
        return quadroSelecionado;
    }

    // --- CRUD ---

    public void criarQuadro(String nome) {
        if (this.timeSelecionado == null) throw new RuntimeException("Selecione um time antes.");
        if (nome == null || nome.isEmpty()) throw new RuntimeException("Nome obrigatório.");

        // Gera ID para o quadro
        int novoId = timeSelecionado.getBoards().size() + 1;

        // ATENÇÃO: Verifique se o construtor do BoardEntity aceita (id, nome) ou só (nome)
        // Estou usando o padrão que combinamos antes:
        BoardEntity novoQuadro = new BoardEntity(novoId, nome);

        timeSelecionado.addBoard(novoQuadro);
        salvarDados();
        notifica();
    }

    public void deletarQuadro(int id) {
        if (this.timeSelecionado == null) throw new RuntimeException("Selecione um time antes.");

        BoardEntity alvo = null;
        for (BoardEntity b : timeSelecionado.getBoards()) {
            if (b.getId() == id) {
                alvo = b;
                break;
            }
        }

        if (alvo == null) throw new RuntimeException("Quadro não encontrado.");

        timeSelecionado.getBoards().remove(alvo);

        // Se deletou o quadro que estava aberto, fecha ele
        if(quadroSelecionado != null && quadroSelecionado.getId() == id) {
            quadroSelecionado = null;
            cardSelecionado = null;
        }

        salvarDados();
        notifica();
    }

    public void editarQuadro(int id, String novoNome) {
        if (this.timeSelecionado == null) throw new RuntimeException("Selecione um time antes.");

        BoardEntity alvo = null;
        for (BoardEntity b : timeSelecionado.getBoards()) {
            if (b.getId() == id) {
                alvo = b;
                break;
            }
        }

        if (alvo == null) throw new RuntimeException("Quadro não encontrado.");
        if (novoNome == null || novoNome.isEmpty()) throw new RuntimeException("Novo nome vazio.");

        alvo.setNome(novoNome);
        salvarDados();
        notifica();
    }

    public String[] listarQuadros() {
        if (this.timeSelecionado == null) return new String[0];

        List<BoardEntity> quadros = timeSelecionado.getBoards();

        // Ordena por ID
        // quadros.sort((b1, b2) -> Integer.compare(b1.getId(), b2.getId()));

        String[] lista = new String[quadros.size()];
        for(int i=0; i<quadros.size(); i++) {
            BoardEntity b = quadros.get(i);
            lista[i] = String.format("%d - %s", b.getId(), b.getNome());
        }
        return lista;
    }

    public String[] consultarQuadro(int id) {
        if (this.timeSelecionado == null) throw new RuntimeException("Selecione um time antes.");

        for (BoardEntity b : timeSelecionado.getBoards()) {
            if (b.getId() == id) {
                return new String[]{
                        "Nome: " + b.getNome(),
                        "ID: " + b.getId()
                };
            }
        }
        throw new RuntimeException("Quadro não encontrado.");
    }

    // ==================================================================================
    // 7. MÓDULO DE CARDS (TAREFAS)
    // ==================================================================================

    // Método auxiliar privado pra não repetir código no listarCards
    private void adicionarColuna(List<String> relatorio, String titulo, String statusBusca) {
        relatorio.add("--- " + titulo + " ---");
        boolean temItem = false;

        for(CardEntity c : quadroSelecionado.getCards()) {
            if(statusBusca.equals(c.getStatus())) {
                relatorio.add(c.toString());
                temItem = true;
            }
        }

        if(!temItem) relatorio.add("  (Vazio)");
        relatorio.add(""); // Linha em branco pra separar visualmente
    }

    // --- NOVO: CRIAR COLUNA PERSONALIZADA ---
    public void criarColuna(String nomeColuna) {
        if (quadroSelecionado == null) throw new RuntimeException("Nenhum quadro aberto.");
        if (nomeColuna == null || nomeColuna.isEmpty()) throw new RuntimeException("Nome vazio.");

        quadroSelecionado.adicionarColuna(nomeColuna.toUpperCase());
        salvarDados();
        notifica();
    }

    public void selecionarCard(int idCard) {
        if (quadroSelecionado == null) throw new RuntimeException("Nenhum quadro selecionado.");

        CardEntity alvo = null;
        for (CardEntity c : quadroSelecionado.getCards()) {
            if (c.getId() == idCard) {
                alvo = c;
                break;
            }
        }

        if (alvo == null) throw new RuntimeException("Card não encontrado.");
        this.cardSelecionado = alvo;
    }

    // --- ATUALIZADO: CRIAR CARD (Vai para a primeira coluna sempre) ---
    public void criarCard(String titulo, String descricao, CardPriority prioridade) {
        if (quadroSelecionado == null) throw new RuntimeException("Nenhum quadro aberto.");

        // Pega o nome da primeira coluna (Ex: "A FAZER")
        String primeiraColuna = quadroSelecionado.getColunas().get(0);

        int novoId = quadroSelecionado.getCards().size() + 1;

        // Cria o card com o status igual ao nome da primeira coluna
        CardEntity novo = new CardEntity(novoId, titulo, descricao, prioridade, primeiraColuna);
        novo.setAssignee(usuarioLogado);

        quadroSelecionado.addCard(novo);
        salvarDados();
        notifica();
    }

    public void editarCard(int id, String titulo, String descricao, CardPriority prioridade) {
        selecionarCard(id);
        if (cardSelecionado == null) throw new RuntimeException("Nenhum card aberto.");

        CardEntity alvo = cardSelecionado;

        if (!(titulo == null || titulo.isEmpty())) {
            alvo.setTitle(titulo);
        };

        if (!(descricao == null || descricao.isEmpty())) {
            alvo.setDescription(descricao);
        }

        if (prioridade != null) {
            alvo.setPriority(prioridade);
        }

        salvarDados();
        notifica();
    }


    // --- ATUALIZADO: MOVER CARD (Dinâmico) ---
    public void moverCard(int idCard) {
        if (quadroSelecionado == null) throw new RuntimeException("Nenhum quadro aberto.");

        CardEntity alvo = null;
        for(CardEntity c : quadroSelecionado.getCards()) {
            if(c.getId() == idCard) alvo = c;
        }
        if(alvo == null) throw new RuntimeException("Card não encontrado.");

        // 1. Descobre em qual coluna o card está agora
        List<String> colunas = quadroSelecionado.getColunas();
        int indexAtual = colunas.indexOf(alvo.getStatus());

        // 2. Calcula qual é a próxima
        if (indexAtual == -1) {
            // Se der erro (ex: a coluna foi deletada), joga pra primeira
            alvo.setStatus(colunas.get(0));
        } else if (indexAtual < colunas.size() - 1) {
            // Move para a próxima da lista
            alvo.setStatus(colunas.get(indexAtual + 1));
        } else {
            throw new RuntimeException("O card já está na última etapa!");
        }

        salvarDados();
        notifica();
    }

    // --- ATUALIZADO: LISTAR (Loop nas colunas) ---
    public String[] listarCardsPorColuna() {
        if (quadroSelecionado == null) return new String[0];

        List<String> relatorio = new ArrayList<>();

        // Para CADA coluna que existe no Board...
        for (String nomeColuna : quadroSelecionado.getColunas()) {

            relatorio.add("--- " + nomeColuna + " ---");
            boolean temItem = false;

            // Procura cards que tenham esse status
            for(CardEntity c : quadroSelecionado.getCards()) {
                if (c.getStatus().equals(nomeColuna)) {
                    relatorio.add(c.toString());
                    temItem = true;
                }
            }

            if(!temItem) relatorio.add("  (Vazio)");
            relatorio.add(""); // Pula linha
        }

        return relatorio.toArray(new String[0]);
    }

}