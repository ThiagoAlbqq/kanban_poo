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
            view.mensagem("A senha deve ter no minimo 6 digitos!");
            view.solicitarSenha();
        }

        while (!view.getSenha1().equals(view.getSenha2())) {
            view.mensagem("As senhas devem coincidir!");
            view.solicitarSenha();
        }

        try {
            this.nome = view.getNome();
            this.email = view.getEmail();
            this.senha = view.getSenha1();

            model.adicionarUsuario(nome, email, senha);
            view.mensagem("Cadastro efetuado com sucesso!");

        } catch (RuntimeException e) {
            view.mensagem("Erro: " + e.getMessage());
        }

        view.opcao();
    }

    public void handleEvent(String event) {
        switch (event) {
            case "1":
                break;
            case "2":
                CadastroUsuarioView viewNova = new CadastroUsuarioView();
                viewNova.init(model);
                break;
            default:
                view.mensagem("Opção inválida. Tente novamente!");
                view.opcao();
                break;
        }
    }

    @Override
    public void update() {}

}