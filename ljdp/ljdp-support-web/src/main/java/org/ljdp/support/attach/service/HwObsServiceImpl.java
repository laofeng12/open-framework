package org.ljdp.support.attach.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.common.oss.HwObsClient;
import org.ljdp.common.oss.PicUrlUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.support.attach.component.HwcloudConfig;
import org.ljdp.support.attach.component.LjdpDfsUtils;
import org.ljdp.support.attach.component.LjdpFileuploadConfig;
import org.ljdp.support.attach.domain.BsImageFile;
import org.ljdp.support.attach.repository.BsImageFileRepository;
import org.ljdp.support.dictionary.DictConstants;
import org.ljdp.util.ByteUtil;
import org.ljdp.util.PicResize;
import org.ljdp.util.VedioUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obs.services.model.CopyObjectResult;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;

/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class HwObsServiceImpl implements HwObsService {
	
	@Resource
	private BsImageFileRepository bsImageFileRepository;
	@Resource
	private HwcloudConfig config;
	@Resource
	private LjdpDfsUtils dfsUtils;
	@Resource
	private LjdpFileuploadConfig fileuploadConfig;
	
	public BsImageFile get(Long id) {
		return bsImageFileRepository.findById(id).get();
	}
	
	public String uploadOnly(Part file, String bucketName) throws Exception {
		if(file == null){
			throw new APIException(-10002, "上传文件不能为空");
		}
		
		String extName = org.ljdp.util.FileUtils.getExtName_(file.getName());
		SequenceService cs = ConcurrentSequence.getInstance();
		String ossName = cs.getSequence("")+"."+extName;
		
		byte[] uploadContents = ByteUtil.input2byte(file.getInputStream());
		
		HwObsClient client = config.getObsClient();
		PutObjectResult oosres = client.putBytes(bucketName, ossName, uploadContents);
		if(oosres.getStatusCode() != 200) {
			throw new APIException(APIConstants.OSS_UPLOAD_FAIL, "上传OBS失败");
		}
		System.out.println("[OSS]upload:"+ossName+","+oosres);
		return ossName;
	}
	
	public BsImageFile upload(Part fileItem, String btype, String bid, Integer seqno, Long userid) throws Exception {
//		if(StringUtils.isBlank(btype)){
//			throw new APIException(-10001, "业务类型不能为空");
//		}
		if(fileItem == null){
			throw new APIException(-10002, "上传文件不能为空");
		}
		
		String filename = fileItem.getSubmittedFileName();
		int lastIndex = filename.lastIndexOf(".");
		if(lastIndex <= 0){
			throw new APIException(-10004, "上传文件名称格式有误");
		}
		String extName = org.ljdp.util.FileUtils.getExtName_(filename);
		
		SequenceService cs = ConcurrentSequence.getInstance();
		String ossName = cs.getSequence("")+"."+extName;
		
		byte[] uploadContents = ByteUtil.input2byte(fileItem.getInputStream());
		
		return doRemoteUpload(null, btype, bid, seqno, userid, filename, ossName, uploadContents);
	}
	
	public BsImageFile upload(String filename, InputStream inputStream, String btype, String bid, Integer seqno, Long userid) throws Exception {
//		if(StringUtils.isBlank(btype)){
//			throw new APIException(-10001, "业务类型不能为空");
//		}
		if(filename == null){
			throw new APIException(-10002, "上传文件不能为空");
		}
		
		int lastIndex = filename.lastIndexOf(".");
		if(lastIndex <= 0){
			throw new APIException(-10004, "上传文件名称格式有误");
		}
		String extName = org.ljdp.util.FileUtils.getExtName_(filename);
		
		SequenceService cs = ConcurrentSequence.getInstance();
		String ossName = cs.getSequence("")+"."+extName;
		
		byte[] uploadContents = ByteUtil.input2byte(inputStream);
		
		return doRemoteUpload(null, btype, bid, seqno, userid, filename, ossName, uploadContents);
	}
	
	/**
	 * 关联上传的附件与当前业务对象
	 */
	public BsImageFile relationObject(String objectkey, String btype, String bid, Integer seqno, Long userid) throws Exception {
		if(StringUtils.isBlank(objectkey)) {
			System.out.println("objectkey=空，可能漏了上传？btype="+btype);
			return null;
		}
		if(objectkey.startsWith("temp-")) {
			HwObsClient client = config.getObsClient();
			String destobjectkey = objectkey.replaceFirst("temp-", btype);
			CopyObjectResult copyRes = client.copy(objectkey, destobjectkey);
			System.out.println("[HWOBS]copy："+copyRes);
			DeleteObjectResult delRes = client.delete(objectkey);
			System.out.println("[HWOBS]delete："+delRes);
			
			BsImageFile img = new BsImageFile();
			img.setId(ConcurrentSequence.getInstance().getSequence());
			img.setBtype(btype);//图片类型
			img.setBid(bid);//业务编码
			img.setPicname(destobjectkey);//图片名称
			img.setPicurl(PicUrlUtils.OBS_PARAMS+"/"+destobjectkey);//图片路径
			img.setUserid(userid);//上传人
			if(seqno != null){
				img.setSeqno(seqno);//显示顺序
			}
			img.setCreatime(new Date());//上传时间
			img.setIsNew(true);
			bsImageFileRepository.save(img);
			return img;
		} else if(StringUtils.isNumeric(objectkey)) {
			BsImageFile img = bsImageFileRepository.getOne(new Long(objectkey));
			if(img == null) {
				throw new APIException(APIConstants.OSS_UPLOAD_FAIL, "找不到key:"+objectkey);
			}
			img.setBid(bid);
			img.setUserid(userid);//上传人
			if(StringUtils.isBlank(img.getBtype())) {
				img.setBtype(btype);
			}
			if(seqno != null){
				img.setSeqno(seqno);//显示顺序
			}
			img.setIsNew(false);
			bsImageFileRepository.save(img);
			return img;
		}
		throw new APIException(APIConstants.OSS_UPLOAD_FAIL, "无法识别key:"+objectkey);
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
		
		return doRemoteUpload(null, btype, bid, seqno, userid, filename, ossName, uploadContents);
	}

	private BsImageFile doRemoteUpload(String btype, String bucket, String bid, Integer seqno, Long userid, String filename,
			String ossName, byte[] uploadContents) throws APIException {
		HwObsClient client = config.getObsClient();
		if(StringUtils.isNotBlank(bucket)) {
			client = config.getObsClient(bucket);
		}
		PutObjectResult oosres = client.putBytes(ossName, uploadContents);
		if(oosres.getStatusCode() != 200) {			
			throw new APIException(APIConstants.OSS_UPLOAD_FAIL, "上传OBS失败");
		}
		System.out.println("[OSS]upload:"+ossName+","+oosres);
		
		BsImageFile img = new BsImageFile();
		img.setId(ConcurrentSequence.getInstance().getSequence());
		img.setBtype(btype);//图片类型
		img.setBucketname(bucket);
		img.setBid(bid);//业务编码
		img.setPicname(filename);//图片名称
		img.setPicurl(PicUrlUtils.OBS_PARAMS+"/"+ossName);//图片路径
		img.setUserid(userid);//上传人
		img.setObjectkey(ossName);
		if(seqno != null){
			img.setSeqno(seqno);//显示顺序
		}
		img.setCreatime(new Date());//上传时间
		img.setFilesize(new Long(uploadContents.length));
		if(filename.endsWith(".mp4") || filename.endsWith(".MP4")) {
			File tempvedio = ByteUtil.createFile(uploadContents, fileuploadConfig.getLocalPath(), ossName);
			long second = VedioUtil.readVideoTime(tempvedio);
			img.setDuration(second);
			tempvedio.deleteOnExit();
		}
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
			HwObsClient client = config.getObsClient();
			if(StringUtils.isNotBlank(img.getBucketname())) {
				client = config.getObsClient(img.getBucketname());
			}
			DeleteObjectResult res = client.delete(key);
			System.out.println("[OSS]delete:"+key+","+res);
			bsImageFileRepository.deleteById(img.getId());
//			if(res.getStatusCode() == 200) {
//			}
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
			HwObsClient client = config.getObsClient();
			PutObjectResult oosres = client.putBytes(smallurl, smallContents);
			if(oosres.getStatusCode() != 200) {			
				throw new APIException(APIConstants.OSS_UPLOAD_FAIL, "上传OBS失败");
			}
			
			System.out.println("成功上传小图："+smallurl);
			
			img.setPicurl(origImgPath);
			if(wh == 100) {
				img.setSmallpic(PicUrlUtils.OBS_PARAMS+"/"+smallurl);//图片路径
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
	
	public String downloadByObsKey(String key, String bucketName) throws Exception{
		HwObsClient client = config.getObsClient();
		ObsObject obsObject = client.getObject(bucketName, key);
		String fullFilePath = org.ljdp.util.FileUtils.joinDirectory(fileuploadConfig.getLocalPath(),key);
		InputStream input = obsObject.getObjectContent();
		File localsave = new File(fullFilePath);
		FileOutputStream output = new FileOutputStream(localsave);
		
		org.ljdp.util.FileUtils.copy(input, output);
		output.close();
		input.close();
		return fullFilePath;
	}
	
	public String downloadByObsKey2Local(String key, String localpath) throws Exception{
		return downloadByObsKey2Local(key, null, localpath);
	}
	public String downloadByObsKey2Local(String key, String bucketName, String localpath) throws Exception{
		HwObsClient client = config.getObsClient();
		if(StringUtils.isNotBlank(bucketName)) {
			client = config.getObsClient(bucketName);
		}
		ObsObject obsObject = client.getObject(key);
		String fullFilePath = org.ljdp.util.FileUtils.joinDirectory(localpath,key);
		InputStream input = obsObject.getObjectContent();
		File localsave = new File(fullFilePath);
		FileOutputStream output = new FileOutputStream(localsave);
		
		org.ljdp.util.FileUtils.copy(input, output);
		output.close();
		input.close();
		return fullFilePath;
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
}
