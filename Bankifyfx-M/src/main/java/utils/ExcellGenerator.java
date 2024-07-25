package utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.collections.ObservableList;
import models.Virement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcellGenerator {
    public static void generateExcel(ObservableList<Virement> data, File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Virement Data");

        // Add title
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setBold(true);
        titleFont.setColor(IndexedColors.BLUE.getIndex()); // Set title font color to blue
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Virement Data");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); // Merge cells for the title

        // Create header row
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(1); // Header row starts from index 1
        headerRow.createCell(0).setCellValue("Compte Source");
        headerRow.createCell(1).setCellValue("Compte Destination");
        headerRow.createCell(2).setCellValue("Montant");


        for (Cell cell : headerRow) {
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows
        for (int i = 0; i < data.size(); i++) {
            Virement virement = data.get(i);
            Row row = sheet.createRow(i + 2); // Data rows start from index 2
            row.createCell(0).setCellValue(virement.getCompte_source());
            row.createCell(1).setCellValue(virement.getCompte_destination());
            row.createCell(2).setCellValue(virement.getMontant());

        }

        // Auto-size columns
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the workbook content to the file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }
}