package org.ljdp.component.bean;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class BaseVO {
	
	private String key;//主键，react的设计中使用

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	public String toStringMultiLine() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public String toStringShortPrefix() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public String toStringSimple() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public static void setKey(List<?> list, String idfield) {
		for (Object v : list) {
			if(v instanceof BaseVO) {
				BaseVO bv = (BaseVO)v;
				if(bv.getKey() == null) {
					try {
						Object id = PropertyUtils.getSimpleProperty(v, idfield);
						if(id != null) {
							bv.setKey(id.toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					Object id = PropertyUtils.getSimpleProperty(v, idfield);
					if(id != null) {
						PropertyUtils.setProperty(v, "key", id.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
