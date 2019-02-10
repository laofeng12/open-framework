package org.ljdp.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public class DataWrapper {
	private static final String asOpr = " as ";
	
	/**
	 * 把数组类型的数据包装成对象.
	 * @param datas
	 * @param selectFields
	 * @param jopoClass
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void toJOPO(Collection datas, List<String> selectFields, Class jopoClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Collection resList = new ArrayList(datas.size());
		Iterator it = datas.iterator();
		while(it.hasNext()) {
			Object result = it.next();
			Object jopo = jopoClass.newInstance();
			if( selectFields.size() > 1 ) {
				Object[] fields = (Object[])result;
				for(int i = 0; i < selectFields.size(); i++) {
					String name = selectFields.get(i);
					if(name.indexOf(asOpr) != -1) {
						name = name.substring(0, name.indexOf(asOpr));
					}
					PropertyUtils.setProperty(jopo, name, fields[i]);
				}
			} else {
				String name = selectFields.get(0);
				PropertyUtils.setProperty(jopo, name, result);
			}
			resList.add(jopo);
		}
		datas.clear();
		datas.addAll(resList);
	}
	
	/**
	 * 把数组类型的数据包装成Map(通常用在多表查询的结果包装).
	 * @param datas
	 * @param selectFields
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void toMap(Collection datas, List<String> selectFields) {
		Collection resList = new ArrayList(datas.size());
		Iterator<Object[]> it = datas.iterator();
		while(it.hasNext()) {
			Object[] res = it.next();
			Map<String, Object> resTable = new HashMap<String, Object>();
			for(int i = 0; i < selectFields.size(); i++) {
				String key = selectFields.get(i);
				if(key.indexOf(asOpr) != -1) {
					key = key.substring(key.indexOf(asOpr) + asOpr.length(), key.length());
				}
				resTable.put(key, res[i]);
			}
			resList.add(resTable);
		}
		datas.clear();
		datas.addAll(resList);
	}
}
