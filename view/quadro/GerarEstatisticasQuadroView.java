package view.quadro;

import controller.quadro.GerarEstatisticasQuadroController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class GerarEstatisticasQuadroView implements Observer {

    private KanbanModel model;
    private GerarEstatisticasQuadroController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new GerarEstatisticasQuadroController();
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
