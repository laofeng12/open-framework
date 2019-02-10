package org.ljdp.component.sequence;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSequence implements SequenceService {
	private static TimeSequence me;
	
	public static TimeSequence getInstance() {
		if(me == null) {
			me = new TimeSequence();
		}
		return me;
	}
	
	private String lastBeginMillis = ""; //上一次tail=0时的时间序列
	private int tail = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
	private NumberFormat nf = NumberFormat.getInstance();
	
	public TimeSequence() {
		nf.setMinimumIntegerDigits(2);
		nf.setGroupingUsed(false);
	}

	public String getSequence(String t) throws SequenceException {
		if(t == null) {
			t = "";
		}
		String name = t + getSequenceString(true);
		return name;
	}

	public long getSequence() throws SequenceException {
		String result = getSequenceString(true);
		return Long.parseLong(result);
	}

	private String getSequenceString(boolean wait) {
		synchronized (this) {
			String dateStr = sdf.format(new Date());
			if(tail == 0) {
				if(lastBeginMillis.equals(dateStr)) {
					System.out.println("已达到秒内的最大序列号，尝试等待1秒...");
					try {
						Thread.sleep(100);
						return getSequenceString(false);
					} catch (InterruptedException e) {
						throw new SequenceException("已达到本秒内的最大序列号");
					}
				}
				lastBeginMillis = dateStr;
			}
			String tailStr = nf.format(tail);
			tail++;
			if(tail > 99) {
				tail = 0;
			}
			String result = dateStr+tailStr;
			return result;
		}
	}

}
