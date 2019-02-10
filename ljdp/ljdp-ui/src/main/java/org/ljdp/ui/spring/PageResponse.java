package org.ljdp.ui.spring;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * page对象接口返回结构
 * @author hzy0769
 *
 * @param <T>
 */
public class PageResponse<T> extends BasicApiResponse implements Page<T>,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5044442399767603939L;
	private final Page<T> page;

	public PageResponse(Page<T> page) {
		super(APIConstants.CODE_SUCCESS);
		this.page = page;
	}
	
	@ApiModelProperty("当前页码（从0开始）")
	@Override
	public int getNumber() {
		return page.getNumber();
	}
	
	@ApiModelProperty("每页大小")
	@Override
	public int getSize() {
		return page.getSize();
	}
	
	@ApiModelProperty("本页数据量")
	@Override
	public int getNumberOfElements() {
		return page.getNumberOfElements();
	}
	
	@ApiModelProperty("是否有上一页")
	@Override
	public boolean hasPrevious() {
		return page.hasPrevious();
	}
	
	@ApiModelProperty("是否第一页")
	@Override
	public boolean isFirst() {
		return page.isFirst();
	}
	
	@ApiModelProperty("是否有下一页")
	@Override
	public boolean hasNext() {
		return page.hasNext();
	}
	
	@ApiModelProperty("是否最后一页")
	@Override
	public boolean isLast() {
		return page.isLast();
	}

	@ApiModelProperty("总页数")
	@Override
	public int getTotalPages() {
		return page.getTotalPages();
	}

	@ApiModelProperty("总数据量")
	@Override
	public long getTotalElements() {
		return page.getTotalElements();
	}

	@Override
	public List<T> getContent() {
		return page.getContent();
	}

	@Override
	public boolean hasContent() {
		return page.hasContent();
	}

	@JsonIgnore
	@ApiIgnore
	@Override
	public Sort getSort() {
		return page.getPageable().getSort();
	}

	@JsonIgnore
	@ApiIgnore
	@Override
	public Pageable nextPageable() {
		return page.nextPageable();
	}

	@JsonIgnore
	@ApiIgnore
	@Override
	public Pageable previousPageable() {
		return page.previousPageable();
	}

	@JsonIgnore
	@ApiIgnore
	@Override
	public Iterator<T> iterator() {
		return page.iterator();
	}

	@JsonIgnore
	@ApiIgnore
	@Override
	public <U> Page<U> map(Function<? super T, ? extends U> converter) {
		return page.map(converter);
	}

}
