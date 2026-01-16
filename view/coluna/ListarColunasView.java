package view.coluna;

import controller.coluna.ListarColunasController;

import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class ListarColunasView implements Observer {

    private KanbanModel model;
    private ListarColunasController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new ListarColunasController();
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
