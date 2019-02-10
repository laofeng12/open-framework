package org.ljdp.common.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.ljdp.component.workflow.Releaseable;

public class LjdpHttpsClient extends BaseHttpClient implements Releaseable {

	private int socketTimeout = 30 * 1000;
	private int connectTimeout = 3 * 1000;
	
	public LjdpHttpsClient() {
		this.attributes = new HashMap<String, Object>();
	}
	
	public LjdpHttpsClient(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}

	public LjdpHttpsClient(String baseURL) {
		this.attributes = new HashMap<String, Object>();
		this.baseURL = baseURL;
	}
	public LjdpHttpsClient(String baseURL, HashMap<String, Object> attributes) {
		this.attributes = attributes;
		this.baseURL = baseURL;
	}
	
	public void initSSLContext() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		this.httpclient = createSSLClient();
		this.requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(true).setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
	}
	
	public void initSSLContextTrustAll() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		this.httpclient = createSSLClientTrustAll();
		this.requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(true).setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
	}
	
	public void initSSLContext(String trustStoreFile, String clientStorePassword) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		this.httpclient = createSSLClientDefault(trustStoreFile, clientStorePassword);
		this.requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(true).setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
	}
	
	protected CloseableHttpClient createSSLClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		return createHttpClientBuilder().build();
	}

	private HttpClientBuilder createHttpClientBuilder() {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		if(attributes != null) {
			if(attributes.containsKey("maxConnTotal")) {
				Integer maxConnTotal = (Integer)attributes.get("maxConnTotal");
				httpClientBuilder.setMaxConnTotal(maxConnTotal);
			}
			if(attributes.containsKey("maxConnPerRoute")) {
				Integer maxConnPerRoute = (Integer)attributes.get("maxConnPerRoute");
				httpClientBuilder.setMaxConnPerRoute(maxConnPerRoute);
			}
			if(attributes.containsKey("socketTimeout")) {
				socketTimeout = (Integer)attributes.get("socketTimeout");
			}
			if(attributes.containsKey("connectTimeout")) {
				connectTimeout = (Integer)attributes.get("connectTimeout");
			}
		}
		return httpClientBuilder;
	}
	
	protected CloseableHttpClient createSSLClientTrustAll() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
//		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//			// 信任所有
//			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
////					System.out.println(authType);
////					for (int i = 0; i < chain.length; i++) {
////						System.out.println(chain[i].toString());
////					}
//				return true;
//			}
//		}).build();
//		X509TrustManager tm = new X509TrustManager() {
//			public void checkClientTrusted(X509Certificate[] xcs, String string) {}
//			public void checkServerTrusted(X509Certificate[] xcs, String string) {}
//			public X509Certificate[] getAcceptedIssuers() {return null;}
//		};
//		SSLContext sslContext = SSLContext.getInstance("TLS");
		
		
		SSLContext sslContext  = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {  
            @Override  
            public boolean isTrusted(X509Certificate[] chain, String authType)  
                    throws CertificateException {
//            	System.out.println(authType);
//				for (int i = 0; i < chain.length; i++) {
//					System.out.println(chain[i].toString());
//				}
                return true;  
            }
        }).build();
//		sslContext.init(null, new TrustManager[]{tm}, new java.security.SecureRandom());
		
//		System.out.println("-======信任所有SSL证书测试......NoopHostnameVerifier..");
		
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,NoopHostnameVerifier.INSTANCE);
		return createHttpClientBuilder().setSSLSocketFactory(sslsf).build();
	}
	
	protected CloseableHttpClient createSSLClientDefault(String trustStoreFile, String clientStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		// 载入 jks 文件 信任服务器证书
		KeyStore trustKs = getPublicKeyStore(trustStoreFile, clientStorePassword);
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustKs, null).build();
		
//		final TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(
//                TrustManagerFactory.getDefaultAlgorithm());
//        tmfactory.init(trustKs);
//        final TrustManager[] tms = tmfactory.getTrustManagers();
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//		sslContext.init(null, tms, null);
        
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		return createHttpClientBuilder().setSSLSocketFactory(sslsf).build();
	}

	protected KeyStore getPublicKeyStore(String trustStoreFile, String clientStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		FileInputStream fin=new FileInputStream(trustStoreFile);
		KeyStore ks=KeyStore.getInstance("jks");
		ks.load(fin, clientStorePassword.toCharArray());
		fin.close();
		return ks;
	}
}
