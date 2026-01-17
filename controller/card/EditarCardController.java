package controller.card;

import models.CardPriority;
import models.KanbanModel;
import view.card.EditarCardView;
import view.Observer;

public class EditarCardController implements Observer {

    private KanbanModel model;
    private EditarCardView view;

    private int id;
    private String titulo, desc;
    private int prioOp;

    public void init(KanbanModel model, EditarCardView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void editar() {
        view.solicitarId();
        view.solicitarTitulo();
        view.solicitarDescricao();
        view.solicitarPrioridade();

        try {
            this.id = view.getId();
            this.titulo = view.getTitulo();
            this.desc = view.getDesc();
            this.prioOp = view.getPrioOp();

            while(prioOp < 0 || prioOp > 3) {
                view.failMensage("Escolha um id valido");
                view.solicitarPrioridade();
                this.prioOp = view.getPrioOp();
            }

            CardPriority prio = null;
            if(prioOp != 0) {
                prio = CardPriority.MEDIA;
                if(prioOp == 1) prio = CardPriority.BAIXA;
                if(prioOp == 3) prio = CardPriority.ALTA;
            }

            model.editarCard(id, titulo, desc, prio);

            view.sucessMensage("Edição efetuada com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}

}
