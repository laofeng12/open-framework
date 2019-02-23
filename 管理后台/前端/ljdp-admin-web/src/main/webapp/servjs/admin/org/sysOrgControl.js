var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {radio: true},
                {title: '组织ID', field: 'orgid'},
        {title: 'DEMID', field: 'demid'},
        {title: '组织名称', field: 'orgname'},
        {title: '组织描述', field: 'orgdesc'},
        {title: '上级组织', field: 'orgsupid'},
        {title: '路径', field: 'path'},
        {title: 'DEPTH', field: 'depth'},
        {title: '组织类型', field: 'orgtypeName'},
        {title: '创建人id', field: 'creatorid'},
        {title: '创建时间', field: 'createtime'},
        {title: '更新人id', field: 'updateid'},
        {title: '更新时间', field: 'updatetime'},
        {title: 'SN', field: 'sn'},
        {title: '来源', field: 'fromtype'},
        {title: '路径全名', field: 'orgpathname'}
    ];
	var tc_height = $(window).height() -$('#myQueryForm').outerHeight(true)
		-$('#myToolbar').outerHeight(true)-5;
	$myTable = mvc.bootstrap.pageTable({
			url: _R.SysOrg['search'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			height: tc_height,
			columns: $columns
		})
	
	//新增按钮
	$('#addBtn').click(function(){
		open(_R.SysOrg['addPage']);
	});
	//修改按钮
	$('#editBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.SysOrg['editPage']+checkeds[0].orgid);
		}
	});
	
	//查看按钮
	$('#showBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.SysOrg['showPage']+checkeds[0].orgid);
		}
	});
	
	//删除按钮
	$('#deleteBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			if(confirm('确定要删除吗？')==true){
				var url = _R.SysOrg['delete'];
				$.ajax({
					url: url,
					data: {id:checkeds[0].orgid},
					type: 'POST',
					success: function(data,ts){
						alert('删除成功')
						$myTable.bootstrapTable('refresh', {});
					}
				})
			}
		}
	});
	
	
});