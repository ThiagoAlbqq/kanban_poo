package view.usuario;

import controller.usuario.DeletarUsuarioController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class DeletarUsuarioView implements Observer {

    private int id;
    private String opcao = "0";

    private KanbanModel model;
    private DeletarUsuarioController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new DeletarUsuarioController();
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