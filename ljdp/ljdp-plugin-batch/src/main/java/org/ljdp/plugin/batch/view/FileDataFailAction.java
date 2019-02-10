package org.ljdp.plugin.batch.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.ExcelBuilder;
import org.ljdp.common.file.FileInfo;
import org.ljdp.component.result.Result;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.core.service.CommonService;
import org.ljdp.core.service.ServiceFactory;
import org.ljdp.plugin.batch.persistent.BtFileDataFail;
import org.ljdp.plugin.batch.persistent.BtFileDataFailDBParam;
import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.ljdp.ui.struts2.ExtJsAction;

public class FileDataFailAction extends ExtJsAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1108720720380238835L;

	public FileDataFailAction() {
		super();
		super.setEntity(BtFileDataFail.class);
		super.setPkNames(new String[] {"dataId"});
		super.setRestrictQueryQuantity(false);//不限制ListAll的数据量
	}
	
	public void exportExcel() {
		try {
			BtFileDataFailDBParam myParam = (BtFileDataFailDBParam)getParam();
			
			CommonService<BtFileImportTask> server = ServiceFactory.buildCommon(BtFileImportTask.class);
			BtFileImportTask task = server.doFindByPK(myParam.get_eq_taskId());
			String taskName = task.getTaskName();
			
			//载入数据
			super.doListAll();
			
			//把字符串拆为数组
			int maxColumn = 0;
			List<List<String>> failDataList = new ArrayList<List<String>>();
			List<String> failReasonList = new ArrayList<String>();
			Iterator<?> it = getDp().getDatas().iterator();
			while (it.hasNext()) {
				BtFileDataFail fail = (BtFileDataFail) it.next();
				String[] items = fail.getDataItem().split("\\|");
				if(items.length > maxColumn) {
					maxColumn = items.length;
				}
				List<String> failArray = new ArrayList<String>(maxColumn+3);
				for(String data : items) {
					failArray.add(data);
				}
				failDataList.add(failArray);
				failReasonList.add(fail.getFailReason());
			}
			for (int i = 0; i < failDataList.size(); i++) {
				List<String> failArray = failDataList.get(i);
				int difSize = maxColumn - failArray.size();
				if(difSize > 0) {
					for(int j = 1; j <= difSize; j++) {
						failArray.add("");
					}
				}
				failArray.add(failReasonList.get(i));
			}
			getDp().getDatas().clear();
			setDp(null);
			
			//构建文件结构
			ExcelBuilder myBuilder = new ExcelBuilder();
			FileInfo excelInfo = new FileInfo();
			excelInfo.setSheetName(taskName);
			myBuilder.setFileInfo(excelInfo);
			for (int i = 0; i < maxColumn; i++) {
				myBuilder.addProperty(null, "原文件数据"+(i+1));
			}
			myBuilder.addProperty(null, "失败原因");
			
			//导出
			if(failDataList.size() > 30000) {
				//大数据要生成文件，然后下载
				String fileSeparator = System.getProperty("file.separator");
				String location = ServletActionContext.getServletContext().getRealPath("/doc/temp");
				String filename = new TimeSequence().getSequence(getUser().getId()) + ".xls";
				String fullname = location + fileSeparator + filename;
				Result result = myBuilder.buildFile(fullname, failDataList);
				if(result.isSuccess()) {
					String url = getBasePath() + "doc/temp/" + filename;
					try {
						getResponse().sendRedirect(url);
					} catch (Exception e) {
					}
				}
			} else {
				//小数据量直接放内存
				getResponse().reset();
				getResponse().setContentType(ContentType.EXCEL);
				try {
					taskName = taskName.replaceFirst(".txt", "");
					taskName = taskName.replaceFirst(".xls", "");
					taskName = taskName.replaceFirst(".xlsx", "");
					taskName = taskName+"_失败记录.xls";
					getResponse().addHeader("Content-disposition", "attachment;filename="
							+ new String(taskName.getBytes("GBK"), "iso-8859-1"));
					myBuilder.build(getResponse().getOutputStream(), failDataList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
