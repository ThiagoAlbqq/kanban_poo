package controller.time;

import models.KanbanModel;
import view.time.ListarTimesView;
import view.Observer;

public class ListarTimesController implements Observer {

    private KanbanModel model;
    private ListarTimesView view;

    public void init(KanbanModel model, ListarTimesView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {

        try {
            String[] lista = model.listarTimes();
            for(String u : lista) {
                view.mensagem(u);
            }

            view.sucessMensage("Times listados com sucesso!\n");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}


}
