package view;

import controller.CadastroUsuarioController;
import controller.LoginController;
import models.KanbanModel;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.Scanner;

public class LoginView implements Observer {


    private String email, senha;
    private String opcao = "0";

    private KanbanModel model;
    private LoginController controller;
    public Scanner sc = Input.scanner;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new LoginController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.login();
        }
    }

    public void title() {
        Prompt.clear();
        Prompt.header("DANDANDAN-KANBAN : LOGIN");
    }

    public void solicitarEmail() {
        email = Prompt.input("Email: ");
    }

    public void solicitarSenha() {
        senha = Prompt.input("Senha: ");
    }

    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    public void sucessMensage(String msg) {
        System.out.println(" ");
        Prompt.success(msg);

        System.out.println("\nPressione Enter para continuar...");
        Prompt.scanner.nextLine();
    }

    public void failMensage(String msg) {
        Prompt.error(msg);
    }

    @Override
    public void update() {
    }

}
