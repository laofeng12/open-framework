package org.ljdp.ui.extjs;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.ljdp.common.json.JSONTools;
import org.ljdp.core.db.DataPackage;

public class ExtUtils {
	/**
	 * 返回数据列表至客户端的Grid控件。
	 * 客户端Store编写如下：
	 * var store = new Ext.data.JsonStore({
			proxy : new Ext.data.HttpProxy({url: ''}),
	  		totalProperty: 'totalProperty', 
	        root: 'root',
	  		fields: []
		});
	 */
	@SuppressWarnings("rawtypes")
	public static void writeJSONGrid(List list, String[] excludes, HttpServletResponse response, String datePattern) throws Exception {
		ExtStore store = new ExtStore();
		store.setTotalProperty(list.size());
		store.setRoot(list);
		JSONTools.writePage(store, excludes, response, datePattern);
	}
	
	public static void writeJSONGrid(List list, String[] excludes, HttpServletResponse response) throws Exception {
		writeJSONGrid(list, excludes, response, null);
	}
	
	/**
	 * 返回数据列表至客户端的Grid控件。
	 * 客户端Store编写如下：
	 * var store = new Ext.data.JsonStore({
			proxy : new Ext.data.HttpProxy({url: ''}),
	  		totalProperty: 'totalProperty', 
	        root: 'root',
	  		fields: []
		});
	 */
	@SuppressWarnings("rawtypes")
	public static void writeJSONGrid(List list, HttpServletResponse response) throws Exception {
		writeJSONGrid(list, null, response, null);
	}
	
	/**
	 * 返回数据列表至客户端的Grid控件。
	 * 客户端Store编写如下：
	 * var store = new Ext.data.JsonStore({
			proxy : new Ext.data.HttpProxy({url: ''}),
	  		totalProperty: 'totalProperty', 
	        root: 'root',
	  		fields: []
		});
	 */
	@SuppressWarnings("rawtypes")
	public static void writeJSONGrid(DataPackage dp, String[] excludes, 
			HttpServletResponse response, String datePattern) {
		ExtStore store = new ExtStore();
		store.setTotalProperty(dp.getTotalCount());
		store.setRoot(dp.getDatas());
		JSONTools.writePage(store, excludes, response, datePattern);
	}

	/**
	 * 返回数据列表至客户端的Grid控件。
	 * 客户端Store编写如下：
	 * var store = new Ext.data.JsonStore({
			proxy : new Ext.data.HttpProxy({url: ''}),
	  		totalProperty: 'totalProperty', 
	        root: 'root',
	  		fields: []
		});
	 */
	@SuppressWarnings("rawtypes")
	public static void writeJSONGrid(DataPackage dp,
			HttpServletResponse response, String datePattern) {
		writeJSONGrid(dp, null, response, datePattern);
	}

	/**
	 * 返回数据列表至客户端的Grid控件。
	 * 客户端Store编写如下：
	 * var store = new Ext.data.JsonStore({
			proxy : new Ext.data.HttpProxy({url: ''}),
	  		totalProperty: 'totalProperty', 
	        root: 'root',
	  		fields: []
		});
	 */
	@SuppressWarnings("rawtypes")
	public static void writeJSONGrid(DataPackage dp, HttpServletResponse response) {
		writeJSONGrid(dp, response, null);
	}
	
	/**
	 * 返回数据列表至客户端的Grid控件。
	 * 客户端Store编写如下：
	 * var store = new Ext.data.JsonStore({
			proxy : new Ext.data.HttpProxy({url: ''}),
	  		totalProperty: 'totalProperty', 
	        root: 'root',
	  		fields: []
		});
	 */
	@SuppressWarnings("rawtypes")
	public static void writeJSONGrid(DataPackage dp, String[] excludes, HttpServletResponse response) {
		writeJSONGrid(dp, excludes, response, null);
	}

	@SuppressWarnings("rawtypes")
	public static Tree buildTreeBySortID(List list, Map<String, String> proMapper) {
		Tree tree = newTree(proMapper);
		tree.buildTreeBySortID(list, proMapper);
		return tree;
	}
	
	@SuppressWarnings("rawtypes")
	public static Tree buildTreeOneLv(List list, Map<String, String> proMapper) {
		Tree tree = newTree(proMapper);
		tree.buildTreeOneLv(list, proMapper);
		return tree;
	}

	private static Tree newTree(Map<String, String> proMapper) {
		String id = "000";
		if(proMapper.containsKey("root.id")) {
			id = proMapper.get("root.id");
		}
		String text = "root";
		if(proMapper.containsKey("root.text")) {
			text = proMapper.get("root.text");
		}
		Tree tree = new Tree(id, text);
		return tree;
	}
	
	@SuppressWarnings("rawtypes")
	public static Tree buildTreeBySort(List list, Map<String, String> proMapper) {
		Tree tree = newTree(proMapper);
		tree.buildTreeBySort(list, proMapper);
		return tree;
	}

	@SuppressWarnings("rawtypes")
	public static void writeJSONTreeBySortID(List list, Map<String, String> proMapper,
			 HttpServletResponse response) {
		Tree tree = buildTreeBySortID(list, proMapper);
		writeJSONTree(tree, response);
	}
	
	@SuppressWarnings("rawtypes")
	public static void writeJSONTree(List list, Map<String, String> proMapper,
			 HttpServletResponse response) {
		Tree tree = buildTreeBySort(list, proMapper);
		writeJSONTree(tree, response);
	}
	
	@SuppressWarnings("rawtypes")
	public static void writeJSONTreeOneLv(List list, Map<String, String> proMapper,
			 HttpServletResponse response) {
		Tree tree = buildTreeOneLv(list, proMapper);
		writeJSONTree(tree, response);
	}

