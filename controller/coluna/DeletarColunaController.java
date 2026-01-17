package controller.coluna;

import models.KanbanModel;
import view.Observer;
import view.coluna.DeletarColunaView;

public class DeletarColunaController implements Observer {

    private KanbanModel model;
    private DeletarColunaView view;

    private int id;

    public void init(KanbanModel model, DeletarColunaView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void deletar() {
        view.solicitarId();

        try {
            this.id = view.getId();

            model.deletarColuna(id);
            view.sucessMensage("Time deletado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
