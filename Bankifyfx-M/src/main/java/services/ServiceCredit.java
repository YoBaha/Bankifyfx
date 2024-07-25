package services;

import models.CategorieCredit;
import models.CompteClient;
import models.Credit;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceCredit implements IService1<Credit> {
    Connection connection;

    public ServiceCredit() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Credit credit) throws SQLException {
        String req = "INSERT INTO credit (interet, duree_totale, montant_totale, date_c, payed, accepted, compte_id, categorie_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, credit.getInteret());
        preparedStatement.setInt(2, credit.getDureeTotale());
        preparedStatement.setDouble(3, credit.getMontantTotale());
        preparedStatement.setDate(4, new java.sql.Date(credit.getDateC().getTime()));
        preparedStatement.setBoolean(5, credit.isPayed());
        preparedStatement.setBoolean(6, credit.isAccepted());
        preparedStatement.setInt(7, credit.getCompte().getId());
        preparedStatement.setInt(8, credit.getCategorie().getId());
        preparedStatement.executeUpdate();
        System.out.println("Crédit ajouté");
    }

    @Override
    public void edit(Credit credit) throws SQLException {
        String req = "UPDATE credit SET interet=?, duree_totale=?, montant_totale=?, date_c=?, payed=?, accepted=?, compte_id=?, categorie_id=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, credit.getInteret());
        preparedStatement.setInt(2, credit.getDureeTotale());
        preparedStatement.setDouble(3, credit.getMontantTotale());
        preparedStatement.setDate(4, new java.sql.Date(credit.getDateC().getTime()));
        preparedStatement.setBoolean(5, credit.isPayed());
        preparedStatement.setBoolean(6, credit.isAccepted());
        preparedStatement.setInt(7, credit.getCompte().getId());
        preparedStatement.setInt(8, credit.getCategorie().getId());
        preparedStatement.setInt(9, credit.getId());
        preparedStatement.executeUpdate();
        System.out.println("Crédit modifié");
    }

    @Override
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM credit WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Crédit supprimé");
    }

    @Override
    public List<Credit> getAll() throws SQLException {
        List<Credit> credits = new ArrayList<>();
        String req = "SELECT * FROM credit";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);

        while (rs.next()) {
            int id = rs.getInt("id");
            int interet = rs.getInt("interet");
            int dureeTotale = rs.getInt("duree_totale");
            double montantTotale = rs.getDouble("montant_totale");
            Date dateC = rs.getDate("date_c");
            boolean payed = rs.getBoolean("payed");
            boolean accepted = rs.getBoolean("accepted");
            int compteId = rs.getInt("compte_id");
            CompteClient compte = new ServiceCompteClient().getById(compteId);
            int categorieId = rs.getInt("categorie_id");
            CategorieCredit categorie = new ServiceCategorieCredit().getById(categorieId);
            Credit credit = new Credit(id, interet, dureeTotale, montantTotale, dateC, payed, accepted, compte, categorie);
            credits.add(credit);
        }
        return credits;
    }

    @Override
    public Credit getById(int id) throws SQLException {
        String req = "SELECT * FROM credit WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            int interet = rs.getInt("interet");
            int dureeTotale = rs.getInt("duree_totale");
            double montantTotale = rs.getDouble("montant_totale");
            Date dateC = rs.getDate("date_c");
            boolean payed = rs.getBoolean("payed");
            boolean accepted = rs.getBoolean("accepted");
            int compteId = rs.getInt("compte_id");
            CompteClient compte = new ServiceCompteClient().getById(compteId);
            int categorieId = rs.getInt("categorie_id");
            CategorieCredit categorie = new ServiceCategorieCredit().getById(categorieId);

            return new Credit(id, interet, dureeTotale, montantTotale, dateC, payed, accepted, compte, categorie);
        } else {
            return null;
        }
    }

    public boolean clientExiste(int id) throws SQLException {
        String req = "SELECT * FROM credit WHERE compte_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    public void accept(int id) throws SQLException {
        String req = "UPDATE credit SET accepted=1 WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1,id);
        preparedStatement.executeUpdate();
        System.out.println("Crédit accepté");
    }

    public List<Credit> getCreditsAttente() throws SQLException {
        List<Credit> credits = new ArrayList<>();
        String req = "SELECT * FROM credit WHERE accepted=0";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            int id = rs.getInt("id");
            int interet = rs.getInt("interet");
            int dureeTotale = rs.getInt("duree_totale");
            double montantTotale = rs.getDouble("montant_totale");
            Date dateC = rs.getDate("date_c");
            boolean payed = rs.getBoolean("payed");
            boolean accepted = rs.getBoolean("accepted");
            int compteId = rs.getInt("compte_id");
            CompteClient compte = new ServiceCompteClient().getById(compteId);
            int categorieId = rs.getInt("categorie_id");
            CategorieCredit categorie = new ServiceCategorieCredit().getById(categorieId);
            Credit credit = new Credit(id, interet, dureeTotale, montantTotale, dateC, payed, accepted, compte, categorie);
            credits.add(credit);
        }
        return credits;
    }

    public List<Credit> getCreditsAccepted() throws SQLException {
        List<Credit> credits = new ArrayList<>();
        String req = "SELECT * FROM credit WHERE accepted=1";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            int id = rs.getInt("id");
            int interet = rs.getInt("interet");
            int dureeTotale = rs.getInt("duree_totale");
            double montantTotale = rs.getDouble("montant_totale");
            Date dateC = rs.getDate("date_c");
            boolean payed = rs.getBoolean("payed");
            boolean accepted = rs.getBoolean("accepted");
            int compteId = rs.getInt("compte_id");
            CompteClient compte = new ServiceCompteClient().getById(compteId);
            int categorieId = rs.getInt("categorie_id");
            CategorieCredit categorie = new ServiceCategorieCredit().getById(categorieId);
            Credit credit = new Credit(id, interet, dureeTotale, montantTotale, dateC, payed, accepted, compte, categorie);
            credits.add(credit);
        }
        return credits;
    }

    public List<Credit> getCreditsPayed() throws SQLException {
        List<Credit> credits = new ArrayList<>();
        String req = "SELECT * FROM credit WHERE payed=1";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            int id = rs.getInt("id");
            int interet = rs.getInt("interet");
            int dureeTotale = rs.getInt("duree_totale");
            double montantTotale = rs.getDouble("montant_totale");
            Date dateC = rs.getDate("date_c");
            boolean payed = rs.getBoolean("payed");
            boolean accepted = rs.getBoolean("accepted");
            int compteId = rs.getInt("compte_id");
            CompteClient compte = new ServiceCompteClient().getById(compteId);
            int categorieId = rs.getInt("categorie_id");
            CategorieCredit categorie = new ServiceCategorieCredit().getById(categorieId);
            Credit credit = new Credit(id, interet, dureeTotale, montantTotale, dateC, payed, accepted, compte, categorie);
            credits.add(credit);
        }
        return credits;
    }

    public Credit getByClient(int clientId) throws SQLException {
        String req = "SELECT * FROM credit WHERE compte_id=? ;";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, clientId);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            int interet = rs.getInt("interet");
            int dureeTotale = rs.getInt("duree_totale");
            double montantTotale = rs.getDouble("montant_totale");
            Date dateC = rs.getDate("date_c");
            boolean payed = rs.getBoolean("payed");
            boolean accepted = rs.getBoolean("accepted");
            int id = rs.getInt("id");
            CompteClient compte = new ServiceCompteClient().getById(clientId);
            int categorieId = rs.getInt("categorie_id");
            CategorieCredit categorie = new ServiceCategorieCredit().getById(categorieId);

            return new Credit(id, interet, dureeTotale, montantTotale, dateC, payed, accepted, compte, categorie);
        } else {
            return null;
        }
    }
}
