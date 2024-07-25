package utils;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;

public class PDFGenerator1 {

    public void generatePDF(ActionEvent event, TableView<?> tableView, String title) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(200, 750);
            contentStream.showText(title);  // Use the provided title
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(25, 700);

            for (TableColumn<?, ?> column : tableView.getColumns()) {
                contentStream.showText(column.getText());
                contentStream.newLineAtOffset(100, 0);
            }

            contentStream.newLineAtOffset(-100 * tableView.getColumns().size(), -20); // Adjust Y offset

            // Add table data
            for (Object item : tableView.getItems()) {
                for (TableColumn<?, ?> column : tableView.getColumns()) {
                    TableColumn<Object, ?> col = (TableColumn<Object, ?>) column;
                    String cellData = col.getCellData(item) != null ? col.getCellData(item).toString() : "";
                    contentStream.showText(cellData);
                    contentStream.newLineAtOffset(100, 0);
                }
                contentStream.newLineAtOffset(-100 * tableView.getColumns().size(), -20); // Adjust Y offset
            }

            contentStream.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Show notification
        Notifications.create()
                .title("PDF Downloaded")
                .text("PDF downloaded successfully!")
                .showInformation();

        try {
            // Choose file location and name using FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                document.save(file);
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}