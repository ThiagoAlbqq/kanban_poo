package view.convite;

import controller.convite.ListarConviteController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class ListarConviteView implements Observer {

    private KanbanModel model;
    private ListarConviteController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new ListarConviteController();
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
