package view;

import controller.EditarQuadroController;
import controller.EditarTimeController;
import models.KanbanModel;

public class EditarQuadroView implements Observer {

    private int id;
    private String novoNome;

    private KanbanModel model;
    private EditarQuadroController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new EditarQuadroController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.editar();
        }
    }

    public void solicitarId() {
        id = Prompt.inputInt("Id");
    }

    public void solicitarNome() {
        novoNome = Prompt.input("Nome");
    }

    public int getId() { return id; }
    public String getNome() { return novoNome; }

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
