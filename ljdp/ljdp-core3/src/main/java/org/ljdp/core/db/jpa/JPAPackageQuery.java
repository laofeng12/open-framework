package org.ljdp.core.db.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.ljdp.core.dao.DAO;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.db.SQLClause;

public class JPAPackageQuery<T> {
	
	@SuppressWarnings("unchecked")
	public DataPackage<T> excuteHQL(EntityManager em, SQLClause hql, DBQueryParam[] params, Integer queryType) {
        DataPackage<T> result = new DataPackage<T>();
		result.setPageNo(params[0].get_pageno());
		result.setPageSize(params[0].get_pagesize());
		int rowcount = 0;
		/* 取记录数 */
		if (queryType.equals(DAO.QUERY_ALL) || queryType.equals(DAO.QUERY_COUNT)) {
			rowcount = JPAQueryUtils.excuteCount(em, hql.buildCountSQL(), params);
			result.setTotalCount(rowcount);
		}
		
		/* 取指定页的数据 */
		if (queryType.equals(DAO.QUERY_ALL) || queryType.equals(DAO.QUERY_DATA)) {
			Collection<T> mydatas = JPAQueryUtils.excuteQuery(em, hql.buildSQL(), params, rowcount);
			result.setDatas(mydatas);
			if(rowcount == 0) {
				result.setTotalCount(mydatas.size());
			}
		}
		
        return result;
    }
	
}
