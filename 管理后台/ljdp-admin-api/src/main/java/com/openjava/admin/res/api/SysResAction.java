package com.openjava.admin.res.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.plugin.sys.resp.ResourceResp;
import org.ljdp.plugin.sys.vo.MenuVO;
import org.ljdp.plugin.sys.vo.ResourceVO;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;

import com.openjava.admin.res.domain.SysRes;
import com.openjava.admin.res.service.SysResService;
import com.openjava.admin.role.domain.SysRoleRes;
import com.openjava.admin.role.service.SysRoleResService;
import com.openjava.admin.sys.domain.SysSubsystem;
import com.openjava.admin.sys.service.SysSubsystemService;
import com.openjava.tree.AntdTreeNode;
import com.openjava.tree.ZTreeNode;
import com.openjava.admin.res.query.SysResDBParam;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="资源管理")
@RestController
@RequestMapping("/admin/res/sysRes")
public class SysResAction {
	
	@Resource
	private SysResService sysResService;
	@Resource
	private SysRoleResService sysRoleResService;
	@Resource
	private SysSubsystemService sysSubsystemService;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public SysRes get(@PathVariable("id")Long id) {
		SysRes m = sysResService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_resname", value = "资源名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_isfolder", value = "可否展开=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_isdisplayinmenu", value = "是否默认菜单=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_isopen", value = "是否打开=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_parentid", value = "父资源id=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "like_defaulturl", value = "默认URLlike", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysRes> doSearch(SysResDBParam params, Pageable pageable){
		Page<SysRes> result =  sysResService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "resname", value = "资源名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "alias", value = "资源别名", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "icon", value = "图标", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "parentid", value = "父资源id", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "defaulturl", value = "默认URL", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "isfolder", value = "可否展开", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "isdisplayinmenu", value = "是否默认菜单", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "isopen", value = "是否打开", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "systemid", value = "系统编码", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(SysRes model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setResid(ss.getSequence());
			SysRes parent = sysResService.get(model.getParentid());
			if(parent != null) {
				model.setSystemid(parent.getSystemid());
			} else {
				//可能是子系统下面的根节点
				SysSubsystem subs = sysSubsystemService.get(model.getParentid());
				if(subs != null) {
					model.setParentid(0L);//设置为根节点
					model.setSystemid(subs.getSystemid());
				}
			}
			model.setPath(sysResService.getFullPath(model.getResid().toString(), model.getParentid()));
			sysResService.doSave(model);
		} else {
			//修改，记录更新时间等
			SysRes db = sysResService.get(model.getResid());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
			db.setPath(model.getPath());
			sysResService.doSave(db);
		}
		DataApiResponse resp = new DataApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")Long id) {
		sysResService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "[树]获取所有资源",notes="返回ztree.js树结构,使用simple模式")
	@Security(session=true)
	@RequestMapping(value="/doZTree",method=RequestMethod.GET)
	public List<ZTreeNode> doZTree() {
		List<ZTreeNode> trees = new ArrayList<>();
		//第一级为系统列表
		List<SysSubsystem> syslist = sysSubsystemService.findAll();
		for (int i = 0; i < syslist.size(); i++) {
			SysSubsystem s = syslist.get(i);
			ZTreeNode n = new ZTreeNode(s.getSystemid().toString(), 
					s.getSysname(), "0");
			n.setOpen(false);
			n.setDrag(false);
			n.setSort(i);
			trees.add(n);
		}
		//第二级开始才是资源
		List<SysRes> list = sysResService.findAllInSort();
		for (int i = 0; i < list.size(); i++) {
			SysRes r = (SysRes) list.get(i);
			ZTreeNode n = new ZTreeNode(r.getResid().toString(), 
					r.getResname(), r.getParentid().toString());
			if(n.getpId().equals("0")) {
				n.setpId(r.getSystemid().toString());
			}
			n.setOpen(false);
			n.setDrag(true);
			n.setSort(i);
			trees.add(n);
		}
		return trees;
	}
	
