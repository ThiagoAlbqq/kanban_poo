package controller.usuario;

import models.KanbanModel;
import view.usuario.DeletarUsuarioView;
import view.Observer;

public class DeletarUsuarioController implements Observer {

    private KanbanModel model;
    private DeletarUsuarioView view;

    private int id;

    public void init(KanbanModel model, DeletarUsuarioView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void deletar() {
        view.solicitarId();

        try {
            this.id = view.getId();

            model.deletarUsuario(id);
            view.sucessMensage("Usuario deletado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}
}
