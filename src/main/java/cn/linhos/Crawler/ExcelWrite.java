package cn.linhos.Crawler;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.List;

/**
 * Created by fprok on 2017/12/19.
 */
/**
 * excel文件存储类
 */
public class ExcelWrite {

    private static HSSFWorkbook workbook = null;
    private String fileName;  //文件名
    private String sheetName;  //工作表名
    private String titleRow[];  //标题

    public ExcelWrite(String fileName, String sheetName, String titleRow[]) {
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.titleRow = titleRow;
    }

    /**
     * 创建excel 文件
     * @throws Exception
     */
    public void createExcel() throws Exception {
        workbook = new HSSFWorkbook();
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        FileOutputStream out = null;
        try {
            HSSFRow row = workbook.getSheet(sheetName).createRow(0);
            for (short i = 0; i < titleRow.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(titleRow[i]);
            }
            out = new FileOutputStream(fileName);
            workbook.write(out);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将list 数据写入excel
     * @param dates 待写入的数据
     */
    public void WriteToExcel( List<Data> dates) {
        int dataSize =dates.size() >100? 100 :dates.size(); //只写入前100个
        File file = new File(fileName);
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        HSSFSheet sheet = workbook.getSheet(sheetName);
        int columnCount = sheet.getRow(0).getLastCellNum();
        try {
            for (int i = 0; i < dataSize; i++) {
                HSSFRow newRow = sheet.createRow(i + 1); //创建一行
                List<String> dataList = dates.get(i).DataToList(i);
                HSSFCell cell =newRow.createCell(0);
                cell.setCellValue(i+1);
                for (int j = 1; j < columnCount; j++) {  //写入一个数据
                    cell = newRow.createCell(j);
                    cell.setCellValue(dataList.get(j));
                }
            }
            out = new FileOutputStream(fileName);
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert out != null;
                out.close();
            } catch (IOException ignored) {
            }
        }
    }



}
