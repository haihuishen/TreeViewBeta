package com.shen.tree.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Node {

	/** 当前"节点id" */
	private int id;

	/** 根节点pId为0 -- 父节点id */
	private int pId = 0;

	/** 当前"节点名字" */
	private String name;

	/** 当前的级别 */
	private int level;

	/** 是否展开 -- 默认 false*/
	private boolean isExpand = false;

	/** 展开或收缩的图片 */
	private int icon;

	/** 下一级的子Node */
	private List<Node> children = new ArrayList<Node>();

	/** 父Node */
	private Node parent;

	public Node() {
	}

	public Node(int id, int pId, String name) {

		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	/** 展开或收缩的图片 */
	public int getIcon()
	{
		return icon;
	}
	/** 展开或收缩的图片 */
	public void setIcon(int icon)
	{
		this.icon = icon;
	}

	/** 当前"节点id" */
	public int getId()
	{
		return id;
	}
	/** 当前"节点id" */
	public void setId(int id)
	{
		this.id = id;
	}

	/** 根节点pId为0 -- 父节点id */
	public int getpId()
	{
		return pId;
	}
	/** 根节点pId为0 -- 父节点id */
	public void setpId(int pId)
	{
		this.pId = pId;
	}

	/** 当前"节点名字" */
	public String getName()
	{
		return name;
	}
	/** 当前"节点名字" */
	public void setName(String name)
	{
		this.name = name;
	}


	public void setLevel(int level)
	{
		this.level = level;
	}

	/** 是否展开 -- 默认 false*/
	public boolean isExpand()
	{
		return isExpand;
	}

	/** 下一级的子Node */
	public List<Node> getChildren()
	{
		return children;
	}
	/** 下一级的子Node */
	public void setChildren(List<Node> children)
	{
		this.children = children;
	}

	/** 父Node */
	public Node getParent()
	{
		return parent;
	}
	/** 父Node */
	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	/**
	 * 是否为"根节点"
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * 判断"父节点"是否展开
	 * 
	 * @return
	 */
	public boolean isParentExpand() {

		if (parent == null)
			return false;
		return parent.isExpand();
	}

	/**
	 * 是否是叶子界点
	 * 
	 * @return
	 */
	public boolean isLeaf()
	{
		return children.size() == 0;
	}

	/** 获取level -- 缩进 */
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * 设置展开
	 * 
	 * @param isExpand
	 */
	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
		if (!isExpand) {

			for (Node node : children) {
				node.setExpand(isExpand);
			}
		}
	}

}
