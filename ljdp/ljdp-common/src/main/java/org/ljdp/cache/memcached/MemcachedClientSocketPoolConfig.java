package org.ljdp.cache.memcached;

public class MemcachedClientSocketPoolConfig {
	private String name;
	private boolean failover;
	private int initConn;
	private int minConn;
	private int maxConn;
	private int maintSleep;
	private boolean nagle;
	private int socketTo;
	private boolean aliveCheck;
	private int maxIdle;
	private String servers;
	private String weights;

	public MemcachedClientSocketPoolConfig() {
		this.failover = true;
		this.initConn = 10;
		this.minConn = 5;
		this.maxConn = 250;

		this.maintSleep = 3000;
		this.nagle = false;

		this.socketTo = 3000;

		this.aliveCheck = true;

		this.maxIdle = 3000;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFailover() {
		return this.failover;
	}

	public void setFailover(boolean failover) {
		this.failover = failover;
	}

	public int getInitConn() {
		return this.initConn;
	}

	public void setInitConn(int initConn) {
		this.initConn = initConn;
	}

	public int getMinConn() {
		return this.minConn;
	}

	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	public int getMaxConn() {
		return this.maxConn;
	}

	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	public int getMaintSleep() {
		return this.maintSleep;
	}

	public void setMaintSleep(int maintSleep) {
		this.maintSleep = maintSleep;
	}

	public boolean isNagle() {
		return this.nagle;
	}

	public void setNagle(boolean nagle) {
		this.nagle = nagle;
	}

	public int getSocketTo() {
		return this.socketTo;
	}

	public void setSocketTo(int socketTo) {
		this.socketTo = socketTo;
	}

	public boolean isAliveCheck() {
		return this.aliveCheck;
	}

	public void setAliveCheck(boolean aliveCheck) {
		this.aliveCheck = aliveCheck;
	}

	public String getServers() {
		return this.servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public String getWeights() {
		return this.weights;
	}

	public void setWeights(String weights) {
		this.weights = weights;
	}

	public int getMaxIdle() {
		return this.maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
}
