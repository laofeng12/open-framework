package org.ljdp.component.result;

import org.ljdp.component.bean.BaseVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BasicApiResponse extends BaseVO implements ApiResponse {
	private static final long serialVersionUID = 8239220462137573580L;
	
	private String requestId;
	private Integer code;
	private String message;
	private String tub;

	public BasicApiResponse() {
//		code = APIConstants.CODE_SUCCESS;
	}
	
	public BasicApiResponse(Integer code) {
		this.code = code;
	}

	public BasicApiResponse(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTub() {
		return tub;
	}

	public void setTub(String tub) {
		this.tub = tub;
	}


}
