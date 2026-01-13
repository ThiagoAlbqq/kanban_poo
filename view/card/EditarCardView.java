package view.card;

import controller.card.EditarCardController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class EditarCardView implements Observer {

    private int id;
    private String titulo, desc;
    private int prioOp;

    private KanbanModel model;
    private EditarCardController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new EditarCardController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.editar();
        }
    }

    public void solicitarId() {
        id = Prompt.inputInt("Id");
    }

    public void solicitarTitulo() {
        Prompt.header("EDITAR TAREFA");
        titulo = Prompt.input("Título");
    }

    public void solicitarDescricao() {
        desc = Prompt.input("Descrição");
    }

    public void solicitarPrioridade() {
        System.out.println("Prioridade: [1] Baixa | [2] Média | [3] Alta");
        prioOp = Prompt.inputInt("Opção");
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrioOp() {
        return prioOp;
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
