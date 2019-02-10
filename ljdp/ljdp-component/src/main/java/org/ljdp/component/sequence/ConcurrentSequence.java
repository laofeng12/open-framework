package org.ljdp.component.sequence;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.ljdp.util.NumberUtil;

/**
 * 支持集群和并发访问的序列生成器
 * @author hzy
 *
 */
public class ConcurrentSequence implements SequenceService{
	// 取IP的最后一位，假设在集群中IP的最后一位是不相同的，则取出的SEQ也不相同
	private static final int IP;
	static {
		int ipadd;
		try {
			String hostadd = InetAddress.getLocalHost().getHostAddress();
			ipadd = Integer.parseInt(hostadd.substring(hostadd.lastIndexOf(".")+1));
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}

	private long millis, old;
	
	/**
	 * 基本序列号的基数，这个基数必须是*的N次方，一毫秒内只能取到基数减一个序列号
	 */
	private final int base;
	
	private long timeReduce = 0;//为了减少生成的序列长度，减去一个过去的时间
	
	public ConcurrentSequence() {
		this.base = 10;
		initTimeParams();
	}
	
	public ConcurrentSequence(int base) {
		this.base = base;
		initTimeParams();
	}

	private static ConcurrentSequence cseq;
	/**
	 * 支持一毫秒内同时生成10个序列
	 * @return
	 */
	public static ConcurrentSequence getInstance() {
		if (cseq == null) {
			cseq = new ConcurrentSequence(10);
		}
		return cseq;
	}
	private static ConcurrentSequence cseq2;
	/**
	 * 支持一毫秒内同时生成100个序列
	 * @return
	 */
	public static ConcurrentSequence getCentumInstance() {
		if (cseq2 == null) {
			cseq2 = new ConcurrentSequence(100);
		}
		return cseq2;
	}
	
	protected void initTimeParams() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			timeReduce = sdf.parse("20170101").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取基本序列号，序列号与时间相关，是当前的毫秒数后加上一个不重复的数构成
	 * 
	 * @return 序列号
	 */
	public long getSequence() throws SequenceException {
		long timeSeq = getTimeSequence(true, 0);
		return timeSeq * 1000 + IP;
	}
	
	private long getTimeSequence(boolean wait, int waitCount) throws SequenceException {
		synchronized (this) {
			long result = System.currentTimeMillis() - timeReduce;
			if (result == millis) {
				old++;
				if (old >= (millis + 1) * base) {
					if(wait && waitCount < 10) {
						waitCount += 1;
						System.out.println("已达到本毫秒内的最大序列号，尝试等待1毫秒..."+waitCount);
						if(base == 10) {
							System.out.println("建议使用getCentumInstance");
						}
						System.out.println();
						try {
							Thread.sleep(1);
							return getTimeSequence(true, waitCount);
						} catch (InterruptedException e) {
							throw new SequenceException("已达到本毫秒内的最大序列号");
						}
					}
					throw new SequenceException("已达到本毫秒内的最大序列号");
				}
				result = old;
			} else {
				millis = result;
				result *= base;
				old = result;
			}
			return result;
		}
	}

	public String getSequence(String t) throws SequenceException {
		if(t == null) {
			t = "";
		}
		long s = getSequence();
//		return t+Long.toHexString(s);
		return t+NumberUtil._10_to_62(s);
	}
	
//	public static void main(String[] args) {
//		SequenceService s = ConcurrentSequence.getInstance();
//		SequenceService cs = ConcurrentSequence.getCentumInstance();
//		for(int i=0;i < 1000;i++) {
//			System.out.println(s.getSequence());
//			System.out.println(cs.getSequence());
//		}
//	}
}
