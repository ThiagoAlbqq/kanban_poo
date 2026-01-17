package controller.convite;

import models.KanbanModel;
import view.Observer;
import view.convite.ListarConviteView;

public class ListarConviteController implements Observer {

    private KanbanModel model;
    private ListarConviteView view;

    public void init(KanbanModel model, ListarConviteView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {
        try {
            String[] lista = model.verificarMeusConvites();
            for(String u : lista) {
                view.mensagem(u);
            }

            view.sucessMensage("Quadros listados com sucesso!\n");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}
}
