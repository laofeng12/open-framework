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

<title>用户管理</title>
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
			<h5>用户管理 <small>基础信息配置</small></h5>
		</div>
		<div class="ibox-content">
			<form id="contentForm" method="post" class="form-horizontal">
				<input type="hidden" name="isNew" value="${isNew}">
				<input type="hidden" name="userid" value="${m.userid}">
				
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">名称：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="fullname" value="${m.fullname}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">帐号类型：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="accounttype" value="${m.accounttype}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=sys.account.type">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">登录账号：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="account" value="${m.account}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">密码：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="password" value="${m.password}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否过期：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="isexpired" value="${m.isexpired}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=public.YN">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">是否锁定：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="islock" value="${m.islock}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=public.YN">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <%-- <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">创建时间：</label>
	                    <div class="col-sm-10">
	                    	<input type="text" class="form-control laydate-icon" name="createtime" value='<fmt:formatDate value="${m.createtime}" pattern="yyyy-MM-dd HH:mm:ss" />'
								 onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
	                    </div>
	                </div>
	            </div> --%>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">状态：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="status" value="${m.status}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=sys.user.state">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">邮箱：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="email" value="${m.email}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">手机号码：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="mobile" value="${m.mobile}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">电话：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="phone" value="${m.phone}">
	                    </div>
	                </div>
	            </div>
	            <%-- <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">性别：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="sex" value="${m.sex}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">头像：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="picture" value="${m.picture}">
	                    </div>
	                </div>
	            </div>
	            
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">来源类型：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="fromtype" value="${m.fromtype}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=sys.user.fromtype">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div>
	             --%>
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
			$('[name=accounttype]').val("${m.accounttype}")
			$('[name=isexpired]').val("${m.isexpired}")
			$('[name=islock]').val("${m.islock}")
			$('[name=status]').val("${m.status}")
			$('[name=fromtype]').val("${m.fromtype}")
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
	   	        url		: _R.SysUser['save'],
	   	        type	: 'post', 
	   	        dataType: 'json'
	   	    });
			return false;
		})
	})
	</script>
</body>

</html>