package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "virement")
public class Virement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String compte_source;
    private String compte_destination;
    private float montant;
    private Date date;
    private Time heure;

    // Constructors
    public Virement() {
    }

    public Virement(String compte_source, String compte_destination, float montant, Date date, Time heure) {
        this.compte_source = compte_source;
        this.compte_destination = compte_destination;
        this.montant = montant;
        this.date = date;
        this.heure = heure;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompte_source() {
        return compte_source;
    }

    public void setCompte_source(String compte_source) {
        this.compte_source = compte_source;
    }

    public String getCompte_destination() {
        return compte_destination;
    }

    public void setCompte_destination(String compte_destination) {
        this.compte_destination = compte_destination;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getHeure() {
        return heure;
    }

    public void setHeure(Time heure) {
        this.heure = heure;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Virement{" +
                "id=" + id +
                ", compte_source='" + compte_source + '\'' +
                ", compte_destination='" + compte_destination + '\'' +
                ", montant=" + montant +
                ", date=" + date +
                ", heure=" + heure +
                '}';
    }
}
