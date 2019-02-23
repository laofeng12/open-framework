<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="private" />

<title>角色选择</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css?v=3.3.7" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/font-awesome.min.css?v=4.6.3" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap-table/bootstrap-table.min.css?v=1.11.0" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/animate.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css?v=${cver}" rel="stylesheet">

<script type="text/javascript">
var contextPath = '${pageContext.request.contextPath}';
</script>
</head>
<body class="white-bg">
	<div class="container-fluid animated fadeInRight">
		<div class="ibox-content">
			<!-- 查询条件 -->
			<form method="get" class="form-horizontal" id="myQueryForm">
			<div class="row">
				<div class="col-xs-2">
					<label class="control-label">名称：</label><br>
					<input type="text" class="form-control" name="like_rolename">
					<label class="control-label">别名：</label><br>
					<input type="text" class="form-control" name="like_alias">
				</div>
				<div class="col-xs-10">
					<!-- 分页表格 -->
					<table id="myTable" style="width: 2500px">
						
					</table>
				</div>
			</div>
			</form>
			
		</div>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js?v=3.3.7"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap-table/bootstrap-table.min.js?v=1.11.0"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js?v=1.11.0"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/layer3/layer.js"></script>
	<!-- 日期：laydate -->
	<script src="${pageContext.request.contextPath}/laydate/laydate.js"></script>
	
	<script src="${pageContext.request.contextPath}/lib/zymvc-2.0.min.js"></script>
	<!-- 业务组件 -->
	<script src="${pageContext.request.contextPath}/servjs/admin/resources.js?v=${jver}"></script>
	<script src="${pageContext.request.contextPath}/servjs/admin/role/sysRoleSelect.js?v=${jver}"></script>
<script type="text/javascript">
$(document).ready(function () {
	
})
</script>
</body>
</html>