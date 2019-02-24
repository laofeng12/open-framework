package org.ljdp.support.attach.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.common.oss.OssFileUtils;
import org.ljdp.common.oss.PicUrlUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.support.attach.component.LjdpDfsUtils;
import org.ljdp.support.attach.domain.BsImageFile;
import org.ljdp.support.attach.repository.BsImageFileRepository;
import org.ljdp.support.dictionary.DictConstants;
import org.ljdp.util.ByteUtil;
import org.ljdp.util.PicResize;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class BsImageFileServiceImpl implements BsImageFileService {
	
	@Resource
	private BsImageFileRepository bsImageFileRepository;
	@Resource
	private LjdpDfsUtils dfsUtils;
	
	public BsImageFile get(Long id) {
		return bsImageFileRepository.findById(id).get();
	}
	
	public BsImageFile upload(Part fileItem, String btype, String bid, Integer seqno, Long userid) throws Exception {
		if(StringUtils.isBlank(btype)){
			throw new APIException(-10001, "业务类型不能为空");
		}
		if(fileItem == null){
			throw new APIException(-10002, "上传文件不能为空");
		}
		
		String filename = fileItem.getSubmittedFileName();
		int lastIndex = filename.lastIndexOf(".");
		if(lastIndex <= 0){
			throw new APIException(-10004, "上传文件名称格式有误");
		}
		
		SequenceService ss = TimeSequence.getInstance();
		String ossName = ss.getSequence(btype);
		
		byte[] uploadContents = ByteUtil.input2byte(fileItem.getInputStream());
		
		return doRemoteUpload(btype, bid, seqno, userid, filename, ossName, uploadContents);
	}

	public BsImageFile upload(String attachId, String btype, String bid, Integer seqno, Long userid) throws Exception {
		//获取上传后临时保存的文件
		FileUploadTask task = (FileUploadTask)MemoryCache.getData(
				DictConstants.CACHE_ATTACH, attachId);
		if(task == null) {
			throw new APIException(APIConstants.CODE_PARAM_ERR, "附件不存在");
		}
		if(StringUtils.isBlank(btype)){
			throw new APIException(-10001, "业务类型不能为空");
		}
		byte[] uploadContents = FileUtils.readFileToByteArray(new File(task.getSaveFileName()));
		String ossName = task.getNewFileName();
		String filename = task.getUploadFileName();
		
		return doRemoteUpload(btype, bid, seqno, userid, filename, ossName, uploadContents);
	}

	private BsImageFile doRemoteUpload(String btype, String bid, Integer seqno, Long userid, String filename,
			String ossName, byte[] uploadContents) throws APIException {
		boolean ossres = OssFileUtils.upload(uploadContents, ossName);
		if(!ossres) {
			throw new APIException(APIConstants.OSS_UPLOAD_FAIL, "上传OSS失败");
		}
		System.out.println("[OSS]upload:"+ossName+","+ossres);
		
		BsImageFile img = new BsImageFile();
		img.setId(ConcurrentSequence.getInstance().getSequence());
		img.setBtype(btype);//图片类型
		img.setBid(bid);//业务编码
		img.setPicname(filename);//图片名称
		img.setPicurl(PicUrlUtils.OSS_PARAMS+"/"+ossName);//图片路径
		img.setUserid(userid);//上传人
		img.setObjectkey(ossName);
		if(seqno != null){
			img.setSeqno(seqno);//显示顺序
		}
		img.setCreatime(new Date());//上传时间
		bsImageFileRepository.save(img);
		return img;
	}
	
	public void removeById(Long imgId) throws Exception{
		BsImageFile img = bsImageFileRepository.getOne(imgId);
		delete(img);
	}

	public void delete(BsImageFile img) {
		if(StringUtils.isNotEmpty(img.getPicurl())) {
			String key = dfsUtils.replaceUrlToEmpt(img.getPicurl());
			if(key.startsWith("/")) {
				key = key.substring(1, key.length());
			}
			boolean flag = OssFileUtils.delete(key);
			System.out.println("[OSS]delete:"+key+","+flag);
			bsImageFileRepository.deleteById(img.getId());
		}
	}
	
	public void removeByTypeAndBId(String btype, String bid) throws Exception{
		List<BsImageFile> list = bsImageFileRepository.queryByBtypeAndBid(btype, bid);
		for (BsImageFile img : list) {
			delete(img);
		}
	}
	
	public void removeByBId(String bid) throws Exception{
		List<BsImageFile> list = bsImageFileRepository.queryByBid(bid);
		for (BsImageFile img : list) {
			delete(img);
		}
	}
	
	public List<BsImageFile> queryByBtypeAndBid(String btype, String bid){
		List<BsImageFile> list = bsImageFileRepository.queryByBtypeAndBid(btype, bid);
		for (BsImageFile img : list) {
			img.setPicurl(dfsUtils.replaceUrl(img.getPicurl()));
		}
		return list;
	}
	
	public List<BsImageFile> queryByBid(String bid){
		List<BsImageFile> list = bsImageFileRepository.queryByBid(bid);
		for (BsImageFile img : list) {
			img.setPicurl(dfsUtils.replaceUrl(img.getPicurl()));
		}
		return list;
	}
	
	/**
	 * 上传指定规格的图片n0(最大图)、n1(350*350px)、n2(160*160px)、n3(130*130px)、n4(100*100px) 
	 * @param img
	 */
	public void uploadPicUseResize(BsImageFile img, String contentType, int wh) throws Exception{
		if(wh == 100) {
			if(StringUtils.isNotBlank(img.getSmallpic())) {
				return;
			}
		}
		String npath = "";
		if(wh == 100) {
			npath = "n4";
		}
//		String picviewUrl = PicUrlUtils.replaceUrl(img.getPicurl());
		String picviewUrl = img.getPicurl();
		LjdpHttpClient httpc = new LjdpHttpClient();
		HttpResponse httpResp = httpc.get(picviewUrl);
		
		byte[] origContents = ByteUtil.input2byte(httpResp.getEntity().getContent());
		if(origContents == null) {
			System.out.println("获取图片识别："+picviewUrl);
			return;
		}
		PicResize pr = new PicResize();
		byte[] smallContents = pr.resizeImage(origContents, contentType, wh, wh);
		
		String origImgPath = dfsUtils.replaceUrlToEmpt(img.getPicurl());
		
		if(origImgPath.startsWith("http")) {
			System.out.println("无法解析图片路径：id="+img.getId()+","+img.getPicurl());
		} else {
			String smallurl = npath+origImgPath;
			boolean ossres = OssFileUtils.upload(smallContents, smallurl);
			if(!ossres) {
				throw new APIException(APIConstants.OSS_UPLOAD_FAIL, "上传OSS失败");
			}
			
			System.out.println("成功上传小图："+smallurl);
			
			img.setPicurl(origImgPath);
			if(wh == 100) {
				img.setSmallpic(PicUrlUtils.OSS_PARAMS+"/"+smallurl);//图片路径
			}
			bsImageFileRepository.save(img);
		}
	}
	
	/**
	 * 获取n4(100*100px)规格的图片
	 * @param btype
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	public BsImageFile getFirstN4ImageFile(String btype, String bid) throws Exception {
		return getFirstSmallImageFile(btype, bid, 100);
	}
	
	/**
	 * 获取指定规格的图片n1(350*350px)、n2(160*160px)、n3(130*130px)、n4(100*100px) 
	 * @param btype
	 * @param bid
	 * @param wh
	 * @return
	 * @throws Exception
	 */
	public BsImageFile getFirstSmallImageFile(String btype, String bid, int wh) throws Exception {
		List<BsImageFile> imgList = queryByBtypeAndBid(btype, bid);
		if(imgList.isEmpty()) {
			return null;
		}
		BsImageFile img = imgList.get(0);
		
		int lastIndex = img.getPicname().lastIndexOf(".");
		if(lastIndex <= 0){
			throw new APIException(-10004, "上传文件名称格式有误");
		}

		String contentType = img.getPicname().substring(lastIndex).toLowerCase();
		contentType = contentType.replace(".", "");
		uploadPicUseResize(img, contentType, wh);
		return img;
	}
	
	public int doUpdateBid(Long fid, String bid) {
		return bsImageFileRepository.updateBid(fid, bid);
	}
	
	@Override
	public List<BsImageFile> queryByBidIsNull(Date creatime, Pageable pageable){
		return bsImageFileRepository.queryByBidIsNull(creatime, pageable);
	}
	@Override
	public List<BsImageFile> queryByObjectkeyIsNull(Pageable pageable){
		return bsImageFileRepository.queryByObjectkeyIsNull(pageable);
	}
	@Override
	public BsImageFile findByObjectkey(String objectkey) {
		return bsImageFileRepository.findByObjectkey(objectkey);
	}
	@Override
	public void doSave(BsImageFile f) {
		bsImageFileRepository.save(f);
	}
	@Override
	public List<BsImageFile> queryByBypeAndGeTime(String btype, Date creatime){
		return bsImageFileRepository.queryByBypeAndGeTime(btype, creatime);
	}
}
