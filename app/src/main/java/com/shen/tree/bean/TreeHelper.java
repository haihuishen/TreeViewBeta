package com.shen.tree.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.shen.tree_view.R;
/**
 *
 */
public class TreeHelper
{
	/**
	 * 传入我们的普通bean，转化为我们排序后的Node
	 * 
	 * @param datas						数据
	 * @param defaultExpandLevel		默认展开几级树
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel)
			throws IllegalArgumentException, IllegalAccessException {
		List<Node> result = new ArrayList<Node>();
		// 将用户数据转化为List<Node>
		List<Node> nodes = convetData2Node(datas);

		// 拿到根节点
		List<Node> rootNodes = getRootNodes(nodes);

		// 排序以及设置Node间关系
		for (Node node : rootNodes) {
			addNode(result, node, defaultExpandLevel, 1);
		}
		return result;
	}

	/**
	 * 过滤出所有"可见的Node"
	 * 
	 * @param nodes
	 * @return
	 */
	public static List<Node> filterVisibleNode(List<Node> nodes) {

		List<Node> result = new ArrayList<Node>();

		for (Node node : nodes) {

			// 如果为跟节点，或者上层目录为展开状态
			if (node.isRoot() || node.isParentExpand()) {
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 将我们的"数据"转化为"树的节点"
	 * 
	 * @param datas
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static <T> List<Node> convetData2Node(List<T> datas)
			throws IllegalArgumentException, IllegalAccessException {

		List<Node> nodes = new ArrayList<Node>();
		Node node = null;

		for (T t : datas) {
			int id = -1;
			int pId = -1;
			String label = null;

			/** 反射+注解 获取成员变量的值 */
			Class<? extends Object> clazz = t.getClass();
			Field[] declaredFields = clazz.getDeclaredFields();

			//反射获取值
			for (Field f : declaredFields) {

				if (f.getAnnotation(TreeNodeId.class) != null){			//获取id
					f.setAccessible(true);								//设置可见
					id = f.getInt(t);
				}
				if (f.getAnnotation(TreeNodePid.class) != null){		//获取pId
					f.setAccessible(true);								//设置可见
					pId = f.getInt(t);
				}
				if (f.getAnnotation(TreeNodeLabel.class) != null){		//获取label
					f.setAccessible(true);								//设置可见
					label = (String) f.get(t);
				}
				if (id != -1 && pId != -1 && label != null){

					break;
				}
			}

			//加入节点
			node = new Node(id, pId, label);
			nodes.add(node);
		}

		/**
		 * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
		 */
		for (int i = 0; i < nodes.size(); i++) {

			Node n = nodes.get(i);

			for (int j = i + 1; j < nodes.size(); j++) {
				Node m = nodes.get(j);
				if (m.getpId() == n.getId()) {				// 判断父子关系,然后挂到对应的list中
					n.getChildren().add(m);
					m.setParent(n);

				} else if (m.getId() == n.getpId()) {
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}

		// 设置图片
		for (Node n : nodes) {
			setNodeIcon(n);
		}

		return nodes;
	}

	/**
	 * 获取"根节点列表"
	 *
	 * @param nodes
	 * @return
	 */
	private static List<Node> getRootNodes(List<Node> nodes)
	{
		List<Node> root = new ArrayList<Node>();
		for (Node node : nodes) {

			if (node.isRoot())
				root.add(node);
		}
		return root;
	}


	/**
	 * 把"一个节点"上的所有的内容都挂上去
	 *
	 *
	 * @param nodes
	 * @param node
	 * @param defaultExpandLeval		默认展开几级树
	 * @param currentLevel				当前是第几级
	 */
	private static void addNode(List<Node> nodes, Node node, int defaultExpandLeval, int currentLevel) {

		nodes.add(node);
		if (defaultExpandLeval >= currentLevel) {
			node.setExpand(true);
		}

		if (node.isLeaf())
			return;

		for (int i = 0; i < node.getChildren().size(); i++) {
			addNode(nodes, node.getChildren().get(i), defaultExpandLeval, currentLevel + 1);
		}
	}

	/**
	 * 根据"是否展开"为"这个节点设置图片"
	 * 
	 * @param node
	 */
	private static void setNodeIcon(Node node) {

		if (node.getChildren().size() > 0 && node.isExpand()) {
			node.setIcon(R.drawable.tree_ex);

		} else if (node.getChildren().size() > 0 && !node.isExpand()) {
			node.setIcon(R.drawable.tree_ec);

		} else
			node.setIcon(-1);
	}

}
