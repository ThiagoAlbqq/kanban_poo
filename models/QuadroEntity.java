package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuadroEntity implements Serializable {

    private int id;
    private String nome;

    private final List<CardEntity> cards = new ArrayList<>();
    private final List<ColunaEntity> colunas = new ArrayList<>();

    public QuadroEntity(int id, String nome) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do quadro deve ser positivo.");
        }

        this.id = id;
        setNome(nome);

        // Colunas padrão do Kanban
        colunas.add(new ColunaEntity(1, "A FAZER"));
        colunas.add(new ColunaEntity(2, "EM ANDAMENTO"));
        colunas.add(new ColunaEntity(3, "CONCLUÍDO"));
    }

    /* ================= ID & NOME ================= */

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do quadro não pode ser vazio.");
        }

        if (nome.length() > 40) {
            throw new IllegalArgumentException("Nome do quadro não pode exceder 40 caracteres.");
        }

        this.nome = nome.trim();
    }

    /* ================= COLUNAS ================= */

    public List<ColunaEntity> getColunas() {
        return Collections.unmodifiableList(colunas);
    }

    public void adicionarColuna(int id, String nome) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da coluna deve ser positivo.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da coluna não pode ser vazio.");
        }

        boolean jaExiste = colunas.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(nome.trim()));

        if (jaExiste) {
            throw new IllegalStateException("Já existe uma coluna com este nome.");
        }

        colunas.add(new ColunaEntity(id, nome));
    }

    public ColunaEntity getColunaPorNome(String nome) {
        return colunas.stream()
                .filter(c -> c.getName().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Coluna não encontrada."));
    }

    /* ================= CARDS ================= */

    public List<CardEntity> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void addCard(CardEntity card) {
        if (card == null) {
            throw new IllegalArgumentException("Não é possível adicionar um card nulo.");
        }

        if (cards.contains(card)) {
            throw new IllegalStateException("Este card já existe no quadro.");
        }

        cards.add(card);
    }

    public void removeCard(CardEntity card) {
        if (card == null) {
            throw new IllegalArgumentException("Não é possível remover um card nulo.");
        }

        if (!cards.remove(card)) {
            throw new IllegalStateException("O card não existe no quadro.");
        }
    }

    /* ================= TO STRING ================= */

    @Override
    public String toString() {
        return String.format(
                "%d - %s (%d cards, %d colunas)",
                id,
                nome,
                cards.size(),
                colunas.size()
        );
    }
}
