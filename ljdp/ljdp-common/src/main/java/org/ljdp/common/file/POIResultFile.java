package org.ljdp.common.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIResultFile extends AbstractResultFile {
	private Workbook workbook;
	private Sheet successSheet;
	private Sheet[] errorSheet;
	private Integer[] errorSerial;
	private int sheetNumber;
	
	
	public POIResultFile() {
    	
    }
	
	public void initialize(String impoFileName) {
		this.initialize(impoFileName, 1);
	}
    
    public void initialize(String impoFileName, int sheetNumber) {
        this.all_count = 0;
        this.ok_count = 0;
        this.fail_count = 0;
        this.sheetNumber = sheetNumber;
        if(null == contentType) {
        	contentType = ContentType.getContentType(impoFileName);
        }
        if(contentType.equals(ContentType.EXCELX)) {
        	resultFileName = impoFileName.replaceFirst("\\.xlsx$", "_result.xlsx");            
        	sucResultFileName = resultFileName;
        	errResultFileName = resultFileName;
        } else {
        	throw new RuntimeException("只支持Excel2007 以上版本");
        }
    }
    
    public void openFile() throws IOException {
    	this.openFile(true);
    }
    
    public void openFile(boolean createsheet) throws IOException {
    	workbook = new XSSFWorkbook();
    	errorSheet = new Sheet[sheetNumber];
    	errorSerial = new Integer[sheetNumber];
    	for(int i = 0; i < sheetNumber; ++i) {
    		errorSerial[i] = 1;//默认从第一行开始写
    	}
    	if(createsheet) {
    		for(int i = 1; i <= sheetNumber; ++i) {
    			errorSheet[i-1] = workbook.createSheet("失败记录："+i);
    		}
    	}
    	if(needSuccFile) {
    		successSheet = workbook.createSheet("成功记录");
    	}
    }
    
    public void createErrorSheet(int sheet, String name) {
    	errorSheet[sheet] = workbook.createSheet("[失败记录"+(sheet+1)+"]："+name);
    }
    
    public void writeTitle(String title) {
    	String[] items = title.split("\\|");
    	Row errRow = null;
    	if(errorSheet[0] != null) {
    		errRow = errorSheet[0].createRow((short)fail_count);
    	}
    	Row succRow = null;
    	if(needSuccFile) {
    		succRow = successSheet.createRow((short)ok_count);
    	}
		for(int i = 0; i < items.length; ++i) {
			if(needSuccFile) {
				Cell cell = succRow.createCell(i);
				cell.setCellValue(items[i]);
			}
			if(errRow != null) {
				Cell cell = errRow.createCell(i);
				cell.setCellValue(items[i]);
			}
		}
    }
    
    /**
     * 在成功的sheet创建一新行，并把给定其他Row的值复制进来了
     * @param sourceRow
     * @param extInfos
     */
    public void writeSuccessRecord(Row sourceRow, String[] extInfos) {
    	if(needSuccFile) {
    		Iterator<Cell> it = sourceRow.cellIterator();
    		Row newRow = successSheet.createRow(getSuccessSerial());
    		int i = 0;
    		while (it.hasNext()) {
				Cell cell = (Cell) it.next();
				if(cell != null) {
					Cell newCell = newRow.createCell(cell.getColumnIndex());
					POIExcelUtil.copyCell(newCell, cell);
				} else {
					newRow.createCell(i).setCellValue("-null");
				}
    			i++;
			}
    		newRow.createCell(i).setCellValue(extInfos[0]);
        }
        this.all_count++;
        this.ok_count++;
    }
    
    public void writeErrorRecord(Row sourceRow, String[] extInfos) {
    	this.writeErrorRecord(0, sourceRow, extInfos);
    }
    /**
     * 在失败的sheet创建一新行，并把给定其他Row的值复制进来了
     * @param sourceRow
     * @param extInfos
     */
    public void writeErrorRecord(int currentSheet, Row sourceRow, String[] extInfos) {
    	Iterator<Cell> it = sourceRow.cellIterator();
		Row newRow = errorSheet[currentSheet].createRow(getNextErrorSerial(currentSheet));
		int i = 0;
		while (it.hasNext()) {
			Cell cell = (Cell) it.next();
			Cell newCell = newRow.createCell(cell.getColumnIndex());
			POIExcelUtil.copyCell(newCell, cell);
			i++;
		}
		newRow.createCell(i).setCellValue(extInfos[0]);
		newRow.createCell(i+1).setCellValue(extInfos[1]);
		newRow.createCell(i+2).setCellValue(extInfos[2]);
		
        this.all_count++;
        this.fail_count++;
    }
    
    public int getNextErrorSerial(int currentSheet) {
    	int s = errorSerial[currentSheet];
    	errorSerial[currentSheet] += 1;
    	return s;
    }
    
    public void close() throws IOException {
    	FileOutputStream out = new FileOutputStream(new File(resultFileName));
    	workbook.write(out);
    	out.close();
    }
}
