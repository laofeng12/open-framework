package org.openjava.boot;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ljdp.common.spring.SpringContext;
import org.ljdp.core.db.jpa.JPASessionFactoryRouter;
import org.ljdp.secure.validate.SessionValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.openjava.framework.user.CloudUserProvider;
import com.openjava.framework.user.LmAuthorityPersistent;
import com.openjava.framework.validate.RedisSessionVaidator;

@EntityScan(basePackages= {
		"org.ljdp.plugin.batch.persistent",
		"org.ljdp.support.attach.domain",
		"com.openjava.**.domain"
})
@EnableJpaRepositories(
		basePackages={"org.ljdp.support.**.repository",
				"com.openjava.**.repository"},
		repositoryFactoryBeanClass=org.ljdp.core.spring.data.LjdpJpaRepositoryFactoryBean.class)
@EnableTransactionManagement(mode=AdviceMode.ASPECTJ)
@SpringBootApplication(
		scanBasePackages={
				"org.ljdp.support.**.component",
				"org.ljdp.support.**.service",
				"org.ljdp.support.**.controller",
				"com.openjava.**.service",
				"com.openjava.**.component",
				"com.openjava.**.api",
				"org.openjava.boot.conf"
				})
@ServletComponentScan(basePackages= {"org.ljdp.support.web.listener2"})
@ImportResource("classpath:springconfig/transaction.xml")
@EnableCaching
public class Application {
	
	//============缓存配置 spring boot 1.0=================
	/*@Bean("cacheManager")
	public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
		RedisCacheManager manager = new RedisCacheManager(redisTemplate);
		// 整体缓存过期时间
        manager.setDefaultExpiration(3600L);
        // 设置缓存过期时间。key和缓存过期时间，单位秒
//        Map<String, Long> expiresMap = new HashMap<>();
//        expiresMap.put("default", 300L);
//        manager.setExpires(expiresMap);
        return manager;
	}*/
	// spring boot 2
	@Bean("cacheManager")
	public CacheManager cacheManager(RedisConnectionFactory factory) {
	    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();  // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
	    config = config.entryTtl(Duration.ofMinutes(60))     // 设置缓存的默认过期时间，也是使用Duration设置
	            .disableCachingNullValues();     // 不缓存空值

	    // 设置一个初始化的缓存空间set集合
	    Set<String> cacheNames =  new HashSet<>();
	    cacheNames.add("default");
	    cacheNames.add("quick");

	    // 对每个缓存空间应用不同的配置
	    Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
	    configMap.put("default", config);
	    configMap.put("quick", config.entryTtl(Duration.ofSeconds(120)));

	    RedisCacheManager cacheManager = RedisCacheManager.builder(factory)     // 使用自定义的缓存配置初始化一个cacheManager
	            .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
	            .withInitialCacheConfigurations(configMap)
	            .build();
	    return cacheManager;
	}
	
	//==============会话认证=====================
	@Bean
	public SessionValidator sessionValidator() {
		return new RedisSessionVaidator();
//		return new EhcacheSessionValidator();
	}
	
	@Bean
	public LmAuthorityPersistent authorityPersistent() {
		return new LmAuthorityPersistent();
	}
	
	//============LJDP相关配置=======================
	@Bean("db.SessionFactoryRouter")
	public JPASessionFactoryRouter sessionFactoryRouter() {
		return new JPASessionFactoryRouter();
	}
	
	@Bean("web.UserProvider")
	public CloudUserProvider lmUserProvider() {
		return new CloudUserProvider();
	}
	
	@Bean
	public LjdpBootRunner ljdpRunner() {
		return new LjdpBootRunner();
	}
	
	@Bean
	public SpringContext springContext() {
		return SpringContext.getEmbedInstance();
	}

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
