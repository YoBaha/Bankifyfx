package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "carte")
public class Carte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String num_c;
    private Date date_exp;
    private String type_c;
    private String statut_c;

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private CompteClient compteClient;


    // Constructors
    public Carte() {
    }

    public Carte(String num_c, Date date_exp, String type_c, String statut_c, CompteClient compteClient) {
        this.num_c = num_c;
        this.date_exp = date_exp;
        this.type_c = type_c;
        this.statut_c = statut_c;
        this.compteClient = compteClient;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNum_c() {
        return num_c;
    }

    public void setNum_c(String num_c) {
        this.num_c = num_c;
    }

    public Date getDate_exp() {
        return date_exp;
    }

    public void setDate_exp(Date date_exp) {
        this.date_exp = date_exp;
    }

    public String getType_c() {
        return type_c;
    }

    public void setType_c(String type_c) {
        this.type_c = type_c;
    }

    public String getStatut_c() {
        return statut_c;
    }

    public void setStatut_c(String statut_c) {
        this.statut_c = statut_c;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public CompteClient getCompteClient() {
        return compteClient;
    }

    public void setCompteClient(CompteClient compteClient) {
        this.compteClient = compteClient;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Carte{" +
                "id=" + id +
                ", num_c='" + num_c + '\'' +
                ", date_exp=" + date_exp +
                ", type_c='" + type_c + '\'' +
                ", statut_c='" + statut_c + '\'' +
                ", compteClient='" + compteClient + '\'' +
                '}';
    }
}
