package org.ljdp.common.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统运行时的环境，各种参数和配置等，影响系统运行时的方式。
 * 系统启动时从配置文件载入主运行环境，启动后使用此环境作为
 * 系统默认的运行时环境。
 * 并支持每个新建线程使用一个新的环境。
 * 
 * @author hzy
 * @version 1.6
 */
public class Env {
	private static Logger log = LoggerFactory.getLogger(Env.class);
	public static final String[] CONFIG_FILE = new String[] {"core", "application"};
	public static final String DIR = "/ljdp/";

    /**
     * 保存每个线程的环境
     */
    private static final ThreadLocal<Env> threadEnv = new ThreadLocal<Env>();
    
    //保存全局的环境
    private static Env currentEnv;
    
    private String run = null;
    private Set<Constant.Run> runstatus = new HashSet<Constant.Run>();
    
    private Constant.SessionFactoryGeter sessionFactoryGeter = null;
    
    //使用的数据库
    private String[] dataBases;
    
    private Constant.TransactionManager transactionManager = null;
    
    //当前使用的配置文件 默认为：/ljdp/application.properties
    private String coreCfg;
    
    //使用的spring配置文件 默认为：/ljdp/ApplicationContext.xml
    private String springCfg = "/ljdp/applicationContext.xml";
    
    private ConfigFile configFile;
    
    public Env() {
    	run = Constant.Run.FORMAL.toString();
    	sessionFactoryGeter = Constant.SessionFactoryGeter.NEW;
    	transactionManager = Constant.TransactionManager.JDBC;
    	dataBases = new String[1];
    	dataBases[0] = "default";
    }

	public Env(String run, Constant.SessionFactoryGeter sessionFactoryGeter,
			Constant.TransactionManager transactionManager, String[] dataBases) {
		this.run = run;
		this.sessionFactoryGeter = sessionFactoryGeter;
		this.transactionManager = transactionManager;
		this.dataBases = dataBases;
	}

	public Env(String config) {
    	loadConfig(config, null);
    }
	
	public Env(String config, String head) {
		loadConfig(config, head);
	}
	
	protected void loadConfig(String config, String head) {
		ConfigFileFactory cfgFactory = ConfigFileFactory.getInstance();
    	ConfigFile cfg;
    	if(StringUtils.isBlank(config)) {
    		cfg = cfgFactory.getAppConfig();
    		this.coreCfg = "application";
    	} else {    		
    		cfg = cfgFactory.get(config);
    		this.coreCfg = config;
    	}
    	log.info("Lightweight J2EE Development Platform");
    	log.info("Environment initialize...");
    	log.info("Read LJDP Config file: " + cfg.getConfig_file_name());
    	loadConfig(cfg, head);
	}

	protected void loadConfig(ConfigFile cfg, String head) {
		if(StringUtils.isNotBlank(head)) {
			head = "core."+head+".";
		} else {
			head = "core.";
		}
    	String re = cfg.getValue(head + "run");
    	if(StringUtils.isNotBlank(re)) {
    		re = re.toUpperCase();
    		for(Constant.Run r : Constant.Run.values()) {
    			if(re.indexOf(r.toString()) != -1) {
    				run = re;
    				break;
    			}
    		}
    	}
        if(run == null) {
            run = Constant.Run.FORMAL.toString();
            log.warn(head + "Run is not config");
        }
        updateRunstatus();
        
        String sf = cfg.getValue(head + "sessionFactoryGeter");
        if(StringUtils.isNotBlank(sf)) {
        	for(Constant.SessionFactoryGeter s : Constant.SessionFactoryGeter.values()) {
        		if(sf.equalsIgnoreCase(s.toString())) {
        			sessionFactoryGeter = s;
        		}
        	}
        }
        if(sessionFactoryGeter == null) {
        	sessionFactoryGeter = Constant.SessionFactoryGeter.NC;
        	log.warn(head + "SessionFactoryGeter is not config");
        }
        
        String dbs = cfg.getValue(head + "dataBases");
        if(StringUtils.isNotBlank(dbs)) {
        	dataBases = StringUtils.split(dbs, " ,");
        	for(int i = 0; i < dataBases.length; i++) {
        		dataBases[i] = dataBases[i].toUpperCase();
        	}
        } else {
        	dataBases = new String[1];
        	dataBases[0] = "default";
        }
        
        String tran = cfg.getValue(head + "transactionManager");
        if(StringUtils.isNotBlank(tran)) {
        	for(Constant.TransactionManager t : Constant.TransactionManager.values()) {
        		if(tran.equalsIgnoreCase(t.toString())) {
        			transactionManager = t;
        		}
        	}
        }
        if(transactionManager == null) {
        	transactionManager = Constant.TransactionManager.NC;
        	log.warn(head + "TransactionManager is not config");
        }
        
        String scfg = cfg.getValue(head+"spring.config");
        if(StringUtils.isNotBlank(scfg)) {
        	this.springCfg = scfg;
        }
        
        log.info(head + "Run = " + run);
        log.info(head + "SessionFactoryGeter = " + sessionFactoryGeter);
        log.info(head + "TransactionManager = " + transactionManager);
        log.info(head + "SpringConfig = " + springCfg);
        log.info(head + "DataBases = " + ArrayUtils.toString(dataBases));
    }

