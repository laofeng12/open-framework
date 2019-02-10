package org.ljdp.component.namespace;

public interface FileNameService {
	/**
	 * 创建一个新的文件名
	 * @param origFileName 原文件名
	 * @param operCode 操作员
	 * @return 新文件的路径名
	 */
	public String createFileName(String origFileName, String operCode);
	
}
