package org.ljdp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.cache.MultiData;
import org.ljdp.cache.SomeData;
import org.ljdp.cache.TestResponse;
import org.ljdp.common.json.JacksonTools;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.image.FontLabel;
import org.ljdp.secure.cipher.SHA256;

import com.fasterxml.jackson.databind.JavaType;

public class MapTest {

	public static void main(String[] args) {
		SomeData d = new SomeData();
		System.out.println(d instanceof Serializable);
		MultiData m = new MultiData();
		System.out.println(m instanceof Serializable);
		FontLabel f = new FontLabel("test", 0,0);
		System.out.println(f instanceof Serializable);
		TestResponse tr = new TestResponse();
		System.out.println(tr instanceof Serializable);
		
		System.out.println("=========================");
		System.out.println(StringUtils.isNumeric("12345"));
		System.out.println(StringUtils.isNumeric("12345.6"));
		System.out.println(StringUtils.isNumeric(""));
		System.out.println(StringUtils.isNumeric(null));
		
		try {
			String tokenId = UUID.randomUUID().toString()+ConcurrentSequence.getCentumInstance().getSequence()+"yunshangshuyuan";
			tokenId = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(SHA256.encode(tokenId));
			System.out.println(tokenId);
			
			HashSet<String> set = new HashSet();
			set.add("1");
			set.add("2");
			set.add("3");
			String json = JacksonTools.getObjectMapper().writeValueAsString(set);
			System.out.println(json);
			
			JavaType stringSetType = JacksonTools.getObjectMapper().getTypeFactory().constructParametricType(HashSet.class, String.class);
			HashSet<String> decodeSet = JacksonTools.getObjectMapper().readValue(json, stringSetType);
			System.out.println(decodeSet);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
