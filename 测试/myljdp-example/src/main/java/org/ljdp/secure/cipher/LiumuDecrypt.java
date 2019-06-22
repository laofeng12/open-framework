package org.ljdp.secure.cipher;

import org.apache.commons.codec.binary.Base64;

public class LiumuDecrypt {

	public static void main(String[] args) {
		
		String data1 = "maHU1hQFDA26Ts_g0-LT5mQM7TKVbUwFtOGeiI44_mw=";
		String decryptAes = decrypt(data1);
		System.out.println(decryptAes);

		String data2 = "rqbRe5kqqadjhHAM-R-NIm3uvabMsiTEmISYrEtTlZ53mVPKw_DWpIBKJoMv6Gd2";
		decryptAes = decrypt(data2);
		System.out.println(decryptAes);
		try {
			System.out.println(MD5.encodeAsString("abc@123"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static String decrypt(String data1) {
		byte[] debase64 = new Base64(true).decode(data1);
		try {
			String sKey = "bGl1bXUtanItYnkt";
			String ivStr = "42C0Wdl_6zsJvXNN";
			AES a = new AES(sKey, ivStr);
			String decryptAes = a.decrypt(debase64);
			return decryptAes;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
