package org.ljdp.ui.common;

import org.ljdp.component.transform.StringTransformer;

public class FieldTranslator {
	/**
	 * 待翻译的字段
	 */
	private String codeField;
	/**
	 * 翻译后存放的字段
	 */
	private String nameField;
	/**
	 * 转换器
	 */
	private StringTransformer transformer;
	
	public FieldTranslator(String codeField, String nameField,
			StringTransformer transformer) {
		this.codeField = codeField;
		this.nameField = nameField;
		this.transformer = transformer;
	}

	public String getCodeField() {
		return codeField;
	}

	public void setCodeField(String codeField) {
		this.codeField = codeField;
	}

	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}

	public StringTransformer getTransformer() {
		return transformer;
	}

	public void setTransformer(StringTransformer transformer) {
		this.transformer = transformer;
	}
	
}
