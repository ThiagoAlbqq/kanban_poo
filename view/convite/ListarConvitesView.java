package view.convite;

import controller.convite.ListarConviteController;
import controller.convite.ListarConvitesController;
import controller.quadro.ListarQuadroController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class ListarConvitesView implements Observer {

    private KanbanModel model;
    private ListarConvitesController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new ListarConvitesController();
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
