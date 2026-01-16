package controller.time;

import models.KanbanModel;
import view.TelaPrincipalView;
import view.time.DeletarTimeView;
import view.Observer;

public class DeletarTimeController implements Observer {

    private KanbanModel model;
    private DeletarTimeView view;

    public void init(KanbanModel model, DeletarTimeView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void deletar() {
        try {
            model.deletarTime();
            view.sucessMensage("Time deletado com sucesso!");
        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
