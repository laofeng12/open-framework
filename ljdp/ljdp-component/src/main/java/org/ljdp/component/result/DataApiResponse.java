package org.ljdp.component.result;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("接口返回通用结构")
public class DataApiResponse<T> extends BasicApiResponse {
	private static final long serialVersionUID = -6152401171263059652L;
	
	@ApiModelProperty("单个数据")
	private T data;
	
	@ApiModelProperty("数组")
	private List<T> rows;

	public DataApiResponse() {
		super(APIConstants.CODE_SUCCESS);
	}
	
	public DataApiResponse(Integer code) {
		super(code);
	}

	public DataApiResponse(Integer code, String message) {
		super(code, message);
	}

	public DataApiResponse(Integer code, String message, T data) {
		super(code, message);
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
