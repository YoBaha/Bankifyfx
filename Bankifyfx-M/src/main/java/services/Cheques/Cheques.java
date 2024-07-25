package Cheques;

import models.Cheques.Cheque;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Cheques  implements IService<Cheque> {

    public Connection cnx;

    public Cheques() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Cheque cheque) throws SQLException {
        String req = "INSERT INTO `cheque`( `destination_c_id`, `compte_id_id`, `montant_c`) VALUES (?,?,?)";

        PreparedStatement stm = cnx.prepareStatement(req);
        stm.setInt(1, cheque.getDestinationCID());
        stm.setInt(2, cheque.getCompteID());
        stm.setFloat(3, cheque.getMontantC());



            stm.executeUpdate();

    }


    public Cheque update1(int chequeID) throws SQLException {
        String query = "SELECT * FROM cheque WHERE id = ?";
        PreparedStatement stm = cnx.prepareStatement(query);
        stm.setInt(1, chequeID);

        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            Cheque cheque = new Cheque();
            cheque.setId(rs.getInt("id"));
            cheque.setIsFav(rs.getInt("isfav"));
            cheque.setDestinationCID(rs.getInt("destination_c_id"));
            cheque.setCompteID(rs.getInt("compte_id_id"));
            cheque.setMontantC(rs.getFloat("montant_c"));
            cheque.setDateEmission(rs.getDate("date_emission"));

            return cheque;


        }
        return null;
    }
    @Override
    public void update(Cheque cheque) throws SQLException {
        String query = "UPDATE cheque SET montant_c = ?, compte_id_id = ?, destination_c_id = ? WHERE id = ?";

        // Declare PreparedStatement variable
        PreparedStatement statement = null;


            // Create a PreparedStatement with the query
            statement = cnx.prepareStatement(query);

            // Set the parameters for the query
            statement.setFloat(1, cheque.getMontantC());
            statement.setInt(2, cheque.getCompteID());
            statement.setInt(3, cheque.getDestinationCID());
            statement.setInt(4, cheque.getId());

            // Execute the update query
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Cheque updated successfully.");
            } else {
                System.out.println("Failed to update Cheque.");
            }

    }
    public void updateFav(Cheque cheque) throws SQLException {
        String query = "UPDATE cheque SET isfav = ? WHERE id = ?";

        // Declare PreparedStatement variable
        PreparedStatement statement = null;


        // Create a PreparedStatement with the query
        statement = cnx.prepareStatement(query);

        // Set the parameters for the query
        statement.setFloat(1, cheque.getIsFav());
        statement.setFloat(2, cheque.getId());


        // Execute the update query
        int rowsUpdated = statement.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Cheque updated successfully.");
        } else {
            System.out.println("Failed to update Cheque.");
        }

    }
    public void update1(Cheque cheque) throws SQLException {
        String query = "UPDATE cheque SET montant_c = ?,  destination_c_id = ? WHERE id = ?";

        // Declare PreparedStatement variable
        PreparedStatement statement = null;


        // Create a PreparedStatement with the query
        statement = cnx.prepareStatement(query);

        // Set the parameters for the query
        statement.setFloat(1, cheque.getMontantC());

        statement.setInt(2, cheque.getDestinationCID());
        statement.setInt(3, cheque.getId());

        // Execute the update query
        int rowsUpdated = statement.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Cheque updated successfully.");
        } else {
            System.out.println("Failed to update Cheque.");
        }

    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM cheque WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(query)) {
            // Set the ID parameter for the query
            stm.setInt(1, id);

            // Execute the delete query
            int rowsDeleted = stm.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Cheque with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No Cheque found with ID " + id + ". Nothing deleted.");
            }
        }
    }

    public List<Cheque> getAll() throws SQLException {
        List<Cheque> cheques = new ArrayList<>();

        String query = "SELECT * FROM cheque";
        try (PreparedStatement stm = cnx.prepareStatement(query);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Cheque cheque = new Cheque();
                cheque.setId(rs.getInt("id"));
                cheque.setIsFav(rs.getInt("isfav"));
                cheque.setDestinationCID(rs.getInt("destination_c_id"));
                cheque.setCompteID(rs.getInt("compte_id_id"));
                cheque.setMontantC(rs.getFloat("montant_c"));
                cheque.setDateEmission(rs.getDate("date_emission"));
                cheques.add(cheque);
            }
        }

        return cheques;
    }
    public List<Cheque> getAll(int CompteID) throws SQLException {
        List<Cheque> cheques = new ArrayList<>();

        String query = "SELECT * FROM cheque WHERE compte_id_id = ?";
        try (PreparedStatement stm = cnx.prepareStatement(query)) {
            stm.setInt(1, CompteID); // Set the value of the parameter CompteID

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Cheque cheque = new Cheque();
                    cheque.setId(rs.getInt("id"));
                    cheque.setIsFav(rs.getInt("isfav"));
                    cheque.setDestinationCID(rs.getInt("destination_c_id"));
                    cheque.setCompteID(rs.getInt("compte_id_id"));
                    cheque.setMontantC(rs.getFloat("montant_c"));
                    cheque.setDateEmission(rs.getDate("date_emission"));
                    cheques.add(cheque);
                }
            }
        }

        return cheques;
    }
    public List<Cheque> getAll(int CompteID,int isfav) throws SQLException {
        List<Cheque> cheques = new ArrayList<>();

        String query = "SELECT * FROM cheque WHERE compte_id_id = ? and isfav = ? ";
        try (PreparedStatement stm = cnx.prepareStatement(query)) {
            stm.setInt(1, CompteID);
            stm.setInt(2, isfav);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Cheque cheque = new Cheque();
                    cheque.setId(rs.getInt("id"));
                    cheque.setIsFav(rs.getInt("isfav"));
                    cheque.setDestinationCID(rs.getInt("destination_c_id"));
                    cheque.setCompteID(rs.getInt("compte_id_id"));
                    cheque.setMontantC(rs.getFloat("montant_c"));
                    cheque.setDateEmission(rs.getDate("date_emission"));
                    cheques.add(cheque);
                }
            }
        }

        return cheques;
    }

    @Override
    public Cheque getById(int chequeId) throws SQLException {
        // Implement getById logic
        return null;

}}
