package org.ljdp.common.oss;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.ConfigFileFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

public class OssFileUtils {
	private static final String bucketName;
	private static final String accessKeyId;
	private static final String accessKeySecret;
	private static final String endpoint;
	
	static {
		ConfigFile cfg = ConfigFileFactory.getInstance().getAppConfig();
		bucketName = cfg.getValue("aliyun_bucketName");
		accessKeyId = cfg.getValue("aliyun_accessKeyId");
		accessKeySecret = cfg.getValue("aliyun_accessKeySecret");
		endpoint = cfg.getValue("aliyun_endpoint");
	}
	
	private static OSSClient client;
	
	private static OSSClient getClient() {
		if(client == null) {
			client = new OSSClient(endpoint, accessKeyId,accessKeySecret);
		}
		return client;
	}
	
	public static void shutdown() {
		getClient().shutdown();
	}

	public static boolean delete(String key) {
		try {
			OSSClient client = getClient();
			client.deleteObject(bucketName, key);
			return true;
		} catch (Exception client) {
		}
		return false;
	}

	public static boolean upload(byte[] content, String uniqueFileName)
	  {
	    InputStream inputStream = null;
	    try {
	      inputStream = new ByteArrayInputStream(content); 

	      ObjectMetadata objectMeta = new ObjectMetadata();
	      objectMeta.setContentLength(content.length);

	      OSSClient client = getClient();
	      client.putObject(bucketName, uniqueFileName, inputStream, objectMeta);

	      return true;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	try{
			      if(inputStream != null){
			    	  inputStream.close();
			      }
	    	}catch(Exception e){
	    		
	    	}
	    }
	    return false;
	  }
	
	  public static boolean upload(File srcFile, String uniqueFileName)
	  {
	    InputStream inputStream = null;
	    try {
	      inputStream = new FileInputStream(srcFile);

	      ObjectMetadata objectMeta = new ObjectMetadata();
	      objectMeta.setContentLength(srcFile.length());

	      OSSClient client = getClient();
	      client.putObject(bucketName, uniqueFileName, inputStream, objectMeta);

	      return true;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	try{
			      if(inputStream != null){
			    	  inputStream.close();
			      }
	    	}catch(Exception e){
	    		
	    	}
	    }
	    return false;
	  }

	  
	  public static boolean download(String key, File targetFile)
	  {
	    OutputStream outputStream = null;
	    try {
	    	if(!targetFile.getParentFile().exists()){
	    		targetFile.getParentFile().mkdir();
	    	}
	    	if(!targetFile.exists()){
	    		targetFile.createNewFile();
	    	}
	      outputStream = new FileOutputStream(targetFile);

	      OSSClient client = getClient();
	      OSSObject object = client.getObject(bucketName, key);
	      InputStream inputStream = object.getObjectContent();
	      IOUtils.copy(inputStream, outputStream);
	      return true;
	    } catch (Throwable e) {
	      e.printStackTrace();
	    } finally {
	      IOUtils.closeQuietly(outputStream);
	    }
	    return false;
	  }
	  
}
