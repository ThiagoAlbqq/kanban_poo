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
            // PASSO 1: Solicitar ID do Card
            view.solicitarIdCard();
            int idCard = view.getIdCard();

            // VERIFICAÇÃO: O Card existe?
            // (Você precisará de um método no Model que retorne o Card ou null)
            CardEntity card = model.buscarCardPorId(idCard);

            if (card == null) {
                view.failMensage("Card com ID " + idCard + " não encontrado.");
                return; // Para aqui
            }

            // PASSO 2: Preparar o terreno
            // Pega as colunas para mostrar as opções (ex: "1 - A Fazer", "2 - Doing")
            String[] listaColunas = model.listarColunas();

            // PASSO 3: Mostrar contexto na View
            view.mostrarContextoDeMovimentacao(
                    card.getTitle(),
                    card.getStatus(),
                    listaColunas
            );

            // PASSO 4: Solicitar Destino
            view.solicitarIdColuna();
            int idColunaDestino = view.getIdColuna();

            // PASSO 5: Executar
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