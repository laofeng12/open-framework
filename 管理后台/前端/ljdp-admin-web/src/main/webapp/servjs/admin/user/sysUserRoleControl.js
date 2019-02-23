var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {radio: true},
        {title: '用户角色关系id', field: 'userroleid', visible:false},
        {title: '用户id', field: 'userid', visible:false},
        {title: '角色ID', field: 'roleid'},
        {title: '角色名称', field: 'rolename'}
    ];
	var tc_height = $(window).height() -$('#myQueryForm').outerHeight(true)
		-$('#myToolbar').outerHeight(true)-5;
	$myTable = mvc.bootstrap.pageTable({
			url: _R.SysUserRole['search'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			height: tc_height,
			columns: $columns
		})
	
	//新增按钮
	$('#addBtn').click(function(){
		layer.open({
			type: 2,
			title: '选择角色',
			shadeClose: true,
			shade: 0.8,
			area: ['90%', '90%'],
			content: _R.SysRole['selectPage']
		});
	});
	
	
	//删除按钮
	$('#deleteBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			if(confirm('确定要删除吗？')==true){
				var url = _R.SysUserRole['delete'];
				$.ajax({
					url: url,
					data: {id:checkeds[0].userroleid},
					type: 'POST',
					success: function(data,ts){
						layer.msg('删除成功');
						$myTable.bootstrapTable('refresh', {});
					}
				})
			}
		}
	});
	
});
//选择了角色后回调事件
function doAddRole(roleId){
	layer.closeAll('iframe');
	$.ajax({
		url: _R.SysUserRole['save'],
		data: {'roleid': roleId, 'userid': myuserid},
		type: 'POST',
		success: function(data,ts){
			layer.msg('添加成功');
			$myTable.bootstrapTable('refresh', {});
		}
	})
}