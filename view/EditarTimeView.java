package view;

import controller.EditarTimeController;
import controller.EditarUsuarioController;
import models.KanbanModel;

public class EditarTimeView implements Observer {

    private int id;
    private String novoNome;
    private String opcao = "0";

    private KanbanModel model;
    private EditarTimeController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new EditarTimeController();
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
