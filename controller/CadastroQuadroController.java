package controller;

import models.KanbanModel;
import view.CadastroQuadroView;
import view.Observer;

public class CadastroQuadroController implements Observer {

    private KanbanModel model;
    private CadastroQuadroView view;

    private String nome;

    public void init(KanbanModel model, CadastroQuadroView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void cadastrar() {
        view.solicitarNome();

        try {
            this.nome = view.getNome();
            model.criarQuadro(nome);
            view.sucessMensage("Cadastro efetuado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}

}
