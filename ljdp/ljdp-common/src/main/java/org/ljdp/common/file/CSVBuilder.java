package org.ljdp.common.file;

/**
 * CVS(逗号分割)文本构建器
 * 
 * @author Liu Chuanfeng
 * 
 */
public class CSVBuilder extends TextFileBuilder {

	public CSVBuilder() {
		super(",", ",", "\r\n");
	}

}
