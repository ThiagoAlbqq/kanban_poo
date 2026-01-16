package controller.time;

import models.KanbanModel;
import view.TelaPrincipalView;
import view.time.DeletarTimeView;
import view.Observer;

public class DeletarTimeController implements Observer {

    private KanbanModel model;
    private DeletarTimeView view;

    private int id;

    public void init(KanbanModel model, DeletarTimeView view) {
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

            model.deletarTime(id);
            view.sucessMensage("Time deletado com sucesso!");
            new TelaPrincipalView().init(model);
        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
