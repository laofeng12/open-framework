package org.ljdp.cache.memcached.transcoder;

import java.io.UnsupportedEncodingException;

import net.rubyeye.xmemcached.transcoders.CachedData;
import net.rubyeye.xmemcached.transcoders.PrimitiveTypeTranscoder;

public class AliNativeTranscoder extends PrimitiveTypeTranscoder<Object> {

	private static final int TYPE_LONG = 16384;
	private static final int TYPE_STRING = 32;
	
	public CachedData encode(Object o) {
		int flags = 0;
		byte[] b = null;
		if(o instanceof Long) {
			flags = TYPE_LONG;
			b = encode(((Long)o).longValue());
		} else if(o instanceof String) {
			flags = TYPE_STRING;
			try {
				b = ((String) o).getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return new CachedData(flags, b, b.length, -1);
	}

	protected byte[] encode(long value) {
		return getBytes(value);
	}

	protected byte[] getBytes(long value) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (value >>> (56 - (i * 8)));
		}
		return b;
	}

	public Object decode(CachedData d) {
		if(d.getFlag() == TYPE_LONG) {
			return decodeLong(d.getData());
		} else if(d.getFlag() == TYPE_STRING){
			try {
				return new String(d.getData(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Unexpected flags for ali type:  " + d.getFlag());
		}
		return decodeLong(d.getData());
	}

	protected Long decodeLong(byte[] b) {
		return new Long(toLong(b));
	}

	protected long toLong(byte[] b) {
		long temp = 0;
		long res = 0;
		for (int i = 0; i < 8; i++) {
			res <<= 8;
			temp = b[i] & 0xff;
			res |= temp;
		}
		return res;
	}
}
