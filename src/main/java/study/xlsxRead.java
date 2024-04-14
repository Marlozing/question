package study;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class xlsxRead {

    public static void main(String[] args) {
        try{
            FileInputStream file = new FileInputStream("word/word.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for(int i = 0; i < 100; i+=2){
                List<String> lines = Files.readAllLines(Path.of("./word\\word" + (i / 2 + 1) + ".txt"));
                for(int j = 0; j < lines.size() - 1; j+=2){
                    Row row = sheet.getRow(j / 2);  // 가로줄

                    Cell englishCell = row.createCell(i);      //세로줄
                    Cell koreanCell = row.createCell(i + 1);

                    englishCell.setCellValue(lines.get(j));     // 단어 넣기
                    koreanCell.setCellValue(lines.get(j + 1));
                }
            }

            FileOutputStream out = new FileOutputStream("word/word.xlsx");
            workbook.write(out);

            out.close();
            file.close();

            System.out.println(readKorean());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> readWord(Integer i){
        HashMap<String, String> word = new HashMap<>();
        if(i <= 50){
            try {
                FileInputStream file = new FileInputStream("word/word.xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                XSSFSheet sheet = workbook.getSheetAt(0);
                for(int j = 0; j < 40; j++){
                    Row row = sheet.getRow(j);
                    word.put(row.getCell(2 * i - 2).getStringCellValue(), row.getCell(2 * i - 1).getStringCellValue());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return word;
    }

    public static List<String> readKorean(){
        List<String> korean = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream("word/word.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < 40; i++) {
                Row row = sheet.getRow(i);
                for(int j = 1; j < 100; j+=2){
                    korean.add(row.getCell(j).getStringCellValue());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        return korean;
    }
}
