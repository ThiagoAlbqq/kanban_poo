package view;

import controller.AcessarQuadroController;
import controller.AcessarTimeController;
import models.KanbanModel;

public class AcessarQuadroView implements Observer {

    private KanbanModel model;
    private AcessarQuadroController controller;

    public void init(KanbanModel model) {
        this.model = model;
        controller = new AcessarQuadroController();
        controller.init(model, this);
        model.attachObserver(this);

        mostrarOpcoes();
    }

    public void mostrarOpcoes() {
        Prompt.clear();
        Prompt.header("SELECIONAR TIME");

        controller.listarQuadrosDisponiveis();

        System.out.println();
        Prompt.separator();
        System.out.println("Digite o ID do time para entrar ou 0 para voltar.");

        int id = Prompt.inputInt("ID do Time");

        if (id == 0) {
            new QuadroView().init(model);
        } else {
            controller.selecionarQuadro(id);
        }
    }

    public void exibirLista(String[] times) {
        if (times.length == 0) {
            System.out.println("   (Esse quadro ainda não tem colunas)");
        } else {
            for (String t : times) {
                System.out.println("   " + t);
            }
        }
    }

    @Override
    public void update() {}
}