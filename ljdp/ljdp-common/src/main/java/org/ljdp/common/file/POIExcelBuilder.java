package org.ljdp.common.file;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ljdp.common.bean.DynamicField;
import org.ljdp.common.bean.MyBeanUtils;

public class POIExcelBuilder extends AbstractFileBuilder {
	
	private Workbook wb;
	private OutputStream os;
	
	public POIExcelBuilder() {
		wb = new XSSFWorkbook();
		fileInfo = new  FileInfo();
	}
	
	public POIExcelBuilder(OutputStream os) {
		wb = new XSSFWorkbook();
		fileInfo = new  FileInfo();
		this.os = os;
	}
	
	public void finish() {
		if(wb != null) {
			try {
				wb.write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Sheet createSheet(String name) {
		return wb.createSheet(name);
	}
	
	@Override
	public void build(OutputStream os, Collection<?> datas) throws Exception {
		if(StringUtils.isEmpty(fileInfo.getSheetName())) {
			fileInfo.setSheetName("sheet0");
		}
		Sheet sheet1 = wb.createSheet(fileInfo.getSheetName());
		
		buildSheet(sheet1, datas, 0);
		
		wb.write(os);
	}
	
	public void buildSheet(String name, Collection<?> datas) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Sheet sheet = wb.createSheet(name);
		buildSheet(sheet, datas, 0);
	}

	public int buildSheet(Sheet sheet, Collection<?> datas, int currentRow) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if( properties.size() > 0) {
			Font font = wb.createFont();
//			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			font.setBold(true);
			font.setFontName("微软雅黑");
			
			CellStyle cellstyle = wb.createCellStyle();
			cellstyle.setBorderBottom(BorderStyle.THIN);
			cellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cellstyle.setBorderLeft(BorderStyle.THIN);
			cellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			cellstyle.setBorderRight(BorderStyle.THIN);
			cellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cellstyle.setBorderTop(BorderStyle.THIN);
			cellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			cellstyle.setAlignment(HorizontalAlignment.CENTER);
			cellstyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellstyle.setFont(font);
			
			Row row = sheet.createRow((short)currentRow);
			for (int i = 0; i < properties.size(); i++) {
				DynamicField field = properties.get(i);
				int ilength = field.getMaxLength();
				if( ilength > 0) {
					sheet.setColumnWidth(i, ilength);
				} else if(properties.size()>=5) {
					sheet.setColumnWidth(i,5000);
				} else if(properties.size()>=8) {
					sheet.setColumnWidth(i,8000);
				} else if(properties.size()>=10) {
					sheet.setColumnWidth(i,10000);
				} else {
					sheet.setColumnWidth(i,3000);
				}
				if(fileInfo.isShowColumnTitle()) {
					Cell cell = row.createCell(i);
					cell.setCellValue(field.getName_cn());
					cell.setCellStyle(cellstyle);
				}
			}
			if(fileInfo.isShowColumnTitle()) {
				++currentRow;
			}
		}
		
		// print datas
		if (!datas.isEmpty()) {
			int dataBeginRow = currentRow;
			Font font = wb.createFont();
			font.setBold(true);
			font.setFontName("宋体");
			
			CellStyle cellstyle = wb.createCellStyle();
			cellstyle.setBorderBottom(BorderStyle.THIN);
			cellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cellstyle.setBorderLeft(BorderStyle.THIN);
			cellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			cellstyle.setBorderRight(BorderStyle.THIN);
			cellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cellstyle.setBorderTop(BorderStyle.THIN);
			cellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			cellstyle.setAlignment(HorizontalAlignment.CENTER);
			cellstyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellstyle.setFont(font);
			
			for (Iterator<?> iterator = datas.iterator(); iterator.hasNext(); ++currentRow) {
				Object element = iterator.next();
				Row row = sheet.createRow((short)currentRow);
				buildRowData(sheet, row, currentRow, element, cellstyle);
			}
			//merge column
			for (int i = 0; i < properties.size(); i++) {
				DynamicField field = properties.get(i);
				if(field.isAutoMerge()) {
					int begin = dataBeginRow;
					int end = begin;
					Iterator<?> it = datas.iterator();
					String begindata = MyBeanUtils.getElementPropertyValue(it.next(), i, field);;
					String enddata = begindata;
					while(it.hasNext()) {
						++end;
						enddata = MyBeanUtils.getElementPropertyValue(it.next(), i, field);
						if( ! enddata.equals(begindata) ) {
							if(end - begin > 1) {
								sheet.addMergedRegion(new CellRangeAddress(
										begin, //first row (0-based)
										end-1, //last row  (0-based)
							            i, //first column (0-based)
							            i  //last column  (0-based)
							    ));
							}
							begin = end;
							begindata = enddata;
						}
					}
					++end;
					if(end - begin > 1) {
						sheet.addMergedRegion(new CellRangeAddress(
								begin, //first row (0-based)
								end-1, //last row  (0-based)
					            i, //first column (0-based)
					            i  //last column  (0-based)
					    ));
					}
				}
			}
		}
		return currentRow;
	}
	
	private void buildRowData(final Sheet ws,final Row row, final int currentRow, Object element, 
			final CellStyle style) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (int i = 0; i < properties.size(); i++) {
			DynamicField field = properties.get(i);
//			CellStyle cs = style;
//			if(field.isLight()) {
//				cs = wb.createCellStyle();
//				cs.cloneStyleFrom(style);
//				cs.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//			}
			String valStr = MyBeanUtils.getElementPropertyValue(element, i, field);
			
			Cell cell = row.createCell(i);
			cell.setCellValue(valStr);
			cell.setCellStyle(style);
		}
	}
	
	
}
