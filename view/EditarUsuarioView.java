package view;

import controller.DeletarUsuarioController;
import controller.EditarUsuarioController;
import models.KanbanModel;

public class EditarUsuarioView implements Observer{

    private int id;
    private String novoNome, novoEmail, novaSenha;
    private String opcao = "0";

    private KanbanModel model;
    private EditarUsuarioController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new EditarUsuarioController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.editar();
        }
    }

    public void solicitarId() {
        System.out.print("Id: ");
        id = Input.lerInt();
    }

    public void solicitarNome() {
        System.out.println("\n--- EDITAR USUARIO ---");
        System.out.print("Nome (Opcional): ");
        novoNome = Input.lerString();
    }

    public void solicitarEmail() {
        System.out.print("Email (Opcional): ");
        novoEmail = Input.lerString();
    }

    public void solicitarSenha() {
        System.out.print("Senha (Opcional): ");
        novaSenha = Input.lerString();
    }

    public int getId() { return id; }
    public String getNome() { return novoNome; }
    public String getEmail() { return novoEmail; }
    public String getSenha() { return novaSenha; }

    public void mensagem(String msg) {
        System.out.println(">> " + msg);
        System.out.println();
    }

    public void opcao() {
        System.out.println("1 - Voltar ao Menu Principal");
        System.out.println("2 - Nova remoção3");
        System.out.println();
        System.out.print("Digite a opcao: ");
        opcao = Input.lerString();
        controller.handleEvent(opcao);
    }

    @Override
    public void update() {
    }

}
