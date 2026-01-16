package view.convite;

import controller.convite.AcessarConviteController;
import controller.quadro.AcessarQuadroController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;
import view.QuadroView;

public class AcessarConviteView implements Observer {

    private KanbanModel model;
    private AcessarConviteController controller;

    public void init(KanbanModel model) {
        this.model = model;
        controller = new AcessarConviteController();
        controller.init(model, this);
        model.attachObserver(this);

        mostrarOpcoes();
    }

    public void mostrarOpcoes() {
        Prompt.clear();
        Prompt.header("SELECIONAR CONVITE");

        controller.listarConvitesDisponiveis();

        System.out.println();
        Prompt.separator();
        System.out.println("Digite o ID do convite para entrar ou 0 para voltar.");

        int id = Prompt.inputInt("ID do convite");
        if (id == 0) {
            new QuadroView().init(model);
        } else {
            controller.selecionarConvite(id);
        }
    }

    public void exibirLista(String[] times) {
        if (times.length == 0) {
            System.out.println("   (Convites ainda não tem convite)");
        } else {
            for (String t : times) {
                System.out.println("   " + t);
            }
        }
    }

    @Override
    public void update() {}

}
