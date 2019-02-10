package org.ljdp.component.sequence;

import java.net.InetAddress;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public abstract class UUIDSequence /*implements SequenceService*/ {

	private static final int IP;
	static {
		int ipadd;
		try {
			ipadd = toInt( InetAddress.getLocalHost().getAddress() );
			ipadd = Math.abs(ipadd);
		}
		catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}
	
	protected long preMillis = 0;//上一个计数器的毫秒数
	protected long currentMillis = 0;
	protected int counter = 0;
	private final long JVM =  System.currentTimeMillis() >>> 8;
	
	protected SecureRandom sr;
	
	private static class Holder {
        static final SecureRandom numberGenerator = new SecureRandom();
    }
	public UUIDSequence() {
		sr = Holder.numberGenerator;
	}
	
	/**
	 * Unique across JVMs on this machine (unless they load this class
	 * in the same quater second - very unlikely)
	 */
	protected long getJVM() {
		return JVM;
	}
	
	/**
	 * Unique in a millisecond for this JVM instance (unless there
	 * are > Short.MAX_VALUE instances created in a millisecond)
	 */
	protected int getCount() {
		counter++;
		if (counter > 3843) {
			if(preMillis == currentMillis) {
				throw new SequenceOutException("已达到本毫秒内的最大序列号");
			}
			counter = 1;
		}
		if(counter == 1) {
			preMillis = currentMillis;
//			currentMillis = getLoTime();
		}
		return counter;
	}
	
	public abstract String getSequence() throws SequenceException;
	
	/**
	 * Unique in a local network
	 */
	protected static int getIP() {
		return IP;
	}
	
	/**
	 * Unique down to millisecond
	 */
//	protected short getHiTime() {
//		return (short) ( System.currentTimeMillis() >>> 32 );
//	}
//	protected int getLoTime() {
//		return (int) System.currentTimeMillis();
//	}
	
	protected long getCurrentTime() {
		currentMillis = System.currentTimeMillis();
		return currentMillis;
	}
	
	protected String getRandom() {
		byte[] randomBytes = new byte[4];
        sr.nextBytes(randomBytes);
        String s = Base64.encodeBase64URLSafeString(randomBytes);
        return s;
	}
	
	public static int toInt(byte[] bytes) {
		int result = 0;
		for ( int i = 0; i < 4; i++ ) {
			result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

}
