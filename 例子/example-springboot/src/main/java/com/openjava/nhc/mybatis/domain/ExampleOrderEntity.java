package com.openjava.nhc.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel("订单主表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("EXAMPLE_ORDER")
public class ExampleOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("订单id")
	@TableId(type=IdType.INPUT)
	@TableField("order_id")
	private String orderId;
	
	@ApiModelProperty("下单账号")
	@Length(min=0, max=32)
	@TableField("oper_account")
	private String operAccount;
	
	@ApiModelProperty("下单时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@TableField("submit_time")
	private Date submitTime;
	
	@ApiModelProperty("订单总额")
	@Max(99999999L)
	@TableField("total_price")
	private Float totalPrice;
	
	@ApiModelProperty("用户名称")
	@Length(min=0, max=32)
	@TableField("user_name")
	private String userName;
	
	@ApiModelProperty("用户地址")
	@Length(min=0, max=100)
	@TableField("user_address")
	private String userAddress;
	
	@ApiModelProperty("订单状态")
	@Max(99L)
	@TableField("order_status")
	private Long orderStatus;
	
	@ApiModelProperty("订单状态名称")
	@TableField(exist=false)
	private String orderStatusName;
	
	@ApiModelProperty("是否新增")
	@TableField(exist=false)
    private Boolean isNew;
	
	@JsonIgnore
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.orderId != null) {
    		return false;
    	}
    	return true;
    }
}
