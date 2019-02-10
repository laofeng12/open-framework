package org.ljdp.ui.spring;

import java.util.Date;

import org.ljdp.util.DateFormater;
import org.springframework.core.convert.converter.Converter;

/**
 * 自动将字符类行转换为日期类型。
 * @author hzy
 *
 */
public class AutoDateConverter implements Converter<String, Date> {

	@Override
    public Date convert(String source) {
		return DateFormater.praseDate(source);
	}
}
