package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColunaEntity implements Serializable {

    private int id;
    private String name;

    private final List<CardEntity> cards = new ArrayList<>();

    public ColunaEntity(int id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da coluna deve ser positivo.");
        }

        this.id = id;
        setName(name);
    }

    public int getId() {
        return id;
    }

    /* ================= NAME ================= */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome da coluna não pode ser vazio.");
        }

        if (name.length() > 30) {
            throw new IllegalArgumentException("O nome da coluna não pode exceder 30 caracteres.");
        }

        this.name = name.trim();
    }

    /* ================= CARDS ================= */

    public void addCard(CardEntity card) {
        if (card == null) {
            throw new IllegalArgumentException("Não é possível adicionar um card nulo.");
        }

        if (cards.contains(card)) {
            throw new IllegalStateException("Este card já está presente na coluna.");
        }

        cards.add(card);
    }

    public void removeCard(CardEntity card) {
        if (card == null) {
            throw new IllegalArgumentException("Não é possível remover um card nulo.");
        }

        if (!cards.remove(card)) {
            throw new IllegalStateException("O card não existe nesta coluna.");
        }
    }

    public List<CardEntity> getCards() {
        return Collections.unmodifiableList(cards);
    }

    /* ================= TO STRING ================= */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(
                "  [Coluna: %s] - %d tarefas\n",
                name,
                cards.size()
        ));

        for (CardEntity card : cards) {
            sb.append("    -> ").append(card).append("\n");
        }

        return sb.toString();
    }
}
