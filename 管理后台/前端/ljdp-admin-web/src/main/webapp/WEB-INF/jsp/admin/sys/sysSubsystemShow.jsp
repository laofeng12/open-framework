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

<title>子系统管理</title>
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
			<h5>子系统管理 <small>基础信息</small></h5>
		</div>
		<div class="ibox-content">
			<form id="contentForm" method="post" class="form-horizontal">
				<input type="hidden" name="systemid" value="${m.systemid}">
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">系统id：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.systemid}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">系统名称：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.sysname}</p>
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
	                    <label class="col-sm-2 control-label">系统LOGO：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.logo}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">默认地址：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.defaulturl}</p>
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
	                    <label class="col-sm-2 control-label">创建时间：</label>
	                    <div class="col-sm-10">
	                    	<fmt:formatDate value="${m.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">创建人员：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.creator}</p>
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
	                    <label class="col-sm-2 control-label">是否需要组织：</label>
	                    <div class="col-sm-10">
	                    	<p class="form-control-static">${m.needorgName}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否激活：</label>
	                    <div class="col-sm-10">
	                    	<p class="form-control-static">${m.isactiveName}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否本地：</label>
	                    <div class="col-sm-10">
	                    	<p class="form-control-static">${m.islocalName}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">主页：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.homepage}</p>
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