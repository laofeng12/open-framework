package org.ljdp.common.json;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ljdp.cache.SomeData;

import com.fasterxml.jackson.databind.JavaType;

public class JackonJsonTest {

	public static void main(String[] args) {
		String json = "";
		try {
			List<SomeData> list = new ArrayList<>();
			list.add(new SomeData("ÄãºÃ1"));
			list.add(new SomeData("ÄãºÃ2"));
			json = JacksonTools.getObjectMapper().writeValueAsString(list);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			JavaType type = JacksonTools.constructParametrizedType(ArrayList.class, List.class, SomeData.class);
			List<SomeData> list = (List<SomeData>)JacksonTools.getObjectMapper().readValue(json, type);
			
			System.out.println(list.getClass());
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
