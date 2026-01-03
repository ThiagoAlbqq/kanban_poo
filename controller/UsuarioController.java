package controller;

import models.KanbanModel;
import view.*;

public class UsuarioController implements Observer {

    private KanbanModel model;
    private UsuarioView view;

    public void init(KanbanModel model, UsuarioView view) {
        if (model != null && view != null){
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void handleEvent(String opcao) {
        switch (opcao) {
            case "1":
                new CadastroUsuarioView().init(model);
                break;
            case "2":
                new ListarUsuariosView().init(model);
                break;
            case "3":
                new DeletarUsuarioView().init(model);
                break;
            case "4":
                new EditarUsuarioView().init(model);
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
