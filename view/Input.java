package view;

import java.util.Scanner;

public class Input {
    public static final Scanner scanner = new Scanner(System.in);

    public static String lerString() {
        return scanner.nextLine();
    }

    public static int lerInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite um número válido.");
            return -1;
        }
    }
}