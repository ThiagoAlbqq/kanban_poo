package controller.quadro;

import models.KanbanModel;
import view.quadro.DeletarQuadroView;
import view.Observer;

public class DeletarQuadroController implements Observer {

    private KanbanModel model;
    private DeletarQuadroView view;

    private int id;

    public void init(KanbanModel model, DeletarQuadroView view) {
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

            model.deletarQuadro(id);
            view.sucessMensage("Time deletado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}
}
