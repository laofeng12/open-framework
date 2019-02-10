package org.ljdp.component.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.ljdp.util.TimeUtils;

public class Session implements Serializable {
	private static final long serialVersionUID = -6580261726632626339L;
	private String id;
	private long lastAccessedTime;//最近访问时间(毫秒)
	private HashMap<String, Serializable> attributes;
	private long maxInactiveInterval = 0;//最大存活时间（秒）小于0则Sessin永远有效，等于0则Session马上无效

	public Session(String id) {
		this.id = id;
		this.lastAccessedTime = System.currentTimeMillis();
		attributes = new HashMap<String, Serializable>();
	}
	
	public String getId() {
		return id;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}
	
	public void setAttribute(String key, Serializable value) {
		attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public Iterator<String> getAttributeNames(){
		return attributes.keySet().iterator();
	}
	
	public boolean isOverdue() {
		return TimeUtils.isOverdueSecond(this.lastAccessedTime, maxInactiveInterval);
	}
	
	public void updateConnectTime() {
		this.lastAccessedTime = System.currentTimeMillis();
	}

	public long getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}
	
	public void invalidate() {
		maxInactiveInterval = 0;
	}
	
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && Session.class.isAssignableFrom(obj.getClass())) {
			Session s = (Session) obj;
			equals = new EqualsBuilder().append(this.id, s.getId()).isEquals();
		}
		return equals;
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.id).toHashCode();
	}
	
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", this.id).toString();
	}
}
