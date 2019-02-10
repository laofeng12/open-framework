package com.openjava.admin.org.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.admin.org.domain.SysOrg;
import com.openjava.admin.org.service.SysOrgService;
import com.openjava.tree.ZTreeNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.openjava.admin.org.query.SysOrgDBParam;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="组织机构管理")
@RestController
@RequestMapping("/admin/org/sysOrg")
public class SysOrgAction {
	
	@Resource
	private SysOrgService sysOrgService;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public SysOrg get(@PathVariable("id")Long id) {
		SysOrg m = sysOrgService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_orgname", value = "组织名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_orgtype", value = "组织类型=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_orgsupid", value = "上级组织=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysOrg> doSearch(SysOrgDBParam params, Pageable pageable){
		if(StringUtils.isNotBlank(params.getLike_orgname())) {
			params.setLike_orgname("%"+params.getLike_orgname()+"%");
		}
		Page<SysOrg> result =  sysOrgService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "orgid", value = "组织ID", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "orgname", value = "组织名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "orgdesc", value = "组织描述", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "orgsupid", value = "上级组织", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "orgtype", value = "组织类型", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "fromtype", value = "来源", required = false, dataType = "Short", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(SysOrg model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setOrgid(ss.getSequence());
			if(model.getDemid() == null) {
				model.setDemid(1L);
			}
			if(model.getOrgtype() == null) {
				model.setOrgtype(1L);
			}
			model.setCreatetime(new Date());
			String[] paths = sysOrgService.getFullPath(model.getOrgid()+"", model.getOrgname(), model.getOrgsupid());
			model.setPath(paths[0]);
			model.setOrgpathname(paths[1]);
			SysOrg dbObj = sysOrgService.doSave(model);
		} else {
			//修改，记录更新时间等
			SysOrg db = sysOrgService.get(model.getOrgid());
			db.setOrgname(model.getOrgname());
			db.setOrgdesc(model.getOrgdesc());
			db.setUpdatetime(new Date());
			sysOrgService.doSave(db);
		}
		DataApiResponse resp = new DataApiResponse();
		resp.setCode(200);
		return resp;
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")Long id) {
		sysOrgService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "[树]获取所有组织",notes="返回ztree.js树结构,使用simple模式")
	@Security(session=true)
	@RequestMapping(value="/doZTree",method=RequestMethod.GET)
	public List<ZTreeNode> doZTree() {
		List<SysOrg> list = sysOrgService.findAll();
		
		List<ZTreeNode> trees = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysOrg o = (SysOrg) list.get(i);
			ZTreeNode n = new ZTreeNode(o.getOrgid().toString(), 
					o.getOrgname(), o.getOrgsupid().toString());
			n.setOpen(true);
			n.setDrag(true);
			n.setSort(i);
			trees.add(n);
		}
		return trees;
	}
}
