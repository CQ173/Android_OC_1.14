package com.huoniao.oc.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.MessageBean;
import com.huoniao.oc.common.sideslip_listview.swipemenu.adapter.BaseSwipListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */

public class MessageAdatpter  extends BaseSwipListAdapter {

    private List<MessageBean.DataBean> messageBeanList;
      public void setData(List<MessageBean.DataBean> messageBeanList){
        this.messageBeanList = messageBeanList;
      };
    @Override
    public int getCount() {
        if(messageBeanList.size()>0){
            return messageBeanList.size();
        }else{
            return 0;
        }

    }

    // 返回列表 行视图的种类
    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {

        if(messageBeanList.get(position).tag.equals("1") || messageBeanList.get(position).tag.equals("2")){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public MessageBean.DataBean getItem(int position) {
        return messageBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if(type == 0) {
            if (convertView == null) {
                convertView = View.inflate(MyApplication.mContext,
                        R.layout.item_mymessage_w, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            MessageBean.DataBean item = getItem(position);
            holder.tv_messageContent.setText(item.getContent());
            SimpleDateFormat timeFormat
                    = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
            SimpleDateFormat timeFormat2  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date  date =new Date();
            try {
                date = timeFormat2.parse(item.getInfoDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formentDate= timeFormat.format(date);
            System.out.println(timeFormat.format(date));

            holder.tv_date.setText(formentDate);

           // holder.tv_circle_red.setVisibility(View.VISIBLE);
            holder.tv_circle_red.setTag(item.getId());   //设置tag防止条目错乱


            if( holder.tv_circle_red.getTag().equals(item.getId())){
               String infoStatus = item.getInfoStatus()== null ? "1":item.getInfoStatus();
                if(infoStatus.equals("1")){
                    holder.tv_circle_red.setVisibility(View.GONE);
                }else{
                    holder.tv_circle_red.setVisibility(View.VISIBLE);
                }
            }

            return convertView;
        }else if(type==1){
            if (convertView == null) {
                convertView = View.inflate(MyApplication.mContext,
                        R.layout.item_mymessage_o, null);
                new ViewHolder2(convertView);
            }
            ViewHolder2 holder = (ViewHolder2) convertView.getTag();
            MessageBean.DataBean item = getItem(position);
            if(item.tag.equals("1")) {
                holder.tv_date.setText("今天");
            }else{
                holder.tv_date.setText("之前");
            }
            return convertView;
        }

        return convertView;
    }

   static  class ViewHolder {
        TextView tv_messageContent;
        TextView tv_circle_red;
        TextView tv_date;
        public ViewHolder(View view) {
            tv_messageContent = (TextView) view.findViewById(R.id.tv_messageContent);
            tv_circle_red = (TextView) view.findViewById(R.id.tv_circle_red);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            view.setTag(this);
        }
    }
    static  class ViewHolder2 {
        TextView tv_date;
        public ViewHolder2(View view) {
            tv_date = (TextView) view.findViewById(R.id.tv_date);

            view.setTag(this);
        }
    }

    @Override
    public boolean getSwipEnableByPosition(int position) {
        int type = getItemViewType(position);
        if(type ==1){
            return false;
        }
        return true;
    }



}
