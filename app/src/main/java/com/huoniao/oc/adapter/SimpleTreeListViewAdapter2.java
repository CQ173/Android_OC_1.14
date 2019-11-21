package com.huoniao.oc.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.common.tree.TreeHelper;
import com.huoniao.oc.common.tree.adapter.TreeListViewAdapter2;

import java.util.List;


public class SimpleTreeListViewAdapter2<T> extends TreeListViewAdapter2<T>{
	private boolean outletsOff;
	public SimpleTreeListViewAdapter2(ListView tree, Context context,
									  List<T> datas, int defaultExpandLevel,boolean outletsOff)
			throws IllegalArgumentException, IllegalAccessException
	{
		super(tree, context, datas, defaultExpandLevel);
		this.outletsOff = outletsOff;
	}

	boolean flagColorDefault=true;
	@Override
	public View getConvertView(Node node, final int position, View convertView,
							   ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.lv_train_ownership_item, parent, false);
			holder = new ViewHolder();
			holder.mIcon = (ImageView) convertView
					.findViewById(R.id.id_item_icon);
			holder.mText = (TextView) convertView
					.findViewById(R.id.id_item_text);
			convertView.setTag(holder);
			holder.ll_tr = (LinearLayout) convertView.findViewById(R.id.ll_tr);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if (node.getIcon() == -1)
		{
			holder.mIcon.setVisibility(View.VISIBLE);
			holder.mIcon.setImageResource(R.drawable.tree_down);
		} else
		{
			holder.mIcon.setVisibility(View.VISIBLE);
			holder.mIcon.setImageResource(node.getIcon());
		}

		if(node.getAllTreeNode().type.equals("2") || node.getAllTreeNode().type.equals("3")){  // 2 火车站   3是代售点
			if(outletsOff &&  node.getAllTreeNode().type.equals("2")){
				holder.mIcon.setVisibility(View.VISIBLE);
			}else{
				holder.mIcon.setVisibility(View.INVISIBLE);
			}

	            if(flagColorDefault){  //标记第一个默认代售点颜色
					flagColorDefault = false;
					setSelectedItem(position, Long.parseLong(node.getAllTreeNode().id.substring(0,15),16));
				}
			}

	 	  if(selected.get(position) && parentPosition==Long.parseLong(node.getAllTreeNode().id.substring(0,15),16) ){
			holder.mText .setTextColor(MyApplication.mContext.getResources().getColor(R.color.gbColor));
	        }else{
	        holder.mText.setTextColor(MyApplication.mContext.getResources().getColor(R.color.grayss));
	        }

		holder.mText.setText(node.getName());
		holder.ll_tr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				expandOrCollapse(position);
				if (mListener != null)
				{
					mListener.onClick(mVisibleNodes.get(position), position);
				}
			}
		});


		return convertView;
	}

	private class ViewHolder
	{
		ImageView mIcon;
		TextView mText;
		LinearLayout ll_tr;
	}

	/**
	 * 动态插入节点并且展开
	 * 
	 * @param position
	 * @param
	 */
	public void addExtraNode(int position,List<AllTreeNode>  allTreeList)
	{
		for (AllTreeNode  allTreeNode:allTreeList
			 ) {
			Node node = mVisibleNodes.get(position);
			int indexOf = mAllNodes.indexOf(node);
			// Node
			Node extraNode = new Node(-1, node.getId(), allTreeNode.name,allTreeNode);
			extraNode.setParent(node);
			node.getChildren().add(extraNode);
			mAllNodes.add(indexOf + 1, extraNode);

			mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		}
		expandOrCollapse(position); //点击展开节点
		notifyDataSetChanged();

	}

	 
	private SparseBooleanArray   selected = new SparseBooleanArray();	    
	    int oid = -1;
	    long parentPosition = -1;
	    public void setSelectedItem(int item,long groupPosition){
	        parentPosition = groupPosition;
	        if(oid != -1){
	            selected.put(oid,false);
	        }
	        selected.put(item,true);
	        oid = item;
	    }

}
