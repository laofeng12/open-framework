package org.ljdp.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ljdp.component.paging.Page;

/**
 * 多表查询对象
 * 示例： 
 *		//表OperatorTs的查询参数
 *		DBQueryParam operParams = new DBQueryParam();
		operParams.addQueryCondition("wayid", RO.EQ, "东区");
		operParams.addQueryCondition("opername", RO.LIKE, "代理商");
		operParams.addSort("onLinetime", "desc");
		
		//表RoleTs的查询参数
		DBQueryParam roleParams = new DBQueryParam();
		roleParams.addQueryCondition("name", RO.LIKE, "管理员");
		roleParams.addSort("type", "asc");
		
		MultiTableQuery query = new MultiTableQuery();
		//设置查询的表
		query.addTable(OperatorTs.class, operParams);
		query.addTable(RoleTs.class, roleParams);
		//设置表关联
		query.addJoin(OperatorTs.class, "roldId", RoleTs.class, "id");
		
		//获取SQL
		String sql = query.createSQLClause().buildSQL();
		
 * @author hzy
 *
 */
public class MultiTableQuery extends Page{
	private int tc = 1;
	
	private Map<Class<?>, String> tableAlias = new HashMap<Class<?>, String>();
	private List<Class<?>> tableList = new ArrayList<Class<?>>();
	private List<DBQueryParam> paramList = new ArrayList<DBQueryParam>();
	private List<EntityJoinClause> joinClauses = new ArrayList<EntityJoinClause>();
	private DBQueryParam[] paramArray;
	
	public void addTable(Class<?> table, DBQueryParam param) {
		paramArray = null;
		tableList.add(table);
		if(param != null) {
			paramList.add(param);
		} else {
			paramList.add(new DBQueryParam());
		}
		tableAlias.put(table, "t"+tc);
		tc++;
	}
	
	public void addJoin(Class<?> entityA, String fieldA, Class<?> entityB,String fieldB) {
		EntityJoinClause j = new EntityJoinClause(entityA, fieldA, entityB, fieldB);
		joinClauses.add(j);
	}
	
	public DBQueryParam[] getParamArray() {
		if(paramArray == null) {
			paramArray = paramList.toArray(new DBQueryParam[] {});
		}
		return paramArray;
	}
	
	public SQLClause createSQLClause() throws Exception{
		SQLClause hql = new SQLClause();
		
		for (int i = 0; i < joinClauses.size(); i++) {
			EntityJoinClause jc = joinClauses.get(i);
			hql.addWhere(jc.expressWhere(tableAlias));
		}
        
		for (int i = 0; i < tableList.size(); i++) {
			Class<?> table = tableList.get(i);
			DBQueryParam params = paramList.get(i);
			String alias = tableAlias.get(table);
			
			SQLUtil.expressClause(hql, table, params, alias);
		}
		
		return hql;
	}
}
