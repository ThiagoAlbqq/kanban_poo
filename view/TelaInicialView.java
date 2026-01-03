package view;

import controller.TelaInicialController;
import models.KanbanModel;

public class TelaInicialView implements Observer {

    private KanbanModel model;
    private TelaInicialController controller;
    private boolean terminar = false;

    public void init(KanbanModel model) {
        if(model == null) return;
        this.controller = new TelaInicialController();
        controller.init(model, this);
        model.attachObserver(this);
        mostrarMenu();
    }

    public void mensagem(String msg) {
        System.out.println(msg);
        System.out.println();
    }

    public void mostrarMenu() {
        do {
            System.out.println("\n=== BEM-VINDO AO KANBAN 10 ===");
            System.out.println("1 - Criar Usuario");
            System.out.println("2 - Listar Usuarios");
            System.out.println("3 - Deletar Usuario");
            System.out.println("4 - Editar Usuario");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            controller.handleEvent(Input.lerString());
        } while (!terminar);
    }

    @Override
    public void update() {}
}