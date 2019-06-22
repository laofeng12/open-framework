package org.ljdp.secure.cipher;

import org.apache.commons.codec.binary.Base64;

public class AESTest {

	public static void main(String[] args) {
		String sKey = "bGl1bXUtanItYnkt";
		String ivStr = "42C0Wdl_6zsJvXNN";
		String msg = "hello world�����";
		
		AES a = new AES(sKey, ivStr);
		try {
			System.out.println("��Ϣ��"+msg);
			String ens = a.encryptString(msg);
			System.out.println("���ܺ�"+ens);
			
			String decs = a.decryptString(ens);
			System.out.println("���ܺ�"+decs);
			
			System.out.println("======����Base64======");
			byte[] enbytes = a.encrypt(msg);
			String ens64 = Base64.encodeBase64String(enbytes);
			System.out.println("���ܺ�"+ens64);
			
			byte[] enbytes2 = Base64.decodeBase64(ens64);
			String decs2 = a.decrypt(enbytes2);
			System.out.println("���ܺ�"+decs2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
