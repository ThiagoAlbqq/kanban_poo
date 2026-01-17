package models;

import java.io.Serializable;

public class ConviteEntity implements Serializable {

    private int id;
    private String remetenteEmail;
    private String destinatarioEmail;
    private int idTime;
    private String nomeTime;
    private String status;

    public ConviteEntity(int id, String remetente, String destinatario, int idTime, String nomeTime) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do convite deve ser positivo.");
        }

        if (idTime <= 0) {
            throw new IllegalArgumentException("ID do time deve ser positivo.");
        }

        this.id = id;
        this.idTime = idTime;

        setRemetenteEmail(remetente);
        setDestinatarioEmail(destinatario);
        setNomeTime(nomeTime);

        this.status = "PENDENTE";
    }

    public int getId() {
        return id;
    }

    /* ================= REMETENTE ================= */

    public String getRemetenteEmail() {
        return remetenteEmail;
    }

    private void setRemetenteEmail(String remetenteEmail) {
        validarEmail(remetenteEmail, "remetente");
        this.remetenteEmail = remetenteEmail.toLowerCase().trim();
    }

    /* ================= DESTINATÁRIO ================= */

    public String getDestinatarioEmail() {
        return destinatarioEmail;
    }

    public void setDestinatarioEmail(String destinatarioEmail) {
        validarEmail(destinatarioEmail, "destinatário");
        this.destinatarioEmail = destinatarioEmail.toLowerCase().trim();
    }

    /* ================= TIME ================= */

    public int getIdTime() {
        return idTime;
    }

    public String getNomeTime() {
        return nomeTime;
    }

    private void setNomeTime(String nomeTime) {
        if (nomeTime == null || nomeTime.isBlank()) {
            throw new IllegalArgumentException("Nome do time não pode ser vazio.");
        }

        if (nomeTime.length() > 30) {
            throw new IllegalArgumentException("Nome do time não pode exceder 30 caracteres.");
        }

        this.nomeTime = nomeTime.trim();
    }

    /* ================= STATUS ================= */

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status do convite não pode ser nulo.");
        }

        String statusNormalizado = status.trim().toUpperCase();

        if (!statusNormalizado.equals("PENDENTE")
                && !statusNormalizado.equals("ACEITO")
                && !statusNormalizado.equals("RECUSADO")) {
            throw new IllegalArgumentException("Status inválido para convite.");
        }

        this.status = statusNormalizado;
    }

    /* ================= REGRAS DE NEGÓCIO ================= */

    public boolean isPendente() {
        return "PENDENTE".equals(status);
    }

    /* ================= VALIDACOES ================= */

    private void validarEmail(String email, String tipo) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email do " + tipo + " não pode ser vazio.");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email do " + tipo + " inválido.");
        }
    }

    /* ================= TO STRING ================= */

    @Override
    public String toString() {
        return String.format(
                "Convite para o time '%s' enviado por %s [%s]",
                nomeTime,
                remetenteEmail,
                status
        );
    }
}
