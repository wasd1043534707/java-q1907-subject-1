package com.study.jvm;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.WorkbookParser;
import jxl.read.biff.SheetImpl;

import java.io.File;

// 使用server模式运行 开启GC日志
// -Xmx512m -server -verbose:gc -XX:+PrintGCDetails
// 禁止程序显示调用gc方法,来规避System.gc带来的fullgc风险   -XX:+DisableExplicitGC
public class FullGCDemo2 {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            WorkbookSettings workbookSettings = new WorkbookSettings();
            //workbookSettings.setGCDisabled(true); // 依赖包里面写

            String excelPath = FullGCDemo2.class.getClassLoader().getResource("FullGCDemo2.xls").getFile();
            Workbook book = Workbook.getWorkbook(new File(excelPath), workbookSettings);

            Sheet sheet = book.getSheet(0);     // 获得第一个工作表对象
            Cell cell1 = sheet.getCell(0, 0);       // 得到第一列第一行的单元格
            String result = cell1.getContents();

            System.out.println(result);
            book.close(); // 第三方依赖包，内部可能适用了system.gc()
            Thread.sleep(2000L);

        }
    }
}
