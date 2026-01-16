package controller.quadro;

import models.KanbanModel;
import view.Observer;
import view.quadro.GerarEstatisticasQuadroView;

public class GerarEstatisticasQuadroController implements Observer {

    private KanbanModel model;
    private GerarEstatisticasQuadroView view;

    public void init(KanbanModel model, GerarEstatisticasQuadroView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {
        try {
            String[] lista = model.gerarEstatisticasDoQuadro();
            for(String u : lista) {
                view.mensagem(u);
            }

            view.sucessMensage("Relatorio listado com sucesso!\n");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
