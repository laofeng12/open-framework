package org.ljdp.component.bean;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.result.ApiResponse;

public class TubTools {
	public static final String SPLIT = "-";
	
	public static void addTrackByBeanId(List list, String pageId, String position, String idFieldName) {
		addTrackByBeanId(null, list, pageId, position, idFieldName);
	}

	/**
	 * 批量添加用户行为记录id
	 * @param list
	 * @param pageId 页面ID
	 * @param position 页面位置ID
	 * @param idFieldName 使用对象里面哪个字段的值，作为当前单元的id
	 */
	public static void addTrackByBeanId(String parentTub, List list, String pageId, String position, String idFieldName) {
		if(list == null || list.isEmpty()) {
			return;
		}
		try {
			for (Object b : list) {
				if(b == null) {
					continue;
				}
				if(b instanceof Map) {
					Map mb = (Map)b;
					Object val = mb.get(idFieldName);
					
					if(val != null) {
						String tub = buildTubValue(parentTub, pageId, position, val);
						mb.put("tub", tub);
					}
				} else if(b instanceof ApiResponse) {
					Object val = PropertyUtils.getProperty(b, idFieldName);
					if(val != null) {
						String tub = buildTubValue(parentTub, pageId, position, val);
						ApiResponse a = (ApiResponse)b;
						a.setTub(tub);
					}
				} else {
					Object val = PropertyUtils.getProperty(b, idFieldName);
					
					if(val != null) {
						String tub = buildTubValue(parentTub, pageId, position, val);
						PropertyUtils.setProperty(b, "tub", tub);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String buildTubValue(String parentTub, String pageId, String position, Object val) {
		String tub = pageId+"."+position+"."+val.toString();
		if(StringUtils.isNotBlank(parentTub)) {
			tub = parentTub + SPLIT+tub;
		}
		return tub;
	}
	
}
