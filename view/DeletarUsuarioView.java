package view;

import controller.DeletarUsuarioController;
import models.KanbanModel;


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
        System.out.print("Id: ");
        id = Input.lerInt();
    }

    public int getId() { return id; }

    public void mensagem(String msg) {
        System.out.println(">> " + msg);
        System.out.println();
    }

    public void opcao() {
        System.out.println("1 - Voltar ao Menu Principal");
        System.out.println("2 - Nova remoção3");
        System.out.println();
        System.out.print("Digite a opcao: ");
        opcao = Input.lerString();
        controller.handleEvent(opcao);
    }

    @Override
    public void update() {
    }

}