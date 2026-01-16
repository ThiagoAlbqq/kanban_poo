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
        this.id = id;
        this.remetenteEmail = remetente;
        this.destinatarioEmail = destinatario;
        this.idTime = idTime;
        this.nomeTime = nomeTime;
        this.status = "PENDENTE";
    }

    // Getters e Setters necessários
    public int getId() { return id; }
    public String getDestinatarioEmail() { return destinatarioEmail; }
    public void setDestinatarioEmail(String destinatario) { this.destinatarioEmail = destinatario; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getIdTime() { return idTime; }
    public String getRemetenteEmail() { return remetenteEmail; }
    public String getNomeTime() { return nomeTime; }

    @Override
    public String toString() {
        return "Convite para o time '" + nomeTime + "' enviado por " + remetenteEmail;
    }
}