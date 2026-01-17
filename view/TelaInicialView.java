package view;

import controller.TelaInicialController;
import models.CancelarException;
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
            Prompt.clear();

            Prompt.header("DANDANDAN-KANBAN");

            Prompt.menuItem("1", "Login");
            Prompt.menuItem("2", "Criar Conta");
            Prompt.menuItem("3", "Gerar Relatorio Completo");
            Prompt.separator();
            Prompt.menuItem("0", "Voltar / Sair");

            System.out.println();

            try {
                String op = Prompt.input("Escolha uma opção");

                if (op.equals("0")) {
                    sair = true;
                } else {
                    controller.handleEvent(op);
                }

            } catch (CancelarException e) {
                // CORREÇÃO: Se digitar "sair", cai aqui e encerra o loop graciosamente
                Prompt.success("Saindo do sistema...");
                sair = true;
            }

        } while (!sair);
    }

    @Override
    public void update() {}
}