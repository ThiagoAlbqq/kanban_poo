package controller.quadro;

import models.KanbanModel;
import view.quadro.ListarQuadrosView;
import view.Observer;

public class ListarQuadroController implements Observer {

    private KanbanModel model;
    private ListarQuadrosView view;

    public void init(KanbanModel model, ListarQuadrosView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {
        try {
            String[] lista = model.listarQuadros();
            for(String u : lista) {
                view.mensagem(u);
            }

            view.sucessMensage("Quadros listados com sucesso!\n");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
