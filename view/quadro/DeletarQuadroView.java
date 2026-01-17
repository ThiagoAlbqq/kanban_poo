package view.quadro;

import controller.quadro.DeletarQuadroController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class DeletarQuadroView implements Observer {

    private KanbanModel model;
    private DeletarQuadroController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new DeletarQuadroController();
            controller.init(model, this);
            model.attachObserver(this);

            // UX: Pergunta antes de fazer a ação destrutiva
            String nomeQuadro = (model.getQuadroSelecionado() != null)
                    ? model.getQuadroSelecionado().getNome()
                    : "este quadro";

            if (Prompt.confirm("Tem certeza que deseja apagar o quadro '" + nomeQuadro + "'?")) {
                controller.deletar();
            }
        }
    }

    public void mostrarMensagemSucesso(String msg) {
        System.out.println(); // Pula linha pra ficar bonito
        Prompt.success(msg);
    }

    public void mostrarMensagemErro(String msg) {
        Prompt.error(msg);
    }

    @Override
    public void update() {
        // Nada a fazer aqui
    }
}