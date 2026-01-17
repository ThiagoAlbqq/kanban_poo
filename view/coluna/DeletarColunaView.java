package view.coluna;

import controller.coluna.DeletarColunaController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class DeletarColunaView implements Observer {

    private int id;

    private KanbanModel model;
    private DeletarColunaController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new DeletarColunaController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.deletar();
        }
    }

    public void solicitarId() {
        id = Prompt.inputInt("ID");
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

}
