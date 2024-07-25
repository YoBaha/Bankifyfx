package models.Cheques;

import java.util.Date;

public class Cheque {
    private int id;
    private int isFav;
    private int destinationCID;
    private int compteID;
    private float montantC;
    private Date dateEmission;

    // Constructors
    public Cheque() {
        this.isFav = 0; // Default value
        this.dateEmission = new Date(); // Default value as sysdate
    }

    public Cheque(int destinationCID, int compteID, float montantC) {
        this();
        this.destinationCID = destinationCID;
        this.compteID = compteID;
        this.montantC = montantC;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public int getDestinationCID() {
        return destinationCID;
    }

    public void setDestinationCID(int destinationCID) {
        this.destinationCID = destinationCID;
    }

    public int getCompteID() {
        return compteID;
    }

    public void setCompteID(int compteID) {
        this.compteID = compteID;
    }

    public float getMontantC() {
        return montantC;
    }

    public void setMontantC(float montantC) {
        this.montantC = montantC;
    }

    public Date getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }

    // toString method for debugging or logging
    @Override
    public String toString() {
        return "Cheque{" +
                "id=" + id +
                ", isFav=" + isFav +
                ", destinationCID=" + destinationCID +
                ", compteID=" + compteID +
                ", montantC=" + montantC +
                ", dateEmission=" + dateEmission +
                '}';
    }


}
