package controller;

import models.KanbanModel;
import view.*;
import view.convite.TelaConvitesView;
import view.time.CadastroTimeView;
import view.time.ListarTimesView;
import view.time.TelaTimeView;
import view.usuario.EditarUsuarioView;

public class TelaPrincipalController implements Observer {

    private KanbanModel model;
    private TelaPrincipalView view;

    public void init(KanbanModel model, TelaPrincipalView view) {
        this.model = model;
        this.view = view;
        model.attachObserver(this);
    }

    public void fazerLogout() {
        model.deslogarUsuario();
        Prompt.success("Logout realizado com sucesso.");
        // O loop do menu acaba e o programa volta (ou encerra) dependendo de onde foi chamado
    }

    public void handleEvent(String opcao) {
        switch (opcao) {
            case "1":
                new TelaTimeView().init(model);
                break;
            case "2":
                // Lógica rápida de criar time aqui mesmo ou chamar outra View
                new CadastroTimeView().init(model);
                break;
            case "3":
                // Chama a checagem novamente
                new TelaConvitesView().init(model);
                break;
            case "4":
                // Vai para edição de usuário (reaproveita sua view de edição)
                new EditarUsuarioView().init(model);
                break;
            default:
                Prompt.error("Opção inválida.");
                Prompt.scanner.nextLine();
        }
    }

    // Exemplo de criar time rápido usando o Prompt
    private void criarTimeRapido() {
        Prompt.header("CRIAR NOVO TIME");
        String nome = Prompt.input("Nome do Time");

        if (!nome.isEmpty()) {
            try {
                model.criarTime(nome);
                Prompt.success("Time '" + nome + "' criado!");
            } catch (Exception e) {
                Prompt.error(e.getMessage());
            }
        }
        System.out.println("Enter para voltar...");
        Prompt.scanner.nextLine();
    }

    @Override
    public void update() {}
}