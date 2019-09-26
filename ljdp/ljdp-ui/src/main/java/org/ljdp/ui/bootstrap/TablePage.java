package org.ljdp.ui.bootstrap;

import java.util.List;

import org.ljdp.component.result.ApiResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("分页对象")
public interface TablePage<T> extends ApiResponse{

	@ApiModelProperty("总数据量")
	public long getTotal();
	@ApiModelProperty("数据集")
	public List<T> getRows();
	@ApiModelProperty("总页数")
	public long getTotalPage();
	@ApiModelProperty("每页显示数量")
	public long getSize();
	@ApiModelProperty("当前页码（从0开始）")
	public long getNumber();
}
