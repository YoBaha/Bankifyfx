package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;
    private Date date_t;
    private String type_t;
    private String statut_t;

    @ManyToOne
    @JoinColumn(name = "carte_id")
    private Carte carte;


    // Constructors
    public Transaction() {
    }

    public Transaction(double montant, Date date_t, String type_t, String statut_t, Carte carte) {
        this.montant = montant;
        this.date_t = date_t;
        this.type_t = type_t;
        this.statut_t = statut_t;
        this.carte = carte;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDate_t() {
        return date_t;
    }

    public void setDate_t(Date date_t) {
        this.date_t = date_t;
    }

    public String getType_t() {
        return type_t;
    }

    public void setType_t(String type_t) {
        this.type_t = type_t;
    }

    public String getStatut_t() {
        return statut_t;
    }

    public void setStatut_t(String statut_t) {
        this.statut_t = statut_t;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }


    // toString method for debugging
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", montant=" + montant +
                ", date_t=" + date_t +
                ", type_t='" + type_t + '\'' +
                ", statut_t='" + statut_t + '\'' +
                ", carte=" + carte +
                '}';
    }
}
