# ljdp-plugin-api

#### 项目介绍
基于java开发的api网关，反向代理

#### 配置代理
在servlet中添加如下设置

```
<!-- 
  所有/api/的请求，都会转发至后端服务器处理
-->
<servlet>
<servlet-name>AgencyTransportServlet</servlet-name>
<servlet-class>org.ljdp.api.servlet.AgencyTransportServlet</servlet-class>
<load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
<servlet-name>AgencyTransportServlet</servlet-name>
<url-pattern>/api/*</url-pattern>
</servlet-mapping>
```

#### 后端服务器地址配置
在文件/ljdp/application.properties添加如下配置

```
#服务器地址
api.url=http://127.0.0.1:8080
#每个路由可用的链接数(暂时只支持一个路由地址，所以和maxConnTotal设置一样即可)
api.maxConnPerRoute=20
#总链接数
api.maxConnTotal=20
```
