package org.ljdp.core.db.jpa;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.RoDBQueryParam;
import org.ljdp.core.query.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

public class JPAQueryUtils {
	private static Logger log = LoggerFactory.getLogger(JPAQueryUtils.class);
	
	public static void bindParam(Query query, Map<String, ?> map){
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			query.setParameter(name, map.get(name));
		}
	}

	public static void bindParam(Query query, Collection<Parameter> values){
		if(values != null) {			
			Iterator<Parameter> it = values.iterator();
			while(it.hasNext()) {
				Parameter param = it.next();
				query.setParameter(param.getName(), param.getValue());
			}
		}
	}
	
	public static int excuteCount(EntityManager em, String sql, DBQueryParam[] paramList) {
        Query query = em.createQuery( sql );
        for(DBQueryParam param : paramList) {
        	if(param != null) {
        		bindParam( query, param.getAllParam() );
        	}
        }
        Number num = (Number)query.getSingleResult();
        return num.intValue();
    }
	
	public static int excuteCount(EntityManager em, String sql, RoDBQueryParam... paramList) {
        Query query = em.createQuery( sql );
        for(RoDBQueryParam param : paramList) {
        	if(param != null) {
        		bindParam( query, param.getAllParam() );
        	}
        }
        Number num = (Number)query.getSingleResult();
        return num.intValue();
    }
	
	@SuppressWarnings("rawtypes")
	public static List excuteQuery(EntityManager em, String sql, DBQueryParam[] params, int totalCount) {
        Query query = em.createQuery(sql);
        for(DBQueryParam param : params) {
        	if(param != null) {
        		JPAQueryUtils.bindParam(query, param.getAllParam());        		
        	}
        }
        int _pageno = params[0].get_pageno();
        int _pagesize = params[0].get_pagesize();
        boolean pageing = false;//是否设置了分页
        if(_pageno > 0 && _pagesize < Integer.MAX_VALUE) {
            query.setMaxResults(_pagesize);
            query.setFirstResult(_pagesize * (_pageno - 1));
            pageing = true;
        } else {
        	int start = params[0].getStart();
        	int limit = params[0].getLimit();
        	if(start >= 0 && limit > 0) {
        		query.setFirstResult(start);
        		query.setMaxResults(limit);
        		pageing = true;
        	}
        }
        if(!pageing) {
        	if(totalCount > 50000) {
        		log.warn("大数据量载入没有设置分页！数据量="+totalCount+",SQL="+sql);
        	}
        }
        return query.getResultList();
    }
	
	@SuppressWarnings("rawtypes")
	public static List excuteQuery(EntityManager em, String sql, RoDBQueryParam params, Pageable pageable) {
		Query query = em.createQuery(sql);
		JPAQueryUtils.bindParam(query, params.getAllParam());
		if(pageable != null) {
			query.setFirstResult((int)pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
		}
		return query.getResultList();
	}
	
}
