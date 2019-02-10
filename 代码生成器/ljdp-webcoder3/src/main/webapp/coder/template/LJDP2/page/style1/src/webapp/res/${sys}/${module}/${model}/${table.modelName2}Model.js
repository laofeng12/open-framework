/**
 * 定义使用的资源
 */
var resources = $.extend({
	//------------页面------------
	'addPage'	: contextPath+"/${sys}/${module}/${model}/${table.modelName}/add.jspx",//新增页面
	'editPage'	: contextPath+"/${sys}/${module}/${model}/${table.modelName}/edit.jspx?id=",//修改页面
	'showPage'	: contextPath+"/${sys}/${module}/${model}/${table.modelName}/show.jspx?id=",//查看页面
	
	//------------CRUD------------
	'query'		: contextPath+"/${sys}/${module}/${model}/${table.modelName}/search.act",//查询
	'save'		: contextPath+"/${sys}/${module}/${model}/${table.modelName}/save.act",//保存
	'delete'	: contextPath+"/${sys}/${module}/${model}/${table.modelName}/delete.act",//删除
	
	//--------导出--------
	'export'	: contextPath+"/${sys}/${module}/${model}/${table.modelName}/export.act",

	//-------------批量业务组件-----------
	'batchPage'		: contextPath+"/${sys}/${module}/${model}/${table.modelName}/batch.jspx",//批量处理页面
	'batchComponent': '${table.modelName2}BatchBO',//文件导入处理组件
	'batchType'		: '${resName}批量'//任务类型，用于记录日志
}, ljdp.resources)
