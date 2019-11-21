package com.huoniao.oc.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;

import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.common.tree.TreeHelper;
import com.huoniao.oc.common.tree.adapter.TreeListViewAdapter;

import java.util.List;


public class SimpleTreeListViewAdapter4<T> extends TreeListViewAdapter<T>
{
	public SimpleTreeListViewAdapter4(ListView tree, Context context,
									  List<T> datas, int defaultExpandLevel)
			throws IllegalArgumentException, IllegalAccessException
	{
		super(tree, context, datas, defaultExpandLevel);
	}

	public void SimpleTreeListViewAdapter4() {

	}

	boolean flagColorDefault=true;
	@Override
	public View getConvertView(Node node, int position, View convertView,
							   ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_achievement_top, parent, false);
			holder = new ViewHolder();

			holder.tv_ranking = (TextView) convertView.findViewById(R.id.tv_ranking);
			convertView.setTag(holder);
			holder.tv_officeName = (TextView) convertView.findViewById(R.id.tv_officeName);
			holder.tv_salesAmount = (TextView) convertView.findViewById(R.id.tv_salesAmount);

			setTextViewGravity(holder.tv_ranking);
			setTextViewGravity(holder.tv_officeName);
			setTextViewGravity(holder.tv_salesAmount);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		AllTreeNode allTreeNode = node.getAllTreeNode();

		if ("3".equals(allTreeNode.char1))
		{
			holder.tv_ranking.setTextColor(MyApplication.mContext.getResources().getColor(R.color.grayfont));
			holder.tv_officeName.setTextColor(MyApplication.mContext.getResources().getColor(R.color.grayfont));
			holder.tv_salesAmount.setTextColor(MyApplication.mContext.getResources().getColor(R.color.grayfont));
			holder.tv_ranking.setTextSize(12);
			holder.tv_officeName.setTextSize(12);
			holder.tv_salesAmount.setTextSize(12);
			//holder.mText.setTextColor(MyApplication.mContext.getResources().getColor(R.color.gray));
			/*holder.mIcon.setVisibility(View.VISIBLE);
			holder.mIcon.setImageResource(R.drawable.tree_down);*/
		} else
		{
			holder.tv_ranking.setTextColor(MyApplication.mContext.getResources().getColor(R.color.alphaBlack));
			holder.tv_officeName.setTextColor(MyApplication.mContext.getResources().getColor(R.color.alphaBlack));
			holder.tv_salesAmount.setTextColor(MyApplication.mContext.getResources().getColor(R.color.alphaBlack));

			holder.tv_ranking.setTextSize(12);
			holder.tv_officeName.setTextSize(12);
			holder.tv_salesAmount.setTextSize(12);
			//holder.mText.setTextColor(MyApplication.mContext.getResources().getColor(R.color.alphaBlack));
		/*	holder.mIcon.setVisibility(View.VISIBLE);
			holder.mIcon.setImageResource(node.getIcon());*/
		}

		/*if(node.getAllTreeNode().type.equals("3")){  //3是代售点
			holder.mIcon.setVisibility(View.INVISIBLE);
	            if(flagColorDefault){  //标记第一个默认代售点颜色
					flagColorDefault = false;
					setSelectedItem(position, Long.parseLong(node.getAllTreeNode().id.substring(0,15),16));
				}
			}*/

	 /*	  if(selected.get(position) && parentPosition==Long.parseLong(node.getAllTreeNode().id.substring(0,15),16) ){
			  holder.mText .setTextColor(MyApplication.mContext.getResources  ().getColor(R.color.gbColor));
	        }else{
	        	
	        	 holder.mText.setTextColor(MyApplication.mContext.getResources  ().getColor(R.color.grayss));
	        }*/

		holder.tv_ranking.setText(allTreeNode.ranking);
		holder.tv_officeName.setText(allTreeNode.officeName);
		holder.tv_salesAmount.setText(allTreeNode.salesAmount);
		return convertView;
	}

	private class ViewHolder{
		TextView tv_ranking;
		TextView tv_officeName;
		TextView tv_salesAmount;

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

	public void expandOrCollapseA(){
		expandOrCollapse(0); //点击展开节点
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
	/**
	 *
	 *  @param tv
	 *  主要判断显示的TV 内容只为一行则居中显示，内容超过两行 则左对齐
	 * */
	private  void setTextViewGravity(final TextView tv){
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if(tv.getLineCount() ==1){
					tv.setGravity(Gravity.CENTER_VERTICAL);

				}else{
					tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);

				}return true;

			}
		});
	}

	public void notifyDataSet(){
		notifyDataSetChanged();
	}



}
