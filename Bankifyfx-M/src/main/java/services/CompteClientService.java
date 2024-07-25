package services;

import models.CompteClient;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompteClientService implements IService<CompteClient> {

    private static Connection connection;

    public CompteClientService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(CompteClient compteClient) throws SQLException {
        String sql = "INSERT INTO compte_client (nom, prenom, rib,mail, tel, solde , sexe) VALUES (?, ?, ?, ? , ?, ? , ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, compteClient.getNom());
        preparedStatement.setString(2, compteClient.getPrenom());
        preparedStatement.setString(3, compteClient.getRib());
        preparedStatement.setString(4, compteClient.getMail());
        preparedStatement.setString(5, compteClient.getTel());
        preparedStatement.setFloat(6, compteClient.getSolde());
        preparedStatement.setString(7, compteClient.getSexe());
        preparedStatement.executeUpdate();

        // Récupérer l'ID généré pour l'entité
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            compteClient.setId(generatedKeys.getInt(1));
        } else {
            throw new SQLException("La création du compte_client a échoué, aucun ID généré récupéré.");
        }
    }

    @Override
    public void update(CompteClient compteClient) throws SQLException {
        String sql = "UPDATE compte_client SET nom = ?, prenom = ?, rib = ?,mail = ? , tel = ?, solde = ? , sexe = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, compteClient.getNom());
        preparedStatement.setString(2, compteClient.getPrenom());
        preparedStatement.setString(3, compteClient.getRib());
        preparedStatement.setString(4, compteClient.getMail());
        preparedStatement.setString(5, compteClient.getTel());
        preparedStatement.setFloat(6, compteClient.getSolde());
        preparedStatement.setString(7, compteClient.getSexe());
        preparedStatement.setInt(8, compteClient.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM compte_client WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, (int) id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<CompteClient> getAll() throws SQLException {
        String sql = "SELECT * FROM compte_client";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<CompteClient> compteClients = new ArrayList<>();
        while (rs.next()) {
            CompteClient compteClient = new CompteClient();
            compteClient.setId(rs.getInt("id"));
            compteClient.setNom(rs.getString("nom"));
            compteClient.setPrenom(rs.getString("prenom"));
            compteClient.setMail(rs.getString("mail"));
            compteClient.setRib(rs.getString("rib"));
            compteClient.setTel(rs.getString("tel"));
            compteClient.setSolde(rs.getFloat("solde"));
            compteClient.setSexe(rs.getString("sexe"));
            compteClients.add(compteClient);
        }
        return compteClients;
    }

    @Override
    public CompteClient getById(int id) throws SQLException {
        String sql = "SELECT * FROM compte_client WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            CompteClient compteClient = new CompteClient();
            compteClient.setId(resultSet.getInt("id"));
            compteClient.setNom(resultSet.getString("nom"));
            compteClient.setPrenom(resultSet.getString("prenom"));
            compteClient.setRib(resultSet.getString("rib"));
            compteClient.setTel(resultSet.getString("tel"));
            compteClient.setSolde(resultSet.getFloat("solde"));
            compteClient.setSexe(resultSet.getString("sexe"));
            return compteClient;
        } else {
            return null;
        }
    }

    public Map<String, Integer> getSexeStatistics() throws SQLException {
        Map<String, Integer> sexeStatistics = new HashMap<>();

        // Define the SQL query to count the number of accounts for each sexe category
        String sql = "SELECT sexe, COUNT(*) AS count FROM compte_client GROUP BY sexe";

        // Execute the query and process the results
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            // Iterate through the results and populate the map
            while (resultSet.next()) {
                String sexe = resultSet.getString("sexe");
                int count = resultSet.getInt("count");
                sexeStatistics.put(sexe, count);
            }
        }

        return sexeStatistics;
    }
    public List<CompteClient> getAllCompteClients() {
        List<CompteClient> compteClients = new ArrayList<>();
        String query = "SELECT * FROM compte_client";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                CompteClient compteClient = new CompteClient();
                compteClient.setId(rs.getInt("id"));
                compteClient.setNom(rs.getString("nom"));
                compteClient.setPrenom(rs.getString("prenom"));
                compteClient.setRib(rs.getString("rib"));
                compteClient.setMail(rs.getString("mail"));
                compteClient.setTel(rs.getString("tel"));
                compteClient.setSolde(rs.getFloat("solde"));

                compteClients.add(compteClient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compteClients;
    }
    public static List<CompteClient> getAllCompte() {
        List<CompteClient> Compte = new ArrayList<>();
        String query = "SELECT * FROM compte_client";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                CompteClient compte = new CompteClient();
                compte.setId(rs.getInt("id"));
                compte.setNom(rs.getString("nom"));
                compte.setPrenom(rs.getString("prenom"));
                compte.setMail(rs.getString("mail"));
                compte.setRib(rs.getString("rib"));
                compte.setTel(rs.getString("tel"));
                compte.setSolde(rs.getFloat("solde"));
                compte.setSexe(rs.getString("sexe"));
                Compte.add(compte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Compte;
    }

}
