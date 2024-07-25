package utils;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.Color;
import java.io.IOException;

public class PDF {

    public void generatePDF(ActionEvent event, TableView<?> tableView, String title) {
        try (PDDocument document = new PDDocument()) {
            // Create a new page with a pink background
            PDPage page = new PDPage();
            PDRectangle pageSize = page.getMediaBox();
            document.addPage(page);

            // Create a content stream for drawing on the PDF
            PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.OVERWRITE, true);

            // Fill the background with pink color
            contentStream.setNonStrokingColor(new Color(255, 192, 203)); // RGB for pink
            contentStream.addRect(0, 0, pageSize.getWidth(), pageSize.getHeight());
            contentStream.fill();

            // Calculate title text width and center it on the page
            float titleFontSize = 16;
            float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(title) / 1000 * titleFontSize;
            float titleX = (pageSize.getWidth() - titleWidth) / 2;

            // Set title color to purple and write the title
            contentStream.setNonStrokingColor(new Color(160, 32, 240)); // RGB for purple
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, titleFontSize);
            contentStream.newLineAtOffset(titleX, 750); // Top position for title
            contentStream.showText(title);
            contentStream.endText();

            // Set up the starting positions for the table headers and rows
            float startY = 720; // Starting y position for table headers
            float startX = 50;  // Starting x position for table headers
            float columnSpacing = 200; // Increased spacing between columns

            // Draw headers for montant, date, type, and statut columns
            contentStream.setNonStrokingColor(new Color(160, 32, 240)); // RGB for purple
            float currentX = startX;

            for (TableColumn<?, ?> column : tableView.getColumns()) {
                // Filter for the required columns
                if (!column.getText().equalsIgnoreCase("montant") &&
                        !column.getText().equalsIgnoreCase("date") &&
                        !column.getText().equalsIgnoreCase("type") &&
                        !column.getText().equalsIgnoreCase("statut")) {
                    continue;
                }

                String header = column.getText();
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(currentX, startY);
                contentStream.showText(header);
                contentStream.endText();

                // Move the x position for the next header
                currentX += columnSpacing; // Increased spacing
            }

            // Draw table rows
            startY -= 20; // Move down from headers
            for (Object item : tableView.getItems()) {
                currentX = startX;

                for (TableColumn<?, ?> column : tableView.getColumns()) {
                    // Filter for the required columns
                    if (!column.getText().equalsIgnoreCase("montant") &&
                            !column.getText().equalsIgnoreCase("date") &&
                            !column.getText().equalsIgnoreCase("type") &&
                            !column.getText().equalsIgnoreCase("statut")) {
                        continue;
                    }

                    TableColumn<Object, ?> col = (TableColumn<Object, ?>) column;
                    String cellData = col.getCellData(item) != null ? col.getCellData(item).toString() : "";

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.setNonStrokingColor(Color.BLACK); // Use black for text
                    contentStream.newLineAtOffset(currentX, startY);
                    contentStream.showText(cellData);
                    contentStream.endText();

                    currentX += columnSpacing; // Increased spacing
                }
                startY -= 20; // Move down to the next row
            }

            // Close the content stream
            contentStream.close();

            // Save the document with the provided title as the filename
            document.save(title + ".pdf");

            // Show a success alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("PDF Generated");
            alert.setHeaderText(null);
            alert.setContentText("PDF generated successfully as " + title + ".pdf");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}