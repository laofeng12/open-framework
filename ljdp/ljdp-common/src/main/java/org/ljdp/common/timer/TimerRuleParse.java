package org.ljdp.common.timer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.timer.model.TimerRule;

public class TimerRuleParse {
	
	/**
	 * 解析下一次运行时间
	 * @param rule 规则
	 * @param lastRunTime 最近一次已运行的时间
	 * @return
	 */
	public static Date parseNextTime(TimerRule rule, Date lastRunTime) throws Exception{
		if(rule == null) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		if(lastRunTime != null && !rule.isRepeat() && !rule.getMinute().equals("*")) {
			long dif = cal.getTimeInMillis() - lastRunTime.getTime();
			if(dif <= 60000) {
				cal.add(Calendar.MINUTE, 1);
			}
		}
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		if(rule.isRepeat()) {
			//带间隔的重复
			if(lastRunTime == null) {
				updateToNextRepeatTime(rule, cal);
			} else {
				//上一次运行时间+运行间隔
				cal.setTime(lastRunTime);
				updateToNextRepeatTime(rule, cal);
				if(cal.getTime().before(new Date())) {
					cal = Calendar.getInstance();
					updateToNextRepeatTime(rule, cal);
				}
			}
		} else {
			if(lastRunTime != null) {
				if(cal.getTime().before(lastRunTime)) {
					cal.setTime(lastRunTime);
				}
			}
			//先计算下次运行时间与当前时间的差值
			//计算月的差值
			//下次运行月与当前月差值，下下次运行月与当前月差值
			int[] nextMonth = {0, 0};
			if(rule.getMonth() != null) {
				//当前月份
				int currentMonth = cal.get(Calendar.MONTH) + 1;
				nextMonth = parseMonth(rule.getMonth(), currentMonth);
			} else if(lastRunTime != null){
				return null;
			}
			
			//计算日的差值
			//下次运行日-当前日，下下次运行日-当前日
			int[] nextDay = {0, 0};
			if(rule.getDay() != null) {
				int currentDay = cal.get(Calendar.DAY_OF_MONTH);
				int maxDayOfMonth = cal.getMaximum(Calendar.DAY_OF_MONTH);
				nextDay = parseDayOfMonth(rule.getDay(), currentDay, maxDayOfMonth);
			} else if(lastRunTime != null){
				return null;
			}
			
			//计算时的差值
			//下次运行时-当前时，下下次运行时-当前时
			int[] nextHour = {0, 0};
			if(rule.getHour() != null) {
				int currentHour = cal.get(Calendar.HOUR_OF_DAY);
				nextHour = parseHourOfDay(rule.getHour(), currentHour);
			} else if(lastRunTime != null){
				return null;
			}
			
			//计算分的差值
			int nextMinute = 0;
			if(rule.getMinute() != null) {
				int currentMinute = cal.get(Calendar.MINUTE);
				String minRule = rule.getMinute();
				nextMinute = parseMinute(minRule, currentMinute);
			} else if(lastRunTime != null){
				return null;
			}
			
			//设置下次运行时间
			//0: 使用下次运行时间与当前时间差值
			//1：使用下下次运行时间与当前时间差值
			int useHour = 0;//默认使用下次运行时差
			int userDay = 0;//默认使用下次运行日差
			int userMonth = 0;//默认使用下次运行月差
			if(nextMinute >= 0) {
				//下次运行分钟数与当前差值>=0
				if(nextHour[0] >= 0) {
					if(nextDay[0] < 0 && nextMonth[0] <= 0) {
						userMonth = 1;
					}
				} else {
					int[] res = updateIfBeforeUserHour(nextHour, nextDay,
							nextMonth, 0, 0, 0);
					userDay = res[0];
					userMonth = res[1];
				}
			} else {
				int[] res;
				if(nextHour[0] == 0) {
					useHour = 1;
					res = updateIfBeforeUserHour(nextHour, nextDay,
							nextMonth, 1, 0, 0);
				} else {
					res = updateIfBeforeUserHour(nextHour, nextDay,
							nextMonth, 0, 0, 0);
				}
				userDay = res[0];
				userMonth = res[1];
			}
			
			cal.add(Calendar.MINUTE, nextMinute);
			cal.add(Calendar.HOUR_OF_DAY, nextHour[useHour]);
			cal.add(Calendar.DAY_OF_MONTH, nextDay[userDay]);
			cal.add(Calendar.MONTH, nextMonth[userMonth]);
			
		}
		return cal.getTime();
	}

