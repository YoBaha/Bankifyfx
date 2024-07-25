package controllers.categorieCreditController;
import models.Credit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.CategorieCredit;
import services.ServiceCategorieCredit;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
public class ListeCredits {
    private final ServiceCategorieCredit service = new ServiceCategorieCredit();
    private CategorieCredit categorie;

    @FXML
    private TableView<Credit> creditsTable;

    @FXML
    private TableColumn<Credit, Integer> idColumn;

    @FXML
    private TableColumn<Credit, Double> montantTotaleColumn;

    @FXML
    private TableColumn<Credit, Integer> dureeTotaleColumn;

    @FXML
    private TableColumn<Credit, Integer> interetColumn;

    @FXML
    private TableColumn<Credit, java.sql.Date> dateCColumn;

    @FXML
    public void initData(CategorieCredit categorie) {
        this.categorie = categorie;
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantTotaleColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotale"));
        dureeTotaleColumn.setCellValueFactory(new PropertyValueFactory<>("dureeTotale"));
        interetColumn.setCellValueFactory(new PropertyValueFactory<>("interet"));
        dateCColumn.setCellValueFactory(new PropertyValueFactory<>("dateC"));
        try {
            List<Credit> credits = service.getCredits(categorie.getId());
            creditsTable.getItems().setAll(credits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
