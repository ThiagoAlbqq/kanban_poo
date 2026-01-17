package view.time;

import controller.time.DashboardTimeController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class DashboardTimeView implements Observer {

    private KanbanModel model;
    private DashboardTimeController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new DashboardTimeController();
            controller.init(model, this);
            model.attachObserver(this);
            mostrarMenu();
        }
    }

    public void mensagem(String msg) {
        System.out.println(">> " + msg);
    }

    public void mostrarMenu() {
        boolean sair = false;

        do {
            if (model.getTimeSelecionado() == null) {
                sair = true;
                continue;
            }

            Prompt.clear();

            String nomeTime = (model.getTimeSelecionado() != null)
                    ? model.getTimeSelecionado().getName()
                    : "TIME";

            Prompt.header("TIME: " + nomeTime.toUpperCase());

            listarQuadrosDisponiveis();

            Prompt.separator();

            System.out.println("[N] Novo Quadro | [E] Editar Nome | [R] Remover Time | [C] Convidar Membro");
            System.out.println("[0] Voltar");

            System.out.println();
            String op = Prompt.input("Digite o ID do quadro para entrar ou uma opção");

            if (op.equals("0")) {
                sair = true;
                controller.sair();
            } else {
                controller.handleEvent(op);
            }

        } while (!sair);
    }

    private void listarQuadrosDisponiveis() {
        String[] quadros = model.listarQuadros();

        if (quadros == null || quadros.length == 0) {
            System.out.println("   (Nenhum quadro criado neste time ainda)");
        } else {
            System.out.println("--- QUADROS DISPONÍVEIS ---");
            for (String q : quadros) {
                System.out.println("   " + q);
            }
        }
    }

    @Override
    public void update() {
    }
}