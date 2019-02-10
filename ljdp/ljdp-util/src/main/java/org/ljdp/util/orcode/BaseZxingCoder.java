package org.ljdp.util.orcode;

import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class BaseZxingCoder {
	// 二维码写码器
	protected MultiFormatWriter mutiWriter;
	protected Map<EncodeHintType, Object> hints;//编码参数

	public BaseZxingCoder() {
		mutiWriter = new MultiFormatWriter();
		hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	}

	public BaseZxingCoder(String character) {
		mutiWriter = new MultiFormatWriter();
		hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, character);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	}
}
