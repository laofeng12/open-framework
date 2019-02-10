package org.ljdp.core.db;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;

public class SQLClause {
	private Log log = LogFactory.getLog(this.getClass());
	
	private static Boolean SHOW_HQL = null;
	
    private StringBuilder select;

    private StringBuilder from;

    private StringBuilder where;
    
    private StringBuilder order;

    private List<Object> whereParamVal;

    public SQLClause() {
    	if(SHOW_HQL == null) {
    		ConfigFile cfg = Env.current().getConfigFile();
    		String showhql = cfg.getValue("show_hql", "false");
    		SHOW_HQL = Boolean.valueOf(showhql);
    	}
    }

    public StringBuilder getOrder() {
    	if(order == null) {
    		order = new StringBuilder();
    	}
        return order;
    }

    public void setOrder(StringBuilder orderBy) {
        this.order = orderBy;
    }

    public void setFrom(StringBuilder from) {
        this.from = from;
    }

    public void setSelect(StringBuilder select) {
        this.select = select;
    }

    public void setWhere(StringBuilder where) {
        this.where = where;
    }

    public List<Object> getWhereParamVal() {
        return whereParamVal;
    }

    public void setWhereParamVal(List<Object> whereParamVal) {
        this.whereParamVal = whereParamVal;
    }

    public StringBuilder getSelect() {
    	if(select == null) {
    		select = new StringBuilder();
    	}
        return select;
    }

    public StringBuilder getFrom() {
    	if(from == null) {
    		from = new StringBuilder();
    	}
        return from;
    }

    public StringBuilder getWhere() {
    	if(where == null) {
    		where = new StringBuilder();
    	}
        return where;
    }

    public String buildCountSQL() {
        StringBuilder sql = new StringBuilder("SELECT count(*)");
        sql.append(getFromClause());
        sql.append(getWhereClause());
        return sql.toString();
    }

    public String buildSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelectClause());
        sql.append(getFromClause());
        sql.append(getWhereClause());
        sql.append(getOrderClause());
        String ss = sql.toString();
        if(SHOW_HQL.booleanValue()) {
        	log.debug(ss);
        }
        return ss;
    }

    public void addFromTable(String table, String alias) {
    	String t = "";
    	if(alias != null) {
    		t = alias;
    	}
    	getFrom().append(table).append(" ").append(t).append(",");
    }
	
	public void addOrderBy(String orderBy, boolean isDesc, String tableAlias) {
		if(isDesc) {
			addOrderBy(orderBy, "desc", tableAlias);
		} else {
			addOrderBy(orderBy, "asc", tableAlias);
		}
	}
	
	public void addOrderBy(String orderBy, String tableAlias) {
		addOrderBy(orderBy, "asc", tableAlias);
	}

    public void addOrderBy(String orderBy, String sort, String tableAlias) {
    	if (StringUtils.isNotBlank(orderBy)) {
			String orderSort = "";
			if (sort.equalsIgnoreCase("desc")) {
				orderSort = " DESC ";
			} else if (sort.equalsIgnoreCase("asc")) {
				orderSort = " ASC ";
			} else if (sort.equalsIgnoreCase("desc nulls last")) {
				orderSort = " DESC NULLS LAST ";
			}
			String prefix = "";
			if(StringUtils.isNotBlank(tableAlias)) {
				prefix = tableAlias+".";
			}
			getOrder().append(prefix).append(orderBy.trim())
						.append(orderSort).append(",");
		}
    }

    /**
     * 把需要查询的字段更新到hql中。
     * @param hql
     * @param selectFields
     */
    public void addSelectFields(List<String> selectFields, String tableAlias) {
        if(selectFields != null && selectFields.size() > 0) {
            String prefix = "";
            if(StringUtils.isNotBlank(tableAlias)) {
            	prefix = tableAlias.trim() + ".";
            }
            int size = selectFields.size();
            for(int i = 0; i < size; i++) {
                String field = (String)selectFields.get(i);
                getSelect().append(prefix).append(field).append(",");
            }
        } else if(StringUtils.isNotBlank(tableAlias)) {
            getSelect().append(tableAlias).append(",");
        }
        getSelect().append(" ");
    }
    
    public void addWhere(StringBuilder myWhere) {
    	if(myWhere != null && myWhere.length() > 0) {
    		if (getWhere().length() > 1) {
    			addANDatLast(getWhere());
    		}
    		getWhere().append(myWhere);
    	}
	}
    
    public StringBuilder getSelectClause() {
    	StringBuilder sql = new StringBuilder();
    	if(select != null && select.length() > 0) {
    		delLastComma(select);
    		String s = select.toString().trim();
    		if(s.length() > 0) {
    			sql.append("SELECT ").append(s).append(" ");
    		}
    	}
    	return sql;
    }
    
    private void delLastComma(StringBuilder strbuf) {
    	if(strbuf != null && strbuf.indexOf(",") != -1) {
    		String tear = strbuf.substring(strbuf.lastIndexOf(",") + 1, strbuf.length());
    		if(StringUtils.isBlank(tear)) {
    			strbuf.deleteCharAt(strbuf.lastIndexOf(","));
    		}
    	}
    }
    
    private void addANDatLast(StringBuilder sb) {
    	if( sb != null ) {
    		int lastAND = sb.lastIndexOf("and");
    		int temp = sb.lastIndexOf("AND");
    		if(lastAND < temp) {
    			lastAND = temp;
    		}
    		if(lastAND != -1) {
    			String tear = sb.substring(lastAND + 3, sb.length());
    			if(StringUtils.isNotBlank(tear)) {
    				sb.append(" and ");
    			}
    		} else {
    			sb.append(" and ");
    		}
    	}
    }
    
    public StringBuilder getFromClause() {
    	StringBuilder sql = new StringBuilder();
    	if(from != null && from.length() > 0) {
    		delLastComma(from);
    		sql.append(" FROM ").append(from);
    	}
    	return sql;
    }

    public StringBuilder getOrderClause() {
    	StringBuilder sql = new StringBuilder();
    	if(order != null && order.length() > 0) {
    		delLastComma(order);
    		sql.append(" ORDER BY ").append(order);
    	}
    	return sql;
    }
    
    public StringBuilder getWhereClause() {
    	StringBuilder sql = new StringBuilder();
    	if ( where != null && where.length() > 0 ) {
    		sql = sql.append(" WHERE ").append(where);
    	}
    	return sql;
    }
    
}
