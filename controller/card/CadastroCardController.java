package controller.card;

import models.CardPriority;
import models.KanbanModel;
import view.card.CadastroCardView;
import view.Observer;

public class CadastroCardController implements Observer {

    private KanbanModel model;
    private CadastroCardView view;

    private String titulo, desc;
    private int prioOp;

    public void init(KanbanModel model, CadastroCardView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void cadastrar() {
        view.solicitarTitulo();
        view.solicitarDescricao();
        view.solicitarPrioridade();

        try {
            this.titulo = view.getTitulo();
            this.desc = view.getDesc();
            this.prioOp = view.getPrioOp();

            CardPriority prio = CardPriority.MEDIA;
            if(prioOp == 1) prio = CardPriority.BAIXA;
            if(prioOp == 3) prio = CardPriority.ALTA;

            model.criarCard(titulo, desc, prio);

            view.sucessMensage("Cadastro efetuado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}

}
