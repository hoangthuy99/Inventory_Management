package com.ra.inventory_management.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Component
public class ExcelUtil {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public boolean hasExcelFormat(MultipartFile file) {
        if (TYPE.equals(file.getContentType())) {
            return true;
        }

        return false;
    }

    public Iterator toIterator(InputStream inputStream) throws IOException {

        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator rows = sheet.rowIterator();
            workbook.close();
            return rows;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file " + e.getMessage());
        }
    }
}
