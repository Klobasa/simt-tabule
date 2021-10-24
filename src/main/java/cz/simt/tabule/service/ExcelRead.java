package cz.simt.tabule.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


@Service
public class ExcelRead {
    public Map<Integer, List<String>> getRoute(String route) {
        String fileLocation = System.getProperty("user.dir") + "/src/main/resources/Routes.xlsx";
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(fileLocation));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        XSSFSheet sheet = workbook.getSheet(route);
        if (sheet == null) {
            System.out.println(LocalDateTime.now() + "Routes " + route + " was not found!");
            return null;
        }

        Map<Integer, List<String>> data = new HashMap<>();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                CellValue cellValue = evaluator.evaluate(cell);
                if (cellValue.getCellType() == CellType.NUMERIC) {
                    data.get(i).add(cellValue.getNumberValue() + "");
                } else {
                    data.get(i).add(cellValue.getStringValue());
                }
            }
            i++;
        }
        return data;
    }
}
