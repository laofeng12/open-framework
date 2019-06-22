package org.ljdp.api;

import java.util.HashMap;
import java.util.Map;

import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;

public class ClientTest {

	public static void main(String[] args) {
		try {
			ApiRequestEntity apientity = new ApiRequestEntity(null);
			apientity.setUrl("/api/ljdp3/fda/cert/JY201604221535380025");
			apientity.addParam("tokenid", "8BDC39605ACD696E425FAD3DF98BED02");
			Map m = ApiClient.doGet(apientity, HashMap.class);
			System.out.println(m);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
