package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CardEntity implements Serializable {

    private int id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    private UsuarioEntity assignee;
    private CardPriority priority;

    public CardEntity(int id, String title, String description, CardPriority priority, String status) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do card deve ser positivo.");
        }

        this.id = id;
        this.createdAt = LocalDateTime.now();

        setTitle(title);
        setDescription(description);
        setPriority(priority);
        setStatus(status);
    }

    public int getId() {
        return id;
    }

    /* ================= TITLE ================= */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Título do card não pode ser vazio.");
        }

        if (title.length() > 50) {
            throw new IllegalArgumentException("Título do card não pode exceder 50 caracteres.");
        }

        this.title = title.trim();
    }

    /* ================= DESCRIPTION ================= */

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            this.description = "";
            return;
        }

        if (description.length() > 255) {
            throw new IllegalArgumentException("Descrição do card não pode exceder 255 caracteres.");
        }

        this.description = description.trim();
    }

    /* ================= STATUS ================= */

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status do card não pode ser vazio.");
        }

        this.status = status.trim().toUpperCase();
    }

    /* ================= ASSIGNEE ================= */

    public UsuarioEntity getAssignee() {
        return assignee;
    }

    public void setAssignee(UsuarioEntity assignee) {
        this.assignee = assignee;
        // null é permitido → card sem responsável
    }

    /* ================= PRIORITY ================= */

    public CardPriority getPriority() {
        return priority;
    }

    public void setPriority(CardPriority priority) {
        this.priority = (priority != null) ? priority : CardPriority.MEDIA;
    }

    /* ================= CREATED AT ================= */

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /* ================= TO STRING ================= */

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        return String.format(
                "  [Card #%d] %s | Desc: %s | Prioridade: %s | Resp: %s | Criado em: %s",
                id,
                title,
                description,
                priority,
                (assignee != null ? assignee.getUsername() : "Ninguém"),
                createdAt.format(fmt)
        );
    }
}
