
	
	<#if model == "">
${table.modelName}: {
		//------------页面------------
		<#if baseFun.add == "on">
		'addPage'	: contextPath+"/${sys}/${module}/${table.modelName2}/edit.ht?method=add",//新增页面
		'editPage'	: contextPath+"/${sys}/${module}/${table.modelName2}/edit.ht?method=edit&id=",//修改页面
		</#if>
		'showPage'	: contextPath+"/${sys}/${module}/${table.modelName2}/show.ht?id=",//查看页面
		
		//------------CRUD------------
		<#if baseFun.add == "on">
		'save'		: contextPath+"/api/${sys}/${module}/${table.modelName2}",//保存
		</#if>
		<#if baseFun.delete == "on">
		'delete'	: contextPath+"/api/${sys}/${module}/${table.modelName2}/delete",//删除
		</#if>
		'search'	: contextPath+"/api/${sys}/${module}/${table.modelName2}/search",//搜索
		
		<#if baseFun.export == "on">
		//--------导出--------
		'export'	: contextPath+"/api/${sys}/${module}/${table.modelName2}/export",
		</#if>
		
		<#if baseFun.importFun == "on">
		//-------------批量业务组件-----------
		'batchPage'	: contextPath+"/${sys}/${module}/${table.modelName2}/batch.ht",//批量处理页面
		'batchComponent': '${table.modelName2}BatchBO',//文件导入处理组件
		'batchType'		: '${resName}批量'//任务类型，用于记录日志
		</#if>
	}
	<#else>
	${table.modelName}: {
		//------------页面------------
		<#if baseFun.add == "on">
		'addPage'	: contextPath+"/${sys}/${module}/${model}/${table.modelName2}/edit.ht?method=add",//新增页面
		'editPage'	: contextPath+"/${sys}/${module}/${model}/${table.modelName2}/edit.ht?method=edit&id=",//修改页面
		</#if>
		'showPage'	: contextPath+"/${sys}/${module}/${model}/${table.modelName2}/show.ht?id=",//查看页面
		
		//------------CRUD------------
		<#if baseFun.add == "on">
		'save'		: contextPath+"/api/${sys}/${module}/${model}/${table.modelName2}",//保存
		</#if>
		<#if baseFun.delete == "on">
		'delete'	: contextPath+"/api/${sys}/${module}/${model}/${table.modelName2}/delete",//删除
		</#if>
		'search'	: contextPath+"/api/${sys}/${module}/${model}/${table.modelName2}/search",//搜索
		
		<#if baseFun.export == "on">
		//--------导出--------
		'export'	: contextPath+"/api/${sys}/${module}/${model}/${table.modelName2}/export",
		</#if>
		
		<#if baseFun.importFun == "on">
		//-------------批量业务组件-----------
		'batchPage'	: contextPath+"/${sys}/${module}/${model}/${table.modelName2}/batch.ht",//批量处理页面
		'batchComponent': '${table.modelName2}BatchBO',//文件导入处理组件
		'batchType'		: '${resName}批量'//任务类型，用于记录日志
		</#if>
	}
	</#if>

