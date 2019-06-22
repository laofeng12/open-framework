package org.ljdp.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomUtils;

public class ImageOverlayTest {
	/**
	 * 
	 * @Title: 构造图片
	 * @Description: 生成水印并返回java.awt.image.BufferedImage
	 * @param file
	 *            源文件(图片)
	 * @param waterFile
	 *            水印文件(图片)
	 * @param x
	 *            距离右下角的X偏移量
	 * @param y
	 *            距离右下角的Y偏移量
	 * @param alpha
	 *            透明度, 选择值从0.0~1.0: 完全透明~完全不透明
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage watermark(File file, File waterFile, int x, int y, float alpha) throws IOException {
		// 获取底图
		BufferedImage buffImg = ImageIO.read(file);
		// 获取层图
		BufferedImage waterImg = ImageIO.read(waterFile);
		// 创建Graphics2D对象，用在底图对象上绘图
		Graphics2D g2d = buffImg.createGraphics();
		int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
		int waterImgHeight = waterImg.getHeight();// 获取层图的高度
		// 在图形和图像中实现混合和透明效果
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// 绘制
		g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
		g2d.dispose();// 释放图形上下文使用的系统资源
		return buffImg;
	}

	/**
	 * 输出水印图片
	 * 
	 * @param buffImg
	 *            图像加水印之后的BufferedImage对象
	 * @param savePath
	 *            图像加水印之后的保存路径
	 */
	private void generateWaterFile(BufferedImage buffImg, String savePath) {
		int temp = savePath.lastIndexOf(".") + 1;
		try {
			ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 *             IO异常直接抛出了
	 * @author bls
	 */
	public static void main(String[] args) throws IOException {
//		String sourceFilePath = "body.png";
//		String waterFilePath = "ears_left.png";
//		String saveFilePath = "test2.png";
//		ImageOverlayTest newImageUtils = new ImageOverlayTest();
//		// 构建叠加层
//		BufferedImage buffImg = ImageOverlayTest.watermark(new File(sourceFilePath), new File(waterFilePath)
//				, 0, 0, 1.0f);
//		// 输出水印图片
//		newImageUtils.generateWaterFile(buffImg, saveFilePath);
		
		testMulti();
//		for(int i=0;i<10;i++) {
//			System.out.println(RandomUtils.nextInt(0, 2));
//		}
		
	}

	public static void testMulti() throws IOException {
		String sourceFilePath = "D:/liumugame/des01/bgwhite.jpg";
		//身体组件
		String[] comFiles = {
				"tail.png",
				"body.png",
				"beard_left.png",
				"beard_right.png",
				"belly.png",
				"ears_left.png",
				"ears_right.png",
				"eye_left.png",
				"eye_right.png",
				"eyebrow_left.png",
				"eyebrow_right.png",
				"hair.png",
				"mouth.png",
				"nose.png",
		};
		//不同风格目录
		String[] destDir = {
				"D:/liumugame/des01/",
				"D:/liumugame/des02/"};
		String[] waterFiles = new String[comFiles.length];
		for (int i= 0; i<comFiles.length; i++) {
			String t = destDir[RandomUtils.nextInt(0, 2)]+comFiles[i];
			waterFiles[i] = t;
		}
		for (int i= 1; i<=waterFiles.length; i++) {
			String waterFilePath = waterFiles[i-1];
			String saveFilePath = "D:/liumugame/输出/"+i+".png";
			if(i == waterFiles.length) {
				//最后一个步骤
				saveFilePath = "D:/liumugame/结果/"+System.currentTimeMillis()+".png";
			}
			ImageOverlayTest newImageUtils = new ImageOverlayTest();
//			// 构建叠加层
			BufferedImage buffImg = ImageOverlayTest.watermark(new File(sourceFilePath), new File(waterFilePath)
					, 0, 0, 1.0f);
			// 输出水印图片
			newImageUtils.generateWaterFile(buffImg, saveFilePath);
			sourceFilePath = saveFilePath;
		}
	}
}
