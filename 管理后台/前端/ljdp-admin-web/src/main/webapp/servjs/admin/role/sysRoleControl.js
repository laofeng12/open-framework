var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {radio: true},
        {title: '角色id', field: 'roleid'},
        {title: '系统编码', field: 'systemid', visible:false},
        {title: '别名', field: 'alias'},
        {title: '角色名称', field: 'rolename'},
        {title: '备注', field: 'memo'},
        {title: '是否允许删除', field: 'allowdelName', visible:false},
        {title: '是否允许编辑', field: 'alloweditName', visible:false},
        {title: '是否启用', field: 'enabledName'}
    ];
	var tc_height = $(window).height() -$('#myQueryForm').outerHeight(true)
		-$('#myToolbar').outerHeight(true);
	$myTable = mvc.bootstrap.pageTable({
			url: _R.SysRole['search'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			height: tc_height,
			columns: $columns
		})
	
	//新增按钮
	$('#addBtn').click(function(){
		open(_R.SysRole['addPage']);
	});
	//修改按钮
	$('#editBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.SysRole['editPage']+checkeds[0].roleid);
		}
	});
	
	//查看按钮
	$('#showBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.SysRole['showPage']+checkeds[0].roleid);
		}
	});
	
	//删除按钮
	$('#deleteBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			if(confirm('确定要删除吗？')==true){
				var url = _R.SysRole['delete'];
				$.ajax({
					url: url,
					data: {id:checkeds[0].roleid},
					type: 'POST',
					success: function(data,ts){
						layer.msg('删除成功');
						$myTable.bootstrapTable('refresh', {});
					}
				})
			}
		}
	});
	
	$('#setResBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			layer.open({
				type: 2,
				title: '设置角色权限：'+checkeds[0].rolename,
				shadeClose: true,
				shade: 0.8,
				area: ['50%', '90%'],
				content: _R.SysRes['roleTree']+checkeds[0].roleid //iframe的url
			});
		}
	});
});

function closeAll(){
	layer.closeAll('iframe');
}