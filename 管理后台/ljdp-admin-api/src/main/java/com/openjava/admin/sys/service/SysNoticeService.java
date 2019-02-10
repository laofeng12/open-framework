package com.openjava.admin.sys.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.sys.domain.SysNotice;
import com.openjava.admin.sys.query.SysNoticeDBParam;

/**
 * 业务层接口
 * @author heizyou
 *
 */
public interface SysNoticeService {
	public Page<SysNotice> query(SysNoticeDBParam params, Pageable pageable);
	
	public List<SysNotice> queryDataOnly(SysNoticeDBParam params, Pageable pageable);
	
	public SysNotice get(String id);
	
	public SysNotice doSave(SysNotice m);
	
	public void doDelete(String id);
	public void doRemove(String ids);
	
	/**
	 * 发布通知
	 * @param ids
	 * @return
	 */
	public int doPublish(String ids);
	/**
	 * 下架通知
	 * @param ids
	 * @return
	 */
	public int doDownNotic(String ids);
	
	public List<SysNotice> findMyNotice();
	
	public int auditPass(String nid);
	public int auditNotPass(String nid);
}
