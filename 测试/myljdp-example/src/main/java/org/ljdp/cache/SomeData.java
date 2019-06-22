package org.ljdp.cache;

import java.io.Serializable;

public class SomeData implements Serializable{
	
	private String value;
	
	public SomeData() {
		
	}

	public SomeData(String value) {
		super();
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SomeData [value=" + value + "]";
	}

}
