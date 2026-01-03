package controller;

import models.KanbanModel;
import view.DeletarUsuarioView;
import view.ListarUsuariosView;
import view.Observer;

public class ListarUsuariosController implements Observer {

    private KanbanModel model;
    private ListarUsuariosView view;

    public void init(KanbanModel model, ListarUsuariosView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {

        try {
            String[] lista = model.listarUsuarios();
            for(String u : lista) {
                view.mensagem(u);
            }

            view.mensagem("Usuarios listados com sucesso!\n");

        } catch (RuntimeException e) {
            view.mensagem("Erro: " + e.getMessage());
        }

        view.opcao();
    }

    public void handleEvent(String event) {
        switch (event) {
            case "1":
                break;
            case "2":
                ListarUsuariosView viewNova = new ListarUsuariosView();
                viewNova.init(model);
                break;
            default:
                view.mensagem("Opção inválida. Tente novamente!");
                view.opcao();
                break;
        }
    }

    @Override
    public void update() {}
    
}
