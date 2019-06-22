package org.ljdp.image;

import java.awt.Font;

public class FontLabel {
	
	private Font font;
	private String content;
	private int x;
	private int y;
	
	public FontLabel(Font font, String content, int x, int y) {
		super();
		this.font = font;
		this.content = content;
		this.x = x;
		this.y = y;
	}

	public FontLabel(String content, int x, int y) {
		super();
		this.content = content;
		this.x = x;
		this.y = y;
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

}
