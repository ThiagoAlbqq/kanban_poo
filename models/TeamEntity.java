package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TeamEntity implements Serializable {

    private int id;
    private String name;

    // Não foi criado/editado durante o CRUD
    private String description;

    private UsuarioEntity owner;

    private List<UsuarioEntity> members = new ArrayList<>();
    private List<BoardEntity> boards = new ArrayList<>();

    public TeamEntity(int id, String name, String description, UsuarioEntity owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;

        if (owner != null) {
            this.members.add(owner);
        }
    }

    public void addMember(UsuarioEntity user) {
        if(user != null && !members.contains(user)) {
            members.add(user);
        }
    }

    public void removeMember(UsuarioEntity user) {
        if (user.equals(this.owner)) {
            throw new IllegalStateException("Não é possível remover o dono do time.");
        }
        members.remove(user);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do time não pode ser vazio.");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode ser vazio.");
        }
        this.description = description;
    }

    public UsuarioEntity getOwner() {
        return owner;
    }

    public void setOwner(UsuarioEntity owner) {
        if(owner == null) {
            throw new IllegalArgumentException("O lider não pode ser nulo");
        };
        this.owner = owner;
        addMember(owner);
    }

    public List<UsuarioEntity> getMembers() {
        return members;
    }

    public void addBoard(BoardEntity board) {
        boards.add(board);
    }

    public List<BoardEntity> getBoards() {
        return boards;
    }

    @Override
    public String toString() {
        return String.format("Time: %s (Dono: %s) - %d membros",
                name,
                (owner != null ? owner.getUsername() : "Sem dono"),
                members.size());
    }
}