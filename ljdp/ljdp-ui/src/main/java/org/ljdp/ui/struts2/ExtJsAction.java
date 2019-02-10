package org.ljdp.ui.struts2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.query.RO;
import org.ljdp.ui.extjs.ExtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExtJsAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5535782239688397848L;
	private Logger log = LoggerFactory.getLogger(ExtJsAction.class);
	private Map<String, String> treeMapper;//Ext树结点中的属性与Java对象中字段的对应关系
	
	private int start;
	private int limit;
	
	private String sort;
	private String dir; 
	
	private String dateFormat = "yyyy-MM-dd";
	private String[] viewExcludes;
	
	@SuppressWarnings("unchecked")
	public void doTreeBySortID() {
		try {
			doListAll();
			//根据树的ID排序，方便建树
			Collections.sort(getDp().toList());
			ExtUtils.writeJSONTreeBySortID(getDp().toList(), treeMapper, getResponse());
		} catch(Throwable e) {
			log.info("doTreeBySortID", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection queryAllChild(Collection list) throws Exception{
		if(list == null) {
			return new ArrayList(); 
		}
		Iterator it = list.iterator();
		ArrayList childs = new ArrayList();
		while(it.hasNext()) {
			Object bean = it.next();
			Object pk = PropertyUtils.getSimpleProperty(bean, treeMapper.get("node.id"));
			DBQueryParam param = new DBQueryParam();
			param.addQueryCondition(treeMapper.get("node.parent"), RO.EQ, pk);
			Collection mylist = queryAll(param);
			Collection mychilds = queryAllChild(mylist);
			mylist.addAll(mychilds);
			childs.addAll(mylist);
		}
		return childs;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doTree() {
		try {
			doListAll();
			Collection childs = queryAllChild(getDp().getDatas());
			getDp().getDatas().addAll(childs);
			onLoadTree();
			ExtUtils.writeJSONTree(getDp().toList(), treeMapper, getResponse());			
		} catch(Throwable e) {
			log.info("doTree", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
	
	public void doAjaxShow() {
		try {
			doShow();
			ExtUtils.writeSuccess(getForm(), getResponse(), dateFormat);
		} catch (Throwable e) {
			log.info("doAjaxShow", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}

	public void doAjaxSave() {
		try {
			doSave();
			if( !getResult().isSuccess() ) {
				throw new Exception(getResult().getMsg());
			}
			ExtUtils.writeSuccess(getResponse());
		} catch(Throwable e) {
			log.info("doAjaxSave", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
	
	public void doAjaxDelete() {
		try {
			doDeleteHandle();
			ExtUtils.writeSuccess(getResponse());
		} catch(Throwable e) {
			log.info("doAjaxDelete", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
	
	public void doAjaxListAll() {
		try {
			doListAll();
			ExtUtils.writeJSONGrid(getDp(), viewExcludes, getResponse(), dateFormat);
		} catch (Throwable e) {
			log.info("doAjaxListAll", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
	
	public void doAjaxList() {
		try {
			doList();
			ExtUtils.writeJSONGrid(getDp(), viewExcludes, getResponse(), dateFormat);
		} catch (Throwable e) {
			log.info("doAjaxList", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
	
	public void doAjaxListData() {
		try {
			doListData();
			ExtUtils.writeJSONGrid(getDp(), viewExcludes, getResponse(), dateFormat);
		} catch (Throwable e) {
			log.info("doAjaxList", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
	
	protected void onLoadTree() {
		
	}
	
	public Map<String, String> getTreeMapper() {
		return treeMapper;
	}

	public void setTreeMapper(Map<String, String> treeMapper) {
		this.treeMapper = treeMapper;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
		if(limit != 0) {
			getParam().setPageno(start / limit + 1);
		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
		getParam().set_pagesize(""+limit);
		if(limit != 0) {
			getParam().setPageno(start / limit + 1);
		}
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String[] getViewExcludes() {
		return viewExcludes;
	}

	public void setViewExcludes(String[] viewExcludes) {
		this.viewExcludes = viewExcludes;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
		getParam().set_orderby(new String[] {this.sort});
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
		getParam().set_sort(new String[] {this.dir});
	}
}
