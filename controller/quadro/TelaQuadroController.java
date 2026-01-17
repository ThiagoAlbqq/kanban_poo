package controller.quadro;

import models.KanbanModel;
import view.Observer;
import view.card.DeletarCardView;
import view.card.EditarCardView;
import view.coluna.DeletarColunaView;
import view.quadro.DeletarQuadroView;
import view.quadro.GerarEstatisticasQuadroView;
import view.quadro.TelaQuadroView;
import view.card.CadastroCardView;
import view.card.MoverCardView;
import view.coluna.CadastroColunaView;

public class TelaQuadroController implements Observer {

    private KanbanModel model;
    private TelaQuadroView view;

    public void init(KanbanModel model, TelaQuadroView view) {
        this.model = model;
        this.view = view;
        this.model.attachObserver(this);
    }

    public void handleEvent(String opcao) {
        switch (opcao) {
            case "1":
                new CadastroCardView().init(model);
                break;

            case "2":
                new MoverCardView().init(model);
                break;

            case "3":
                new EditarCardView().init(model);
                break;

            case "4":
                new DeletarCardView().init(model);
                break;

            case "5":
                new CadastroColunaView().init(model);
                break;

            case "6":
                new DeletarColunaView().init(model);
                break;

            case "7":
                new GerarEstatisticasQuadroView().init(model);
                break;

            case "8":
                new DeletarQuadroView().init(model);
                break;

            default:
                view.mensagem("Opção inválida.");
        }
    }

    public void sair() {
        model.selecionarQuadro(-1);
    }

    @Override
    public void update() {
    }
}