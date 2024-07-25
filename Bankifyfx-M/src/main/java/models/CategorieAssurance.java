package models;

public class CategorieAssurance {
    private int idCategorie; // Assuming this is an auto-increment primary key
    private String nomCategorie;
    private String description;
    private String typeCouverture;
    private String agenceResponsable;

    // Constructor
    public CategorieAssurance(int idCategorie, String nomCategorie, String description, String typeCouverture, String agenceResponsable) {
        this.idCategorie = idCategorie;
        this.nomCategorie = nomCategorie;
        this.description = description;
        this.typeCouverture = typeCouverture;
        this.agenceResponsable = agenceResponsable;
    }

    // Getters and Setters
    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeCouverture() {
        return typeCouverture;
    }

    public void setTypeCouverture(String typeCouverture) {
        this.typeCouverture = typeCouverture;
    }

    public String getAgenceResponsable() {
        return agenceResponsable;
    }

    public void setAgenceResponsable(String agenceResponsable) {
        this.agenceResponsable = agenceResponsable;
    }
}
