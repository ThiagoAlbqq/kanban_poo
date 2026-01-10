package view;

import controller.ListarQuadroController;
import models.KanbanModel;

public class ListarQuadrosView implements Observer {

    private KanbanModel model;
    private ListarQuadroController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new ListarQuadroController();
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
