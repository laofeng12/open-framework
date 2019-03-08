package com.openjava.admin.batch.api;

import java.net.InetAddress;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.plugin.batch.view.spring.DownloadController;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.admin.batch.domain.BatchFileimportTask;
import com.openjava.admin.batch.query.BatchFileimportTaskDBParam;
import com.openjava.admin.batch.service.BatchImportTaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;


/**
 * api接口
 * @author 何子右
 *
 */
@Api(tags="批任务管理器")
@RestController
@RequestMapping("/admin/batch/batchFileimportTask")
public class BatchFileimportTaskAction {
	
	@Resource
	private BatchImportTaskService batchImportTaskService;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public BatchFileimportTask get(@PathVariable("id")String id) {
		BatchFileimportTask m = batchImportTaskService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_taskName", value = "任务名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_taskType", value = "任务类型=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_operator", value = "操作人账号=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_operatorId", value = "操作人ID=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_operatorName", value = "操作人名称=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_procState", value = "处理状态（job.process.state）=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_createDate", value = "创建时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_createDate", value = "创建时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "eq_procWay", value = "处理方式=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<BatchFileimportTask> doSearch(BatchFileimportTaskDBParam params, Pageable pageable){
		if(StringUtils.isNotBlank(params.getLike_taskName())) {
			params.setLike_taskName("%"+params.getLike_taskName()+"%");
		}
		//只能查自己的
		params.setEq_operator(SsoContext.getAccount());
		//时间倒序显示
		Pageable mypage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				pageable.getSort().and(Sort.by(Sort.Order.desc("createDate"))));
		
		Page<BatchFileimportTask> result =  batchImportTaskService.query(params, mypage);
		
		return new TablePageImpl<>(result);
	}
	

	
	
	
	/**
	 * 导出Excel文件
	 */
	@Security(session=true)
	@RequestMapping(value="/export",method=RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			BatchFileimportTaskDBParam params) throws Exception{
		try {
			Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
			Page<BatchFileimportTask> result = batchImportTaskService.query(params, pageable);
			
			POIExcelBuilder myBuilder = new POIExcelBuilder(response.getOutputStream());
			//设置导出字段，以下是示例，请自行编写
//			myBuilder.addProperty("useraccount", "账号");
//			myBuilder.addProperty("username", "用户姓名");
//			myBuilder.addProperty("creatime", "创建时间", FieldType.BASE_DATE, "yyyy-MM-dd");//设置时间格式
//			myBuilder.addProperty("userStatus", "用户状态", SysCodeUtil.codeToMap("sys.user.status"));//自动数据字典【tsys_code】翻译
//			Map<K, V> tfMap1 = new HashMap();
//			tfMap1.put(1, "状态1");
//			tfMap1.put(2, "状态2");
//			myBuilder.addProperty("userStatus", "用户状态",tfMap1);//写死静态字典翻译
			
			myBuilder.buildSheet("批任务管理器", result.getContent());//放到第一个sheet
			
			String filename = "批任务管理器("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
			response.setContentType(ContentType.EXCEL);
			response.addHeader("Content-disposition", "attachment;filename="
					+ new String(filename.getBytes("GBK"), "iso-8859-1"));
			//开始导出
			myBuilder.finish();
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write(e.getMessage());
			} catch (Exception e2) {
			}
		}
	}
	
	@RequestMapping(value="/downloadErrorFile",method=RequestMethod.GET)
	@Security(session=true)
	public void downloadErrorFile(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("taskId")String taskId) {
		BatchFileimportTask task = batchImportTaskService.get(taskId);
		
		try {
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			if(StringUtils.isNotBlank(task.getServerIp()) && !hostAddress.equals(task.getServerIp())) {
				//转到此任务实际执行的服务器处理
				String serverUrl = "http://"+task.getServerIp()+":"+request.getLocalPort();
				String newDownUrl = serverUrl + "/api/admin/batch/batchFileimportTask/downloadErrorFile?taskId="+taskId;
				System.out.println("转发下载请求："+newDownUrl);
				LjdpHttpClient httpclient = new LjdpHttpClient();
				HttpResponse apiResp = httpclient.get(newDownUrl);
				HttpClientUtils.copyToServletResponse(response, apiResp);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		String[] errFileInfo = FileUtils.getNameAndExt(task.getTaskName());
//		String errorFileName = errFileInfo[0]+"_失败信息"+errFileInfo[1];
		String errorFileName = "【失败结果】"+task.getTaskName();
		
		DownloadController downloadCtl = new DownloadController();
		downloadCtl.downloadFile(request, response, 
				 //客户端显示的文件名
				errorFileName,
				task.getFailLog()	//文件在本地服务器存放路径
				);
	}
}
