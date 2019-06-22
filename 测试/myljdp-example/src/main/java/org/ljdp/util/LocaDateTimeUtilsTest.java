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
        System.out.println(LocalDateTimeUtils.formatNow("yyyy��MM��dd�� HH:mm:ss:SSS"));
    }

    public static void betweenTwoTime_test() {
        LocalDateTime start = LocalDateTime.of(2017, 02, 01, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        System.out.println("��:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.YEARS));
        System.out.println("��:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MONTHS));
        System.out.println("��:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.DAYS));
        System.out.println("����:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HALF_DAYS));
        System.out.println("Сʱ:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HOURS));
        System.out.println("����:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MINUTES));
        System.out.println("��:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.SECONDS));
        System.out.println("����:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MILLIS));
        //=============================================================================================
        /*
                                      ��:1
                                      ��:13
                                      ��:396
                                      ����:792
                                      Сʱ:9506
                                      ����:570362
                                      ��:34221720
                                      ����:34221720000
        */
    }

    public static void plus_test() {
        //���Ӷ�ʮ����
        System.out.println(LocalDateTimeUtils.formatTime(LocalDateTimeUtils.plus(LocalDateTime.now(),
                20,
                ChronoUnit.MINUTES), "yyyy��MM��dd�� HH:mm"));
        //��������
        System.out.println(LocalDateTimeUtils.formatTime(LocalDateTimeUtils.plus(LocalDateTime.now(),
                2,
                ChronoUnit.YEARS), "yyyy��MM��dd�� HH:mm"));
        //=============================================================================================
        /*
                                        2017��07��22�� 22:53
                                        2019��07��22�� 22:33
         */
    }

    
}
