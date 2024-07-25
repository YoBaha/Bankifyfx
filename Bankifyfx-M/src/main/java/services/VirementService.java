package services;

import models.Virement;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VirementService implements IService<Virement> {

    private Connection connection;

    public VirementService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Virement virement) throws SQLException {
        String sql = "INSERT INTO virement (compte_source, compte_destination, montant, date, heure) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, virement.getCompte_source());
        preparedStatement.setString(2, virement.getCompte_destination());
        preparedStatement.setFloat(3, virement.getMontant());
        preparedStatement.setDate(4, virement.getDate());
        preparedStatement.setTime(5, virement.getHeure());
        preparedStatement.executeUpdate();

        // Récupérer l'ID généré pour l'entité
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            virement.setId(generatedKeys.getInt(1));
        } else {
            throw new SQLException("La création du virement a échoué, aucun ID généré récupéré.");
        }
    }

    @Override
    public void update(Virement virement) throws SQLException {
        String sql = "UPDATE virement SET compte_source = ?, compte_destination = ?, montant = ?, date = ?, heure = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, virement.getCompte_source());
        preparedStatement.setString(2, virement.getCompte_destination());
        preparedStatement.setFloat(3, virement.getMontant());
        preparedStatement.setDate(4, virement.getDate());
        preparedStatement.setTime(5, virement.getHeure());
        preparedStatement.setInt(6, virement.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM virement WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Virement> getAll() throws SQLException {
        String sql = "SELECT * FROM virement";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Virement> virements = new ArrayList<>();
        while (rs.next()) {
            Virement virement = new Virement();
            virement.setId(rs.getInt("id"));
            virement.setCompte_source(rs.getString("compte_source"));
            virement.setCompte_destination(rs.getString("compte_destination"));
            virement.setMontant(rs.getFloat("montant"));
            virement.setDate(rs.getDate("date"));
            virement.setHeure(rs.getTime("heure"));
            virements.add(virement);
        }
        return virements;
    }

    @Override
    public Virement getById(int id) throws SQLException {
        String sql = "SELECT * FROM virement WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Virement virement = new Virement();
            virement.setId(resultSet.getInt("id"));
            virement.setCompte_source(resultSet.getString("compte_source"));
            virement.setCompte_destination(resultSet.getString("compte_destination"));
            virement.setMontant(resultSet.getFloat("montant"));
            virement.setDate(resultSet.getDate("date"));
            virement.setHeure(resultSet.getTime("heure"));
            return virement;
        } else {
            return null;
        }
    }
}
