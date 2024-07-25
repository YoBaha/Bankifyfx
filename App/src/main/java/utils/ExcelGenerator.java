package utils;

import javafx.collections.ObservableList;
import models.CategorieAssurance;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelGenerator {

    public static void generateExcel(ObservableList<CategorieAssurance> data, File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Categorie Data");

        // Add title
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setBold(true);
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Categorie Assurance Data");
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
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nom Categorie");
        headerRow.createCell(2).setCellValue("Description");
        headerRow.createCell(3).setCellValue("Type Couverture");
        headerRow.createCell(4).setCellValue("Agence Responsable");

        for (Cell cell : headerRow) {
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows
        for (int i = 0; i < data.size(); i++) {
            CategorieAssurance categorie = data.get(i);
            Row row = sheet.createRow(i + 2); // Data rows start from index 2
            row.createCell(0).setCellValue(categorie.getIdCategorie());
            row.createCell(1).setCellValue(categorie.getNomCategorie());
            row.createCell(2).setCellValue(categorie.getDescription());
            row.createCell(3).setCellValue(categorie.getTypeCouverture());
            row.createCell(4).setCellValue(categorie.getAgenceResponsable());
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
