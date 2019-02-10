package org.ljdp.core.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.SQLClause;
import org.ljdp.core.db.SQLUtil;
import org.ljdp.core.db.session.SessionManagerBean;
import org.ljdp.util.DataWrapper;

public abstract class BaseEntityDAO<T> extends SessionManagerBean{
	protected final static String[] alias = {" this0 ", " this1 "};
	protected Class<T> entityClass;
	
	public BaseEntityDAO() {
		super();
	}
	
	public BaseEntityDAO(Class<T> voClass) {
		this.entityClass = voClass;
	}
	
	public Class<T> getEntityClass() {
		return entityClass;
	}
	
	public void setEntityClass(Class<T> voClass) {
		this.entityClass = voClass;
	}

	protected void checkVO(Object pk) {
        if ( !pk.getClass().equals(entityClass) ) {
			throw new RuntimeException(entityClass.getName()+" 的DAO不能操作 "+pk.getClass().getName());
		}
    }

	protected SQLClause createSQLClause(DBQueryParam params) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		SQLClause hql = new SQLClause();
        SQLUtil.expressClause(hql, entityClass, params, alias[0]);
		return hql;
	}

	@SuppressWarnings("rawtypes")
	protected void wrapDataPackage(List dataList,
			DBQueryParam[] paramsArray) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
		if(paramsArray[0].getSelectFields() != null 
				&& paramsArray[0].getSelectFields().size() > 0 
	    		&& paramsArray[0].getReturnType() != DBQueryParam.TYPE_DEFAULT) {
	    	if(paramsArray.length > 1 
	    			|| paramsArray[0].getReturnType() == DBQueryParam.TYPE_MAP) {
	    		ArrayList<String> selectFields = new ArrayList<String>();
	    		for(DBQueryParam param : paramsArray) {
	    			selectFields.addAll(param.getSelectFields());
	    		}
	    		DataWrapper.toMap(dataList, selectFields);
	    	} else if(paramsArray[0].getReturnType() == DBQueryParam.TYPE_POJO){
	    		DataWrapper.toJOPO(dataList, paramsArray[0].getSelectFields(), entityClass);
	    	}
	    }
	}
	
	/**
	 * 判断是否需要进行两个表的联合查询 
	 * @param params
	 * @param joinClause
	 * @param joinParams
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	protected DBQueryParam[] parseDBQueryParam(DBQueryParam params,
			String joinClause, DBQueryParam joinParams, SQLClause hql)
			throws Exception {
		DBQueryParam[] paramsArray;
        if (joinClause != null && joinParams != null) {
        	hql.addSelectFields(joinParams.getSelectFields(), alias[1]);
            SQLUtil.addJoinClause(hql, joinClause, joinParams, alias);
            paramsArray = new DBQueryParam[] {params, joinParams};
        } else {
        	paramsArray = new DBQueryParam[] {params};
        }
		return paramsArray;
	}

}
