package controller;

import models.KanbanModel;
import view.ListarCardsView;
import view.Observer;

public class ListarCardsController implements Observer {

    private KanbanModel model;
    private ListarCardsView view;

    public void init(KanbanModel model, ListarCardsView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {
        try {
            String[] lista = model.listarCardsPorColuna();
            for(String u : lista) {
                view.mensagem(u);
            }

            view.sucessMensage("Cards listados com sucesso!\n");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
