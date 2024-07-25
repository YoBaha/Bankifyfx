package services;

import models.CategorieCredit;
import models.Credit;
import javafx.scene.chart.*;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategorieCredit implements IService1<CategorieCredit>{
    Connection connection;
    public ServiceCategorieCredit(){
        connection= MyDatabase.getInstance().getConnection();

    }
    @Override
    public void add(CategorieCredit categorie) throws SQLException {
        String req="insert into categorie_credit (nom,min_montant,max_montant)"+
                "values('"+categorie.getNom()+"','"+categorie.getMinMontant()+"',"+categorie.getMaxMontant()+")";

        Statement statement=connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("categorie ajoute");
    }

    @Override
    public void edit(CategorieCredit categorie) throws SQLException {
        String req="update categorie_credit set nom=?,min_montant=?,max_montant=?  where id=?";
        PreparedStatement preparedStatement= connection.prepareStatement(req);
        preparedStatement.setString(1, categorie.getNom());
        preparedStatement.setDouble(2,categorie.getMinMontant());
        preparedStatement.setDouble(3,categorie.getMaxMontant());
        preparedStatement.setInt(4,categorie.getId());
        preparedStatement.executeUpdate();
        System.out.println("categorie modifie");
    }

    @Override
    public void delete(int id) throws SQLException {
        String req="delete from categorie_credit where id="+id+";";
        Statement statement=connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("categorie supprime");
    }

    @Override
    public List<CategorieCredit> getAll() throws SQLException {
        List<CategorieCredit> categories= new ArrayList<>();
        String req="select * from categorie_credit";
        Statement statement= connection.createStatement();
        ResultSet rs= statement.executeQuery(req);

        while (rs.next()){
            CategorieCredit categorie= new CategorieCredit();
            categorie.setNom(rs.getString("nom"));
            categorie.setMinMontant(rs.getDouble("min_montant"));
            categorie.setMaxMontant(rs.getDouble("max_montant"));
            categorie.setId(rs.getInt("id"));

            categories.add(categorie);
        }
        return categories;
    }

    @Override
    public CategorieCredit getById(int id) throws SQLException {
        String req = "select * from categorie_credit WHERE id ="+id+";";
        Statement statement= connection.createStatement();
        ResultSet rs= statement.executeQuery(req);

        if (rs.next()) {
            String nom = rs.getString("nom");
            double minMontant = rs.getDouble("min_montant");
            double maxMontant = rs.getDouble("max_montant");

            return new CategorieCredit(id, nom, minMontant, maxMontant);
        } else {
            return null;
        }
    }

    public boolean nomExiste(String nom) throws SQLException {
        String req = "SELECT * FROM categorie_credit WHERE nom = ?";
        PreparedStatement statement = connection.prepareStatement(req);
        statement.setString(1, nom);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    public boolean modifNom(String nom, int id) throws SQLException {
        String req = "SELECT * FROM categorie_credit WHERE nom = ? and id not like ?";
        PreparedStatement statement = connection.prepareStatement(req);
        statement.setString(1, nom);
        statement.setInt(2, id);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    public List<Credit> getCredits(int id) throws SQLException {
        List<Credit> credits= new ArrayList<>();
        String req="select * from credit where categorie_id ="+id+" and accepted = 1;";
        Statement statement= connection.createStatement();
        ResultSet rs= statement.executeQuery(req);

        while (rs.next()){
            Credit credit= new Credit();
            credit.setMontantTotale(rs.getDouble("montant_totale"));
            credit.setDureeTotale(rs.getInt("duree_totale"));
            credit.setInteret(rs.getInt("interet"));
            credit.setDateC(rs.getDate("date_c"));
            credit.setId(rs.getInt("id"));
            credits.add(credit);
        }
        return credits;
    }

    public CategorieCredit getByNom(String nom) throws SQLException {
        String req = "select * from categorie_credit WHERE nom =? ;";
        PreparedStatement statement = connection.prepareStatement(req);
        statement.setString(1, nom);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("id");
            double minMontant = rs.getDouble("min_montant");
            double maxMontant = rs.getDouble("max_montant");
            return new CategorieCredit(id, nom, minMontant, maxMontant);
        } else {
            return null;
        }
    }

    public BarChart<String, Number> generateCreditCategoryChart() throws SQLException {
        List<CategorieCredit> categories = this.getAll();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Nombre de crédits par catégorie");

        for (CategorieCredit categorie : categories) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(categorie.getNom());

            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM credit WHERE categorie_id = ?");
            statement.setInt(1, categorie.getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int nombreDeCredits = resultSet.getInt(1);
            series.getData().add(new XYChart.Data<>(categorie.getNom(), nombreDeCredits));
            barChart.getData().add(series);
        }
        return barChart;
    }
}
