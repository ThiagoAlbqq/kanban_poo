package view.time;

import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class DashboardTimeView implements Observer {

    private KanbanModel model;
    private controller.time.DashboardTimeController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            // O Controller precisará de lógica para distinguir ID de Opção
            controller = new controller.time.DashboardTimeController();
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

            // Pega o nome do time selecionado de forma segura
            String nomeTime = (model.getTimeSelecionado() != null)
                    ? model.getTimeSelecionado().getName()
                    : "TIME";

            Prompt.header("TIME: " + nomeTime.toUpperCase());

            // AQUI É O PULO DO GATO: Listamos os quadros direto na home do time
            listarQuadrosDisponiveis();

            Prompt.separator();

            // Opções de gestão com Letras para não confundir com IDs
            System.out.println("[N] Novo Quadro | [RQ] Remover Quadro | [E] Editar Nome | [R] Remover Time | [C] Convidar Membro");
            System.out.println("[0] Voltar");

            System.out.println();
            // O input agora aceita ID (número) ou Opção (Letra)
            String op = Prompt.input("Digite o ID do quadro para entrar ou uma opção");

            if (op.equals("0")) {
                sair = true;
                controller.sair(); // Volta para o Dashboard
            } else {
                // O Controller deve verificar:
                // Se for número -> Entra no Quadro
                // Se for letra -> Executa ação (N, E, R)
                controller.handleEvent(op);
            }

        } while (!sair);
    }

    private void listarQuadrosDisponiveis() {
        // Supondo que o model tenha um método que retorne String[] formatada
        // Ex: "1 - Desenvolvimento (5 cards)"
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
        // O loop principal redesenha a tela, então aqui pode ficar vazio
        // ou conter apenas um refresh se você usar threads separadas.
    }
}