package org.ljdp.common.file.nameservice;

import java.util.Date;

import org.ljdp.component.namespace.FileNameService;
import org.ljdp.util.DateFormater;
import org.ljdp.util.FileUtils;

public class FileTimeNameService implements FileNameService {

	public String createFileName(String origFileName, String operCode) {
		String name = origFileName;
		String time = DateFormater.formatDatetime_SHORT(new Date());
		String ext = FileUtils.getExtName(origFileName);
		int ei = origFileName.indexOf(ext);
		if(ei > 0 ) {
			name = origFileName.substring(0, ei) + "_" + time + ext;
		} else {
			name = origFileName + "." + time;
		}
		return name;
	}

}
