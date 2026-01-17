package view.convite;

import controller.convite.CadastroConviteController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class CadastroConviteView implements Observer {

    private String email;

    private KanbanModel model;
    private CadastroConviteController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new CadastroConviteController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.cadastrar();
        }
    }

    public void solicitarEmail() {
        Prompt.header("DANDANDAN-KANBAN : Criar Convite");
        email = Prompt.input("Email do convidado");
    }
    public String getEmail() { return email; }

    public void sucessMensage(String msg) {
        System.out.println(" ");
        Prompt.success(msg);
    }

    public void failMensage(String msg) {
        Prompt.error(msg);
    }

    @Override
    public void update() {
    }

}
