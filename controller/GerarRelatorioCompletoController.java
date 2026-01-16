package controller;

import models.KanbanModel;
import view.GerarRelatorioCompletoView;
import view.Observer;

public class GerarRelatorioCompletoController implements Observer {

    private KanbanModel model;
    private GerarRelatorioCompletoView view;

    public void init(KanbanModel model, GerarRelatorioCompletoView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {
        try {
            String[] lista = model.gerarRelatorioCompleto();
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
