<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="private" />

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
	<div class="container-fluid animated fadeInRight">
		<div class="ibox-content">
			<!-- 查询条件 -->
			<form method="get" class="form-horizontal" id="myQueryForm">
			<div class="row">
				<div class="col-sm-4">
					<div class="form-group">
						<label class="col-sm-4 control-label">组织名称like：</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" name="like_orgname">
						</div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="form-group">
						<label class="col-sm-4 control-label">上级组织=：</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" name="eq_orgsupid">
						</div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="form-group">
						<label class="col-sm-4 control-label">组织类型=：</label>
						<div class="col-sm-8">
							<select class="form-control m-b" name="eq_orgtype"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=org.type">
						      	<option value="">==请选择==</option>
						    </select>
						</div>
					</div>
				</div>
			</div>
			</form>
			<!-- 工具条 -->
			<div id="myToolbar" class="btn-group hidden-xs" role="group">
				<button type="button" class="btn btn-outline btn-default" id="showBtn">
					<i class="glyphicon glyphicon-book" aria-hidden="true"></i> 查看
				</button>
				<button type="button" class="btn btn-outline btn-default" id="addBtn">
					<i class="glyphicon glyphicon-plus" aria-hidden="true"></i> 新增
				</button>
				<button type="button" class="btn btn-outline btn-default" id="editBtn">
					<i class="glyphicon glyphicon-edit" aria-hidden="true"></i> 编辑
				</button>
				<button type="button" class="btn btn-outline btn-default" id="deleteBtn">
					<i class="glyphicon glyphicon-trash" aria-hidden="true"></i> 删除
				</button>
				<button type="button" class="btn btn-outline btn-default" id="importBtn">
					<i class="glyphicon glyphicon-import" aria-hidden="true"></i> 导入
				</button>
				<button type="button" class="btn btn-outline btn-default" id="exportBtn">
					<i class="glyphicon glyphicon-export" aria-hidden="true"></i> 导出
				</button>
			</div>
			<!-- 分页表格 -->
			<table id="myTable" style="width: 2500px">
				
			</table>
		</div>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js?v=3.3.7"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap-table/bootstrap-table.min.js?v=1.11.0"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js?v=1.11.0"></script>
	
	<!-- 日期：laydate -->
	<script src="${pageContext.request.contextPath}/laydate/laydate.js"></script>
	
	<script src="${pageContext.request.contextPath}/lib/zymvc-2.0.min.js"></script>
	<!-- 业务组件 -->
	<script src="${pageContext.request.contextPath}/servjs/admin/resources.js?v=${jver}"></script>
	<script src="${pageContext.request.contextPath}/servjs/admin/org/sysOrgControl.js?v=${jver}"></script>
<script type="text/javascript">
$(document).ready(function () {
	
})
</script>
</body>
</html>