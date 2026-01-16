package controller;

import models.KanbanModel;
import view.Observer;
import view.QuadroView;
import view.quadro.*;

public class QuadroController implements Observer {

    private KanbanModel model;
    private QuadroView view;

    public void init(KanbanModel model, QuadroView view) {
        if (model != null && view != null){
            this.model = model;
            this.view = view;
            this.model.attachObserver(this);
        }
    }

    public void handleEvent(String opcao) {
        switch (opcao) {
            case "1": new CadastroQuadroView().init(model); break;
            case "2": new ListarQuadrosView().init(model); break;
            case "3": new DeletarQuadroView().init(model); break;
            case "4": new EditarQuadroView().init(model); break;
            case "5": new AcessarQuadroView().init(model); break;
            case "0":
                view.mensagem("Saindo...");
                System.exit(0);
                break;
            default:
                view.mensagem("Opção Invalida");
        }
    }

    @Override
    public void update() {
        if (view != null) {
            view.mensagem("[Sistema] Os dados dos quadros foram alterados.");
        }
    }
}