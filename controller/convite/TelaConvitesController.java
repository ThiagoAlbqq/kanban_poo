package controller.convite;

import models.KanbanModel;
import view.Observer;
import view.convite.TelaConvitesView;

public class TelaConvitesController implements Observer {

    private KanbanModel model;
    private TelaConvitesView view;

    public void init(KanbanModel model, TelaConvitesView view) {
        this.model = model;
        this.view = view;
    }

    public void handleEvent(String opcao) {
        try {
            int idConvite = Integer.parseInt(opcao);

            // Apenas verifica se o convite existe antes de abrir o menu
            // Seu model lança Exception se não achar, então usamos try/catch
            try {
                // Tentamos buscar só para validar se o ID é válido
                model.buscarConvitePorId(idConvite);

                // Se não deu erro, mostramos o menu
                String decisao = view.mostrarMenuResposta(String.valueOf(idConvite));
                processarDecisao(idConvite, decisao);

            } catch (IllegalArgumentException e) {
                view.mensagemErro(e.getMessage()); // "Convite não encontrado"
            }

        } catch (NumberFormatException e) {
            view.mensagemErro("Opção inválida. Digite apenas o ID numérico.");
        }
    }

    private void processarDecisao(int idConvite, String decisao) {
        try {
            switch (decisao.toUpperCase()) {
                case "A": // Aceitar
                    // true = aceitou
                    model.responderConvite(idConvite, true);
                    view.mensagemSucesso("Convite aceito! Time adicionado à sua lista.");
                    break;

                case "R": // Recusar
                    // false = recusou
                    model.responderConvite(idConvite, false);
                    view.mensagemSucesso("Convite recusado.");
                    break;

                case "V": // Voltar
                    break;

                default:
                    view.mensagemErro("Opção inválida.");
            }
        } catch (IllegalStateException e) {
            // Captura erros do model como "Este convite não é para você"
            view.mensagemErro(e.getMessage());
        }
    }

    public void sair() {
        // Nada específico para limpar
    }

    @Override
    public void update() {}
}