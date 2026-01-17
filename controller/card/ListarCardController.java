package controller.card;

import models.KanbanModel;
import view.Observer;
import view.card.ListarCardView;

public class ListarCardController implements Observer {

    private int cardId;
    private KanbanModel model;
    private ListarCardView view;

    public void init(KanbanModel model, ListarCardView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {
        view.solicitarId();
        try {
            this.cardId = view.getCardId();
            model.selecionarCard(cardId);
            String[] card = model.listarCard();
            for(String u : card) {
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
