package com.openjava.example.order.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author 子右
 *
 */
public class ExampleOrderProductDBParam extends RoDBQueryParam {
	private String eq_orderItemId;//订单明细ID --主键查询
	
	private Long eq_orderId;//订单ID = ?
	private String like_shopName;//商家名称 like ?
	private String like_productName;//产品名称 like ?
	private Long le_salePrice;//销售价格 <= ?
	private Long ge_salePrice;//销售价格 >= ?
	
	public String getEq_orderItemId() {
		return eq_orderItemId;
	}
	public void setEq_orderItemId(String orderItemId) {
		this.eq_orderItemId = orderItemId;
	}
	
	public Long getEq_orderId() {
		return eq_orderId;
	}
	public void setEq_orderId(Long orderId) {
		this.eq_orderId = orderId;
	}
	public String getLike_shopName() {
		return like_shopName;
	}
	public void setLike_shopName(String shopName) {
		this.like_shopName = shopName;
	}
	public String getLike_productName() {
		return like_productName;
	}
	public void setLike_productName(String productName) {
		this.like_productName = productName;
	}
	public Long getLe_salePrice() {
		return le_salePrice;
	}
	public void setLe_salePrice(Long salePrice) {
		this.le_salePrice = salePrice;
	}
	public Long getGe_salePrice() {
		return ge_salePrice;
	}
	public void setGe_salePrice(Long salePrice) {
		this.ge_salePrice = salePrice;
	}
}