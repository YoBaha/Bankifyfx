package services;

import models.Carte;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import models.CompteClient;
import services.CompteClientService;

public class CarteService implements IService<Carte> {

    private Connection connection;

    public CarteService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public boolean isCardNumberUnique(String num_c) throws SQLException {
        String sql = "SELECT COUNT(*) FROM carte WHERE num_c = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, num_c);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count == 0; // True if count is 0, meaning the card number is unique
        }
        return false; // Default to false if there was an error
    }




    @Override
    public void add(Carte carte) throws SQLException {
        String sql = "INSERT INTO carte (num_c, date_exp, type_c, statut_c, account_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carte.getNum_c());
        preparedStatement.setDate(2, new java.sql.Date(carte.getDate_exp().getTime()));
        preparedStatement.setString(3, carte.getType_c());
        preparedStatement.setString(4, carte.getStatut_c());
        CompteClient compteClient = carte.getCompteClient();

        // If compteClient is null, you need to decide how to handle this situation.
        // Here, we simply set the compte_client_id parameter to null, which may depend on your database schema.
        // If your schema does not allow null values for this field, you may need to handle this differently.
        if (compteClient != null) {
            preparedStatement.setLong(5, compteClient.getId());
        } else {
            // If CompteClient is null, decide how to handle this case:
            // - You might want to assign a default value or
            // - Throw an exception to indicate that a valid CompteClient is required.
            preparedStatement.setNull(5, java.sql.Types.BIGINT);
            // Alternatively, you could handle the null case differently based on your application requirements.
        }

        // Execute the SQL update statement
        preparedStatement.executeUpdate();
    }
    public TreeSet<Carte> sortByCritere(String critere) {
        switch (critere) {
            case "Number":
                // Sort by Carte number (num_c)
                return getAllCartes().stream()
                        .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Carte::getNum_c))));

            case "Date":
                // Sort by Carte date (date_c)
                return getAllCartes().stream()
                        .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Carte::getDate_exp))));

            default:
                // If an unknown criterion is provided, return an empty TreeSet
                return new TreeSet<>();
        }
    }
    @Override
    public void update(Carte carte) throws SQLException {
        String sql = "UPDATE carte SET num_c = ?, date_exp = ?, type_c = ?, statut_c = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carte.getNum_c());
        preparedStatement.setDate(2, new java.sql.Date(carte.getDate_exp().getTime()));
        preparedStatement.setString(3, carte.getType_c());
        preparedStatement.setString(4, carte.getStatut_c());
        preparedStatement.setLong(5, carte.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM carte WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Carte> getAll() throws SQLException {
        String sql = "SELECT * FROM carte";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Carte> cartes = new ArrayList<>();
        while (rs.next()) {
            Carte carte = new Carte();
            carte.setId(rs.getLong("id"));
            carte.setNum_c(rs.getString("num_c"));
            carte.setDate_exp(rs.getDate("date_exp"));
            carte.setType_c(rs.getString("type_c"));
            carte.setStatut_c(rs.getString("statut_c"));

            cartes.add(carte);
        }
        return cartes;
    }

    @Override
    public Carte getById(int id) throws SQLException {
        String sql = "SELECT * FROM carte WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Carte carte = new Carte();
            carte.setId(resultSet.getLong("id"));
            carte.setNum_c(resultSet.getString("num_c"));
            carte.setDate_exp(resultSet.getDate("date_exp"));
            carte.setType_c(resultSet.getString("type_c"));
            carte.setStatut_c(resultSet.getString("statut_c"));

            return carte;
        } else {
            return null;
        }
    }

    public List<Carte> getAllCartes() {
        List<Carte> cartes = new ArrayList<>();
        String query = "SELECT * FROM carte";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Carte carte = new Carte();
                carte.setId(rs.getLong("id"));
                carte.setNum_c(rs.getString("num_c"));
                carte.setDate_exp(rs.getDate("date_exp"));
                carte.setType_c(rs.getString("type_c"));
                carte.setStatut_c(rs.getString("statut_c"));

                cartes.add(carte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartes;
    }


    public Map<String, Integer> getCardTypeStatistics() throws SQLException {
        Map<String, Integer> cardTypeStatistics = new HashMap<>();

        // Define the SQL query to count the number of cards for each type
        String sql = "SELECT type_c, COUNT(*) AS count FROM carte WHERE type_c IN ('Visa', 'MasterCard') GROUP BY type_c";

        // Execute the query and process the results
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            // Iterate through the results and populate the map
            while (resultSet.next()) {
                String cardType = resultSet.getString("type_c");
                int count = resultSet.getInt("count");
                cardTypeStatistics.put(cardType, count);
            }
        }

        return cardTypeStatistics;
    }




}