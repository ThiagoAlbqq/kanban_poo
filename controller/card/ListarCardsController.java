package controller.card;

import models.KanbanModel;
import view.Observer;
import view.card.DeletarCardView;

public class ListarCardsController implements Observer {

    private KanbanModel model;
    private DeletarCardView.ListarCardsView view;

    public void init(KanbanModel model, DeletarCardView.ListarCardsView view) {
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
