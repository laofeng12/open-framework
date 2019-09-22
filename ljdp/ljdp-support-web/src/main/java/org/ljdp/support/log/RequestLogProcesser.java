package org.ljdp.support.log;

import java.io.Serializable;
import java.util.List;

/**
 * 请求日志处理器
 * @author hzy
 *
 */
public interface RequestLogProcesser {

	public void doBatch(List<Serializable> logList);
}
