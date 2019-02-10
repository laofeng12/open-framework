package org.ljdp.common.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.ljdp.common.bean.DynamicField;
import org.ljdp.common.bean.TableHead;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.transform.MapTransformer;

public abstract class AbstractFileBuilder implements FileBuilder {
	protected ArrayList<DynamicField> properties = new ArrayList<DynamicField>();
	protected FileInfo fileInfo;
	
	protected List<List<TableHead>> heads = new ArrayList<List<TableHead>>();

	//-------------列显示长度由ExcelBuilder自动设置-------------
	
	public void addProperty(String desc) {
		addProperty(null, desc, 0);
	}
	
	public void addProperty(String name, String desc) {
		addProperty(name, desc, 0);
	}
	
	public void addProperty(String name, String desc, String type) {
		addProperty(name, desc, type, 0);
	}

	public void addProperty(String name, String desc, String type, String format) {
		addProperty(name, desc, type, format, 0);
	}

	public void addProperty(String name, String desc, Transformer transformer) {
		addProperty(name, desc, transformer, 0);
	}
	
	@SuppressWarnings("rawtypes")
	public void addProperty(String name, String desc, Map map) {
		addProperty(name, desc, map, 0);
	}

	//-------------需要设置列显示长度，在导出Excel时有用-------------
	
	public void addProperty(String desc, int length) {
		addProperty(null, desc, length);
	}
	
	public void addProperty(String name, String desc, int length) {
		DynamicField pro = new DynamicField(name, null, desc);
		pro.setMaxLength(length);
		properties.add(pro);
	}

	public void addProperty(String name, String desc, String type, int length) {
		DynamicField pro = new DynamicField(name, null, desc, type);
		pro.setMaxLength(length);
		properties.add(pro);
	}

	public void addProperty(String name, String desc, String type, String format, int length) {
		DynamicField pro = new DynamicField(name, null, desc, type);
		pro.setCustomizeType(format);
		pro.setMaxLength(length);
		properties.add(pro);
	}

	public void addProperty(String name, String desc, Transformer transformer, int length) {
		DynamicField pro = new DynamicField(name, null, desc);
		pro.setTransformer(transformer);
		pro.setMaxLength(length);
		properties.add(pro);
	}
	
	@SuppressWarnings("rawtypes")
	public void addProperty(String name, String desc, Map map, int length) {
		DynamicField pro = new DynamicField(name, null, desc);
		pro.setTransformer(new MapTransformer(map));
		pro.setMaxLength(length);
		properties.add(pro);
	}

	public void addProperty(DynamicField field) {
		properties.add(field);
	}
	
	public DynamicField getLastProperty() {
		return properties.get(properties.size() - 1);
	}
	
	public void resetProperty() {
		properties.clear();
	}
	
	public void setAutoMerge(String name, boolean a) {
		for (int i = 0; i < properties.size(); i++) {
			DynamicField f = (DynamicField) properties.get(i);
			if(f.getName().equals(name)) {
				f.setAutoMerge(a);
			}
		}
	}
	
	//--------------------------设置标题头------------
	
	public void addHead(int index, String name, int colspan, int rowspan) {
		if(index < 1 || colspan < 1) {
			throw new IllegalArgumentException("参数不对");
		}
		if(heads.size() < index) {
			for(int i = index - heads.size(); i > 0; --i) {
				heads.add(new ArrayList<TableHead>());
			}
		}
		List<TableHead> list = heads.get(index-1);
		list.add(new TableHead(name, colspan, rowspan));
	}
	
	public void addHead(int index, String name, int colspan) {
		addHead(index, name, colspan, 1);
	}
	
	public void addHead(int index, String name) {
		addHead(index, name, 1);
	}
	
	public void addBlankHead(int index) {
		addBlankHead(index, 1);
	}
	
	public void addBlankHead(int index, int colspan) {
		addHead(index, "", colspan, 0);
	}

	public abstract void build(OutputStream os, Collection<?> datas)
			throws Exception;
	
	public Result buildFile(String filename, Collection<?> datas) {
		Result result = new GeneralResult();
		try {
			File file = new File(filename);
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
			build(os, datas);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}
	
	public void resetFileInfo() {
		fileInfo = null;
	}

}
