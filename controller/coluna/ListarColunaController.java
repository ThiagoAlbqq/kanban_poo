package controller.coluna;

import models.KanbanModel;
import view.Observer;
import view.coluna.ListarColunaView;

public class ListarColunaController implements Observer {

    private int id;
    private KanbanModel model;
    private ListarColunaView view;

    public void init(KanbanModel model, ListarColunaView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {
        try {
            view.solicitarId();
            this.id = view.getId();
            String[] lista = model.buscarColunaPorIdString(id);
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
