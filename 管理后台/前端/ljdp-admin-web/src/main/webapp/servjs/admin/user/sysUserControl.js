var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {radio: true},
        {title: '用户id', field: 'userid', visible:false},
        {title: '帐号类型', field: 'accounttypeName', visible:false},
        {title: '来源类型', field: 'fromtypeName', visible:false},
        {title: '名称', field: 'fullname'},
        {title: '登录账号', field: 'account'},
        {title: '手机号码', field: 'mobile', visible:false},
        {title: '是否过期', field: 'isexpiredName'},
        {title: '是否锁定', field: 'islockName'},
        {title: '创建时间', field: 'createtime'},
        {title: '状态', field: 'statusName'}
    ];
	var tc_height = $(window).height() -$('#myQueryForm').outerHeight(true)-35;
	$myTable = mvc.bootstrap.pageTable({
			url: _R.SysUser['search'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			height: tc_height,
			columns: $columns
		})
	
	//新增按钮
	$('#addBtn').click(function(){
		open(_R.SysUser['addPage']);
	});
	//修改按钮
	$('#editBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.SysUser['editPage']+checkeds[0].userid);
		}
	});
	
	//查看按钮
	$('#showBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.SysUser['showPage']+checkeds[0].userid);
		}
	});
	
	//删除按钮
	$('#deleteBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			if(confirm('确定要删除吗？')==true){
				var url = _R.SysUser['delete'];
				$.ajax({
					url: url,
					data: {id:checkeds[0].userid},
					type: 'POST',
					success: function(data,ts){
						alert('删除成功')
						$myTable.bootstrapTable('refresh', {});
					}
				})
			}
		}
	});
	$('#orgBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			layer.open({
				type: 2,
				title: '设置用户组织：'+checkeds[0].fullname,
				shadeClose: true,
				shade: 0.8,
				area: ['80%', '90%'],
				content: _R.SysUserOrg['list']+checkeds[0].userid //iframe的url
			});
		}
	})
	$('#roleBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			layer.open({
				type: 2,
				title: '设置用户角色：'+checkeds[0].fullname,
				shadeClose: true,
				shade: 0.8,
				area: ['80%', '90%'],
				content: _R.SysUserRole['list']+checkeds[0].userid //iframe的url
			});
		}
	})
});