package com.yl.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	/**
	 * 导出Excel
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @param title
	 *            标题
	 * @param values
	 *            内容
	 * @param wb
	 *            HSSFWorkbook对象
	 * @return
	 */
	public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

		// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
		if (wb == null) {
			wb = new HSSFWorkbook();
		}

		// 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(sheetName);

		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
		HSSFRow row = sheet.createRow(0);

		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		// 声明列对象
		HSSFCell cell = null;

		// 创建标题
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}

		// 创建内容
		for (int i = 0; i < values.length; i++) {
			row = sheet.createRow(i + 1);
			for (int j = 0; j < values[i].length; j++) {
				// 将内容按顺序赋给对应的列对象
				row.createCell(j).setCellValue(values[i][j]);
			}
		}
		return wb;
	}
	 /**
     * @param filepath //文件路径
     * @param filename //文件名
     * @param startrow //开始行号
     * @param startcol //开始列号
     * @param sheetnum //sheet
     * @return list
     */
    
    /*读取xls文件*/
    public static List<Map<String, String>> readExcelByFileForXls(File file, int startrow, int startcol, int sheetnum) {
        List<Map<String, String>> varList = new ArrayList<Map<String, String>>();
        
        try {
            //File target = new File(filepath, filename);
            //File target = new File(file);
            FileInputStream fi = new FileInputStream(file);
            HSSFWorkbook wb = new HSSFWorkbook(fi);//xslx
            HSSFSheet sheet= wb.getSheetAt(sheetnum); //sheet 从0开始

            int rowNum = sheet.getLastRowNum() + 1;                     //取得最后一行的行号
            
            for (int i = startrow; i < rowNum; i++) {                    //行循环开始
                
                Map<String, String> varpd = new HashMap<String, String>();
                HSSFRow row = sheet.getRow(i);                             //行
                int cellNum = row.getLastCellNum();                     //每行的最后一个单元格位置
                
                for (int j = startcol; j < cellNum; j++) {                //列循环开始
                    
                    HSSFCell cell = row.getCell(Short.parseShort(j + ""));
                    String cellValue = null;
                    if (null != cell) {
                        switch (cell.getCellType()) {                     // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                        case 0:
                            cellValue = String.valueOf((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            cellValue = cell.getStringCellValue();
                            break;
                        case 2:
                            cellValue = cell.getNumericCellValue() + "";
                            // cellValue = String.valueOf(cell.getDateCellValue());
                            break;
                        case 3:
                            cellValue = "";
                            break;
                        case 4:
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case 5:
                            cellValue = String.valueOf(cell.getErrorCellValue());
                            break;
                        }
                    } else {
                        cellValue = "";
                    }
                    
                    varpd.put("var"+j, cellValue);
                    
                }
                varList.add(varpd);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return varList;
    }
    
    /*读取xlsx文件*/
    public static List<Map<String, String>> readExcelByFileForXlsx(File file, int startrow, int startcol, int sheetnum) {
        List<Map<String, String>> varList = new ArrayList<Map<String, String>>();        
        try {
            //File target = new File(filepath, filename);
            //File target = new File(file);
            FileInputStream fi = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
            XSSFSheet sheet= wb.getSheetAt(sheetnum); //sheet 从0开始
            int rowNum = sheet.getLastRowNum() + 1;                     //取得最后一行的行号
            
            for (int i = startrow; i < rowNum; i++) {                    //行循环开始
                
                Map<String, String> varpd = new HashMap<String, String>();
                XSSFRow row = sheet.getRow(i);                             //行
                int cellNum = row.getLastCellNum();                     //每行的最后一个单元格位置
                
                for (int j = startcol; j < cellNum; j++) {                //列循环开始
                    
                    XSSFCell cell = row.getCell(Short.parseShort(j + ""));
                    String cellValue = null;
                    if (null != cell) {
                        switch (cell.getCellType()) {                     // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                        case 0:
                            cellValue = String.valueOf((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            cellValue = cell.getStringCellValue();
                            break;
                        case 2:
                            cellValue = cell.getNumericCellValue() + "";
                            // cellValue = String.valueOf(cell.getDateCellValue());
                            break;
                        case 3:
                            cellValue = "";
                            break;
                        case 4:
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case 5:
                            cellValue = String.valueOf(cell.getErrorCellValue());
                            break;
                        }
                    } else {
                        cellValue = "";
                    }
                    
                    varpd.put("var"+j, cellValue);
                    
                }
                varList.add(varpd);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return varList;
    }
}
