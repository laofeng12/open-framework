package org.ljdp.ui.bootstrap;

import java.util.List;

import org.ljdp.component.result.BasicApiResponse;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TablePageImpl<T> extends BasicApiResponse implements TablePage<T> {
	private static final long serialVersionUID = -7232710708767097630L;
	
	private long total;
	private List<T> rows;
	private int totalPage;
	private int size;
	private int number;
	
	public TablePageImpl() {
		
	}
	
	public TablePageImpl(Page<T> page) {
		this.total = page.getTotalElements();
		this.rows = page.getContent();
		this.totalPage = page.getTotalPages();
		this.size = page.getSize();
		this.number = page.getNumber();
	}
	
	public TablePageImpl(long total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
