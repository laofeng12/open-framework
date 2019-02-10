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
							<#elseif item.userDict == true>
							<select class="form-control" name="${item.columnName}" value="${d}{m.${item.columnName}}"
						      	mvc_combox="${context}/api/framework/sys/code/list?codetype=${item.dictDefined}">
						      	<option value="">==请选择==</option>
						    </select>
	                    	<#else>
	                        <input type="text" class="form-control" name="${item.columnName}" value="${d}{m.${item.columnName}}">
	                        </#if>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
					</#list>
				</#list>
				<#if enableAttach==true>
				<div class="row">
					<div class="form-group">
	                    <label class="col-sm-2 control-label">附件：</label>
	                    <div class="col-sm-10">
	                        <a data-toggle="modal" href="#attachWin" >添加附件</a>
  							<div id="filelist"></div>
	                    </div>
	                </div>
	            </div>
	            <div class="hr-line-dashed"></div>
				</#if>
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

<#if enableAttach==true>
<!-- 附件上传窗口 -->
<div class="modal fade" id="attachWin" f-show="filelist" f-param-name="attachIds" tabindex="-1" role="dialog"  aria-hidden="true">
<form enctype="multipart/form-data">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" >上传文件</h4>
			</div>
			<div class="modal-body">
				<div class="row">
			       <div class="form-group">
			           <div class="col-sm-12">
			               <input type="file" class="form-control" name="file">
			           </div>
			       </div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="submit" class="btn btn-primary">上传</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
	<input type="hidden" name="fileBusiType" value="test">
	<!-- 文件服务器中存放的相对路径，一般建议用当前模块的业务编码命名  -->
	<input type="hidden" name="busiPath" value="tempupload">
</form>
</div>
</#if>

	<script src="${context}/js/jquery.min.js?v=2.1.4"></script>
	<script src="${context}/lib/jquery.form.min.js"></script>
	<script src="${context}/js/bootstrap.min.js?v=3.3.7"></script>
	<script src="${context}/js/plugins/bootstrap-table/bootstrap-table.min.js?v=1.11.0"></script>
	<script src="${context}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js?v=1.11.0"></script>
	<#if enableAttach==true>
	<script src="${context}/js/plugins/prettyfile/bootstrap-prettyfile.js"></script>
	</#if>
	<!-- 日期：laydate -->
	<script src="${context}/laydate/laydate.js"></script>
	
	
	<script type="text/javascript" src="${context}/lib/zymvc-2.0.min.js"></script>
	<!-- 业务组件 -->
	<script src="${context}/servjs/${sys}/resources.js?v=${d}{jver}"></script>
	
	<script type="text/javascript">
	$(document).ready(function () {
		mvc.ready(function(){
			<#list table.columnList as item>
			<#if item.userDict == true>
			$('[name=${item.columnName}]').val("${d}{m.${item.columnName}}")
			</#if>
			</#list>
		});
		<#if enableAttach==true>
		$('input[type="file"]').prettyFile();
		initMultiUploadModal('attachWin');//上传文件窗口初始化
		</#if>
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
	   	        url		: _R.${table.modelName}['save'],
	   	        type	: 'post', 
	   	        dataType: 'json'
	   	    });
			return false;
		})
	})
	</script>
</body>

</html>