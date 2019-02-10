package org.ljdp.plugin.batch.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ljdp.component.bean.BaseVO;


@Entity
@Table(name="BATCH_FILEDATA_FAIL"
)
public class BtFileDataFail extends BaseVO implements java.io.Serializable {
	// Fields

	private static final long serialVersionUID = 3287910893530291715L;
	private Long dataId;
	private String taskId;
	private String dataItem;
	private String failReason;

	// Constructors

	/** default constructor */
	public BtFileDataFail() {
	}

	/** minimal constructor */
	public BtFileDataFail(Long dataId) {
		this.dataId = dataId;
	}

	/** full constructor */
	public BtFileDataFail(Long dataId, String taskId, String dataItem,
			String failReason) {
		this.dataId = dataId;
		this.taskId = taskId;
		this.dataItem = dataItem;
		this.failReason = failReason;
	}

	// Property accessors
	@Id 
    @Column(name="DATA_ID", unique=true, nullable=false, precision=18, scale=0)
	public Long getDataId() {
		return this.dataId;
	}

	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}

	@Column(name="TASK_ID", length=30)
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name="DATA_ITEM", length=2500)
	public String getDataItem() {
		return this.dataItem;
	}

	public void setDataItem(String dataItem) {
		this.dataItem = dataItem;
	}

	@Column(name="FAIL_REASON", length=500)
	public String getFailReason() {
		return this.failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

}