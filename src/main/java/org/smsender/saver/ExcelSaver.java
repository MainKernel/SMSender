package org.smsender.saver;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.smsender.entity.PhoneObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ExcelSaver {
    public static void saveNumber(List<PhoneObject> phones, Path path, String filename) throws IOException {

        Workbook workbook = new HSSFWorkbook();
        // Creating sheet
        Sheet sheet = workbook.createSheet("Phone Numbers");


        // Creating header
        // Fist creating first row
        Row headerRow = sheet.createRow(0);
        // Then creating first cell fore that row
        headerRow.createCell(0).setCellValue("Phone Number");
        // Then creating second cell for that row
        headerRow.createCell(1).setCellValue("Messenger");

        // Now we will save data to Excel document
        for (int i = 0; i < phones.size(); i++) {
            // Creating a row for a document
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(phones.get(i).phone());
            row.createCell(1).setCellValue(phones.get(i).message());
        }
        sheet.autoSizeColumn(0);
        try (FileOutputStream os = new FileOutputStream(path.toString() + "/" + filename + ".xlsx")) {
            workbook.write(os);
        }

        workbook.close();
    }
}