	public static void writeJSONTree(Tree tree, HttpServletResponse response) {
		JSONTools.writePage(tree.getRoot().getChildren(), response);
	}
	
	/**
	 * 返回成功状态至客户端
	 * Ext.Ajax.request({
	 * 		success: function(result){
	 * 			▶客户端将会调用在此回调方法
	 * 			var json = Ext.util.JSON.decode(result.responseText);
	 * 			alert(json.success == true);
	 * 		}
	 * });
	 * @param msg
	 * @param response
	 */
	public static void writeSuccess(String msg, HttpServletResponse response) {
		FormResult res = new FormResult();
		res.setSuccess(true);
		res.setMsg(msg);
		JSONTools.writePage(res, response);
	}
	
	/**
	 * 返回成功状态至客户端
	 * Ext.Ajax.request({
	 * 		success: function(result){
	 * 			▶客户端将会调用在此回调方法
	 * 			var json = Ext.util.JSON.decode(result.responseText);
	 * 			alert(json.success == true);
	 * 		}
	 * });
	 * @param response
	 */
	public static void writeSuccess(HttpServletResponse response) {
		writeSuccess("ok", response);
	}
	
	/**
	 * 返回成功状态至客户端
	 * Ext.Ajax.request({
	 * 		success: function(result){
	 * 			▶客户端将会调用在此回调方法
	 * 			var json = Ext.util.JSON.decode(result.responseText);
	 * 			alert(json.success == true);
	 * 		}
	 * });
	 * @param data 自定义的数据结构
	 * @param response
	 */
	public static void writeSuccess(Object data, HttpServletResponse response) {
		FormResult res = new FormResult();
		res.setSuccess(true);
		res.setData(data);
		JSONTools.writePage(res, response);
	}
	
	public static void writeSuccess(Object data, String[] excludes, HttpServletResponse response) {
		FormResult res = new FormResult();
		res.setSuccess(true);
		res.setData(data);
		JSONTools.writePage(res, excludes, response);
	}
	
	/**
	 * 返回成功状态至客户端
	 * Ext.Ajax.request({
	 * 		success: function(result){
	 * 			▶客户端将会调用在此回调方法
	 * 			var json = Ext.util.JSON.decode(result.responseText);
	 * 			alert(json.success == true);
	 * 		}
	 * });
	 * @param data 自定义的数据结构
	 * @param response
	 * @param datePattern 日期格式
	 */
	public static void writeSuccess(Object data, HttpServletResponse response, String datePattern) {
		FormResult res = new FormResult();
		res.setSuccess(true);
		res.setData(data);
		JSONTools.writePage(res, response, datePattern);
	}
	
	public static void writeSuccess(Object data, String[] excludes, HttpServletResponse response, String datePattern) {
		FormResult res = new FormResult();
		res.setSuccess(true);
		res.setData(data);
		JSONTools.writePage(res, excludes, response, datePattern);
	}
	
	/**
	 * 返回失败状态至客户端
	 * Ext.Ajax.request({
	 * 		failure: function(result){
	 * 			▶客户端将会调用在此回调方法
	 * 			var json = Ext.util.JSON.decode(result.responseText);
	 * 			alert(json.success == false);
	 * 		}
	 * });
	 * @param response
	 */
	public static void writeFailure(String msg, HttpServletResponse response) {
		FormResult res = new FormResult();
		res.setSuccess(false);
		res.setMsg(msg);
		response.setStatus(500);
		JSONTools.writePage(res, response);
	}
	
	/**
	 * 业务办理失败，返回成功状态至客户端
	 * Ext.Ajax.request({
	 * 		success: function(result){
	 * 			▶客户端将会调用在此回调方法
	 * 			var json = Ext.util.JSON.decode(result.responseText);
	 * 			alert(json.success == false);
	 * 		}
	 * });
	 * @param response
	 */
	public static void writeBusiFail(String msg, HttpServletResponse response) {
		FormResult res = new FormResult();
		res.setSuccess(false);
		res.setMsg(msg);
		JSONTools.writePage(res, response);
	}

	/**
	 * 返回提交成功状态至客户端
	 * form.getForm().submit({
			url: submitURL,
			success : function(form, action){
				▶将返回到这个回调方法中
				var json = Ext.util.JSON.decode(action.response.responseText);
	 * 			alert(json.success == true);
			},
			failure : function(form, action){
			}	
	   })
	 * @param response
	 */
	public static void writeHtmlSuccess(HttpServletResponse response) {
		FormResult res = new FormResult();
		res.setSuccess(true);
		res.setMsg("ok");
		JSONTools.writeHTML(res, response);
	}
	
	/**
	 * 返回提交成功状态至客户端
	 * form.getForm().submit({
			url: submitURL,
			success : function(form, action){
				▶将返回到这个回调方法中
				var json = Ext.util.JSON.decode(action.response.responseText);
	 * 			alert(json.success == true);
			},
			failure : function(form, action){
			}	
	   })
	 * @param response
	 */
	public static void writeHtmlSuccess(String msg, HttpServletResponse response) {
		FormResult res = new FormResult();
		res.setSuccess(true);
		res.setMsg(msg);
		
		JSONTools.writeHTML(res, response);
	}

	/**
	 * 返回提交成功状态至客户端
	 * form.getForm().submit({
			url: submitURL,
			success : function(form, action){
			},
			failure : function(form, action){
				▶将返回到这个回调方法中
				var json = Ext.util.JSON.decode(action.response.responseText);
	 * 			alert(json.success == false);
			}
	   })
	 * @param response
	 */
	public static void writeHtmlFailure(String msg, HttpServletResponse response) {
		FormResult res = new FormResult();
		res.setSuccess(false);
		res.setMsg(msg);
		JSONTools.writeHTML(res, response);
	}
}
