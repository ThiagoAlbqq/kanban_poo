package models;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class UsuarioEntity implements Serializable {

    private static final AtomicInteger count = new AtomicInteger(0);

    private int id;
    private String username;
    private String email;
    private String passwordHash;

    public UsuarioEntity(String username, String email, String password) {
        this.id = count.incrementAndGet();
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("O username não pode ser vazio.");
        }
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }
        this.passwordHash = hashPassword(password);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, encodedhash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash de senha", e);
        }
    }

    public boolean checkPassword(String passwordInput) {
        String inputHash = hashPassword(passwordInput);
        return this.passwordHash.equals(inputHash);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Usuario: %s | Email: %s", id, username, email);
    }
}