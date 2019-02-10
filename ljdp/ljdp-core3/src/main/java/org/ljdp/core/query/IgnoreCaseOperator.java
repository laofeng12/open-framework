package org.ljdp.core.query;

public class IgnoreCaseOperator {
    /** 
     * 忽略大小写(Ignore Case)
     * */
    public final String IGNORE_CASE = "_ic";
    
    /** 等于(忽略大小写) */
    public final String EQ = IGNORE_CASE + RelationOperate.EQ;
    
    /** 不等于(忽略大小写) */
    public final String NE = IGNORE_CASE + RelationOperate.NE;

    /** like运算(忽略大小写) */
    public final String LIKE = IGNORE_CASE + RelationOperate.LIKE;

    /** not like运算(忽略大小写) */
    public final String NLIKE = IGNORE_CASE + RelationOperate.NLIKE;
}
