package com.openjava.example.order.domain;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author 子右
 *
 */
@ApiModel("订单信息")
@Entity
@Table(name = "EXAMPLE_ORDER")
public class ExampleOrder extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("订单号")
	@NotNull
	private Long orderId;
	
	@ApiModelProperty("下单用户")
	@Size(min=1, max=10)
	private String operAccount;
	
	@ApiModelProperty("下单时间")
	private Date submitTime;
	
	@ApiModelProperty("订单总额")
	@Max(9999)
	private Long totalPrice;
	
	@ApiModelProperty("收货人名称")
	@Length(min=1, max=10)
	private String userName;
	
	@ApiModelProperty("收货地址")
	private String userAddress;
	
	@ApiModelProperty("订单状态")
	private Long orderStatus;
	@ApiModelProperty("订单状态名称")
	private String orderStatusName;
	
	
	@Id
	@Column(name = "ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	

	@Column(name = "OPER_ACCOUNT")
	public String getOperAccount() {
		return operAccount;
	}
	public void setOperAccount(String operAccount) {
		this.operAccount = operAccount;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SUBMIT_TIME")
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	

	@Column(name = "TOTAL_PRICE")
	public Long getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}
	

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	

	@Column(name = "USER_ADDRESS")
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	

	@Column(name = "ORDER_STATUS")
	public Long getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Long orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	@Transient
	public String getOrderStatusName() {
		return orderStatusName;
	}
	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}
}