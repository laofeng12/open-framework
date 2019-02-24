package org.ljdp.support.attach.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.ljdp.component.bean.BaseVO;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 实体
 * @author hzy
 *
 */
@Entity
@Table(name = "BS_IMAGE_FILE")
public class BsImageFile extends BaseVO implements Persistable<Long>,java.io.Serializable {
	private static final long serialVersionUID = 8925761552231058290L;
	
	private Long id;//主键
	private String btype;//业务类型(商品图片、商品明细、论坛图片、服务图片等)
	private String bid;//业务编码
	private String picname;//图片名称
	private String picurl;//图片地址
	private Integer seqno;//显示顺序
	private Long userid;//创建人
	private Date creatime;//创建时间
	private String smallpic;//小尺寸图片地址
	private String bucketname;//桶（OSS，OBS上面的存放参数）
	private Long filesize;//文件大小（字节）
	private Long duration;//播放时长（毫秒）（音视频）
	private String objectkey;//文件对象id，唯一
	
    private Boolean isNew;
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.id != null) {
    		return false;
    	}
    	return true;
    }
    
    @Transient
    public Boolean getIsNew() {
		return isNew;
	}
    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	

	@Column(name = "BTYPE")
	public String getBtype() {
		return btype;
	}
	public void setBtype(String btype) {
		this.btype = btype;
	}

	@Column(name = "BID")
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}

	@Column(name = "PICNAME")
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}

	@Column(name = "PICURL")
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	@Column(name = "SEQNO")
	public Integer getSeqno() {
		return seqno;
	}
	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}

	@Column(name = "USERID")
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATIME")
	public Date getCreatime() {
		return creatime;
	}
	public void setCreatime(Date creatime) {
		this.creatime = creatime;
	}

	@Column(name = "SMALLPIC")
	public String getSmallpic() {
		return smallpic;
	}
	public void setSmallpic(String smallpic) {
		this.smallpic = smallpic;
	}

	@Column(name = "bucketname")
	public String getBucketname() {
		return bucketname;
	}

	public void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}

	/**
	 * 获取文件大小（字节）
	 * @return
	 */
	@Column(name = "filesize")
	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	/**
	 * 获取音视频播放时长（毫秒）
	 * @return
	 */
	@Column(name = "duration")
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	@Column(name = "objectkey")
	public String getObjectkey() {
		return objectkey;
	}

	public void setObjectkey(String objectkey) {
		this.objectkey = objectkey;
	}
}