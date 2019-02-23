/**
 * 定义使用的所有资源
 */
var resources = $.extend({
	SysUser: {
		//------------页面------------
		'addPage'	: contextPath+"/admin/user/sysUser/edit.ht?method=add",//新增页面
		'editPage'	: contextPath+"/admin/user/sysUser/edit.ht?method=edit&id=",//修改页面
		'showPage'	: contextPath+"/admin/user/sysUser/show.ht?id=",//查看页面
		//------------CRUD------------
		'save'		: contextPath+"/api/admin/user/sysUser",//保存
		'delete'	: contextPath+"/api/admin/user/sysUser/delete",//删除
		'search'	: contextPath+"/api/admin/user/sysUser/search",//搜索
	},
	SysOrg: {
		//------------页面------------
		'addPage'	: contextPath+"/admin/org/sysOrg/edit.ht?method=add&parentid=",//新增页面
		'editPage'	: contextPath+"/admin/org/sysOrg/edit.ht?method=edit&id=",//修改页面
		'showPage'	: contextPath+"/admin/org/sysOrg/show.ht?id=",//查看页面
		//------------CRUD------------
		'save'		: contextPath+"/api/admin/org/sysOrg",//保存
		'delete'	: contextPath+"/api/admin/org/sysOrg/delete",//删除
		'search'	: contextPath+"/api/admin/org/sysOrg/search",//搜索
		'ztree'	: contextPath+"/api/admin/org/sysOrg/doZTree",//树结构
	},
	SysUserOrg: {
		'list': contextPath+'/admin/user/sysUserOrg/list.ht?userid=',
		//------------CRUD------------
		'add'		: contextPath+"/api/admin/user/sysUserOrg/add",//添加组织
		'delete'	: contextPath+"/api/admin/user/sysUserOrg/delete",//删除
		'search'	: contextPath+"/api/admin/user/sysUserOrg/search",//搜索
	},
	SysRole: {
		//------------页面------------
		'addPage'	: contextPath+"/admin/role/sysRole/edit.ht?method=add",//新增页面
		'editPage'	: contextPath+"/admin/role/sysRole/edit.ht?method=edit&id=",//修改页面
		'showPage'	: contextPath+"/admin/role/sysRole/show.ht?id=",//查看页面
		'selectPage': contextPath+'/admin/role/sysRole/select.ht',//选择角色
		//------------CRUD------------
		'save'		: contextPath+"/api/admin/role/sysRole",//保存
		'delete'	: contextPath+"/api/admin/role/sysRole/delete",//删除
		'search'	: contextPath+"/api/admin/role/sysRole/search",//搜索
		'updateRes'	: contextPath+"/api/admin/role/sysRole/updateRes",//更新角色菜单权限
	},
	SysUserRole: {
		//------------页面------------
		'list': contextPath+'/admin/user/sysUserRole/list.ht?userid=',
		//------------CRUD------------
		'save'		: contextPath+"/api/admin/user/sysUserRole",//保存
		'delete'	: contextPath+"/api/admin/user/sysUserRole/delete",//删除
		'search'	: contextPath+"/api/admin/user/sysUserRole/search",//搜索
	},
	SysRes: {
		//------------页面------------
		'addPage'	: contextPath+"/admin/res/sysRes/edit.ht?method=add&parentid=",//新增页面
		'editPage'	: contextPath+"/admin/res/sysRes/edit.ht?method=edit&id=",//修改页面
		'roleTree'	: contextPath+"/admin/res/sysRes/roleTree.ht?roleid=",
		//------------CRUD------------
		'save'		: contextPath+"/api/admin/res/sysRes",//保存
		'delete'	: contextPath+"/api/admin/res/sysRes/delete",//删除
		'ztree'	: contextPath+"/api/admin/res/sysRes/doZTree",//树结构
		'ztreeByRole'	: contextPath+"/api/admin/res/sysRes/zTreeByRole?roleid=",//角色资源权限树
	},
	SysSubsystem: {
		//------------页面------------
		'addPage'	: contextPath+"/admin/sys/sysSubsystem/edit.ht?method=add",//新增页面
		'editPage'	: contextPath+"/admin/sys/sysSubsystem/edit.ht?method=edit&id=",//修改页面
		'showPage'	: contextPath+"/admin/sys/sysSubsystem/show.ht?id=",//查看页面
		
		//------------CRUD------------
		'save'		: contextPath+"/api/admin/sys/sysSubsystem",//保存
		'delete'	: contextPath+"/api/admin/sys/sysSubsystem/delete",//删除
		'search'	: contextPath+"/api/admin/sys/sysSubsystem/search",//搜索
	}
}, ljdp.resources);
var _R=resources;
