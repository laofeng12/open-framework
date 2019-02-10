package org.ljdp.common.test;

public class MyData {
	
	private String name;

	public MyData(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MyData [name=" + name + "]";
	}
}
