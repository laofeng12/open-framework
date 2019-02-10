package org.ljdp.component.sequence;

import java.math.BigDecimal;

import org.ljdp.util.NumberUtil;

public class UUIDHexSequence extends UUIDSequence {
	
	public UUIDHexSequence() {
		super();
	}
	
	/*protected String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace( 8-formatted.length(), 8, formatted );
		return buf.toString();
	}
	
	protected String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace( 4-formatted.length(), 4, formatted );
		return buf.toString();
	}*/
	
	protected String formatTo2Bit(String val) {
		StringBuffer buf = new StringBuffer("00");
		buf.replace( 2-val.length(), 2, val );
		return buf.toString();
	}
	
	public String getSequence() throws SequenceException {
		synchronized(this) {
			try {
				return new StringBuffer(36)
						.append( NumberUtil._10_to_62( getIP() ) )
//						.append("-")
						.append( NumberUtil._10_to_62( getJVM() ) )
//						.append("-")
						.append( NumberUtil._10_to_62( getCurrentTime() ) )
//						.append("-")
						.append(formatTo2Bit( NumberUtil._10_to_62( getCount() ) ))
//						.append("-")
						.append(getRandom())
						.toString();
			} catch (SequenceOutException e) {
				System.out.println("已达到本毫秒内的最大序列号，尝试等待1毫秒");
				try {
					Thread.sleep(1);
					return getSequence();
				} catch (InterruptedException e2) {
					throw new SequenceException("已达到本毫秒内的最大序列号");
				}
			}
		}
	}
	
	/*public String getSequence(String t) throws SequenceException {
		String seq = "";
		if(t != null) {
			seq = t;
		}
		synchronized(this) {
			int lower = getLoTime();
			if(preMillis == lower) {
				throw new SequenceException("已达到本毫秒内的最大序列号");
			}
			return new StringBuffer(36)
				.append(seq)
				.append( format( getIP() ) )
				.append( format( getJVM() ) )
				.append( format( getHiTime() ) )
				.append( format( lower ) )
				.append( format( getCount() ) )
				.toString();
		}
	}
	
	public long getSequence() throws SequenceException {
		return getSequence(true);
	}

	public long getSequence(boolean wait) throws SequenceException {
		synchronized(this) {
			int lower = Math.abs(getLoTime());
			short c = getCount();
			if(c >= 100) {
				counter = 0;
				if(wait) {
					System.out.println("已达到本毫秒内的最大序列号，尝试等待1毫秒");
					preMillis = lower;
					try {
						Thread.sleep(1);
						return getSequence(false);
					} catch (InterruptedException e) {
						throw new SequenceException("已达到本毫秒内的最大序列号");
					}
				} else {
					throw new SequenceException("已达到本毫秒内的最大序列号");
				}
			}
			if(preMillis == lower) {
				throw new SequenceException("已达到本毫秒内的最大序列号");
			}
			String seq = Math.abs((short)getIP())+""+getHiTime()+""+lower+""+c;
			BigDecimal dec = new BigDecimal(seq);
			long seqlong = Math.abs(dec.longValue());
			return seqlong;
		}
	}*/

//	public static void main(String[] args) {
//		UUIDHexSequence u = new UUIDHexSequence();
//		for(int i=0;i<3843; i++) {
//			System.out.println(u.getSequence());
//		}
//	}
}
