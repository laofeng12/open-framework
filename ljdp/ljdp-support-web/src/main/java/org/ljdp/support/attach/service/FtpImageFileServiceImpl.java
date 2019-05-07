package org.ljdp.support.attach.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.ftp.ApacheFTPClient;
import org.ljdp.common.ftp.FTPException;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.common.oss.PicUrlUtils;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.support.attach.component.LjdpDfsUtils;
import org.ljdp.support.attach.component.LjdpFtpConfig;
import org.ljdp.support.attach.domain.BsImageFile;
import org.ljdp.support.attach.repository.BsImageFileRepository;
import org.ljdp.support.attach.vo.AttachVO;
import org.ljdp.support.dictionary.DictConstants;
import org.ljdp.util.ByteUtil;
import org.ljdp.util.DateFormater;
import org.ljdp.util.FileUtils;
import org.ljdp.util.PicResize;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FTP远程文件服务器
 * @author hzy
 *
 */
@Service
@Transactional
public class FtpImageFileServiceImpl implements FtpImageFileService {
	@Resource
	private BsImageFileRepository bsImageFileRepository;
	@Resource
	private LjdpFtpConfig ftpConfig;
	@Resource
	private LjdpDfsUtils dfsUtils;
	
	private boolean showConfig = false;
	
	public BsImageFile get(Long id) {
		Optional<BsImageFile> o = bsImageFileRepository.findById(id);
		if(o.isPresent()) {			
			return o.get();
		}
		return null;
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
		String ext = FileUtils.getExtName_(filename);
		
		SequenceService ss = TimeSequence.getInstance();
		String saveName = ss.getSequence("")+"."+ext;
		String remotePath = FileUtils.joinDirectory(ftpConfig.getRemotePath(), btype, saveName);
		
		return doRemoteUpload(btype, bid, seqno, userid, filename, remotePath, fileItem.getInputStream(),
				new String[] {ftpConfig.getRemotePath(), btype, null});
	}
	
	public BsImageFile upload(AttachVO fileVO, String btype, String bid, Integer seqno, Long userid) throws Exception {
		if(StringUtils.isBlank(btype)){
			throw new APIException(-10001, "业务类型不能为空");
		}
		if(fileVO == null){
			throw new APIException(-10002, "上传文件不能为空");
		}
		
		String filename = fileVO.getName();
		int lastIndex = filename.lastIndexOf(".");
		if(lastIndex <= 0){
			throw new APIException(-10004, "上传文件名称格式有误");
		}
		String ext = FileUtils.getExtName_(filename);
		
		SequenceService ss = TimeSequence.getInstance();
		String saveName = ss.getSequence("")+"."+ext;
		String remotePath = FileUtils.joinDirectory(ftpConfig.getRemotePath(), btype, saveName);
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(fileVO.getContents());
		return doRemoteUpload(btype, bid, seqno, userid, filename, remotePath, inputStream,
				new String[] {ftpConfig.getRemotePath(), btype, null});
	}

	public BsImageFile upload(String attachId, String btype, String bid, Integer seqno, Long userid) throws Exception {
		RedisTemplate<String, Object> redisTemplate = (RedisTemplate)SpringContextManager.getBean("redisTemplate");
		if(redisTemplate != null) {
			AttachVO vo = (AttachVO)redisTemplate.boundValueOps(attachId).get();
			if(vo == null) {
				throw new APIException(APIConstants.CODE_PARAM_ERR, "附件不存在");
			}
			String filename = vo.getName();
			String ext = FileUtils.getExtName_(vo.getName());
			String newFilename = vo.getFid()+"."+ext;
			String classify = DateFormater.formatYM(new Date());
			String remoteFullName = FileUtils.joinDirectory(ftpConfig.getRemotePath(), btype, classify, newFilename);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(vo.getContents());
			return doRemoteUpload(btype, bid, seqno, userid, filename, remoteFullName, inputStream,
					new String[] {ftpConfig.getRemotePath(), btype, classify});
		} else {
			//获取上传后临时保存的文件
			FileUploadTask task = (FileUploadTask)MemoryCache.getData(
					DictConstants.CACHE_ATTACH, attachId);
			if(task == null) {
				throw new APIException(APIConstants.CODE_PARAM_ERR, "附件不存在");
			}
			if(StringUtils.isBlank(btype)){
				throw new APIException(-10001, "业务类型不能为空");
			}
			String filename = task.getUploadFileName();
			String remoteFullName = FileUtils.joinDirectory(ftpConfig.getRemotePath(), task.getContextPath(),task.getClassifyPath(),task.getNewFileName());
			InputStream input= new FileInputStream(task.getSaveFileName());
//		System.out.println("attachId="+attachId);
//		System.out.println("Local Path="+task.getSaveFileName());
//		System.out.println("Remote FTP Path="+remoteFullName);
			return doRemoteUpload(btype, bid, seqno, userid, filename, remoteFullName, input,
					new String[] {ftpConfig.getRemotePath(), task.getContextPath(),task.getClassifyPath()});
		}
	}
	
