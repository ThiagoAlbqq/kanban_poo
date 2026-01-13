package controller.usuario;

import models.KanbanModel;
import view.usuario.EditarUsuarioView;
import view.Observer;

public class EditarUsuarioController implements Observer {

    private KanbanModel model;
    private EditarUsuarioView view;

    private int id;
    private String novoNome, novoEmail, novaSenha;

    public void init(KanbanModel model, EditarUsuarioView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void editar() {
        view.solicitarId();
        view.solicitarNome();
        view.solicitarEmail();
        view.solicitarSenha();

        while (!view.getSenha().isEmpty() && view.getSenha().length() < 6) {
            view.failMensage("Se for alterar a senha, ela deve ter no minimo 6 digitos!");
            view.solicitarSenha();
        }

        try {
            this.id = view.getId();
            this.novoNome = view.getNome();
            this.novoEmail = view.getEmail();
            this.novaSenha = view.getSenha();

            model.editarUsuario(id, novoNome, novoEmail, novaSenha);

            view.sucessMensage("Edição efetuada com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}
}