package controller.coluna;

import models.KanbanModel;
import view.Observer;
import view.coluna.EditarColunaView;
import view.quadro.EditarQuadroView;

public class EditarColunaController implements Observer {

    private KanbanModel model;
    private EditarColunaView view;

    private int id;
    private String novoNome;

    public void init(KanbanModel model, EditarColunaView view) {
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

            model.editarColuna(id, novoNome);

            view.sucessMensage("Edição efetuada com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
