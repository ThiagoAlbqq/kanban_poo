package view;

import controller.ListarUsuariosController;
import models.KanbanModel;

public class ListarUsuariosView implements Observer {

    private String opcao = "0";

    private KanbanModel model;
    private ListarUsuariosController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new ListarUsuariosController();
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
