package controller.convite;

import models.KanbanModel;
import view.Observer;
import view.convite.CadastroConviteView;

public class CadastroConviteController implements Observer {

    private String emailDestinatario;
    private KanbanModel model;
    private CadastroConviteView view;

    public void init(KanbanModel model, CadastroConviteView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void cadastrar() {
        view.solicitarEmail();

        try {
            this.emailDestinatario = view.getEmail();
            model.enviarConvite(emailDestinatario);
            view.sucessMensage("Convite enviado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}

}
