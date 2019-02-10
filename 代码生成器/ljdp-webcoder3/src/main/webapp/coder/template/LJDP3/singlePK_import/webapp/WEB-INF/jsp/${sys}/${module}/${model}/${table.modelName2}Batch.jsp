<#assign context = "$\{pageContext.request.contextPath}"/>
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

<title>${resName}-批量导入</title>
<link href="${context}/css/bootstrap.min.css?v=3.3.7" rel="stylesheet">
<link href="${context}/css/font-awesome.min.css?v=4.6.3" rel="stylesheet">

<link href="${context}/css/style.css?v=1.0" rel="stylesheet">

<script type="text/javascript">
var contextPath = '${context}';
</script>
</head>

<body class="white-bg">
	<div class="container">
		<div class="ibox-title">
			<h5>Excel文件导入 <small></small></h5>
		</div>
		<div class="ibox-content">
			<form id="fileBatchForm1" enctype="multipart/form-data" class="form-horizontal">
				<!--TODO： 这儿填写模块代码，或写对应表的entity映射名 -->
				<input type="hidden" name="busiPath" value="${table.modelName2}">
	            <div class="row">
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">上传</label>
	                    <div class="col-sm-10">
	                        <input type="file" class="form-control" name="file">
	                    </div>
	                </div>
				</div>
				<div class="row">
					<label class="col-sm-2 control-label">文件格式：</label>
					<div class="col-sm-10">
					<span class="help-block ">
					.xls Excle文件，参考模板【<a href="${context}/example/import/test.xlsx">批量导入模板.xlsx</a>】
					</span>
					</div>
				</div>
				<div class="row">
					<label class="col-sm-2 control-label">数据格式：</label>
					<div class="col-sm-10">
					<span class="help-block ">
					<#list table.columnList as item>${item.comment}|</#list>
					</span>
					</div>
				</div>
				<div class="row">
					<label class="col-sm-2 control-label">注意事项：</label>
					<div class="col-sm-10">
					<span class="help-block ">
					1、文件第一行为标题，系统从第二行数据开始处理
					</span>
					</div>
				</div>
	            <div class="hr-line-dashed"></div>
	            <div class="row">
                    <div class="col-sm-3 pull-right">
                		<div class="form-group">
                        <button class="btn btn-primary" type="submit">提交文件</button>
                        <button class="btn btn-white" type="button" onclick="if(confirm('确定要关闭此窗口吗？')){window.close()}">取消</button>
                		</div>
                    </div>
                </div>
			</form>
		</div>
	</div>
	

	<script src="${context}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${context}/lib/jquery.form.min.js"></script>
	
	<script src="${context}/js/bootstrap.min.js?v=3.3.7"></script>
	<script src="${context}/js/plugins/prettyfile/bootstrap-prettyfile.js"></script>
	
	<script src="${context}/lib/zymvc-2.0.js"></script>
	<!-- 业务组件 -->
	<script src="${context}/servjs/${sys}/resources.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function () {
		$('input[type="file"]').prettyFile();
		//文件上传成功后
		var onFileUploadSuccess = function(data){
			if(data.success == true){
	        	//alert("文件上传成功："+data.data);
	        	processFile(data.data)
	    	} else{
	    		alert("文件上传失败："+data.msg);
	    	}
		}
		
		//提交处理文件请求
		var processFile = function(fid){
			$.ajax({
				url: _R['fileBatchProce'],
				data: {
					fileId: fid,
					component: _R.${table.modelName}['batchComponent'],
					batchType: _R.${table.modelName}['batchType'],
					submitParams: ''//填写自定义参数,格式：param1=test1&params2=test2
				},
				type: 'post',
				dataType: 'json',
				success: function(data){
					//alert(data.success+' '+data.msg);
					if(data.success == true){
						//提交任务成功，跳转至日志页面查看
			        	window.location = _R['fileTaskQueryPage']
			    	} else{
			    		alert("文件提交失败："+data.msg);
			    	}
				}
			})
		}
		//点击提交事件
		$('#fileBatchForm1').submit(function() {
			if(confirm('确定要提交文件处理吗？')==true){
				var options = {
		   	        success	: onFileUploadSuccess,
		   	        error: function(){
		   	        	alert('error')
		   	        },
		   	        url		: _R['uploadFile'], 
		   	        type	: 'post', 
		   	        dataType: 'json'
		   	    };
				
				$('#fileBatchForm1').mask('正在提交，请稍后...')
		        $(this).ajaxSubmit(options);
			}
	        return false;
		})
	})
	</script>
</body>

</html>