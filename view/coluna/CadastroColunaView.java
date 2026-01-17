package view.coluna;

import controller.coluna.CadastroColunaController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class CadastroColunaView implements Observer {

    private String nome;
    private KanbanModel model;
    private CadastroColunaController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new CadastroColunaController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.cadastrar();
        }
    }

    public void solicitarNome() {
        Prompt.header("NOVA COLUNA");
        nome = Prompt.input("Nome da Coluna");
    }

    public String getNome() {
        return nome;
    }

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
