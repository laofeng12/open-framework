package org.ljdp.plugin.batch.check;

import java.util.ArrayList;

import org.ljdp.common.bean.DynamicField;

import jxl.Cell;

public abstract class AbstractCheckFormat implements CheckFormat {
	protected int beginIndex = 1;//默认从第一行开始检查，如果第一行是标题，请设置为2
	protected int passCount = 0;//成功通过检查的数量
	protected int currentRow = 0;//当前检查的到的行

	public void checkLine(String line) throws FormatException {
		throw new UnsupportedOperationException("不支持固定字段的字符行检查");
	}

	public void checkLine(String line, ArrayList<DynamicField> fieldList)
			throws FormatException {
		throw new UnsupportedOperationException("不支持动态字段的字符行检查");
	}

	public void checkLine(Cell[] cells) throws FormatException {
		throw new UnsupportedOperationException("不支持固定字段的Excel行检查");
	}

	public void checkLine(Cell[] cells, ArrayList<DynamicField> fieldList)
			throws FormatException {
		throw new UnsupportedOperationException("不支持动态字段的Excel行检查");
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

}
