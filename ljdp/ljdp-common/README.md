# 轻量级java开发平台常用组件封装

对一些第三方开源组件常用场景下的封装，进一步简化项目使用时的代码量。
下面对几个常用工具的介绍
# memcached组件封装
## 初始化配置文件
```
ICacheManager<MemcachedClient> manager = CacheUtil.getCacheManager();
manager.setConfigFile("memcached.xml");
manager.start();
```

## 配置文件:memcached.xml
```
<memcached>
    <socketpool name="defalutPool" failover="true" initConn="1" minConn="1" maxConn="1" maintSleep="0"
        nagle="false" socketTO="3000" aliveCheck="true">
        <servers>127.0.0.1:11211</servers>
    </socketpool>
    <client name="defalutCache" defaultEncoding="UTF-8" socketpool="defalutPool" defaultExpire="300000">
        <errorHandler></errorHandler>
    </client>
</memcached>
```

## 使用

```
manager.getCache("defalutCache").add(key, exp, value);
manager.getCache("defalutCache").get(key);
```

## 接入spring-cache

```
	<cache:annotation-driven />
	<bean id="cacheManager" class="org.ljdp.cache.spring.MemcachedCacheManager">
     <property name="caches">
       <set>
         <bean class="org.ljdp.cache.spring.MemcachedCache">
         	<property name="name" value="defalutCache"/>
         </bean>
       </set>
     </property>
   </bean>
```
## 接入spring-boot-cache
```
	//============缓存配置=================
	@Bean("cacheManager")
	public CacheManager cacheManager() {
		MemcachedCache defc = new MemcachedCache();
		defc.setName("defalutCache");
		MemcachedCache quickc = new MemcachedCache();
		quickc.setName("quickCache");
		List<MemcachedCache> caches = new ArrayList<>();
		caches.add(defc);
		caches.add(quickc);
		
		MemcachedCacheManager manager = new MemcachedCacheManager();
		manager.setCaches(caches);
		manager.afterPropertiesSet();
		return manager;
	}
```

# ehcache组件封装
包路径：org.ljdp.common.ehcache
## 初始化配置

```
//初始化一个缓存区域
MemoryCache.initCache("defalutCache", CacheType.LIFE);
//设置缓存时间10分钟
MemoryCache.config("defalutCache", CacheParam.TIME_LIFE_MINUTE, 10);
```

## 使用

```
MemoryCache.getCache("defalutCache").putData(key, data);
MemoryCache.getCache("defalutCache").getData(key);
```

## 接入spring-cache

```
<bean id="cacheManager"
    class="org.springframework.cache.ehcache.EhCacheCacheManager" p:cache-manager-ref="ehcache"/>

<!-- EhCache library setup -->
<bean id="ehcache"
	class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean" p:config-location="ehcache.xml"/>
</beans>
```


# 文件生成工具
## excel文件（POI组件）
类：org.ljdp.common.file.POIExcelBuilder
### 常见web导出excel文件使用示例

```
POIExcelBuilder myBuilder = new POIExcelBuilder(response.getOutputStream());
//设置导出字段，对应List里面对象的字段
myBuilder.addProperty("certSeq", "许可证序列");
myBuilder.addProperty("businessLicense", "工商注册号");
myBuilder.addProperty("entName", "企业名称");

myBuilder.buildSheet("test", result.getContent());//放到第一个sheet

String filename = "test.xlsx";
response.setContentType(ContentType.EXCEL);
response.addHeader("Content-disposition", "attachment;filename="
		+ new String(filename.getBytes("GBK"), "iso-8859-1"));
//开始导出
myBuilder.finish();
```

## Txt,Csv等文件
查看CSVBuilder，TextFileBuilder等，使用比较简单，等有空在补说明

# FTP工具
封装了apache的ftp工具包，使用起来更便捷
```
ApacheFTPClient ftp = new ApacheFTPClient("127.0.0.1", "admin", "123456");
ftp.downloadFile("www/abc/140425151415380.doc", "E:/tempfile/下载后的文件.doc");
ftp.close();
```

# HTTP请求组件
使用了apache的http工具包，实现了一些常用的get,post,post json报文等请求，也支持https
## 普通form格式请求
```
Map<String, Object> httpSetting = new HashMap<>();
httpSetting.put("socketTimeout", 30*1000);
//https的话，改用LjdpHttpClients就行
LjdpHttpClient httpclient = new LjdpHttpClient(httpSetting);
List<NameValuePair> params = new ArrayList<>();
params.add(new BasicNameValuePair("userId", "1234"));
params.add(new BasicNameValuePair("userTel", "1234"));
HttpResponse resp = httpclient.post("http://sun1.wxdg.sun0769.com/SunAct/verify/authorize", params);
//假设返回的结果是json报文，使用jackson工具转换为对象
ObjectMapper om = new ObjectMapper();
DgSunAppAuthResp authRes = om.readValue(resp.getEntity().getContent(), DgSunAppAuthResp.class);
```

## application/json格式提交
```
HttpResponse resp = httpclient.postJSON(String url, String content)
```

# XML处理
## java对象转为xml结构
使用dom4j实现，常用在把接口数据转为xml报文返回

```
ReflectBeanToXML beantoxml = new ReflectBeanToXML();
//设置忽略这些属性不转到xml
beantoxml.addIgnoreProperty("user.sex");
//对属性的值进行翻译后再输出到xml，转换器使用org.apache.commons.collections.Transformer
beantoxml.addTransformer("user.sex", new Transformer())
Document doc = beantoxml.toXML(bean);
String xmlstring = XMLUtils.asXMLString(doc);
```
