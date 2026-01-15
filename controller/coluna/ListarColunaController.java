package controller.coluna;

import models.KanbanModel;
import view.Observer;
import view.quadro.ListarQuadrosView;

public class ListarColunaController implements Observer {

    private KanbanModel model;
    private ListarColunaView view;

    public void init(KanbanModel model, ListarColunaView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {
        try {
            String[] lista = model.listarColuna();
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
