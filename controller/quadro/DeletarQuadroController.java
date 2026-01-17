package controller.quadro;

import models.KanbanModel;
import view.quadro.DeletarQuadroView;
import view.Observer;

public class DeletarQuadroController implements Observer {

    private KanbanModel model;
    private DeletarQuadroView view;

    public void init(KanbanModel model, DeletarQuadroView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void deletar() {
        try {
            model.deletarQuadro();
            view.mostrarMensagemSucesso("Quadro deletado com sucesso!");
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg == null) {
                e.printStackTrace();
                view.mostrarMensagemErro("Erro inesperado (NullPointer). Verifique o console.");
            } else {
                view.mostrarMensagemErro("Erro: " + msg);
            }
        }
    }

    @Override
    public void update() {}
}