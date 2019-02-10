package org.ljdp.component.paging;

import org.apache.commons.lang3.StringUtils;

public class Page {
	private int start;
	private int limit;
	
	private int pageno;
	private int pagesize;
	
	private boolean initPageNo = false;
	private boolean initPageSize = false;
	
//	private int pageStartIndex = 1;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
		if(limit != 0) {
			this.pageno = start / limit + 1;
		}
		initPageNo = true;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
		this.pagesize = limit;
		if(limit != 0) {
			this.pageno = start / limit + 1;
		}
		initPageSize = true;
	}
	
	public int getPageno() {
	    if(pageno <= 0) {
	        pageno = 1;
	    }
		return pageno;
	}
	
	public void setPageno(int pageno) {
		this.pageno = pageno;
		this.start = (pageno - 1) * this.limit;
		initPageNo = true;
	}

	public void setPageno(String _pageno) {
		if(StringUtils.isNotBlank(_pageno)) {
			setPageno(Integer.parseInt(_pageno));
		} else {
			setPageno(1);
		}
	}

	public int getPagesize() {
	    if ( pagesize <= 0 ) {
            pagesize = Integer.MAX_VALUE;
        }
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
		this.limit = pagesize;
		this.start = (this.pageno - 1) * this.limit;
		initPageSize = true;
	}
	
	public void setPagesize(String _pagesize) {
		if(StringUtils.isNotBlank(_pagesize)) {
			setPagesize(Integer.parseInt(_pagesize));
		} else {
			setPagesize(10);
		}
	}
	
	public boolean isInitPaging() {
		return initPageNo && initPageSize;
	}
}
