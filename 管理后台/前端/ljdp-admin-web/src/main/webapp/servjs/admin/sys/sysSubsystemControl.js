var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {radio: true},
        {title: '系统id', field: 'systemid'},
        {title: '系统名称', field: 'sysname'},
        {title: '别名', field: 'alias'},
        {title: '系统LOGO', field: 'logo'},
        {title: '默认地址', field: 'defaulturl'},
        {title: '备注', field: 'memo'},
        {title: '创建时间', field: 'createtime', visible:false},
        {title: '创建人员', field: 'creator', visible:false},
        {title: '是否允许删除', field: 'allowdelName', visible:false},
        {title: '是否需要组织', field: 'needorgName', visible:false},
        {title: '是否激活', field: 'isactiveName'},
        {title: '是否本地', field: 'islocalName', visible:false},
        {title: '主页', field: 'homepage'}
    ];
	var tc_height = $(window).height() -$('#myQueryForm').outerHeight(true)
		-$('#myToolbar').outerHeight(true)-5;
	$myTable = mvc.bootstrap.pageTable({
			url: _R.SysSubsystem['search'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			height: tc_height,
			columns: $columns
		})
	
	//新增按钮
	$('#addBtn').click(function(){
		open(_R.SysSubsystem['addPage']);
	});
	//修改按钮
	$('#editBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.SysSubsystem['editPage']+checkeds[0].systemid);
		}
	});
	
	//查看按钮
	$('#showBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			open(_R.SysSubsystem['showPage']+checkeds[0].systemid);
		}
	});
	
	//删除按钮
	$('#deleteBtn').click(function(){
		var checkeds = $myTable.bootstrapTable('getSelections');
		if(checkeds.length === 1){
			if(confirm('确定要删除吗？')==true){
				var url = _R.SysSubsystem['delete'];
				$.ajax({
					url: url,
					data: {id:checkeds[0].systemid},
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