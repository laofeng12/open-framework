Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var corpNameField = new Ext.form.Hidden({
		fieldLabel: '企业编码',
		name: 'corpName',
		allowBlank: false
		,value:'openjava'
	});
	var sysNameField = new Ext.form.TextField({
		fieldLabel: '系统包编码',
		name: 'sysName',
		allowBlank: false
		,value:''
	});
	var moduleNameField = new Ext.form.TextField({
		fieldLabel: '一级模块包编码',
		name: 'moduleName',
		allowBlank: false
		//,value:'order'
	});
	var moduleName2Field = new Ext.form.TextField({
		fieldLabel: '二级模块包编码',
		name: 'model'
	});
	
	var tableNameField = new Ext.form.TextField({
		fieldLabel: '表名',
		name: 'tableName',
		allowBlank: false
		//,value:'ECS_BASEINFO_MOBILENOSEC'
	});
	var authorNameField = new Ext.form.TextField({
		fieldLabel: '作者',
		name: 'authorName',
		allowBlank: false
		//,value:'hzy'
	});
	var sysidField = new Ext.form.TextField({
		fieldLabel: '系统标识',
		name: 'sysId',
		allowBlank: false
		,value:'1'
		,hidden: true
	});
	var tableModelNameField = new Ext.form.TextField({
		fieldLabel: '表模型名称',
		name: 'tableModelName',
		allowBlank: false
	});
	var resNameField = new Ext.form.TextField({
		fieldLabel: '菜单名称',
		name: 'resName',
		allowBlank: false
		//,value:'hzy'
	});
	var dbNameField = new Ext.form.TextField({
		fieldLabel: '数据库名',
		name: 'dbName',
		allowBlank: false
		,value:'default'
	});
	
	var attachBox = new Ext.form.Checkbox({
		fieldLabel: '增加附件管理',
        boxLabel: '是',
        name	: 'enableAttach',
        listeners : {
        	check : function(me,checked){
        		if(checked === true){
        			attachClsField.show();
        		} else{
        			attachClsField.hide();
        		}
        	}
        }
	})
	var attachClsField = new Ext.form.TextField({
		fieldLabel: '附件持久化类',
		name: 'attachClass',
		hidden: true
	});
	
	var paramsCfg = new Ext.form.Hidden({
		name: 'paramsJSON'
	})
	
	var frameComboBox = new Ext.form.ComboBox({
	 	   fieldLabel: '开发框架',
		   name: 'frameType',
		   hiddenName: 'frameType',
		   store: new Ext.data.SimpleStore({
			   fields: ['value', 'text'],
			   data: [
				   ['LJDP5.2','LJDP5.2(code合并至HTTP header)'],
				   ['LJDP5.1','LJDP5.1(springboot2+antDesignPro(前后端分离))'],
				   ['LJDP5.0','LJDP5.0(springboot2+antDesignPro(前后端分离))'],
				   ['LJDP4.0','LJDP4.0(springboot1+antDesignPro(前后端分离))'],
				   ['LJDP3.0','LJDP3.0(spring+jsp+bootstrap(前后端分离))'],
			       ['LJDP2.0','LJDP2.0(Java7+JQuery)']
			   ]
		   }),
		   displayField:'text',
		   valueField	: 'value',
		   typeAhead: true,
		   mode: 'local',
		   triggerAction: 'all',
		   allowBlank: false,
		   editable : false,
		   value:'LJDP5.1'
		});
	
	var styleComboBox = new Ext.form.ComboBox({
	 	   fieldLabel: '页面风格',
		   name: 'styleType',
		   hiddenName: 'styleType',
		   store: new Ext.data.SimpleStore({
			   fields: ['value', 'text'],
			   data: [
			          ['','不生成页面'],
			          ['style1','风格一'],
			          ['style2','风格二（弹窗）']
			   ]
		   }),
		   displayField:'text',
		   valueField	: 'value',
		   typeAhead: true,
		   mode: 'local',
		   triggerAction: 'all',
		   allowBlank: false,
		   editable : false,
		   value:''
		});
	
	var form = new Ext.form.FormPanel({
		title: '代码生成器（增删改查）',
		region: 'north',
		frame:true,
		autoScroll:true,
		border: false,
		bodyBorder: false,
		labelWidth: 105,
		height: 210,
		labelAlign: 'right',
		buttonAlign:'center',
		items:[paramsCfg,{
			xtype : 'panel',
			layout: 'column',
			defaults	: {anchor: '100%'},
			items:[{
				columnWidth : .3,
				layout 		: 'form',
				border 		: false,
				defaultType	: 'textfield',
				defaults	: {anchor: '95%'},
				items	: [sysNameField,tableNameField,sysidField,tableModelNameField,attachBox]
			},{
				columnWidth : .3,
				layout 		: 'form',
				border 		: false,
				defaultType	: 'textfield',
				defaults	: {anchor: '95%'},
				items	: [moduleNameField,authorNameField,resNameField,attachClsField]
			},{
				columnWidth : .3,
				layout 		: 'form',
				border 		: false,
				defaultType	: 'textfield',
				defaults	: {anchor: '95%'},
				items: [moduleName2Field,frameComboBox, styleComboBox, dbNameField]
			}]
		},{
			xtype : 'panel',
			layout: 'form',
			defaults	: {anchor: '60%'},
			items: [corpNameField,{
	            xtype: 'checkboxgroup',
	            fieldLabel: '可选功能',
	            items: [
	                /*{boxLabel: 'MVC控制', name: 'mvc'},*/
	                {boxLabel: '后端API', name: 'mvc_api'},
	                {boxLabel: '前端MVC', name: 'mvc_web'},
	                {boxLabel: '查询', name: 'query'},
	                {boxLabel: '新增/修改', name: 'add'},
	                {boxLabel: '删除', name: 'delete'},
	                {boxLabel: '导入', name: 'importFun'},
	                {boxLabel: '导出', name: 'export'},
	                {boxLabel: '手机页面', name: 'mobilepage'}
	            ]
	        }]
		}],
		buttons:[{
			text: '第一步：解析表',
			handler: function(){
				store.load({params:{'tableName': tableNameField.getValue(),
								'dbName': dbNameField.getValue()}})
			}
		},{
			text: '第二步：生成代码',
			handler: function(){
				var records = store.getModifiedRecords();
				var list = [];
				for(var i=0; i<records.length; i++){
					var r = records[i]
					list.push(r.data);
				}
				paramsCfg.setValue(Ext.encode(list));
				
				ExtAjaxRequest({
					form: form,
					url: '/ljdp/coder/webcoder.action?method=doBuild',
					showSuccess: true,
					showFailure: true,
					complete: function(response,result){
						
					},
					failure: function(response,result){
						
					}
				})
			}
		}]
	})
	
	var store = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({
			url: _CONTEXT_PATH + '/ljdp/coder/webcoder.action?method=doListColumn',
			method: 'POST'
		}),
  		totalProperty	: 'totalProperty', 
        root			: 'root',
  		fields			: ['code','name','eq','ne','lt','gt','le','ge','like','nlike','in','nin','isnull'
  		      			   ,'dictDefined','sort'
  		      			   ]
	});
	store.on('exception',function(proxy, type, action, options,response){
		alert(response.responseText)
	})
	
	var cm = new Ext.grid.ColumnModel({
		columns:[
			{header: "字段", width: 150, sortable: false, dataIndex: 'code', align: 'center'},
			{header: "名称", width: 150, sortable: false, dataIndex: 'name', align: 'center'},
			{xtype: 'checkcolumn',header: "等于=", width: 100, dataIndex: 'eq'},
			{xtype: 'checkcolumn',header: "不等<>", width: 100, dataIndex: 'ne'},
			{xtype: 'checkcolumn',header: "大于等于>=", width: 100, dataIndex: 'ge'},
			{xtype: 'checkcolumn',header: "小于等于<=", width: 100, dataIndex: 'le'},
			{xtype: 'checkcolumn',header: "大于>", width: 100, dataIndex: 'gt',hidden:true},
			{xtype: 'checkcolumn',header: "小于<", width: 100, dataIndex: 'lt',hidden:true},
			{xtype: 'checkcolumn',header: "like", width: 100, dataIndex: 'like'},
			{xtype: 'checkcolumn',header: "not like", width: 100, dataIndex: 'nlike'},
			{xtype: 'checkcolumn',header: "in", width: 100, dataIndex: 'in'},
			{xtype: 'checkcolumn',header: "not in", width: 100, dataIndex: 'nin'},
			{xtype: 'checkcolumn',header: "值是否空", width: 100, dataIndex: 'isnull'},
			{xtype: 'checkcolumn',header: "是否需要排序", width: 120, dataIndex: 'sort'},
			{header: "数据字典", width: 110, sortable: false, dataIndex: 'dictDefined', align: 'left'
				,editor: new Ext.form.TextField()
			}
		]
	})
	
	var grid = new Ext.grid.EditorGridPanel({
		title:'查询条件设置',
		store: store,
	    cm: cm,
	    region: 'center',
	    loadMask : {msg:'正在读取表信息...'},
	    clicksToEdit: 1
	})
	
	new Ext.Viewport({
    	layout:'border',
    	border: false,
		bodyBorder: false,
    	items: [form,grid]
    })
	
})