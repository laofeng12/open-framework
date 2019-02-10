package com.openjava.example.order.vo;

import java.util.Date;

/**
 * 订单多表查询结果视图对象
 * @author hzy
 *
 */
public class OrderDetailView {
	private Long orderId;//订单号
	private String operAccount;//下单用户
	private Date submitTime;//下单时间
	private Long totalPrice;//订单总额
	private String userName;//收货人名称
	private String userAddress;//收货地址
	
	private String orderItemId;//订单明细ID
	private String shopName;//商家名称
	private String productName;//产品名称
	private String productSpecVal1;//产品规格1值
	private String productSpecVal2;//产品规格2值
	private Long salePrice;//销售价格
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOperAccount() {
		return operAccount;
	}
	public void setOperAccount(String operAccount) {
		this.operAccount = operAccount;
	}
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public Long getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductSpecVal1() {
		return productSpecVal1;
	}
	public void setProductSpecVal1(String productSpecVal1) {
		this.productSpecVal1 = productSpecVal1;
	}
	public String getProductSpecVal2() {
		return productSpecVal2;
	}
	public void setProductSpecVal2(String productSpecVal2) {
		this.productSpecVal2 = productSpecVal2;
	}
	public Long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Long salePrice) {
		this.salePrice = salePrice;
	}
}
