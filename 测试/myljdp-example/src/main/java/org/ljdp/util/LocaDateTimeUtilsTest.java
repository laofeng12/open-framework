package org.ljdp.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class LocaDateTimeUtilsTest {
	
	public static void main(String[] args) {
		betweenTwoTime_test();
		
		System.out.println("-----------------------------------------");
		format_test();
		System.out.println("-----------------------------------------");
		plus_test();
		System.out.println("-----------------------------------------");
		System.out.println(LocalDateTimeUtils.parseDateTime("2018-02-20 10:11:12", "yyyy-MM-dd HH:mm:ss"));
		System.out.println(LocalDateTimeUtils.parseDate("2018-02-20", "yyyy-MM-dd"));
		System.out.println(LocalDateTimeUtils.parse("2018-02-20 10:11:12"));
		System.out.println(LocalDateTimeUtils.parse("2018/02/20 10:11:12"));
		System.out.println(LocalDateTimeUtils.parse("2018-02-20 10:11"));
		System.out.println(LocalDateTimeUtils.parse("2018/02/20 10:11"));
		System.out.println(LocalDateTimeUtils.parse("2018-02-20"));
		System.out.println(LocalDateTimeUtils.parse("2018/02/20"));
		System.out.println(LocalDateTimeUtils.parse("2018-02"));
		System.out.println(LocalDateTimeUtils.parse("2018/02"));
		System.out.println(LocalDateTimeUtils.parse("201802"));
	}

    public static void format_test() {
        System.out.println(LocalDateTimeUtils.formatNow("yyyy年MM月dd日 HH:mm:ss:SSS"));
    }

    public static void betweenTwoTime_test() {
        LocalDateTime start = LocalDateTime.of(2017, 02, 01, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        System.out.println("年:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.YEARS));
        System.out.println("月:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MONTHS));
        System.out.println("日:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.DAYS));
        System.out.println("半日:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HALF_DAYS));
        System.out.println("小时:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HOURS));
        System.out.println("分钟:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MINUTES));
        System.out.println("秒:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.SECONDS));
        System.out.println("毫秒:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MILLIS));
        //=============================================================================================
        /*
                                      年:1
                                      月:13
                                      日:396
                                      半日:792
                                      小时:9506
                                      分钟:570362
                                      秒:34221720
                                      毫秒:34221720000
        */
    }

    public static void plus_test() {
        //增加二十分钟
        System.out.println(LocalDateTimeUtils.formatTime(LocalDateTimeUtils.plus(LocalDateTime.now(),
                20,
                ChronoUnit.MINUTES), "yyyy年MM月dd日 HH:mm"));
        //增加两年
        System.out.println(LocalDateTimeUtils.formatTime(LocalDateTimeUtils.plus(LocalDateTime.now(),
                2,
                ChronoUnit.YEARS), "yyyy年MM月dd日 HH:mm"));
        //=============================================================================================
        /*
                                        2017年07月22日 22:53
                                        2019年07月22日 22:33
         */
    }

    
}
