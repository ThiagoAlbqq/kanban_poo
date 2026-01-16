package view.card;

import controller.card.DeletarCardController;
import controller.card.ListarCardsController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class DeletarCardView implements Observer {

    private int id;

    private KanbanModel model;
    private DeletarCardController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new DeletarCardController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.deletar();
        }
    }

    public void solicitarId() {
        id = Prompt.inputInt("Id");
    }

    public int getId() { return id; }

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

    public static class ListarCardsView implements Observer {

        private KanbanModel model;
        private ListarCardsController controller;

        public void init(KanbanModel model) {
            if (model != null) {
                this.model = model;
                controller = new ListarCardsController();
                controller.init(model, this);
                model.attachObserver(this);

                controller.listar();
            }
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
}
