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

    public void mensagem(String msg) {
        System.out.println(">> " + msg);
    }

    public void opcao() {
        System.out.println("1 - Voltar ao Menu Principal");
        System.out.println("2 - Nova listagem");
        System.out.println();
        System.out.print("Digite a opcao: ");
        opcao = Input.lerString();
        controller.handleEvent(opcao);
    }

    @Override
    public void update() {
    }

}
