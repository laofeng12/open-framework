package org.ljdp.cache.memcached;

public class MemcachedClientConfig {
	private String name;
	private boolean compressEnable;
	private String defaultEncoding;
	private String errorHandler;
	private String socketPool;
	private String command;
	private String transcoder;
	private int defaultExpire = 0;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompressEnable() {
		return this.compressEnable;
	}

	public void setCompressEnable(boolean compressEnable) {
		this.compressEnable = compressEnable;
	}

	public String getDefaultEncoding() {
		return this.defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public String getErrorHandler() {
		return this.errorHandler;
	}

	public void setErrorHandler(String errorHandler) {
		this.errorHandler = errorHandler;
	}

	public String getSocketPool() {
		return this.socketPool;
	}

	public void setSocketPool(String socketPool) {
		this.socketPool = socketPool;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTranscoder() {
		return transcoder;
	}

	public void setTranscoder(String transcoder) {
		this.transcoder = transcoder;
	}

	public int getDefaultExpire() {
		return defaultExpire;
	}

	public void setDefaultExpire(int defaultExpire) {
		this.defaultExpire = defaultExpire;
	}

}
