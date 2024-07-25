package Cheques;

import models.Cheques.Reclamation;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Reclamations  implements IService<Reclamation>{
    public Connection cnx;

    public Reclamations() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Reclamation reclamation) throws SQLException {
        String req = "INSERT INTO Reclamtion(categorie, statut_r, cheque_id_id) VALUES (?, ?, ?)";

        PreparedStatement stm = cnx.prepareStatement(req);
        stm.setString(1, reclamation.getCategorie());
        stm.setString(2, reclamation.getStatutR());
        stm.setInt(3, reclamation.getChequeId()); // Assuming this is the ID of the associated cheque

        stm.executeUpdate();
    }
@Override
    public  void update(Reclamation reclamation) throws SQLException {
        String reclamationQuery = "UPDATE reclamtion SET categorie = ?, statut_r = ? WHERE id = ?";


        // Declare PreparedStatement variable for reclamation update
        PreparedStatement reclamationStatement = null;

        try {
            // Create a PreparedStatement with the reclamation update query
            reclamationStatement = cnx.prepareStatement(reclamationQuery);

            // Set the parameters for the reclamation update query
            reclamationStatement.setString(1, reclamation.getCategorie());
            reclamationStatement.setString(2, reclamation.getStatutR());
            reclamationStatement.setInt(3, reclamation.getId());

            // Execute the reclamation update query
            int reclamationRowsUpdated = reclamationStatement.executeUpdate();

            if (reclamationRowsUpdated > 0) {
                System.out.println("Reclamation updated successfully.");
            } else {
                System.out.println("Failed to update Reclamation.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the reclamation statement
            if (reclamationStatement != null) {
                reclamationStatement.close();
            }
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reclamtion WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(query)) {
            // Set the ID parameter for the query
            stm.setInt(1, id);

            // Execute the delete query
            int rowsDeleted = stm.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Reclaamtion with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No Reclaamtion found with ID " + id + ". Nothing deleted.");
            }
        }
    }

    public List<Reclamation> getAll() throws SQLException {
        return null;
    }





    public List<Reclamation> getAll(int chequeID) throws SQLException {
        List<Reclamation> Rec1 = new ArrayList<>();

        if (cnx == null) {
            // Log an error or handle the situation appropriately
            System.err.println("Connection is null. Cannot retrieve reclamations.");
            return Rec1; // Return empty list
        }

        String query = "SELECT * FROM reclamtion WHERE cheque_id_id = ?";
        try (PreparedStatement stm = cnx.prepareStatement(query)) {
            stm.setInt(1, chequeID);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Reclamation Rec = new Reclamation();
                    Rec.setId(rs.getInt("id"));
                    Rec.setCategorie(rs.getString("categorie"));
                    Rec.setStatutR(rs.getString("statut_r"));
                    Rec1.add(Rec);
                }
            }
        }

        // Check if Rec1 is null and return an empty list in that case
        if (Rec1 == null) {
            return new ArrayList<>();
        }

        return Rec1;
    }




    @Override
    public Reclamation getById(int id) throws SQLException {
        // Implement getById logic
        return null;

    }

    public Reclamation getReclamationById(int id) throws SQLException {
        String selectQuery = "SELECT * FROM reclamtion WHERE id = ?";
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        Reclamation reclamation = null;

        try {
            selectStatement = cnx.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Create a new Reclamation object and populate its attributes from the result set
                reclamation = new Reclamation();
                reclamation.setId(resultSet.getInt("id"));
                reclamation.setCategorie(resultSet.getString("categorie"));
                reclamation.setStatutR(resultSet.getString("statut_r"));
                // Populate other attributes as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the result set and statement
            if (resultSet != null) {
                resultSet.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
        }

        return reclamation;
    }
}
