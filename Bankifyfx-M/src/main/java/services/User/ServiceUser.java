package services.User;

import models.User;
import utils.MyDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServiceUser implements IService<User> {
    Connection connection;
    Statement ste;

    public ServiceUser() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void Ajouter(User t) {
        try {
            String passwordencrypted = encrypt(t.getPassword());
            String req = "INSERT INTO user (nom, prenom, email, password, dateNaissance, genre) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, t.getNom());
            preparedStatement.setString(2, t.getPrenom());
            preparedStatement.setString(3, t.getEmail());
            preparedStatement.setString(4, passwordencrypted);
            preparedStatement.setDate(5, new java.sql.Date(t.getDateNaissance().getTime()));
            preparedStatement.setString(6, t.getGenre());
            preparedStatement.executeUpdate();
            System.out.println("User ajouté");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void Modifier(User u) {
        String req = "UPDATE user SET nom=?, prenom=?, email=?, dateNaissance=?, genre=?, picture=? WHERE id=?";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, u.getNom());
            preparedStatement.setString(2, u.getPrenom());
            preparedStatement.setString(3, u.getEmail());
            preparedStatement.setDate(4, new java.sql.Date(u.getDateNaissance().getTime()));
            preparedStatement.setString(5, u.getGenre());
            preparedStatement.setString(6, u.getPicture()); // Set the picture attribute
            preparedStatement.setInt(7, u.getId());
            preparedStatement.executeUpdate();
            System.out.println("User modifié");
        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }

    @Override
    public void Supprimer(User u) {
        String sql = "delete from user where id=?";
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, u.getId());
            ste.executeUpdate();
            System.out.println("Utilisateur supprimé");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void SupprimerById(int t) {
        String sql = "DELETE FROM user WHERE id=?";
        try {

            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, t);

            ste.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ObservableList<User> afficherUtilisateurs() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM user WHERE role = 'client'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(req);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setDateNaissance(rs.getDate("dateNaissance"));
                user.setGenre(rs.getString("genre"));
                user.setIsActive(rs.getBoolean("isActive"));
                users.add(user);
            }
        } catch (SQLException ex) {

            System.out.println("Error fetching users: " + ex.getMessage());
        }
        return users;
    }
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            String req = "SELECT * FROM user";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(req);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Date dateNaissance= rs.getDate("dateNaissance");
                String genre = rs.getString("genre");

                // Create a User object and add it to the list
                User user = new User(id, nom, prenom, email, password, dateNaissance, genre);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE email = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void changePassword(String email, String newPassword) {
        String passwordencrypted = encrypt(newPassword);
        String query = "UPDATE user SET password=? WHERE email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, passwordencrypted);
            preparedStatement.setString(2, email);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Mot de passe modifié avec succès");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet email");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public User authenticateUser(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        String encryptedPassword = encrypt(password);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, encryptedPassword);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // User found, return user object
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                Date dateNaissance= resultSet.getDate("dateNaissance");
                String genre = resultSet.getString("genre");
                boolean verified = resultSet.getBoolean("verified");
                String role = resultSet.getString("role");
                boolean isActive = resultSet.getBoolean("isActive");
                String picture = resultSet.getString("picture");
                // Additional fields to fetch from database as needed

                return new User(id, nom, prenom, email, password, dateNaissance, genre,verified,role,isActive,picture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }

    public void Modifier1(User u) {
        String req = "UPDATE user SET nom=?, prenom=?, email=?, dateNaissance=?, genre=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, u.getNom());
            preparedStatement.setString(2, u.getPrenom());
            preparedStatement.setString(3, u.getEmail());
            preparedStatement.setDate(4, new java.sql.Date(u.getDateNaissance().getTime()));
            preparedStatement.setString(5, u.getGenre());
            // Set the picture attribute
            preparedStatement.setInt(6, u.getId());
            preparedStatement.executeUpdate();
            System.out.println("User modifié");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // User found, return user object
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String password = resultSet.getString("password");
                Date dateNaissance = resultSet.getDate("dateNaissance");
                String genre = resultSet.getString("genre");
                boolean verified = resultSet.getBoolean("verified");
                String role = resultSet.getString("role");
                boolean isActive = resultSet.getBoolean("isActive");
                String picture = resultSet.getString("picture");

                // Create and return the user object
                return new User(id, nom, prenom, email, password, dateNaissance, genre, verified, role, isActive,picture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }

    public void setVerified(String email, boolean verified) {
        String query = "UPDATE user SET verified = ? WHERE email = ?";

        try{ PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters
            preparedStatement.setBoolean(1, verified);
            preparedStatement.setString(2, email);

            // Execute update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No user found with email: " + email);
            } else {
                System.out.println("User verified status updated successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user verified status: " + e.getMessage());
        }
    }

    public void activateUser(String email) {
        setisActive(email, true);
    }

    public void deactivateUser(String email) {
        setisActive(email, false);
    }

    public void setisActive(String email, boolean isActive) {
        String query = "UPDATE user SET isActive = ? WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, isActive);
            preparedStatement.setString(2, email);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No user found with email: " + email);
            } else {
                String status = isActive ? "activated" : "deactivated";
                System.out.println("User account " + status + " successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user account status: " + e.getMessage());
        }
    }

}


