package controller.quadro;

import models.KanbanModel;
import view.quadro.EditarQuadroView;
import view.Observer;

public class EditarQuadroController implements Observer {

    private KanbanModel model;
    private EditarQuadroView view;

    private int id;
    private String novoNome;

    public void init(KanbanModel model, EditarQuadroView view) {
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

            model.editarQuadro(id, novoNome);

            view.sucessMensage("Edição efetuada com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}
}
