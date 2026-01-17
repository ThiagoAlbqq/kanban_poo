package view.coluna;

import controller.card.EditarCardController;
import controller.coluna.EditarColunaController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class EditarColunaView implements Observer {

    private int id;
    private String nome;

    private KanbanModel model;
    private EditarColunaController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new EditarColunaController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.editar();
        }
    }

    public void solicitarId() {
        id = Prompt.inputInt("ID");
    }

    public void solicitarNome() {
        Prompt.header("EDITAR COLUNA");
        nome = Prompt.input("Nome da coluna");
    }

    public String getNome() {
        return nome;
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
