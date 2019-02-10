package com.openjava.admin.sys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.sys.domain.SysNotice;
import com.openjava.admin.sys.query.SysNoticeDBParam;
import com.openjava.admin.sys.repository.SysNoticeRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author heizyou
 *
 */
@Service
@Transactional
public class SysNoticeServiceImpl implements SysNoticeService {
	
	@Resource
	private SysNoticeRepository sysNoticeRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	public Page<SysNotice> query(SysNoticeDBParam params, Pageable pageable){
		Page<SysNotice> pageresult = sysNoticeRepository.query(params, pageable);
		Map<String, SysCode> noticetype = sysCodeService.getCodeMap("notice.type");
		Map<String, SysCode> noticestatus = sysCodeService.getCodeMap("notice.status");
		for (SysNotice m : pageresult.getContent()) {
			if(m.getNtype() != null) {
				SysCode c = noticetype.get(m.getNtype().toString());
				if(c != null) {
					m.setNtypeName(c.getCodename());
				}
			}
			if(m.getNstatus() != null) {
				SysCode c = noticestatus.get(m.getNstatus().toString());
				if(c != null) {
					m.setNstatusName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<SysNotice> queryDataOnly(SysNoticeDBParam params, Pageable pageable){
		return sysNoticeRepository.queryDataOnly(params, pageable);
	}
	
	public SysNotice get(String id) {
		Optional<SysNotice> o = sysNoticeRepository.findById(id);
		if(o.isPresent()) {
			SysNotice m = o.get();			
			if(m.getNtype() != null) {
				Map<String, SysCode> noticetype = sysCodeService.getCodeMap("notice.type");
				SysCode c = noticetype.get(m.getNtype().toString());
				if(c != null) {				
					m.setNtypeName(c.getCodename());
				}
			}
			if(m.getNstatus() != null) {
				Map<String, SysCode> noticestatus = sysCodeService.getCodeMap("notice.status");
				SysCode c = noticestatus.get(m.getNstatus().toString());
				if(c != null) {				
					m.setNstatusName(c.getCodename());
				}
			}
			return m;
		}
		return null;
	}
	
	public SysNotice doSave(SysNotice m) {
		return sysNoticeRepository.save(m);
	}
	
	public void doDelete(String id) {
		sysNoticeRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			sysNoticeRepository.deleteById(new String(items[i]));
		}
	}
	
	@Override
	public int doPublish(String ids) {
		String[] items = ids.split(",");
		List<String> nidList = new ArrayList<>();
		for (int i = 0; i < items.length; i++) {
			nidList.add(items[i]);
		}
		return sysNoticeRepository.publish(nidList);
	}
	
	@Override
	public int doDownNotic(String ids) {
		String[] items = ids.split(",");
		List<String> nidList = new ArrayList<>();
		for (int i = 0; i < items.length; i++) {
			nidList.add(items[i]);
		}
		return sysNoticeRepository.downNotic(nidList);
	}
	
	@Override
	public List<SysNotice> findMyNotice(){
		Pageable pageable = new PageRequest(0, 10);
		return sysNoticeRepository.findByPublish(pageable);
	}
	
	@Override
	public int auditPass(String nid) {
		return sysNoticeRepository.auditPass(nid);
	}
	@Override
	public int auditNotPass(String nid) {
		return sysNoticeRepository.auditNotPass(nid);
	}
}
