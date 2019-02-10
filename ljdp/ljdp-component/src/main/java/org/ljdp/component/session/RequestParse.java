package org.ljdp.component.session;


public class RequestParse {

	public static Request parse(String params) {
		Request request = new Request("");
		if(params == null) {
			return request;
		}
		String[] items = params.split("&");
		for(String item : items) {
			String[] rule = item.split("=");
			if(rule.length == 2) {
				request.addParameter(rule[0], rule[1]);
			}
		}
		return request;
	}
}
