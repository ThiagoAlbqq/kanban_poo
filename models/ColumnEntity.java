package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ColumnEntity implements Serializable {

    private int id;
    private String name;

    private List<CardEntity> cards = new ArrayList<>();

    public ColumnEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da coluna não pode ser vazia.");
        }
        this.name = name;
    }

    public void addCard(CardEntity card) {
        if (card != null) {
            this.cards.add(card);
        }
    }

    public void removeCard(CardEntity card) {
        this.cards.remove(card);
    }

    public List<CardEntity> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  [Coluna: %s] - %d tarefas\n", getName(), cards.size()));
        for (CardEntity card : cards) {
            sb.append("    -> ").append(card.toString()).append("\n");
        }
        return sb.toString();
    }
}