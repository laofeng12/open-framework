package org.ljdp.support.attach.service;

import java.io.InputStream;

import javax.servlet.http.Part;

import org.ljdp.support.attach.domain.BsImageFile;

public interface HwObsService extends BsImageFileService{

	/**
	 * 关联上传的附件与当前业务对象
	 * @param objectkey
	 * @param btype
	 * @param bid
	 * @param seqno
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public BsImageFile relationObject(String objectkey, String btype, String bid, Integer seqno, Long userid) throws Exception;
	
	/**
	 * 持久化保存临时存储在redis的文件对象
	 * @param objectkey
	 * @param btype
	 * @param bid
	 * @param seqno
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public BsImageFile saveUploadFromRedis(String objectkey, String btype, String bid, Integer seqno, Long userid) throws Exception;
	
	public String uploadOnly(Part file, String bucketName) throws Exception;
	public BsImageFile uploadOnly(Part fileItem, String btype, String bid, Integer seqno, Long userid) throws Exception;
	
	public BsImageFile upload(String filename, InputStream inputStream, String btype, String bid, Integer seqno, Long userid) throws Exception;
	
	
	public String downloadByObsKey(String key, String bucketName) throws Exception;
	
	public String downloadByObsKey2Local(String key,String localpath) throws Exception;
	public String downloadByObsKey2Local(String key, String bucketName, String localpath) throws Exception;
}
