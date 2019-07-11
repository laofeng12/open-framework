package org.ljdp.common.file;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.DynamicField;
import org.ljdp.common.bean.FieldType;
import org.ljdp.util.DateFormater;

/**
 * 基于FileBuilder接口的文本文件构造器
 * 
 */
public class TextFileBuilder extends AbstractFileBuilder {
	protected String tSplitChar = "|";
	protected String dSplitChar = "|";
	protected String lineSeparator = "\r\n";

	public TextFileBuilder() {
	}

	public TextFileBuilder(String tSplitChar, String dSplitChar) {
		this.tSplitChar = tSplitChar;
		this.dSplitChar = dSplitChar;
	}

	public TextFileBuilder(String tSplitChar, String dSplitChar,
			String lineSeparator) {
		this.tSplitChar = tSplitChar;
		this.dSplitChar = dSplitChar;
		this.lineSeparator = lineSeparator;
	}

	public void build(OutputStream os, Collection<?> datas) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
		if (fileInfo == null) {
			fileInfo = new FileInfo();
		}
		
//		int currentRow = 0;
		Writer writer = new OutputStreamWriter(os);
		// print title
		writeTitle(writer);

		// print datas
		writeDatas(writer, datas);
		
	}

	public void writeTitle(Writer writer) throws IOException {
		if (properties.size() > 0) {
			for (int i = 0; i < properties.size(); i++) {
				if (fileInfo.isShowColumnTitle()) {
					writer.write((properties.get(i)).getName_cn());
					if (i < properties.size() - 1) {
						writer.write(tSplitChar);
					}
				}
			}
			if (fileInfo.isShowColumnTitle()) {
				writer.write(lineSeparator);
//				++currentRow;
			}
		}
	}

	public void writeDatas(Writer writer, Collection<?> datas)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, IOException {
		if (!datas.isEmpty()) {
			Object element, value = null;
			DynamicField field;
			String valStr;
			for (Iterator<?> iterator = datas.iterator(); iterator.hasNext(); /*++currentRow*/) {
				element = iterator.next();
				for (int i = 0; i < properties.size(); i++) {
					field = properties.get(i);
					if(StringUtils.isNotEmpty(field.getName())) {
						value = PropertyUtils.getProperty(element, field.getName());
					}
					if (value == null) {
						value = "";
					}
					valStr = value.toString();
					if (field.getTransformer() != null) {
						Object valObj = field.getTransformer().transform(value);
						if(valObj != null) {
							valStr = valObj.toString();
						}
					} else if (StringUtils.isNotBlank(field.getBaseType())) {
						if (field.getBaseType().equals(FieldType.BASE_DATE)
								|| field.getBaseType().equals(
										FieldType.BASE_TIMESAMP)) {
							valStr = DateFormater.format(value, field.getCustomizeType());
						} else if(field.getBaseType().equals(FieldType.BASE_FLOAT)) {
							NumberFormat nf = NumberFormat.getNumberInstance();
							nf.setMaximumFractionDigits(2);
							nf.setGroupingUsed(false);
							valStr = nf.format(value);
						} else if(field.getBaseType().equals(FieldType.BASE_INTEGER)) {
							NumberFormat nf = NumberFormat.getIntegerInstance();
							nf.setGroupingUsed(false);
							valStr = nf.format(value);
						}
					}
					writer.write(valStr);
					if (i < properties.size() - 1) {
						writer.write(dSplitChar);
					}
				}
				writer.write(lineSeparator);
			}
		}
		writer.flush();
	}
}
