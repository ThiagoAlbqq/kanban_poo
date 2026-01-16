package controller.convite;

import models.KanbanModel;
import view.Observer;
import view.convite.DeletarConviteView;
import view.quadro.DeletarQuadroView;

public class DeletarConviteController implements Observer {

    private KanbanModel model;
    private DeletarConviteView view;

    private int id;

    public void init(KanbanModel model, DeletarConviteView view) {
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

            model.deletarConvite(id);
            view.sucessMensage("Convite deletado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
