package org.ljdp.support.attach.component;

import java.util.HashMap;

import org.ljdp.common.oss.HwObsClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hwcloud")
public class HwcloudConfig {

	private String accessKey;
	private String accessKeySecret;
	private String endpoint;
	private String bucketname;
	
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getBucketname() {
		return bucketname;
	}
	public void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}
	
	private HwObsClient obsclient;
	private HashMap<String, HwObsClient> bucketClient = new HashMap<>();
	public synchronized HwObsClient getObsClient() {
		if(obsclient == null) {
			obsclient = new HwObsClient(accessKey, accessKeySecret, endpoint, bucketname);
		}
		return obsclient;
	}
	
	public synchronized HwObsClient getObsClient(String bucket) {
		if(bucketClient.containsKey(bucket)) {
			return bucketClient.get(bucket);
		}
		HwObsClient c = new HwObsClient(accessKey, accessKeySecret, endpoint, bucket);
		bucketClient.put(bucket, c);
		return c;
	}
}
