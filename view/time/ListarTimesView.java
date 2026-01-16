package view.time;

import controller.time.ListarTimesController;
import controller.time.TelaTimeController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class ListarTimesView implements Observer {

    private KanbanModel model;
    private ListarTimesController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new ListarTimesController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.listar();
        }
    }

    public void mensagem(String mensagem) {
        System.out.println(mensagem);
    }

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

    public static class TelaTimeView implements Observer {

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
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }

        public void mostrarMenu() {
            boolean sair = false;

            do {
                Prompt.clear();
                Prompt.header("MEUS TIMES");

                // LISTA OS TIMES PARA O USUÁRIO ESCOLHER
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
            // Seu model deve retornar algo como ["1 - Time A", "2 - Time B"]
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
            // Atualiza caso a lista mude
        }
    }
}