	public BsImageFile upload(File file, String btype, String bid, Integer seqno, Long userid) throws Exception {
		String filename = file.getName();
		String newFilename = filename;
		String classify = DateFormater.formatYM(new Date());
		String remoteFullName = FileUtils.joinDirectory(ftpConfig.getRemotePath(), btype, classify, newFilename);
		
		FileInputStream inputStream = new FileInputStream(file);
		
		return doRemoteUpload(btype, bid, seqno, userid, filename, remoteFullName, inputStream,
				new String[] {ftpConfig.getRemotePath(), btype, classify});
	}

	private BsImageFile doRemoteUpload(String btype, String bid, Integer seqno, Long userid, String filename,
			String remoteFullName, InputStream localInputStream,
			String[] directorys) throws APIException, FTPException, IOException {
		if(!showConfig) {
			showConfig = true;
			System.out.println(ftpConfig.toStringMultiLine());
		}
		ApacheFTPClient ftpclient = new ApacheFTPClient(ftpConfig.getUrl(),ftpConfig.getPort(), ftpConfig.getUsername(), ftpConfig.getPassword(), ftpConfig.getMode());
		
		//初始化目录
		if(StringUtils.isNotEmpty(directorys[0])) {
			ftpclient.changeWorkingDirectory(directorys[0]);
		}
		for(int i=1; i < directorys.length; i++) {
			String d = directorys[i];
			if(StringUtils.isNotEmpty(d)) {
				d = d.replaceAll("/", "");
				System.out.println("ftp mkdir: "+d);
				System.out.println(ftpclient.makeDirectory(d));
				System.out.println(ftpclient.changeWorkingDirectory(d));
			}
		}
		
		boolean ossres = ftpclient.uploadFile(remoteFullName, localInputStream);
		if(!ossres) {
			System.out.println("Upload FTP fail："+remoteFullName);
			System.out.println("Reply="+ftpclient.getFtp().getReply());
			System.out.println("ReplyCode="+ftpclient.getFtp().getReplyCode());
			System.out.println("ReplyString="+ftpclient.getFtp().getReplyString());
			throw new APIException(APIConstants.FTP_UPLOAD_FAIL, "上传FTP失败");
		}
		System.out.println("[FTP]upload:"+remoteFullName+","+ossres);
		
		BsImageFile img = new BsImageFile();
		img.setId(ConcurrentSequence.getInstance().getSequence());
		img.setBtype(btype);//图片类型
		img.setBid(bid);//业务编码
		img.setPicname(filename);//图片名称
		img.setPicurl(FileUtils.joinDirectory(PicUrlUtils.FTP_PARAMS, remoteFullName));//图片路径
		img.setUserid(userid);//上传人
		img.setObjectkey(remoteFullName);
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

	public void delete(BsImageFile img)  {
		if(StringUtils.isNotEmpty(img.getPicurl())) {
			try {				
				String key = dfsUtils.replaceUrlToEmpt(img.getPicurl());
				ApacheFTPClient ftpclient = new ApacheFTPClient(ftpConfig.getUrl(),ftpConfig.getPort(), ftpConfig.getUsername(), ftpConfig.getPassword(), ftpConfig.getMode());
				
				boolean flag = ftpclient.deleteFile(key);
				System.out.println("[FTP]delete:"+key+","+flag);
				if(flag) {
					bsImageFileRepository.deleteById(img.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	@Override
	public List<BsImageFile> queryByBidUndo(String bid) {
		List<BsImageFile> list = bsImageFileRepository.queryByBid(bid);
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
			String localTempPath = FileUtils.joinDirectory(ftpConfig.getLocalTempPath(), origImgPath);
			FileUtils.writeByteArrayToFile(new File(localTempPath), smallContents);
			ApacheFTPClient ftpclient = new ApacheFTPClient(ftpConfig.getUrl(),ftpConfig.getPort(), ftpConfig.getUsername(), ftpConfig.getPassword(), ftpConfig.getMode());
			
			boolean ossres = ftpclient.uploadFile(smallurl, localTempPath);
			if(!ossres) {
				throw new APIException(APIConstants.FTP_UPLOAD_FAIL, "上传FTP失败");
			}
			
			System.out.println("成功上传小图："+smallurl);
			
			img.setPicurl(origImgPath);
			if(wh == 100) {
				img.setSmallpic(FileUtils.joinDirectory(PicUrlUtils.FTP_PARAMS, smallurl));//图片路径
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
	
	@Override
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
