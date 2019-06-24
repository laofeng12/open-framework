package org.ljdp.support.attach.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ljdp.fileupload")
public class LjdpFileuploadConfig {

	private String localPath;
	private String vedioBucket;
	private String audioBucket;
	private String imageBucket;
	private String chatBucket;

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getVedioBucket() {
		return vedioBucket;
	}

	public void setVedioBucket(String vedioBucket) {
		this.vedioBucket = vedioBucket;
	}

	public String getAudioBucket() {
		return audioBucket;
	}

	public void setAudioBucket(String audioBucket) {
		this.audioBucket = audioBucket;
	}

	public String getImageBucket() {
		return imageBucket;
	}

	public void setImageBucket(String imageBucket) {
		this.imageBucket = imageBucket;
	}

	public String getChatBucket() {
		return chatBucket;
	}

	public void setChatBucket(String chatBucket) {
		this.chatBucket = chatBucket;
	}
}
