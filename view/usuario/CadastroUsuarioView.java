package view.usuario;

import controller.usuario.CadastroUsuarioController;
import models.KanbanModel;
import view.Input;
import view.Observer;
import view.Prompt;

import java.util.Scanner;

public class CadastroUsuarioView implements Observer {

    private String nome, email, senha1, senha2;
    private String opcao = "0";

    private KanbanModel model;
    private CadastroUsuarioController controller;
    public Scanner sc = Input.scanner;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new CadastroUsuarioController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.cadastrar();
        }
    }

    public void solicitarNome() {
        Prompt.header("DANDANDAN-KANBAN : Criar Conta");
        nome = Prompt.input("Nome do Usuario");
    }

    public void solicitarEmail() {
        email = Prompt.input("Email da Conta");
    }

    public void solicitarSenha() {
        senha1 = Prompt.input("Senha da Conta");
        senha2 = Prompt.input("Confirma senha");
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha1() { return senha1; }
    public String getSenha2() { return senha2; }

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