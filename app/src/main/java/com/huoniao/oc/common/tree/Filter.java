package com.huoniao.oc.common.tree;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.R;
import com.huoniao.oc.bean.BaseData;
import com.huoniao.oc.bean.FilterDataBean;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by admin on 2017/8/30.
 */

public class Filter implements View.OnClickListener, AdapterView.OnItemClickListener {
    List<FilterDataBean.DataBean> list;
    private GridView gridView;
    private ImageView iv_back;
    private TextView tv_save;
    private MyPopWindow myPopWindow;
    private List<FilterDataBean.DataBean> linList = new ArrayList<>(); //临时记录的集合
    private CommonAdapter<FilterDataBean.DataBean> commonAdapter;

    public Filter(List<FilterDataBean.DataBean> t ){
        list = t;
        for (int i = 0; i < list.size(); i++) {
            Log.d("sort1",list.get(i).getSort()+"");
        }
        Collections.sort(list, new TypeOrStateSort());
        for (int i = 0; i < list.size(); i++) {
            Log.d("sort2",list.get(i).getSort()+"");
        }

    }

    //多个用逗号隔开
    public void filterState(final Activity activity, View textView){
        myPopWindow = new MyPopAbstract(){

            private TextView tv_confirm;

            @Override
            protected void setMapSettingViewWidget(View view) {
                iv_back = (ImageView) view.findViewById(R.id.iv_back);
                iv_back.setVisibility(View.GONE);
                tv_save = (TextView) view.findViewById(R.id.tv_save);
                tv_save.setVisibility(View.VISIBLE);
                tv_save.setText("取消");
                gridView = (GridView) view.findViewById(R.id.gv_filter_transaction);
                tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                setAdapter(activity);
                tv_save.setOnClickListener(Filter.this);
                tv_confirm.setOnClickListener(Filter.this);
                gridView.setOnItemClickListener(Filter.this);

            }

            @Override
            protected int layout() {
                return R.layout.pop_filter_transaction;
            }
        }.poPwindow(activity,true).showAsDropDown(textView);
    }

   @TargetApi(16)
    private void setAdapter(final Activity activity) {
        if(list.size()>0) {
            commonAdapter = new CommonAdapter<FilterDataBean.DataBean>(activity,list, R.layout.filter_text ) {
                @Override
                public void convert(ViewHolder holder, FilterDataBean.DataBean t) {
                      TextView tv = holder.getView(R.id.tv);
                    BaseData baseData =  t;
                    tv.setText(baseData.label);
                    if(baseData.flag){
                        setTextStyle(tv, R.drawable.textbackgroud3, Color.rgb(255, 255, 255));
                    }else{
                        setTextStyle(tv, R.drawable.textbackgroud4, Color.rgb(21, 21, 23));
                    }
                }
            };

            gridView.setAdapter(commonAdapter);
        }
    }

    private void setTextStyle(TextView textView, int resid, int color){
        textView.setBackgroundResource(resid);
        textView.setTextColor(color);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_save:
               if(filterLinstener != null){
                   filterLinstener.cancle();
               }
                break;
            case R.id.tv_confirm:
                if(filterLinstener!=null){

                    if(linList.size()>0){
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0; i < linList.size(); i++) {
                            stringBuffer.append(linList.get(i).getValue()+",");
                        }

                            filterLinstener.result(stringBuffer.toString());

                    }else{
                        filterLinstener.result("");
                    }

                }
                break;
        }
        myPopWindow.dissmiss();


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {
        FilterDataBean.DataBean db = (FilterDataBean.DataBean) list.get(j);
        if(db.flag){ //选中状态
            if (!linList.isEmpty()) {
                for (int i = 0; i < linList.size(); i++) {
                    BaseData db2 = linList.get(i);
                    if (db2.id.equals(db.id)) {
                        linList.remove(db2);
                    }

                }
                db.flag=false;
            }
        }else{
            db.flag=true;
            linList.add(db);
        }

        commonAdapter.notifyDataSetChanged();
    }

    private  FilterLinstener filterLinstener;
    public void setFilterLinstener(FilterLinstener filterLinstener){
        this.filterLinstener = filterLinstener;
    }
    public interface  FilterLinstener{
        void  result(String s);
        void cancle();
    }

    /**
     * 对从服务器获取的申请类型和状态进行升序排序
     */
    private class TypeOrStateSort implements Comparator<BaseData> {

        @Override
        public int compare(BaseData lhs, BaseData rhs) {
            return lhs.getSort() - rhs.getSort();
        }
    }

}
