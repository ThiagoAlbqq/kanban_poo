package view.usuario;

import controller.usuario.TelaUsuariosController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class TelaUsuarioView implements Observer {

    private KanbanModel model;
    private TelaUsuariosController controller;
    private boolean terminar = false;

    public void terminarSistema(){
        terminar = true;
    }

    public void init(KanbanModel model) {
        if(model != null){
            this.model = model;
            controller = new TelaUsuariosController();
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

            Prompt.header("Gestão de Usuários");

            Prompt.menuItem("1", "Criar Novo Usuário");
            Prompt.menuItem("2", "Listar Todos");
            Prompt.menuItem("3", "Editar Usuário");
            Prompt.menuItem("4", "Remover Usuário");
            Prompt.separator();
            Prompt.menuItem("0", "Voltar / Sair");

            System.out.println();

            String op = Prompt.input("Escolha uma opção");

            if (op.equals("0")) {
                sair = true;
            } else {
                controller.handleEvent(op);
            }

        } while (!sair);
    }

    @Override
    public void update(){
        return;
    }

}