	/**
	 * 构建当前线程环境
	 */
	public static void buildThreadEnv() {
		buildThreadEnv("");
	}
	
	public static void buildThreadEnv(String config) {
		buildThreadEnv(config, null);
	}
	
	/**
	 * 构建当前线程环境
	 */
	public static synchronized void buildThreadEnv(String config, String head) {
		config = getConfigFile(config);
		Env env = new Env(config, head);
		setThreadEnv(env);
	}
	
	/**
	 * 构建当前线程环境
	 */
	public static void buildThreadEnv(Env env) {
		setThreadEnv(env);
	}

	/**
	 * 获取当前线程的环境
	 * @return
	 */
	public static Env getThreadEnv() {
		return threadEnv.get();
	}

	protected static void setThreadEnv(Env env) {
		threadEnv.set(env);
	}
	
	/**
	 * 构建当前的主环境
	 */
	public static void buildCurrentEnv() {
		buildCurrentEnv(null);
	}
	
	public static void buildCurrentEnv(String config) {
		buildCurrentEnv(config, null);
	}
	
	public static synchronized void buildCurrentEnv(String config, String head) {
		if(null == currentEnv) {
			log.info("Initializing Current Environment: config=" + config + ", head=" + head);
			config = getConfigFile(config);
			currentEnv = new Env(config, head);
		}
	}

	protected static String getConfigFile(String config) {
		if(StringUtils.isBlank(config)) {
			for(String cf : CONFIG_FILE) {
				String mycfg = DIR+cf + ConfigFileFactory.EXT_NAME;
				if(Env.class.getResource(mycfg) != null) {
					config = DIR+cf;
					break;
				}
			}
		}
		return config;
	}
	
	public ConfigFile getConfigFile() {
		if(configFile == null) {
			ConfigFileFactory cfgFactory = ConfigFileFactory.getInstance();
			configFile = cfgFactory.get(this.coreCfg);
		}
    	return configFile;
	}
	
	public static synchronized void rebuildCurrentEnv(Env env) {
		currentEnv = env;
	}
	
	public static synchronized void rebuildCurrentEnv() {
		currentEnv = null;
		buildCurrentEnv();
	}

	/**
	 * 获得当前的主环境，如果配置了当前线程的专用环境则返回当前线程的环境。
	 * @return
	 */
	public static synchronized Env current() {
		Env env = getThreadEnv();
		if(env != null) {
			return env;
		}
		if(currentEnv == null) {
			buildCurrentEnv();
		}
		return currentEnv;
	}
	
	public static Env getCurrent() {
		return currentEnv;
	}

	public String getRun() {
		return run;
	}

	public void setRun(String run) {
		this.run = run;
		updateRunstatus();
	}
	
	public Set<Constant.Run> runstatus(){
		return runstatus;
	}
	
	public void updateRunstatus() {
		runstatus.clear();
		if(run != null) {
			String[] items = run.split(",");
			for(String item : items) {
				if(StringUtils.isNotEmpty(item)) {
					try {
						runstatus.add(Constant.Run.valueOf(item));
					} catch (Exception e) {
						log.info("run status ["+item+"] is unknow");
					}
				}
			}
		}
	}

	public Constant.SessionFactoryGeter sessionFactoryGeter() {
		return sessionFactoryGeter;
	}

	public void setSessionFactoryGeter(Constant.SessionFactoryGeter sessionFactory) {
		this.sessionFactoryGeter = sessionFactory;
	}
    
    public Constant.TransactionManager transactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(Constant.TransactionManager transaction) {
		this.transactionManager = transaction;
	}

	public String[] getDataBases() {
		return dataBases;
	}

	public void setDataBases(String[] dataBases) {
		this.dataBases = dataBases;
	}

	public String getCoreCfg() {
		return coreCfg;
	}

	public void setCoreCfg(String config) {
		this.coreCfg = config;
	}

	public String getSpringCfg() {
		return springCfg;
	}

	public void setSpringCfg(String springCfg) {
		this.springCfg = springCfg;
	}

}
