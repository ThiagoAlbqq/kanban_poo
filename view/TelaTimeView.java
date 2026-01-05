package view;

import controller.TimeController;
import models.KanbanModel;


public class TelaTimeView implements Observer {

    private KanbanModel model;
    private TimeController controller;

    public void init(KanbanModel model) {
        if(model != null){
            this.model = model;
            controller = new TimeController();
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

            Prompt.header("Gestão de Times");

            // CRUD de times
            Prompt.menuItem("1", "Criar Novo Time");
            Prompt.menuItem("2", "Listar Todos os Times");
            Prompt.menuItem("3", "Editar Time");
            Prompt.menuItem("4", "Remover Time");
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
