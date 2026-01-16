package controller.card;

import models.KanbanModel;
import view.Observer;
import view.card.CadastroCardView;
import view.card.DeletarCardView;
import view.card.EditarCardView;
import view.card.ListarCardView;

public class TelaCardController implements Observer {

    private KanbanModel model;
    private EditarCardView.TelaCardView view;

    public void init(KanbanModel model, EditarCardView.TelaCardView view) {
        if (model != null && view != null){
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void handleEvent(String opcao) {
        switch (opcao) {
            case "1":
                new CadastroCardView().init(model);
                break;
            case "2":
                new ListarCardView().init(model);
                break;
            case "3":
                new DeletarCardView().init(model);
                break;
            case "4":
                new EditarCardView().init(model);
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
