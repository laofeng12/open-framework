var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {radio: true},
        {title: '用户机构关系id', field: 'userorgid', visible:false},
        {title: '组织机构编码', field: 'orgid', visible:false},
        {title: '用户编码', field: 'userid', visible:false},
        {title: '组织名称', field: 'orgname'},
        {title: '路径', field: 'orgpathname'},
    ];
	var tc_height = $(window).height() -$('#myQueryForm').outerHeight(true)
		-$('#myToolbar').outerHeight(true)-5;
	$myTable = mvc.bootstrap.pageTable({
			url: _R.SysUserOrg['search'],//查询接口
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
			title: '选择组织',
			shadeClose: true,
			shade: 0.8,
			area: ['50%', '90%'],
			content: '/admin/org/sysOrg/treeSelect.ht'
		});
	});
	
	//删除按钮
	$('#deleteBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			if(confirm('确定要删除吗？')==true){
				var url = _R.SysUserOrg['delete'];
				$.ajax({
					url: url,
					data: {id:checkeds[0].userorgid},
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
//选择了组织后回调事件
function doAddOrg(orgId){
	layer.closeAll('iframe');
	//alert('父窗口：'+orgId);
	$.ajax({
		url: _R.SysUserOrg['add'],
		data: {'orgid': orgId, 'userid': myuserid},
		type: 'POST',
		success: function(data,ts){
			layer.msg('添加成功');
			$myTable.bootstrapTable('refresh', {});
		}
	})
}