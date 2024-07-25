package models;

public class Assurance {
    private int idAssurance;
    private String typeAssurance;
    private double montantPrime;
    private String nomAssure;
    private String nomBeneficiaire;
    private String infoAssurance;

    // Constructors
    public Assurance(int idAssurance, String typeAssurance, double montantPrime, String nomAssure, String nomBeneficiaire, String infoAssurance) {
        this.idAssurance = idAssurance;
        this.typeAssurance = typeAssurance;
        this.montantPrime = montantPrime;
        this.nomAssure = nomAssure;
        this.nomBeneficiaire = nomBeneficiaire;
        this.infoAssurance = infoAssurance;
    }

    // Getters and Setters
    public int getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(int idAssurance) {
        this.idAssurance = idAssurance;
    }

    public String getTypeAssurance() {
        return typeAssurance;
    }

    public void setTypeAssurance(String typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    public double getMontantPrime() {
        return montantPrime;
    }

    public void setMontantPrime(double montantPrime) {
        this.montantPrime = montantPrime;
    }

    public String getNomAssure() {
        return nomAssure;
    }

    public void setNomAssure(String nomAssure) {
        this.nomAssure = nomAssure;
    }

    public String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    public void setNomBeneficiaire(String nomBeneficiaire) {
        this.nomBeneficiaire = nomBeneficiaire;
    }

    public String getInfoAssurance() {
        return infoAssurance;
    }

    public void setInfoAssurance(String infoAssurance) {
        this.infoAssurance = infoAssurance;
    }
}
