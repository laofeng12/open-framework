# ljdp-plugin-sys

#### 项目介绍
ljdp扩展功能之管理后台系统组件，实现登录，注销，进入首页（同时获取当前用户角色有权限访问的菜单）

#### 配置
项目使用springmvc，在mvc的配置文件中增加如下配置让spring扫描我们需要访问的controller

```
<context:component-scan base-package="org.ljdp.plugin.sys.controller"/>
```
springmvc的servlet配置中，需要添加如下两个映射

```
  <servlet-mapping>
    <servlet-name>web-servlet</servlet-name>
    <url-pattern>*.jspx</url-pattern>
  </servlet-mapping>
  <servlet-mapping>
    <servlet-name>web-servlet</servlet-name>
    <url-pattern>*.act</url-pattern>
  </servlet-mapping>
```


#### 访问地址
###### 登录页面:/dashboard/user/login.jspx
###### 登录接口地址:/dashboard/user/login.act
###### 注销接口地址：/dashboard/user/logout.act
###### 首页页面：/dashboard/index.jspx

#### 业务实现
本组件只是做了个框架，具体业务需要自己实现：
1. 登录实现（实现接口org.ljdp.plugin.sys.service.LjdpUserService）
2. 获取当前用户的角色与菜单（实现接口org.ljdp.plugin.sys.service.LjdpResService）

在spring的配置文件中添加接口的实现

```
<bean id="ljdp.sys.UserService" class="com.openjava.admin.user.client.SysUserServiceClient"></bean>
<bean id="ljdp.sys.ResService" class="com.openjava.admin.user.client.SysUserResServiceClient"></bean>
```


