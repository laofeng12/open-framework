package org.ljdp.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;

@SuppressWarnings("rawtypes")
public interface DAO<T> extends GeneralDAO {
	/** 查询类型 */
	/* 查询数量和数据 */
	public static final Integer QUERY_ALL = 0;

	/* 只查询数量 */
	public static final Integer QUERY_COUNT = 10;

	/* 只查询数据 */
	public static final Integer QUERY_DATA = 20;

	/** 操作类型 */
	/* 插入 */
	public static final Integer OPER_CREATE = 1;

	/* 删除 */
	public static final Integer OPER_DELETE = 2;

	/* 更新 */
	public static final Integer OPER_UPDATE = 3;

	/* 查询 */
	public static final Integer OPER_QUERY = 4;

	public Class<T> getEntityClass();

	public void setEntityClass(Class<T> voClass);

	public void clear(Object vo);

	public void removeByPK(Serializable pk);
	
	public void removeByVOPK(T vo);

	public T findFirstByProperty(String proName, Object value);

	public T findFirstByProperty(String proName, Object value,
			boolean sessionCache);

	public T findByLongPK(Long pk);

	public T findByPK(Serializable pk);

	public T findByVOPK(T vo);

	public Collection<T> findAll();

	public Number getMaxValue(String prop);

	public Number getMaxValue(String prop, DBQueryParam params)
			throws Exception;

	public DataPackage<T> query(DBQueryParam params) throws Exception;

	public DataPackage<T> query(DBQueryParam params, Integer queryType)
			throws Exception;

	public DataPackage<T> query(DBQueryParam params, String joinClause,
			DBQueryParam joinParams) throws Exception;

	/**
	 * 查询数据库表，根据params中的字段生成相应查询的条件（where子句）。 如果有多个同名的字段，则它们生成的条件是AND关系,
	 * 如果一个字段有多个值(数组)，则这个字段生成的条件为OR关系。 支持两表连接查询，需要填写连接子句和连接表查询参数。
	 * 查询条件params需要是DBQueryParam类或是它的继承类.
	 * 
	 * 一、 当使用字段作为参数时字段名要求一定的命名规则:(一般用于处理网页的请求) 
	 * 例如：字段 _eq_name的值为"myName"，则生成的条件子句为：name = 'myName'。
	 * 
	 * 规则：字段名＝前缀+原VO中的字段名 查询条件前缀列表(可以查看Operator类获知所有支持的查询条件) 
	 * 
	 * 通用运算:（支持所有数据类型）
	 * 等于     不等于     小于       大于      小于等于      大于等于        BETWEEN     IN    NOT IN 
	 *  =     <>     <      >      <=        >=
	 * _eq_  _ne_   _lt_   _gt_   _le_      _ge_    _between_   _in_   _nin_
	 * 
	 * 字符串运算：
	 * (like)    (not like) (等于,忽略大小写) (不等于,忽略大小写) (like,忽略大小写) (not like,忽略大小写) 
	 *  _like_    _nlike_       _ic_eq_           _ic_ne_           _ic_like_         _ic_nlike
	 * 
	 * 日期运算:
	 * 等于                   不等于            小于                 大于               小于等于            大于等于 
	 * _dt_eq_   _dt_ne_   _dt_lt_   _dt_gt_   _dt_le_     _dt_ge_
	 * 
	 * 自定义SQL
	 * _sql_
	
	 * 
	 * 二、也可以直接使用DBQueryParam类作为参数，
	 * 例如： 
	 * 	DBQueryParam params = new DBQueryParam();
	 *  params.addQueryCondition(fieldA, RO.EQ, value0);
	 *  params.addQueryCondition(fieldB, RO.GT, value1);
	 *  则生成的SQL为：fieldA=? and fieldB>?。
	 *  
	 *  上面语句相当于：
	 *  params.addQueryCondition(fieldA, "_eq_", value0);
	 *  params.addQueryCondition(fieldB, "_gt_", value1);
	 *  
	 * 
	 * 三、日期字段即可以用通用运算符，也可以用日期运算符。
	 * 例如下面几种写法理论上效果一样：
	 * String format;
	 * format="yyyy-MM-dd"
	 * format="yyyy/MM/dd"
	 * format="yyyy-MM-dd HH:mm:ss"
	 * format="yyyy/MM/dd HH:mm:ss"
	 * format="yyyy-MM-dd HH:mm"
	 * format="yyyy/MM/dd HH:mm"
	 * format="yyyyMMddHHmmss"
	 * SimpleDateFormat sdf = new SimpleDateFormat(format);//format可以是上面任一类型
	 * 
	 * 1.
	 * 	public class MyDBParam extends DBQueryParam {
	 * 		private Date _eq_createDate;
	 * 	    ....
	 * 	}
	 * 	MyDBParam param = new MyDBParam();
	 * 	param.set_eq_createDate(new Date());
	 * 
	 * 2.
	 * 	public class MyDBParam extends DBQueryParam {
	 * 		private String _dt_eq_createDate;
	 * 		....
	 *  }
	 *  MyDBParam param = new MyDBParam();
	 *  param.set_dt_eq_createDate(sdf.format(new Date()));
	 * 
	 * 3.
	 *  DBQueryParam param = new DBQueryParam();
	 *  params.addQueryCondition(createDate, RO.EQ, new Date());
	 *  
	 * 4.
	 *  DBQueryParam param = new DBQueryParam();
	 *  params.addQueryCondition(createDate, RO.DT.EQ, sdf.format(new Date()));
	 *  RO.DT.EQ相当于"_dt_eq_"
	 * 
	 * 
	 * @param params 查询条件
	 * @param joinClause 两个表连接查询的连接子句，如：FROM USER user inner join user.address add
	 *           则连接子句就是：inner join ?.address, 必需把别名写成?
	 * @param joinParams 连接表的查询条件，与params一样用法
	 * @param queryType 查询类型，QUERY_COUNT:只查数据量，QUERY_DATA:只查数据，QUERY_ALL:都查
	 * @return
	 * @throws Exception
	 */
	public DataPackage<T> query(DBQueryParam params, String joinClause,
			DBQueryParam joinParams, Integer queryType) throws Exception;

	public DataPackage queryByNameSQL(String name, DBQueryParam params);
	
	public DataPackage queryByNameSQL(String name, DBQueryParam params, Integer queryType);
	
	public List queryEntityNative(String sql);
	public List queryEntityNative(String sql, Map<String, ?> map);

}
