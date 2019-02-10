# ljdp-core3

#### 项目介绍
轻量java开发平台核心数据处理层，动态sql构建器，动态数据源配置。

### 一、动态SQL构建
#### 背景
例如下图这种是个常见的后台工单查询菜单，包含了大量动态的条件。
![image](http://lmstore.oss-cn-shenzhen.aliyuncs.com/demo/ordermultiquery.png)
后端一般的做法是根据前端参数，组装成sql后执行查询，编写过程非常繁琐费时。
#### 此组件解决方法
使用运算符（eq,ne,lt,gt,le,ge等）与查询字段拼接作为查询参数，后端自动解析为sql查询语句。

#### 参数命名规则
例如：字段 eq_name的值为"myName"，则生成的条件子句为：name = 'myName'。
#### 代码例子
例如定义了这样一个用户表查询对象
```
public class SysUserDBParam extends RoDBQueryParam {
	private String like_fullname;//名称 like ?
	private String eq_account;//登录账号 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date le_createtime;//创建时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date ge_createtime;//创建时间 >= ?
}
```
然后mvc添加一个路由接收查询请求
```
@RequestMapping(value="/search")
	public TablePage<SysUser> search(SysUserDBParam params, Pageable pageable){
		Page<SysUser> result =  sysUserService.query(params, pageable);
		.....
	}
```
- 请求A：/search?like_fullname=管理&le_createtime=2018-04-19
- 生成查询条件：  where fullname like '%管理%' and createtime<=(2018-04-19日期类型)
- 请求B：/search?eq_account='admin'&ge_createtime=2018-04-19
- 生成查询条件： where account='admin' and createtime>=(2018-04-19日期类型)

#### 通用运算:（支持所有数据类型）

说明 | 等于 | 不等于|小于|大于|小于等于|大于等于|BETWEEN|IN|NOT IN|是否空值
---|---|---|---|---|---|---|---|---|---|---
sql运算符| =|<>|<| >| <=|   >=| between|in  |  not in  | true=(is null) false=(is not null)
代码中写法|eq_ | ne_ | lt_ |  gt_ |  le_| ge_   | between_  | in_  | nin_  |  null_

#### 字符串运算
说明 | like|not like|等于,忽略大小写|不等于,忽略大小写|like,忽略大小写|not like,忽略大小写
---|---|---|---|---|---|---|---
代码中写法|like_ |nlike_|ic_eq_|ic_ne_|ic_like_|ic_nlike

#### 自定义SQL
说明 | 自定义SQL
---|---
代码中写法|sql_


#### 直接使用DBQueryParam
有时需要在后端构建动态sql，也可以这样写：
```
DBQueryParam params = new DBQueryParam();
params.addQueryCondition(fieldA, RO.EQ, value0);
params.addQueryCondition(fieldB, RO.GT, value1);
//或者也可以这样写：
params.addQueryCondition(fieldA, "_eq_", value0);
params.addQueryCondition(fieldB, "_gt_", value1);
```
则生成的SQL为：fieldA=? and fieldB>?

#### 嵌套属性，复合主键使用‘$’符号进行连接
###### 方法1：继承DBQueryParam
先自定义一个查询对象
```
public class HycardWorkfCardDBParam extends DBQueryParam {
	
	private String eq_id$workfId;
	private Long eq_id$cardId;

    ...
}
```
然后使用
```
DBQueryParam params = new HycardWorkfCardDBParam();
params.setEq_id$workfId("*******");
```
###### 方法2：直接使用DBQueryParam
```
DBQueryParam params = new DBQueryParam();
params.addQueryCondition("id$workfId", "_eq_", value0);
params.addQueryCondition("id$cardId", "_eq_", value0);
```

#### 多表关联查询
当涉及多个表字段的动态组合查询，也是可以的，只需要自己编写表之间的关联就行，下面是个示例代码。

```
//首先初始化JpaMultiDynamicQueryDAO对象。(暂时只支持JPA)
    private EntityManager em;
	
	private JpaMultiDynamicQueryDAO dao;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
		dao = new JpaMultiDynamicQueryDAO(em);
	}
```
下面是多表查询写法
```
public Page<?> queryShopItemAndProd(ProdProductDBParam prodPrams, ShopItemDBParam itemParams, Pageable pageable){
    //先定义查询对象关联的表
	prodPrams.setTableAlias("p");
	itemParams.setTableAlias("s");
	//编写查询HQL的主部分（不用写查询条件）
	String multiHql = "select s,p from ShopItem s, ProdProduct p where s.pid=p.pid ";
	//执行查询
	return Page<?> dbresult = dao.query(multiHql, pageable, prodPrams,itemParams);
	return dbresult;
}
```
### 二、动态数据源
可以实现一个项目中访问不同的数据库
#### 1、第一步：配置多个数据源

```
spring:
  datasource:
    primary: 
      druid:
        url: jdbc:mysql://127.0.0.1:3306/jshop1?useUnicode=true&characterEncoding=utf-8&useSSL=false
        username: root
        password: 123456
    second: 
      druid:
        url: jdbc:mysql://127.0.0.1:3306/jshop2?useUnicode=true&characterEncoding=utf-8&useSSL=false
        username: root
        password: 123456
```
#### 2、第二步：初始化数据源
###### 数据源1
```
@Configuration
@ConfigurationProperties("spring.datasource.primary.druid")
public class PrimaryDruidDataSourceConfig extends DruidProperty{

	@Bean
	public DataSourceWrapper primaryDataSourceWrapper() throws SQLException {
		DruidDataSource ds = new DruidDataSource();
		ds.setName("主数据源");
		super.initConfig(ds);
        DataSourceWrapper dsw = new DataSourceWrapper(ds);
        return dsw;
	}

}
```
###### 数据源2
```
@Configuration
@ConfigurationProperties("spring.datasource.second.druid")
public class SecondDruidDataSourceConfig extends DruidProperty{

	@Bean
	public DataSourceWrapper secondDataSourceWrapper() throws SQLException {
		DruidDataSource ds = new DruidDataSource();
		ds.setName("副数据源");
		super.initConfig(ds);
        DataSourceWrapper dsw = new DataSourceWrapper(ds);
        return dsw;
	}
}
```
###### 设置主数据源

```
@Configuration
public class MyDataSourcesConf {
	
	@Autowired
	private DataSourceWrapper primaryDataSourceWrapper;
	@Autowired
	private DataSourceWrapper secondDataSourceWrapper;
	
	@Bean
	@Primary
	public DataSource dataSource() {
		DynamicDataSource dds = new DynamicDataSource();
		dds.setDefaultTargetDataSource(primaryDataSourceWrapper.getDataSource());
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put("primary", primaryDataSourceWrapper.getDataSource());
		targetDataSources.put("second", secondDataSourceWrapper.getDataSource());;
		dds.setTargetDataSources(targetDataSources);
		dds.afterPropertiesSet();
		return dds;
    }


}
```

#### 3、第三步：AOP截拦数据库访问

```
@Component
@Aspect
@Order(-1) //注意顺序要在事务管理器之前执行
public class DynamicDataSourceAOP {
	
	private DynamicDataSourceAspect aop = new DynamicDataSourceAspect();

	@Pointcut("execution(* com.openjava..service..*Impl.*(..)))")  
	public void executeService(){
	  
	}
	
	@Around("executeService()")
	public Object doAround(ProceedingJoinPoint point) throws Throwable{
		return aop.doAround(point);
	}
}
```

#### 4、最后，如何使用？
用TargetDataSource定义方法访问的数据源

```
@TargetDataSource("primary")
public ApiDoc getFromPrimary(Integer id) {
	ApiDoc m = apiDocRepository.findOne(id);
	return m;
}
/**
 * 切换另外一个数据库
*/
@TargetDataSource("second")
public ApiDoc getFromSecond(Integer id) {
	ApiDoc m = apiDocRepository.findOne(id);
	return m;
}
```





