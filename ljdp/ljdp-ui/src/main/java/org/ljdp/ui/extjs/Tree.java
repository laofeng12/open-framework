package org.ljdp.ui.extjs;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class Tree {
	private TreeNode root;
	
	public Tree(String id, String text) {
		root = new TreeNode(id, text);
	}
	
	/**
	 * 只有一层的树
	 * @param datas
	 * @param proMapper
	 */
	@SuppressWarnings("rawtypes")
	public void buildTreeOneLv(List datas, Map<String, String> proMapper) {
		try {
			for(Object obj : datas) {
				TreeNode node = createNode(obj, proMapper);
				root.appendChild(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 使用建树算法1生成一棵Ext树.
	 * 说明：
	 * 1. datas已经按深度排序好(即深度浅的node放在前面); 
	 * 2. 子节点的ID是由父节点ID加上一个3位的序列生成.
	 * @param datas
	 */
	@SuppressWarnings("rawtypes")
	public void buildTreeBySortID(List datas, Map<String, String> proMapper) {
		try {
			for(Object obj : datas) {
				String parent = proMapper.get("node.parent");
				try {
					parent = BeanUtils.getProperty(obj, parent);
				} catch (NoSuchMethodException e1) {
					//e1.printStackTrace();
				}
				if(parent.equals(root.getId())) {
					TreeNode node = createNode(obj, proMapper);
					root.appendChild(node);
				} else {
					String[] parentids = getParentIDs(parent);
					ArrayList<TreeNode> currentList = root.getChildren();
					TreeNode needNode = null;
					for(String pid : parentids) {
						needNode = findNode(currentList, pid);
						if(needNode != null) {
							currentList = needNode.getChildren();
						}
					}
					if(needNode != null) {
						needNode.setLeaf(false);
						TreeNode node = createNode(obj, proMapper);
						needNode.appendChild(node);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public TreeNode getNodeById(String id, TreeNode curnode) {
		if(curnode.getId().equals(id)) {
			return curnode;
		}
		ArrayList<TreeNode> childs = curnode.getChildren();
		int len  = childs.size();
		for(int i = 0; i < len; ++i) {
			TreeNode node = childs.get(i);
			TreeNode resnode = getNodeById(id, node);
			if(resnode != null) {
				return resnode;
			}
		}
		return null;
	}
	
	/**
	 * 使用建树算法2生成一棵Ext树.
	 * 说明：
	 * 1. datas已经按深度排序好(即深度浅的node放在前面); 
	 * @param datas
	 * @param proMapper
	 */
	@SuppressWarnings("rawtypes")
	public void buildTreeBySort(List datas, Map<String, String> proMapper) {
		boolean autoleaf = true;
		if(proMapper.containsKey("autoLeaf")) {
			if(proMapper.get("autoLeaf").equalsIgnoreCase("false")) {
				autoleaf = false;
			}
		}
		try {
			for(Object obj : datas) {
				String parent = proMapper.get("node.parent");
				try {
					parent = BeanUtils.getProperty(obj, parent);
				} catch (NoSuchMethodException e1) {
					//e1.printStackTrace();
				}
				TreeNode needNode = getNodeById(parent, root);
				if(needNode != null) {
					needNode.setLeaf(false);
					TreeNode node = createNode(obj, proMapper);
					if(!autoleaf) {
						node.setLeaf(false);
					}
					needNode.appendChild(node);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public TreeNode createNode(Object obj, Map<String, String> proMapper)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		String id = BeanUtils.getProperty(obj, proMapper.get("node.id"));
		String text = BeanUtils.getProperty(obj, proMapper.get("node.text"));
		TreeNode node = new TreeNode(id, text, true);
		if(proMapper.containsKey("node.iconCls")) {
			String iconCls = BeanUtils.getProperty(obj, proMapper.get("node.iconCls"));
			node.setIconCls(iconCls);
		}
		if(proMapper.containsKey("node.uri")) {
			String uri = BeanUtils.getProperty(obj, proMapper.get("node.uri"));
			node.setUri(uri);
		}
		String check = proMapper.get("node.checked");
		if(StringUtils.isNotBlank(check)) {
			if(check.equalsIgnoreCase("enable")) {				
				node.setChecked(false);
			} else {
				Boolean checked = (Boolean)PropertyUtils.getProperty(
						obj, proMapper.get("node.checked"));
				if(checked != null ) {
					if(checked.booleanValue()) {
						node.setChecked(true);
					} else {
						node.setChecked(false);
					}
				}
			}
		}
		String leaf = proMapper.get("node.leaf");
		if(leaf != null && (leaf.equalsIgnoreCase("false") || leaf.equalsIgnoreCase("true"))) {
			node.setLeaf(new Boolean(leaf).booleanValue());
		} else {
			try {
				Boolean isLeaf = (Boolean)PropertyUtils.getProperty(obj, leaf);
				node.setLeaf(isLeaf.booleanValue());
			}catch(Exception e) {
			}
		}
		return node;
	}
	
	public TreeNode findNode(List<TreeNode> list, String id) {
		for(TreeNode node : list) {
			if(node.getId().equals(id)) {
				return node;
			}
		}
		return null;
	}
	
	public String[] getParentIDs(String parent) {
		ArrayList<String> ids = new ArrayList<String>();
		for(int len = 6; len <= 30 && len < parent.length(); len += 2) {
			String id = parent.substring(0, len);
			ids.add(id);
		}
		ids.add(parent);
		return ids.toArray(new String[] {});
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	public static void main(String[] args) {
		Tree tree = new Tree("000", "root");
		System.out.println(ArrayUtils.toString(tree.getParentIDs("0001010203")));
	}
	
}
