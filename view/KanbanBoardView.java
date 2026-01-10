package view;

import controller.KanbanBoardController;
import models.KanbanModel;

public class KanbanBoardView implements Observer {

    private KanbanModel model;
    private KanbanBoardController controller;

    public void init(KanbanModel model) {
        this.model = model;
        controller = new KanbanBoardController();
        controller.init(model, this);
        model.attachObserver(this);

        mostrarQuadro();
    }

    public void mostrarQuadro() {
        boolean sair = false;

        do {
            limparTela();

            // Título com o nome do quadro atual
            String nomeQuadro = "Quadro";
            if(model.getQuadroSelecionado() != null) {
                nomeQuadro = model.getQuadroSelecionado().getNome();
            }
            Prompt.header("QUADRO: " + nomeQuadro.toUpperCase());

            // --- DESENHA AS COLUNAS ---
            // Pede ao model os dados formatados
            String[] colunas = model.listarCardsPorColuna();

            if (colunas.length == 0) {
                System.out.println("   (Quadro vazio)");
            } else {
                for(String linha : colunas) {
                    // Se for título de coluna, pinta de ciano
                    if(linha.startsWith("---")) {
                        System.out.println("\u001B[36m" + linha + "\u001B[0m");
                    } else {
                        System.out.println(linha);
                    }
                }
            }

            System.out.println();
            Prompt.separator();

            // ADICIONE A OPÇÃO 3 AQUI
            System.out.println("[1] Novo Card  |  [2] Mover Card  |  [3] Nova Coluna");
            System.out.println("[0] Voltar");

            String op = Prompt.input("Ação");

            switch(op) {
                case "1": controller.criarCard(); break;
                case "2": controller.moverCard(); break;

                // ADICIONE O CASE 3
                case "3": controller.criarColuna(); break;

                case "0": sair = true; break;
                default: Prompt.error("Opção inválida."); pausa();
            }

        } while (!sair);
    }

    // Utilitários visuais para o Controller usar
    public void limparTela() { Prompt.clear(); }
    public void pausa() {
        System.out.println("Enter para continuar...");
        Prompt.scanner.nextLine();
    }

    @Override
    public void update() {}
}