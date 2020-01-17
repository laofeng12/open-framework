package org.ljdp.support.batch.controller;

import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.ljdp.common.bean.FieldType;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.ljdp.plugin.batch.persistent.BtFileImportTaskDBParam;
import org.ljdp.plugin.batch.view.spring.DownloadController;
import org.ljdp.secure.annotation.Security;
import org.ljdp.support.batch.service.BatchFileimportTaskService;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.ui.extjs.ExtUtils;
import org.ljdp.util.DateFormater;
import org.ljdp.util.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * 控制层
 * @author hzy
 *
 */
@Controller
@RequestMapping("/ljdp/filebatch")
public class BatchFileimportTaskController {
	
	@Resource
	private BatchFileimportTaskService batchFileimportTaskService;

	@RequestMapping(value="/search.act",method=RequestMethod.GET)
	@ResponseBody
	@Security(session=true)
	public TablePage<BtFileImportTask> doSearch(BtFileImportTaskDBParam params, Pageable pageable){
		if(params.getLe_createDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(params.getLe_createDate());
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
			params.setLe_createDate(cal.getTime());
		}
		Page<BtFileImportTask> result =  batchFileimportTaskService.query(params, pageable);
		
		return new TablePageImpl<>(result.getTotalElements(), result.getContent());
	}
	

	/**
	 * 导出Excel文件
	 */
	@RequestMapping(value="/export.act")
	@Security(session=true)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			BtFileImportTaskDBParam params) throws Exception{
		if(params.getLe_createDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(params.getLe_createDate());
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
			params.setLe_createDate(cal.getTime());
		}
		Pageable pageable = new PageRequest(0, 20000);//限制只能导出2w，防止内存溢出
		Page<BtFileImportTask> result =  batchFileimportTaskService.query(params, pageable);
		try {
			POIExcelBuilder myBuilder = new POIExcelBuilder(response.getOutputStream());
			//设置导出字段，以下是示例，请自行编写
			myBuilder.addProperty("taskName", "任务名");
			myBuilder.addProperty("taskType", "任务类型");
			myBuilder.addProperty("operatorName", "操作人");
			myBuilder.addProperty("createDate", "发起时间", FieldType.BASE_DATE, "yyyy-MM-dd HH:mm:ss");
			myBuilder.addProperty("totalRec", "总数据量");
			myBuilder.addProperty("succRec", "成功数");
			myBuilder.addProperty("failRec", "失败数");
			myBuilder.addProperty("procStateDesc", "状态");
			
			myBuilder.buildSheet("文件导入任务", result.getContent());//放到第一个sheet
			
			String filename = "文件导入日志("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
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
	
	
	@RequestMapping(value="/downloadErrorFile.act",method=RequestMethod.GET)
	@Security(session=true)
	public void downloadErrorFile(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("taskId")String taskId) {
		
		BtFileImportTask task = batchFileimportTaskService.get(taskId);
		if(task == null) {
			ExtUtils.writeHtmlFailure("任务不存在", response);
			return;
		}
		if(StringUtils.isEmpty(task.getFailLog())) {
			ExtUtils.writeHtmlFailure("没有失败记录", response);
			return;
		}
		try {
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			if(StringUtils.isNotBlank(task.getServerIP()) && !hostAddress.equals(task.getServerIP())) {
				//转到此任务实际执行的服务器处理
				String serverUrl = "http://"+task.getServerIP()+":8086";
				String newDownUrl = serverUrl + "/api/ljdp/filebatch/downloadErrorFile.act?taskId="+taskId;
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
	
	@RequestMapping(value="/downloadUploadFile.act",method=RequestMethod.GET)
	@Security(session=true)
	public void downloadUploadFile(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("taskId")String taskId) {
		
		BtFileImportTask task = batchFileimportTaskService.get(taskId);
		if(task == null) {
			ExtUtils.writeHtmlFailure("任务不存在", response);
			return;
		}
		if(StringUtils.isEmpty(task.getProceFileName())) {
			ExtUtils.writeHtmlFailure("没有保存原文件", response);
			return;
		}
		
		String fileName = "【原文件】"+task.getTaskName();
		
		DownloadController downloadCtl = new DownloadController();
		downloadCtl.downloadFile(request, response, 
				 //客户端显示的文件名
				fileName,
				task.getProceFileName()	//文件在本地服务器存放路径
				);
	}
}
