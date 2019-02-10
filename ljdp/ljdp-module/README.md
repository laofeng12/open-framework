# ljdp-module

#### 项目介绍
轻量java开发平台，业务组件，目前只有一个文件数据批量处理组件，支持excel和txt，主要用在文件数据导入

### 一、文件数据处理模块
#### 处理流程
1. 执行initialization()方法进行任务初始化
2. 读取文件第n数据，执行doProcessRecord()方法进行处理，循环这一步直到文件读取完最后一行。
3. 执行finalWork()，做最后的业务处理
4. 执行destory()，如果需要可以销毁处理过程中用到的资料，例如缓存等。

#### 实现业务接口
编写一个类实现接口：org.ljdp.component.strategy.FileBusinessObject。也就是需要自己编写具体的业务代码实现上面流程需要用的initialization，执行doProcessRecord，finalWork，destory方法。
##### 事务封装
建议继承org.ljdp.module.batch.TransactionFileBatchBO，它会对doProcessRecord方法增加的数据库事务封装。

#### 开始使用
```
FileBusinessObject bo = new 自定义的业务对象
FileBatchTask batch = BOFileBatchTask(bo);
//设置任务主键ID
batch.setId(ConcurrentSequence.getInstance().getSequence(""));
//设置任务的操作者
batch.setUser(getUser());
//设置任务类型
batch.setType(batchType);
//设置处理方式，前台实时处理|后台定时处理
batch.setWay(BatchFileDic.PW_F);
//设置处理的文件全路径(这个是重点，最终读取的文件是这个)
batch.setFilename(fileFullName);
//设置文件名称
batch.setUploadFileName(fileName);
//设置任务名称
batch.setName(fileName);
//从第二行数据开始处理（通常第一行是标题）
batch.setBeginIndex(1);
//设置每轮事务处理的数据行数，如果涉及数据库提交，建议设置为100-1000。如果对成功率要求较高，那么设置为1
batch.setBatchSize(1);

//最后新建个线程启动任务(生产环境建议使用线程池)
new Thread(batch).start();
```
#### 查看处理进度
获取当前游标，里面包含进度信息
```
batch.getCursor()
```
