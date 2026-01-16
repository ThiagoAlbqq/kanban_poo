package controller.quadro;

import models.KanbanModel;
import view.*;
import view.quadro.AcessarQuadroView;

public class AcessarQuadroController implements Observer {

    private KanbanModel model;
    private AcessarQuadroView view;

    public void init(KanbanModel model, AcessarQuadroView view) {
        this.model = model;
        this.view = view;
        model.attachObserver(this);
    }

    public void listarQuadrosDisponiveis() {
        String[] lista = model.listarQuadros();
        view.exibirLista(lista);
    }

    public void selecionarQuadro(int id) {
        try {
            model.selecionarQuadro(id);

            Prompt.success("Quadro selecionado: " + model.getTimeSelecionado().getName());

            new TelaQuadroView().init(model);

        } catch (RuntimeException e) {
            Prompt.error(e.getMessage());
            Prompt.scanner.nextLine();
            view.mostrarOpcoes();
        }
    }

    @Override
    public void update() {}

}
