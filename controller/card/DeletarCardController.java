package controller.card;

import models.KanbanModel;
import view.Observer;
import view.card.DeletarCardView;

public class DeletarCardController implements Observer {

    private KanbanModel model;
    private DeletarCardView view;

    private int id;

    public void init(KanbanModel model, DeletarCardView view) {
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

            model.deletarCard(id);
            view.sucessMensage("Card deletado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
