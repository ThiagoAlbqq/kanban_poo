package controller.convite;

import models.KanbanModel;
import view.Observer;
import view.convite.TelaConvitesView;

public class TelaConvitesController implements Observer {

    private KanbanModel model;
    private TelaConvitesView view;

    public void init(KanbanModel model, TelaConvitesView view) {
        this.model = model;
        this.view = view;
    }

    public void handleEvent(String opcao) {
        try {
            int idConvite = Integer.parseInt(opcao);
            try {
                model.buscarConvitePorId(idConvite);
                String decisao = view.mostrarMenuResposta(String.valueOf(idConvite));
                processarDecisao(idConvite, decisao);

            } catch (IllegalArgumentException e) {
                view.mensagemErro(e.getMessage());
            }

        } catch (NumberFormatException e) {
            view.mensagemErro("Opção inválida. Digite apenas o ID numérico.");
        }
    }

    private void processarDecisao(int idConvite, String decisao) {
        try {
            switch (decisao.toUpperCase()) {
                case "A":
                    model.responderConvite(idConvite, true);
                    view.mensagemSucesso("Convite aceito! Time adicionado à sua lista.");
                    break;

                case "R":
                    model.responderConvite(idConvite, false);
                    view.mensagemSucesso("Convite recusado.");
                    break;

                case "V":
                    break;

                default:
                    view.mensagemErro("Opção inválida.");
            }
        } catch (IllegalStateException e) {
            view.mensagemErro(e.getMessage());
        }
    }

    public void sair() {
    }

    @Override
    public void update() {}
}