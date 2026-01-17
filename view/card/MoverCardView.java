package view.card;

import controller.card.MoverCardController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

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
            controller.moverCard();
        }
    }

    public void solicitarIdCard() {
        Prompt.header("MOVER CARD");
        idCard = Prompt.inputInt("Digite o ID do Card");
    }

    public void mostrarContextoDeMovimentacao(String tituloCard, String colunaAtual, String[] colunasDisponiveis) {
        System.out.println("");
        System.out.println("   » Card Selecionado: \u001B[33m" + tituloCard + "\u001B[0m");
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
    }

    public void failMensage(String msg) {
        System.out.println(" ");
        Prompt.error(msg);
    }

    @Override
    public void update() {
    }
}