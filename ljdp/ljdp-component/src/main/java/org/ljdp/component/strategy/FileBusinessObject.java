package org.ljdp.component.strategy;

import jxl.Cell;

import org.ljdp.component.result.BatchResult;

/**
 * 处理文件数据的业务对象接口
 * @author Administrator
 *
 */
public interface FileBusinessObject {
	public String getTitle();

	public void destory();

	public Boolean initialization();
	
	/**
	 * 所有子任务完成后的最后处理工作，返回整个任务是否成功完成，
	 * 只要有一个子任务失败，则应该返回false;
	 * @return
	 */
	public Boolean finalWork();

//	public BatchResult doProcessRecord(String[] line, int size);
	
	public BatchResult doProcessRecord(int sheetLocation, String[] line, int size);

	public BatchResult doProcessRecord(Cell[][] row, int size);
}
