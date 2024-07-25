package controllers.Compte;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import services.CompteClientService;

import java.sql.SQLException;
import java.util.Map;

public class stat {

    private final CompteClientService compteClientService = new CompteClientService();

    @FXML
    private PieChart pieChart;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Load data into the PieChart when the controller is initialized
        loadSexeDistribution();
    }

    /**
     * Loads sexe distribution data and sets it on the PieChart.
     */
    private void loadSexeDistribution() {
        try {
            // Get sexe distribution statistics from CompteClientService
            Map<String, Integer> sexeStatistics = compteClientService.getSexeStatistics();

            // Create an ObservableList to hold the data for the PieChart
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            // Populate the pieChartData with the sexe statistics
            for (Map.Entry<String, Integer> entry : sexeStatistics.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            // Set the data on the PieChart
            pieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately (e.g., log the error or show an alert)
        }
    }

    /**
     * Handles the back button click event.
     * Closes the current statistics window.
     *
     * @param event The event that triggered this method.
     */
    public void handleBackButton(ActionEvent event) {
        // Get the source of the event (the button)
        Button sourceButton = (Button) event.getSource();
        // Close the window associated with the button
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.close();
    }

}