package org.ljdp.component.strategy;

import org.ljdp.component.result.BatchResult;
import org.ljdp.component.session.Request;
import org.ljdp.component.task.TaskCursor;

public abstract class AbstractTaskBO implements BusinessObject {
	private TaskCursor cursor;

	public Object doBusiness(Object... params) throws Exception {
		Request request = (Request)params[0];
		TaskCursor cursor = (TaskCursor)params[0];
		setCursor(cursor);
		return doBusiness(request);
	}

	public abstract BatchResult doBusiness(Request request) throws Exception;
	
	public TaskCursor getCursor() {
		return cursor;
	}
	
	public void setCursor(TaskCursor cursor) {
		this.cursor = cursor;
	}
}

