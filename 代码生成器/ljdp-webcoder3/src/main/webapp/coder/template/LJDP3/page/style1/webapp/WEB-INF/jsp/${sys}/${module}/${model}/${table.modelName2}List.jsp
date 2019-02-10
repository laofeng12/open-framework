<#assign context = "$\{pageContext.request.contextPath}"/>
<#assign d = "$"/>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="private" />

<title>${resName}</title>
<link href="${context}/css/bootstrap.min.css?v=3.3.7" rel="stylesheet">
<link href="${context}/css/font-awesome.min.css?v=4.6.3" rel="stylesheet">
<link href="${context}/css/plugins/bootstrap-table/bootstrap-table.min.css?v=1.11.0" rel="stylesheet">
<link href="${context}/css/animate.min.css" rel="stylesheet">
<link href="${context}/css/style.css?v=${d}{cver}" rel="stylesheet">

<script type="text/javascript">
var contextPath = '${context}';
</script>
</head>
<body class="white-bg">
	<div class="container-fluid animated fadeInRight">
		<div class="ibox-content">
			<!-- 查询条件 -->
			<form method="get" class="form-horizontal" id="myQueryForm">
			<#list rowDbParamList as rows>
			<div class="row">
				<#list rows as item>
				<div class="col-sm-3">
					<div class="form-group">
						<label class="col-sm-4 control-label">${item.name}${item.symbol}：</label>
						<div class="col-sm-8">
							<#if item.extJsFieldType=='combo'>
							<select class="form-control m-b" name="${item.condition}_${item.columnName}">
	                            <option value="">==请选择==</option>
								<option value="true">空</option>
								<option value="false">非空</option>
	                        </select>
							<#elseif item.extJsFieldType=='dictCombo'>
							<select class="form-control m-b" name="${item.condition}_${item.columnName}"
						      	mvc_combox="${context}/api/framework/sys/code/list?codetype=${item.dictDefined}">
						      	<option value="">==请选择==</option>
						    </select>
						    <#elseif item.extJsFieldType=='datefield'>
						    <input type="text" class="form-control laydate-icon" name="${item.condition}_${item.columnName}"
									 onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
							<#else>
							<input type="text" class="form-control" name="${item.condition}_${item.columnName}">
							</#if>
						</div>
					</div>
				</div>
				</#list>
			</div>
			</#list>
			</form>
			<!-- 工具条 -->
			<div id="myToolbar" class="btn-group hidden-xs" role="group">
				<button type="button" class="btn btn-outline btn-default" id="showBtn">
					<i class="glyphicon glyphicon-book" aria-hidden="true"></i> 查看
				</button>
				<#if baseFun.add == "on">
				<button type="button" class="btn btn-outline btn-default" id="addBtn">
					<i class="glyphicon glyphicon-plus" aria-hidden="true"></i> 新增
				</button>
				<button type="button" class="btn btn-outline btn-default" id="editBtn">
					<i class="glyphicon glyphicon-edit" aria-hidden="true"></i> 编辑
				</button>
				</#if>
				<#if baseFun.delete == "on">
				<button type="button" class="btn btn-outline btn-default" id="deleteBtn">
					<i class="glyphicon glyphicon-trash" aria-hidden="true"></i> 删除
				</button>
				</#if>
				<#if baseFun.importFun == "on">
				<button type="button" class="btn btn-outline btn-default" id="importBtn">
					<i class="glyphicon glyphicon-import" aria-hidden="true"></i> 导入
				</button>
				</#if>
				<#if baseFun.export == "on">
				<button type="button" class="btn btn-outline btn-default" id="exportBtn">
					<i class="glyphicon glyphicon-export" aria-hidden="true"></i> 导出
				</button>
				</#if>
			</div>
			<!-- 分页表格 -->
			<table id="myTable" style="width: 2500px">
				
			</table>
		</div>
	</div>
	
	<script src="${context}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${context}/js/bootstrap.min.js?v=3.3.7"></script>
	<script src="${context}/js/plugins/bootstrap-table/bootstrap-table.min.js?v=1.11.0"></script>
	<script src="${context}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js?v=1.11.0"></script>
	<!-- layui组件 -->
	<script src="${context}/js/plugins/layer3/layer.js"></script>
	<script src="${context}/laydate/laydate.js"></script>
	
	<script src="${context}/lib/zymvc-2.0.min.js"></script>
	<!-- 业务组件 -->
	<script src="${context}/servjs/${sys}/resources.js?v=${d}{jver}"></script>
	<#if model == "">
	<script src="${context}/servjs/${sys}/${module}/${table.modelName2}Control.js?v=${d}{jver}"></script>
	<#else>
	<script src="${context}/servjs/${sys}/${module}/${model}/${table.modelName2}Control.js?v=${d}{jver}"></script>
	</#if>
<script type="text/javascript">
$(document).ready(function () {
	
})
</script>
</body>
</html>