package view.card;

import controller.card.ListarCardController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class ListarCardView implements Observer {

    private int cardId;
    private KanbanModel model;
    private ListarCardController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new ListarCardController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.listar();
        }
    }

    public int getCardId() {return cardId;}

    public void solicitarId() {
        cardId = Prompt.inputInt("ID");
    }

    public void mensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void sucessMensage(String msg) {
        System.out.println(" ");
        Prompt.success(msg);
    }

    public void failMensage(String msg) {
        Prompt.error(msg);
    }

    @Override
    public void update() {
    }

}
