package com.huoniao.oc.bean;
public class NodeResource {
	public String parentId;
	public String title;
	public String value;
	//protected int iconId;
	public String curId;

	public NodeResource(String parentId, String curId, String title,
						String value) {//, int iconId
		super();
		this.parentId = parentId;
		this.title = title;
		this.value = value;
		//this.iconId = iconId;
		this.curId = curId;
	}

	public String getParentId() {
		return parentId;
	}

	public String getTitle() {
		return title;
	}

	public String getValue() {
		return value;
	}

//	public int getIconId() {
//		return iconId;
//	}

	public String getCurId() {
		return curId;
	}

}
