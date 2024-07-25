package services;

import models.Transaction;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionService implements IService<Transaction> {

    private Connection connection;

    public TransactionService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction (montant, date_t, type_t, statut_t) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDouble(1, transaction.getMontant());
        preparedStatement.setDate(2, new java.sql.Date(transaction.getDate_t().getTime()));
        preparedStatement.setString(3, transaction.getType_t());
        preparedStatement.setString(4, transaction.getStatut_t());
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(Transaction transaction) throws SQLException {
        String sql = "UPDATE transaction SET montant = ?, date_t = ?, type_t = ?, statut_t = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDouble(1, transaction.getMontant());
        preparedStatement.setDate(2, new java.sql.Date(transaction.getDate_t().getTime()));
        preparedStatement.setString(3, transaction.getType_t());
        preparedStatement.setString(4, transaction.getStatut_t());
        preparedStatement.setLong(5, transaction.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM transaction WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }



    @Override
    public List<Transaction> getAll() throws SQLException {
        String sql = "SELECT * FROM transaction";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Transaction> transactions = new ArrayList<>();
        while (rs.next()) {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getLong("id"));
            transaction.setMontant(rs.getDouble("montant"));
            transaction.setDate_t(rs.getDate("date_t"));
            transaction.setType_t(rs.getString("type_t"));
            transaction.setStatut_t(rs.getString("statut_t"));

            transactions.add(transaction);
        }
        return transactions;
    }

    @Override
    public Transaction getById(int id) throws SQLException {
        String sql = "SELECT * FROM transaction WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Transaction transaction = new Transaction();
            transaction.setId(resultSet.getLong("id"));
            transaction.setMontant(resultSet.getDouble("montant"));
            transaction.setDate_t(resultSet.getDate("date_t"));
            transaction.setType_t(resultSet.getString("type_t"));
            transaction.setStatut_t(resultSet.getString("statut_t"));

            return transaction;
        } else {
            return null;
        }
    }
}
