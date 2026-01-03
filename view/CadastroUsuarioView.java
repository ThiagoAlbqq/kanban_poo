package view;

import controller.CadastroUsuarioController;
import models.KanbanModel;
import java.util.Scanner;

public class CadastroUsuarioView implements Observer {

    private String nome, email, senha1, senha2;
    private String opcao = "0";

    private KanbanModel model;
    private CadastroUsuarioController controller;
    public Scanner sc = Input.scanner;

    public void init(KanbanModel model) {
        if (model != null) {
            this.model = model;
            controller = new CadastroUsuarioController();
            controller.init(model, this);
            model.attachObserver(this);

            controller.cadastrar();
        }
    }

    public void solicitarNome() {
        System.out.println("\n--- NOVO CADASTRO ---");
        System.out.print("Nome: ");
        nome = sc.nextLine();
    }

    public void solicitarEmail() {
        System.out.print("Email: ");
        email = sc.nextLine();
    }

    public void solicitarSenha() {
        System.out.print("Senha (min 6 digitos): ");
        senha1 = sc.nextLine();
        System.out.print("Confirmar senha: ");
        senha2 = sc.nextLine();
    }

    // --- Getters para o Controller usar (Igual ao dela) ---
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha1() { return senha1; }
    public String getSenha2() { return senha2; }

    public void mensagem(String msg) {
        System.out.println(">> " + msg);
        System.out.println();
    }

    public void opcao() {
        System.out.println("1 - Voltar ao Menu Principal");
        System.out.println("2 - Novo cadastro");
        System.out.println();
        System.out.print("Opcao: ");
        opcao = sc.nextLine();
        controller.handleEvent(opcao);
    }

    @Override
    public void update() {
    }
}