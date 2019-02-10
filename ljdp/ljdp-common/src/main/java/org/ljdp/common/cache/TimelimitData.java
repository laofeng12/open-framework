package org.ljdp.common.cache;

import org.ljdp.component.workflow.Releaseable;
import org.ljdp.util.TimeUtils;

public class TimelimitData implements Comparable<TimelimitData>, Releaseable{
	private String key;
	private Object data;
	private long bornTime;//出生时间
	private long lastAccessedTime;//最近访问时间
	private long maxInactiveInterval;//最大不活动时间限制，超出这个时间没使用可能会被回收
	private long baseSurvivalSecond=0;//基本生存时间，在此时间内不会被回收，0表示不起作用

	public TimelimitData(Object data, long secondLimit) {
		super();
		this.data = data;
		this.bornTime = System.currentTimeMillis();
		this.lastAccessedTime = this.bornTime;
		this.maxInactiveInterval = secondLimit;
	}
	
	/**
	 * 是否已经过期
	 * @return
	 */
	public boolean isOverdue() {
		return TimeUtils.isOverdueSecond(this.lastAccessedTime, maxInactiveInterval);
	}
	
	/**
	 * 数据是否可以被丢弃
	 * @return
	 */
	public boolean isGrowUp() {
		return TimeUtils.isOverdueSecond(this.bornTime, baseSurvivalSecond);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void updateAccessedTime() {
		this.lastAccessedTime = System.currentTimeMillis();
	}

	public long getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public int compareTo(TimelimitData o) {
		return (int)(this.getLastAccessedTime() - o.getLastAccessedTime());
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getBornTime() {
		return bornTime;
	}

	public long getBaseSurvivalSecond() {
		return baseSurvivalSecond;
	}

	public void setBaseSurvivalSecond(long baseSurvivalSecond) {
		this.baseSurvivalSecond = baseSurvivalSecond;
	}

	public void release() {
		if(this.data instanceof Releaseable) {
			Releaseable r = (Releaseable)this.data;
			r.release();
		}
	}
	
//	public static void main(String[] args) {
//		try {
//			List<TimelimitData> list = new LinkedList<TimelimitData>();
//			list.add(new TimelimitData(null, 0));
//			Thread.sleep(100);
//			list.add(new TimelimitData(null, 0));
//			Thread.sleep(100);
//			list.add(new TimelimitData(null, 0));
//			Thread.sleep(100);
//			list.add(new TimelimitData(null, 0));
//			Thread.sleep(100);
//			list.add(new TimelimitData(null, 0));
//			Thread.sleep(100);
//			list.add(new TimelimitData(null, 0));
//			Thread.sleep(100);
//			list.add(new TimelimitData(null, 0));
//			
//			Collections.sort(list);
//			
//			for (int i = 0; i < list.size(); i++) {
//				TimelimitData t = list.get(i);
//				System.out.println(t.getLastAccessedTime());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
