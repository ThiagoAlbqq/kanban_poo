package view;

import models.CancelarException;

import java.util.Scanner;

public class Prompt {

    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BOLD = "\u001B[1m";

    public static final Scanner scanner = new Scanner(System.in);

    public static void clear() {
        try {
            String sistema = System.getProperty("os.name").toLowerCase();

            if (sistema.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception excecao) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
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

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("sair")) {
            throw new CancelarException();
        }

        return input;
    }

    public static int inputInt(String texto) {
        System.out.print(BOLD + "> " + texto + ": " + RESET);

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("sair")) {
            throw new CancelarException();
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void success(String msg) {
        System.out.println(GREEN + "✔ " + msg + RESET);
    }

    public static void error(String msg) {
        System.out.println(RED + "✖ " + msg + RESET);
    }

    public static boolean confirm(String pergunta) {
        while (true) {
            System.out.print(CYAN + "? " + RESET + BOLD + pergunta + RESET + " (Y/n) ");

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equalsIgnoreCase("sair")) {
                throw new CancelarException();
            }

            if (input.isEmpty() || input.equals("y") || input.equals("s")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }

        }
    }
}