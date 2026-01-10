package view;

import controller.AcessarTimeController;
import models.KanbanModel;

public class AcessarTimeView implements Observer {

    private KanbanModel model;
    private AcessarTimeController controller;

    public void init(KanbanModel model) {
        this.model = model;
        controller = new AcessarTimeController();
        controller.init(model, this);
        model.attachObserver(this);

        mostrarOpcoes();
    }

    public void mostrarOpcoes() {
        Prompt.clear();
        Prompt.header("SELECIONAR TIME");

        controller.listarTimesDisponiveis();

        System.out.println();
        Prompt.separator();
        System.out.println("Digite o ID do time para entrar ou 0 para voltar.");

        int id = Prompt.inputInt("ID do Time");

        if (id == 0) {
            return;
        } else {
            controller.selecionarTime(id);
        }
    }

    public void exibirLista(String[] times) {
        if (times.length == 0) {
            System.out.println("   (Você ainda não participa de nenhum time)");
        } else {
            for (String t : times) {
                System.out.println("   " + t);
            }
        }
    }

    @Override
    public void update() {}
}