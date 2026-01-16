package view;

import controller.GerarRelatorioCompletoController;
import controller.quadro.GerarEstatisticasQuadroController;
import models.KanbanModel;

public class GerarRelatorioCompletoView implements Observer {

    private KanbanModel model;
    private GerarRelatorioCompletoController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new GerarRelatorioCompletoController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.listar();
        }
    }

    public void mensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void sucessMensage(String msg) {
        System.out.println(" ");
        Prompt.success(msg);
    }

    public void failMensage(String msg) {
        Prompt.error(msg);
    }

    @Override
    public void update() {
    }

}
