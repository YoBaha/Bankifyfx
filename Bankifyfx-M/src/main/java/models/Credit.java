package models;

import java.util.Date;

public class Credit {
    private int id,interet,dureeTotale;
    private double montantTotale;
    private Date dateC;
    private boolean payed,accepted;
    private CompteClient compte;
    private CategorieCredit categorie;

    public Credit() {
    }

    public Credit(int interet, int dureeTotale, double montantTotale, Date dateC, boolean payed, boolean accepted, CompteClient compte, CategorieCredit categorie) {
        this.interet = interet;
        this.dureeTotale = dureeTotale;
        this.montantTotale = montantTotale;
        this.dateC = dateC;
        this.payed = payed;
        this.accepted = accepted;
        this.compte = compte;
        this.categorie = categorie;
    }

    public Credit(int id, int interet, int dureeTotale, double montantTotale, Date dateC, boolean payed, boolean accepted, CompteClient compte, CategorieCredit categorie) {
        this.id = id;
        this.interet = interet;
        this.dureeTotale = dureeTotale;
        this.montantTotale = montantTotale;
        this.dateC = dateC;
        this.payed = payed;
        this.accepted = accepted;
        this.compte = compte;
        this.categorie = categorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInteret() {
        return interet;
    }

    public void setInteret(int interet) {
        this.interet = interet;
    }

    public int getDureeTotale() {
        return dureeTotale;
    }

    public void setDureeTotale(int dureeTotale) {
        this.dureeTotale = dureeTotale;
    }

    public double getMontantTotale() {
        return montantTotale;
    }

    public void setMontantTotale(double montantTotale) {
        this.montantTotale = montantTotale;
    }

    public Date getDateC() {
        return dateC;
    }

    public void setDateC(Date dateC) {
        this.dateC = dateC;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public CompteClient getCompte() {
        return compte;
    }

    public void setCompte(CompteClient compte) {
        this.compte = compte;
    }

    public CategorieCredit getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieCredit categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", interet=" + interet +
                ", dureeTotale=" + dureeTotale +
                ", montantTotale=" + montantTotale +
                ", dateC=" + dateC +
                ", payed=" + payed +
                ", accepted=" + accepted +
                ", compte=" + compte +
                ", categorie=" + categorie +
                '}';
    }
}
