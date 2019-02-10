package org.openjava.boot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.ljdp.common.spring.SpringContext;
import org.ljdp.core.db.jpa.JPASessionFactoryRouter;
import org.ljdp.secure.validate.SessionValidator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.openjava.framework.user.CloudUserProvider;
import com.openjava.framework.user.LmAuthorityPersistent;
import com.openjava.framework.validate.EhcacheSessionValidator;
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
				"com.openjava.**.dao",
				"com.openjava.**.api",
				"org.openjava.boot.conf"
				})
@MapperScan({
			"com.openjava.**.mapper",
		})
@ServletComponentScan(basePackages= {
				"org.ljdp.support.web.listener2",
				"org.ljdp.support.web.servlet",
				"com.openjava.pay.notify"})
@ImportResource("classpath:springconfig/transaction.xml")
@EnableCaching
public class Application {
	
	//============缓存配置=================
	
	//==============会话认证=====================
	@Value("${ljdp.security.api.skey}")
	private String apiSkey;
	@Bean
	public SessionValidator sessionValidator() {
		return new RedisSessionVaidator(apiSkey);
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
	public CloudUserProvider webUserProvider() {
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
