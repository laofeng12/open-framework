package org.ljdp.core.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.util.DateFormater;

public class Condition implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8346054669076877458L;

	private String name; //字段名

    private Object value; //字段的值

    private String operType; //主运算符
    
    private Set<String> assistOperType;//辅助运算符
    
    private String rawOperation;

    public Condition(String name, String operation, Object value) {
        this.name = name;
        this.value = value;
        assistOperType = new HashSet<String>();
        if(RO.containsOperate(operation)) {
        	operation = RO.getOperateKey(operation);
        }
        this.rawOperation = operation+name;
        String[] items = StringUtils.split(operation, "_");
        for(int i = 0; i < items.length - 1; i++) {
            assistOperType.add("_" + items[i]);
        }
        this.operType = "_" + items[items.length - 1] + "_";
    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public String getOperType() {
        return operType;
    }

//    public void setOperType(String operType) {
//        this.operType = operType;
//    }

    public Object getValue() {
        return value;
    }

//    public void setValue(Object value) {
//        this.value = value;
//    }
    
    public boolean containsAssistOper(String oper) {
//        String asoper = oper.replaceAll("_", "");
        return assistOperType.contains(oper);
    }
    
    public void processValueAsString() {
        value = ((String)value).trim();
    }
    
    public void processValueAsStringArray() {
        String[] values = (String[])value;
        ArrayList<String> valueList = new ArrayList<String>();
        for(String t : values) {
            if(t != null && t.trim().length() > 0) {
                valueList.add(t.trim());
            }
        }
        valueList.trimToSize();
        value = valueList.toArray(new String[] {});
    }
    
    /**
     * 当操作是like这样的运算符时需要对参数做些处理。
     * @param value
     * @return
     */
    public void processValueAsLike() {
        if(value instanceof String) {
            value = "%" + (value) + "%";
        } else if(value instanceof String[]) {
            String[] values = (String[])value;
            for(int i = 0; i < values.length; ++i) {
                values[i] = "%" + values[i] + "%";
            }
        }
    }
    
    public void addPrefixToName(String prefix) {
        name = prefix + name;
    }
    
    public boolean processValueAsDate() {
        try {
        	String valueDS = value.toString().trim();
        	java.util.Date javaDate = DateFormater.praseDate(valueDS);
        	if(javaDate == null) {
        		throw new Exception("无法识别此日期字符的格式："+valueDS);
        	}
        	if(valueDS.length() == DateFormater.PATTERN_ISODATE.length()) {
        		java.sql.Date sqld = new java.sql.Date(javaDate.getTime());
                value = sqld;
        	} else {
        		java.sql.Timestamp timestamp = new java.sql.Timestamp(javaDate.getTime());
                value = timestamp;
        	}
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isDateType() {
    	if(value instanceof java.util.Date ||
    			value instanceof java.sql.Date ||
    			value instanceof java.sql.Time ||
    			value instanceof java.sql.Timestamp) {
    		return true;
    	}
    	return false;
    }

	public String getRawOperation() {
		return rawOperation;
	}

	public void setRawOperation(String rawOperation) {
		this.rawOperation = rawOperation;
	}
}
