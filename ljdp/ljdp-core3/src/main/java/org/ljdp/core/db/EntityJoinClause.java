package org.ljdp.core.db;

import java.util.Map;

public class EntityJoinClause {
	private Class<?> entityA;
	private String fieldA;
	
	private Class<?> entityB;
	private String fieldB;
	
	public EntityJoinClause(Class<?> entityA, String fieldA, Class<?> entityB,
			String fieldB) {
		this.entityA = entityA;
		this.fieldA = fieldA;
		this.entityB = entityB;
		this.fieldB = fieldB;
	}
	
	public Class<?> getEntityA() {
		return entityA;
	}
	public void setEntityA(Class<?> entityA) {
		this.entityA = entityA;
	}
	public String getFieldA() {
		return fieldA;
	}
	public void setFieldA(String fieldA) {
		this.fieldA = fieldA;
	}
	public Class<?> getEntityB() {
		return entityB;
	}
	public void setEntityB(Class<?> entityB) {
		this.entityB = entityB;
	}
	public String getFieldB() {
		return fieldB;
	}
	public void setFieldB(String fieldB) {
		this.fieldB = fieldB;
	}
	
	/**
	 * 转换为Where语句
	 * @return
	 */
	public StringBuilder expressWhere(Map<Class<?>, String> tableAlias) {
		String aliasA = "";
		String aliasB = "";
		if(tableAlias != null) {
			if(tableAlias.containsKey(entityA)) {
				aliasA = tableAlias.get(entityA);
			}
			if(tableAlias.containsKey(entityB)) {
				aliasB = tableAlias.get(entityB);
			}
			if(aliasA.length() > 0) {
				aliasA += ".";
			}
			if(aliasB.length() > 0) {
				aliasB += ".";
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(aliasA).append(fieldA).append("=").append(aliasB).append(fieldB);
		return sb;
	}
}
