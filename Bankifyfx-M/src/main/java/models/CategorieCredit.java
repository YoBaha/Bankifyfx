package models;

public class CategorieCredit {
    private int id;
    private String nom;
    private double minMontant,maxMontant;

    public CategorieCredit() {
    }

    public CategorieCredit(String nom, double minMontant, double maxMontant) {
        this.nom = nom;
        this.minMontant = minMontant;
        this.maxMontant = maxMontant;
    }

    public CategorieCredit(int id,String nom, double minMontant, double maxMontant) {
        this.id = id;
        this.nom = nom;
        this.minMontant = minMontant;
        this.maxMontant = maxMontant;
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

    public double getMinMontant() {
        return minMontant;
    }

    public void setMinMontant(double minMontant) {
        this.minMontant = minMontant;
    }

    public double getMaxMontant() {
        return maxMontant;
    }

    public void setMaxMontant(double maxMontant) {
        this.maxMontant = maxMontant;
    }

    @Override
    public String toString() {
        return "CategorieCredit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", minMontant=" + minMontant +
                ", maxMontant=" + maxMontant +
                '}';
    }
}
