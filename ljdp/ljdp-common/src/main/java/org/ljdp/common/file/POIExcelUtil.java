package org.ljdp.common.file;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.ljdp.util.DateFormater;

public class POIExcelUtil {
	public static boolean isEmptyRow(Row row) {
		if (row == null) {
			return true;
		}
		boolean empty = true;
		Iterator<Cell> it = row.cellIterator();
		while (it.hasNext()) {
			Cell cell = it.next();
			if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String val = cell.getStringCellValue();
					if (val.trim().length() > 0) {
						empty = false;
						break;
					}
				} else {
					empty = false;
					break;
				}
			}
		}
		return empty;
	}

	public static String toStringLine(Row row) {
		StringBuffer sb = new StringBuffer(row.getPhysicalNumberOfCells() * 10);
		
		int n = row.getLastCellNum();
		for(int i = 0;i < n;i++){
			Cell cell = row.getCell(i);
			if(cell != null) {
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					sb.append(cell.getStringCellValue()).append("|");
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						String dates = DateFormater.formatDate(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
						sb.append(dates).append("|");
					} else {
						sb.append(cell.getNumericCellValue()).append("|");
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					sb.append(cell.getBooleanCellValue()).append("|");
					break;
				case Cell.CELL_TYPE_FORMULA:
					sb.append(cell.getCellFormula()).append("|");
					break;
				case Cell.CELL_TYPE_BLANK:
					sb.append("|");
					break;
				default:
					sb.append(cell.getStringCellValue()).append("|");
				}
			} else {
				sb.append("|");
			}
		}
		return sb.toString();
	}
	
	public static StringBuffer toOrginStringBuffer(Row row) {
		StringBuffer sb = new StringBuffer(row.getPhysicalNumberOfCells() * 20);
		int n = row.getLastCellNum();
		for(int i = 0;i < n;i++){
			Cell cell = row.getCell(i);
			if(cell != null) {
				sb.append(cell.getStringCellValue()).append("|");
			} else {
				sb.append("|");
			}
		}
		return sb;
	}
	
	public static Cell copyCell(Cell destCell, Cell sourceCell) {
		destCell.setCellType(sourceCell.getCellType());
		switch (sourceCell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(sourceCell)) {
				destCell.setCellValue(sourceCell.getDateCellValue());
			} else {
				destCell.setCellValue(sourceCell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			destCell.setCellFormula(sourceCell.getCellFormula());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			destCell.setCellValue(sourceCell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_ERROR:
			sourceCell.setCellErrorValue(sourceCell.getErrorCellValue());
			break;
		default:
			destCell.setCellValue(sourceCell.getStringCellValue());
		}
		return destCell;
	}
}
