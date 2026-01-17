package view.quadro;

import controller.quadro.TelaQuadroController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

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
    }

    public void mostrarMenu() {
        boolean sair = false;

        do {
            if (model.getQuadroSelecionado() == null) {
                sair = true;
                continue;
            }

            Prompt.clear();

            String nomeQuadro = (model.getQuadroSelecionado() != null)
                    ? model.getQuadroSelecionado().getNome()
                    : "Quadro";

            Prompt.header("BOARD: " + nomeQuadro.toUpperCase());
            desenharBoard();
            Prompt.separator();

            System.out.println(" -- TAREFAS --");
            System.out.println(" [1] Novo Card    | [2] Mover Card | [3] Editar Card  | [4] Excluir Card\n");

            System.out.println(" -- ESTRUTURA --");
            System.out.println(" [5] Nova Coluna  | [6] Excluir Coluna | [7] Relatório  | [8] Remover Quadro\n");
            System.out.println(" [0] Voltar");

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
    }
}