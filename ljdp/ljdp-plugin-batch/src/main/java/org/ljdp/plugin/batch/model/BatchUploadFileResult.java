package org.ljdp.plugin.batch.model;

import java.util.List;

import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.plugin.batch.task.FileUploadTask;

public class BatchUploadFileResult extends GeneralBatchResult {
	
	private static final long serialVersionUID = 8190463013425246L;

	@SuppressWarnings("unchecked")
	public List<FileUploadTask> getSuccessTask(){
		return (List<FileUploadTask>)getData();
	}
}
