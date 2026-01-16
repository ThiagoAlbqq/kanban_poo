package view.card;

import controller.card.MoverCardController;
import models.KanbanModel;
import view.Observer;
import view.Prompt; // Seu utilitário

public class MoverCardView implements Observer {

    private int idCard, idColuna;
    private KanbanModel model;
    private MoverCardController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new MoverCardController();
            controller.init(model, this);
            model.attachObserver(this);
            // Inicia o fluxo
            controller.moverCard();
        }
    }

    public void solicitarIdCard() {
        Prompt.header("MOVER CARD");
        // Se o usuário digitar 'sair', o Prompt lança a Exception que o controller captura
        idCard = Prompt.inputInt("Digite o ID do Card");
    }

    // NOVO: Mostra o card selecionado e as opções de destino
    public void mostrarContextoDeMovimentacao(String tituloCard, String colunaAtual, String[] colunasDisponiveis) {
        System.out.println("");
        System.out.println("   » Card Selecionado: \u001B[33m" + tituloCard + "\u001B[0m"); // Amarelo
        System.out.println("   » Origem Atual: " + colunaAtual);
        System.out.println("");
        System.out.println("--- PARA QUAL COLUNA? ---");

        if (colunasDisponiveis != null) {
            for (String col : colunasDisponiveis) {
                System.out.println("   " + col);
            }
        }
        System.out.println("-------------------------");
    }

    public void solicitarIdColuna() {
        idColuna = Prompt.inputInt("Digite o ID da Coluna de Destino");
    }

    public int getIdCard() { return idCard; }
    public int getIdColuna() { return idColuna; }

    public void sucessMensage(String msg) {
        System.out.println(" ");
        Prompt.success(msg);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    public void failMensage(String msg) {
        System.out.println(" ");
        Prompt.error(msg);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @Override
    public void update() {
    }
}