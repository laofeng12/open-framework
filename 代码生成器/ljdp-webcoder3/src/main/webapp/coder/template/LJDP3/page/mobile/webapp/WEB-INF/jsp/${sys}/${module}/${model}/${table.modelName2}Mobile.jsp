<#assign context = "$\{pageContext.request.contextPath}"/>
<#assign d = "$"/>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="ibox-content">
	<!-- 查询条件 -->
	<form method="get" class="form-horizontal" id="myQueryForm">
	<div class="row">
		
		<div id="myToolbar" class="toolbar col-xs-12 mb10">
			
		</div>
	</div>
	<!-- 分页表格 -->
	<table id="myTable"  class="listTableM">
		
	</table>
	</form>
</div>
<script src="${context}/servjs/${sys}/resources.js"></script>
<#if model == "">
<script src="${context}/servjs/${sys}/${module}/${table.modelName2}Mobile.js"></script>
<#else>
<script src="${context}/servjs/${sys}/${module}/${model}/${table.modelName2}Mobile.js"></script>
</#if>