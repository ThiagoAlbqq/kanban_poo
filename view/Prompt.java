package view;

import models.CancelarException;
import java.util.Scanner;

public class Prompt {

    // Cores pra deixar o terminal mais visual
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String BOLD = "\u001B[1m";

    public static final Scanner scanner = new Scanner(System.in);

    // Tenta limpar o console
    public static void clear() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }

    public static void separator() {
        System.out.println(CYAN + "--------------------------------------------------" + RESET);
    }

    public static void header(String titulo) {
        separator();
        System.out.println(CYAN + " » " + BOLD + titulo.toUpperCase() + RESET);
        separator();
    }

    public static void menuItem(String atalho, String descricao) {
        System.out.println(CYAN + " [" + atalho + "] " + RESET + descricao);
    }

    public static String input(String texto) {
        System.out.print(BOLD + "> " + texto + ": " + RESET);
        String entrada = scanner.nextLine().trim();

        // Atalho de fuga em qualquer momento do sistema
        if (entrada.equalsIgnoreCase("sair")) {
            throw new CancelarException();
        }
        return entrada;
    }

    // Garante que o usuário digite um número
    public static int inputInt(String texto) {
        while (true) {
            String entrada = input(texto);
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Digite um número válido." + RESET);
            }
        }
    }

    public static void success(String msg) {
        System.out.println(GREEN + "✔ " + msg + RESET);
        aguardarEnter();
    }

    public static void error(String msg) {
        System.out.println(RED + "✖ " + msg + RESET);
        aguardarEnter();
    }

    // Pausa pro usuário ler o que aconteceu antes de limpar a tela
    private static void aguardarEnter() {
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
    }

    // Usariamos isso no convite
    public static boolean confirm(String pergunta) {
        while (true) {
            System.out.print(CYAN + "? " + RESET + BOLD + pergunta + RESET + " (Y/n) ");
            String entrada = scanner.nextLine().trim().toLowerCase();

            if (entrada.equalsIgnoreCase("sair")) throw new CancelarException();

            // Enter vazio conta como Sim (padrão de CLI)
            if (entrada.isEmpty() || entrada.equals("y") || entrada.equals("s")) {
                return true;
            } else if (entrada.equals("n")) {
                return false;
            }
            // Se digitar outra coisa, o loop roda de novo
        }
    }
}