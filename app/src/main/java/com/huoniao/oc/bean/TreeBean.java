package com.huoniao.oc.bean;

import com.huoniao.oc.common.tree.annotation.TreeNodeId;
import com.huoniao.oc.common.tree.annotation.TreeNodeLabel;
import com.huoniao.oc.common.tree.annotation.TreeNodePid;
import com.huoniao.oc.common.tree.annotation.TreeNodeYy;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/3.
 */

public class TreeBean implements Serializable{
    @TreeNodeId
    private long _id;
    @TreeNodePid
    private long parentId;
    @TreeNodeLabel
    private String name;
    private long length;
    private String desc;
    @TreeNodeYy
    private AllTreeNode allTreeNode;
   public TreeBean(){}

    public TreeBean(long _id, long parentId, String name,AllTreeNode allTreeNode)
    {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
        this.allTreeNode = allTreeNode;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public AllTreeNode getAllTreeNode() {
        return allTreeNode;
    }

    public void setAllTreeNode(AllTreeNode allTreeNode) {
        this.allTreeNode = allTreeNode;
    }
}
