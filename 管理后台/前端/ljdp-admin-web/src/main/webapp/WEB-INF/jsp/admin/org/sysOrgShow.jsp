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

<title>组织管理</title>
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
			<h5>组织管理 <small>基础信息</small></h5>
		</div>
		<div class="ibox-content">
			<form id="contentForm" method="post" class="form-horizontal">
				<input type="hidden" name="orgid" value="${m.orgid}">
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">组织ID：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.orgid}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">DEMID：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.demid}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">组织名称：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.orgname}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">组织描述：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.orgdesc}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">上级组织：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.orgsupid}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">路径：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.path}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">DEPTH：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.depth}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">组织类型：</label>
	                    <div class="col-sm-10">
	                    	<p class="form-control-static">${m.orgtypeName}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">创建人id：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.creatorid}</p>
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
	                    <label class="col-sm-2 control-label">更新人id：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.updateid}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">更新时间：</label>
	                    <div class="col-sm-10">
	                    	<fmt:formatDate value="${m.updatetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">SN：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.sn}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">来源：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.fromtype}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">路径全名：</label>
	                    <div class="col-sm-10">
	                        <p class="form-control-static">${m.orgpathname}</p>
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