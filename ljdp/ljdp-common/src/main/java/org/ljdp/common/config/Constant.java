package org.ljdp.common.config;

public class Constant {
	/**
	 * 系统运行方式。
	 * TEST: 测试运行。
	 * FORMAL: 正式运行。
	 * NOSERVER: 独立应用程序
	 * DEV:开发环境
	 */
	public enum Run {TEST, FORMAL, NOSERVER, APPSERVER, NOLOGIN, DEV}
	
	/**
	 * 获取SessionFactory的方式。
	 * NEW: 新建
	 * JNDI: 在JNDI查找
	 * SPRING: 在spring的context中查找
	 * NC: 末配置
	 */
	public enum SessionFactoryGeter {NEW, JNDI, SPRING, NC}
	
	/**
     * 实现事务管理的方式
     * NO: 无事务管理
     * JDBC: 实现的一个单数据源事务管理
     * SPRING: 由Spring管理事务
     * NC: 末配置
     */
    public enum TransactionManager {NO, JDBC, SPRING, NC};
}

