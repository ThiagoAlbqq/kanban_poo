package view.time;

import controller.time.TelaTimeController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class TelaTimeView implements Observer {

    private KanbanModel model;
    private TelaTimeController controller;

    public void init(KanbanModel model) {
        this.model = model;
        this.controller = new TelaTimeController();
        controller.init(model, this);
        model.attachObserver(this);
        mostrarMenu();
    }

    public void mensagem(String msg) {
        System.out.println(">> " + msg);
    }

    public void mostrarMenu() {
        boolean sair = false;

        do {
            Prompt.clear();
            Prompt.header("MEUS TIMES");

            listarTimes();

            Prompt.separator();
            System.out.println("[C] Criar Novo Time");
            System.out.println("[0] Voltar");
            System.out.println();

            String op = Prompt.input("Digite o ID do Time para entrar ou uma opção");

            if (op.equals("0")) {
                sair = true;
                controller.sair();
            } else {
                controller.handleEvent(op);
            }

        } while (!sair);
    }

    private void listarTimes() {
        String[] times = model.listarMeusTimes();

        if (times == null || times.length == 0) {
            System.out.println("   (Você não está em nenhum time)");
        } else {
            for (String t : times) {
                System.out.println("   " + t);
            }
        }
    }

    @Override
    public void update() {
    }
}
