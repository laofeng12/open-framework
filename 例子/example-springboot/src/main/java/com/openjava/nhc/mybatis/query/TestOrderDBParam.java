package com.openjava.nhc.mybatis.query;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class TestOrderDBParam {
	private String eq_orderId;//订单id --主键查询
	
	private String eq_operAccount;//下单账号 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime le_submitTime;//下单时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime ge_submitTime;//下单时间 >= ?
	private List<Float> in_totalPrice;//订单总额 in ?
	private String like_userName;//用户名称 like ?
	private List<String> nin_userAddress;//用户地址 not in ?
	private Boolean null_orderStatus;//订单状态 is (not) null ?
	
	public String getEq_orderId() {
		return eq_orderId;
	}
	public void setEq_orderId(String orderId) {
		this.eq_orderId = orderId;
	}
	
	public String getEq_operAccount() {
		return eq_operAccount;
	}
	public void setEq_operAccount(String operAccount) {
		this.eq_operAccount = operAccount;
	}
	public LocalDateTime getLe_submitTime() {
		return le_submitTime;
	}
	public void setLe_submitTime(LocalDateTime submitTime) {
		this.le_submitTime = submitTime;
	}
	public LocalDateTime getGe_submitTime() {
		return ge_submitTime;
	}
	public void setGe_submitTime(LocalDateTime submitTime) {
		this.ge_submitTime = submitTime;
	}
	public List<Float> getIn_totalPrice() {
		return in_totalPrice;
	}
	public void setIn_totalPrice(List<Float> totalPrice) {
		this.in_totalPrice = totalPrice;
	}
	public String getLike_userName() {
		return like_userName;
	}
	public void setLike_userName(String userName) {
		this.like_userName = userName;
	}
	public List<String> getNin_userAddress() {
		return nin_userAddress;
	}
	public void setNin_userAddress(List<String> userAddress) {
		this.nin_userAddress = userAddress;
	}
	public Boolean getNull_orderStatus() {
		return null_orderStatus;
	}
	public void setNull_orderStatus(Boolean orderStatus) {
		this.null_orderStatus = orderStatus;
	}
}
