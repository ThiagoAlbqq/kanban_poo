package controller.card;

import models.CancelarException;
import models.CardEntity;
import models.KanbanModel;
import view.Observer;
import view.card.MoverCardView;

public class MoverCardController implements Observer {

    private KanbanModel model;
    private MoverCardView view;

    public void init(KanbanModel model, MoverCardView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void moverCard() {
        try {
            view.solicitarIdCard();
            int idCard = view.getIdCard();

            CardEntity card = model.buscarCardPorId(idCard);

            if (card == null) {
                view.failMensage("Card com ID " + idCard + " não encontrado.");
                return;
            }

            String[] listaColunas = model.listarColunas();

            view.mostrarContextoDeMovimentacao(
                    card.getTitle(),
                    card.getStatus(),
                    listaColunas
            );

            view.solicitarIdColuna();
            int idColunaDestino = view.getIdColuna();

            model.moverCardParaColuna(idCard, idColunaDestino);
            view.sucessMensage("Card '" + card.getTitle() + "' movido com sucesso!");

        } catch (CancelarException e) {
            view.failMensage("Operação cancelada.");
        } catch (RuntimeException e) {
            view.failMensage("Erro ao mover: " + e.getMessage());
        }
    }

    @Override
    public void update() {}
}