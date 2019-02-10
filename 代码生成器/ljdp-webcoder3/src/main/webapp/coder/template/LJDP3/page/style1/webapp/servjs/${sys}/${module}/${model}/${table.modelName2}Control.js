var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {radio: true},
        <#list table.columnList as item>
        <#if item.userDict == true>
        {title: '${item.comment}', field: '${item.columnName}Name'}<#if item_has_next>,</#if>
        <#else>
        {title: '${item.comment}', field: '${item.columnName}'}<#if item_has_next>,</#if>
        </#if>
        </#list>
    ];
	var tc_height = $(window).height() -$('#myQueryForm').outerHeight(true)
		-$('#myToolbar').outerHeight(true)-5;
	$myTable = mvc.bootstrap.pageTable({
			url: _R.${table.modelName}['search'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			height: tc_height,
			columns: $columns
		})
	
	<#if baseFun.add == "on">
	//新增按钮
	$('#addBtn').click(function(){
		open(_R.${table.modelName}['addPage']);
	});
	//修改按钮
	$('#editBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.${table.modelName}['editPage']+checkeds[0].${table.keyField});
		}
	});
	</#if>
	
	//查看按钮
	$('#showBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.${table.modelName}['showPage']+checkeds[0].${table.keyField});
		}
	});
	
	<#if baseFun.delete == "on">
	//删除按钮
	$('#deleteBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			if(confirm('确定要删除吗？')==true){
				var url = _R.${table.modelName}['delete'];
				$.ajax({
					url: url,
					data: {id:checkeds[0].${table.keyField}},
					type: 'POST',
					success: function(data,ts){
						layer.msg('删除成功');
						$myTable.bootstrapTable('refresh', {});
					}
				})
			}
		}
	});
	</#if>
	
	<#if baseFun.export == "on">
	//导出按钮
	$('#exportBtn').click(function(){
		document.getElementById('myQueryForm').action = _R.${table.modelName}['export'];
		document.getElementById('myQueryForm').submit();
	});
	</#if>
	
	<#if baseFun.importFun == "on">
	//导入按钮
	$('#importBtn').click(function(){
		open(_R.${table.modelName}['batchPage']);
	});
	</#if>
});