package org.ljdp.core.spring.data;

public class DynamicParamSQLException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2162223671737967810L;

	public DynamicParamSQLException(Exception e) {
		super("动态参数SQL查询异常", e);
	}
}
