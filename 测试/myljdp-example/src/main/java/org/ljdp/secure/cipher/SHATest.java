package org.ljdp.secure.cipher;

import java.security.NoSuchAlgorithmException;

public class SHATest {

	public static void main(String[] args) {
		try {
			String s="audit_info={\"terminal_info\":\"192.168.1.1\",\"query_timestamp\":\"670985423406\"}&beginTimestamp=20191231000000&column=[\"字段1\",\"字段2\",\"字段3\"]&data=[[\"数据1\",\"数据2\",\"数据3\"],[\"数据1\",\"数据2\",\"数据3\"]]&endTImestamp=20191231000000&pushSequence=12345689&key=192006250b4c09247ec02edce69f6a2d";
			System.out.println(SHA256.encodeAsBase64(s));//ssodemo
			System.out.println(MD5.encodeAsString("dmpadmin"));
			System.out.println(SHA256.encodeAsString(s).toUpperCase());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

}
