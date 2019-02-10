package org.ljdp.core.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.util.MyArrayUtils;

/**
 * 查询数据库表，根据params中的字段生成相应查询的条件（where子句）。 如果有多个同名的字段，则它们生成的条件是AND关系,
 * 如果一个字段有多个值(数组)，则这个字段生成的条件为OR关系。 支持两表连接查询，需要填写连接子句和连接表查询参数。
 * 查询条件params需要是DBQueryParam类或是它的继承类.
 * 
 * 一、 SQL参数字段名的命名规则:
 * 例如：字段 eq_name的值为"myName"，则生成的条件子句为：name = 'myName'。
 * 
 * 代码例子，例如定义了这样一个用户表查询对象
  public class SysUserDBParam extends RoDBQueryParam {
		private String like_fullname;//名称 like ?
		private String eq_account;//登录账号 = ?
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date le_createtime;//创建时间 <= ?
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date ge_createtime;//创建时间 >= ?
   }
   然后mvc添加一个路由接收查询请求
	@RequestMapping(value="/search")
	public TablePage<SysUser> search(SysUserDBParam params, Pageable pageable){
		Page<SysUser> result =  sysUserService.query(params, pageable);
		.....
	}
   请求A：/search?like_fullname=管理&le_createtime=2018-04-19
   	那么会自动生成sql查询条件：  where fullname like '%管理%' and createtime<=(2018-04-19日期类型)
    
    请求B：/search?eq_account='admin'&ge_createtime=2018-04-19
    那么会自动生成sql查询条件： where account='admin' and createtime>=(2018-04-19日期类型)
   
 * 
 * 规则：字段名＝前缀+原VO中的字段名 查询条件前缀列表(可以查看Operator类获知所有支持的查询条件) 
 * 
 * 通用运算:（支持所有数据类型）
 * 等于           不等于          小于             大于            小于等于              大于等于         BETWEEN     IN    NOT IN   是否空值
 *  =     <>     <      >      <=        >=      between     in    not in   true=(is null) false=(is not null)
 * _eq_  _ne_   _lt_   _gt_   _le_      _ge_    _between_   _in_   _nin_    _null_
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
 * 三、嵌套属性，复合主键使用‘$’符号进行连接
 * 例如可以使用以下两种方式：
 * 1、
 * public class HycardWorkfCardDBParam extends DBQueryParam {
	
	private String _eq_id$workfId;
	private Long _eq_id$cardId;
 * 
 * DBQueryParam params = new HycardWorkfCardDBParam();
 * 
 * 2、
 * DBQueryParam params = new DBQueryParam();
 * params.addQueryCondition("id$workfId", "_eq_", value0);
 * params.addQueryCondition("id$cardId", "_eq_", value0);
 * 
 * 
 * 
 * 四、日期字段即可以用通用运算符，也可以用日期运算符。
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
 */
public class DBQueryParam extends RoDBQueryParam {
	
	/**
	 * Java对象类型
	 */
	public static final int TYPE_POJO = 1;
	
	/**
	 * MAP类型
	 */
	public static final int TYPE_MAP = 2;
	
	/**
	 * 数据库默认返回类型.当设置了selectFields后通常返回的是数组类型,
	 * 通常都会转换成TYPE_POJO或TYPE_MAP返回,
	 * 如果返回包涵了多个表中的字段,一般会转换成TYPE_MAP类型返回.
	 */
	public static final int TYPE_DEFAULT = 3;

	private int _pageno = 0;
	private int _pagesize = 0;
	
	private int start = 0;
	private int limit = 0;

	private String[] _orderby;
	private String[] _sort;

	private boolean _desc;
	
	private String[] _selectItems;

	private String _pk;
	
	private int returnType = TYPE_POJO;
	
	private Boolean restrictQueryQuantity = false;
	
	private List<Set<String>> orOperations = new ArrayList<Set<String>>();
	

	public boolean is_desc() {
		return _desc;
	}

	public void set_desc(boolean desc) {
		_desc = desc;
	}

	public String[] get_orderby() {
		if(_orderby == null) {
			_orderby = new String[0];
		}
		return _orderby;
	}

	public void set_orderby(String[] _orderby) {
		this._orderby = _orderby;
	}

	public int get_pageno() {
	    if(_pageno <= 0) {
	        _pageno = 1;
	    }
		return _pageno;
	}

	public void set_pageno(String _pageno) {
		if(StringUtils.isNotBlank(_pageno)) {
			this._pageno = Integer.parseInt(_pageno);
		} else {
			this._pageno = 1;
		}
	}
	
	public void setPageno(int pageno) {
		this._pageno = pageno;
	}

	public int get_pagesize() {
	    if ( _pagesize <= 0 ) {
            _pagesize = Integer.MAX_VALUE;
        }
		return _pagesize;
	}

	public void set_pagesize(String _pagesize) {
		if(StringUtils.isNotBlank(_pagesize)) {
			this._pagesize = Integer.parseInt(_pagesize);
		} else {
			this._pagesize = 10;
		}
	}
	
	public void setPagesize(int pagesize) {
		this._pagesize = pagesize;
	}

    public String[] get_sort() {
    	if(_sort == null) {
    		_sort = new String[0];
    	}
        return _sort;
    }

    public void set_sort(String[] _sort) {
        this._sort = _sort;
    }

	public String[] get_selectItems() {
		return _selectItems;
	}

	public void set_selectItems(String[] items) {
		_selectItems = items;
	}
	
	public void addSort(String field, String sort) {
		if(StringUtils.isBlank(field)) {
			return;
		}
		List<String> orderbys = MyArrayUtils.toList(get_orderby());
		List<String> sorts = MyArrayUtils.toList(get_sort());
		orderbys.add(field);
		if(StringUtils.isBlank(sort)) {
			sort="asc";
		}
		sorts.add(sort);
		_orderby = orderbys.toArray(new String[orderbys.size()]);
		_sort = sorts.toArray(new String[sorts.size()]);
	}

	
	public String getUniqueParamName(String name) {
		return findNotContainKey(name, 0);
	}
	

	public String get_pk() {
		return _pk;
	}

	public void set_pk(String _pk) {
		this._pk = _pk;
	}


	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public Boolean getRestrictQueryQuantity() {
		return restrictQueryQuantity;
	}

	public void setRestrictQueryQuantity(Boolean restrictQueryQuantity) {
		this.restrictQueryQuantity = restrictQueryQuantity;
	}

	public List<Set<String>> getOrOperations() {
		return orOperations;
	}
	
	public void addOrOperation(String... opers) {
		if(opers.length <= 1) {
			throw new RuntimeException("or语句必需2个或以上的条件合成");
		}
		Set<String> sets = new HashSet<String>();
		for (int i = 0; i < opers.length; i++) {
			sets.add(opers[i]);
		}
		orOperations.add(sets);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String[] getOrderby() {
		return _orderby;
	}

	public void setOrderby(String[] orderby) {
		this._orderby = orderby;
	}

	public String[] getSort() {
		return _sort;
	}

	public void setSort(String[] sort) {
		this._sort = sort;
	}

	public String[] getSelectItems() {
		return _selectItems;
	}

	public void setSelectItems(String[] selectItems) {
		this._selectItems = selectItems;
	}

	public String getPk() {
		return _pk;
	}

	public void setPk(String pk) {
		this._pk = pk;
	}

}
