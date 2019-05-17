package com.openjava.example.file.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.support.attach.component.LjdpDfsUtils;
import org.ljdp.support.attach.domain.BsImageFile;
import org.ljdp.support.attach.service.FtpImageFileService;
import org.ljdp.support.attach.service.HwObsService;
import org.ljdp.support.attach.vo.AttachVO;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.example.file.domain.ExampleUploadFile;
import com.openjava.example.file.query.ExampleUploadFileDBParam;
import com.openjava.example.file.service.ExampleUploadFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="上传文件演示")
@CacheConfig(cacheNames = {"quickcache"})
@RestController
@RequestMapping("/example/file/exampleUploadFile")
public class ExampleUploadFileAction {
	
	@Resource
	private ExampleUploadFileService exampleUploadFileService;
	@Resource
	private FtpImageFileService ftpImageFileService;
	//引入华为云OBS服务类
	@Resource
	private HwObsService hwObsService;
	@Resource
	private LjdpDfsUtils dfsUtils;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ExampleUploadFile get(@PathVariable("id")Long id) {
		ExampleUploadFile m = exampleUploadFileService.get(id);
		if(m != null) {
			//FTP
			//这儿演示只显示第一个附件，如果是多个，应该用Table显示
			List<BsImageFile> list = ftpImageFileService.queryByBid(m.getFid().toString());
			//获取前端展示的地址
			if(!list.isEmpty()) {
//				for (BsImageFile bf : list) {
//					String viewUrl = dfsUtils.getViewUrl(bf);//转换为客户端可以查看的地址
//					String downloadUrl = dfsUtils.getDownloadUrl(bf);//转换为客户端可以下载的地址
//				}
				BsImageFile f1 = list.get(0);
				m.setFurl(dfsUtils.getViewUrl(f1));//转换为客户端可以查看的地址
				m.setDownloadUrl(dfsUtils.getDownloadUrl(f1));//转换为客户端可以下载的地址
			}
			//OBS
			//【例子】获取商品轮播图给前端显示
			ArrayList<AttachVO> bannerList = new ArrayList<>();
			List<BsImageFile> dblist1 = hwObsService.queryByBtypeAndBid("填写自己定义btype", id.toString());
			dblist1.forEach(f -> {
				bannerList.add(new AttachVO(f.getId().toString(), f.getPicurl()));
			});
			//【例子】获取商品详情给前端显示
			ArrayList<AttachVO> detailList = new ArrayList<>();
			List<BsImageFile> dblist2 = hwObsService.queryByBtypeAndBid("填写自己定义btype", id.toString());
			dblist2.forEach(f -> {
				detailList.add(new AttachVO(f.getId().toString(), f.getPicurl()));
			});
		}
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_fname", value = "文件名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_createTime", value = "创建时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_createTime", value = "创建时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<ExampleUploadFile> doSearch(ExampleUploadFileDBParam params, Pageable pageable){
		Page<ExampleUploadFile> result =  exampleUploadFileService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	
	@Security(session=true)
	@RequestMapping(value="/doListAttach",method=RequestMethod.GET)
	public TablePage<BsImageFile> doListAttach(@RequestParam("bid")String bid){
		List<BsImageFile> fileList =  ftpImageFileService.queryByBid(bid);
		fileList.forEach(f -> {
			f.setPicurl(dfsUtils.getViewUrl(f));//转换为前端可用的地址
		});
		
		return new TablePageImpl<>(fileList.size(), fileList);
	}
	
//	@RequestMapping(method=RequestMethod.POST)
//	public DataApiResponse<ExampleUploadFile> doSaveTest(@RequestBody ExampleUploadFile model) throws Exception{
//		String[] addAttachIds = new String[] {};
//		String[] delAttachIds = new String[] {};
//		if(addAttachIds != null) {
//			for (int i = 0; i < addAttachIds.length; i++) {
//				String fid = addAttachIds[i];
//				BsImageFile f = hwObsService.relationObject(fid, "请填写业务类型,后端定义", model.getFid()+"", i, SsoContext.getUserId());
//				//如果只支持1个附件,可以把附件地址也记录到当前对接里,方便查看
//				//System.out.println(f.getPicurl());
//			}
//		}
//		if(delAttachIds != null) {
//			//编辑页面删除的文件
//			for (int i = 0; i < delAttachIds.length; i++) {
//				String fid = delAttachIds[i];
//				hwObsService.removeById(new Long(fid));
//			}
//		}
//		DataApiResponse<ExampleUploadFile> resp = new DataApiResponse();
//		return resp;
//	}
	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "fid", value = "文件id", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "fname", value = "文件名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "furl", value = "文件路径", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "createTime", value = "创建时间", required = false, dataType = "Date", paramType = "post"),
		@ApiImplicitParam(name = "updateUser", value = "创建用户", required = false, dataType = "String", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(ExampleUploadFile model, @RequestParam("isNew")Boolean isNew
			,@RequestParam(value="attachIds",required=false)String attachIds
			,@RequestParam(value="delAttachIds",required=false)String delAttachIds
			) throws Exception{
		ExampleUploadFile dbObj;
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setFid(ss.getSequence());
			model.setCreateTime(new Date());
			model.setUpdateUser(SsoContext.getAccount());
			dbObj = exampleUploadFileService.doSave(model);
		} else {
			//修改，记录更新时间等
			dbObj = exampleUploadFileService.get(model.getFid());
			MyBeanUtils.copyPropertiesNotBlank(dbObj, model);
			exampleUploadFileService.doSave(dbObj);
		}
		
		if(StringUtils.isNotEmpty(attachIds)) {
			String[] attachItems = attachIds.split(",");
			for (int i = 0; i < attachItems.length; i++) {
				String aid = attachItems[i];
				//上传附件(FTP方案)
				ftpImageFileService.upload(aid, "example", dbObj.getFid()+"", i, SsoContext.getUserId());
				//OBS上传
				hwObsService.relationObject(aid, "example", dbObj.getFid()+"", i, SsoContext.getUserId());
			}
		}
		if(StringUtils.isNotEmpty(delAttachIds)) {
			//编辑页面时删除的文件
			String[] attachItems = delAttachIds.split(",");
			for (int i = 0; i < attachItems.length; i++) {
				String aid = attachItems[i];
				//FTP(方案)
				ftpImageFileService.removeById(new Long(aid));
				//OBS(方案)
				hwObsService.removeById(new Long(aid));
			}
		}
		
		DataApiResponse resp = new DataApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")Long id) throws Exception{
		//先删除附件
		ftpImageFileService.removeByBId(id.toString());
		//OBS
		hwObsService.removeByBId(id.toString());
		//再删除数据库记录
		exampleUploadFileService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "批量删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public ApiResponse doRemove(@RequestParam("ids")String ids) {
		exampleUploadFileService.doRemove(ids);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@Cacheable
	@Security(session=false)
	@RequestMapping(value="/testCache",method=RequestMethod.GET)
	public ApiResponse testCache(@RequestParam("tid")String tid) {
		System.out.println("===================进入了cache===================");
		ApiResponse resp = new BasicApiResponse(200, tid+"序列："+System.currentTimeMillis());
		return resp;
	}
}
