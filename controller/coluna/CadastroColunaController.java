package controller.coluna;

import models.KanbanModel;
import view.Observer;
import view.quadro.CadastroQuadroView;

public class CadastroColunaController implements Observer {

    private KanbanModel model;
    private CadastroColunaView view;

    private String nome;

    public void init(KanbanModel model, CadastroColunaView view) {
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
            model.criarColuna(nome);
            view.sucessMensage("Cadastro efetuado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}

}
