package org.ljdp.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import io.swagger.annotations.ApiModelProperty;

public class ValidationUtils {
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private static Map<Class<?>, Map<String, String>> clsModelPropertyMap = new ConcurrentHashMap<>();
	
    public static <T> ValidationResult validateEntity(T obj, Class<?>... groups) {
    	praseModelProperty(obj);
    	Map<String, String> fieldNames = clsModelPropertyMap.get(obj.getClass());
    	
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, groups);
        // if( CollectionUtils.isNotEmpty(set) ){
        if (set != null && set.size() != 0) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<String, String>();
            for (ConstraintViolation<T> cv : set) {
            	String name = cv.getPropertyPath().toString();
            	if(fieldNames.containsKey(name)) {
            		name = fieldNames.get(name);
            	}
                errorMsg.put(name, cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

	private static <T> void praseModelProperty(T obj) {
		if(!clsModelPropertyMap.containsKey(obj.getClass())) {
    		synchronized (obj.getClass()) {
    			if(!clsModelPropertyMap.containsKey(obj.getClass())) {		
    				Map<String, String> mypropertys = new HashMap<>();
    				Field[] fields = obj.getClass().getDeclaredFields();
    				for (int i = 0; i < fields.length; i++) {
    					Field f = fields[i];
    					ApiModelProperty amp = f.getAnnotation(ApiModelProperty.class);
    					if(amp != null) {
    						mypropertys.put(f.getName(), amp.value());
    					}
    				}
    				clsModelPropertyMap.put(obj.getClass(), mypropertys);
    			}
    		}
    	}
	}
 
	public static <T> ValidationResult validateProperty(T obj, String propertyName) {
		return validateProperty(obj, propertyName, Default.class);
	}
    public static <T> ValidationResult validateProperty(T obj, String propertyName, Class<?>... groups) {
    	praseModelProperty(obj);
    	Map<String, String> fieldNames = clsModelPropertyMap.get(obj.getClass());
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, groups);
        if (set != null && set.size() != 0) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<String, String>();
            for (ConstraintViolation<T> cv : set) {
            	String name = propertyName;
            	if(fieldNames.containsKey(name)) {
            		name = fieldNames.get(name);
            	}
                errorMsg.put(name, cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }
}
