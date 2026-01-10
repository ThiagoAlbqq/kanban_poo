package controller;

import models.KanbanModel;
import view.*;

public class AcessarTimeController implements Observer {

    private KanbanModel model;
    private AcessarTimeView view;

    public void init(KanbanModel model, AcessarTimeView view) {
        this.model = model;
        this.view = view;
        model.attachObserver(this);
    }

    public void listarTimesDisponiveis() {
        String[] lista = model.listarMeusTimes();
        view.exibirLista(lista);
    }

    public void selecionarTime(int id) {
        try {
            model.selecionarTime(id);

            Prompt.success("Time selecionado: " + model.getTimeSelecionado().getName());

            new QuadroView().init(model);

        } catch (RuntimeException e) {
            Prompt.error(e.getMessage());
            Prompt.scanner.nextLine();
            view.mostrarOpcoes();
        }
    }

    @Override
    public void update() {}
}