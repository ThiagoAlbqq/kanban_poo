package controller;

import models.KanbanModel;
import view.CadastroUsuarioView;
import view.Observer;

public class CadastroUsuarioController implements Observer {

    private KanbanModel model;
    private CadastroUsuarioView view;

    private String nome, email, senha;

    public void init(KanbanModel model, CadastroUsuarioView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void cadastrar() {
        view.solicitarNome();
        view.solicitarEmail();
        view.solicitarSenha();

        while (view.getSenha1().length() < 6) {
            view.failMensage("A senha deve ter no minimo 6 digitos!");
            view.solicitarSenha();
        }

        while (!view.getSenha1().equals(view.getSenha2())) {
            view.failMensage("As senhas devem coincidir!");
            view.solicitarSenha();
        }

        try {
            this.nome = view.getNome();
            this.email = view.getEmail();
            this.senha = view.getSenha1();

            model.adicionarUsuario(nome, email, senha);
            view.sucessMensage("Cadastro efetuado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}

}