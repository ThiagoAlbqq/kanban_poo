package controller.time;

import models.KanbanModel;
import view.time.CadastroTimeView;
import view.Observer;

public class CadastroTimeController implements Observer {

    private KanbanModel model;
    private CadastroTimeView view;

    private String nome;

    public void init(KanbanModel model, CadastroTimeView view) {
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
            model.criarTime(nome);
            view.sucessMensage("Cadastro efetuado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}


}
