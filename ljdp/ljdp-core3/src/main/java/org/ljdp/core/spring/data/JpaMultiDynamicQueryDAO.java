package org.ljdp.core.spring.data;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ljdp.core.db.RoDBQueryParam;
import org.ljdp.core.db.SQLUtil;
import org.ljdp.core.db.jpa.JPAQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class JpaMultiDynamicQueryDAO {
	private EntityManager em;

	public JpaMultiDynamicQueryDAO(EntityManager em) {
		this.em = em;
	}

	/**
	 * 多表关联动态查询
	 * @param jpql
	 * @param paramArray
	 * @param pageable
	 * @return
	 */
	public Page<?> query(String jpql, Pageable pageable , RoDBQueryParam... paramArray){
		try {
			StringBuilder wheresClause = buildWhereClause(paramArray);
			StringBuilder orderby = buildSortClause(pageable.getSort());
			
			//查询总数
			int total = excuteMultiTableCount(jpql, wheresClause, paramArray);
			//查询数据
			List<?> content = excuteMultiTableList(jpql, wheresClause,
					orderby, paramArray, pageable);
			
			Page<?> pageresult = new PageImpl(
					content, pageable, total);
			
			return pageresult;
		} catch (Exception e) {
			throw new DynamicParamSQLException(e);
		}
	}
	
	public List<?> query(String jpql, Sort sort, RoDBQueryParam... paramArray){
		try {
			StringBuilder wheresClause = buildWhereClause(paramArray);
			StringBuilder orderby = buildSortClause(sort);
			
			List<?> content = excuteMultiTableList(jpql, wheresClause,
					orderby, paramArray, null);
			return content;
		} catch (Exception e) {
			throw new DynamicParamSQLException(e);
		}
	}
	
	public List<?> query(String jpql, RoDBQueryParam... paramArray){
		try {
			StringBuilder wheresClause = buildWhereClause(paramArray);
			
			List<?> content = excuteMultiTableList(jpql, wheresClause,
					null, paramArray, null);
			return content;
		} catch (Exception e) {
			throw new DynamicParamSQLException(e);
		}
	}
	
	public int count(String jpql, RoDBQueryParam... paramArray){
		try {
			StringBuilder wheresClause = buildWhereClause(paramArray);
			int total = excuteMultiTableCount(jpql, wheresClause, paramArray);
			return total;
		} catch (Exception e) {
			throw new DynamicParamSQLException(e);
		}
	}

	public List<?> excuteMultiTableList(String jpql,
			StringBuilder wheresClause, StringBuilder orderby,
			RoDBQueryParam[] paramArray, Pageable pageable) {
		String selectSql = jpql;
		if(wheresClause.length() > 0) {
			selectSql += " and " + wheresClause.toString();
		}
		if(orderby != null && orderby.length() > 0) {
			selectSql += " ORDER BY "+ orderby.toString();
		}
		Query query = em.createQuery(selectSql);
		for(RoDBQueryParam param : paramArray) {
			if(param != null) {
				JPAQueryUtils.bindParam(query, param.getAllParam());        		
			}
		}
		if(pageable != null) {
			query.setFirstResult((int)pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
		}
		
		List<?> content = query.getResultList();
		return content;
	}

	protected int excuteMultiTableCount(String jpql,
			StringBuilder wheresClause, RoDBQueryParam[] paramArray) {
		String countSql = "select count(*) "+SQLUtil.getFromPart(jpql);
		if(wheresClause .length()> 0) {
			countSql += " and " + wheresClause.toString();
		}
		Query query = em.createQuery( countSql );
		for(RoDBQueryParam param : paramArray) {
			if(param != null) {
				JPAQueryUtils.bindParam( query, param.getAllParam() );
			}
		}
		Number num = (Number)query.getSingleResult();
		return num.intValue();
	}

	public StringBuilder buildSortClause(Sort sort) {
		StringBuilder orderby = new StringBuilder();
		if(sort != null) {
			Iterator<Sort.Order> it = sort.iterator();
			while (it.hasNext()) {
				Sort.Order order = (Sort.Order) it.next();
				if(orderby.length() > 0) {
					orderby.append(",");
				}
				orderby.append(order.getProperty())
				.append(" ").append(order.getDirection().toString());
			}
		}
		return orderby;
	}

	public StringBuilder buildWhereClause(RoDBQueryParam[] paramArray)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		StringBuilder wheresClause = new StringBuilder();
		for (int i = 0; i < paramArray.length; i++) {
			RoDBQueryParam p = paramArray[i];
			if(p != null) {
				StringBuilder w = SQLUtil.buildWhereSQL(p, p.getTableAlias());
				
				if(w.length() > 0) {
					if(wheresClause.length() > 0) {
						wheresClause.append(" and ");
					}
					wheresClause.append(w);
				}
			}
		}
		return wheresClause;
	}
	
//	public static void main(String[] args) {
//		JpaMultiDynamicQueryDAO dao = new JpaMultiDynamicQueryDAO(null);
//		try {
//			StringBuilder sb = dao.buildWhereClause(new RoDBQueryParam[] {
//					new RoDBQueryParam(), new RoDBQueryParam()
//			});
//			System.out.println(sb.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
