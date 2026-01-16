package controller.convite;

import models.KanbanModel;
import view.Observer;
import view.Prompt;
import view.convite.AcessarConviteView;
import view.convite.ListarConviteView;

public class AcessarConviteController implements Observer {

    private KanbanModel model;
    private AcessarConviteView view;

    public void init(KanbanModel model, AcessarConviteView view) {
        this.model = model;
        this.view = view;
        model.attachObserver(this);
    }

    public void listarConvitesDisponiveis() {
        String[] lista = model.verificarMeusConvites();
        view.exibirLista(lista);
    }

    public void selecionarConvite(int id) {
        try {
            model.buscarConvitePorId(id);

            Prompt.success("Convite selecionado: " + model.getTimeSelecionado().getName());

            new ListarConviteView().init(model);

        } catch (RuntimeException e) {
            Prompt.error(e.getMessage());
            Prompt.scanner.nextLine();
            view.mostrarOpcoes();
        }
    }

    @Override
    public void update() {}


}
