package org.ljdp.plugin.batch.check;

import java.util.ArrayList;

import org.ljdp.common.bean.DynamicField;

import jxl.Cell;

public interface CheckFormat {
	public void checkLine(String line) throws FormatException;
	
	public void checkLine(String line, ArrayList<DynamicField> fieldList) throws FormatException;
	
	public void checkLine(Cell[] cells) throws FormatException;
	
	public void checkLine(Cell[] cells, ArrayList<DynamicField> fieldList) throws FormatException;

	/**
	 * 从第几行开始检查,默认从第一行开始
	 * @return
	 */
	public int getBeginIndex();

	public void setBeginIndex(int beginIndex);

	public int getPassCount();

	public void setPassCount(int passCount);

	public int getCurrentRow();

	public void setCurrentRow(int currentRow);
	
}
