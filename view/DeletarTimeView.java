package view;

import controller.DeletarTimeController;
import controller.DeletarUsuarioController;
import models.KanbanModel;

public class DeletarTimeView implements Observer {

    private int id;

    private KanbanModel model;
    private DeletarTimeController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new DeletarTimeController();
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

}
