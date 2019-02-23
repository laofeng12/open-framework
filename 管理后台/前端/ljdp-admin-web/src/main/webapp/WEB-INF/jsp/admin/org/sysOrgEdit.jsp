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
			<h5>${title } <small>基础信息配置</small></h5>
		</div>
		<div class="ibox-content">
			<form id="contentForm" method="post" class="form-horizontal">
				<input type="hidden" name="isNew" value="${isNew}">
				<input type="hidden" name="orgid" value="${m.orgid}">
				<input type="hidden" name="demid" value="${m.demid}">
				<input type="hidden" name="orgsupid" value="${m.orgsupid}">
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">组织名称：</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="orgname" value="${m.orgname}">
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">组织描述：</label>
	                    <div class="col-sm-10">
	                    	<textarea rows="10" style="width: 90%" name="orgdesc">${m.orgdesc}</textarea>
	                    </div>
	                </div>
	            </div>
	            
	            <%-- <div class="hr-line-dashed"></div>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">组织类型：</label>
	                    <div class="col-sm-10">
							<select class="form-control" name="orgtype" value="${m.orgtype}"
						      	mvc_combox="${pageContext.request.contextPath}/api/framework/sys/code/list?codetype=org.type">
						      	<option value="">==请选择==</option>
						    </select>
	                    </div>
	                </div>
	            </div> --%>
	            
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
		/* mvc.ready(function(){
			$('[name=orgtype]').val("${m.orgtype}")
		}); */
		//改为 ajax提交
		$('#contentForm').submit(function() {
			$('#allsubmit').attr("disabled","disabled");
			$(this).ajaxSubmit({
	   	        success	: function(data){
		   	 		if(data.code == 200){
			   	 		alert('保存成功');
			   	 		parent.removeIframe();
			   	 		//parent.location.reload();
		   	    	} else{
		   	    		$('#allsubmit').removeAttr("disabled");
		   	    		alert(data.message);
		   	    	}
		   		},
	   	        url		: _R.SysOrg['save'],
	   	        type	: 'post', 
	   	        dataType: 'json'
	   	    });
			return false;
		})
	})
	</script>
</body>

</html>