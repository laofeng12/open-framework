package org.ljdp.support.attach.service;

import java.io.File;

import org.ljdp.support.attach.domain.BsImageFile;
import org.ljdp.support.attach.vo.AttachVO;

public interface FtpImageFileService extends BsImageFileService{

	public BsImageFile upload(File file, String btype, String bid, Integer seqno, Long userid) throws Exception;
	
	public BsImageFile upload(AttachVO fileVO, String btype, String bid, Integer seqno, Long userid) throws Exception;
}
