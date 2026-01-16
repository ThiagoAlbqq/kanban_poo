package view.card;

import controller.card.EditarCardController;
import controller.card.TelaCardController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class EditarCardView implements Observer {

    private int id;
    private String titulo, desc;
    private int prioOp;

    private KanbanModel model;
    private EditarCardController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new EditarCardController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.editar();
        }
    }

    public void solicitarId() {
        id = Prompt.inputInt("Id");
    }

    public void solicitarTitulo() {
        Prompt.header("EDITAR TAREFA");
        titulo = Prompt.input("Título");
    }

    public void solicitarDescricao() {
        desc = Prompt.input("Descrição");
    }

    public void solicitarPrioridade() {
        System.out.println("Prioridade: [1] Baixa | [2] Média | [3] Alta");
        prioOp = Prompt.inputInt("Opção");
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


    public int getId() { return id; }

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

    public static class TelaCardView implements Observer {

        private KanbanModel model;
        private TelaCardController controller;

        public void init(KanbanModel model) {
            if(model != null){
                this.model = model;
                controller = new TelaCardController();
                controller.init(model, this);
                model.attachObserver(this);
                mostrarMenu();
            }
        }

        public void mensagem(String msg){
            System.out.println(">>" + msg);
        }

        public void mostrarMenu() {
            boolean sair = false;

            do {
                Prompt.clear();

                Prompt.header("Gestão de Cards");

                Prompt.menuItem("1", "Criar Novo Card");
                Prompt.menuItem("2", "Listar Todos");
                Prompt.menuItem("3", "Editar Card");
                Prompt.menuItem("4", "Remover Card");
                Prompt.separator();
                Prompt.menuItem("0", "Voltar / Sair");

                System.out.println();

                String op = Prompt.input("Escolha uma opção");

                if (op.equals("0")) {
                    sair = true;
                } else {
                    controller.handleEvent(op);
                }

            } while (!sair);
        }

        @Override
        public void update(){
            return;
        }

    }
}