	//如果下次要运行的小时在当前小时之前，则更新下次运行日和月
	private static int[] updateIfBeforeUserHour(int[] nextHour, int[] nextDay,
			int[] nextMonth, int useHour, int userDay, int userMonth) {
		int[] res = {userDay, userMonth};
		if(nextHour[useHour] < useHour) {
			res = getUserDayAndMonth(nextDay, nextMonth, userDay,
					userMonth);
		}
		return res;
	}

	//如果运行日等于今天，则更新为下一次运行日。并取得下次运行月
	private static int[] getUserDayAndMonth(int[] nextDay, int[] nextMonth,
			int userDay, int userMonth) {
		if(nextDay[userDay] == 0) {
			userDay = 1;
			userMonth = updateToNextMonthIfNotInCurrent(nextDay, nextMonth, userDay,
					userMonth);
		} else {
			userMonth = updateToNextMonthIfNotInCurrent(nextDay, nextMonth, userDay,
					userMonth);
		}
		return new int[]{userDay, userMonth};
	}

	//如果下次运行日在当前日之前并且下次运行月等于当月，则更新为下下次运行月
	private static int updateToNextMonthIfNotInCurrent(int[] nextDay, int[] nextMonth,
			int userDay, int userMonth) {
		int temp = userMonth;
		if(nextDay[userDay] <= 0 && nextMonth[userMonth] <= 0) {
			temp = userMonth + 1;
		}
		return temp;
	}

	private static void updateToNextRepeatTime(TimerRule rule, Calendar cal) {
		int difM = parseInt(rule.getMonth());
		int difD = parseInt(rule.getDay());
		int difH = parseInt(rule.getHour());
		int difm = parseInt(rule.getMinute());
		cal.add(Calendar.MONTH, difM);
		cal.add(Calendar.DAY_OF_MONTH, difD);
		cal.add(Calendar.HOUR_OF_DAY, difH);
		cal.add(Calendar.MINUTE, difm);
	}

	private static int parseMinute(String minRule, int currentMinute)
			throws Exception {
		int nextMinute = 0;
		if(!minRule.equals("*")) {
			String[] minutes = minRule.split(",");
			ArrayList<Integer> mlist = new ArrayList<Integer>();
			for(String m : minutes) {
				int im = parseInt(m);
				if(im >= 0 && im < 60) {
					mlist.add(new Integer(im));
				}
			}
			if(mlist.size() == 0) {
				throw new Exception("解析分钟失败："+minRule);
			}
			Collections.sort(mlist);
			
			boolean inThisHour = false;//是否在这个小时运行
			for(int i = 0; i < mlist.size(); ++i) {
				int runminute = mlist.get(i).intValue();
				if(runminute == currentMinute) {
					inThisHour = true;
				}
				if(runminute > currentMinute) {
					inThisHour = true;
					nextMinute = runminute - currentMinute;
				}
			}
			if(!inThisHour) {
				nextMinute = mlist.get(0).intValue() - currentMinute;
			}
			
		} else {
			nextMinute = 1;
		}
		return nextMinute;
	}

	private static int[] parseHourOfDay(String hourRule, int currentHour)
			throws Exception {
		int[] nextHour = {0, 0};
		if(!hourRule.equals("*")) {
			String[] hours = hourRule.split(",");
			ArrayList<Integer> hlist = new ArrayList<Integer>();
			for(String h : hours) {
				int ih = parseInt(h);
				if(ih >= 0 && ih < 24) {
					hlist.add(new Integer(ih));
				}
			}
			if(hlist.size() == 0) {
				throw new Exception("解析小时失败："+hourRule);
			}
			Collections.sort(hlist);
			
			boolean inThisDay = false;//是否在今天运行
			for(int i = 0; i < hlist.size(); ++i) {
				int runhour = hlist.get(i).intValue();
				if(runhour == currentHour) {
					inThisDay = true;
					if((i+1) < hlist.size()) {
						nextHour[1] = hlist.get(i+1).intValue() - currentHour;
					} else {
						nextHour[1] = hlist.get(0).intValue() - currentHour;
					}
					break;
				}
				if(runhour > currentHour) {
					inThisDay = true;
					nextHour[0] = runhour - currentHour;
					break;
				}
			}
			if(!inThisDay) {
				//不在今天运行
				nextHour[0] = hlist.get(0).intValue() - currentHour;
			}
		} else {
			nextHour[1] = 1;
		}
		return nextHour;
	}

