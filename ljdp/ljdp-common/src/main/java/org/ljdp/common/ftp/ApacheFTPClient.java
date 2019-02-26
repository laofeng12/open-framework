package org.ljdp.common.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * apache commons net FTP工具
 * @author hzy
 *
 */
public class ApacheFTPClient {

	private FTPClient ftp;
	
	public ApacheFTPClient(String server, Integer port, String username, String password, String mode) throws FTPException{
		try {
			if(port == null) {
				port = 21;
			} else if(port.intValue() <= 21) {
				port = 21;
			}
			ftp = new FTPClient();
			ftp.connect(server, port);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply))
	        {
	            ftp.disconnect();
	            throw new FTPException("FTP server refused connection.");
	        }
			if (!ftp.login(username, password))
            {
                ftp.logout();
                throw new FTPException("login fail.");
            }
			System.out.println("Remote system = " + ftp.getSystemType());
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			if(mode != null) {
				if(mode.equalsIgnoreCase("PASV")) {
					System.out.println("enterLocalPassiveMode");
					ftp.enterLocalPassiveMode();
				} else if(mode.equalsIgnoreCase("PORT")) {
					System.out.println("enterLocalActiveMode");
					ftp.enterLocalActiveMode();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					f.printStackTrace();
				}
			}
			throw new FTPException("Could not connect to server.["+e.getMessage()+"]");
		}
	}
	
	/**
	 * 上传文件
	 * @param remote 远程完整路径名
	 * @param local 本地完整路径名
	 * @throws IOException 
	 */
	public boolean uploadFile(String remote, String local) throws IOException {
		InputStream input= new FileInputStream(local);
        boolean res = ftp.storeFile(remote, input);
        input.close();
        return res;
	}
	
	public boolean uploadFile(String remote, InputStream localInputStream) throws IOException {
        boolean res = ftp.storeFile(remote, localInputStream);
        return res;
	}
	
	public boolean deleteFile(String remote) throws IOException {
		return ftp.deleteFile(remote);
	}
	
	public boolean makeDirectory(String pathname) throws IOException {
		return ftp.makeDirectory(pathname);
	}
	
	public int mkd(String pathname) throws IOException {
		return ftp.mkd(pathname);
	}
	
	public boolean changeWorkingDirectory(String pathname) throws IOException {
		return ftp.changeWorkingDirectory(pathname);
	}
	
	/**
	 * 下载文件
	 * @param remote 远程完整路径名
	 * @param local 本地完整路径名
	 * @throws IOException
	 */
	public boolean downloadFile(String remote, String local) throws IOException {
		String[] items = ftp.listNames(remote);
		if(items != null) {
			OutputStream output= new FileOutputStream(local);
			boolean res = ftp.retrieveFile(remote, output);
			output.close();
			if(!res) {
				File f = new File(local);
				f.delete();
			}
			return res;
		}
		throw new IOException("FTP不存在文件："+remote);
	}
	
	/**
	 * 下载文件
	 * @param remote 远程完整路径名
	 * @param output 输出流
	 * @return
	 * @throws IOException
	 */
	public boolean downloadFile(String remote, OutputStream output) throws IOException {
		String[] items = ftp.listNames(remote);
		if(items != null) {
			boolean res = ftp.retrieveFile(remote, output);
			return res;
		}
		throw new IOException("FTP不存在文件："+remote);
	}
	
	public void close() {
		try {
			ftp.noop(); // check that control connection is working OK
            ftp.logout();
			ftp.disconnect();
		} catch (IOException f) {
			f.printStackTrace();
		}
	}

	public FTPClient getFtp() {
		return ftp;
	}
	
//	public static void main(String[] args) {
//		try {
//			ApacheFTPClient ftp = new ApacheFTPClient("127.0.0.1", "iworkwrite", "iWork@6092");
//			ftp.downloadFile("iwork_upload/uploadfile/jtxy/2014-04-25/140425151415380.doc", "E:/tempfile/微信函测试.doc");
//			ftp.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
