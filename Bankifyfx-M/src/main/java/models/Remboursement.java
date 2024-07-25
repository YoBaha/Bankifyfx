package models;

import java.util.Date;

public class Remboursement {
    private int id,dureeRestante;
    private double montantR,montantRestant;
    private Date dateR;
    private Credit credit;

    public Remboursement() {
    }

    public Remboursement(int dureeRestante, double montantR, double montantRestant, Date dateR, Credit credit) {
        this.dureeRestante = dureeRestante;
        this.montantR = montantR;
        this.montantRestant = montantRestant;
        this.dateR = dateR;
        this.credit = credit;
    }

    public Remboursement(int id, int dureeRestante, double montantR, double montantRestant, Date dateR, Credit credit) {
        this.id = id;
        this.dureeRestante = dureeRestante;
        this.montantR = montantR;
        this.montantRestant = montantRestant;
        this.dateR = dateR;
        this.credit = credit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDureeRestante() {
        return dureeRestante;
    }

    public void setDureeRestante(int dureeRestante) {
        this.dureeRestante = dureeRestante;
    }

    public double getMontantR() {
        return montantR;
    }

    public void setMontantR(double montantR) {
        this.montantR = montantR;
    }

    public double getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(double montantRestant) {
        this.montantRestant = montantRestant;
    }

    public Date getDateR() {
        return dateR;
    }

    public void setDateR(Date dateR) {
        this.dateR = dateR;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Remboursement{" +
                "id=" + id +
                ", dureeRestante=" + dureeRestante +
                ", montantR=" + montantR +
                ", montantRestant=" + montantRestant +
                ", dateR=" + dateR +
                ", credit=" + credit +
                '}';
    }
}
