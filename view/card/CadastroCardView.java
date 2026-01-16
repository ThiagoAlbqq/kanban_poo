package view.card;

import controller.card.CadastroCardController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class CadastroCardView implements Observer {

    private int idColuna;
    private String titulo, desc;
    private int prioOp;
    private String opcao = "0";

    private KanbanModel model;
    private CadastroCardController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new CadastroCardController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.cadastrar();
        }
    }

    public void solicitarIdColuna() {
        Prompt.header("NOVA TAREFA");
        idColuna = Prompt.inputInt("ID Coluna");
    }

    public void solicitarTitulo() {
        titulo = Prompt.input("Título");
    }

    public void solicitarDescricao() {
        desc = Prompt.input("Descrição");
    }

    public void solicitarPrioridade() {
        System.out.println("Prioridade: [1] Baixa | [2] Média | [3] Alta");
        prioOp = Prompt.inputInt("Opção");
    }

    public int getIdColuna() {
        return idColuna;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrioOp() {
        return prioOp;
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
