package org.ljdp.plugin.batch.view.spring;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.result.Result;
import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.module.filetask.FileBatchTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LjdpBatchController extends AbstractBatchFileController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String batchsize;//一个事务的批处理数量
	private int beginIndex = 1;//默认第一行是标题，从第二行开始导入，index从0开始
	
	@Override
	protected FileBusinessObject getBusinessObject(String busiCompent) {
		FileBusinessObject bo = null;
		try {
			bo = (FileBusinessObject)Class.forName(busiCompent).newInstance();
		} catch (Exception e) {
			bo = (FileBusinessObject)SpringContextManager.getBean(
					busiCompent);
		}
		if(bo == null) {
			log.error("初始化前台业务对象失败:"+busiCompent);
		}
		return bo;
	}
	
	@Override
	protected FileBatchTask getBatchTask(String fsBusiCompent, String bsBusiCompent, String submitParams) {
		FileBatchTask fbt = super.getBatchTask(fsBusiCompent, bsBusiCompent, submitParams);
		fbt.setBeginIndex(beginIndex);
		
		ConfigFile corecfg = Env.current().getConfigFile();
		String isdeleteAfterProcess = corecfg.getValue("batch.fileupload.deleteAfterProcess");
		if(StringUtils.isNotBlank(isdeleteAfterProcess)) {
			boolean flag = Boolean.parseBoolean(isdeleteAfterProcess);
			if( !flag ) {
				fbt.setDeleteAfterProcess(false);
			} else {
				fbt.setDeleteAfterProcess(true);//处理完成后删除原文件
			}
		}
		
		if(StringUtils.isNotBlank(batchsize)) {
			fbt.setBatchSize(Integer.parseInt(batchsize));
			super.setAutoBatchSize(false);
		}
		return fbt;
	}
	
	/**
	 * 接收文件请求，提交文件至批处理任务
	 * @param compent  业务组件名称，如果对象继承BaseFileImportBO则使用前台导入，如果继承BaseDBTaskBO则使用后台导入
	 * @param submitParams 提交的web参数格式，例如：a=1&b=2&c=3
	 * @param fileFullName 文件存放的完整路径
	 * @param fileName		上传的文件名
	 * @param totalCount	文件行数
	 * @return
	 * @see org.ljdp.plugin.batch.bo.BaseDBTaskBO,org.ljdp.plugin.batch.bo.BaseFileImportBO
	 */
	public Result doBatchProcess(String compent, 
			String submitParams, 
			String fileFullName, String fileName, String batchType, int totalCount) {
		String fsBusiCompent, bsBusiCompent;
		Object com = SpringContextManager.getBean(compent);
		if(com.getClass().getSuperclass().getName()
				.equals("org.ljdp.plugin.batch.bo.BaseDBTaskBO")) {
			fsBusiCompent = "org.ljdp.plugin.batch.bo.BaseFileImportBO";
			bsBusiCompent = compent;
		} else {
			fsBusiCompent = compent;
			bsBusiCompent = null;
		}
		
		return doBatchProcess(fsBusiCompent, bsBusiCompent, submitParams, fileFullName, fileName, batchType, totalCount);
	}

	public String getBatchsize() {
		return batchsize;
	}

	public void setBatchsize(String batchsize) {
		this.batchsize = batchsize;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

}
