package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class CardEntity implements Serializable {

    private int id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    private UsuarioEntity assignee;

    private CardPriority priority;

    public CardEntity(int id, String title, String description, CardPriority priority, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority != null ? priority : CardPriority.MEDIA;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public UsuarioEntity getAssignee() { return assignee; }
    public void setAssignee(UsuarioEntity assignee) {
        this.assignee = assignee;
    }

    public CardPriority getPriority() { return priority; }
    public void setPriority(CardPriority priority) { this.priority = priority; }

    public LocalDateTime getCreatedAt() { return createdAt; }

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