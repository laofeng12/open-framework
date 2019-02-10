package org.ljdp.common.file.nameservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ljdp.component.namespace.FileNameService;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.util.FileUtils;

/**
 * 按日期时间对分类文件名
 * @author Hzy
 *
 */
public class DateClassifyNameService implements FileNameService {

	public String createFileName(String origFileName, String operCode) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dir = sdf.format(new Date());
		String ext = FileUtils.getExtName(origFileName);
		SequenceService cs = ConcurrentSequence.getInstance();
		String newFilename = cs.getSequence("") + ext;
		String newLocation = "/" + dir +"/";
		String newLocalFileName = newLocation + newFilename;
		return newLocalFileName;
	}

}
