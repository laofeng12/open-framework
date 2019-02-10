package org.ljdp.cache.spring;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.ljdp.common.json.JacksonTools;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 自定义Spring Cache模块的key生成器，由于默认的SimpleKeyGenerator太容易冲突了
 * @author hzy0769
 *
 */
public class CacheKeyGenerator implements KeyGenerator {
    // custom cache key
    public static final String NULL_PARAM_KEY = "NULL";
    
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        String mname = method.getName();
        if(mname.indexOf("_") > 0) {
        	mname = mname.split("_")[0];
        }
        key.append(target.getClass().getName()).append("#").append(mname).append(":");
        
        for (Object param : params) {
            if (param == null) {
//                System.out.println("input null param for Spring cache, use default key="+NULL_PARAM_KEY);
                key.append(NULL_PARAM_KEY);
            } else if (ClassUtils.isPrimitiveArray(param.getClass())) {
                int length = Array.getLength(param);
                for (int i = 0; i < length; i++) {
                    key.append(Array.get(param, i));
                    key.append(',');
                }
            } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
                key.append(param);
            } else {
                try {
					key.append(JacksonTools.getObjectMapper().writeValueAsString(param));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					System.out.println("Using an object as a cache key may lead to unexpected results. " +
							"Either use @Cacheable(key=..) or implement CacheKey. Method is " + target.getClass() + "#" + method.getName());
					key.append(param.hashCode());
				}
            }
            key.append('|');
        }
 
        String finalKey = key.toString();
		try {
			String cacheKeyHash = encodeAsBase64(finalKey);
			return cacheKeyHash;
		} catch (Exception e) {
			e.printStackTrace();
			return finalKey;
		}
    }
    
    private String encodeAsBase64(String data) throws Exception {
    	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] md5Bytes = messageDigest.digest(data.getBytes("UTF-8"));
		return Base64.encodeBase64String(md5Bytes);
	}
}
