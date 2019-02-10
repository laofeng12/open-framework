package org.ljdp.common.bean;

public class TableHead {
	private String name;
	private int colspan;
	private int rowspan;
	private int beginCol;
	
	public TableHead(int beginCol, String name, int colspan, int rowspan) {
		super();
		this.beginCol = beginCol;
		this.name = name;
		this.colspan = colspan;
		this.rowspan = rowspan;
	}
	
	public TableHead(String name, int colspan, int rowspan) {
		super();
		this.name = name;
		this.colspan = colspan;
		this.rowspan = rowspan;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	public int getBeginCol() {
		return beginCol;
	}
	public void setBeginCol(int beginCol) {
		this.beginCol = beginCol;
	}
}
