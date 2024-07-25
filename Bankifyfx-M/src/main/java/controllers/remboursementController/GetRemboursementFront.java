package controllers.remboursementController;
import models.CategorieCredit;
import models.Remboursement;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.Credit;
import services.ServiceCategorieCredit;
import services.ServiceRemboursement;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
public class GetRemboursementFront {
    private final ServiceRemboursement service = new ServiceRemboursement();
    private Credit credit;

    @FXML
    private TableView<Remboursement> remboursementsTable;

    @FXML
    private TableColumn<Remboursement, Integer> idColumn;

    @FXML
    private TableColumn<Remboursement, Double> montantRColumn;

    @FXML
    private TableColumn<Remboursement, Integer> montantRestantColumn;

    @FXML
    private TableColumn<Remboursement, java.sql.Date> dateRColumn;

    @FXML
    private TableColumn<Remboursement, Integer> dureeRestanteColumn;

    @FXML
    private TableColumn<Remboursement, Void> pdfColumn;

    @FXML
    public void initData(Credit credit) {
        this.credit = credit;
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantRColumn.setCellValueFactory(new PropertyValueFactory<>("montantR"));
        montantRestantColumn.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));
        dateRColumn.setCellValueFactory(new PropertyValueFactory<>("dateR"));
        dureeRestanteColumn.setCellValueFactory(new PropertyValueFactory<>("dureeRestante"));
        pdfColumn.setCellFactory(new Callback<TableColumn<Remboursement, Void>, TableCell<Remboursement, Void>>() {
            @Override
            public TableCell<Remboursement, Void> call(final TableColumn<Remboursement, Void> param) {
                return new TableCell<Remboursement, Void>() {
                    private final Button pdfButton = new Button("Obtenir votre réçu");
                    {
                        pdfButton.setOnAction(event -> {
                            Remboursement remboursement = getTableView().getItems().get(getIndex());
                            handlePdf(remboursement);
                        });
                        pdfButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pdfButton);
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });
        loadData();
    }

    private void handlePdf(Remboursement remboursement) {
        service.generatePdf(remboursement);
    }

    private void loadData() {
        try {
            List<Remboursement> remboursements = service.getByCredit(credit.getId());
            remboursementsTable.getItems().setAll(remboursements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
