# ljdp-webcoder3

#### 项目介绍
ljdp框架的代码生成器。使用ExtJs实现。

#### 软件架构
生成的代码基于前后端分离设计。
##### api框架
- 主要技术栈：springmvc+spring+spring data jpa+hibernate。
- api文档：swagger
- api认证：LJDP-secure
- api日志：LJDP-secure

##### 前端框架
有两套前端框架

###### 第一套：开发方式为jsp，主要由后端人员开发
技术栈：bootstrap+bootstrap Table+jquery+jsp

###### 第二套：开发方式为react，主要由前端人员开发
技术栈：react + Ant Design Pro

#### 功能
1. 查询（=，>，<，like，in，not in, is null，is not null等条件都支持）
2. 新增
3. 修改
4. 删除
5. 导出
6. 导入
7. 上传附件
8. 支持数据字典

#### 安装教程
用一个maven工程引入这个war包，然后配置好数据库地址后用tomcat启动使用。参考这个工程：https://gitee.com/hzy0769/webcoder-generate

