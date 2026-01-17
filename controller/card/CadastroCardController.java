package controller.card;

import models.CardPriority;
import models.KanbanModel;
import view.card.CadastroCardView;
import view.Observer;
import view.coluna.ListarColunasView;

public class CadastroCardController implements Observer {

    private KanbanModel model;
    private CadastroCardView view;

    private String titulo, desc;
    private int prioOp, idColuna;

    public void init(KanbanModel model, CadastroCardView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void cadastrar() {
        try {
            new ListarColunasView().init(model);

            view.solicitarIdColuna();

            this.idColuna = view.getIdColuna();
            model.selecionarColuna(idColuna);

            view.solicitarTitulo();
            view.solicitarDescricao();
            view.solicitarPrioridade();

            this.titulo = view.getTitulo();
            this.desc = view.getDesc();
            this.prioOp = view.getPrioOp();

            while(prioOp < 0 || prioOp > 3) {
                view.failMensage("Escolha um id valido");
                view.solicitarPrioridade();
                this.prioOp = view.getPrioOp();
            }

            CardPriority prio = CardPriority.MEDIA;
            if(prioOp == 1) prio = CardPriority.BAIXA;
            if(prioOp == 3) prio = CardPriority.ALTA;

            model.criarCardColuna(idColuna, titulo, desc, prio);

            view.sucessMensage("Cadastro efetuado com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }

    }

    @Override
    public void update() {}

}
