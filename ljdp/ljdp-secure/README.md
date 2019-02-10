# 轻量java开发平台安全组件
系统安全相关的功能统一放在这个项目中，包含日志记录，api访问验证，页面访问验证，防xss攻击，异常截拦，加密解密套件等。

## 日志aop：ControllerLogAspect
在org.ljdp.log.aop下面。aop日志截拦器，可以记录：所有请求参数，返回的结果，访问的方法，客户端ip，当前服务器ip，时间。建议用来截拦mvc的请求。日志是异步保存的，原理是所有日志先保存在BlockingQueue中，然后需要编写一个线程来处理队列中的数据（建议批量提交至mysql或者mongodb，不要一条条insert）。
### 开始使用
#### spring配置
```
<bean id="logAspect" class="org.ljdp.log.aop.ControllerLogAspect"/>
<aop:config proxy-target-class="true">
	<aop:pointcut expression="(execution(* org.ljdp.support..controller..*.*(..)))" id="ljdpPointcut"/>
	<aop:aspect ref="logAspect">
		<aop:around pointcut-ref="ljdpPointcut" method="doAudit"/>
	</aop:aspect>
</aop:config>
```
#### 日志保存配置
在/ljdp/application.properties文件下面增加如下配置

```
#设置存储日志的数据库表映射对象,继承org.ljdp.log.model.RequestLog接口
request.log.entity=com.shop.log.domain.LogApiRequest

#日志队列的长度
service_max_capacity=100000
```
#### 过滤
如果需要设定某些方法不记录日志，可以使用LogConfig注解

```
@LogConfig(save=false)
@RequestMapping(value="/search",method=RequestMethod.GET)
public TablePage<BsArea> doSearch(BsAreaDBParam params, Pageable pageable){
		
}
```
#### 日志保存线程示例
最后需要自己编写一个线程异步处理日志队列，下面是例子

```
public class ApiLogSaveRunner implements Runnable {
	@Override
	public void run() {
		while(running) {
			try {
				Thread.sleep(1000);//每秒保存一次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BlockingQueue<RequestLog> queue = ControllerLogAspect.queue;
			int size = queue.size();
			if(size > 0) {
				List<RequestLog> logList = new ArrayList<>(size);
				for(int i=0; i < size; i++) {
					RequestLog r = queue.take();
					logList.add(r);
				}
				service.doBatchCreate(logList);
			}
		}
	}
}
```

## 异常aop：ExceptionAspect
在org.ljdp.secure.aop下面，如果方法抛出Exception，那么将记录异常完整信息到错误日志队列中，然后统一返回json报文：{code:500,message:"错误信息"}给客户端。错误日志队列需要自己编写线程进行异步处理。适合用在rest接口。
### 配置
参考ControllerLogAspect
### 日志保存配置
在/ljdp/application.properties文件下面增加如下配置

```
#设置存储错误日志的数据库表映射对象
request.errorlog.entity=com.shop.log.domain.LogApiError

```
### 日志保存线程示例
参考上面的ControllerLogAspect

## 接口请求安全aop：SecurityAspect
1、验证客户端是否有权限访问api，需要自己编写验证方法，只需实现接口：org.ljdp.secure.validate.SessionValidator。
2、对输入的参数进行了escape处理，可以防xss攻击。
### 接口规范
在ljdp平台开发规范里，所有api返回的对象，都实现org.ljdp.component.result.ApiResponse接口，所以即使验证不通过或者代码异常（例如上面的ExceptionAspect），返回的都是ApiResponse的对象，客户端都可以正常解析。
### aop配置
参考ControllerLogAspect
### 配置验证器
自己编写完成验证器后（实现接口SessionValidator），添加如下spring配置即可

```
<bean id="sessionValidator" class="org.ljdp.secure.validate.impl.LmcitySessionVaidator"></bean>
```
### 注解详细配置
如果需要精确配置每个不同方法有不同的处理，可以使用@Secure注解。

```
@Security(session=true,escape=true, filterJs=true,filterQuotes=true,excludes= {"param1","param2"})
@RequestMapping(value="/{id}",method=RequestMethod.GET)
public ApiDoc get(@PathVariable("id")Integer id) {
	ApiDoc m = apiDocService.get(id);
	return m;
}
```
- session：是否使用SessionValidator进行访问权限验证
- escape：是否强制对所有参数进行escape（防xss）
- filterJs：是否检测js方法并进行过滤（防js注入)
- filterQuotes:是否过滤掉所有引号（防js注入)
- excludes：排除这些参数不进行处理

## 加密解密套件
在org.ljdp.secure.cipher下面，主要由aes,md5,sha等常用的密码算法。

## 安全过滤器：DefaultSecurityFilter
主要用在前端工程，检测当前访问是否已经登录，如果没有登录那么跳转至登录页面。
### 配置
```
<filter>
  <filter-name>securityFilter</filter-name>
  <filter-class>org.ljdp.secure.filter.DefaultSecurityFilter</filter-class>
  设置哪些请求路径需要进行安全验证
  <init-param>
      <param-name>include-pattern</param-name>
      <param-value>
      	/m/order/*|/m/usercenter/*|/m/onlinecall/*
      </param-value>
  </init-param>
  设置验证的方法，需要自己实现
  <init-param>
  		<param-name>check-class</param-name>
  		<param-value>com.b2c.web.LoginChecker</param-value>
  </init-param>
  登录页面地址，如果验证不通过，那么重定向至这个页面
  <init-param>
  		<param-name>login-url</param-name>
  		<param-value>/m/useraccess/accountLogin</param-value>
  </init-param>
  如果是get请求，那么重定向时会自动把所有参数带到登录页面的。如果是post请求，参数会暂时记录在session中，登录成功后需要通过一个中间页面重新进行原来的post提交。不是必须
  <init-param>
  		<param-name>return-post-url</param-name>
  		<param-value>/m/useraccess/toReturn</param-value>
  </init-param>
  当前服务器的域名,不是必须
  <init-param>
  		<param-name>server-domain</param-name>
  		<param-value>www.domain.com</param-value>
  </init-param>
</filter>
```
