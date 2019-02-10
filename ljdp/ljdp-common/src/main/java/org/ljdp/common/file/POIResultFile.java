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
	private Sheet errorSheet;
	
	public POIResultFile() {
    	
    }
    
    public void initialize(String impoFileName) {
        this.all_count = 0;
        this.ok_count = 0;
        this.fail_count = 0;
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
    	workbook = new XSSFWorkbook();
    	errorSheet = workbook.createSheet("失败记录");
    	if(needSuccFile) {
    		successSheet = workbook.createSheet("成功记录");
    	}
    }
    
    public void writeTitle(String title) {
    	String[] items = title.split("\\|");
    	Row errRow = errorSheet.createRow((short)fail_count);
    	Row succRow = null;
    	if(needSuccFile) {
    		succRow = successSheet.createRow((short)ok_count);
    	}
		for(int i = 0; i < items.length; ++i) {
			if(needSuccFile) {
				Cell cell = succRow.createCell(i);
				cell.setCellValue(items[i]);
			}
			Cell cell = errRow.createCell(i);
			cell.setCellValue(items[i]);
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
    
    /**
     * 在失败的sheet创建一新行，并把给定其他Row的值复制进来了
     * @param sourceRow
     * @param extInfos
     */
    public void writeErrorRecord(Row sourceRow, String[] extInfos) {
    	Iterator<Cell> it = sourceRow.cellIterator();
		Row newRow = errorSheet.createRow(getErrorSerial());
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
    
    public void close() throws IOException {
    	FileOutputStream out = new FileOutputStream(new File(resultFileName));
    	workbook.write(out);
    	out.close();
    }
}
