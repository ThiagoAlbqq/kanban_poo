package controller;

import models.KanbanModel;
import view.*;

import java.awt.event.ActionListener;

public class TelaInicialController implements Observer {
    private KanbanModel model;
    private TelaInicialView view;

    public void init(KanbanModel model, TelaInicialView view) {
        if(model == null || view == null) return;
        this.model = model;
        this.view = view;
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