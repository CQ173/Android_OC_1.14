package com.huoniao.oc.trainstation;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.CallBack.OnStartDragListener;
import com.huoniao.oc.CallBack.SimpleItemTouchHelperCallback;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.CustomSortingAdapter;
import com.huoniao.oc.bean.CustomSortingBean;
import com.huoniao.oc.util.Define;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2019/6/5.
 */

public class CustomSortingActivity extends BaseActivity implements OnStartDragListener{

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;

    private CustomSortingAdapter customSortingAdapter;
    private EventBus event;

    private ArrayList<CustomSortingBean.DataBean> list = new ArrayList<>();

    private ItemTouchHelper mItemTouchHelper;
    private List<CustomSortingBean.DataBean> dataBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sorting);
        ButterKnife.inject(this);
        iinitView();
    }

    private void iinitView(){
        event = EventBus.getDefault();
        getRemittancedata();
    }

    @OnClick({R.id.ll_back , R.id.tv_Preservation})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_Preservation://保存按钮
                StringBuilder sb= new StringBuilder();
                for (CustomSortingBean.DataBean dataBean : dataBeanList) {
                    if (sb.length() > 0){
                        sb.append(",");
                    }
                    sb.append(dataBean.getId());
                }
                trainPaymentSortSave(sb.toString());
                break;
        }
    }

    /**
     * 获取排序列表接口
     */
    private void trainPaymentSortSave(String ids) {
        String url = Define.URL + "fb/trainPaymentSortSave";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ids" , ids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "trainPaymentSortSave", "", true, false);
    }

    /**
     * 获取排序列表接口
     */
    private void getRemittancedata() {
        String url = Define.URL + "fb/trainPaymentSortList";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "trainPaymentSortList", "", true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "trainPaymentSortList":
                setcustomsorting(json);
                break;
            case "trainPaymentSortSave":

                break;
        }
    }

    private void setcustomsorting(JSONObject json){
        Gson gson = new Gson();
        CustomSortingBean customSortingBean = gson.fromJson(json.toString() , CustomSortingBean.class );
        dataBeanList = customSortingBean.getData();
        customSortingAdapter = new CustomSortingAdapter(dataBeanList,this, event);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(customSortingAdapter);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(customSortingAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerview);

        customSortingAdapter.setOnItemClickListener(new CustomSortingAdapter.ItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int positon) {
//                Log.i("onItemClick```", "" + positon);
            }

            @Override
            public void onItemLongClick(final RecyclerView.ViewHolder holder, final int positon) {
//                Log.i("onItemClick```", "Long" + positon);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        event.unregister(this);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
