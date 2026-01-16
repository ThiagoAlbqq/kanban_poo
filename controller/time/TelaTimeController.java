package controller.time;

import models.KanbanModel;
import view.Observer;
import view.time.CadastroTimeView;
import view.time.DashboardTimeView;
import view.time.ListarTimesView;
import view.time.TelaTimeView;

public class TelaTimeController implements Observer {

    private KanbanModel model;
    private TelaTimeView view;

    public void init(KanbanModel model, TelaTimeView view) {
        this.model = model;
        this.view = view;
    }

    public void handleEvent(String opcao) {
        try {
            int idTime = Integer.parseInt(opcao);

            model.selecionarTime(idTime);
            new DashboardTimeView().init(model);

        } catch (NumberFormatException e) {
            if (opcao.equalsIgnoreCase("C")) {
                new CadastroTimeView().init(model);
            } else {
                view.mensagem("Opção inválida.");
            }
        }
    }

    public void sair() {
        model.selecionarTime(-1); // Limpa seleção
    }

    @Override
    public void update() {}
}