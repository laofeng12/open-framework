package com.openjava.example.compare.component;

import org.ljdp.common.file.ContentType;
import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.plugin.batch.bo.BaseFileImportBO;
import org.ljdp.plugin.batch.task.LJDPFileBatchBO;

import java.util.List;

/**
 * 继承LJDPFileBatchBO，任务记录不会保存数据库，只在内存临时保存。
 * 继承BaseFileImportBO，任务记录会保存数据库，后续可以通过taskId查询。
 */
public class FileReaderBO extends BaseFileImportBO {
    public List<String> contents;

    public FileReaderBO(List<String> contents){
        this.contents = contents;
    }

    @Override
    protected BatchResult doProcessRecord(String s) {
    	String[] items;
    	String contentType = super.getFileBatchTask().getFileContentType();
    	if(contentType.equals(ContentType.TEXT)) {
    		items = s.split("\\|");
    	} else if(contentType.equals(ContentType.CSV)) {
    		items = s.split("\\,");
    	}
        BatchResult result = new GeneralBatchResult();
        contents.add(s);
        result.setSuccess(true);
        if(contents.size() % 3 == 0) {
        	result.setSuccess(false);
        	result.setMessage("失败了");
        }
        return result;
    }

    @Override
    public String getTitle() {
        return "字段1|字段2|字段3|";
    }

    @Override
    public void destory() {

    }

    @Override
    public Boolean finalWork() {
        return true;
    }
}
