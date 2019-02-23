var treeDom='resTree';
var setting = {
	async: {
		enable: true,
		url: _R.SysRes['ztreeByRole']+roleid,
		type: 'get'
	},
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};


$(document).ready(function(){
	$.fn.zTree.init($("#"+treeDom), setting);
	$('#allsubmit').click(function(){
		$('#allsubmit').button('loading');
		var index = layer.load();
		//获取所有选择的菜单
		var treeObj = $.fn.zTree.getZTreeObj(treeDom);
		var nodes = treeObj.getCheckedNodes(true);
		var checkIds = '';
		for(var i=0;i<nodes.length; i++){
			checkIds += nodes[i].id+","
		}
		//alert(checkIds)
		//保存
		$.ajax({
			url: _R.SysRole['updateRes'],
			type: 'POST',
			dataType : "json",
			data: {
				'roleid': roleid,
				'resIds': checkIds
			},
			success: function(resp){
				layer.close(index);
				layer.msg('更新成功');
				setTimeout(function(){
					parent.closeAll();
				},3000);
			}
		})
	})
});
