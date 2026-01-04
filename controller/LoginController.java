package controller;

import models.KanbanModel;
import view.*;

public class LoginController implements Observer {

    private KanbanModel model;
    private LoginView view;

    private String email, senha;

    public void init(KanbanModel model, LoginView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void login() {
        view.title();
        view.solicitarEmail();
        view.solicitarSenha();

        try {
            this.email = view.getEmail();
            this.senha = view.getSenha();

            model.autenticarUsuario(email, senha);
            view.sucessMensage("Login efetuado com sucesso!");

            new TelaPrincipalView().init(model);

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}

}
