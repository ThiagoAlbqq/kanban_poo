package view.convite;

import controller.convite.TelaConvitesController;
import models.KanbanModel;
import view.Observer;
import view.Prompt;

public class TelaConvitesView implements Observer {

    private KanbanModel model;
    private TelaConvitesController controller;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            this.controller = new TelaConvitesController();
            controller.init(model, this);
            model.attachObserver(this);
            mostrarMenu();
        }
    }

    public void mensagemSucesso(String msg) {
        System.out.println("");
        Prompt.success(msg);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    public void mensagemErro(String msg) {
        System.out.println("");
        Prompt.error(msg);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    public void mostrarMenu() {
        boolean sair = false;

        do {
            Prompt.clear();
            Prompt.header("NOTIFICAÇÕES & CONVITES");

            listarConvites(); // <--- AQUI VAI A LÓGICA DE PARSEAR A STRING

            Prompt.separator();
            System.out.println("[0] Voltar");
            System.out.println();

            String op = Prompt.input("Digite o ID do Convite para responder ou 0 para sair");

            if (op.equals("0")) {
                sair = true;
                controller.sair();
            } else {
                controller.handleEvent(op);
            }

        } while (!sair);
    }

    // --- ADAPTADO PARA O SEU MODEL ---
    private void listarConvites() {
        // Seu model retorna: "ID#NOME_TIME#REMETENTE"
        String[] convitesRaw = model.verificarMeusConvites();

        if (convitesRaw == null || convitesRaw.length == 0) {
            System.out.println("   (Nenhuma notificação nova)");
        } else {
            for (String c : convitesRaw) {
                try {
                    // Quebra a string nos separadores #
                    String[] partes = c.split("#");
                    // partes[0] = ID, partes[1] = Time, partes[2] = Email

                    System.out.printf("   [%s] Convite: Time '%s' (De: %s)%n", partes[0], partes[1], partes[2]);

                } catch (Exception e) {
                    System.out.println("   [Erro de formatação]: " + c);
                }
            }
        }
    }

    public String mostrarMenuResposta(String idConvite) {
        System.out.println("");
        System.out.println("   -----------------------------------");
        System.out.println("   RESPONDER CONVITE #" + idConvite);
        System.out.println("   -----------------------------------");
        System.out.println("   [A] Aceitar");
        System.out.println("   [R] Recusar");
        System.out.println("   [V] Voltar sem responder");
        System.out.println("");

        return Prompt.input("Sua escolha");
    }

    @Override
    public void update() {
        // Redesenha no loop
    }
}