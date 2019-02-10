package org.ljdp.module.transform;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.transform.StringTransformer;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.query.RO;
import org.ljdp.core.service.CommonService;
import org.ljdp.core.service.ServiceFactory;

public class SingleTableTransformer implements StringTransformer {

	private CommonService<?> server;
	private String codeField;
	private String nameField;
	private String defaultSQL;
	@SuppressWarnings("rawtypes")
	private Class codeCls;
	
	public SingleTableTransformer(Class<?> cls, String codeField, String nameField) throws Exception{
		server = ServiceFactory.buildCommon(cls);
		this.codeField = codeField;
		this.nameField = nameField;
		this.codeCls = cls.getDeclaredField(codeField).getType();
	}
	
	@SuppressWarnings("rawtypes")
	public String transform(Object value) {
		if(value == null) {
			return "";
		}
		DBQueryParam params = new DBQueryParam();
		params.setReturnType(DBQueryParam.TYPE_DEFAULT);
		params.addSelectField(nameField);
		if( !codeCls.equals(value.getClass()) ) {
			try {
				Constructor defCs = codeCls.getConstructor(String.class);
				value = defCs.newInstance(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		params.addQueryCondition(codeField, RO.EQ, value);
		if(StringUtils.isNotBlank(defaultSQL)) {
			params.addQueryCondition("defaultSQL", RO.SQL, defaultSQL);
		}
		try {
			Collection col = server.doDatas(params);
			Iterator it = col.iterator();
			if(it.hasNext()) {
				Object result = it.next();
				if(result != null) {
					return result.toString();
				}
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(value instanceof String || value instanceof Integer
				|| value instanceof Long || value instanceof Short
				|| value instanceof Byte) {
			return value.toString();
		}
		return null;
	}

	public String getDefaultSQL() {
		return defaultSQL;
	}

	public void setDefaultSQL(String defaultSQL) {
		this.defaultSQL = defaultSQL;
	}

}
