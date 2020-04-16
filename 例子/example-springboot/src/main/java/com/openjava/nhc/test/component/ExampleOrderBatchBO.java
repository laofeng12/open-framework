package com.openjava.nhc.test.component;

import java.util.Date;
import javax.annotation.Resource;

import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.util.DateFormater;
import org.ljdp.plugin.batch.bo.BaseFileImportBO;
import org.springframework.stereotype.Component;

import com.openjava.nhc.test.domain.ExampleOrder;
import com.openjava.nhc.test.service.ExampleOrderService;

/**
 * 订单管理文件导入的业务对象（前台处理）
 * 运行顺序如下：
 * initialization();
 * for(读取文件每行数据->record){
 *    doProcessRecord(record);
 * }
 * finalWork();
 * destory();
 * 
 * @author 自由
 */
@Component
public class ExampleOrderBatchBO extends BaseFileImportBO {

	@Resource
	private ExampleOrderService exampleOrderService;
	
	@Override
	public String getTitle() {
		return "下单账号|下单时间|订单总额|用户名称|用户地址|订单状态|";
	}
	private int currentcount = 0;
	@Override
	protected BatchResult doProcessRecord(String record) {
		currentcount++;
		System.out.println(record);
		BatchResult result = new GeneralBatchResult();
		try {
			if(currentcount % 2 == 0) {
				throw new Exception("测试报错");
			}
			String[] items = record.split("\\|");
			int i = 0;
			//读取文件字段
			String operAccount = items[i++];
			Date submitTime = DateFormater.praseDate(items[i++]);
			Float totalPrice = new Float(items[i++]);
			String userName = items[i++];
			String userAddress = items[i++];
			Integer orderStatus = new Integer(items[i++]);
			
			//新增记录
			ExampleOrder j = new ExampleOrder();
			SequenceService ss = TimeSequence.getInstance();
			j.setOrderId(ss.getSequence(""));
			j.setOperAccount(operAccount);
//			j.setSubmitTime(submitTime);
			j.setTotalPrice(totalPrice);
			j.setUserName(userName);
			j.setUserAddress(userAddress);
			j.setOrderStatus(orderStatus);
			
//			exampleOrderService.doSave(j);
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	@Override
	public BatchResult doProcessRecord(int sheetLocation, String[] line, int size) {
		BatchResult result = new GeneralBatchResult();
		//开始事务
		beginTransaction();
		for(int i=0; i < size; i++) {
			String record = line[i];
			if(checkValidate(record)) {
				//验证通过正常处理
			} else {
				//验证失败，写入错误文件
				result.addFailRecord(i, "失败原因");
			}
		}
		//提交事务
		commitTransaction();
		return result;
	}
	
	private boolean checkValidate(String record) {
		return true;
	}
	private void beginTransaction() {
		
	}
	private void commitTransaction() {
		
	}
}