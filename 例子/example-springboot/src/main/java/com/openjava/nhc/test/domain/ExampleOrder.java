package com.openjava.nhc.test.domain;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Max;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Persistable;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author 自由
 *
 */
@ApiModel("订单管理")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "EXAMPLE_ORDER")
public class ExampleOrder implements Persistable<String>,Serializable {
	
	@ApiModelProperty("订单id")
	@Id
	@Column(name = "order_id")
	private String orderId;
	
	@ApiModelProperty("下单账号")
	@Length(min=0, max=32)
	@Column(name = "oper_account")
	private String operAccount;
	
	@ApiModelProperty("下单时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submit_time")
	private Date submitTime;
	
	@ApiModelProperty("订单总额")
	@Max(99999999L)
	@Column(name = "total_price")
	private Float totalPrice;
	
	@ApiModelProperty("用户名称")
	@Length(min=0, max=32)
	@Column(name = "user_name")
	private String userName;
	
	@ApiModelProperty("用户地址")
	@Length(min=0, max=100)
	@Column(name = "user_address")
	private String userAddress;
	
	@ApiModelProperty("订单状态")
	@Max(99L)
	@Column(name = "order_status")
	private Float orderStatus;
	@ApiModelProperty("订单状态名称")
	@Transient
	private String orderStatusName;
	
	
	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public String getId() {
        return this.orderId;
	}
    
    @JsonIgnore
    @Transient
    @Override
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