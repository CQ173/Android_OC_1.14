package com.huoniao.oc.common.tree;

import com.huoniao.oc.bean.AllTreeNode;

import java.util.ArrayList;
import java.util.List;

public class Node
{
	public Node()
	{
	}

	public Node(long id, long pId, String name,AllTreeNode yy)
	{
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.yy = yy;
	}



	private long id;
	/**
	 * 跟节点的pid=0
	 */
	private long pId = 0;
	private String name;
	private AllTreeNode yy;
	/**
	 * 树的层级
	 */
	private int level;
	/**
	 * 是否是展开
	 */
	private boolean isExpand = false;
	private int icon;

	private Node parent;
	private List<Node> children = new ArrayList<Node>();

	
	public AllTreeNode  getAllTreeNode(){
		return yy;
	}
	public void setAllTreeNode(AllTreeNode yy){
		this.yy = yy;
	}
	public long getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public long getpId()
	{
		return pId;
	}

	public void setpId(int pId)
	{
		this.pId = pId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getIcon()
	{
		return icon;
	}

	public void setIcon(int icon)
	{
		this.icon = icon;
	}

	public Node getParent()
	{
		return parent;
	}

	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	public List<Node> getChildren()
	{
		return children;
	}

	public void setChildren(List<Node> children)
	{
		this.children = children;
	}

	/**
	 * 属否是根节点
	 * 
	 * @return
	 */
	public boolean isRoot()
	{
		return parent == null;
	}

	/**
	 * 判断当前父节点的收缩状态
	 * 
	 * @return
	 */
	public boolean isParentExpand()
	{
		if (parent == null)
			return false;
		return parent.isExpand();
	}

	/**
	 * 是否是叶子节点
	 * 
	 * @return
	 */
	public boolean isLeaf()
	{
		return children.size() == 0;
	}

	/**
	 * 得到当前节点的层级
	 * @return
	 */
	public int getLevel()
	{
		return parent == null ? 0 : parent.getLevel() + 1;
	}
	
	public boolean isExpand()
	{
		return isExpand;
	}

	public void setExpand(boolean isExpand)
	{
		this.isExpand = isExpand;
		
		if(!isExpand)
		{
			for(Node node :children)
			{
				node.setExpand(false);
			}
		}
	}
	
	

	public void setLevel(int level)
	{
		this.level = level;
	}

	@Override
	public String toString()
	{
		return "Node [id=" + id + ", pId=" + pId + ", name=" + name
				+ ", level=" + level + ", isExpand=" + isExpand + ", icon="
				+ icon + "]";
	}

}
