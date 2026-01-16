package view;

import controller.CardController;
import models.KanbanModel;

public class CardView implements Observer {

    private KanbanModel model;
    private CardController controller;

    public void init(KanbanModel model) {
        if(model != null){
            this.model = model;
            controller = new CardController();
            controller.init(model, this);
            model.attachObserver(this);
            mostrarMenu();
        }
    }

    public void mensagem(String msg){
        System.out.println(">>" + msg);
    }

    public void mostrarMenu() {
        boolean sair = false;

        do {
            Prompt.clear();

            Prompt.header("Gestão de Cards");

            Prompt.menuItem("1", "Criar Novo Card");
            Prompt.menuItem("2", "Listar Todos");
            Prompt.menuItem("3", "Editar Card");
            Prompt.menuItem("4", "Remover Card");
            Prompt.separator();
            Prompt.menuItem("0", "Voltar / Sair");

            System.out.println();

            String op = Prompt.input("Escolha uma opção");

            if (op.equals("0")) {
                sair = true;
            } else {
                controller.handleEvent(op);
                System.out.println("\nPressione Enter para continuar...");
                Prompt.scanner.nextLine();
            }

        } while (!sair);
    }

    @Override
    public void update(){
        return;
    }

}
