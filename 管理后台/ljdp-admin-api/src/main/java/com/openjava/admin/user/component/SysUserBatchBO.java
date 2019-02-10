package com.openjava.admin.user.component;

import java.util.Date;
import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.util.DateFormater;
import org.ljdp.plugin.batch.bo.BaseFileImportBO;
import org.springframework.stereotype.Component;

import com.openjava.admin.user.domain.SysUser;
import com.openjava.admin.user.service.SysUserService;

/**
 * 文件导入的业务对象（前台处理）
 * 运行顺序如下：
 * initialization();
 * for(读取文件每行数据->record){
 *    doProcessRecord(record);
 * }
 * finalWork();
 * destory();
 * 
 * @author hzy
 */
@Component
public class SysUserBatchBO extends BaseFileImportBO {

	@Resource
	private SysUserService sysUserService;
	
	@Override
	public String getTitle() {
		return "名称|帐号类型(SYS.AccountType)|登录账号|密码|是否过期|是否锁定|创建时间|状态|邮箱|手机号码|电话|性别|头像|来源类型|";
	}
	
	@Override
	protected BatchResult doProcessRecord(String record) {
		BatchResult result = new GeneralBatchResult();
		try {
			DBAccessUser u = super.getUser();
			System.out.println(u.getId()+","+u.getAccount()+","+u.getName());
			System.out.println(record);
			String[] items = record.split("\\|");
			System.out.println(ArrayUtils.toString(items));
			int i = 0;
			//读取文件字段
			/*String fullname = items[i++];
			String accounttype = items[i++];
			String account = items[i++];
			String password = items[i++];
			Short isexpired = new Short(items[i++]);
			Short islock = new Short(items[i++]);
			Date createtime = DateFormater.praseDate(items[i++]);
			Short status = new Short(items[i++]);
			String email = items[i++];
			String mobile = items[i++];
			String phone = items[i++];
			String sex = items[i++];
			String picture = items[i++];
			Short fromtype = new Short(items[i++]);*/
			
			//新增记录
			/*SysUser j = new SysUser();
			SequenceService ss = ConcurrentSequence.getInstance();
			j.setUserid(ss.getSequence());
			j.setFullname(fullname);
			j.setAccounttype(accounttype);
			j.setAccount(account);
			j.setPassword(password);
			j.setIsexpired(isexpired);
			j.setIslock(islock);
			j.setCreatetime(createtime);
			j.setStatus(status);
			j.setEmail(email);
			j.setMobile(mobile);
			j.setPhone(phone);
			j.setSex(sex);
			j.setPicture(picture);
			j.setFromtype(fromtype);
			
			sysUserService.doSave(j);*/
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}
}
