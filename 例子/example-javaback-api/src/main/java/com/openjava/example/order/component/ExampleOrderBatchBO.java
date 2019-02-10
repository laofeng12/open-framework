package com.openjava.example.order.component;

import java.util.Date;
import javax.annotation.Resource;

import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.util.DateFormater;
import org.ljdp.plugin.batch.bo.BaseFileImportBO;
import org.springframework.stereotype.Component;

import com.openjava.example.order.domain.ExampleOrder;
import com.openjava.example.order.service.ExampleOrderService;

/**
 * 文件导入的业务对象（前台处理）
 * 运行顺序如下：
 * initialization();
 * for(读取文件每行数据->record){
 *    doProcessRecord(record);
 * }
 * finalWork();
 * destory();
 * 
 * @author 子右
 */
@Component
public class ExampleOrderBatchBO extends BaseFileImportBO {

	@Resource
	private ExampleOrderService exampleOrderService;
	
	@Override
	public String getTitle() {
		return "下单用户|下单时间|订单总额|收货人名称|收货地址|订单状态|";
	}

	@Override
	protected BatchResult doProcessRecord(int sheetLocation, String record) {
		if(sheetLocation == 0) {
			return doProcessRecordSheet1(record);
		} else if(sheetLocation == 1) {
			return doProcessRecordSheet2(record);
		} else {
			BatchResult result = new GeneralBatchResult();
			result.setSuccess(false);
			result.setMsg("不支持第"+sheetLocation+"个sheet");
			return result;
		}
	}
	
	protected BatchResult doProcessRecordSheet1(String record) {
		System.out.println("[Sheet1]"+record);
		DBAccessUser dbu = super.getUser();
		System.out.println("登录用户1："+dbu.getId()+","+dbu.getAccount()+","+dbu.getName());
		BaseUserInfo u = super.getUserInfo();
		if(u != null) {
			System.out.println("登录用户2："+u.getUserId()+","+u.getUserAccount()+","+u.getUserName());
		}
		BatchResult result = new GeneralBatchResult();
		try {
			String[] items = record.split("\\|");
			int i = 0;
			//读取文件字段
			String operAccount = items[i++];
			Date submitTime = DateFormater.praseDate(items[i++]);
			Double totalPriceD = new Double(items[i++]);
			String userName = items[i++];
			String userAddress = items[i++];
			Double orderStatusD = new Double(items[i++]);
			
			//新增记录
			ExampleOrder j = new ExampleOrder();
			SequenceService ss = ConcurrentSequence.getInstance();
			j.setOrderId(ss.getSequence());
			j.setOperAccount(operAccount);
			j.setSubmitTime(submitTime);
			j.setTotalPrice(totalPriceD.longValue());
			j.setUserName(userName);
			j.setUserAddress(userAddress);
			j.setOrderStatus(orderStatusD.longValue());
			
			exampleOrderService.doSave(j);
			
			result.setSuccess(true);
		} catch (Exception e) {
//			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}

	protected BatchResult doProcessRecordSheet2(String record) {
		System.out.println("[Sheet2]"+record);
		BatchResult result = new GeneralBatchResult();
		result.setSuccess(true);
		return result;
	}
}
