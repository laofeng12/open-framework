# ljdp-ui

#### 项目介绍
轻量java开发平台视图层，主要用在mvc中的模型层，便于控制器与视图层的数据对接。

#### 一、bootstarp table
如果视图层的分页表格，用的是 [bootstrap table](http://bootstrap-table.wenzhixin.net.cn/zh-cn/)
，那么可以org.ljdp.ui.bootstrap包下面的模型。
##### 例如后端控制这样返回数据
```
@RequestMapping(value="/search",method=RequestMethod.GET)
public TablePage<SysSubsystem> doSearch(SysSubsystemDBParam params, Pageable pageable){
	Page<SysSubsystem> result =  sysSubsystemService.query(params, pageable);
	
	return new TablePageImpl<>(result.getTotalElements(), result.getContent());
}
```
##### 前端用bootstrap table js初始化表格
```
$('#myTable').bootstrapTable({
    url: 'http://127.0.0.1/user/search'
}})
```
#### 二、ExtJs
在org.ljdp.ui.extjs下面，与前端extJs框架对接的一些方法和模型，早期项目用的比较多，现在都改用bootstrap，基本很少使用了。

#### 三、访问日志
设置过滤器：org.ljdp.ui.filter.AccessLogFilter。
```
<filter>
    <filter-name>AccessLogFilter</filter-name>
    <filter-class>org.ljdp.ui.filter.AccessLogFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>AccessLogFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```
那么所有请求的HTTP报文详细内容都会以直观的方式打印在控制台，适合在开发阶段的调试。生产环境请关闭。

