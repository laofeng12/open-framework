package org.ljdp.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

//import javax.imageio.ImageIO;

public class ImageUtils {

	public static BufferedImage getImageFromCode(String textCode, int width, int height, 
			int interLine, boolean randomLocation, Color backColor,Color fontColor, Color lineColor) {
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		//随机操作对象
		Random r=new Random();
		//绘制背景
		g.setColor(backColor==null?getRandomColor(r):backColor);
		g.fillRect(0,0,width,height);
		//绘制干扰线
		if(interLine>0){
            int x=r.nextInt(4),y=0;
            int x1=width-r.nextInt(4),y1=0;
            for(int i=0;i<interLine;i++){
                g.setColor(lineColor==null?getRandomColor(r):lineColor);
                y=r.nextInt(height-r.nextInt(4));
                y1=r.nextInt(height-r.nextInt(4));
                g.drawLine(x,y,x1,y1);
            }
        }
		//写验证码
		int fsize=(int)(height*0.8);//字体大小为图片高度的80%
        int fx=0;
        int fy=fsize;
        g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,fsize));
        //写字符
        for(int i=0;i<textCode.length();i++){
        	fy=randomLocation?(int)((Math.random()*0.3+0.6)*height):fy;//每个字符高低是否随机
        	g.setColor(fontColor==null?getRandomColor(r):fontColor);
            g.drawString(textCode.charAt(i)+"",fx,fy);
            fx+=(width / textCode.length()) * (Math.random() * 0.3 + 0.8); //依据宽度浮动
        }
        
        //扭曲图片
        shearX(g, width, height, backColor);
        shearY(g, width, height, backColor);
        
        //添加噪点
        float yawpRate = 0.05f;// 噪声率
        int area = (int) (yawpRate * width * height);//噪点数量
        for (int i = 0; i < area; i++) {
            int xxx = r.nextInt(width);
            int yyy = r.nextInt(height);
            int rgb = getRandomColor(r).getRGB();
            image.setRGB(xxx, yyy, rgb);
        }
        g.dispose();
        return image;
	}
	private static void shearX(Graphics g, int w1, int h1, Color color) {
        Random random=new Random();
        int period = 2;
 
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
           double d = (double) (period >> 1)* Math.sin((double) i / (double) period + (2.2831853071795862D * (double) phase)/ (double) frames);
           g.copyArea(0, i, w1, 1, (int) d, 0);
           if (borderGap) {
               g.setColor(color);
               g.drawLine((int) d, i, 0, i);
               g.drawLine((int) d + w1, i, w1, i);
           }
        }
	}
	private static void shearY(Graphics g, int w1, int h1, Color color) {
        Random random=new Random();
        int period = random.nextInt(40) + 10; // 50;
 
        boolean borderGap = true;
        int frames = 20;
        int phase = random.nextInt(2);
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (2.2831853071795862D * (double) phase)/ (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
 
        }
 
    }
	private static Color getRandomColor(Random random) {
		int r = Math.abs(random.nextInt(255));
		int g = Math.abs(random.nextInt(255));
		int b = Math.abs(random.nextInt(255));
		return new Color(r, g, b);
	}
	
//	public static void main(String[] args) {
//        String randomCode = RandomCode.getCode(4, null);
//        System.out.println(randomCode);
//        BufferedImage imageFromCode = ImageUtils.getImageFromCode(randomCode, 200, 80, 20, true, Color.WHITE, Color.BLACK, null);
//         try {
//            File file = new File("C:\\Users\\hzy07\\Pictures/test01.jpg");
//            ImageIO.write(imageFromCode,"jpg",file);
//            System.out.println("成功保存到："+file.getAbsolutePath());
//        } catch (IOException e) {
//            System.out.println("保存失败");
//            e.printStackTrace();
//        } 
//    }
}
