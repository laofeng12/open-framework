package org.ljdp.cache.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLInputFactory;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.cache.CacheUtil;
import org.ljdp.cache.ICacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class MemcachedCacheManager implements ICacheManager<MemcachedClient> {

	private final Logger Logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String MEMCACHED_CONFIG_FILE = "memcached.xml";

	private ConcurrentHashMap<String, MemcachedClient> cachepool;
	private List<MemcachedClientConfig> memcachedClientconfigs;
	private Map<String, MemcachedClientConfig> memcachedClientConfigMap;
	private List<MemcachedClientSocketPoolConfig> memcachedClientSocketPoolConfigs;
	private List<MemcachedClientClusterConfig> memcachedClientClusterConfigs;
	private String configFile;

	public void start() {
		this.cachepool = new ConcurrentHashMap<>();

		this.memcachedClientconfigs = new ArrayList<>();
		this.memcachedClientSocketPoolConfigs = new ArrayList<>();
		this.memcachedClientClusterConfigs = new ArrayList<>();
		
		loadConfig(this.configFile);

		if ((this.memcachedClientconfigs != null) && (this.memcachedClientconfigs.size() > 0)
				&& (this.memcachedClientSocketPoolConfigs != null)
				&& (this.memcachedClientSocketPoolConfigs.size() > 0)) {
			memcachedClientConfigMap = new HashMap<>(memcachedClientconfigs.size());
			for (MemcachedClientConfig cc : memcachedClientconfigs) {
				memcachedClientConfigMap.put(cc.getName(), cc);
			}
			try {
				initMemCacheClientPool();
			} catch (Exception ex) {
				Logger.error("MemcachedManager init error ,please check !");
				throw new RuntimeException("MemcachedManager init error ,please check !", ex);
			}

		} else {
			Logger.error("no config info for MemcachedManager,please check !");
			throw new RuntimeException("no config info for MemcachedManager,please check !");
		}
	}

	public MemcachedClient getCache(String name) {
		return cachepool.get(name);
	}

	public void stop() {
		try {
			for (int i = 0; i < memcachedClientconfigs.size(); i++) {
				MemcachedClientConfig mcc = (MemcachedClientConfig) memcachedClientconfigs.get(i);
				MemcachedClient client = getCache(mcc.getName());
				client.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cachepool.clear();
			if (this.memcachedClientconfigs != null) {
				this.memcachedClientconfigs.clear();
			}
			if (this.memcachedClientSocketPoolConfigs != null) {
				this.memcachedClientSocketPoolConfigs.clear();
			}
			if (this.memcachedClientClusterConfigs != null)
				this.memcachedClientClusterConfigs.clear();
		}
	}

	public String getConfigFile() {
		return this.configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public void reload(String configFile) {
		if ((configFile != null) && (!configFile.equals(""))) {
			this.configFile = configFile;
		}
		stop();
		start();
	}

	protected void initMemCacheClientPool() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		for (int i = 0; i < memcachedClientconfigs.size(); i++) {
			MemcachedClientConfig mcc = (MemcachedClientConfig) memcachedClientconfigs.get(i);
			MemcachedClientSocketPoolConfig pool = getPoolConfig(mcc.getSocketPool());

			List<InetSocketAddress> addrList = AddrUtil.getAddresses(pool.getServers());
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(addrList);
			builder.setConnectionPoolSize(pool.getMaxConn());
			if(mcc.getCommand().equals("binary")) {
				builder.setCommandFactory(new BinaryCommandFactory());
			}
			if(StringUtils.isNotBlank(mcc.getTranscoder())) {
				Class cls = Class.forName(mcc.getTranscoder());
				Transcoder tcode = (Transcoder)cls.newInstance();
				builder.setTranscoder(tcode);
			}
			
//			for (InetSocketAddress inetSocketAddress : addrList) {
//				builder.addAuthInfo(inetSocketAddress, AuthInfo.plain("10000", "pwd"));
//			}
			
			MemcachedClient c = builder.build();
			cachepool.put(mcc.getName(), c);
		}
	}

	protected MemcachedClientSocketPoolConfig getPoolConfig(String name) {
		for (int i = 0; i < memcachedClientSocketPoolConfigs.size(); i++) {
			MemcachedClientSocketPoolConfig pc = (MemcachedClientSocketPoolConfig) memcachedClientSocketPoolConfigs
					.get(i);
			if (pc.getName().equals(name)) {
				return pc;
			}
		}
		return null;
	}
	
	public MemcachedClientConfig getClientConfig(String name) {
		return memcachedClientConfigMap.get(name);
	}

	protected void loadConfig(String configFile) {
		try {
			URL url = null;
			ClassLoader loader = Thread.currentThread().getContextClassLoader();

			if ((configFile != null) && (!configFile.equals(""))) {
				if (configFile.startsWith("http"))
					url = new URL(configFile);
				else
					url = loader.getResource(configFile);
			} else {
				url = loader.getResource(MEMCACHED_CONFIG_FILE);
			}
			XMLInputFactory factory = XMLInputFactory.newInstance();

			if (url == null) {
				Logger.error("no memcached config find! please put memcached.xml in your classpath");
				throw new RuntimeException("no memcached config find! please put memcached.xml in your classpath");
			}
			
			CacheUtil.loadMemcachedConfigFromURL(url, factory, this.memcachedClientconfigs,
					this.memcachedClientSocketPoolConfigs, this.memcachedClientClusterConfigs);

			Logger.info("load config from :" + url.getFile());

		} catch (Exception ex) {
			Logger.error("MemcachedManager loadConfig error !");
			throw new RuntimeException("MemcachedManager loadConfig error !", ex);
		}
	}
}
