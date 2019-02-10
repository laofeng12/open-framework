package org.ljdp.core.query;

import java.util.HashMap;
import java.util.Map;

public class RelationOperate {
    //基本运算符
    
    /** 等于 */
    public static final String EQ = "_eq_";
    
    /** 不等于 */
    public static final String NE = "_ne_";

    /** 小于 */
    public static final String LT = "_lt_";
    
    /** 大于 */
    public static final String GT = "_gt_";
    /** 小于等于 */
    public static final String LE = "_le_";
    
    /** 大于等于 */
    public static final String GE = "_ge_";

    /** like运算 */
    public static final String LIKE = "_like_";

    /** not like运算 */
    public static final String NLIKE = "_nlike_";
    
    /**
     * 直接写sql语句。
     */
    public static final String SQL = "_sql_";
    
    /**
     * SQL的between语句。
     */
    public static final String BETWEEN = "_between_";
    
    public static final String IN = "_in_";
    
    public static final String NIN = "_nin_";
    
    public static final String NULL = "_null_";

    //组合运算操作符

    /**
     * 忽略大小写的运算
     */
    public static final IgnoreCaseOperator IC;
    
    /**
     * 日期类型的运算
     */
    public static final DateOperator DT;
    
    //系统运算符名与SQL关系运算符对应
    private static final Map<String, String> relationMap = new HashMap<String, String>();
    
    //常用SQL运算符与系统运行符对应
    private static final Map<String, String> commonOperate = new HashMap<String, String>();
    
    static {
        relationMap.put(EQ, "=");
        relationMap.put(NE, "<>");
        relationMap.put(LT, "<");
        relationMap.put(GT, ">");
        relationMap.put(LE, "<=");
        relationMap.put(GE, ">=");
        relationMap.put(LIKE, "like");
        relationMap.put(NLIKE, "not like");
        relationMap.put(BETWEEN, "between");
        relationMap.put(IN, "in");
        relationMap.put(NIN, "not in");
        relationMap.put(NULL, "is null");
        relationMap.put(SQL, "");
        
        commonOperate.put("=", EQ);
        commonOperate.put("<>", NE);
        commonOperate.put("<", LT);
        commonOperate.put(">", GT);
        commonOperate.put("<=", LE);
        commonOperate.put(">=", GE);
        commonOperate.put("like", LIKE);
        commonOperate.put("not like", NLIKE);
        commonOperate.put("between", BETWEEN);
        commonOperate.put("in", IN);
        commonOperate.put("not in", NIN);
        commonOperate.put("is null", NULL);
        
        IC = new IgnoreCaseOperator();
        DT = new DateOperator();
        
        commonOperate.put("_dlt_", DT.LT);
        commonOperate.put("_dle_", DT.LE);
        commonOperate.put("_deq_", DT.EQ);
        commonOperate.put("_dne_", DT.NE);
        commonOperate.put("_dgt_", DT.GT);
        commonOperate.put("_dge_", DT.GE);
        
        commonOperate.put("_ilike_", IC.LIKE);
        commonOperate.put("_inlike_", IC.NLIKE);
        commonOperate.put("_ieq_", IC.EQ);
        commonOperate.put("_ine_", IC.NE);
    }
    
    public static String getOperation(String key) {
//        String oper = "_" + key + "_";
        return relationMap.get(key);
    }
    
    public static boolean containsKey(String key) {
//        String oper = "_" + key + "_";
        return relationMap.containsKey(key);
    }
    
    public static boolean containsOperate(String operate) {
    	return commonOperate.containsKey(operate);
    }
    
    public static String getOperateKey(String operate) {
    	return commonOperate.get(operate);
    }
}
