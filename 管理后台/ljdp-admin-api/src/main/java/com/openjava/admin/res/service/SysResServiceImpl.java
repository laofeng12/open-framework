package com.openjava.admin.res.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.res.domain.SysRes;
import com.openjava.admin.res.query.SysResDBParam;
import com.openjava.admin.res.repository.SysResRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysResServiceImpl implements SysResService {
	
	@Resource
	private SysResRepository sysResRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	@Override
	public Page<SysRes> query(SysResDBParam params, Pageable pageable){
		Page<SysRes> pageresult = sysResRepository.query(params, pageable);
		Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
		for (SysRes m : pageresult.getContent()) {
			if(m.getIsfolder() != null) {
				SysCode c = publicYN.get(m.getIsfolder().toString());
				if(c != null) {
					m.setIsfolderName(c.getCodename());
				}
			}
			if(m.getIsdisplayinmenu() != null) {
				SysCode c = publicYN.get(m.getIsdisplayinmenu().toString());
				if(c != null) {
					m.setIsdisplayinmenuName(c.getCodename());
				}
			}
			if(m.getIsopen() != null) {
				SysCode c = publicYN.get(m.getIsopen().toString());
				if(c != null) {
					m.setIsopenName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	@Override
	public List<SysRes> queryDataOnly(SysResDBParam params, Pageable pageable){
		return sysResRepository.queryDataOnly(params, pageable);
	}
	
	@Override
	public SysRes get(Long id) {
		Optional<SysRes> o = sysResRepository.findById(id);
		if(o.isPresent()) {
			SysRes m = o.get();
			Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
			if(m.getIsfolder() != null) {
				SysCode c = publicYN.get(m.getIsfolder().toString());
				m.setIsfolderName(c.getCodename());
			}
			if(m.getIsdisplayinmenu() != null) {
				SysCode c = publicYN.get(m.getIsdisplayinmenu().toString());
				m.setIsdisplayinmenuName(c.getCodename());
			}
			if(m.getIsopen() != null) {
				SysCode c = publicYN.get(m.getIsopen().toString());
				m.setIsopenName(c.getCodename());
			}
			return m;
		}
		System.out.println("找不到菜单资源："+id);
		return null;
	}
	
	@Override
	public SysRes doSave(SysRes m) {
		return sysResRepository.save(m);
	}
	
	@Override
	public void doDelete(Long id) {
		sysResRepository.deleteById(id);
	}
	
	@Override
	public List<SysRes> findAll(){
		return sysResRepository.findAll();
	}
	
	/**
	 * 获取完整路径信息
	 * @param pathId 完整路径id
	 * @param parentid 上级节点id
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getFullPath(String pathId, Long parentid){
		if(parentid != null) {
			Optional<SysRes> o = sysResRepository.findById(parentid);
			if(o.isPresent()) {
				SysRes p = o.get();
				pathId = p.getResid()+":"+pathId;
				return getFullPath(pathId, p.getParentid());
			} else {
				pathId = parentid+"."+pathId;
			}
		}
		return pathId;
	}
	
	@Override
	public List<SysRes> findMyRes(Long userid){
		return sysResRepository.findMyRes(userid);
	}
	
	@Override
	public List<SysRes> findAllInSort(){
		return sysResRepository.findAllInSort();
	}
}
