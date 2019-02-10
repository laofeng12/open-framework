package com.openjava.example.order.query;

import java.util.Date;
import java.util.List;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author 子右
 *
 */
public class ExampleOrderDBParam extends RoDBQueryParam {
	private Long eq_orderId;//订单号 --主键查询
	
	private String eq_operAccount;//下单用户 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_submitTime;//下单时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_submitTime;//下单时间 >= ?
	private Long eq_orderStatus;//订单状态 = ?
	
	private Long[] eq_totalPrice;
	
	private List<String> in_userName;
	private String in_userAddress;
	
	private String sql_query1;//定义的HQL
	
	public Long getEq_orderId() {
		return eq_orderId;
	}
	public void setEq_orderId(Long orderId) {
		this.eq_orderId = orderId;
	}
	
	public String getEq_operAccount() {
		return eq_operAccount;
	}
	public void setEq_operAccount(String operAccount) {
		this.eq_operAccount = operAccount;
	}
	public Date getLe_submitTime() {
		return le_submitTime;
	}
	public void setLe_submitTime(Date submitTime) {
		this.le_submitTime = submitTime;
	}
	public Date getGe_submitTime() {
		return ge_submitTime;
	}
	public void setGe_submitTime(Date submitTime) {
		this.ge_submitTime = submitTime;
	}
	public Long getEq_orderStatus() {
		return eq_orderStatus;
	}
	public void setEq_orderStatus(Long orderStatus) {
		this.eq_orderStatus = orderStatus;
	}
	public Long[] getEq_totalPrice() {
		return eq_totalPrice;
	}
	public void setEq_totalPrice(Long[] eq_totalPrice) {
		this.eq_totalPrice = eq_totalPrice;
	}
	public List<String> getIn_userName() {
		return in_userName;
	}
	public void setIn_userName(List<String> in_userName) {
		this.in_userName = in_userName;
	}
	public String getIn_userAddress() {
		return in_userAddress;
	}
	public void setIn_userAddress(String in_userAddress) {
		this.in_userAddress = in_userAddress;
	}
	public String getSql_query1() {
		return sql_query1;
	}
	public void setSql_query1(String sql_query1) {
		this.sql_query1 = sql_query1;
	}
}