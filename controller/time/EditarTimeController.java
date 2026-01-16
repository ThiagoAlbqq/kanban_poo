package controller.time;

import models.KanbanModel;
import view.time.EditarTimeView;
import view.Observer;

public class EditarTimeController implements Observer {


    private KanbanModel model;
    private EditarTimeView view;

    private String novoNome;

    public void init(KanbanModel model, EditarTimeView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void editar() {
        view.solicitarNome();

        try {
            this.novoNome = view.getNome();

            model.editarTime(novoNome);

            view.sucessMensage("Edição efetuada com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
