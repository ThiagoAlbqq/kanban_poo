package controller;

import models.KanbanModel;
import view.EditarTimeView;
import view.Observer;

public class EditarTimeController implements Observer {


    private KanbanModel model;
    private EditarTimeView view;

    private int id;
    private String novoNome;

    public void init(KanbanModel model, EditarTimeView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void editar() {
        view.solicitarId();
        view.solicitarNome();

        try {
            this.id = view.getId();
            this.novoNome = view.getNome();

            model.editarTime(id, novoNome);

            view.sucessMensage("Edição efetuada com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
