package models;

import java.util.Date;
import javafx.scene.control.Button;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Date dateNaissance;
    private String genre;
    private boolean verified;
    private String role;
    private boolean isActive;
    private String picture;



    public Button supprimer;





    public User(int id, String nom, String prenom, String email, String password, Date dateNaissance, String genre, boolean verified,String role,boolean isActive,String picture) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.dateNaissance = dateNaissance;
        this.genre = genre;
        this.verified = verified;
        this.role = role;
        this.isActive = isActive;
        this.picture = picture;
    }
    public User(int id, String nom, String prenom, String email, Date dateNaissance, String genre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.genre = genre;

    }


    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public Button getSupprimer() {
        return supprimer;
    }

    public void setSupprimer(Button supprimer) {
        this.supprimer = supprimer;
    }

    public User() {
    }

    public User(String nom, String prenom, String email, String password, Date dateNaissance, String genre) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.dateNaissance = dateNaissance;
        this.genre = genre;
        this.verified = false;
    }

    public User(int id, String nom, String prenom, String email, String password, Date dateNaissance, String genre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.dateNaissance = dateNaissance;
        this.genre = genre;
        this.verified = false;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", date de naissance=" + dateNaissance +
                ", genre='" + genre + '\'' +
                '}';
    }





}
