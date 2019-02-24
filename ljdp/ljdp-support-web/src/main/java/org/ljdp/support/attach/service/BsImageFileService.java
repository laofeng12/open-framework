package org.ljdp.support.attach.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.Part;

import org.ljdp.support.attach.domain.BsImageFile;
import org.springframework.data.domain.Pageable;

/**
 * 图片服务接口
 * @author hzy
 *
 */
public interface BsImageFileService {
	
	public BsImageFile get(Long id);
	
	/**
	 * 上传图片至oss
	 * @param fileItem 通过servlet上传的文件
	 */
	public BsImageFile upload(Part fileItem, String btype, String bid, Integer seqno, Long userid) throws Exception;
	
	/**
	 * 上传图片至oss
	 * @param attachId 临时上传的文件ID
	 */
	public BsImageFile upload(String attachId, String btype, String bid, Integer seqno, Long userid) throws Exception;
	
	public void removeById(Long imgId) throws Exception;
	
	public List<BsImageFile> queryByBtypeAndBid(String btype, String bid);
	
	public List<BsImageFile> queryByBid(String bid);
	
	public void removeByTypeAndBId(String btype, String bid) throws Exception;
	
	public void removeByBId(String bid) throws Exception;
	
	public void delete(BsImageFile img);
	
	/**
	 * 上传指定规格的图片n0(最大图)、n1(350*350px)、n2(160*160px)、n3(130*130px)、n4(100*100px) 
	 * @param img
	 */
	public void uploadPicUseResize(BsImageFile img, String contentType, int wh) throws Exception;
	/**
	 * 获取n4(100*100px)规格的图片
	 * @param btype
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	public BsImageFile getFirstN4ImageFile(String btype, String bid) throws Exception;
	/**
	 * 获取指定规格的图片n1(350*350px)、n2(160*160px)、n3(130*130px)、n4(100*100px) 
	 * @param btype
	 * @param bid
	 * @param wh
	 * @return
	 * @throws Exception
	 */
	public BsImageFile getFirstSmallImageFile(String btype, String bid, int wh) throws Exception;
	
	/**
	 * 更新附件记录所有关联的具体业务id
	 * @param fid
	 * @param bid
	 * @return
	 */
	public int doUpdateBid(Long fid, String bid);
	
	/**
	 * 查找bid是空的记录，通常是上传后没有做保存提交。
	 * @param creatime
	 * @param pageable
	 * @return
	 */
	public List<BsImageFile> queryByBidIsNull(Date creatime, Pageable pageable);
	
	List<BsImageFile> queryByObjectkeyIsNull(Pageable pageable);
	
	public BsImageFile findByObjectkey(String objectkey);
	
	public void doSave(BsImageFile f);
	
	/**
	 * 查询在时间点后上传的文件
	 * @param btype
	 * @param creatime
	 * @return
	 */
	List<BsImageFile> queryByBypeAndGeTime(String btype, Date creatime);
}
