package controller.time;

import models.KanbanModel;
import view.Observer;
import view.TelaTimeNewView;
import view.TelaQuadroView;
import view.convite.CadastroConviteView;
import view.quadro.CadastroQuadroView;
import view.time.DeletarTimeView;
import view.time.EditarTimeView;

public class TelaTimeNewController implements Observer {

    private KanbanModel model;
    private TelaTimeNewView view;

    public void init(KanbanModel model, TelaTimeNewView view) {
        this.model = model;
        this.view = view;
        this.model.attachObserver(this);
    }

    public void handleEvent(String opcao) {
        try {
            int idQuadro = Integer.parseInt(opcao);
            model.selecionarQuadro(idQuadro);
            new TelaQuadroView().init(model);
        } catch (NumberFormatException e) {
            switch (opcao.toUpperCase()) {
                case "N":
                    new CadastroQuadroView().init(model);
                    break;
                case "E":
                    new EditarTimeView().init(model);
                    break;
                case "R":
                    new DeletarTimeView().init(model);
                    break;
                case "C":
                    new CadastroConviteView().init(model);
                    break;

                default:
                    view.mensagem("Opção ou ID inválido.");
            }
        }
    }

    public void sair() {
        model.selecionarTime(-1);
    }

    @Override
    public void update() {
    }
}