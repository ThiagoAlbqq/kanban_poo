package controller;

import models.KanbanModel;
import view.DeletarUsuarioView;
import view.Observer;

public class DeletarUsuarioController implements Observer {

    private KanbanModel model;
    private DeletarUsuarioView view;

    private int id;

    public void init(KanbanModel model, DeletarUsuarioView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void deletar() {
        view.solicitarId();

        try {
            this.id = view.getId();

            model.deletarUsuario(id);
            view.mensagem("Usuario deletado com sucesso!");

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
                DeletarUsuarioView viewNova = new DeletarUsuarioView();
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
