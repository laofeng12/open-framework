package org.ljdp.coder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.ljdp.coder.pdm.Column;
import org.ljdp.coder.pdm.DBInfoUtils;
import org.ljdp.coder.pdm.DbParam;
import org.ljdp.coder.pdm.Table;
import org.ljdp.coder.pdm.TableParser;
import org.ljdp.util.RandomCode;
import org.ljdp.webcoder.vo.BaseFunctionVO;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class GeneratorMojo {
	
	private final String PLACEHOLDER_CORP = "${corp}";//企业代号
	private final String PLACEHOLDER_SYS = "${sys}";//系统编码
	private final String PLACEHOLDER_MODULE = "${module}";//一级模块名称
	private final String PLACEHOLDER_MODEL = "${model}";//二级模块
	private final String PLACEHOLDER_RANDOMCODE = "${randomCode}";//随机码
	private final String PLACEHOLDER_TABLEMODELNAME = "${table.modelName}";
	private final String PLACEHOLDER_TABLEMODELNAME2 = "${table.modelName2}";
	private final String ENCODING_UTF8 = "UTF-8";
	
	public String templateDirectory;
	public String pageTemplateDirectory;
	public String mobilePageTemplateDirectory;
	public String outputDirectory;
	public String corpName;
	public String sysName;
	public String moduleName;
	public String model;
	public String tableName;
	public String tableModelName;
	public String authorName;
	public List<DbParam> dbParamList;//查询条件
	public List<List<DbParam>> rowDbParamList;//查询条件，按照每4个一行存放
	public Boolean enableAttach = false;//增加附件管理
	public String attachClass;//附件持久化类
	private Boolean buildPageJsp = false;//是否生成页面
	public String resourceName;//菜单名称
	public String sysId;//系统标识
	public Map<String, String> dictMap;//数据字典定义
	public List<String> sortList;//排序字段(表字段)
	public List<String> sortFieldList = new ArrayList<>();//排序字段(实体类映射的字段名称)
	public String frameType;//框架类型
	public BaseFunctionVO baseFunVO;
	
	private String codeType;
	
	private File outputDirectoryFile;
	public int randomCode;
	
	private Table tableMeta;
	
	public void execute(String dbName) throws Exception{
		int rowColNum = 3;//编辑页面，每行显示属性列数
		if(frameType != null) {
			if(frameType.equals("LJDP4.0")) {
				rowColNum = 2;
			}
		}
		randomCode = RandomCode.getNumberOf4();
		tableMeta = TableParser.parse(dbName, tableName, null, dictMap, rowColNum);
//		if(StringUtils.isBlank(model)) {
//			model = tableMeta.getModelName2();
//		}
		if(dbParamList != null && dbParamList.size() > 0) {
			//设置查询参数的数据类型
			List<Column> columnList = tableMeta.getColumnList();
			for (int i = 0; i < dbParamList.size(); i++) {
				DbParam p = (DbParam) dbParamList.get(i);
				for (int j = 0; j < columnList.size(); j++) {
					Column c = (Column) columnList.get(j);
					if(p.getCode().equals(c.getCode())) {
						if(p.getJavaDataType() == null) {
							p.setJavaDataType(c.getJavaDataType());
						}
						if(p.getExtJsFieldType() == null) {
							p.setExtJsFieldType(c.getExtJsFieldType());
						}
						p.setColumnName(c.getColumnName());
						break;
					}
				}
			}
		}
		//设置排序字段
		List<Column> columnList = tableMeta.getColumnList();
		for (int i = 0; i < columnList.size(); i++) {
			Column column = (Column) columnList.get(i);
			for (int j = 0; j < sortList.size(); j++) {
				String sortfield = (String) sortList.get(j);
				if(column.getCode().equals(sortfield)) {
					column.setSort(true);
					sortFieldList.add(column.getColumnName());
				}
			}
		}
		
		String importJavaTplDir = null;//导入代码模板目录
		String mvcJavaTplDir = null;//spring Mvc controller代码
		String mvcBackTplDir = null;
		String mvcFrontTplDir = null;
		String queryTplDir = null;//查询条件java模板代码
		if(tableMeta.getKeyMap().size() <= 1) {
			templateDirectory = templateDirectory+"/singlePK";
			if(baseFunVO.getImportFun().equals("on")) {
				importJavaTplDir = templateDirectory+"_import";
			}
			if(baseFunVO.getMvc().equals("on")) {
				mvcJavaTplDir = templateDirectory+"_mvc";
			}
			if(baseFunVO.getQuery().equals("on")) {
				queryTplDir = templateDirectory+"_query";
			}
			if(baseFunVO.getMvc_api().equals("on")) {
				mvcBackTplDir = templateDirectory+"_mvc_back";
			}
			if(baseFunVO.getMvc_web().equals("on")) {
				mvcFrontTplDir = templateDirectory+"_mvc_front";
			}
		} else {
			templateDirectory = templateDirectory+"/multiPK";
		}
		
		outputDirectoryFile = new File(outputDirectory);
		if(!outputDirectoryFile.exists()){
			outputDirectoryFile.mkdirs();
		}else if(!outputDirectoryFile.isDirectory()){
			throw new Exception("ERROR:outputDirectory:" + outputDirectory + " 不是文件夹");
		}
		if(pageTemplateDirectory != null) {
			buildPageJsp = true;
		}

		//java代码
		File templateDirectoryFile = new File(templateDirectory);
		if(!templateDirectoryFile.exists()) {
			throw new Exception("ERROR:templateDirectory:" + templateDirectoryFile + " 目录不存在");
		} else if(!templateDirectoryFile.isDirectory()){
			throw new Exception("ERROR:templateDirectory:" + templateDirectoryFile + " 不是文件夹");
		}
		codeType = "java";
		excuteTemplate(templateDirectoryFile);
		
		//导入java代码生成
		if(importJavaTplDir != null) {
			File importJavaTplFile = new File(importJavaTplDir);
			codeType = "java";
			excuteTemplate(importJavaTplFile);
		}
		//spring mvc controller代码生成
		if(mvcJavaTplDir != null) {
			File mvcJavaTplFile = new File(mvcJavaTplDir);
			codeType = "java";
			excuteTemplate(mvcJavaTplFile);
		}
		if(mvcBackTplDir != null) {
			codeType = "java";
			excuteTemplate(new File(mvcBackTplDir));
		}
		if(mvcFrontTplDir != null) {
			codeType = null;
			excuteTemplate(new File(mvcFrontTplDir));
		}
		//生成查询条件的java对象
		if(queryTplDir != null) {
			File queryTplDirFile = new File(queryTplDir);
			codeType = "java";
			excuteTemplate(queryTplDirFile);
		}
		
		//页面
		if(pageTemplateDirectory != null) {
			File pageTemplateDirectoryFile = new File(pageTemplateDirectory);
			if(!pageTemplateDirectoryFile.exists()){
				throw new Exception("ERROR:pagetemplateDirectory:" + pageTemplateDirectoryFile + " 目录不存在");
			}else if(!pageTemplateDirectoryFile.isDirectory()){
				throw new Exception("ERROR:pagetemplateDirectory:" + pageTemplateDirectoryFile + " 不是文件夹");
			}
			codeType = null;
			excuteTemplate(pageTemplateDirectoryFile);
		}
		
		if(mobilePageTemplateDirectory != null) {
			File mobilePageFile = new File(mobilePageTemplateDirectory);
			if(!mobilePageFile.exists()){
				throw new Exception("ERROR:mobilePageTemplateDirectory:" + mobilePageFile + " 目录不存在");
			}else if(!mobilePageFile.isDirectory()){
				throw new Exception("ERROR:mobilePageTemplateDirectory:" + mobilePageFile + " 不是文件夹");
			}
			excuteTemplate(mobilePageFile);
		}
	}

	protected void excuteTemplate(File templateFile) throws IOException, TemplateException, Exception {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		configuration.setTemplateLoader(new FileTemplateLoader(templateFile));
		configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_22));
		Properties p = new Properties();
		p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("freemarker.properties"));		
		configuration.setSettings(p);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		recursionFile(configuration, templateFile, templateFile);
	}
	
	private void recursionFile(Configuration configuration, File currentFile, File templateDir) throws Exception{
		if(currentFile.isDirectory()){
			File []childFiles = currentFile.listFiles();
			for (int i = 0; i < childFiles.length; i++) {
				recursionFile(configuration, childFiles[i], templateDir);
			}	
		} else {
			String relativePath = currentFile.getCanonicalPath().substring(templateDir.getCanonicalPath().length());
			String relativePath0 = relativePath;
			if(codeType != null) {
				if(codeType.equals("java")) {
					//把多个java目录代码合并
					int secondpath = relativePath.indexOf("/", 1);
					if(secondpath == -1) {
						secondpath = relativePath.indexOf("\\", 1);
					}
					relativePath0 = relativePath.substring(secondpath, relativePath.length());
				}
			}
			
			String relativePath1 = relativePath0.replace(PLACEHOLDER_CORP, corpName);
			relativePath1 = relativePath1.replace(PLACEHOLDER_SYS, sysName);
			relativePath1 = relativePath1.replace(PLACEHOLDER_MODULE, moduleName);
			//二级模块包名可以空
			if(StringUtils.isBlank(model)) {
				relativePath1 = relativePath1.replace(PLACEHOLDER_MODEL+"\\", "");
				model = "";
			} else {
				relativePath1 = relativePath1.replace(PLACEHOLDER_MODEL, model);
			}
			
			relativePath1 = relativePath1.replace(PLACEHOLDER_RANDOMCODE, randomCode+"");
			relativePath1 = relativePath1.replace(PLACEHOLDER_TABLEMODELNAME, tableMeta.getModelName());
			relativePath1 = relativePath1.replace(PLACEHOLDER_TABLEMODELNAME2, tableMeta.getModelName2());
			File outputFile = new File(outputDirectoryFile.getCanonicalPath() +relativePath1);
			
			if(!outputFile.getParentFile().exists()){
				outputFile.getParentFile().mkdirs();
			}
			
			System.out.println("来源："+currentFile.getPath());
			System.out.println("输出："+outputFile.getPath());
			
			//获取菜单ID
			Long resId = getResId();
			
			Map root = new HashMap();
			root.put("corp", corpName);
			root.put("sys", sysName);
			root.put("module", moduleName);
			root.put("model", model);
			root.put("tableModelName", tableModelName);
			root.put("table", tableMeta);
			root.put("author", authorName);
			root.put("dbParamList", dbParamList);
			root.put("resId", resId);
			root.put("resName", resourceName);
			root.put("sysId", sysId);
			root.put("enableAttach", enableAttach);
			root.put("attachClass", attachClass);
			root.put("rowDbParamList", rowDbParamList);
			root.put("sysResId", sysId+resId.toString());
			root.put("sortFieldList", sortFieldList);
			root.put("baseFun", baseFunVO);
			root.put("buildPageJsp", buildPageJsp);
			if(dictMap.size() > 0) {
				root.put("userDict", true);
			} else {
				root.put("userDict", false);
			}
			
			root.put("randomCode", randomCode);
			
//			System.out.println("randomCode="+randomCode);
			
			Template template = configuration.getTemplate(relativePath,ENCODING_UTF8);
			
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),ENCODING_UTF8));
			template.process(root, out);
			out.flush();
			out.close();
		}
		
	}

	private void test() {
		try {
			Table table = TableParser.parse("default",tableName,null, null, 3);
			System.out.println(table);
			System.out.println(table.getCode());
			System.out.println(table.getModelName());
			System.out.println(table.getModelName2());
			List<Column> clist = table.getColumnList();
			for (int j = 0; j < clist.size(); j++) {
				Column c = (Column) clist.get(j);
				System.out.println(c.getJavaDataType()+" "+c.getColumnName()+" "+c.getName()+" "+c.getComment());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用时间生成一个资源ID
	 * @return
	 */
	public static long getResId() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date begind = sdf.parse("2016-04-01");
			
			long dif = System.currentTimeMillis() - begind.getTime();
			return dif / 1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeneratorMojo m = new GeneratorMojo();
		m.templateDirectory	= "C:/SVN/code/LJDP2/ljdp-webcoder/target/ljdp-webcoder/coder/template/LJDP2";
		m.outputDirectory = "C:/SVN/code/LJDP2/ljdp-webcoder/target/ljdp-webcoder/coder/output";
		m.sysName = "dzqd";
		m.moduleName = "order";
		m.model = "info";
		m.tableName = "yyd_borrow";
		m.authorName = "作者";
		m.dbParamList = new ArrayList<DbParam>();
		m.sysId = "115";
		m.resourceName = "测试自动生成菜单sql";
		Map<String, String> dictMap = new HashMap<String, String>();
		m.dictMap = dictMap;
		List<String> sortList = new ArrayList<>();
		m.sortList = sortList;
		m.baseFunVO = new BaseFunctionVO();
		
		try {
//			m.test();
			m.execute("mysql");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
