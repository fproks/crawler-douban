package cn.linhos.Crawler;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fprok on 2017/12/19.
 */
public class ExcelWriteTest {

    /**
     * excel 存储测试方法
     *
     * @throws Exception
     */
    @Test
    public void writeToExcel() throws Exception {

        List<Data> dataArrayList = new ArrayList<>();
        dataArrayList.add(new Data("wwoo", 8.6F, 5000, "linhos", "工业"));
        dataArrayList.add(new Data("wwoo1", 8.6F, 5000, "linhos", "工业"));
        dataArrayList.add(new Data("wwoo2", 8.6F, 5000, "linhos", "工业"));
        ExcelWrite excelWrite = new ExcelWrite("123.xls", "123", new String[]{"id", "num"});
        excelWrite.createExcel();
        excelWrite.writeToExcel(dataArrayList);
    }
}