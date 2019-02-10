package org.ljdp.core.db;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.core.query.Condition;
import org.ljdp.core.query.RelationOperate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLUtil {
	private static Logger log = LoggerFactory.getLogger(SQLUtil.class);
	
    public static String buildDeleteSQL(String tableName, String[] keyName) {
        StringBuilder delSql = new StringBuilder(80)
                                .append("delete from ")
                                .append(tableName)
                                .append(" this where ");
        for(int i = 0; i < keyName.length; i++) {
        	if(i > 0) {
        		delSql.append(" and ");
        	}
        	delSql.append(" this.").append(keyName[i]).append("=? ");
        }
        return delSql.toString();
    }
    
    public static String buildFindQuery(String tableName, String[] keyName) {
        StringBuilder hql = new StringBuilder(100);
        hql.append(" from ").append(tableName)
            .append(" this where ");
        for(int i = 0; i < keyName.length; i++) {
        	if(i > 0) {
        		hql.append(" and ");
        	}
        	hql.append(" this.").append(keyName[i]).append("=? ");
        }
        return hql.toString();
    }
    
//    public static SQLClause getCommonSQLClause(String tableName, String tableAlias) {
//        SQLClause sql = new SQLClause();
//        sql.addFromTable(tableName, tableAlias);
////        sql.setWhereParamVal(new ArrayList<Object>());
//        return sql;
//    }
    
    public static StringBuilder buildWhereSQL(DBQueryParam params) throws Exception{
    	return buildWhereSQL(params, null);
    }
    
    public static StringBuilder buildWhereSQL(DBQueryParam params, String prefix) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	RoDBQueryParam tp = (RoDBQueryParam)params;
    	return buildWhereSQL(tp, prefix);
    }
    
    /**
     * 根据查询参数params建立SQL中的where子句。
     * 例如：((field0 = ?) and (field1 > ?) and (field2 = ? or field2 = ?))
     * @param params
     * @param prefix
     * @param paramVal
     * @return
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public static StringBuilder buildWhereSQL(RoDBQueryParam params, String prefix) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (prefix == null) {
            prefix = "";
        }
        prefix = prefix.trim();
        if (prefix.length() > 0) {
            prefix = prefix + ".";
        }
        StringBuilder whereClause = new StringBuilder(200);
        Map<String, String> clauseMap = new HashMap<String, String>();
        Iterator<Condition> conditionIt = getConditions(params).iterator();
        /* 循环处理所有参数 */
        while( conditionIt.hasNext() ) {
            Condition condition = (Condition)conditionIt.next();
            if( null == condition ) {
                continue;
            }
            boolean ok = checkAndProcessParam(condition);
            if( !ok ) {
                continue;
            }
            /* 开始建立where子句 */
            condition.addPrefixToName(prefix);
            ArrayList<Object> paramVal = new ArrayList<Object>();
            String clause = generWhereClause(condition, paramVal).toString();
            if(StringUtils.isNotEmpty(clause)) {
            	for(int i = 0; i < paramVal.size(); ++i) {
            		//支持嵌套对象参数，主要用于复合主键
            		String paramName = params.getUniqueParamName(condition.getName().replaceAll("\\.", "_"));
            		clause = clause.replaceFirst("\\?", ":" + paramName);
            		params.addParameter(paramName, paramVal.get(i));
            	}
            	//把子句临时保存至map
            	clauseMap.put(condition.getRawOperation(), clause);
            }
        }
        
        if(params instanceof DBQueryParam) {
        	DBQueryParam dbp = (DBQueryParam)params;
        	//合并or子句
            Iterator<Set<String>> orClauseIt = dbp.getOrOperations().iterator();
            while (orClauseIt.hasNext()) {
    			Set<String> orOperSet = (Set<String>) orClauseIt.next();
    			StringBuilder orClause = new StringBuilder("(");
    			Iterator<String> it = orOperSet.iterator();
    			while (it.hasNext()) {
    				String oper = (String) it.next();
    				String clause = clauseMap.get(oper);
    				if(orClause.length() > 1) {
    					orClause.append(" or ");
    				}
    				orClause.append(clause);
    				clauseMap.remove(oper);
    			}
    			orClause.append(") and ");
    			whereClause.append(orClause.toString());
    		}
        }
        
        //合并子句为完整语句
        Iterator<String> clauseIt = clauseMap.values().iterator();
        while (clauseIt.hasNext()) {
			String clause = (String) clauseIt.next();
			whereClause.append(clause).append(" and ");
		}
        
        if (whereClause.length() > 4) {
            whereClause = whereClause.delete(whereClause.length() - 4,
                    whereClause.length() - 1);
        }
        
        return whereClause;
    }
    
    /**
     * 把关联的查询子句更新到sql中。
     * @param joinClause
     * @param joinParams
     * @param sql
     * @throws Exception
     */
    public static void addJoinClause(SQLClause sql, String joinClause, DBQueryParam joinParams, String[] tableAlias) throws Exception {
        if (!joinClause.trim().equals("")) {
            sql.getSelect().insert(0, " DISTINCT ");
            joinClause = joinClause.replaceFirst("\\?", tableAlias[0]);
            sql.getFrom().append(joinClause).append(tableAlias[1]);
            /* 建立联合查询时第二个表的where子句 */
            StringBuilder whereClause2 = SQLUtil.buildWhereSQL(joinParams, tableAlias[1]);
            if (sql.getWhere().length() > 0 && whereClause2.length() > 0) {
                sql.getWhere().append(" and ").append(whereClause2);
            } else {
                sql.getWhere().append(whereClause2);
            }
        }
    }
    
    /**
     * 根据参数生成where子句中的比较子句，可能需要先对参数进行检查和处理。
     * 例如：(field0 = ?)
     * @param condition
     * @param paramVal
     * @return
     */
    private static StringBuilder generWhereClause(Condition condition, ArrayList<Object> paramVal) {
        String key = condition.getOperType();
        if ( RelationOperate.containsKey( key ) ) {
            if( key.equalsIgnoreCase(RelationOperate.SQL) ) {
                StringBuilder clause = new StringBuilder(50);
                clause.append("(").append(condition.getValue())
                	.append(")");
                return clause;
            }
            if( key.equalsIgnoreCase(RelationOperate.IN)
            		|| key.equalsIgnoreCase(RelationOperate.NIN) ) {
            	if(condition.getValue() instanceof String) {
            		StringBuilder clause = new StringBuilder(100);
            		clause.append("(").append(condition.getName())
            		.append(" ").append(RelationOperate.getOperation(key))
            		.append(" (").append(condition.getValue())
            		.append("))");
            		return clause;
            	}
            }
            if( key.equalsIgnoreCase(RelationOperate.NULL) ) {
            	Object val = condition.getValue();
            	StringBuilder clause = new StringBuilder(50);
            	if(val != null) {
            		String flag = val.toString();
            		if(flag.equalsIgnoreCase("true")) {
            			clause.append("(").append(condition.getName())
            			.append(" is null )");
            		} else if(flag.equalsIgnoreCase("false")) {
            			clause.append("(").append(condition.getName())
            			.append(" is not null )");
            		}
            	}
            	return clause;
            }
            if ( key.equalsIgnoreCase(RelationOperate.LIKE) 
                    || key.equalsIgnoreCase(RelationOperate.NLIKE) ) {
            	//模糊匹配方式改由开发自定义，不再默认全匹配
//                condition.processValueAsLike();
            }
            if(condition.containsAssistOper(RelationOperate.DT.DATE)) {
                if( !condition.processValueAsDate() ) {
                    return new StringBuilder();
                }
            }
            String ordinClause = generOrdinWhereSubClause(condition);
            StringBuilder clause = buildSubClause(condition, paramVal, ordinClause);
            return clause;
        }
        log.warn("无法识别比较参数：" + key + ", 请参考RelationOperate");
        return new StringBuilder();
    }

    /**
     * 将处理完成的参数生成where子句中的比较子句。
     * @param condition
     * @param paramVal
     * @param operation
     * @return
     */
    private static StringBuilder buildSubClause(Condition condition, ArrayList<Object> paramVal, String ordinClause) {
        StringBuilder clause;
        Object value = condition.getValue();
        ordinClause = ordinClause.replaceFirst("#OPERATION#", RelationOperate.getOperation(condition.getOperType()));
        if ( value instanceof Object[] ) {
            if(condition.getOperType().equalsIgnoreCase(RelationOperate.BETWEEN)) {
                clause = new StringBuilder(25);
                clause.append(ordinClause);
                Object[] values = (Object[])value;
                paramVal.add(values[0]);
                paramVal.add(values[1]);
            } else {
                clause = buildORclause(value, ordinClause, paramVal);
            }
        } else {
            clause = new StringBuilder(50);
            clause.append(ordinClause);
            paramVal.add(value);
        }
        return clause;
    }

    /**
     * 建立or关系的比较子句。
     * @param value
     * @param operation
     * @param paramVal
     * @return
     */
    private static StringBuilder buildORclause(Object value, String operation, ArrayList<Object> paramVal) {
        StringBuilder clause;
        Object[] values = (Object[])value;
        if(values.length < 1) {
            return null;
        } else if(values.length == 1) {
            clause = new StringBuilder(25);
            clause.append(operation);
            paramVal.add(values[0]);
        } else {
            clause = new StringBuilder(100);
            clause.append("(");
            for(int i = 0; i < values.length; i++) {
                clause.append(operation).append(" or ");
                paramVal.add(values[i]);
            }
            clause.delete(clause.length()-3, clause.length());
            clause.append(") ");
        }
        return clause;
    }

    /**
     * 生成一个通用的关系运算子句。
     * @param condition
     * @return
     */
    private static String generOrdinWhereSubClause(Condition condition) {
        String operation = null;
        if( condition.getOperType().equalsIgnoreCase(RelationOperate.BETWEEN) ) {
            operation = "(" + condition.getName() + " #OPERATION# ? and ?)";
        } else if ( condition.containsAssistOper(RelationOperate.IC.IGNORE_CASE) ) {
            operation = "( lower(" + condition.getName() + ") #OPERATION# lower(?))";
        } else {
            operation = "(" + condition.getName() + " #OPERATION# (?))";
        }
        return operation;
    }
    
    /**
     * 对参数值进行检验，去掉空的参数值,不处理空的参数。
     * @param condition
     * @return
     */
    private static boolean checkAndProcessParam(Condition condition) {
        if (condition.getValue() == null) {
            return false;
        }
        if (condition.getValue() instanceof String) {
            condition.processValueAsString();
            if (((String) condition.getValue()).length() <= 0) {
                return false;
            }
        } else if(condition.getValue() instanceof String[]) {
            condition.processValueAsStringArray();
            if(((String[])condition.getValue()).length == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取所有参数列表。
     * 参数包括名称，参数值，和操作符（也叫运算类型，如：>、＝..）
     * @param params
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
	private static List<Condition> getConditions(RoDBQueryParam params) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	List<Condition> totalList = new ArrayList<Condition>();
    	List<Condition> paramList = params.getQueryConditions();
    	if(paramList != null) {
    		totalList.addAll(paramList);
    	}
    	Map<?, ?> props = PropertyUtils.describe(params);
    	Iterator<?> it = props.keySet().iterator();
    	while(it.hasNext()) {
    		/* 提取参数的名称(field)，参数的值(value)，要进行的关系运算类型(key) */
            String key, field;
            Object value;
            key = (String)it.next();
            if(key.indexOf("_") >= 0) {//因SpringMVC不支持下划线开头的参数，所以不用startWith
//            	value = PropertyUtils.getSimpleProperty(params, key);
            	value = props.get(key);
            	if(value != null) {
//            		System.out.println(key + " " + value);
            		field = key.substring(key.lastIndexOf("_") + 1);
            		field = field.replaceAll("\\$", ".");//解析复合主键，嵌套对象
            		String oper = key.replaceFirst(field, "");
            		if(oper.length() > 1) {
            			Condition param = new Condition(field, oper, value);
            			totalList.add(param);
            		}
            	}
            }
    	}
    	return totalList;
    }
    

	public static String[] toPageSQL(String sql, DBQueryParam param) {
		String countSQL = "select count(*) " + sql;
		String select = "";
		List<String> sfs = param.getSelectFields();
		if(sfs != null && sfs.size() > 0 ) {
			for (int i = 0; i < sfs.size(); i++) {
				String str = sfs.get(i);
				if(select.length() > 0) {
					select += ",";
				}
				select += str;
			}
			select = "select " + select +" ";
		} else {
			select = "select * ";
		}
		String querySQL = select + sql;
		return new String[] {countSQL, querySQL};
	}
	
	public static void expressClause(SQLClause hql, Class<?> voClass,
			DBQueryParam params, String alias) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		/* 添加select子句的查询字段 */
        hql.addSelectFields(params.getSelectFields(), alias);
        
        /* 添加要查询的表:from子句 */
        hql.addFromTable(voClass.getName(), alias);
        
        /* 建立where子句 */
        hql.addWhere( buildWhereSQL(params, alias) );
        
        /* 添加排序 */
        String[] _orderby = params.get_orderby();
        String[] _sort = params.get_sort();
        for(int i = 0; i < _orderby.length; ++i) {
        	if(i < _sort.length && StringUtils.isNotBlank(_sort[i])) {
        		hql.addOrderBy(_orderby[i], _sort[i], alias);
        	} else {
        		hql.addOrderBy(_orderby[i], params.is_desc(), alias);
        	}
        }
	}
	
	/**
	 * 单表查询，直接返回表对象，不支持select字段
	 */
	public static void expressClause(SQLClause hql, Class<?> voClass,
			RoDBQueryParam params, String alias) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		/* 添加select子句的查询字段 */
//        hql.addSelectFields(params.getSelectFields(), alias);
        
        /* 添加要查询的表:from子句 */
        hql.addFromTable(voClass.getName(), alias);
        
        /* 建立where子句 */
        hql.addWhere( buildWhereSQL(params, alias) );
        
	}
	
	/**
	 * 返回去除了select(from之前)的部分
	 * @param sql
	 * @return
	 */
	public static String getFromPart(String sql) {
    	int fi = sql.indexOf("from");
    	if(fi < 0) {
    		fi = sql.indexOf("FROM");
    	}
    	return sql.substring(fi, sql.length());
    }
	
	/**
	 * 把SQL查询条件部分改为原生数据库SQL
	 * @param entityCls
	 * @param sb
	 * @param prefix
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static String toNativeWhereSQL(Class<?> entityCls, StringBuilder sb, String prefix) throws NoSuchMethodException, SecurityException {
		String sql = sb.toString();
		Map<String, String> map = FieldUtils.getDeclaredColumns(entityCls);
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String f = (String) it.next();
			String c = map.get(f);
			String currentName = "\\("+prefix+"."+f;
			String nativeName = "\\("+prefix+"."+c;
			sql = sql.replaceAll(currentName, nativeName);
		}
		return sql;
	}
	
	public static String buildNativeWhereSQL(Class<?> entityCls, RoDBQueryParam params, String prefix) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		StringBuilder sb = buildWhereSQL(params, prefix);
		return toNativeWhereSQL(entityCls, sb, prefix);
	}
}
