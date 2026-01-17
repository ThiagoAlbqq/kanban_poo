package controller.usuario;

import models.KanbanModel;
import view.usuario.EditarUsuarioView;
import view.Observer;

public class EditarUsuarioController implements Observer {

    private KanbanModel model;
    private EditarUsuarioView view;

    private String novoNome, novoEmail, novaSenha;

    public void init(KanbanModel model, EditarUsuarioView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void editar() {
        String[] usuarioAtual = model.getUsuarioLogadoString();

        view.message("--- EDITAR PERFIL (Deixe vazio para manter) ---");

        view.message("Nome atual: " + usuarioAtual[1]);
        view.solicitarNome();
        this.novoNome = view.getNome();

        view.message("Email atual: " + usuarioAtual[2]);
        view.solicitarEmail();
        this.novoEmail = view.getEmail();

        view.message("Senha (min 6 digitos):");
        view.solicitarSenha();

        while (!view.getSenha().isEmpty() && view.getSenha().length() < 6) {
            view.failMensage("A senha é muito curta! Tente novamente ou deixe vazio.");
            view.solicitarSenha();
        }
        this.novaSenha = view.getSenha(); // Usa a nova

        try {
            model.editarUsuario(novoNome, novoEmail, novaSenha);

            view.sucessMensage("Edição efetuada com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}
}