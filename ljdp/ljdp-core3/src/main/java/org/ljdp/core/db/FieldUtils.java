package org.ljdp.core.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

public class FieldUtils {

	/**
	 * 获取实体定义的字段与数据字段名称关系
	 * Map<key：实体字段名称，value:数据库字段名称>
	 * @param cls
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Map<String, String> getDeclaredColumns(Class<?> cls) throws NoSuchMethodException, SecurityException{
		Map<String, String> map = new HashMap<>();
		Field[] f = cls.getDeclaredFields();
		for (int i = 0; i < f.length; i++) {
			String n = f[i].getName();
			String m = "get"+n.substring(0, 1).toUpperCase()+""+n.substring(1, n.length());
			Method method = cls.getDeclaredMethod(m);
			if(method != null) {
				Column c = method.getDeclaredAnnotation(Column.class);
				if(c != null) {
					map.put(n, c.name());
				}
			}
		}
		return map;
	}
}
