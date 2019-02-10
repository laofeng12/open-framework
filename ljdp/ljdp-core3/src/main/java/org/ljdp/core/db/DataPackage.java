package org.ljdp.core.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.ljdp.component.bean.PropertyCopy;

/**
 * <p>一般查询返回的结果集 </p>
 *
 */
public class DataPackage<T> implements Serializable {
	private static final long serialVersionUID = 8493919996832381918L;

	private int totalCount;

	private int pageSize;

	private int pageNo;

	private Collection<T> datas;
	
	public DataPackage() {
		
	}

	public DataPackage(Collection<T> datas, int totalCount) {
		super();
		this.totalCount = totalCount;
		this.datas = datas;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int rowCount) {
		this.totalCount = rowCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Collection<T> getDatas() {
		return datas;
	}

	public void setDatas(Collection<T> datas) {
		this.datas = datas;
	}

	public boolean hasSomething() {
		return (datas != null && datas.size() > 0);
	}
	
	public int getTotalPage(){
		return (int) Math.ceil(((double) getTotalCount()) / ((double) getPageSize()));
    }
	
	public List<T> toList() {
    	return (List<T>)getDatas();
    }
    
	public LinkedList<T> toLinkeList() {
    	LinkedList<T> list = new LinkedList<T>();
    	for(T obj : getDatas()) {
    		list.add(obj);
    	}
    	return list;
    }
    
	public T getFirst() {
    	List<T> list = toList();
    	if(list.size() > 0) {
    		return list.get(0);
    	}
    	return null;
    }
	
	public boolean isEmpty() {
		if(datas != null && datas.size() > 0) {
			return false;
		}
		return true;
	}

    /**
     * 将DataPackage的Data转换为指定的类型Class cls的List
     * @param cls 需转换为哪种类型的List
     */
	public <E> List<E> copyToList(Class<E> cls, PropertyCopy<E, T> pc) {
        List<E> list = new ArrayList<E>(getDatas().size());
        for (Iterator<T> it = getDatas().iterator(); it.hasNext();) {
            T orig = it.next();
            try {
                E vo = cls.newInstance();
                PropertyUtils.copyProperties(vo, orig);
                if(pc != null) {
                	pc.copy(vo, orig);
                }
                list.add(vo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
	
	/**
	 * 获取指定字段的值集合
	 * @param field
	 * @return
	 */
	public List<?> getPropertyList(String field){
		List<Object> list = new ArrayList<Object>(getDatas().size());
		for (Iterator<T> it = getDatas().iterator(); it.hasNext();) {
			 T orig = it.next();
			 try {
				 Object val = PropertyUtils.getProperty(orig, field);
				list.add(val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public <E> List<E> copyToList(Class<E> cls) {
		return copyToList(cls, null);
	}
    
	/**
	 * 把存放T对象的DataPackage复制并转换为存放E对象的DataPackage
	 * 先使用org.apache.commons.beanutils.PropertyUtils复制
	 * 再使用org.opensource.ljdp.component.bean.PropertyCopy进行自定义复制
	 * 通常用于把数据库对象转换为页面显示的视图对象，
	 * 因为源数据库对象中一些关系映射的值无法在视图层使用，因为这些值设置了延时加载（LAZY）
	 * @param cls
	 * @return
	 */
	public <E> DataPackage<E> copyToNewDP(Class<E> voCls, PropertyCopy<E, T> pc) throws Exception{
		List<E> list = new ArrayList<E>();
		Iterator<T> it = datas.iterator();
		while (it.hasNext()) {
			T orig = it.next();
			E vo = voCls.newInstance();
			PropertyUtils.copyProperties(vo, orig);
			list.add(vo);
			if(pc != null) {
				pc.copy(vo, orig);
			}
		}
		DataPackage<E> dp = new DataPackage<E>(list, this.totalCount);
		return dp;
	}
	
	/**
	 * 把存放T对象的DataPackage复制并转换为存放E对象的DataPackage
	 * 使用org.apache.commons.beanutils.PropertyUtils复制
	 * 通常用于把数据库对象转换为页面显示的视图对象，
	 * 因为源数据库对象中一些关系映射的值无法在视图层使用，因为这些值设置了延时加载（LAZY）
	 * @param cls
	 * @return
	 */
	public <E> DataPackage<E> copyToNewDP(Class<E> voCls) throws Exception{
		return copyToNewDP(voCls, null);
	}
}
