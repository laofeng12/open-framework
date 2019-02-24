package org.ljdp.support.attach.repository;

import java.util.Date;
import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import org.ljdp.support.attach.domain.BsImageFile;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface BsImageFileRepository extends DynamicJpaRepository<BsImageFile, Long>, BsImageFileRepositoryCustom{
	
	@Query("from BsImageFile where btype=:btype and bid=:bid order by seqno, creatime")
	public List<BsImageFile> queryByBtypeAndBid(@Param("btype")String btype, @Param("bid")String bid);
	
	@Query("from BsImageFile where bid=:bid order by seqno, creatime")
	public List<BsImageFile> queryByBid(@Param("bid")String bid);
	
	/**
	 * 更新附件所关联的业务记录id
	 * @param fid
	 * @param bid
	 * @return
	 */
	@Modifying
	@Query("update BsImageFile set bid=:bid where id=:fid")
	public int updateBid(@Param("fid")Long fid, @Param("bid")String bid);
	
	@Query("from BsImageFile where bid is null and creatime<=:creatime")
	public List<BsImageFile> queryByBidIsNull(@Param("creatime")Date creatime, Pageable pageable);
	
	@Query("from BsImageFile where objectkey is null")
	public List<BsImageFile> queryByObjectkeyIsNull(Pageable pageable);
	
	public BsImageFile findByObjectkey(String objectkey);
	
	@Query("from BsImageFile where btype=:btype and creatime>=:creatime and bid is not null")
	public List<BsImageFile> queryByBypeAndGeTime(@Param("btype")String btype, @Param("creatime")Date creatime);
}
