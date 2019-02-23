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
			<h5>${title } <small>基础信息配置</small></h5>
		</div>
		<div class="ibox-content">
			<form id="contentForm" method="post" class="form-horizontal">
				<input type="hidden" name="isNew" value="${isNew}">
				<input type="hidden" name="resid" value="${m.resid}">
				<input type="hidden" name="parentid" value="${m.parentid}">
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">资源名称：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="resname" value="${m.resname}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">资源别名：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="alias" value="${m.alias}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">图标：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="icon" value="${m.icon}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">默认URL：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="defaulturl" value="${m.defaulturl}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">可否有子节点：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="isfolder" value="${m.isfolder}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=public.YN">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">显示到菜单：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="isdisplayinmenu" value="${m.isdisplayinmenu}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=public.YN">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">默认打开：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="isopen" value="${m.isopen}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=public.YN">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">排序：</label>
	                    <div class="col-sm-10">
							<input type="text" class="form-control" name="sort" value="${m.sort}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
	            <div class="row">
                    <div class="col-sm-3 pull-right">
                		<div class="form-group">
                        <button class="btn btn-save" id="allsubmit" type="submit">保存内容</button>
                		</div>
                    </div>
                </div>
			</form>
		</div>
	</div>


	<script src="${pageContext.request.contextPath}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${pageContext.request.contextPath}/lib/jquery.form.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js?v=3.3.7"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap-table/bootstrap-table.min.js?v=1.11.0"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js?v=1.11.0"></script>
	<!-- 日期：laydate -->
	<script src="${pageContext.request.contextPath}/laydate/laydate.js"></script>
	
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/zymvc-2.0.min.js"></script>
	<!-- 业务组件 -->
	<script src="${pageContext.request.contextPath}/servjs/admin/resources.js?v=${jver}"></script>
	
	<script type="text/javascript">
	$(document).ready(function () {
		mvc.ready(function(){
			$('[name=isfolder]').val("${m.isfolder}")
			$('[name=isdisplayinmenu]').val("${m.isdisplayinmenu}")
			$('[name=isopen]').val("${m.isopen}")
		});
		//改为 ajax提交
		$('#contentForm').submit(function() {
			$('#allsubmit').attr("disabled","disabled");
			$(this).ajaxSubmit({
	   	        success	: function(data){
		   	 		if(data.code == 200){
			   	 		alert('保存成功');
			   	 		parent.removeIframe();
		   	    	} else{
		   	    		$('#allsubmit').removeAttr("disabled");
		   	    		alert(data.message);
		   	    	}
		   		},
	   	        url		: _R.SysRes['save'],
	   	        type	: 'post', 
	   	        dataType: 'json'
	   	    });
			return false;
		})
	})
	</script>
</body>

</html>