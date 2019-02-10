package com.openjava.example.order.vo;

import java.util.List;

import com.openjava.example.order.domain.ExampleOrder;
import com.openjava.example.order.domain.ExampleOrderProduct;

import io.swagger.annotations.ApiModelProperty;

public class ExampleOrderRequest extends ExampleOrder {

	@ApiModelProperty("保存/更新")
	private Boolean isNew;
	
	@ApiModelProperty("产品明细")
	private List<ExampleOrderProduct> products;

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public List<ExampleOrderProduct> getProducts() {
		return products;
	}

	public void setProducts(List<ExampleOrderProduct> products) {
		this.products = products;
	}
}
