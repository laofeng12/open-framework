package org.ljdp.cache;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.ljdp.cache.memcached.MemcachedCacheManager;
import org.ljdp.cache.memcached.MemcachedClientClusterConfig;
import org.ljdp.cache.memcached.MemcachedClientConfig;
import org.ljdp.cache.memcached.MemcachedClientSocketPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;

public class CacheUtil {
	private static final Logger Logger = LoggerFactory.getLogger(CacheUtil.class);
	
	private static ICacheManager<MemcachedClient> manager;
	
	public static ICacheManager<MemcachedClient> getCacheManager() {
		if(manager == null) {
			manager = new MemcachedCacheManager();
		}
		return manager;
	}
	
	public static void loadMemcachedConfigFromURL(URL url, XMLInputFactory factory,
			List<MemcachedClientConfig> memcachedClientconfigs,
			List<MemcachedClientSocketPoolConfig> memcachedClientSocketPoolConfigs,
			List<MemcachedClientClusterConfig> memcachedClientClusterConfig) {
		MemcachedClientConfig node = null;
		MemcachedClientSocketPoolConfig socketnode = null;
		MemcachedClientClusterConfig clusternode = null;

		InputStream in = null;
		XMLEventReader r = null;
		try {
			in = url.openStream();
			r = factory.createXMLEventReader(in);

			String servers = null;
			String weights = null;
			while (r.hasNext()) {
				XMLEvent event = r.nextEvent();
//				System.out.println(event);
				if (event.isStartElement()) {
					StartElement start = event.asStartElement();

					String tag = start.getName().getLocalPart();
					if (tag.equalsIgnoreCase("client")) {
						node = new MemcachedClientConfig();

						if (start.getAttributeByName(new QName("", "name")) != null)
							node.setName(start.getAttributeByName(new QName("", "name")).getValue());
						else {
							throw new RuntimeException("memcached client name can't not be null!");
						}
						if (start.getAttributeByName(new QName("", "socketpool")) != null)
							node.setSocketPool(start.getAttributeByName(new QName("", "socketpool")).getValue());
						else {
							throw new RuntimeException("memcached client socketpool can't not be null!");
						}
						if (start.getAttributeByName(new QName("", "compressEnable")) != null) {
							node.setCompressEnable(Boolean.parseBoolean(
									start.getAttributeByName(new QName("", "compressEnable")).getValue()));
						} else {
							node.setCompressEnable(true);
						}
						if (start.getAttributeByName(new QName("", "defaultEncoding")) != null) {
							node.setDefaultEncoding(
									start.getAttributeByName(new QName("", "defaultEncoding")).getValue());
						} else {
							node.setDefaultEncoding("UTF-8");
						}
						if (start.getAttributeByName(new QName("", "command")) != null) {
							node.setCommand(start.getAttributeByName(new QName("", "command")).getValue());
						} else {
							node.setCommand("text");
						}
						if (start.getAttributeByName(new QName("", "defaultExpire")) != null) {
							String exp = start.getAttributeByName(new QName("", "defaultExpire")).getValue();
							node.setDefaultExpire(Integer.parseInt(exp));
						} else {
						}
					}

					if ((tag.equalsIgnoreCase("errorHandler")) && (node != null)) {
						event = r.peek();

						if (!event.isCharacters())
							continue;
						node.setErrorHandler(event.asCharacters().getData());
						r.nextEvent();
					}
					
					if ((tag.equalsIgnoreCase("transcoder")) && (node != null)) {
						event = r.peek();

						if (!event.isCharacters())
							continue;
						node.setTranscoder(event.asCharacters().getData());
						r.nextEvent();
					}

					if (tag.equalsIgnoreCase("socketpool")) {
						socketnode = new MemcachedClientSocketPoolConfig();

						servers = null;
						weights = null;

						if (start.getAttributeByName(new QName("", "name")) != null)
							socketnode.setName(start.getAttributeByName(new QName("", "name")).getValue());
						else {
							throw new RuntimeException("memcached client socketpool name can't not be null!");
						}
						if (start.getAttributeByName(new QName("", "failover")) != null) {
							socketnode.setFailover(Boolean
									.parseBoolean(start.getAttributeByName(new QName("", "failover")).getValue()));
						}

						if (start.getAttributeByName(new QName("", "initConn")) != null) {
							socketnode.setInitConn(
									Integer.parseInt(start.getAttributeByName(new QName("", "initConn")).getValue()));
						}

						if (start.getAttributeByName(new QName("", "minConn")) != null) {
							socketnode.setMinConn(
									Integer.parseInt(start.getAttributeByName(new QName("", "minConn")).getValue()));
						}

						if (start.getAttributeByName(new QName("", "maxConn")) != null) {
							socketnode.setMaxConn(
									Integer.parseInt(start.getAttributeByName(new QName("", "maxConn")).getValue()));
						}

						if (start.getAttributeByName(new QName("", "maintSleep")) != null) {
							socketnode.setMaintSleep(
									Integer.parseInt(start.getAttributeByName(new QName("", "maintSleep")).getValue()));
						}

						if (start.getAttributeByName(new QName("", "nagle")) != null) {
							socketnode.setNagle(
									Boolean.parseBoolean(start.getAttributeByName(new QName("", "nagle")).getValue()));
						}

						if (start.getAttributeByName(new QName("", "socketTO")) != null) {
							socketnode.setSocketTo(
									Integer.parseInt(start.getAttributeByName(new QName("", "socketTO")).getValue()));
						}

						if (start.getAttributeByName(new QName("", "maxIdle")) != null) {
							socketnode.setMaxIdle(
									Integer.parseInt(start.getAttributeByName(new QName("", "maxIdle")).getValue()));
						}

						if (start.getAttributeByName(new QName("", "aliveCheck")) != null)
							;
						socketnode.setAliveCheck(
								Boolean.parseBoolean(start.getAttributeByName(new QName("", "aliveCheck")).getValue()));
					}

					if ((tag.equalsIgnoreCase("servers")) && (socketnode != null)) {
						event = r.peek();

						if (!event.isCharacters())
							continue;
						servers = event.asCharacters().getData();
						socketnode.setServers(servers);
						r.nextEvent();
					}

					if ((tag.equalsIgnoreCase("weights")) && (socketnode != null)) {
						event = r.peek();

						if (!event.isCharacters())
							continue;
						weights = event.asCharacters().getData();
						socketnode.setWeights(weights);
						r.nextEvent();
					}

					if (tag.equalsIgnoreCase("cluster")) {
						clusternode = new MemcachedClientClusterConfig();

						if (start.getAttributeByName(new QName("", "name")) != null)
							clusternode.setName(start.getAttributeByName(new QName("", "name")).getValue());
						else {
							throw new RuntimeException("memcached cluster name can't not be null!");
						}
						if (start.getAttributeByName(new QName("", "mode")) != null)
							;
						clusternode.setMode(start.getAttributeByName(new QName("", "mode")).getValue());
					}

					if ((tag.equalsIgnoreCase("memCachedClients")) && (clusternode != null)) {
						event = r.peek();

						if (!event.isCharacters())
							continue;
						String clients = event.asCharacters().getData();

						if ((clients != null) && (!clients.equals(""))) {
							clusternode.setMemCachedClients(clients.split(","));
						}
						r.nextEvent();
					}

				}

				if (event.isEndElement()) {
					EndElement end = event.asEndElement();
					
					if ((node != null) && (end.getName().getLocalPart().equalsIgnoreCase("client"))) {
						memcachedClientconfigs.add(node);
						Logger.info(" add memcachedClient config :"+node.getName());
					}
					
					if ((socketnode != null) && (end.getName().getLocalPart().equalsIgnoreCase("socketpool"))) {
						memcachedClientSocketPoolConfigs.add(socketnode);
						Logger.info(" add socketpool config :"+socketnode.getName());
					}
					
					if ((clusternode != null) && (end.getName().getLocalPart().equalsIgnoreCase("cluster"))) {
						memcachedClientClusterConfig.add(clusternode);
						Logger.info(" add cluster config :"+clusternode.getName());
					}
				}
			}

			try {
				if (r != null) {
					r.close();
				}
				if (in != null) {
					in.close();
				}
				r = null;
				in = null;
			} catch (Exception ex) {
				throw new RuntimeException("processConfigURL error !", ex);
			}
		} catch (Exception ex) {
			Logger.error("MemcachedManager loadConfig error !" + " config url :" + url.getFile());
			try {
				if (r != null) {
					r.close();
				}
				if (in != null) {
					in.close();
				}
				r = null;
				in = null;
			} catch (Exception ex2) {
				throw new RuntimeException("processConfigURL error !", ex2);
			}
		} finally {
			try {
				if (r != null) {
					r.close();
				}
				if (in != null) {
					in.close();
				}
				r = null;
				in = null;
			} catch (Exception ex) {
				throw new RuntimeException("processConfigURL error !", ex);
			}
		}
	}
}
