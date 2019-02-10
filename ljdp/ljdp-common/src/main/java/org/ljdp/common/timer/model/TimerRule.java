package org.ljdp.common.timer.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 定时任务规则。<br/>
 * *: 表示每（月/日/时/分）运行。<br/>
 * 数字（n）：表示第n月n日n时n分运行。多个数字间用','分隔<br/>
 * repeat=true：表示每隔n（月/日/时/分）运行一次;如果（月/日/时/分）都设置了值，则使用优先级最高的设置。<br/>
 * 字段优先级: repeat>月>日>时>分。<br/>
 * 
 * 例子：<br/>
 * 1、month=* day=* hour=* minute=30
 * 	每月每日每小时的30分运行。<br/>
 * 2、month=* day=* hour=8 minute=30
 * 	每月每日的8:30运行。<br/>
 * 3、month=* day=15 hour=* minute=0
 * 	每月15号每小时0分运行。<br/>
 * 4、month=* day=1,5,10,15 hour=6,18,22 minute=0
 * 	每月1号,5号,10号,15号的6点,18点,22点运行。<br/>
 * 5、minute=30 repeat=true
 * 	每隔30分钟运行一次。<br/>
 * 6、hour=1 repeat=true
 * 	每隔1小时运行一次。<br/>
 * 7、month=* day=* hour=* minute=* repeat=true/false
 * 	每分钟运行一次。<br/>
 * 8、day=1,hour=1,repeat=true
 * 	每隔一天一小时运行一次
 * 
 * @author Administrator
 *
 */
public class TimerRule {
	private String month = "*";
	private String day = "*";
	private String hour = "*";//24小时制
	private String minute = "*";
	private boolean repeat = false;
	
	public TimerRule() {
		
	}
	
	public TimerRule(String month, String day, String hour, String minute) {
		super();
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}
	public TimerRule(boolean repeat) {
		super();
		this.repeat = repeat;
	}

	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public boolean isRepeat() {
		return repeat;
	}
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
