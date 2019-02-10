package org.ljdp.support.attach.vo;

import org.ljdp.component.result.GeneralResult;

public class ImageResult extends GeneralResult {
	private static final long serialVersionUID = 3783851776561230520L;
	private Integer code;
	private String fid;
	private String viewUrl;

	public ImageResult(boolean succ) {
		super(succ);
	}
	
	public ImageResult() {
		super();
	}

	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getViewUrl() {
		return viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
