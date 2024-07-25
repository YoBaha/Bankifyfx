package services;

import models.CompteClient;

import utils.MyDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ServiceCompteClient implements IService1<CompteClient> {
    Connection connection;
    public ServiceCompteClient(){
        connection= MyDatabase.getInstance().getConnection();

    }
    @Override
    public void add(CompteClient compteClient) throws SQLException {

    }

    @Override
    public void edit(CompteClient compteClient) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<CompteClient> getAll() throws SQLException {
        return null;
    }

    @Override
    public CompteClient getById(int id) throws SQLException {
        String req = "select * from compte_client WHERE id ="+id+";";
        Statement statement= connection.createStatement();
        ResultSet rs= statement.executeQuery(req);

        if (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            String rib = rs.getString("rib");
            String mail = rs.getString("mail");
            String tel = rs.getString("tel");
            float solde = rs.getFloat("solde");
            String sexe = rs.getString("sexe");


            return new CompteClient( nom, prenom, rib, mail, tel, solde,sexe);
        } else {
            return null;
        }
    }
}
