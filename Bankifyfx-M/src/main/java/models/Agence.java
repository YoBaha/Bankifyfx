package models;

public class Agence {
    private int id;
    private String nomAgence;
    private String emailAgence;
    private String telAgence;

    // Constructor
    public Agence(int id, String nomAgence, String emailAgence, String telAgence) {
        this.id = id;
        this.nomAgence = nomAgence;
        this.emailAgence = emailAgence;
        this.telAgence = telAgence;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomAgence() {
        return nomAgence;
    }

    public void setNomAgence(String nomAgence) {
        this.nomAgence = nomAgence;
    }

    public String getEmailAgence() {
        return emailAgence;
    }

    public void setEmailAgence(String emailAgence) {
        this.emailAgence = emailAgence;
    }

    public String getTelAgence() {
        return telAgence;
    }

    public void setTelAgence(String telAgence) {
        this.telAgence = telAgence;
    }
}
