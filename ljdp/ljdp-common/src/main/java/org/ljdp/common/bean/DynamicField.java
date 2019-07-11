package org.ljdp.common.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.collections.Transformer;

public class DynamicField {
	private String column;//字段名,对应数据库中的字段
	private String name_cn;//字段中文名
	private String baseType;//基本类型，如STRING,NUMBER,DATE
	private String customizeType;//自定义类型
	private int maxLength;
	private String name; //字段名，对应Java对象中的字段
	private int decimalRange; // 小数位数
	private long fieldID;
	
	private Transformer transformer;//数据翻译，转换
	
	private String encode;//编码,用此编码检查字符串是否符合长度
	
	private boolean light = false;//高亮显示此字段
	
	private boolean autoMerge = false;//自动对相同数据合并
	
	public DynamicField() {
		
	}
	
	public DynamicField(String name, String column, String name_cn, String baseType,
			String customizeType) {
		super();
		this.name = name;
		this.column = column;
		this.name_cn = name_cn;
		this.baseType = baseType;
		this.customizeType = customizeType;
	}
	
	public DynamicField(String name, String column, String name_cn, String baseType) {
		this(name, column, name_cn, baseType, null);
	}
	
	public DynamicField(String name, String column, String name_cn) {
		this(name, column, name_cn, null);
	}
	
	public DynamicField(String name, String column) {
		this(name, column, null);
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getName_cn() {
		return name_cn;
	}

	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}

	public String getBaseType() {
		return baseType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	public String getCustomizeType() {
		return customizeType;
	}

	public void setCustomizeType(String customizeType) {
		this.customizeType = customizeType;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DynamicField) {
			DynamicField odf = (DynamicField)obj;
			return new EqualsBuilder().append(getColumn(), odf.getColumn()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getColumn()).toHashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getDecimalRange() {
		return decimalRange;
	}

	public void setDecimalRange(int decimalRange) {
		this.decimalRange = decimalRange;
	}

	public long getFieldID() {
		return fieldID;
	}

	public void setFieldID(long fieldID) {
		this.fieldID = fieldID;
	}

	public Transformer getTransformer() {
		return transformer;
	}

	public void setTransformer(Transformer transformer) {
		this.transformer = transformer;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public boolean isLight() {
		return light;
	}

	public void setLight(boolean light) {
		this.light = light;
	}

	public boolean isAutoMerge() {
		return autoMerge;
	}

	public void setAutoMerge(boolean autoMerge) {
		this.autoMerge = autoMerge;
	}

	
}
