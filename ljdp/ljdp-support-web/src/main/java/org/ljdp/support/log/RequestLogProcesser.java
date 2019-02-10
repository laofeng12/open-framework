package org.ljdp.support.log;

import java.util.List;

import org.ljdp.log.model.RequestLog;

/**
 * 请求日志处理器
 * @author hzy
 *
 */
public interface RequestLogProcesser {

	public void doBatch(List<RequestLog> logList);
}
