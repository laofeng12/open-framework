var $myTable;
jQuery(function($) {
	"use strict";
	//构建分页查询表格
	var $columns = [
        {title: '角色id', field: 'roleid', visible:false},
        {title: '系统编码', field: 'systemid', visible:false},
        {title: '别名', field: 'alias'},
        {title: '角色名称', field: 'rolename'},
        {title: '备注', field: 'memo', visible:false},
        {title: '是否允许删除', field: 'allowdelName', visible:false},
        {title: '是否允许编辑', field: 'alloweditName', visible:false},
        {title: '是否启用', field: 'enabledName'}
    ];
	var tc_height = $(window).height()-35;
	$myTable = mvc.bootstrap.pageTable({
			url: _R.SysRole['search'],//查询接口
			queryForm : '#myQueryForm',//查询条件录入
			table: '#myTable',//展示列表
			toolbar: "#myToolbar",//按钮区域
			height: tc_height,
			columns: $columns,
			onClickCell: function(field,value,row,el){
				if(field == 'enabledName'){
					alert('双击确定选择')
				}
			},
			onDblClickRow: function(row, el,field){
				window.parent.doAddRole(row.roleid)
			}
		})
	
	
});