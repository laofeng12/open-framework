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

<title>角色管理</title>
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
	<div class="container animated fadeInLeft">
		<div class="ibox-title">
			<h5>角色管理 <small>基础信息</small></h5>
		</div>
		<div class="ibox-content">
			<form id="contentForm" method="post" class="form-horizontal">
				<input type="hidden" name="roleid" value="${m.roleid}">
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">角色id：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.roleid}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">系统编码：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.systemid}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">别名：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.alias}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">角色名称：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.rolename}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">备注：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.memo}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否允许删除：</label>
	                    <div class="col-sm-10">
	                    	<p class="form-control-static">${m.allowdelName}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否允许编辑：</label>
	                    <div class="col-sm-10">
	                    	<p class="form-control-static">${m.alloweditName}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否启用：</label>
	                    <div class="col-sm-10">
	                    	<p class="form-control-static">${m.enabledName}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				
			</form>
		</div>
	</div>
	

	<script src="${pageContext.request.contextPath}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js?v=3.3.7"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap-table/bootstrap-table.min.js?v=1.11.0"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js?v=1.11.0"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/zymvc-2.0.min.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function () {
		
	})
	</script>
</body>

</html>