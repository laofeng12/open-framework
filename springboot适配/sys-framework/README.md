# sys-framework

#### 项目介绍
基于LJDP框架规范的一个通用实现，包含了日志保存的对象定义，api安全授权的session管理，数据字典管理

#### API访问授权的session管理

```
//设置session检查器，当api配置了 @Secure(true)时，会执行此检查
@Bean
public RedisSessionVaidator sessionValidator() {
	return new RedisSessionVaidator();
}
//session持久化实现，避免缓存失效导致用户需要重新认证（非必须）
@Bean
public LmAuthorityPersistent authorityPersistent() {
	return new LmAuthorityPersistent();
}
```

#### 设置日志存储
在配置文件/ljdp/core.properties或者/ljdp/application.properties中添加api访问日志储存的对象

```
//访问日志
request.log.entity=com.openjava.shop.log.domain.LogApiRequest
//报错日志
request.errorlog.entity=com.openjava.shop.log.domain.LogApiError
```
可参考组件：[ljdp-secure](https://gitee.com/hzy0769/ljdp-secure)
#### 数据字典相关api
内置了一套数据字典管理的api，对应库表tsys_code。只需把包路径添加进spring的bean管理即可

```
@SpringBootApplication(
		scanBasePackages={
				"com.openjava.**.service",
				"com.openjava.**.component",
				"com.openjava.**.api",
				})
```
