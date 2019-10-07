package org.ljdp.component.result;

public class APIConstants {
	
	public static int HTTP_POSITION_BODY = 1;//存放在报文主体中
	public static int HTTP_POSITION_HEAD = 2;//存放在http header中
	
	public static final int CODE_SUCCESS = 200;
	public static final int CODE_FAILD = 400;
	public static final int CODE_SERVER_ERR = 500;
	public static final int CODE_PARAM_ERR = -1; 
	public static final int CODE_VERIFYCODE_ERR = -2;
	public static final int CODE_SERVICE_BUSY = 20000;//服务器繁忙
	
	public static final int ACCOUNT_NO_LOGIN = 20019;//需要登录
	public static final int CODE_AUTH_FAILED = 20020;//认证失败
	public static final int ACCESS_NO_USER = 20021;//没有登录用户，返回空数据，不强制跳转登录页
	public static final int IDENTITY_NOTPASS = 20022;//身份验证不通过
	public static final int ROLE_NOTPASS = 20023;//角色验证不通过
	public static final int USER_NOTPASS = 20024;//用户验证不通过
	
	public static final int OSS_UPLOAD_FAIL = 20030;//OSS上传失败
	public static final int FTP_UPLOAD_FAIL = 20031;//ftp上传失败
	public static final int PARAMS_NOT_Valid = 20032;//参数错误
}
