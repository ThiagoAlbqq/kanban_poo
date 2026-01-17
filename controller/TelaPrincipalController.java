package controller;

import models.KanbanModel;
import view.*;
import view.convite.TelaConvitesView;
import view.time.CadastroTimeView;
import view.time.ListarTimesView;
import view.time.TelaTimeView;
import view.usuario.EditarUsuarioView;

public class TelaPrincipalController implements Observer {

    private KanbanModel model;
    private TelaPrincipalView view;

    public void init(KanbanModel model, TelaPrincipalView view) {
        this.model = model;
        this.view = view;
        model.attachObserver(this);
    }

    public void fazerLogout() {
        model.deslogarUsuario();
        Prompt.success("Logout realizado com sucesso.");
    }

    public void handleEvent(String opcao) {
        switch (opcao) {
            case "1":
                new TelaTimeView().init(model);
                break;
            case "2":
                new CadastroTimeView().init(model);
                break;
            case "3":
                new TelaConvitesView().init(model);
                break;
            case "4":
                new EditarUsuarioView().init(model);
                break;
            default:
                Prompt.error("Opção inválida.");
                Prompt.scanner.nextLine();
        }
    }

    @Override
    public void update() {}
}