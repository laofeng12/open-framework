package org.ljdp.core.query;

public class DateOperator {
    /**
     * 运算的对象为日期类型
     */
    public final String DATE = "_dt";
    
    /** 等于 */
    public final String EQ = DATE + RelationOperate.EQ;
    
    /** 不等于 */
    public final String NE = DATE + RelationOperate.NE;

    /** 小于 */
    public final String LT = DATE + RelationOperate.LT;
    
    /** 大于 */
    public final String GT = DATE + RelationOperate.GT;
    /** 小于等于 */
    public final String LE = DATE + RelationOperate.LE;
    
    /** 大于等于 */
    public final String GE = DATE + RelationOperate.GE;
}
