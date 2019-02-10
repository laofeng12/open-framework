package com.openjava.admin.org.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.org.domain.SysOrg;
import com.openjava.admin.org.query.SysOrgDBParam;
import com.openjava.admin.org.repository.SysOrgRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysOrgServiceImpl implements SysOrgService {
	
	@Resource
	private SysOrgRepository sysOrgRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	public Page<SysOrg> query(SysOrgDBParam params, Pageable pageable){
		Page<SysOrg> pageresult = sysOrgRepository.query(params, pageable);
		Map<String, SysCode> orgtype = sysCodeService.getCodeMap("org.type");
		for (SysOrg m : pageresult.getContent()) {
			if(m.getOrgtype() != null) {
				/*SysCode c = orgtype.get(m.getOrgtype().toString());
				if(c != null) {
					m.setOrgtypeName(c.getCodename());
				}*/
			}
		}
		return pageresult;
	}
	
	public List<SysOrg> queryDataOnly(SysOrgDBParam params, Pageable pageable){
		return sysOrgRepository.queryDataOnly(params, pageable);
	}
	
	public SysOrg get(Long id) {
		Optional<SysOrg> m = sysOrgRepository.findById(id);
		if(m.get().getOrgtype() != null) {
			/*Map<String, SysCode> orgtype = sysCodeService.getCodeMap("org.type");
			SysCode c = orgtype.get(m.getOrgtype().toString());
			m.setOrgtypeName(c.getCodename());*/
		}
		return m.get();
	}
	
	/**
	 * 获取组织完整路径信息
	 * @param pathId 完整路径id
	 * @param fullName 完整路径名称
	 * @param orgsupid 上级节点id
	 * @return
	 * @throws Exception
	 */
	public String[] getFullPath(String pathId, String fullName, Long orgsupid){
		if(orgsupid != null) {
			Optional<SysOrg> o = sysOrgRepository.findById(orgsupid);
			if(o.isPresent()) {
				SysOrg p = o.get();
				fullName = p.getOrgname() + "/" + fullName;
				pathId = p.getOrgid()+"."+pathId;
				return getFullPath(pathId, fullName, p.getOrgsupid());
			} else {
				fullName = "/" + fullName;
				pathId = orgsupid+"."+pathId;
			}
		}
		return new String[] {pathId, fullName};
	}
	
	public SysOrg doSave(SysOrg m) {
		return sysOrgRepository.save(m);
	}
	
	public void doDelete(Long id) {
		sysOrgRepository.deleteById(id);
	}
	
	public List<SysOrg> findAll() {
		return sysOrgRepository.findAll();
	}
}
