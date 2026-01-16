package controller.time;

import models.KanbanModel;
import view.Observer;
import view.TelaTimeNewView; // A tela que você me mandou
import view.time.CadastroTimeView;
import view.time.ListarTimesView;

public class TelaTimeController implements Observer {

    private KanbanModel model;
    private ListarTimesView.TelaTimeView view;

    public void init(KanbanModel model, ListarTimesView.TelaTimeView view) {
        this.model = model;
        this.view = view;
    }

    public void handleEvent(String opcao) {
        try {
            // Tenta converter o input para ID de Time
            int idTime = Integer.parseInt(opcao);

            // Tenta "Setar" o time no model
            model.selecionarTime(idTime);
            new TelaTimeNewView().init(model);

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