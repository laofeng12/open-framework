package org.ljdp.common.oss;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import org.ljdp.util.FileUtils;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.model.CopyObjectResult;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;

/**
 * 华为云OBS服务
 * @author hzy0769
 *
 */
public class HwObsClient {
	private String bucketName;
	private String accessKey;
	private String accessKeySecret;
	private String endpoint;
	private ObsClient client;
	
	public HwObsClient(String accessKey, String accessKeySecret, String endpoint, String bucketName) {
		super();
		this.accessKey = accessKey;
		this.accessKeySecret = accessKeySecret;
		this.endpoint = endpoint;
		this.bucketName = bucketName;
	}
	
	public synchronized ObsClient createClient() {
		if(client == null) {			
			// 创建ObsConfiguration配置类实例
			ObsConfiguration config = new ObsConfiguration();
			config.setEndPoint(endpoint);
			config.setSocketTimeout(30000);
			config.setMaxErrorRetry(1);
			
			// 创建ObsClient实例
			client = new ObsClient(accessKey, accessKeySecret, config);
		}
		return client;
	}
	
	public PutObjectResult putFile(String fileName) {
		String key = FileUtils.getFileName(fileName);
		ObsClient c = createClient();
		PutObjectResult res = c.putObject(bucketName, key, new File(fileName));
		return res;
	}
	public PutObjectResult putFile(String objectkey, String fileName) {
		ObsClient c = createClient();
		PutObjectResult res = c.putObject(bucketName, objectkey, new File(fileName));
		return res;
	}
	
	public PutObjectResult putBytes(String objectkey, byte[] contents) {
		ObsClient c = createClient();
		PutObjectResult res = c.putObject(bucketName, objectkey, new ByteArrayInputStream(contents));
		return res;
	}
	public PutObjectResult putBytes(String bucketName, String objectkey, byte[] contents) {
		ObsClient c = createClient();
		PutObjectResult res = c.putObject(bucketName, objectkey, new ByteArrayInputStream(contents));
		return res;
	}
	
	public PutObjectResult putStream(String objectkey, FileInputStream fis) {
		ObsClient c = createClient();
		PutObjectResult res = c.putObject(bucketName, objectkey, fis);
		return res;
	}
	
	public DeleteObjectResult delete(String objectkey) {
		ObsClient c = createClient();
		DeleteObjectResult res = c.deleteObject(bucketName, objectkey);
		return res;
	}
	
	public CopyObjectResult copy(String sourceobjectkey, String destobjectkey) {
		ObsClient c = createClient();
		CopyObjectResult result = c.copyObject(bucketName, sourceobjectkey, bucketName, destobjectkey);
		return result;
	}
	
	public ObsObject getObject(String objectkey) {
		ObsClient c = createClient();
		ObsObject obsObject = c.getObject(bucketName, objectkey);
		return obsObject;
	}
	public ObsObject getObject(String bucketName, String objectkey) {
		ObsClient c = createClient();
		ObsObject obsObject = c.getObject(bucketName, objectkey);
		return obsObject;
	}
	
	public synchronized void close() {
		try {
			createClient().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client = null;
	}
	
//	public static void main(String[] args) {
//		HwObsClient c = new HwObsClient("", "", "obs.cn-south-1.myhwclouds.com", "");
//		ObsObject obsObject = c.getObject("bookaudio3", "QuestionManager/20180418024731_我要更勇敢选择题一1.MP3");
//		System.out.println(obsObject);
//	}
}
