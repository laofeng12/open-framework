package org.ljdp.component.bean;

/**
 * 自定义的对象属性复制，把T中的属性复制到E
 * @author hzy
 *
 */
public interface PropertyCopy<E, T> {
	
	public void copy(E dest, T orig) throws Exception;
}
