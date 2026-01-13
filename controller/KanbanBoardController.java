package controller;

import models.CardPriority;
import models.KanbanModel;
import view.*;

public class KanbanBoardController implements Observer {

    private KanbanModel model;
    private KanbanBoardView view;

    public void init(KanbanModel model, KanbanBoardView view) {
        this.model = model;
        this.view = view;
        model.attachObserver(this);
    }

    public void criarCard() {
        view.limparTela();
        Prompt.header("NOVA TAREFA");

        String titulo = Prompt.input("Título");
        String desc = Prompt.input("Descrição");

        System.out.println("Prioridade: [1] Baixa | [2] Média | [3] Alta");
        int prioOp = Prompt.inputInt("Opção");
        CardPriority prio = CardPriority.MEDIA;
        if(prioOp == 1) prio = CardPriority.BAIXA;
        if(prioOp == 3) prio = CardPriority.ALTA;

        try {
            model.criarCard(titulo, desc, prio);
            Prompt.success("Card criado com sucesso!");
        } catch (Exception e) {
            Prompt.error(e.getMessage());
        }
        view.pausa();
    }

    public void criarColuna() {
        view.limparTela();
        Prompt.header("ADICIONAR NOVA COLUNA");

        System.out.println("Ex: 'QA', 'Revisão', 'Arquivado'");
        String nome = Prompt.input("Nome da Coluna");

        try {
            model.criarColuna(nome);
            Prompt.success("Coluna '" + nome.toUpperCase() + "' adicionada!");
        } catch (Exception e) {
            Prompt.error(e.getMessage());
        }
        view.pausa();
    }

    public void moverCard() {
        int idCard = Prompt.inputInt("Digite o ID do Card para mover");

        try {
            model.moverCard(idCard);
            Prompt.success("Card movido de coluna!");
        } catch (Exception e) {
            Prompt.error(e.getMessage());
        }
        view.pausa();
    }

    @Override
    public void update() {
    }
}