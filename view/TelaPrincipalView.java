package view;

import controller.TelaPrincipalController;
import models.KanbanModel;

public class TelaPrincipalView implements Observer {

    private KanbanModel model;
    private TelaPrincipalController controller;

    public void init(KanbanModel model) {
        this.model = model;
        controller = new TelaPrincipalController();
        controller.init(model, this);
        model.attachObserver(this);
        mostrarMenu();
    }

    public void mostrarMenu() {
        boolean sair = false;

        do {
            Prompt.clear();

            String nomeUsuario = "Usuário";
            if (model.getUsuarioLogadoString() != null) {
                nomeUsuario = model.getUsuarioLogadoString()[1];
            }

            Prompt.header("Dashboard de " + nomeUsuario);

            System.out.println("\u001B[36m -- TIMES -- \u001B[0m");
            Prompt.menuItem("1", "Meus Times (Acessar Quadro)");
            Prompt.menuItem("2", "Criar Novo Time");

            System.out.println("\n\u001B[36m -- CONTA -- \u001B[0m");
            Prompt.menuItem("3", "Notificações / Convites");
            Prompt.menuItem("4", "Meu Perfil (Editar)");

            Prompt.separator();
            Prompt.menuItem("0", "Sair (Logout)");

            System.out.println();
            String op = Prompt.input("O que deseja fazer?");

            if (op.equals("0")) {
                sair = true;
                controller.fazerLogout();
            } else {
                controller.handleEvent(op);
            }

        } while (!sair);
    }

    @Override
    public void update() {
    }
}