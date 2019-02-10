package org.ljdp.common.file;

/**
 * 导出文件的信息
 * @author Hzy
 *
 */
public class FileInfo {
	private String title;
	private String queryInfo;
	private String totalInfo;
	private String filename;
	private String sheetName;
	private Object columnTotalInfo;
	private String buttomInfo;
	private boolean showColumnTitle = true;//是否显示标题
	private boolean lightLastRow = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQueryInfo() {
		return queryInfo;
	}

	public void setQueryInfo(String queryInfo) {
		this.queryInfo = queryInfo;
	}

	public String getTotalInfo() {
		return totalInfo;
	}

	public void setTotalInfo(String totalInfo) {
		this.totalInfo = totalInfo;
	}

	public Object getColumnTotalInfo() {
		return columnTotalInfo;
	}

	public void setColumnTotalInfo(Object data) {
		this.columnTotalInfo = data;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isShowColumnTitle() {
		return showColumnTitle;
	}

	public void setShowColumnTitle(boolean titleEnable) {
		this.showColumnTitle = titleEnable;
	}

	public String getButtomInfo() {
		return buttomInfo;
	}

	public void setButtomInfo(String buttomInfo) {
		this.buttomInfo = buttomInfo;
	}

	public boolean isLightLastRow() {
		return lightLastRow;
	}

	public void setLightLastRow(boolean lightLastRow) {
		this.lightLastRow = lightLastRow;
	}
}
