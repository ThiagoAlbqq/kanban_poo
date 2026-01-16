package view;

import controller.quadro.TelaQuadroController;
import models.KanbanModel;

public class TelaQuadroView implements Observer {

    private KanbanModel model;
    private TelaQuadroController controller;

    public void init(KanbanModel model) {
        if(model != null){
            this.model = model;
            controller = new TelaQuadroController();
            controller.init(model, this);
            model.attachObserver(this);
            mostrarMenu();
        }
    }

    public void mensagem(String msg){
        System.out.println(">> " + msg);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    public void mostrarMenu() {
        boolean sair = false;

        do {
            Prompt.clear();

            String nomeQuadro = (model.getQuadroSelecionado() != null)
                    ? model.getQuadroSelecionado().getNome()
                    : "Quadro";

            Prompt.header("BOARD: " + nomeQuadro.toUpperCase());
            desenharBoard();
            Prompt.separator();

            System.out.println("[1] Novo Card | [2] Mover Card | [3] Nova Coluna");
            System.out.println("[0] Voltar");

            System.out.println();
            String op = Prompt.input("Escolha uma opção");

            if (op.equals("0")) {
                sair = true;
                controller.sair();
            } else {
                controller.handleEvent(op);
            }

        } while (!sair);
    }

    private void desenharBoard() {
        String[] visualizacao = model.listarCardsPorColuna();
        for (String linha : visualizacao) {
            if(linha.startsWith("---")) System.out.println("\u001B[36m" + linha + "\u001B[0m");
            else System.out.println(linha);
        }
    }

    @Override
    public void update() {
        // O loop cuida de atualizar a tela
    }
}