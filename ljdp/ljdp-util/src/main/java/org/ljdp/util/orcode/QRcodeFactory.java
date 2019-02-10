package org.ljdp.util.orcode;

import java.util.HashMap;

/**
 * 二维码工具工厂，获取一些常用的二维码生成工具
 * @author hzy
 */
public class QRcodeFactory {
	
	private static HashMap<String, OREncoder> orMap = new HashMap<>();
	private static HashMap<String, EmbedOREncoder> emborMap = new HashMap<>();
	
	public static OREncoder getOREncoder(String character) {
		if(!orMap.containsKey(character)) {
			orMap.put(character, new OREncoder(character));
		}
		return orMap.get(character);
	}
	
	public static EmbedOREncoder getEmbedOREncoder(String character) {
		if(!emborMap.containsKey(character)) {
			emborMap.put(character, new EmbedOREncoder(character));
		}
		return emborMap.get(character);
	}

}