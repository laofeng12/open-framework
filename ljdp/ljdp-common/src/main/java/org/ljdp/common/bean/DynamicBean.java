package org.ljdp.common.bean;

import java.util.ArrayList;

import net.sf.cglib.beans.BeanGenerator;

public class DynamicBean {

	@SuppressWarnings("rawtypes")
	public static Class createClass(ArrayList<DynamicField> dyFields) {
		BeanGenerator beanGener = buildGenerator(dyFields);
		return (Class)beanGener.createClass();
	}
	
	public static Object create(ArrayList<DynamicField> dyFields) {
		BeanGenerator beanGener = buildGenerator(dyFields);
		return beanGener.create();
	}

	@SuppressWarnings("rawtypes")
	public static BeanGenerator buildGenerator(ArrayList<DynamicField> dyFields) {
		BeanGenerator beanGener = new BeanGenerator();
		for(DynamicField dyField : dyFields) {
			Class type;
			String ftype = dyField.getBaseType();
			if(ftype.equals(FieldType.BASE_CHAR)
					|| ftype.equals(FieldType.BASE_STRING)) {
				type = String.class;
			} else if(ftype.equals(FieldType.BASE_NUMBER) && dyField.getDecimalRange() > 0
					|| ftype.equals(FieldType.BASE_FLOAT)) {
				type = Float.class;
			} else if(ftype.equals(FieldType.BASE_DATE) || ftype.equals(FieldType.BASE_TIMESAMP)) {
				type = java.util.Date.class;
			} else {
				type = String.class;
			}
			beanGener.addProperty(dyField.getName(), type);
//			if(type.equals(java.util.Date.class)) {
//				String pagename = NameUtils.toPageName(dyField.getName());
//				beanGener.addProperty(pagename, String.class);
//			}
		}
		return beanGener;
	}

}
