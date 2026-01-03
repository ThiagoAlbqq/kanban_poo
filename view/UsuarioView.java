package view;

import models.KanbanModel;

public class UsuarioView implements Observer {

    private KanbanModel model;
    private UsuarioController controller;
    private boolean terminar = false;

    public void terminarSistema(){
        terminar = true;
    }

    public void init(KanbanModel model) {
        if(model != null){
            this.model = model;
            controller = new UsuarioController();
            controller.init(model, this);
            model.attachObserver(this);
            menuPrincipal();
        }
    }

    public void mensagem(String msg){
        System.out.println(">>" + msg);
    }

    public void mostrarMenu() {
        do {
            System.out.println("\n=== BEM-VINDO AO KANBAN 10 ===");
            System.out.println("1 - Criar Usuario");
            System.out.println("2 - Listar Usuarios");
            System.out.println("3 - Deletar Usuario");
            System.out.println("4 - Editar Usuario");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            controller.handleEvent(Input.lerString());
        } while (!terminar);
    }

    @Override
    public void update(){
        return;
    }

}