	@ApiOperation(value = "[树]获取角色拥有的资源权限",notes="返回ztree.js树结构,使用simple模式")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleid", value = "角色编码", required = true, paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/zTreeByRole",method=RequestMethod.GET)
	public List<ZTreeNode> zTreeByRole(@RequestParam("roleid")Long roleid) {
		List<ZTreeNode> trees = new ArrayList<>();
		//第一级为系统列表
		List<SysSubsystem> syslist = sysSubsystemService.findAll();
		for (int i = 0; i < syslist.size(); i++) {
			SysSubsystem s = syslist.get(i);
			ZTreeNode n = new ZTreeNode(s.getSystemid().toString(), 
					s.getSysname(), "0");
			n.setOpen(false);
			n.setDrag(false);
			n.setNocheck(true);
			n.setSort(i);
			trees.add(n);
		}
		//第二级开始为菜单
		List<SysRes> list = sysResService.findAllInSort();
		//当前角色拥有的权限
		List<SysRoleRes> roleResList = sysRoleResService.findByRoleid(roleid);
		
		for (int i = 0; i < list.size(); i++) {
			SysRes r = (SysRes) list.get(i);
			ZTreeNode n = new ZTreeNode(r.getResid().toString(), 
					r.getResname(), r.getParentid().toString());
			if(n.getpId().equals("0")) {
				n.setpId(r.getSystemid().toString());
			}
			n.setOpen(false);
			n.setDrag(false);
			n.setChecked(false);
			n.setSort(i);
			if(r.getSort() != null && r.getSort() > 0) {
				n.setSort(r.getSort());
			}
			trees.add(n);
			
			roleResList.forEach(rr -> {
				if(rr.getResid().equals(r.getResid())) {
					n.setChecked(true);
				}
			});
		}
		return trees;
	}
	
	@ApiOperation(value = "[树]获取角色拥有的资源权限(ant design)",notes="返回ant design的树结构")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleid", value = "角色编码", required = true, paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/antTreeByRole",method=RequestMethod.GET)
	public DataApiResponse<AntdTreeNode> antTreeByRole(@RequestParam("roleid")Long roleid) {
		List<ZTreeNode> ztrees = zTreeByRole(roleid);
		
		AntdTreeNode root = new AntdTreeNode("0", "根(不用显示根节点)", "");
		antdAddChildNode(ztrees, root, "0");
		
		DataApiResponse<AntdTreeNode> resp = new DataApiResponse<>();
		resp.setRows(root.getChildren());
		return resp;
	}
	
	/**
	 * 把ztree的list结构，转为antdesign的树结构
	 * @param ztrees
	 * @param node
	 * @param parentkey
	 */
	private void antdAddChildNode(List<ZTreeNode> ztrees, AntdTreeNode node, String parentkey) {
		for (ZTreeNode zn : ztrees) {
			if(zn.getpId().equals(parentkey)) {
				AntdTreeNode child = new AntdTreeNode(zn.getId(), zn.getName(), parentkey);
				child.setChecked(zn.getChecked());
				node.getChildren().add(child);
				antdAddChildNode(ztrees, child, child.getKey());
			}
		}
	}
	
	@ApiOperation(value = "[树]获取用户拥有的菜单权限",notes="返回树结构")
	@Security(session=true)
	@RequestMapping(value="/myMenus",method=RequestMethod.GET)
	public ResourceResp myMenus() {
		ResourceResp resp = myResources(null);
		//转换为ant design pro的菜单结构
		if(!resp.getResources().isEmpty()) {
			List<MenuVO> menus = new ArrayList(resp.getResources().size());
			resp.getResources().forEach(r -> {
				menus.add(MenuVO.fromResources(r));
			});
			resp.setMenus(menus);
			resp.setResources(null);
		}
		return resp;
	}
	
	@ApiOperation(value = "[树]获取用户拥有的菜单权限(ID)",notes="返回树结构")
	@Security(session=true)
	@RequestMapping(value="/myMenuIds",method=RequestMethod.GET)
	public ResourceResp myMenuIds() {
		List<SysRes> myres = sysResService.findMyRes(SsoContext.getUserId());
		//提前所有资源的id
		List<String> resourceIds = new ArrayList(myres.size());
		if(!myres.isEmpty()) {
			//递归把所有菜单提取出来
			myres.forEach(r -> {
				resourceIds.add(r.getResid().toString());
			});
		}
		ResourceResp resp = new ResourceResp();
		resp.setResourceIds(resourceIds);
		return resp;
	}
	
	@ApiOperation(value = "[树]获取用户拥有的资源权限",notes="返回树结构")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "用户编码", required = true, paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/myResources",method=RequestMethod.GET)
	public ResourceResp myResources(@RequestParam(value="userId",required=false)Long userId) {
		if(userId == null) {
			userId = SsoContext.getUserId();
		}
		
		List<SysRes> list = sysResService.findMyRes(userId);
		if(list.isEmpty()) {
			ResourceResp resp = new ResourceResp();
			resp.setResources(new ArrayList<>(0));
			return resp;
		}
		//根据父节点进行分组
		Map<String,List<ResourceVO>> map = new HashMap<>();
		list.forEach(r -> {
//			System.out.println(r.getResid()+","+r.getResname());
			if(map.containsKey(r.getParentid().toString())) {
				map.get(r.getParentid().toString()).add(toResVO(r));
			} else {
				List<ResourceVO> group = new ArrayList<>();
				group.add(toResVO(r));
				map.put(r.getParentid().toString(), group);
			}
		});
		
		//获取根节点列表
		List<ResourceVO> roots = map.get("0");
		recursionAddSubRes(roots, map);
		
		ResourceResp resp = new ResourceResp();
		resp.setResources(roots);
		return resp;
	}
	
	private ResourceVO toResVO(SysRes r) {
		ResourceVO v = new ResourceVO();
		v.setResId(r.getResid().toString());
		v.setResName(r.getResname());
		v.setParentResId(r.getParentid().toString());
		if(r.getIcon() != null) {
			if(!r.getIcon().startsWith("/")) {
				v.setResIcon(r.getIcon());
			}
		}
		if(v.getResIcon() == null) {
			v.setResIcon("fa fa-columns");
		}
		v.setResURL(r.getDefaulturl());
		return v;
	}
	
	/**
	 * 递归添加子节点
	 * @param currentList
	 * @param map
	 */
	private void recursionAddSubRes(List<ResourceVO> currentList, Map<String,List<ResourceVO>> map) {
		if(currentList == null) {
			return;
		}
		currentList.forEach(r -> {
			if(map.containsKey(r.getResId())) {
				//当前节点有子节点
				List<ResourceVO> sublist = map.get(r.getResId());
				r.setSubResList(sublist);
				//对子列表继续同样操作
				recursionAddSubRes(sublist, map);
			}
		});
	}
}
