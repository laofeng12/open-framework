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
			<h5>角色管理 <small>基础信息配置</small></h5>
		</div>
		<div class="ibox-content">
			<form id="contentForm" method="post" class="form-horizontal">
				<input type="hidden" name="isNew" value="${isNew}">
				<input type="hidden" name="roleid" value="${m.roleid}">
	            <%-- <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">系统编码：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="systemid" value="${m.systemid}">
	                    </div>
	                </div>
	            </div> --%>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">角色名称：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="rolename" value="${m.rolename}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">别名：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="alias" value="${m.alias}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">备注：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="memo" value="${m.memo}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否允许删除：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="allowdel" value="${m.allowdel}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=public.YN">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否允许编辑：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="allowedit" value="${m.allowedit}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=public.YN">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否启用：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="enabled" value="${m.enabled}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=public.YN">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
	            <div class="row">
                    <div class="col-sm-3 pull-right">
                		<div class="form-group">
                        <button class="btn btn-save" id="allsubmit" type="submit">保存内容</button>
                        <button class="btn btn-white" type="button" onclick="if(confirm('确定要关闭此窗口吗？')){window.close()}">取消</button>
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
			$('[name=allowdel]').val("${m.allowdel}")
			$('[name=allowedit]').val("${m.allowedit}")
			$('[name=enabled]').val("${m.enabled}")
		});
		//改为 ajax提交
		$('#contentForm').submit(function() {
			$('#allsubmit').attr("disabled","disabled");
			$(this).ajaxSubmit({
	   	        success	: function(data){
		   	 		if(data.code == 200){
			   	 		alert('保存成功');
						window.opener.$myTable.bootstrapTable('selectPage', 1);
						window.close();
		   	    	} else{
		   	    		$('#allsubmit').removeAttr("disabled");
		   	    		alert(data.message);
		   	    	}
		   		},
	   	        url		: _R.SysRole['save'],
	   	        type	: 'post', 
	   	        dataType: 'json'
	   	    });
			return false;
		})
	})
	</script>
</body>

</html>