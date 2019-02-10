package org.ljdp.component.namespace;

/**
 * 命名空间管理器
 * @author HZY
 *
 */
public interface NameSpaceManager {

	/**
	 * 根据一实体或接口和名称类型命名一个实体功能实现类名或接口实现类名。
	 * @param entity 实体类，接口类
	 * @param name 名称类型
	 * @return 实体功能实现类或接口实现类
	 * @throws NameNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public String classNameing(Class entity, String name) throws NameNotFoundException;

	/**
	 * 根据一实体或接口和名称类型寻找一个实体功能实现类或接口实现类
	 * @param entity
	 * @param name
	 * @return
	 * @throws NameNotFoundException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public Class classSeek(Class entity, String name) throws NameNotFoundException, ClassNotFoundException;
}