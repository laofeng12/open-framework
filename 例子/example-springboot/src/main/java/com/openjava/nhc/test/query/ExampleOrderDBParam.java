package com.openjava.nhc.test.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author 自由
 *
 */
public class ExampleOrderDBParam extends RoDBQueryParam {
	private String eq_orderId;//订单id --主键查询
	
	private String like_operAccount;//下单账号 like ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_submitTime;//下单时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_submitTime;//下单时间 >= ?
	private Float eq_totalPrice;//订单总额 = ?
	private String like_userName;//用户名称 like ?
	private Float eq_orderStatus;//订单状态 = ?
	
	public String getEq_orderId() {
		return eq_orderId;
	}
	public void setEq_orderId(String orderId) {
		this.eq_orderId = orderId;
	}
	
	public String getLike_operAccount() {
		return like_operAccount;
	}
	public void setLike_operAccount(String operAccount) {
		this.like_operAccount = operAccount;
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
	public Float getEq_totalPrice() {
		return eq_totalPrice;
	}
	public void setEq_totalPrice(Float totalPrice) {
		this.eq_totalPrice = totalPrice;
	}
	public String getLike_userName() {
		return like_userName;
	}
	public void setLike_userName(String userName) {
		this.like_userName = userName;
	}
	public Float getEq_orderStatus() {
		return eq_orderStatus;
	}
	public void setEq_orderStatus(Float orderStatus) {
		this.eq_orderStatus = orderStatus;
	}
}