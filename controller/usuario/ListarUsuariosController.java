package controller.usuario;

import models.KanbanModel;
import view.usuario.ListarUsuariosView;
import view.Observer;

public class ListarUsuariosController implements Observer {

    private KanbanModel model;
    private ListarUsuariosView view;

    public void init(KanbanModel model, ListarUsuariosView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {

        try {
            String[] lista = model.listarUsuarios();
            for(String u : lista) {
                view.mensagem(u);
            }

            view.sucessMensage("Usuarios listados com sucesso!\n");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}
    
}
