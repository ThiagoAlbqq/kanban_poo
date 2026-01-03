package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardEntity implements Serializable {

    private static final AtomicInteger count = new AtomicInteger(0);

    private int id;
    private String name;

    private List<ColumnEntity> columns = new ArrayList<>();

    public BoardEntity(String name) {
        this.id = count.incrementAndGet();
        this.name = name;

        // Opcional: Já iniciar com colunas padrão
        // initDefaultColumns();
    }

    public void addColumn(String columnName) {
        if(columnName == null || columnName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da coluna não pode ser vazio");
        }
        ColumnEntity newColumn = new ColumnEntity(columnName);
        columns.add(newColumn);
    }

    public void initDefaultColumns() {
        if (columns.isEmpty()) {
            addColumn("To Do (A Fazer)");
            addColumn("Doing (Fazendo)");
            addColumn("Done (Feito)");
        }
    }

    public void removeColumnById(int columnId) {
        boolean removed = columns.removeIf(col -> col.getId() == columnId);
        if (!removed) {
            System.out.println("Coluna com ID " + columnId + " não encontrada.");
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnEntity> getColumns() {
        return columns;
    }

    public void moveCard(int cardId, int fromColumnId, int toColumnId) {
        ColumnEntity fromColumn = findColumnById(fromColumnId);
        ColumnEntity toColumn = findColumnById(toColumnId);

        if (fromColumn == null || toColumn == null) {
            System.out.println("Erro: Coluna de origem ou destino não encontrada.");
            return;
        }

        CardEntity cardToMove = null;
        for (CardEntity card : fromColumn.getCards()) {
            if (card.getId() == cardId) {
                cardToMove = card;
                break;
            }
        }

        if (cardToMove == null) {
            System.out.println("Erro: Card " + cardId + " não está na coluna " + fromColumn.getName());
            return;
        }

        fromColumn.removeCard(cardToMove);
        toColumn.addCard(cardToMove);

        System.out.println("Sucesso: Card '" + cardToMove.getTitle() + "' movido de "
                + fromColumn.getName() + " para " + toColumn.getName());
    }

    private ColumnEntity findColumnById(int id) {
        for (ColumnEntity col : columns) {
            if (col.getId() == id) return col;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Quadro: %s (ID: %d) ===\n", name, id));

        if (columns.isEmpty()) {
            sb.append("   (Quadro vazio, adicione colunas)\n");
        } else {
            for (ColumnEntity col : columns) {
                sb.append(col.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}