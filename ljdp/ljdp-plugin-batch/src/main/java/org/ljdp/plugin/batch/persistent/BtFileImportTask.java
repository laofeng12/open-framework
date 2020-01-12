package org.ljdp.plugin.batch.persistent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.ljdp.component.bean.BaseVO;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="BATCH_FILEIMPORT_TASK"
)
public class BtFileImportTask extends BaseVO implements java.io.Serializable {
	// Fields

	private static final long serialVersionUID = 9081020205343681983L;
	private String taskId;
	private String taskName;
	private String taskType;//对应ExampleOrderBatchAction.java中传入的batchType
	private String operType;
	private String operatorId;
	private String operator;//操作人账号
	private String operatorName;
	private String bsBo;
	private Integer totalRec;//总记录数
	private Integer succRec;//成功数量
	private Integer failRec;//失败数量
	private String failLog;//失败结果文件路径
	private String procState;//任务状态
	private String procWay;
	private Long costTime;//耗时（秒）
	private Date createDate;//任务创建时间
	private String serverIP;
	private Date beginTime;//任务开始时间
	private Date finishTime;//任务结束时间
	private String proceFileName;//上传的文件名

	// Constructors

	/** default constructor */
	public BtFileImportTask() {
	}

	/** minimal constructor */
	public BtFileImportTask(String taskId) {
		this.taskId = taskId;
	}

	/** full constructor */
//	public BtFileImportTask(String taskId, String taskName, String taskType,
//			String operType, String operator, String operatorName, String bsBo,
//			Integer totalRec, Integer succRec, Integer failRec, String failLog,
//			String procState, String procWay, Long costTime) {
//		this.taskId = taskId;
//		this.taskName = taskName;
//		this.taskType = taskType;
//		this.operType = operType;
//		this.operator = operator;
//		this.operatorName = operatorName;
//		this.bsBo = bsBo;
//		this.totalRec = totalRec;
//		this.succRec = succRec;
//		this.failRec = failRec;
//		this.failLog = failLog;
//		this.procState = procState;
//		this.procWay = procWay;
//		this.costTime = costTime;
//	}

	// Property accessors
	@Id 
    @Column(name="TASK_ID", unique=true, nullable=false, length=30)
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name="TASK_NAME", length=100)
	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Column(name="TASK_TYPE", length=30)
	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@Column(name="OPER_TYPE", length=30)
	public String getOperType() {
		return this.operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	@Column(name="OPERATOR", length=20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name="OPERATOR_NAME", length=30)
	public String getOperatorName() {
		return this.operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Column(name="BS_BO", length=200)
	public String getBsBo() {
		return this.bsBo;
	}

	public void setBsBo(String bsBo) {
		this.bsBo = bsBo;
	}

	@Column(name="TOTAL_REC", precision=8, scale=0)
	public Integer getTotalRec() {
		return this.totalRec;
	}

	public void setTotalRec(Integer totalRec) {
		this.totalRec = totalRec;
	}

	@Column(name="SUCC_REC", precision=8, scale=0)
	public Integer getSuccRec() {
		return this.succRec;
	}

	public void setSuccRec(Integer succRec) {
		this.succRec = succRec;
	}

	@Column(name="FAIL_REC", precision=8, scale=0)
	public Integer getFailRec() {
		return this.failRec;
	}

	public void setFailRec(Integer failRec) {
		this.failRec = failRec;
	}

	@Column(name="FAIL_LOG", length=100)
	public String getFailLog() {
		return this.failLog;
	}

	public void setFailLog(String failLog) {
		this.failLog = failLog;
	}

	@Column(name="PROC_STATE", length=15)
	public String getProcState() {
		return this.procState;
	}

	public void setProcState(String procState) {
		this.procState = procState;
	}

	@Column(name="PROC_WAY", length=10)
	public String getProcWay() {
		return this.procWay;
	}

	public void setProcWay(String procWay) {
		this.procWay = procWay;
	}

	@Column(name="COST_TIME", precision=10, scale=0)
	public Long getCostTime() {
		return this.costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	@Transient
	public String getProcStateDesc() {
		if (this.procState == null) {
			return "";
		}
		if (this.procState.equals(BatchFileDic.IMPORTING)) {
			return "正在入库";
		}
		if (this.procState.equals(BatchFileDic.IMPORTERROR)) {
			return "入库失败";
		}
		if (this.procState.equals(BatchFileDic.WAIT)) {
			return "入库成功，待后台处理";
		}
		if (this.procState.equals(BatchFileDic.PROCESSING)) {
			return "正在处理";
		}
		if (this.procState.equals(BatchFileDic.SUSPEND)) {
			return "暂停";
		}
		if (this.procState.equals(BatchFileDic.FINISH)) {
			if(this.failRec != null && this.failRec > 0) {
    			return "已完成（部分失败）";
    		}
    		return "已完成";
		}
		if (this.procState.equals(BatchFileDic.FAILEND)) {
			return "非正常结束";
		}
		return "";
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATE_DATE", length=7)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="SERVER_IP", length=20)
	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	@Column(name="OPERATOR_ID", length=20)
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BEGIN_TIME", length = 7)
	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_TIME", length = 7)
	public Date getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "PROCE_FILE_NAME", length = 100)
	public String getProceFileName() {
		return this.proceFileName;
	}

	public void setProceFileName(String proceFileName) {
		this.proceFileName = proceFileName;
	}
}