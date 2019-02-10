package org.ljdp.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class LocalDateTimeUtils {
	// 获取当前时间的LocalDateTime对象
	// LocalDateTime.now();

	// 根据年月日构建LocalDateTime
	// LocalDateTime.of();

	// 比较日期先后
	// LocalDateTime.now().isBefore(),
	// LocalDateTime.now().isAfter(),

	// Date转换为LocalDateTime
	public static LocalDateTime convertDateToLDT(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	// LocalDateTime转换为Date
	public static Date convertLDTToDate(LocalDateTime time) {
		return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}

	// 获取指定日期的毫秒
	public static Long getMilliByTime(LocalDateTime time) {
		return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	// 获取指定日期的秒
	public static Long getSecondsByTime(LocalDateTime time) {
		return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
	}

	// 获取指定时间的指定格式
	public static String formatTime(LocalDateTime time, String pattern) {
		return time.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	/**
	 * 把字符串日期解析为LocalDateTime对象
	 * @param text
	 * @param pattern
	 * @return
	 */
	public static LocalDateTime parseDateTime(String text, String pattern) {
		return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
	}
	
	public static LocalDate parseDate(String text, String pattern) {
		return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
	}
	
	public static Temporal parse(String date) {
		if(StringUtils.isEmpty(date)) {
			return null;
		}
		date = date.trim();
		if(date.length() <= DateFormater.PATTERN_ISODATE.length() &&
				date.length() >= (DateFormater.PATTERN_ISODATE.length()-2) &&
				countChar(date, '-') == 2) {
			return parseDate(date, DateFormater.PATTERN_ISODATE);
		}
		else if(date.length() <= DateFormater.PATTERN_ISODATE2.length() &&
				date.length() >= (DateFormater.PATTERN_ISODATE2.length()-2) &&
				countChar(date, '/') == 2) {
			return parseDate(date, DateFormater.PATTERN_ISODATE2);
		}
		else if(date.length() <= DateFormater.PATTERN_yMdHms.length() &&
				date.length() >= (DateFormater.PATTERN_yMdHms.length()-5) &&
				countChar(date, '-') == 2 &&
				countChar(date, ':') == 2) {
			return parseDateTime(date, DateFormater.PATTERN_yMdHms);
		} else if(date.length() <= DateFormater.PATTERN_yMdHms2.length() &&
				date.length() >= (DateFormater.PATTERN_yMdHms2.length()-5) &&
				countChar(date, '/') == 2 &&
				countChar(date, ':') == 2) {
			return parseDateTime(date, DateFormater.PATTERN_yMdHms2);
		}
		else if(date.length() <= DateFormater.PATTERN_yMdHm.length() &&
				date.length() >= (DateFormater.PATTERN_yMdHm.length()-4) &&
				countChar(date, '-') == 2 &&
				countChar(date, ':') == 1) {
			return parseDateTime(date, DateFormater.PATTERN_yMdHm);
		}
		else if(date.length() <= DateFormater.PATTERN_yMdHm2.length() &&
				date.length() >= (DateFormater.PATTERN_yMdHm2.length()-4) &&
				countChar(date, '/') == 2 &&
				countChar(date, ':') == 1) {
			return parseDateTime(date, DateFormater.PATTERN_yMdHm2);
		}
		else if(date.length() == DateFormater.PATTERN_string.length()) {
			return parseDateTime(date, DateFormater.PATTERN_string);
		}
		else if(date.length() <= DateFormater.PATTERN_MONTH.length() &&
				date.length() >= (DateFormater.PATTERN_MONTH.length()-1) &&
				countChar(date, '-') == 1) {
			return parseDate(date+"-01", DateFormater.PATTERN_ISODATE);
		}
		else if(date.length() <= DateFormater.PATTERN_MONTH2.length() &&
				date.length() >= (DateFormater.PATTERN_MONTH2.length()-1) &&
				countChar(date, '/') == 1) {
			return parseDate(date+"/01", DateFormater.PATTERN_ISODATE2);
		}
		else if(date.length() == DateFormater.PATTERN_yM.length()) {
			return parseDate(date+"01", DateFormater.PATTERN_yMd);
		}
		return null;
	}

	// 获取当前时间的指定格式
	public static String formatNow(String pattern) {
		return formatTime(LocalDateTime.now(), pattern);
	}

	// 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
	public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
		return time.plus(number, field);
	}

	// 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
	public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
		return time.minus(number, field);
	}

	/**
	 * 获取两个日期的差 field参数为ChronoUnit.*
	 * 
	 * @param startTime
	 * @param endTime
	 * @param field
	 *            单位(年月日时分秒)
	 * @return
	 */
	public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
		Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
		if (field == ChronoUnit.YEARS)
			return period.getYears();
		if (field == ChronoUnit.MONTHS)
			return period.getYears() * 12 + period.getMonths();
		return field.between(startTime, endTime);
	}

	// 获取一天的开始时间，2017,7,22 00:00
	public static LocalDateTime getDayStart(LocalDateTime time) {
		return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	// 获取一天的结束时间，2017,7,22 23:59:59.999999999
	public static LocalDateTime getDayEnd(LocalDateTime time) {
		return time.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
	}
	
	private static int countChar(String data, char c) {
		int count = 0;
		if(data != null) {
			for(int i = 0; i < data.length(); ++i) {
				if(data.charAt(i) == c) {
					count++;
				}
			}
		}
		return count;
	}
}
