package org.ljdp.common.file.nameservice;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.namespace.FileNameService;
import org.ljdp.util.DateFormater;
import org.ljdp.util.FileUtils;

public class UserTimeNameService implements FileNameService {

	public String createFileName(String uploadFileName, String operCode){
		String head = operCode;
		if ( StringUtils.isBlank(head) ) {
            head = "";
        } else {
            head += "_";
        }
		String exname = FileUtils.getExtName(uploadFileName);
		String filename = head + DateFormater.formatDatetime_SHORT(new Date()) + exname;
		return filename;
	}

}
