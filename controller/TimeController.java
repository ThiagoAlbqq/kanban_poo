package controller;

import models.KanbanModel;
import view.*;
import view.time.*;

public class TimeController implements Observer {

    private KanbanModel model;
    private TelaTimeView view;

    public void init(KanbanModel model, TelaTimeView view) {
        if (model != null && view != null){
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void handleEvent(String opcao) {
        switch (opcao) {
            case "1":
                new CadastroTimeView().init(model);
                break;
            case "2":
                new ListarTimesView().init(model);
                break;
            case "3":
                new EditarTimeView().init(model);
                break;
            case "4":
                new DeletarTimeView().init(model);
                break;
            case "5":
                new AcessarTimeView().init(model);
                break;
            case "0":
                view.mensagem("Saindo...");
                System.exit(0);
                break;
            default:
                view.mensagem("Opção Invalida");
                view.mostrarMenu();
        }
    }

    @Override
    public void update() {}


}
