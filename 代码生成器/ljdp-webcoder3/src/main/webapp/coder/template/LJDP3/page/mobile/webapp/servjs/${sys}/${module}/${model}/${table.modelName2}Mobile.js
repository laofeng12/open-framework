var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        <#list table.columnList as item>
        <#if item.userDict == true>
        {title: '${item.comment}', field: '${item.columnName}Name'}<#if item_has_next>,</#if>
        <#else>
        {title: '${item.comment}', field: '${item.columnName}'}<#if item_has_next>,</#if>
        </#if>
        </#list>
    ];
	
	$myTable = mvc.bootstrap.pageTable({
			mobile:true,
			url: _R.${table.modelName}['search'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			columns: $columns
		})
	
});