package org.ljdp.common.file;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelxTest {

	public static void main(String[] args) {
		try {
			String f = "C:/Users/Administrator/Desktop/新建文件夹/地址_惠来_测试20160408.xlsx";
			InputStream input = new FileInputStream(f);
			Workbook wb = WorkbookFactory.create(input);
			Sheet sheet = wb.getSheetAt(0);
			int numbers = 0;
			for(int i = 1; i <= sheet.getLastRowNum(); ++i) {
				Row row = sheet.getRow(i);
				if( !POIExcelUtil.isEmptyRow(row)) {
					++numbers;
				}
				System.out.println(numbers);
			}
			input.close();
			System.out.println(numbers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
