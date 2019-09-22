package org.ljdp;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.cache.MultiData;
import org.ljdp.cache.SomeData;
import org.ljdp.cache.TestResponse;
import org.ljdp.image.FontLabel;

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
	}

}
