package org.ljdp.plugin.batch.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ljdp.common.bean.FieldType;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.ExcelBuilder;
import org.ljdp.common.file.FileInfo;
import org.ljdp.core.service.GeneralService;
import org.ljdp.core.service.ServiceFactory;
import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.ljdp.plugin.batch.persistent.BtFileImportTaskDBParam;
import org.ljdp.ui.extjs.ExtUtils;
import org.ljdp.ui.extjs.TreeNode;
import org.ljdp.ui.struts2.ExtJsAction;
import org.ljdp.util.DateFormater;

public class FileImportTaskAction extends ExtJsAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4120943569995702011L;

	public FileImportTaskAction() {
		super();
		super.setEntity(BtFileImportTask.class);
		super.setPkNames(new String[] {"taskId"});
		super.setDateFormat("yyyy-MM-dd HH:mm:ss");
	}

//	@Override
//	protected void onBeforeList() {
//		BtFileImportTaskDBParam param = (BtFileImportTaskDBParam)getParam();
//		//设置时间
//		if(param.getLe_createDate() != null) {
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(param.getLe_createDate());
//			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
//			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
//			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
//			param.setLe_createDate(cal.getTime());
//		}
//	}
	
	/**
	 * 获取所有任务类型
	 */
	public void listAllTaskType() {
		try {
			List<TreeNode> results = new ArrayList<TreeNode>();
			GeneralService gs = ServiceFactory.buildGeneral();
			List<?> list = gs.doQuery("select distinct taskType from BtFileImportTask");
			for (int i = 0; i < list.size(); i++) {
				String tasktype = (String)list.get(i);
				TreeNode node = new TreeNode(tasktype, tasktype);
				results.add(node);
			}
			ExtUtils.writeJSONGrid(results, getResponse());
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
	
	public void exportTask() {
		try {
			//载入数据
			super.doListAll();
			
			//构建文件结构
			ExcelBuilder myBuilder = new ExcelBuilder();
			FileInfo excelInfo = new FileInfo();
			excelInfo.setSheetName("历史导入日志");
			myBuilder.setFileInfo(excelInfo);
			myBuilder.addProperty("taskName", "任务名");
			myBuilder.addProperty("taskType", "任务类型");
			myBuilder.addProperty("operatorName", "操作人");
			myBuilder.addProperty("createDate", "发起时间", FieldType.BASE_DATE, "yyyy-MM-dd HH:mm:ss");
			myBuilder.addProperty("totalRec", "总数据量");
			myBuilder.addProperty("succRec", "成功数");
			myBuilder.addProperty("failRec", "失败数");
			myBuilder.addProperty("procStateDesc", "状态");
			
			String fileName = "历史导入日志_"+DateFormater.formatDatetime_SHORT(new Date())+".xls";
			
			getResponse().reset();
			getResponse().setContentType(ContentType.EXCEL);
			getResponse().addHeader("Content-disposition", "attachment;filename="
					+ new String(fileName.getBytes("GBK"), "iso-8859-1"));
			myBuilder.build(getResponse().getOutputStream(), getDp().getDatas());
			
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}
}
