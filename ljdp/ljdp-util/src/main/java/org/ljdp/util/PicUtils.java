package org.ljdp.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;

public class PicUtils {
	public static byte[] resize(byte[] source, String format, int targetW, int targetH, boolean flag) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// ByteArrayInputStream in = new ByteArrayInputStream(source);
			// //将b作为输入流；
			BufferedImage image = getBufferedImage(source);// ImageIO.read(in);
			BufferedImage result = resize(image, targetW, targetH, flag);
			ImageIO.write(result, format, out);
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static BufferedImage getBufferedImage(byte[] file) {
		Image image = Toolkit.getDefaultToolkit().createImage(file);
		return getBufferedImage(image);
	}

	public static BufferedImage getBufferedImage(String filename) {
		Image image = Toolkit.getDefaultToolkit().getImage(filename);
		return getBufferedImage(image);
	}

	public static BufferedImage getBufferedImage(Image image) {
		return getBufferedImage(image, false);
	}

	public static BufferedImage getBufferedImage(Image image, boolean hasAlpha) {

		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels
		// boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.TRANSLUCENT;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			// int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	public static byte[] compress(byte[] data, int widthdist, int heightdist) {

		// ByteArrayInputStream is = new ByteArrayInputStream(data);

		BufferedImage src = null;
		ByteArrayOutputStream out = null;

		try {
			src = getBufferedImage(data);// ImageIO.read(is);
			out = new ByteArrayOutputStream(data.length);
			// 为等比压缩计算输出的宽高
			double rate1 = ((double) src.getWidth(null)) / (double) widthdist + 0.1;
			double rate2 = ((double) src.getHeight(null)) / (double) heightdist + 0.1;
			double rate = rate1 > rate2 ? rate1 : rate2;
			int new_w = (int) (((double) src.getWidth(null)) / rate);
			int new_h = (int) (((double) src.getHeight(null)) / rate);
			// 设定宽高
			BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			// 设定文件扩展名
			// String filePrex = oldFile
			// .substring(0, oldFile.lastIndexOf('.'));
			// newFile = filePrex + "SCALE_AREA_AVERAGING"
			// + oldFile.substring(filePrex.length());
			// 生成图片
			// 两种方法,效果与质量都相同,效率差不多
			// tag.getGraphics().drawImage(src.getScaledInstance(widthdist,heightdist,Image.SCALE_SMOOTH),
			// 0, 0, null);
			tag.getGraphics().drawImage(src.getScaledInstance(new_w, new_h, Image.SCALE_AREA_AVERAGING), 0, 0, null);

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.flush();
			out.close();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage resize(BufferedImage source, int targetW, int targetH, boolean flag) {
		// targetW，targetH分别表示目标长和宽
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		// 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
		// 则将下面的if else语句注释即可
		if (flag) {
			if (sx > sy) {
				sx = sy;
				targetW = (int) (sx * source.getWidth());
			} else {
				sy = sx;
				targetH = (int) (sy * source.getHeight());
			}
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	public static String getImageType(byte[] data) throws IOException {

		String type = "";
		ByteArrayInputStream bais = null;
		MemoryCacheImageInputStream mcis = null;
		try {
			bais = new ByteArrayInputStream(data);
			mcis = new MemoryCacheImageInputStream(bais);
			Iterator itr = ImageIO.getImageReaders(mcis);
			while (itr.hasNext()) {
				ImageReader reader = (ImageReader) itr.next();
				if (reader instanceof PNGImageReader) {
					type = "png";
				} else if (reader instanceof BMPImageReader) {
					type = "bmp";
				} else {
					type = "jpg";
				}
			}
		} finally {
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException ioe) {

				}
			}

			if (mcis != null) {
				try {
					mcis.close();
				} catch (IOException ioe) {

				}
			}
		}
		return type;
	}

	public static byte[] compressPic(byte[] data) {

		ByteArrayInputStream is = new ByteArrayInputStream(data);

		BufferedImage src = null;
		ByteArrayOutputStream out = null;
		ImageWriter imgWrier;
		ImageWriteParam imgWriteParams;

		byte[] result = null;
		try {
			String type = getImageType(data);
			// 指定写图片的方式为 jpg
			imgWrier = ImageIO.getImageWritersByFormatName(type).next();
			imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
			// 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
			imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			// 这里指定压缩的程度，参数qality是取值0~1范围内，
			imgWriteParams.setCompressionQuality((float) 0.5);// (float)0.5/data.length

			imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
			ColorModel colorModel = ColorModel.getRGBdefault();
			// 指定压缩时使用的色彩模式
			imgWriteParams.setDestinationType(
					new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

			src = getBufferedImage(data);// ImageIO.read(is);
			out = new ByteArrayOutputStream(data.length);

			imgWrier.reset();
			// 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何 OutputStream构造
			imgWrier.setOutput(ImageIO.createImageOutputStream(out));
			// 调用write方法，就可以向输入流写图片
			imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);

			out.flush();
			out.close();
			is.close();
			result = out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void saveImageAsJpg(String fromFileStr, String saveToFileStr, int width, int hight) throws Exception {
		BufferedImage srcImage;
		// String ex =
		// fromFileStr.substring(fromFileStr.indexOf("."),fromFileStr.length());
		String imgType = "JPEG";
		if (fromFileStr.toLowerCase().endsWith(".png")) {
			imgType = "PNG";
		}
		// System.out.println(ex);
		File saveFile = new File(saveToFileStr);
		File fromFile = new File(fromFileStr);
		srcImage = getBufferedImage(fromFileStr);// ImageIO.read(fromFile);

		if (width > 0 || hight > 0) {
			srcImage = resize(srcImage, width, hight, true);
		}
		ImageIO.write(srcImage, imgType, saveFile);

	}

	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static void main(String argv[]) {
		try {
			// 参数1(from),参数2(to),参数3(宽),参数4(高)
			// PicUtils.saveImageAsJpg("/Users/work/1","/User/work/2",50,50);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
