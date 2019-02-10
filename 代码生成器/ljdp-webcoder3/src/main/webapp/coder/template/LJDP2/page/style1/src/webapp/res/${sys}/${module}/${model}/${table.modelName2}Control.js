var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {radio: true},
        <#list table.columnList as item>
        {title: '${item.comment}', field: '${item.columnName}'}<#if item_has_next>,</#if>
        </#list>
    ];
	var tc_height = $(window).height() -$('#myQueryForm').outerHeight(true)
		-$('#myToolbar').outerHeight(true)-5;
	$myTable = zybootstrap.pageTable({
			url: resources['query'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			height: tc_height,
			columns: $columns
		})
	
	//新增按钮
	$('#addBtn').click(function(){
		open(resources['addPage']);
	});
	//修改按钮
	$('#editBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(resources['editPage']+checkeds[0].${table.keyField});
		}
	});
	//查看按钮
	$('#showBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(resources['showPage']+checkeds[0].${table.keyField});
		}
	});
	//删除按钮
	$('#deleteBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			if(confirm('确定要删除吗？')==true){
				var url = resources['delete'];
				$.ajax({
					url: url,
					data: {id:checkeds[0].${table.keyField}},
					type: 'POST',
					success: function(data,ts){
						alert('删除成功')
						$myTable.bootstrapTable('refresh', {});
					}
				})
			}
		}
	});
	
	//导出按钮
	$('#exportBtn').click(function(){
		document.getElementById('myQueryForm').action = resources['export'];
		document.getElementById('myQueryForm').submit();
	});
	
	//导出按钮
	$('#importBtn').click(function(){
		open(resources['batchPage']);
	});
});