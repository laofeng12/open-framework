package com.openjava.pay.notify;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.commons.pay.wxpay.constants.WxCfg_Chjy;

@WebServlet(name="WxPayNotify", urlPatterns={"/wxpaynotify"},loadOnStartup=1)
public class WxPayNotifyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 645029696583599716L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ExampleWeixinNotify notify = new ExampleWeixinNotify();
		notify.doPayNotify(req,resp, WxCfg_Chjy.G_PARTNER);
	}

	
	
}
