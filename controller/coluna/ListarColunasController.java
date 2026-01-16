package controller.coluna;

import models.KanbanModel;
import view.Observer;
import view.coluna.ListarColunasView;
import view.time.ListarTimesView;

public class ListarColunasController implements Observer {

    private KanbanModel model;
    private ListarColunasView view;

    public void init(KanbanModel model, ListarColunasView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void listar() {

        try {
            String[] lista = model.listarColunas();
            for(String c : lista) {
                view.mensagem("   " + c);
            }

            view.sucessMensage("Colunas listados com sucesso!\n");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}


}
