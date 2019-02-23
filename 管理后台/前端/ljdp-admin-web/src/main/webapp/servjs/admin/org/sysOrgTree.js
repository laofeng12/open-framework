var setting = {
	async: {
		enable: true,
		url: _R.SysOrg['ztree'],
		type: 'get'
	},
	view: {
		addHoverDom: addHoverDom,
		removeHoverDom: removeHoverDom,
		selectedMulti: false
	},
	edit: {
		enable: true,
		editNameSelectAll: true,
		showRemoveBtn: showRemoveBtn,
		showRenameBtn: false
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeRemove: beforeRemove,
		onRemove: onRemove,
		onClick: onClick
	}
};
var log, className = "dark";
function onClick(event, treeId, treeNode, clickFlag) {
	$('#ifrcontent').attr('src', _R.SysOrg['editPage']+treeNode.id)
}

function beforeRemove(treeId, treeNode) {
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" beforeRemove ] " + treeNode.name);
	var zTree = $.fn.zTree.getZTreeObj("orgTree");
	zTree.selectNode(treeNode);
	return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
}
function onRemove(e, treeId, treeNode) {
	showLog("[ "+getTime()+" onRemove ] " + treeNode.name);
	$.ajax({
		url: _R.SysOrg['delete'],
		type: 'POST',
		dataType : "json",
		data: {
			id: treeNode.id
		},
		success: function(resp){
			layer.msg('删除成功');
			$('#ifrcontent').attr('src','');
		}
	})
}

function showRemoveBtn(treeId, treeNode) {
	return !treeNode.isParent;
}

function showLog(str) {
	console.log(str);
}
function getTime() {
	var now= new Date(),
	h=now.getHours(),
	m=now.getMinutes(),
	s=now.getSeconds(),
	ms=now.getMilliseconds();
	return (h+":"+m+":"+s+ " " +ms);
}

var newCount = 1;
function addHoverDom(treeId, treeNode) {
	if(treeNode.getPath().length >= 4){
		return;
	}
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='add node' onfocus='this.blur();'></span>";
	sObj.after(addStr);
	var btn = $("#addBtn_"+treeNode.tId);
	if (btn) btn.bind("click", function(){
		$('#ifrcontent').attr('src', _R.SysOrg['addPage']+treeNode.id)
		return false;
	});
};
function removeHoverDom(treeId, treeNode) {
	$("#addBtn_"+treeNode.tId).unbind().remove();
};


$(document).ready(function(){
	$.fn.zTree.init($("#orgTree"), setting);
	
});
function removeIframe(){
	$('#ifrcontent').attr('src','');
	var treeObj = $.fn.zTree.getZTreeObj('orgTree');
	treeObj.reAsyncChildNodes(null, "refresh");
}