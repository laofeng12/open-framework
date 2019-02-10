package org.ljdp.plugin.batch.view;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.module.filetask.FileBatchTask;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.task.LJDPFileBatchTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LjdpBatchAction extends AbstractBatchFileAction {
	private static final long serialVersionUID = -2286421291193553658L;
	private Logger log = LoggerFactory.getLogger(LjdpBatchAction.class);
	private String bo;//前台业务对象
	private String bsBO;//后台业务对象
	private String batchsize;

	@Override
	protected FileBusinessObject getBusinessObject() {
		FileBusinessObject bObj = null;
		try {
			bObj = (FileBusinessObject)Class.forName(bo).newInstance();
		} catch (Exception e) {
			log.error("找不到前台业务对象",e);
		}
		return bObj;
	}

	/**
	 * 前台业务对象
	 * @return
	 */
	public String getBo() {
		return bo;
	}

	public void setBo(String bo) {
		this.bo = bo;
	}

	@Override
	protected FileBatchTask getBatchTask() {
		FileBatchTask fbt = super.getBatchTask();
		fbt.setBeginIndex(1);//默认第一行是标题，从第二行开始导入，index从0开始
		
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

	public String getBatchsize() {
		return batchsize;
	}

	public void setBatchsize(String batchsize) {
		this.batchsize = batchsize;
	}

	@Override
	protected boolean onBeforeProcess() {
		if(StringUtils.isNotEmpty(bsBO)) {
			try {
				Class.forName(bsBO);
			} catch (ClassNotFoundException e) {
				log.error("找不到后台业务对象："+bsBO);
				return false;
			}
		}
		LJDPFileBatchTask task = (LJDPFileBatchTask)getBatchTask();
		if(StringUtils.isNotEmpty(bsBO)) {
			task.setBsBusinessObject(bsBO);
			task.setWay(BatchFileDic.PW_FB_F);
		}
		return super.onBeforeProcess();
	}

	/**
	 * 后台业务对象
	 * @return
	 */
	public String getBsBO() {
		return bsBO;
	}

	public void setBsBO(String bsBO) {
		this.bsBO = bsBO;
	}

}
