package org.ljdp.webcoder.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.ljdp.coder.GeneratorMojo;
import org.ljdp.coder.extjs.FieldType;
import org.ljdp.coder.model.TableQueryParamVO;
import org.ljdp.coder.pdm.Column;
import org.ljdp.coder.pdm.DataType;
import org.ljdp.coder.pdm.DbParam;
import org.ljdp.coder.pdm.Table;
import org.ljdp.coder.pdm.TableParser;
import org.ljdp.common.json.JSONTools;
import org.ljdp.core.query.RO;
import org.ljdp.ui.extjs.ExtUtils;
import org.ljdp.webcoder.vo.BaseFunctionVO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Scope("prototype")
@RequestMapping("/ljdp/coder/webcoder.action")
public class RbcCoderAction {

	/**
	 * 
	 * @param sysName 系统编码
	 * @param moduleName 模块编码
	 * @param tableName 表名
	 * @param authorName 作者
	 * @param sysId 系统id
	 * @param resName 菜单名称
	 * @param paramsJSON
	 */
	@RequestMapping(params="method=doBuild")
	public void doBuild(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("corpName")String corpName,
			@RequestParam("sysName")String sysName,
			@RequestParam("moduleName")String moduleName,
			@RequestParam(value="model", required=false)String model,
			@RequestParam("tableName")String tableName,
			@RequestParam("tableModelName")String tableModelName,
			@RequestParam("authorName")String authorName,
			@RequestParam("sysId")String sysId,
			@RequestParam("resName")String resName,
			@RequestParam("paramsJSON")String paramsJSON,
			@RequestParam(value="enableAttach", required=false)String enableAttach,
			@RequestParam(value="attachClass", required=false)String attachClass,
			@RequestParam("frameType")String frameType,
			@RequestParam("styleType")String styleType,
			@RequestParam("dbName")String dbName,
			BaseFunctionVO baseFunVO) {
		String coderPath = request.getSession().getServletContext().getRealPath("/coder");
		System.out.println("coderPath="+coderPath);
		System.out.println("corpName="+corpName);
		System.out.println("sysName="+sysName);
		System.out.println("moduleName="+moduleName);
		System.out.println("model="+model);
		System.out.println("tableName="+tableName);
		System.out.println("tableModelName="+tableModelName);
		System.out.println("authorName="+authorName);
		System.out.println("sysId="+sysId);
		System.out.println("resName="+resName);
		System.out.println("paramsJSON="+paramsJSON);
		System.out.println("enableAttach="+enableAttach);
		System.out.println("attachClass="+attachClass);
		System.out.println("frameType="+frameType);
		System.out.println("styleType="+styleType);
		System.out.println("BaseFunction="+baseFunVO.toStringShortPrefix());
		
		String templatePath = coderPath +"/template/bdc";
		String outputPath = coderPath +"/output";
		if(frameType.equals("LJDP2.0")) {
			templatePath = coderPath +"/template/LJDP2";
		} else if(frameType.equals("LJDP3.0")) {
			templatePath = coderPath +"/template/LJDP3";
		} else if(frameType.equals("LJDP4.0")) {
			templatePath = coderPath +"/template/LJDP4";
		} else if(frameType.equals("LJDP5.0")) {
			templatePath = coderPath +"/template/LJDP5";
		} else if(frameType.equals("LJDP5.1")) {
			templatePath = coderPath +"/template/LJDP5.1";
		} else if(frameType.equals("LJDP5.2")) {
			templatePath = coderPath +"/template/LJDP5.2";
		}
		String pageTemplatePath = null;
		String mobilePageTemplateDirectory = null;
		if(StringUtils.isNotEmpty(styleType)) {
			pageTemplatePath = templatePath +"/page/" +styleType;
			
			if(baseFunVO.getMobilepage().equals("on")) {
				mobilePageTemplateDirectory = templatePath +"/page/mobile";
			}
		}
		
		Map<String, String> dictMap = new HashMap<String, String>();//使用数据字典翻译的字段
		List<String> sortList = new ArrayList<>();
		//解析查询参数
		List<DbParam> paramList = new ArrayList<DbParam>();
		Collection fieldParamList = JSONTools.toJavaObjectCollection(paramsJSON, TableQueryParamVO.class);
		Iterator it = fieldParamList.iterator();
		while (it.hasNext()) {
			TableQueryParamVO vo = (TableQueryParamVO) it.next();
			System.out.println(vo.toStringShortPrefix());
			
			if(vo.getEq() != null && vo.getEq().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.EQ.replaceAll("_", ""));
				p.setSymbol("=");
				paramList.add(p);
				
				//设置数据字典
				if(StringUtils.isNotBlank(vo.getDictDefined())) {
					p.setExtJsFieldType(FieldType.DICTCOMBO);
					p.setDictDefined(vo.getDictDefined());
					
					dictMap.put(vo.getCode(), vo.getDictDefined());
				}
			}
			if(vo.getNe() != null && vo.getNe().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.NE.replaceAll("_", ""));
				p.setSymbol("<>");
				paramList.add(p);

				//设置数据字典
				if(StringUtils.isNotBlank(vo.getDictDefined())) {
					p.setExtJsFieldType(FieldType.DICTCOMBO);
					p.setDictDefined(vo.getDictDefined());
					
					dictMap.put(vo.getCode(), vo.getDictDefined());
				}
			}
			if(vo.getLt() != null && vo.getLt().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.LT.replaceAll("_", ""));
				p.setSymbol("<");
				paramList.add(p);
			}
			if(vo.getGt() != null && vo.getGt().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.GT.replaceAll("_", ""));
				p.setSymbol(">");
				paramList.add(p);
			}
			if(vo.getLe() != null && vo.getLe().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.LE.replaceAll("_", ""));
				p.setSymbol("<=");
				paramList.add(p);
			}
			if(vo.getGe() != null && vo.getGe().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.GE.replaceAll("_", ""));
				p.setSymbol(">=");
				paramList.add(p);
			}
			if(vo.getLike() != null && vo.getLike().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.LIKE.replaceAll("_", ""));
				p.setSymbol("like");
				p.setJavaDataType(DataType.STRING);
				p.setExtJsFieldType(FieldType.TEXT);
				paramList.add(p);
			}
			if(vo.getNlike() != null && vo.getNlike().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.NLIKE.replaceAll("_", ""));
				p.setSymbol("not like");
				p.setJavaDataType(DataType.STRING);
				p.setExtJsFieldType(FieldType.TEXT);
				paramList.add(p);
			}
			if(vo.getIn() != null && vo.getIn().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.IN.replaceAll("_", ""));
				p.setSymbol("in");
				paramList.add(p);
			}
			if(vo.getNin() != null && vo.getNin().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.NIN.replaceAll("_", ""));
				p.setSymbol("not in");
				paramList.add(p);
			}
			if(vo.getIsnull() != null && vo.getIsnull().booleanValue()) {
				DbParam p = new DbParam();
				p.setCode(vo.getCode());
				p.setName(vo.getName());
				p.setCondition(RO.NULL.replaceAll("_", ""));
				p.setSymbol("is (not) null");
				p.setJavaDataType("Boolean");
				p.setExtJsFieldType(FieldType.COMBO);
				paramList.add(p);
			}
			if(vo.getSort() != null && vo.getSort().booleanValue()) {
				sortList.add(vo.getCode());
			}
		}
		for (int i = 0; i < paramList.size(); i++) {
			DbParam p = (DbParam) paramList.get(i);
			System.out.println(p.toStringShortPrefix());
		}
		
		GeneratorMojo m = new GeneratorMojo();
		m.templateDirectory	= templatePath;
		m.pageTemplateDirectory = pageTemplatePath;
		m.mobilePageTemplateDirectory = mobilePageTemplateDirectory;
		m.outputDirectory = outputPath;
		m.corpName = corpName;
		m.sysName = sysName;
		m.moduleName = moduleName;
		m.model = model;
		m.tableName = tableName;
		m.tableModelName = tableModelName;
		m.authorName = authorName;
		m.sysId = sysId;
		m.resourceName = resName;
		m.dbParamList = paramList;
		m.dictMap = dictMap;
		m.sortList = sortList;
		m.baseFunVO = baseFunVO;
		m.frameType = frameType;
		if(enableAttach != null && enableAttach.equals("on")) {
			m.enableAttach = true;
			m.attachClass = attachClass;
		}
		
		//对查询条件按4个一行进行拆分
		List<List<DbParam>> rowParamList = new ArrayList<List<DbParam>>();
		for (int i = 0; i < paramList.size(); ) {
			List<DbParam> rowList = new ArrayList<DbParam>();
			for(int j=0; i < paramList.size() && j < 4; i++, j++) {
				rowList.add(paramList.get(i));
			}
			if(rowList.size() > 0) {
				rowParamList.add(rowList);
			}
		}
		m.rowDbParamList = rowParamList;
		
		try {
			m.execute(dbName);
			ExtUtils.writeSuccess(response);
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeFailure(e.getMessage(), response);
		}
		
	}
	
	@RequestMapping(params="method=doListColumn")
	public void doListColumn(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("tableName")String tableName,
			@RequestParam("dbName")String dbName) {
		System.out.println("tableName="+tableName);
		try {
			Table table = TableParser.parse(dbName, tableName, null, null, 3);
			List<TableQueryParamVO> paramList = new ArrayList<TableQueryParamVO>();
			List<Column> columnList = table.getColumnList();
			for (int i = 0; i < columnList.size(); i++) {
				Column c = (Column) columnList.get(i);
				if(!c.isIskey()) {
					TableQueryParamVO p = new TableQueryParamVO();
					p.setCode(c.getCode());
					p.setName(c.getComment());
					
					paramList.add(p);
				}
			}
			ExtUtils.writeJSONGrid(paramList, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
