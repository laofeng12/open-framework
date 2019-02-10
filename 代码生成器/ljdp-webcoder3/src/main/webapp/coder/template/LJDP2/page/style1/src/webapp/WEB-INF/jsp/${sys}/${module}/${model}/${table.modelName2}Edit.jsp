<#assign context = "$\{pageContext.request.contextPath}"/>
<#assign success = "$\{success}"/>
<#assign editAble = "$\{editAble}"/>
<#assign isNew = "$\{isNew}"/>
<#assign d = "$"/>
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

<title>${resName}</title>
<link href="${context}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
<link href="${context}/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
<link href="${context}/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
<link href="${context}/css/animate.min.css" rel="stylesheet">
<link href="${context}/css/style.min.css?v=4.1.0" rel="stylesheet">

<script type="text/javascript">
var contextPath = '${context}';
</script>
</head>

<body class="white-bg">
	<div class="container animated fadeInLeft">
		<div class="ibox-title">
			<h5>${resName} <small>基础信息配置</small></h5>
		</div>
		<div class="ibox-content">
			<form id="contentForm" method="post" class="form-horizontal">
				<input type="hidden" name="isNew" value="${isNew}">
				<input type="hidden" name="${table.keyField}" value="${d}{m.${table.keyField}}">
				<#list table.rowCloumnList as rows>
					<#list rows as item>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">${item.comment}：</label>
	                    <div class="col-sm-10">
	                    	<#if item.javaDataType == "Date">
	                    	<input type="text" class="form-control laydate-icon" name="${item.columnName}" value='<fmt:formatDate value="${d}{m.${item.columnName}}" pattern="yyyy-MM-dd HH:mm:ss" />'
								 onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
	                    	<#else>
	                        <input type="text" class="form-control" name="${item.columnName}" value="${d}{m.${item.columnName}}">
	                        </#if>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
					</#list>
				</#list>
				
	            <div class="row">
                    <div class="col-sm-3 pull-right">
                		<div class="form-group">
                        <button class="btn btn-primary" type="submit">保存内容</button>
                        <button class="btn btn-white" type="button" onclick="if(confirm('确定要关闭此窗口吗？')){window.close()}">取消</button>
                		</div>
                    </div>
                </div>
			</form>
		</div>
	</div>
	

	<script src="${context}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${context}/js/bootstrap.min.js?v=3.3.6"></script>
	<script src="${context}/js/plugins/bootstrap-table/bootstrap-table.min.js?v=1.10.1"></script>
	<script src="${context}/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js?v=1.10.1"></script>
	<script src="${context}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js?v=1.10.1"></script>
	
	<!-- 日期：laydate -->
	<script src="${context}/laydate/laydate.js"></script>
	
	
	<script type="text/javascript" src="${context}/lib/zymvc-2.0.js"></script>
	<!-- 业务组件 -->
	<script type="text/javascript" src="${context}/res/${sys}/${module}/${model}/${table.modelName2}Model.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function () {
		var success = '${success}';
		var editAble = '${editAble}';
		//提交完成后也返回此页面提示
		if(success === 'true'){
			alert('保存成功');
			window.opener.$myTable.bootstrapTable('selectPage', 1);
			window.close();
		}
		if(editAble == 'false'){
			//点击查看时，禁止编辑提交
			$(':input').attr("readonly","readonly");
			$(':submit').hide();
		} else{
			//设置提交地址
			$('#contentForm').attr('action', resources['save'])
		}
	})
	</script>
</body>

</html>