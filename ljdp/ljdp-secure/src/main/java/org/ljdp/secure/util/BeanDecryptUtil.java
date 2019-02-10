package org.ljdp.secure.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

public class BeanDecryptUtil {

	public static void decrypt(Object bean, AESDecrypter aes) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Field[] fields = bean.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			if(f.getType().equals(String.class)) {
				String val = (String)PropertyUtils.getProperty(bean, f.getName());
				if(StringUtils.isNotBlank(val)) {
					String deval = aes.decrypt(val);
					PropertyUtils.setSimpleProperty(bean, f.getName(), deval);
				}
			} else if(!f.getType().getName().startsWith("java")) {
				//嵌套对象递归解密
				Object obj = PropertyUtils.getProperty(bean, f.getName());
				if(obj != null) {
					BeanDecryptUtil.decrypt(obj, aes);
				}
			}
			
		}
	}
}
