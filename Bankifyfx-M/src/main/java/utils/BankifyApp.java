package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankifyApp {

    public static void main(String[] args) {
        try (Connection connection = MyDatabase.getConnection()) {
            String query = "SELECT * FROM assurance";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int idAssurance = resultSet.getInt("id_assurance");
                        String typeAssurance = resultSet.getString("type_assurance");
                        double montantPrime = resultSet.getDouble("montant_prime");
                        String nomAssure = resultSet.getString("nom_assure");
                        String nomBeneficiaire = resultSet.getString("nom_beneficiaire");
                        String infoAssurance = resultSet.getString("info_assurance");

                        // Process the retrieved data
                        System.out.println("id_assurance: " + idAssurance);
                        System.out.println("type_assurance: " + typeAssurance);
                        System.out.println("montant_prime: " + montantPrime);
                        System.out.println("nom_assure: " + nomAssure);
                        System.out.println("nom_beneficiaire: " + nomBeneficiaire);
                        System.out.println("info_assurance: " + infoAssurance);
                        System.out.println("------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
