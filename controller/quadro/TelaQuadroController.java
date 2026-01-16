package controller.quadro;

import models.KanbanModel;
import view.Observer;
import view.TelaQuadroView;
import view.card.CadastroCardView;
import view.card.MoverCardView;
import view.coluna.CadastroColunaView;

public class TelaQuadroController implements Observer {

    private KanbanModel model;
    private TelaQuadroView view;

    public void init(KanbanModel model, TelaQuadroView view) {
        this.model = model;
        this.view = view;
        this.model.attachObserver(this);
    }

    public void handleEvent(String opcao) {
        switch (opcao) {
            case "1":
                // Delega para a tela de criar card
                new CadastroCardView().init(model);
                break;

            case "2":
                // Delega para a tela de mover card
                new MoverCardView().init(model);
                break;

            case "3":
                // Delega para a tela de criar coluna
                new CadastroColunaView().init(model);
                break;

            default:
                view.mensagem("Opção inválida.");
        }
    }

    public void sair() {
        // Limpa a seleção ao voltar para o menu anterior
        model.selecionarQuadro(-1);
    }

    @Override
    public void update() {
        // Vazio: A View se redesenha sozinha no loop
    }
}