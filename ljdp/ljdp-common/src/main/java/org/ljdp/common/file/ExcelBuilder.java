package org.ljdp.common.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.DynamicField;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.bean.TableHead;

public class ExcelBuilder extends AbstractFileBuilder {
	
	private int sheets = 0;
	private WritableWorkbook xlsWb;
	
	public ExcelBuilder() {
	}
	
	public ExcelBuilder(OutputStream os) throws IOException {
		xlsWb = Workbook.createWorkbook(os);
		fileInfo = new  FileInfo();
	}
	
	public void finish() {
		if(xlsWb != null) {
			try {
				xlsWb.write();
				xlsWb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setSheetName(String name) {
		if(fileInfo == null) {
			fileInfo = new  FileInfo();
		}
		fileInfo.setSheetName(name);
	}
	
	public void setSheetName(String name, int position) {
		this.sheets = position;
		setSheetName(name);
	}
	
	@SuppressWarnings("rawtypes")
	public void build(Collection datas) throws Exception{
		if(xlsWb == null) {
			throw new Exception("未初始化文件，参考：ExcelBuilder(OutputStream os)");
		}
		WritableSheet ws = xlsWb.createSheet(fileInfo.getSheetName(), sheets);
	    sheets++;
	    
	    buildSheet(ws, datas);
	}
	
	public WritableSheet createSheet(String name) {
		WritableSheet ws = xlsWb.createSheet(name, sheets);
	    sheets++;
	    return ws;
	}
	
	@SuppressWarnings("rawtypes")
	public void build(String sheetName, Collection datas) throws Exception{
		setSheetName(sheetName);
		build(datas);
	}
	
	@SuppressWarnings("rawtypes")
	public void build(OutputStream os, Collection datas) throws Exception{
		if(fileInfo == null) {
			fileInfo = new  FileInfo();
		}
		if(StringUtils.isEmpty(fileInfo.getSheetName())) {
			fileInfo.setSheetName("sheet0");
		}
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(os);
		    WritableSheet ws = wwb.createSheet(fileInfo.getSheetName(), 0);
		    sheets++;
		    
		    buildSheet(ws, datas);
		    
		    wwb.write();
		} finally {
			if(wwb != null) {
				wwb.close();
			}
//			if(os != null) {
//				os.close();
//			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void build(OutputStream os, InputStream ins, Collection datas) throws Exception{
		if(fileInfo == null || StringUtils.isEmpty(fileInfo.getSheetName())) {
			System.out.println("请设置导出Excel信息");
			return;
		}
		WritableWorkbook wwb = null;
		try {
			Workbook wb = Workbook.getWorkbook(ins);
			wwb = Workbook.createWorkbook(os, wb);
		    WritableSheet ws = wwb.createSheet(fileInfo.getSheetName(), sheets);
		    sheets++;
		    
		    buildSheet(ws, datas);
		    
		    wwb.write();
		} finally {
			if(wwb != null) {
				wwb.close();
			}
			if(ins != null) {
				ins.close();
			}
//			if(os != null) {
//				os.close();
//			}
		}
	}
	@SuppressWarnings("rawtypes")
	public int buildSheet(WritableSheet ws, Collection datas)
			throws WriteException, RowsExceededException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		return buildSheet(ws, datas, 0);
	}

	@SuppressWarnings("rawtypes")
	public int buildSheet(WritableSheet ws, Collection datas, int currentRow)
			throws WriteException, RowsExceededException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		//print title
		if(StringUtils.isNotEmpty(fileInfo.getTitle())) {
			WritableFont font = new WritableFont(WritableFont.TIMES,14,WritableFont.BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(jxl.format.Alignment.CENTRE);
			wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			ws.mergeCells(0, currentRow, properties.size()-1, 2);
			currentRow = currentRow+3;
			Label label = new Label(0, 0, fileInfo.getTitle(), wcf);
			ws.addCell(label);
		}
		
		//print query info
		if(StringUtils.isNotEmpty(fileInfo.getQueryInfo())) {
			WritableFont font = new WritableFont(WritableFont.TIMES,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);		
			wcf.setAlignment(jxl.format.Alignment.CENTRE);
			wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			ws.mergeCells(0, currentRow, properties.size()-1, currentRow+1);
			Label label = new Label(0, currentRow, fileInfo.getQueryInfo(), wcf);
			currentRow = currentRow+2;
			ws.addCell(label);
		}
		
		//print total info
		if(StringUtils.isNotEmpty(fileInfo.getTotalInfo())){
			WritableFont font = new WritableFont(WritableFont.TIMES,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
		    wcf.setAlignment(jxl.format.Alignment.CENTRE);
		    ws.mergeCells(0, currentRow, properties.size()-1, currentRow+1);
		    Label label = new Label(0, currentRow, fileInfo.getTotalInfo(), wcf);
		    currentRow = currentRow+2;
			ws.addCell(label);
		}
		
		//print column head
		if(heads.size() > 0) {
			WritableFont font = new WritableFont(WritableFont.TIMES,10,WritableFont.BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(jxl.format.Alignment.CENTRE);
			wcf.setBackground(jxl.format.Colour.GREY_25_PERCENT);
			wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			for(int i = 0; i < heads.size(); ++i) {
				int column = 0;
				List<TableHead> head = heads.get(i);
				for(int j = 0; j < head.size(); ++j) {
					TableHead th = head.get(j);
					if(th.getRowspan() != 0) {
						if(th.getRowspan() < 0) {
							//往上合并
							int row = currentRow + th.getRowspan() + 1;
							ws.mergeCells(column, row, column+th.getColspan()-1, currentRow);
							Label label = new Label(column, row, th.getName(), wcf);
							ws.addCell(label);
						} else {
							//往下合并
							ws.mergeCells(column, currentRow, column+th.getColspan()-1, currentRow+th.getRowspan()-1);
							Label label = new Label(column, currentRow, th.getName(), wcf);
							ws.addCell(label);
						}
					}
					column += th.getColspan();
				}
				++currentRow;
			}
		}
		
		 //print property title
		if( properties.size() > 0 ) {
			WritableFont font = new WritableFont(WritableFont.TIMES,10,WritableFont.BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setBackground(jxl.format.Colour.GREY_25_PERCENT);
			wcf.setAlignment(jxl.format.Alignment.CENTRE);
			wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			for (int i = 0; i < properties.size(); i++) {
				DynamicField field = properties.get(i);
				int ilength = field.getMaxLength();
				if( ilength > 0) {
					ws.setColumnView(i,ilength);
				} else if(properties.size()>5) {
					ws.setColumnView(i,18);
				} else {
					ws.setColumnView(i,28);
				}
				if(fileInfo.isShowColumnTitle()) {
					Label label = new Label(i, currentRow, field.getName_cn(), wcf);
					ws.addCell(label);
				}
			}
			if(fileInfo.isShowColumnTitle()) {
				++currentRow;
			}
		}
		
		// print datas
		if (!datas.isEmpty()) {
			int dataBeginRow = currentRow;
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(jxl.format.Alignment.CENTRE);
			wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			wcf.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			for (Iterator iterator = datas.iterator(); iterator.hasNext(); ++currentRow) {
				Object element = iterator.next();
				WritableCellFormat wcformat = wcf;
				if(fileInfo.isLightLastRow() && !iterator.hasNext()) {
					wcformat = new WritableCellFormat();
					wcformat.setAlignment(jxl.format.Alignment.CENTRE);
					wcformat.setBorder(jxl.format.Border.ALL,
							jxl.format.BorderLineStyle.THIN);
					wcformat.setBackground(jxl.format.Colour.LIGHT_GREEN);
				}
				buildRowData(ws, currentRow, element, wcformat);
			}
			//merge column
			for (int i = 0; i < properties.size(); i++) {
				DynamicField field = properties.get(i);
				if(field.isAutoMerge()) {
					int begin = dataBeginRow;
					int end = begin;
					Iterator it = datas.iterator();
					String begindata = MyBeanUtils.getElementPropertyValue(it.next(), i, field);;
					String enddata = begindata;
					while(it.hasNext()) {
						++end;
						enddata = MyBeanUtils.getElementPropertyValue(it.next(), i, field);
						if( ! enddata.equals(begindata) ) {
							if(end - begin > 1) {
								ws.mergeCells(i, begin, i, end-1);
							}
							begin = end;
							begindata = enddata;
						}
					}
					++end;
					if(end - begin > 1) {
						ws.mergeCells(i, begin, i, end-1);
					}
				}
			}
		}
		
  // print column total info
		if (fileInfo.getColumnTotalInfo() != null) {
			Object element = fileInfo.getColumnTotalInfo();
			WritableFont font = new WritableFont(WritableFont.TIMES, 10,
					WritableFont.BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setBackground(jxl.format.Colour.LIGHT_GREEN);
			wcf.setAlignment(jxl.format.Alignment.CENTRE);
			wcf.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			buildRowData(ws, currentRow, element, wcf);
			++currentRow;
		}
		
		//print buttom info
		if(StringUtils.isNotEmpty(fileInfo.getButtomInfo())) {
			WritableFont font = new WritableFont(WritableFont.TIMES,8,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setBackground(jxl.format.Colour.ICE_BLUE);
			wcf.setAlignment(jxl.format.Alignment.CENTRE);
		    ws.mergeCells(0, currentRow, properties.size()-1, currentRow);
		    Label label = new Label(0, currentRow, fileInfo.getButtomInfo(), wcf);
			ws.addCell(label);
		}
		return currentRow;
	}

	private void buildRowData(final WritableSheet ws, final int currentRow, Object element, 
			final WritableCellFormat wcf)
			throws WriteException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			RowsExceededException {
		for (int i = 0; i < properties.size(); i++) {
			WritableCellFormat wcformat = wcf;
//			if (i == 0) {
//				wcf.setAlignment(jxl.format.Alignment.LEFT);
//			} else {
//				wcf.setAlignment(jxl.format.Alignment.CENTRE);
//			}
			DynamicField field = properties.get(i);
			if(field.isLight()) {
				wcformat = new WritableCellFormat();
				wcformat.setAlignment(wcf.getAlignment());
				wcformat.setBorder(jxl.format.Border.ALL,
						wcf.getBorder(jxl.format.Border.BOTTOM));
				wcformat.setBackground(jxl.format.Colour.LIGHT_GREEN);
			}
			String valStr = MyBeanUtils.getElementPropertyValue(element, i, field);
			Label label = new Label(i, currentRow, valStr, wcformat);
			ws.addCell(label);
		}
	}


}