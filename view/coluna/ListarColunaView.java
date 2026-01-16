package view.coluna;

import controller.coluna.ListarColunaController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class ListarColunaView implements Observer {

    private int id;
    private KanbanModel model;
    private ListarColunaController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new ListarColunaController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.listar();
        }
    }

    public int getId() {return id;}

    public void solicitarId() {
        id = Prompt.inputInt("ID");
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
