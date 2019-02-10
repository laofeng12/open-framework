package org.ljdp.common.file;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.ljdp.common.bean.DynamicField;
import org.ljdp.component.result.Result;

public interface FileBuilder {
	//-------------列显示长度由ExcelBuilder自动设置-------------
	
	public void addProperty(String name, String desc, String type);

	public void addProperty(String name, String desc, String type, String format);

	public void addProperty(String name, String desc, Transformer transformer);

	public void addProperty(String name, String desc);
	
	@SuppressWarnings("rawtypes")
	public void addProperty(String name, String desc, Map map);

	//-------------需要设置列显示长度，在导出Excel时有用-------------
	
	public void addProperty(String name, String desc, int length);

	public void addProperty(String name, String desc, String type,
			int length);

	public void addProperty(String name, String desc, String type,
			String format, int length);

	public void addProperty(String name, String desc, Transformer transformer,
			int length);
	
	@SuppressWarnings("rawtypes")
	public void addProperty(String name, String desc, Map map, int length);

	public void addProperty(DynamicField field);

	public FileInfo getFileInfo();

	public void setFileInfo(FileInfo excelInfo);

	public void build(OutputStream os, Collection<?> datas) throws Exception;
	
	public Result buildFile(String filename, Collection<?> datas) throws Exception;

}
