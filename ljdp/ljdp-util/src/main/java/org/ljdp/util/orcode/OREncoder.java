package org.ljdp.util.orcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

import org.ljdp.util.FileUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class OREncoder extends BaseZxingCoder{

	public OREncoder() {
		super();
	}
	
	public OREncoder(String character) {
		super(character);
	}
	
	public OREncoder(Map<EncodeHintType, Object> hints) {
		super();
		this.hints = hints;
	}

	/**
	 * 生成二维图片
	 * @param content 内容
	 * @param width
	 * @param height
	 * @param destImagePath 二维码存放路径（不包含文件名）
	 * @param destImageName 二维码图片名称（不包含格式扩展名）
	 * @param format 二维码图片格式（jpg，png）
	 * @return 返回二维码文件名称（不含路径）
	 * @throws WriterException
	 * @throws IOException
	 */
	public String encode(String content, int width, int height, String destImagePath, String destImageName,
			String format) throws WriterException, IOException {
		String fileName = destImageName + "." + format;
		File f = new File(FileUtils.joinDirectory(destImagePath, fileName));
		if (f.exists()) {
			System.out.println("二维码文件已存在：" + fileName);
			return fileName;
		}
		
		BitMatrix bitMatrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵

		Path nioPath = FileSystems.getDefault().getPath(destImagePath, fileName);
		MatrixToImageWriter.writeToPath(bitMatrix, format, nioPath);// 输出图像
		return fileName;
	}

}
