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
public class GetRemboursementBack {
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
    public void initData(Credit credit) {
        this.credit = credit;
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantRColumn.setCellValueFactory(new PropertyValueFactory<>("montantR"));
        montantRestantColumn.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));
        dateRColumn.setCellValueFactory(new PropertyValueFactory<>("dateR"));
        dureeRestanteColumn.setCellValueFactory(new PropertyValueFactory<>("dureeRestante"));
        try {
            List<Remboursement> remboursements = service.getByCredit(credit.getId());
            remboursementsTable.getItems().setAll(remboursements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
