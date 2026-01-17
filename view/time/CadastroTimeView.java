package view.time;

import controller.time.CadastroTimeController;
import models.KanbanModel;
import view.Input;
import view.Observer;
import view.Prompt;

import java.util.Scanner;

public class CadastroTimeView implements Observer {

    private String nome;

    private KanbanModel model;
    private CadastroTimeController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new CadastroTimeController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.cadastrar();
        }
    }

    public void solicitarNome() {
        Prompt.header("DANDANDAN-KANBAN : Criar Time");
        nome = Prompt.input("Nome do Time");
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
