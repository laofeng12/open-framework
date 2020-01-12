package org.ljdp.module.batch;

import jxl.Cell;

import org.apache.poi.ss.usermodel.Row;
import org.ljdp.component.namespace.NameNotFoundException;
import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.core.service.GeneralService;
import org.ljdp.core.service.ServiceFactory;
import org.ljdp.module.filetask.AbstractFileBatchBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransactionFileBatchBO extends AbstractFileBatchBO {
	private Logger log = LoggerFactory.getLogger(TransactionFileBatchBO.class);

	protected GeneralService gs;

	@Override
	public Boolean initialization() {
		try {
			gs = ServiceFactory.buildGeneral();
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public BatchResult doProcessRecord(int sheetLocation, String[] line, int size) {
		BatchResult res = null;
		try {
			res = (BatchResult)gs.doTransaction(new BusinessObject() {
				
				public Object doBusiness(Object... params) throws Exception {
					String[] line = (String[])params[0];
					Integer size = (Integer)params[1];
					return process(sheetLocation, line, size);
				}
			}, new Object[] {line, size});
		} catch (Throwable e) {
			while(e.getCause() != null) {
				e = e.getCause();
			}
			res = new GeneralBatchResult();
			res.setSuccess(false);
			res.setMsg(e.getMessage());
			res.setData(e.getClass());
			//数据库异常翻译
			if(res.getMsg() != null) {
				if(res.getMsg().indexOf("ORA-00001") >= 0) {//违反唯一约束条件
					res.setMsg("数据已存在，请检查是否重复导入");
				}
			}
			onTransactionException(e, res);
		}
		return res;
	}
	
	/**
	 * 事务提交失败时进入此方法，建议子类实现，对异常信息进行处理封装，避免数据原始信息返回前台显示
	 * @param e
	 * @param res
	 */
	protected void onTransactionException(Throwable e, BatchResult res) {
		log.error("doProcessRecord(String[] line, int size)", e);
	}
	
	private final BatchResult process(int sheetLocation, String[] line, int size) throws Exception {
		BatchResult res = null;
		for(int i = 0; i < size; ++i) {
			res = doProcessRecord(sheetLocation, line[i]);
			if(res == null) {
				throw new Exception("返回结果为NULL，可能不支持TXT或Excel文件");
			}
			if(!res.isSuccess()) {
				break;
			}
		}
		return res;
	}
	
	protected abstract BatchResult doProcessRecord(int sheetLocation, String line);
	
	@Override
	public BatchResult doProcessRecord(int sheetLocation, Row[] rows, int size) {
		BatchResult res = null;
		try {
			GeneralService gs = ServiceFactory.buildGeneral();
			res = (BatchResult)gs.doTransaction(new BusinessObject() {
				
				public Object doBusiness(Object... params) throws Exception {
					 Row[] rows = (Row[])params[0];
					Integer size = (Integer)params[1];
					return process(sheetLocation, rows, size);
				}
			}, new Object[] {rows, size});
		} catch (Throwable e) {
			while(e.getCause() != null) {
				e = e.getCause();
			}
			log.error("doProcessRecord(Row[] rows, int size)", e);
			res = new GeneralBatchResult();
			res.setSuccess(false);
			res.setMsg(e.getMessage());
		}
		return res;
	}
	
	private final BatchResult process(int sheetLocation, Row[] rows, int size)  {
		BatchResult res = null;
		for(int i = 0; i < size; ++i) {
			res = doProcessRecord(sheetLocation, rows[i]);
			if(res == null || !res.isSuccess()) {
				break;
			}
		}
		return res;
	}
	
	protected BatchResult doProcessRecord(int sheetLocation, Row row) {
		return null;
	}

	@Override
	public BatchResult doProcessRecord(Cell[][] rows, int size) {
		BatchResult res = null;
		try {
			GeneralService gs = ServiceFactory.buildGeneral();
			res = (BatchResult)gs.doTransaction(new BusinessObject() {
				
				public Object doBusiness(Object... params) throws Exception {
					Cell[][] rows = (Cell[][])params[0];
					Integer size = (Integer)params[1];
					return process(rows, size);
				}
			}, new Object[] {rows, size});
		} catch (Throwable e) {
			while(e.getCause() != null) {
				e = e.getCause();
			}
			log.error("doProcessRecord(Cell[][] rows, int size)", e);
			res = new GeneralBatchResult();
			res.setSuccess(false);
			res.setMsg(e.getMessage());
		}
		return res;
	}
	
	private final BatchResult process(Cell[][] rows, int size)  {
		BatchResult res = null;
		for(int i = 0; i < size; ++i) {
			res = doProcessRecord(rows[i]);
			if(res == null || !res.isSuccess()) {
				break;
			}
		}
		return res;
	}
	
	protected BatchResult doProcessRecord(Cell[] row) {
		return null;
	}

}
