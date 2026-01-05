package controller;

import models.KanbanModel;
import view.*;

public class TelaPrincipalController implements Observer {

    private KanbanModel model;
    private TelaPrincipalView view;

    public void init(KanbanModel model, TelaPrincipalView view) {
        this.model = model;
        this.view = view;
        model.attachObserver(this);
    }

    // Aquele método "Vite" que fizemos antes, agora integrado no login
    public void checarNotificacoesAoEntrar() {
        String[] convites = model.verificarMeusConvites();

        if (convites.length > 0) {
            Prompt.clear();
            Prompt.separator();
            System.out.println("🔔 VOCÊ TEM CONVITES PENDENTES");
            Prompt.separator();

            for (String c : convites) {
                String[] partes = c.split("#");
                int idConvite = Integer.parseInt(partes[0]);
                String nomeTime = partes[1];
                String quemMandou = partes[2];

                // Usa o Prompt bonito (Y/n)
                boolean aceitou = Prompt.confirm("Entrar no time '" + nomeTime + "' de " + quemMandou + "?");

                try {
                    model.responderConvite(idConvite, aceitou);
                    if (aceitou) Prompt.success("Você entrou no time!");
                    else Prompt.error("Convite recusado.");
                } catch (Exception e) {
                    Prompt.error(e.getMessage());
                }
            }
            // Pausa rápida para ler
            System.out.println("\nPressione Enter para ir ao Dashboard...");
            Prompt.scanner.nextLine();
        }
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
                checarNotificacoesAoEntrar();
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