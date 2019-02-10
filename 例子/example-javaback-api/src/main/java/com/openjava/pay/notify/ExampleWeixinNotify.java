package com.openjava.pay.notify;

import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;

import com.commons.pay.vo.PayResultVO;
import com.commons.pay.wxpay.notify.BasicWeixinNotify;

public class ExampleWeixinNotify extends BasicWeixinNotify {

	@Override
	protected Result checkAndUpdateOrder(PayResultVO payResult) {
		System.out.println("========微信支付回调=====================");
		System.out.println(payResult);
		Result r = new GeneralResult(true);
		return r;
	}

}
