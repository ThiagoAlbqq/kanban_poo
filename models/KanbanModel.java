package models;

import view.Observer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KanbanModel implements Serializable {

    private static KanbanModel instanciaUnica;

    private List<UsuarioEntity> usuarios = new ArrayList<>();
    private List<ConviteEntity> convites;
    private List<TeamEntity> times;
    private UsuarioEntity usuarioLogado;
    private static final String NOME_ARQUIVO = "kanban_db.ser";

    private transient ArrayList<Observer> observers;

    private KanbanModel() {
        this.usuarios = new ArrayList<>();
        this.times = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.convites = new ArrayList<>();
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

    public void salvarDados() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            out.writeObject(this);
            System.out.println("[Sistema] Dados salvos.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    private static KanbanModel carregarDados() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            KanbanModel model = (KanbanModel) in.readObject();
            model.observers = new ArrayList<>();
            return model;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public void adicionarUsuario(String nome, String email, String senha) {
        for (UsuarioEntity u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new RuntimeException("Já existe um usuário com o email informado.");
            }
        }

        if (nome == null || nome.isEmpty()) throw new RuntimeException("Nome obrigatório.");
        if (senha == null || senha.length() < 6) throw new RuntimeException("Senha deve ter no mínimo 6 caracteres.");

        UsuarioEntity novo = new UsuarioEntity(nome, email, senha);
        usuarios.add(novo);
        salvarDados();

        notifica();
    }

    public void deletarUsuario(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido.");
        }

        UsuarioEntity alvo = null;

        for (UsuarioEntity u : usuarios) {
            if (u.getId() == id) {
                alvo = u;
                break;
            }
        }

        if (alvo == null) {
            throw new RuntimeException("Usuário não encontrado com o ID informado.");
        }

        if (usuarioLogado != null && usuarioLogado.getId() == id) {
            throw new RuntimeException("Você não pode deletar seu próprio usuário enquanto está logado.");
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

        if (alvo == null) {
            throw new RuntimeException("Usuário não encontrado para edição.");
        }

        if (novoEmail != null && !novoEmail.isEmpty() && !novoEmail.equals(alvo.getEmail())) {
            for (UsuarioEntity u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(novoEmail) && u.getId() != id) {
                    throw new RuntimeException("Este novo email já está em uso por outro usuário.");
                }
            }
            alvo.setEmail(novoEmail);
        }

        if (novoNome != null && !novoNome.isEmpty()) {
            alvo.setUsername(novoNome);
        }

        if (novaSenha != null && !novaSenha.isEmpty()) {
            if (novaSenha.length() < 6) throw new RuntimeException("Nova senha muito curta.");
            alvo.setPassword(novaSenha);
        }

        salvarDados();
        notifica();
    }

    public String[] listarUsuarios() {

        if (usuarios == null || usuarios.isEmpty()) {
            return new String[0];
        }

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
                String[] dados = new String[3];
                dados[0] = "Nome: " + u.getUsername();
                dados[1] = "Email: " + u.getEmail();
                dados[2] = "ID: " + u.getId();
                return dados;
            }
        }
        throw new RuntimeException("Usuário não encontrado.");
    }

    public void autenticarUsuario(String email, String senha){
        for (UsuarioEntity u : usuarios) {
            if (u.getEmail().equals(email) && u.checkPassword(senha)) {
                this.usuarioLogado = u;
                notifica();
                return;
            }
        }
        throw new IllegalStateException("Houve um erro ao efetuar o login");
    }

    public void deslogarUsuario() {
        this.usuarioLogado = null;
    }

    public UsuarioEntity getUsuarioLogado() {
        return usuarioLogado;
    }

    public void criarTime(String nome) {
        if (nome == null || nome.isEmpty()) throw new RuntimeException("Nome do time obrigatório.");

        int novoId = times.size() + 1;

        TeamEntity novoTime = new TeamEntity(nome, "", usuarioLogado);

        if (usuarioLogado != null) {
            novoTime.addMember(usuarioLogado);
        }

        times.add(novoTime);
        salvarDados();
        notifica();
    }

    public String[] listarTimes() {
        String[] lista = new String[times.size()];
        for (int i = 0; i < times.size(); i++) {
            TeamEntity t = times.get(i);
            lista[i] = String.format("ID: %d | Time: %s | Membros: %d", t.getId(), t.getName(), t.getMembers().size());
        }
        return lista;
    }

    public void editarTime(int id, String novoNome) {

        TeamEntity alvo = null;
        for (TeamEntity t : times) {
            if (t.getId() == id) {
                alvo = t;
                break;
            }
        }

        if (alvo == null) {
            throw new RuntimeException("Time não encontrado para edição.");
        }

        if (novoNome != null && !novoNome.isEmpty()) {
            alvo.setName(novoNome);
        }

        salvarDados();
        notifica();

    }

    // Função para deltar time dado o ID (para lideres e admins)
    public void deletarTime(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido.");
        }

        TeamEntity alvo = null;

        // TODO[]: para casos onde o cara não é admin, precisamos validar se ele é Lider da equipe
        for (TeamEntity t : times) {
            if (t.getId() == id) {
                alvo = t;
                break;
            }
        }

        if (alvo == null) {
            throw new RuntimeException("Time não encontrado com o ID informado.");
        }

        times.remove(alvo);
        salvarDados();
        notifica();
    }

    public void entrarNoTime(int idTime) {
        if (usuarioLogado == null) throw new RuntimeException("Ninguém logado.");

        TeamEntity timeAlvo = null;
        for(TeamEntity t : times) {
            if(t.getId() == idTime) timeAlvo = t;
        }

        if (timeAlvo == null) throw new RuntimeException("Time não encontrado.");

        if (timeAlvo.getMembers().contains(usuarioLogado)) {
            throw new RuntimeException("Você já está neste time.");
        }

        timeAlvo.addMember(usuarioLogado);
        salvarDados();
        notifica();
    }

    public void enviarConvite(String emailDestinatario, int idTime) {
        if (usuarioLogado == null) throw new RuntimeException("Ninguém logado.");

        TeamEntity time = null;
        for(TeamEntity t : times) {
            if(t.getId() == idTime) time = t;
        }
        if(time == null) throw new RuntimeException("Time não encontrado.");

        boolean usuarioExiste = false;
        for(UsuarioEntity u : usuarios) {
            if(u.getEmail().equalsIgnoreCase(emailDestinatario)) usuarioExiste = true;
        }
        if(!usuarioExiste) throw new RuntimeException("Usuário não encontrado.");

        int idConvite = convites.size() + 1;
        ConviteEntity convite = new ConviteEntity(
                idConvite,
                usuarioLogado.getEmail(),
                emailDestinatario,
                idTime,
                time.getName()
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
}