<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<title>资源管理</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css?v=3.3.7" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/font-awesome.min.css?v=4.6.3" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/animate.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css?v=${cver}" rel="stylesheet">
<style type="text/css">
div.content_wrap {}

div.zTreeDemoBackground {text-align:left;}

ul.ztree {margin-top: 0px;border: 1px solid #617775;width:100%;height:100%;overflow-y:scroll;overflow-x:auto;}
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}

.mytree li span.button{line-height:0; margin:0; width:16px; height:16px; display: inline-block; vertical-align:middle;
	border:0 none; cursor: pointer;outline:none;
	background-color:transparent; background-repeat:no-repeat; background-attachment: scroll;
	background-image:url("/js/plugins/ztree/zTreeStyle/img/zTreeStandard.png");}
.mytree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}

.panel-toolbar {
    height: 32px;
    margin-top: 0px;
    padding: 3px 5px 5px;
    background: #D9E9FF;
    border-bottom: solid 1px #cacaca;
    border-top: solid 1px #cacaca;
}
.group {
    margin-right: 2px;
    margin-left: 0px;
    padding-left: 0px;
    float: right;
}
.l-bar-separator {
    border-left: 1px solid #9AC6FF;
    border-right: 1px solid white;
    float: right;
    height: 18px;
    margin: 2px;
}
.btn-sm {
    padding: 3px 10px;
}
</style>
<script type="text/javascript">
var contextPath = '${pageContext.request.contextPath}';

var roleid='${roleid}';

</script>
</head>

<body>
		
	<div class="panel-toolbar">
		<div class="l-bar-separator"></div>
		<div class="group">
			<button class="btn btn-save btn-sm" data-loading-text="正在保存..." id="allsubmit">保 存</button>
		</div>
	</div>
	<div class="zTreeDemoBackground">
		<ul id="resTree" class="ztree"></ul>
	</div>

	<script src="${pageContext.request.contextPath}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${pageContext.request.contextPath}/lib/jquery.form.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js?v=3.3.7"></script>

	<!-- 日期：laydate -->
	<script src="${pageContext.request.contextPath}/laydate/laydate.js"></script>
	<!-- ztree -->
	<link href="${pageContext.request.contextPath}/js/plugins/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js/plugins/ztree/jquery.ztree.all.min.js"></script>
	
	<script src="${pageContext.request.contextPath}/js/plugins/layer/layer.min.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/zymvc-2.0.min.js"></script>
	<!-- 业务组件 -->
	<script src="${pageContext.request.contextPath}/servjs/admin/resources.js?v=${jver}"></script>
	<script src="${pageContext.request.contextPath}/servjs/admin/res/sysResRoleTree.js?v=${jver}"></script>
	<script type="text/javascript">
	$(document).ready(function () {
		
	})
	</script>
</body>

</html>