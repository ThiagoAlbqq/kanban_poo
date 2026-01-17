package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeEntity implements Serializable {

    private int id;
    private String name;
    private String description;

    private UsuarioEntity owner;

    private final List<UsuarioEntity> members = new ArrayList<>();
    private final List<QuadroEntity> boards = new ArrayList<>();

    public TimeEntity(int id, String name, String description, UsuarioEntity owner) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do time deve ser positivo.");
        }

        this.id = id;

        setName(name);
        setDescription(description);
        setOwner(owner);
    }

    /* ================= ID ================= */

    public int getId() {
        return id;
    }

    /* ================= NAME ================= */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome do time não pode ser vazio.");
        }

        if (name.length() > 40) {
            throw new IllegalArgumentException("O nome do time não pode exceder 40 caracteres.");
        }

        this.name = name.trim();
    }

    /* ================= DESCRIPTION ================= */

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            this.description = "";
            return;
        }

        if (description.length() > 255) {
            throw new IllegalArgumentException("A descrição não pode exceder 255 caracteres.");
        }

        this.description = description.trim();
    }

    /* ================= OWNER ================= */

    public UsuarioEntity getOwner() {
        return owner;
    }

    public void setOwner(UsuarioEntity owner) {
        if (owner == null) {
            throw new IllegalArgumentException("O líder do time não pode ser nulo.");
        }

        this.owner = owner;

        if (!members.contains(owner)) {
            members.add(owner);
        }
    }

    /* ================= MEMBERS ================= */

    public List<UsuarioEntity> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public void addMember(UsuarioEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("Não é possível adicionar um membro nulo.");
        }

        if (members.contains(user)) {
            throw new IllegalStateException("Usuário já é membro do time.");
        }

        members.add(user);
    }

    public void removeMember(UsuarioEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuário inválido.");
        }

        if (user.equals(owner)) {
            throw new IllegalStateException("Não é possível remover o dono do time.");
        }

        if (!members.remove(user)) {
            throw new IllegalStateException("Usuário não pertence a este time.");
        }
    }

    /* ================= BOARDS ================= */

    public List<QuadroEntity> getBoards() {
        return Collections.unmodifiableList(boards);
    }

    public void removeBoard(QuadroEntity quadro) {
        if (this.boards != null) {
            this.boards.remove(quadro);
        }
    }

    public void addBoard(QuadroEntity board) {
        if (board == null) {
            throw new IllegalArgumentException("Quadro inválido.");
        }

        if (boards.contains(board)) {
            throw new IllegalStateException("Este quadro já existe no time.");
        }

        boards.add(board);
    }

    /* ================= TO STRING ================= */

    @Override
    public String toString() {
        return String.format(
                "Time: %s (Dono: %s) - %d membros",
                name,
                owner.getUsername(),
                members.size()
        );
    }
}
