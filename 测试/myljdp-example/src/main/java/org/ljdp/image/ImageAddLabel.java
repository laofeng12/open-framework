package org.ljdp.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageAddLabel {
	
	/**
	 * 导入本地图片到缓冲区
	 * @throws IOException 
	 */
	public BufferedImage loadImageLocal(String imgName) throws IOException {
		return ImageIO.read(new File(imgName));
	}
	
	/**
	 * 生成新图片到本地
	 * @throws IOException 
	 */
	public void writeImageLocal(String newImage, BufferedImage img) throws IOException {
		if (newImage != null && img != null) {
			File outputfile = new File(newImage);
			ImageIO.write(img, "jpg", outputfile);
		}
	}
	
	/**
	 * 修改图片，添加文字,返回修改后的图片缓冲区
	 */
	public BufferedImage modifyImage(BufferedImage img, Font font, FontLabel[] labels) {
		Graphics2D g = img.createGraphics();
		g.setBackground(Color.WHITE);
		g.setColor(Color.BLACK);// 设置字体颜色
		g.setFont(font);
		for (int i = 0; i < labels.length; i++) {
			g.drawString(labels[i].getContent(), labels[i].getX(), labels[i].getY());
		}
		g.dispose();
		return img;
	}
	
	public static void main(String[] args) {
		ImageAddLabel tt = new ImageAddLabel();
		try {
			//读取模板文件
			BufferedImage source = tt.loadImageLocal("C:/temp/image/certificate2.jpg");
			
			Font font = new Font("微软雅黑", Font.PLAIN, 100);
			FontLabel label1 = new FontLabel("张三峰", 402, 910);
			FontLabel label2 = new FontLabel("通过等级7", 1690, 1410);
			FontLabel[] labels = {label1, label2};
			
			BufferedImage dest = tt.modifyImage(source, font, labels);
			
			tt.writeImageLocal("C:/temp/image/test.jpg", dest);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("success");
	}

}
