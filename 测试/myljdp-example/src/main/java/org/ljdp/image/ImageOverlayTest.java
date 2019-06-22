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
	 * @Title: ����ͼƬ
	 * @Description: ����ˮӡ������java.awt.image.BufferedImage
	 * @param file
	 *            Դ�ļ�(ͼƬ)
	 * @param waterFile
	 *            ˮӡ�ļ�(ͼƬ)
	 * @param x
	 *            �������½ǵ�Xƫ����
	 * @param y
	 *            �������½ǵ�Yƫ����
	 * @param alpha
	 *            ͸����, ѡ��ֵ��0.0~1.0: ��ȫ͸��~��ȫ��͸��
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage watermark(File file, File waterFile, int x, int y, float alpha) throws IOException {
		// ��ȡ��ͼ
		BufferedImage buffImg = ImageIO.read(file);
		// ��ȡ��ͼ
		BufferedImage waterImg = ImageIO.read(waterFile);
		// ����Graphics2D�������ڵ�ͼ�����ϻ�ͼ
		Graphics2D g2d = buffImg.createGraphics();
		int waterImgWidth = waterImg.getWidth();// ��ȡ��ͼ�Ŀ��
		int waterImgHeight = waterImg.getHeight();// ��ȡ��ͼ�ĸ߶�
		// ��ͼ�κ�ͼ����ʵ�ֻ�Ϻ�͸��Ч��
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// ����
		g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
		g2d.dispose();// �ͷ�ͼ��������ʹ�õ�ϵͳ��Դ
		return buffImg;
	}

	/**
	 * ���ˮӡͼƬ
	 * 
	 * @param buffImg
	 *            ͼ���ˮӡ֮���BufferedImage����
	 * @param savePath
	 *            ͼ���ˮӡ֮��ı���·��
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
	 *             IO�쳣ֱ���׳���
	 * @author bls
	 */
	public static void main(String[] args) throws IOException {
//		String sourceFilePath = "body.png";
//		String waterFilePath = "ears_left.png";
//		String saveFilePath = "test2.png";
//		ImageOverlayTest newImageUtils = new ImageOverlayTest();
//		// �������Ӳ�
//		BufferedImage buffImg = ImageOverlayTest.watermark(new File(sourceFilePath), new File(waterFilePath)
//				, 0, 0, 1.0f);
//		// ���ˮӡͼƬ
//		newImageUtils.generateWaterFile(buffImg, saveFilePath);
		
		testMulti();
//		for(int i=0;i<10;i++) {
//			System.out.println(RandomUtils.nextInt(0, 2));
//		}
		
	}

	public static void testMulti() throws IOException {
		String sourceFilePath = "D:/liumugame/des01/bgwhite.jpg";
		//�������
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
		//��ͬ���Ŀ¼
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
			String saveFilePath = "D:/liumugame/���/"+i+".png";
			if(i == waterFiles.length) {
				//���һ������
				saveFilePath = "D:/liumugame/���/"+System.currentTimeMillis()+".png";
			}
			ImageOverlayTest newImageUtils = new ImageOverlayTest();
//			// �������Ӳ�
			BufferedImage buffImg = ImageOverlayTest.watermark(new File(sourceFilePath), new File(waterFilePath)
					, 0, 0, 1.0f);
			// ���ˮӡͼƬ
			newImageUtils.generateWaterFile(buffImg, saveFilePath);
			sourceFilePath = saveFilePath;
		}
	}
}
