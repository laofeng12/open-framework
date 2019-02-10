package org.ljdp.module.filetask;

import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.component.task.TaskCursor;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.component.user.DBAccessUser;

public abstract class AbstractFileBatchBO implements FileBusinessObject {
	private String operType;
	private TaskCursor cursor;
	private DBAccessUser user;
	private BaseUserInfo userInfo;
	private BOFileBatchTask fileBatchTask;
	private String submitParams;

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public TaskCursor getCursor() {
		return cursor;
	}

	public void setCursor(TaskCursor cursor) {
		this.cursor = cursor;
	}

	public DBAccessUser getUser() {
		if(user == null) {
			return DBAccessUser.getInnerUser();
		}
		return user;
	}

	public void setUser(DBAccessUser user) {
		this.user = user;
	}

	public BOFileBatchTask getFileBatchTask() {
		return fileBatchTask;
	}

	public void setFileBatchTask(BOFileBatchTask fileBatchTask) {
		this.fileBatchTask = fileBatchTask;
	}

	public String getSubmitParams() {
		return submitParams;
	}

	public void setSubmitParams(String submitParams) {
		this.submitParams = submitParams;
	}

	public BaseUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(BaseUserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
