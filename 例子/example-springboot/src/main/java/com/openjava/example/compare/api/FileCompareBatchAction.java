package com.openjava.example.compare.api;

import com.openjava.example.compare.component.FileReaderBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.file.AbstractResultFile;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.module.filetask.FileBatchTask;
import org.ljdp.plugin.batch.pool.MemoryTaskPool;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.ljdp.plugin.batch.task.EhcacheFileBatchTask;
import org.ljdp.plugin.batch.view.spring.AbstractBatchComController;
import org.ljdp.secure.annotation.Security;
import org.ljdp.support.attach.component.LjdpFileuploadConfig;
import org.ljdp.util.DateFormater;
import org.ljdp.util.FileUtils;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Api(tags="文件内容对比")
@RestController
@RequestMapping("/example/compare/file")
public class FileCompareBatchAction extends AbstractBatchComController {

    private static List<String[]> resultList = new ArrayList<>();

    @Resource
    private LjdpFileuploadConfig fileuploadConfig;
//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation(value = "开始处理", nickname="process")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file1", value = "文件1", required = true, dataType = "file", paramType = "post"),
            @ApiImplicitParam(name = "file2", value = "文件2", required = true, dataType = "file", paramType = "post"),
    })
    @RequestMapping(value="/process", method= RequestMethod.POST)
    @Security(session=false)
    public Result doBatchProcess(@RequestParam("file1") Part file1,
                                 @RequestParam(value="file2",required = false) Part file2) throws Exception{
        //1、先保存到服务器
        String fullFilePath1 = FileUtils.joinDirectory(fileuploadConfig.getLocalPath(),file1.getSubmittedFileName());
//        String fullFilePath2 = FileUtils.joinDirectory(fileuploadConfig.getLocalPath(),file2.getSubmittedFileName());
        System.out.println(fullFilePath1);
//        System.out.println(fullFilePath2);
        file1.write(fullFilePath1);
//        file2.write(fullFilePath2);
      
        //2、创建业务对象
        List<String> fileContents1 = new ArrayList<>();
        FileReaderBO readerBO1 = new FileReaderBO(fileContents1);
//        System.out.println(file1.getContentType());
//        FileReaderBO bo;
//        if(file1.getContentType().equals(ContentType.TEXT)) {
//        	bo = new FileReaderBO(fileContents1);
//        } else if(file1.getContentType().equals(ContentType.CSV)) {
//        	bo = new FileReaderBO(fileContents1);
//        }

        //3、开始异步处理文件
        Result result1 = super.doBatchProcess(readerBO1, fullFilePath1,
                file1.getSubmittedFileName(), "对比文件1", 0);
//        Result result2 = super.doBatchProcess(readerBO2, fullFilePath2,
//                file2.getSubmittedFileName(), "对比文件2", 0);
        if(!result1.isSuccess()){
            return result1;
        }
//        if(!result2.isSuccess()){
//            return result2;
//        }
        //4、获取任务状态
        FileBatchTask task1 = readerBO1.getFileBatchTask();
//        FileBatchTask task2 = readerBO2.getFileBatchTask();
        while (true){
            Thread.sleep(500);
            if(task1.isFinished() ){
                break;
            }
        }

//        if(task1.getFail()>0){
//            Result fail1 = new GeneralResult();
//            fail1.setSuccess(false);
//            fail1.setMessage(file1.getSubmittedFileName()+"处理失败，请下载失败文件查看");
//            fail1.setData(task1.getId());
//            return fail1;
//        }

        System.out.println("读出数据--------------");
        fileContents1.forEach(line->{
        	System.out.println(line);
        });

        //返回
        List<String> ids = new ArrayList<>();
        ids.add(task1.getId());
        Result result = new GeneralResult();
        result.setSuccess(true);
        result.setData(ids);
        return result;
    }

   

    @Override
    protected FileBatchTask getBatchTask(FileBusinessObject bo) {
        FileBatchTask task = new EhcacheFileBatchTask(bo);
        //第一行是标题，从第二行开始导入，index从0开始
        task.setBeginIndex(1);
        //处理完成后删除原文件
        task.setDeleteAfterProcess(false);
        /*
         * 一个事务批量提交的数量（当事务回滚，会把当前事务涉及的数据都回滚）
         *  场景1：主要是把文件数据insert入库，建议设置为100~1000
         *  场景2：进行业务办理，每行数据都是一次独立的业务办理，建议设置为1
         *  场景3：整个文件数据是一个单独的业务表单，例如导入一张试卷，建议设置尽量大的值，超出当前预计的表单最大数量，建议>=1000
         */
        task.setBatchSize(1);
        //如果是excel文件，填写一共有多少个sheet需要处理
        task.setSheetNumber(1);
        task.setShowRate(true);
        task.setResultType(AbstractResultFile.TOW_RES_FILE);
        return task;
    }

    @ApiOperation(value = "获取任务", nickname="getTask")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "string", paramType = "path"),
    })
    @RequestMapping(value="/task/{taskId}", method= RequestMethod.GET)
    @Security(session=false)
    public BaseBatchTask getTask(@PathVariable("taskId")String taskId) {
        BaseBatchTask task = TaskPoolManager.getFgPool().getTaskByID(taskId);
        if(task == null) {
            //任务已经结束放到临时内存
            task = MemoryTaskPool.getTask(taskId);
//            task = (BaseBatchTask)redisTemplate.opsForValue().get(taskId);
        }
        return task;
    }
}
