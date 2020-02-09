package org.ljdp.plugin.batch.task;

import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.module.batch.TransactionFileBatchBO;
import org.ljdp.module.filetask.FileBatchTask;
import org.ljdp.plugin.batch.model.TaskInfoVO;

public abstract class LJDPFileBatchBO extends TransactionFileBatchBO {
	
	public TaskInfoVO getBatchTaskInfo() {
		FileBatchTask task = (FileBatchTask)getFileBatchTask();
		TaskInfoVO vo = new TaskInfoVO(task.getId(), task.getUploadFileName());
		vo.setType(task.getType());
		Object user = task.getUser();
		if(user != null) {
			if(user instanceof DBAccessUser) {
				DBAccessUser dbUser = (DBAccessUser)user;
				vo.setOperatorId(dbUser.getId());
				vo.setOperatorAccount(dbUser.getAccount());
				vo.setOperatorName(dbUser.getName());
			} else  {
				BaseUserInfo baseuser = (BaseUserInfo)user;
				vo.setOperatorId(baseuser.getUserId());
				vo.setOperatorAccount(baseuser.getUserAccount());
				vo.setOperatorName(baseuser.getUserName());
			}
		}
		vo.setProcessWay(task.getWay());
		vo.setBsBusinessObject(task.getBsBusinessObject());
		vo.setOperType(task.getOperType());
		vo.setTotalNum(task.getTotalRecords());
		vo.setSuccessNum(task.getOk());
		vo.setFailNum(task.getFail());
		vo.setUserTime(task.getUseTime());
		vo.setFailLog(task.getErrResultFile());
		vo.setProcessFileName(task.getFilename());
		return vo;
	}
}
