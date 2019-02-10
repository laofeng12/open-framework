package org.ljdp.common.timer.thread;

import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.ljdp.common.timer.ScheduleManager;
import org.ljdp.common.timer.ScheduleMemoryPool;
import org.ljdp.common.timer.model.ScheduleInstance;
import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.session.Request;
import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.component.task.TaskStatus;

public class ScheduleThread extends BaseBatchTask {
	private static final long serialVersionUID = 6122711449220713236L;
	
	private Request request;
	private Object resultObj;
	private BusinessObject bo;

	public ScheduleThread(String id, BusinessObject bo, Request request) {
		setId(id);
		this.request = request;
		this.bo = bo;
	}

	public void run() {
		try {
			beginTask();
//			String tasktype = request.getParameter("tasktype");
//			if(tasktype.equals("file")) {
//				String batchsize = request.getParameter("batchsize");
//				if(batchsize.length() > 0) {
//					try {
//						this.setBatchSize(Integer.parseInt(batchsize));
//					} catch (NumberFormatException e) {
//						e.printStackTrace();
//					}
//				}
//				doFileWork();
//			} else {
				doDefaultWork();
//			}
		} finally {
			endTask();
		}
	}

//	private void doFileWork() {
//		String impfile = request.getParameter("impfile");
//		String bakdir = request.getParameter("bakdir");
//		String[] items = FileUtils.getPathAndName(impfile);
//		String impDir = items[0];
//		String fnReg = items[1];
//		String[] fnames = FileUtils.getFileList(impDir, fnReg);
//		ArrayList<BatchResult> resList = new ArrayList<BatchResult>();
//		for(int i = 0; i < fnames.length; i++) {
//			String fname = fnames[i];
//			super.setFilename(impDir + fname);
//			super.setTotalRecords(getTotalProcedure() + getRecordNumber(getFilename()));
//			super.run();
//			if(getResult().isSuccess()) {
//				try {
//					String bakFileName = new FileTimeNameService().createFileName(fname, "");
//					String bakFullPathFileName = FileUtils.joinDirectory(bakdir, bakFileName);
//					if(FileUtils.move(impDir + fname, bakFullPathFileName)) {
//						getResult().setOutputFileName(bakFullPathFileName);
//					} else {
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		    }
//			resList.add(getResult());
//		}
//		if(resList.size() > 1) {
//			resultObj = resList;
//		} else {
//			resultObj = resList.get(0);
//		}
//	}
	
	@SuppressWarnings("rawtypes")
	private void doDefaultWork() {
		try {
			start();
			resultObj = bo.doBusiness(request, getCursor());
			if(resultObj != null) {
				Class[] intf = resultObj.getClass().getInterfaces();
				if(ArrayUtils.contains(intf, BatchResult.class)) {
					setResult((BatchResult)resultObj);
				}
			}
		} catch (Exception e) {
			while(e.getCause() != null) {
				e = (Exception)e.getCause();
			}
			e.printStackTrace();
			result = new GeneralBatchResult();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			resultObj = getResult();
		} finally {
			finish();
		}
	}
	
	protected void beginTask() {
		ScheduleMemoryPool.putThread(this);
		ScheduleInstance instance = getRunInstance();
		instance.setStatus(TaskStatus.RUNNING);
		instance.setActualTime(new Date());
		instance.setCursor(getCursor());
	}
	
	protected void endTask() {
		ScheduleInstance instance = getRunInstance();
		if(getResult() != null) {
			if(getResult().isSuccess()) {
				instance.setStatus(TaskStatus.SUCCESS);
			} else {
				instance.setStatus(TaskStatus.FAILURE);
			}
			instance.setMessage(getResult().getMsg());
		} else {
			instance.setStatus(TaskStatus.SUCCESS);
		}
		instance.setFinishTime(new Date());
		instance.setResultObj(resultObj);
		ScheduleMemoryPool.removeThread(getId());
		if( instance.isIsolate() ) {
			//实例被隔离，直接删除
			ScheduleMemoryPool.removeInstance(instance.getId());
		} else {
			//扫描对应定时任务，生成下次运行实例
			ScheduleManager.scanTask(instance.getTaskId());
		}
	}

	public ScheduleInstance getRunInstance() {
		ScheduleInstance instance = ScheduleMemoryPool.getInstance(getId());
		return instance;
	}
	
}
