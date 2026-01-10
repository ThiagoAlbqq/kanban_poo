package view;

import controller.TelaInicialController;
import models.KanbanModel;

public class TelaInicialView implements Observer {

    private KanbanModel model;
    private TelaInicialController controller;

    public void init(KanbanModel model) {
        if(model == null) return;
        this.controller = new TelaInicialController();
        controller.init(model, this);
        this.model = model;
        model.attachObserver(this);
        mostrarMenu();
    }

    public void mensagem(String msg) {
        System.out.println(msg);
        System.out.println();
    }

    public void mostrarMenu() {
        boolean sair = false;

        do {
            Prompt.header("DANDANDAN-KANBAN");

            Prompt.menuItem("1", "Login");
            Prompt.menuItem("2", "Criar Conta");
            Prompt.separator();
            Prompt.menuItem("0", "Voltar / Sair");

            System.out.println();

            String op = Prompt.input("Escolha uma opção");

            if (op.equals("0")) {
                sair = true;
            } else {
                controller.handleEvent(op);
                System.out.println("\nPressione Enter para continuar...");
                Prompt.scanner.nextLine();
            }

        } while (!sair);
    }

    @Override
    public void update() {}
}