package utils;

import javafx.collections.ObservableList;
import models.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Exel{

    /**
     * Generates an Excel file based on the provided list of Transaction objects.
     *
     * @param transactions The list of Transaction objects to be exported.
     * @param file         The file to write the Excel content to.
     * @throws IOException If an I/O error occurs during writing the file.
     */
    public static void generateExcel(ObservableList<Transaction> transactions, File file) throws IOException {
        // Create a new workbook and a sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transaction Data");

        // Add title
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setBold(true);
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Transaction Data");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5)); // Merge cells for the title

        // Create header row
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(1);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Montant");
        headerRow.createCell(2).setCellValue("Date");
        headerRow.createCell(3).setCellValue("Type");
        headerRow.createCell(4).setCellValue("Statut");


        for (Cell cell : headerRow) {
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            Row row = sheet.createRow(i + 2);
            row.createCell(0).setCellValue(transaction.getId());
            row.createCell(1).setCellValue(transaction.getMontant());
            row.createCell(2).setCellValue(transaction.getDate_t().toString());
            row.createCell(3).setCellValue(transaction.getType_t());
            row.createCell(4).setCellValue(transaction.getStatut_t());

        }

        // Auto-size columns
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the workbook content to the file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // Close the workbook
        workbook.close();
    }
}