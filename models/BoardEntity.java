package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardEntity implements Serializable {

    private int id;
    private String nome;
    private List<CardEntity> cards;

    // NOVO: A lista de nomes das colunas (Ex: "TODO", "DOING", "QA", "DONE")
    private List<String> colunas;

    public BoardEntity(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.cards = new ArrayList<>();
        this.colunas = new ArrayList<>();

        // Colunas Padrão
        colunas.add("A FAZER");
        colunas.add("EM ANDAMENTO");
        colunas.add("CONCLUÍDO");
    }

    public List<String> getColunas() { return colunas; }

    public void adicionarColuna(String nome) {
        colunas.add(nome);
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<CardEntity> getCards() {
        return cards;
    }

    // --- Métodos Auxiliares de Lista ---

    public void addCard(CardEntity card) {
        this.cards.add(card);
    }

    public void removeCard(CardEntity card) {
        this.cards.remove(card);
    }

    @Override
    public String toString() {
        return String.format("%d - %s (%d cards)", id, nome, cards.size());
    }
}