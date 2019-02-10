package org.ljdp.plugin.batch.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ljdp.component.bean.BaseVO;


@Entity
@Table(name="BATCH_FILEDATA"
)
public class BtFileData extends BaseVO implements java.io.Serializable {
	private static final long serialVersionUID = 7098511812264953355L;
	// Fields
	private Long dataId;
	private String taskId;
	private String dataItem;

	// Constructors

	/** default constructor */
	public BtFileData() {
	}

	/** minimal constructor */
	public BtFileData(Long dataId) {
		this.dataId = dataId;
	}

	/** full constructor */
	public BtFileData(Long dataId, String taskId, String dataItem) {
		this.dataId = dataId;
		this.taskId = taskId;
		this.dataItem = dataItem;
	}

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

}