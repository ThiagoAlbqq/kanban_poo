package view;

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
        id = Prompt.inputInt("Id");
    }

    public void solicitarNome() {
        novoNome = Prompt.input("Nome");
    }

    public void solicitarEmail() {
        novoEmail = Prompt.input("Email");
    }

    public void solicitarSenha() {
        novaSenha = Prompt.input("Senha");
    }

    public int getId() { return id; }
    public String getNome() { return novoNome; }
    public String getEmail() { return novoEmail; }
    public String getSenha() { return novaSenha; }

    public void sucessMensage(String msg) {
        System.out.println(" ");
        Prompt.success(msg);
    }

    public void failMensage(String msg) {
        Prompt.error(msg);
    }

    @Override
    public void update() {
    }

}
