package org.ljdp.plugin.batch.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.timer.ScheduleManager;
import org.ljdp.plugin.batch.bo.ScanDbWaitTaskBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BatchTaskListener implements ServletContextListener {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public void contextDestroyed(ServletContextEvent arg0) {
		ScheduleManager.shutdownAll();
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			//扫描时间间隔（分）
			ConfigFile cfg = Env.current().getConfigFile();
			String m = cfg.getValue("batch.bs.scan.interval", "5");
			String ss = cfg.getValue("batch.bs.scan.state", "all");
			String ipLimit = cfg.getValue("batch.bs.scan.iplimit", "true");
			
			//文件导入后台处理器：每5分钟扫描一次
			ScheduleManager.schedule("BATCH-FILEIMPORT-BS","文件导入后台处理器", 
					"minute="+m+" repeat=true", new ScanDbWaitTaskBO(), "scan.state="+ss+"&scan.iplimit="+ipLimit);
			log.info("批量数据任务管理器：已启动数据库任务扫描器");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("启动后台扫描器失败", e);
		}
	}
}
