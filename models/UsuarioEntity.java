package models;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsuarioEntity implements Serializable {

    private int id;
    private String username;
    private String email;
    private String passwordHash;

    public UsuarioEntity(int id, String username, String email, String password) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do usuário deve ser positivo.");
        }

        this.id = id;
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    /* ================= ID ================= */

    public int getId() {
        return id;
    }

    /* ================= USERNAME ================= */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("O username não pode ser vazio.");
        }

        if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("O username deve ter entre 3 e 20 caracteres.");
        }

        this.username = username.trim();
    }

    /* ================= EMAIL ================= */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio.");
        }

        String emailNormalizado = email.trim().toLowerCase();

        if (!emailNormalizado.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido.");
        }

        this.email = emailNormalizado;
    }

    /* ================= PASSWORD ================= */

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("A senha não pode ser vazia.");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        this.passwordHash = hashPassword(password);
    }

    public boolean checkPassword(String passwordInput) {
        if (passwordInput == null) {
            return false;
        }

        return passwordHash.equals(hashPassword(passwordInput));
    }

    /* ================= HASH ================= */

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            return String.format("%064x", new BigInteger(1, encodedHash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash de senha", e);
        }
    }

    /* ================= TO STRING ================= */

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Usuário: %s | Email: %s",
                id,
                username,
                email
        );
    }
}
