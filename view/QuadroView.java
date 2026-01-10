package view;

import controller.QuadroController;
import controller.UsuarioController;
import models.KanbanModel;

public class QuadroView implements Observer {


    private KanbanModel model;
    private QuadroController controller;

    public void init(KanbanModel model) {
        if(model != null){
            this.model = model;
            controller = new QuadroController();
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

            Prompt.header("Gestão de Quadros");

            Prompt.menuItem("1", "Criar Quadro");
            Prompt.menuItem("2", "Listar Quadro");
            Prompt.menuItem("3", "Editar Quadro");
            Prompt.menuItem("4", "Remover Quadro");
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
