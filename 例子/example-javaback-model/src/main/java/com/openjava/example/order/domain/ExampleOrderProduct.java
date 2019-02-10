package com.openjava.example.order.domain;

import java.util.Date;

import javax.persistence.*;

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
@ApiModel("订单产品")
@Entity
@Table(name = "EXAMPLE_ORDER_PRODUCT")
public class ExampleOrderProduct extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("订单明细ID")
	private String orderItemId;
	
	@ApiModelProperty("订单ID")
	private Long orderId;
	
	@ApiModelProperty("商家名称")
	private String shopName;
	
	@ApiModelProperty("产品名称")
	private String productName;
	
	@ApiModelProperty("产品规格1值")
	private String productSpecVal1;
	
	@ApiModelProperty("产品规格2值")
	private String productSpecVal2;
	
	@ApiModelProperty("销售价格")
	private Long salePrice;
	
	
	@Id
	@Column(name = "ORDER_ITEM_ID")
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	

	@Column(name = "ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	

	@Column(name = "SHOP_NAME")
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	

	@Column(name = "PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	

	@Column(name = "PRODUCT_SPEC_VAL1")
	public String getProductSpecVal1() {
		return productSpecVal1;
	}
	public void setProductSpecVal1(String productSpecVal1) {
		this.productSpecVal1 = productSpecVal1;
	}
	

	@Column(name = "PRODUCT_SPEC_VAL2")
	public String getProductSpecVal2() {
		return productSpecVal2;
	}
	public void setProductSpecVal2(String productSpecVal2) {
		this.productSpecVal2 = productSpecVal2;
	}
	

	@Column(name = "SALE_PRICE")
	public Long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Long salePrice) {
		this.salePrice = salePrice;
	}
	
}