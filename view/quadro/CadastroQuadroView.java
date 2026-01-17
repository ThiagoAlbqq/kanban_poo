package view.quadro;

import controller.quadro.CadastroQuadroController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class CadastroQuadroView implements Observer {

    private String nome;

    private KanbanModel model;
    private CadastroQuadroController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new CadastroQuadroController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.cadastrar();
        }
    }

    public void solicitarNome() {
        Prompt.header("DANDANDAN-KANBAN : Criar Quadro");
        nome = Prompt.input("Nome do Quadro");
    }
    public String getNome() { return nome; }

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