	private static int[] parseDayOfMonth(String dayRule, int currentDay,
			int maxDayOfMonth) throws Exception {
		int[] nextDay = {0, 0};
		if(!dayRule.equals("*")) {
			String[] days = dayRule.split(",");
			ArrayList<Integer> dlist = new ArrayList<Integer>();
			for(String d : days) {
				int intd = parseInt(d);
				if(intd > 0 && intd <= maxDayOfMonth) {
					dlist.add(new Integer(intd));
				}
			}
			if(dlist.size() == 0) {
				throw new Exception("解析日期失败："+dayRule);
			}
			Collections.sort(dlist);
			
			boolean inThisMonth = false;//是否在这个月运行
			for(int i = 0; i < dlist.size(); ++i) {
				int runday = dlist.get(i).intValue();
				if(runday == currentDay) {
					//今天运行
					inThisMonth = true;
					//下下次运行日差值=下次运行日-今天
					if((i+1) < dlist.size()) {
						nextDay[1] = dlist.get(i+1).intValue() - currentDay;
					} else {
						nextDay[1] = dlist.get(0).intValue() - currentDay;
					}
					break;
				}
				if(runday > currentDay) {
					//下次运行日差值
					inThisMonth = true;
					nextDay[0] = runday - currentDay;
					break;
				}
			}
			if(!inThisMonth) {
				//不在这个月运行
				//下次运行日差值
				nextDay[0] = dlist.get(0).intValue() - currentDay;
			}
		} else {
			nextDay[1] = 1;
		}
		return nextDay;
	}

	private static int[] parseMonth(String rule, int currentMonth) throws Exception {
		int[] nnMonth = {0,0};
		if(!rule.equals("*")) {
			//取运行月份
			String[] months = rule.split(",");
			ArrayList<Integer> mlist = new ArrayList<Integer>();
			for(String m : months) {
				int im = parseInt(m);
				if(im > 0 && im <= 12) {
					mlist.add(new Integer(im));
				}
			}
			if(mlist.size() == 0) {
				throw new Exception("解析月份失败："+rule);
			}
			Collections.sort(mlist);
			
			boolean inThisYear = false;//是否今年内运行
			for(int i = 0; i < mlist.size(); ++i) {
				int runMonth = mlist.get(i).intValue();//运行月份
				if( runMonth== currentMonth) {
					//这个月运行
					inThisYear = true;
					//下下次与下次差值
					if((i+1)<mlist.size()) {
						nnMonth[1] = mlist.get(i+1).intValue() - currentMonth;
					} else {
						nnMonth[1] =  12 - currentMonth + mlist.get(0).intValue();
					}
					break;
				} else if(runMonth > currentMonth) {
					//第一次出现并大于当前月的就是下一次运行月份
					inThisYear = true;
					nnMonth[0] = runMonth - currentMonth;
					break;
				}
			}
			if(!inThisYear) {
				//运行月份不在今年内，下年运行
				nnMonth[0] = 12 - currentMonth + mlist.get(0).intValue();
			}
		} else {
			nnMonth[1] = 1;
		}
		return nnMonth;
	}
	
	private static int parseInt(String num) {
		if(num != null) {
			if(StringUtils.isNumeric(num)) {
				int n = Integer.parseInt(num);
				if(n > 0) {
					return n;
				}
			}
		}
		return 0;
	}
	
	public static TimerRule parseString(String ruleString) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		TimerRule timerRule = new TimerRule();
		if(ruleString == null) {
			return timerRule;
		}
		String[] items = ruleString.split(" ");
		for(String item : items) {
			String[] rule = item.split("=");
			if(rule.length == 2) {
				if(rule[0].equals("repeat")) {
					BeanUtils.setProperty(timerRule, rule[0], new Boolean(rule[1]));
				} else {
					BeanUtils.setProperty(timerRule, rule[0], rule[1]);
				}
			}
		}
		return timerRule;
	}

}
