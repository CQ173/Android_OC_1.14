package com.huoniao.oc.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MessageAdatpter;
import com.huoniao.oc.bean.MessageBean;
import com.huoniao.oc.common.sideslip_listview.PullToRefreshSwipeMenuListView;
import com.huoniao.oc.common.sideslip_listview.pulltorefresh.interfaces.IXListViewListener;
import com.huoniao.oc.common.sideslip_listview.swipemenu.bean.SwipeMenu;
import com.huoniao.oc.common.sideslip_listview.swipemenu.bean.SwipeMenuItem;
import com.huoniao.oc.common.sideslip_listview.swipemenu.interfaces.OnMenuItemClickListener;
import com.huoniao.oc.common.sideslip_listview.swipemenu.interfaces.SwipeMenuCreator;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_save;

public class MessageA extends BaseActivity implements IXListViewListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_add_card)
    TextView tvAddCard;
    @InjectView(tv_save)
    TextView tvSave;
    @InjectView(R.id.sideslip_listview)
    PullToRefreshSwipeMenuListView sideslipListview;
    @InjectView(R.id.activity_message)
    LinearLayout activityMessage;
    @InjectView(R.id.tv_message_state)
    TextView tvMessageState;
    private VolleyNetCommon volleyNetCommon;
    private MessageAdatpter messageAdatpter;
    private String time;
    private List<MessageBean.DataBean> arra = new ArrayList<MessageBean.DataBean>();
    private int nextPage;
    private int lastSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.inject(this);
        initWidget();
        initData();
    }

    private void initData() {
        volleyNetCommon = new VolleyNetCommon();
        getMessage(1);
    }

    private void initWidget() {
        //获取当前系统时间
        if(DateUtils.nowTime != null){
            time = DateUtils.nowTime;
        }else {
            time = DateUtils.getTime();
        }

        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText("全部已读");
        tvTitle.setText("我的消息");
        before = true;
        sideslipListview.setPullRefreshEnable(false);
        sideslipListview.setPullLoadEnable(true);
        sideslipListview.setXListViewListener(this);
    }

    private void getMessage(int next) {
        if (next == -1) {
            Toast.makeText(this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
            onLoad();
            return;
        }
        cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNo", next);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = Define.URL + "/fb/infoList";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {

            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                try {
                    nextPage = json.getInt("next");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                MessageBean messageBean = gson.fromJson(json.toString(), MessageBean.class);

                List<MessageBean.DataBean> data = messageBean.getData();
             if(data.size()<=0){
                 Toast.makeText(MessageA.this, "暂无消息！", Toast.LENGTH_SHORT).show();
                 sideslipListview.setVisibility(ViewGroup.GONE);
                 tvMessageState.setVisibility(ViewGroup.VISIBLE);
                 return;
             }

                sortMethod(data);

                onLoad();  // 关闭加载
            }

            @Override
            protected void PdDismiss() {
                cpd.dismiss();
            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
        }, "infoList", true);
        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    private static boolean before = true;  //控制不会再创建之后条目了
    private boolean startTag = true;//控制不会再创建今天条目了
    private void addMultipleDate(List<MessageBean.DataBean> sortList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < sortList.size(); i++) {
            try {
                Date date = dateFormat.parse(sortList.get(i).getInfoDate());
                MessageBean.DataBean s = sortList.get(i);
                String previousDate = dateFormat.format(date);
                if (time.equals(previousDate)) {
                    if (i == 0 && startTag) {
                        startTag = false;
                        MessageBean.DataBean s1 = new MessageBean.DataBean();
                        s1.setInfoDate(s.getInfoDate());
                        s1.setInfoStatus(s.getInfoStatus());
                        s1.setId(s.getId());
                        s1.setContent(s.getContent());
                        s1.tag = "1";  //代表分组标题
                        arra.add(s1);
                        MessageBean.DataBean s2 = new MessageBean.DataBean();
                        s2.setInfoDate(s.getInfoDate());
                        s2.setInfoStatus(s.getInfoStatus());
                        s2.setId(s.getId());
                        s2.setContent(s.getContent());
                        s2.tag = "0";
                        arra.add(s2);  //代表分组组员
                    } else {
                        MessageBean.DataBean s1 = new MessageBean.DataBean();
                        s1.setInfoDate(s.getInfoDate());
                        s1.setInfoStatus(s.getInfoStatus());
                        s1.setId(s.getId());
                        s1.setContent(s.getContent());
                        s1.tag = "0";
                        arra.add(s1);
                    }
                } else {
                    if (before) {
                        before = false;
                        MessageBean.DataBean s1 = new MessageBean.DataBean();
                        s1.setInfoDate(s.getInfoDate());
                        s1.setInfoStatus(s.getInfoStatus());
                        s1.setId(s.getId());
                        s1.setContent(s.getContent());
                        s1.tag = "2";
                        arra.add(s1);
                        MessageBean.DataBean s2 = new MessageBean.DataBean();
                        s2.setInfoDate(s.getInfoDate());
                        s2.setInfoStatus(s.getInfoStatus());
                        s2.setId(s.getId());
                        s2.setContent(s.getContent());
                        s2.tag = "0";
                        arra.add(s2);  //代表分组组员
                    } else {
                        MessageBean.DataBean s2 = new MessageBean.DataBean();
                        s2.setInfoDate(s.getInfoDate());
                        s2.setInfoStatus(s.getInfoStatus());
                        s2.setId(s.getId());
                        s2.setContent(s.getContent());
                        s2.tag = "0";
                        arra.add(s2);  //代表分组组员
                    }

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        for (MessageBean.DataBean s : arra) {
            System.out.println("System.out" + s.getInfoDate() + "---------" + s.tag);
        }

        if (messageAdatpter == null) {
            messageAdatpter = new MessageAdatpter();
            messageAdatpter.setData(arra);


                sideslipListview.setAdapter(messageAdatpter);

        }else{
            messageAdatpter.setData(arra);
        }


        messageAdatpter.notifyDataSetChanged();
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("标记已读");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitleSize(18);
                // set a icon
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                //deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        sideslipListview.setMenuCreator(creator);

        sideslipListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageBean.DataBean dataBean = arra.get(i - 1);
                if(dataBean.tag.equals("1") || dataBean.tag.equals("2")){
                    return;
                }

                if (dataBean.getInfoStatus().equals("1")) {
                    Intent intent = new Intent(MessageA.this, MainMessageDetailsA.class);
                    intent.putExtra("content", dataBean.getContent());
                    intent.putExtra("date", dataBean.getInfoDate());

                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                } else {
                    readMethod(dataBean, i, true,false); //点击消息为已读状态  true表示是点击条目进来的 false表示是标记已读进来的
                }
            }
        });

        // step 2. listener item click event
        sideslipListview.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                MessageBean.DataBean dataBean = arra.get(position);
                switch (index) {
                    case 0:
                        // open
                        readMethod(dataBean, position, false,false);

                        break;
                    case 1:
                        // delete
                        // delete(item);
                        /*mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();*/
                        deleteMessage(dataBean, position);
                        break;
                }
            }
        });

        sideslipListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = sideslipListview.getChildAt(sideslipListview.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == sideslipListview.getHeight()) {
                        lastSize = firstVisibleItem + visibleItemCount - 2;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //do nothing
            }

        });

    }

    //删除消息
    private void deleteMessage(MessageBean.DataBean dataBean, final int position) {
        cpd.show();
        if (volleyNetCommon != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ids", dataBean.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = Define.URL + "fb/infoDelete";
            JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
                @Override
                public void volleyResponse(Object o) {

                }

                @Override
                public void volleyError(VolleyError volleyError) {
                    Toast.makeText(MessageA.this,R.string.netError, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected void netVolleyResponese(JSONObject json) {
                    arra.remove(position);
                    messageAdatpter.notifyDataSetChanged();
                }

                @Override
                protected void PdDismiss() {
                    cpd.dismiss();
                }
                @Override
                protected void errorMessages(String message) {
                    super.errorMessages(message);
                    Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                }
            }, "infoDelete", true);
            volleyNetCommon.addQueue(jsonObjectRequest);
        }
    }

    //标记消息已读状态
    private void readMethod(final MessageBean.DataBean dataBean, final int i, final boolean b, final boolean allRead) {
        cpd.show();
        if (volleyNetCommon != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                if(allRead){
                 jsonObject.put("id","");
                }else {
                    jsonObject.put("id", dataBean.getId());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = Define.URL + "fb/readInfo";
            JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
                @Override
                public void volleyResponse(Object o) {

                }

                @Override
                public void volleyError(VolleyError volleyError) {
                    Toast.makeText(MessageA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected void netVolleyResponese(JSONObject json) {

                    if (b) {   // true表示是点击条目进来的 false表示是标记已读进来的
                        arra.get(i - 1).setInfoStatus("1");
                        messageAdatpter.notifyDataSetChanged();
                        Intent intent = new Intent(MessageA.this, MainMessageDetailsA.class);
                        intent.putExtra("content", dataBean.getContent());
                        intent.putExtra("date", dataBean.getInfoDate());

                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    } else {
                        if(allRead){  //如果是true表示全部置为消息已读状态
                            if(arra != null && arra.size()>0) {
                                for (int i = 0; i < arra.size(); i++) {
                                    arra.get(i).setInfoStatus("1");
                                }
                                messageAdatpter.notifyDataSetChanged();
                            }
                        }else {
                            arra.get(i).setInfoStatus("1");
                            messageAdatpter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                protected void PdDismiss() {
                    cpd.dismiss();
                }
                @Override
                protected void errorMessages(String message) {
                    super.errorMessages(message);
                    Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                }
            }, "readInfo", true);

            volleyNetCommon.addQueue(jsonObjectRequest);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(101); //返回键更新主页面数据
                finish();
                break;
            case R.id.tv_save:
                //消息全部置为已读

                 readMethod(null,0,false,true);  //第一和第二个参数在这里没有作用   第三表示 true表示是点击条目进来的 false表示是标记已读进来的 第四 表示把消息全部置为已读状态  必须是 第三false和第三true组合

                break;
        }
    }


    Date ds1;
    Date ds2;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void sortMethod(List<MessageBean.DataBean> sortList) {


        Comparator<MessageBean.DataBean> comparator = new Comparator<MessageBean.DataBean>() {
            public int compare(MessageBean.DataBean s1, MessageBean.DataBean s2) {

                try {
                    ds1 = dateFormat.parse(s1.getInfoDate());
                    ds2 = dateFormat.parse(s2.getInfoDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (ds1.before(ds2)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        //这里就会自动根据规则进行排序
        Collections.sort(sortList, comparator);
        addMultipleDate(sortList);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                MyApplication.mContext.getResources().getDisplayMetrics());
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

            getMessage(nextPage);//加载更多


    }

    private void onLoad() {
      /*  sideslipListview.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
        sideslipListview.stopRefresh();*/
        sideslipListview.stopLoadMore();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll("readInfo");
            volleyNetCommon.getRequestQueue().cancelAll("infoList");
            volleyNetCommon.getRequestQueue().cancelAll("infoDelete");
        }
    }

    @Override
    protected boolean onkeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(101);  //返回更新消息数目
        }
        return super.onkeyDown(keyCode, event);
    }


}
