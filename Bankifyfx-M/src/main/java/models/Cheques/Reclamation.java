package models.Cheques;

import java.io.*;

public class Reclamation implements  Serializable {
    public int id;
    public int chequeId;
    public String categorie;
    public String statutR;

    // Constructors
    public Reclamation() {
        this.statutR = "En Cours"; // Default value
    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        // If you have nested non-serializable objects, serialize them here.
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        // If you have nested non-serializable objects, deserialize them here.
    }

    public Reclamation(int id, int chequeId, String categorie) {
        this.id = id;
        this.chequeId = chequeId;
        this.categorie = categorie;
        this.statutR = "En Cours"; // Default value
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChequeId() {
        return chequeId;
    }

    public void setChequeId(int chequeId) {
        this.chequeId = chequeId;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getStatutR() {
        return statutR;
    }

    public void setStatutR(String statutR) {
        this.statutR = statutR;
    }

    // toString method for debugging or logging
    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", chequeId=" + chequeId +
                ", categorie='" + categorie + '\'' +
                ", statutR='" + statutR + '\'' +
                '}';
    }
}
