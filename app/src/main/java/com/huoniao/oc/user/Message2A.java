package com.huoniao.oc.user;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.MessageBean;
import com.huoniao.oc.bean.NotificationBean;
import com.huoniao.oc.common.ExpandableTextView;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.myOnTabSelectedListener;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.DensityUtil;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/10.
 */

public class Message2A extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tab_layout)
    TabLayout tabLayout;
    @InjectView(R.id.messageListView)
    PullToRefreshListView messageListView;
    @InjectView(R.id.ll_messageState)
    LinearLayout llMessageState;
    @InjectView(R.id.iv_three)
    ImageView ivThree;  //小三角
    @InjectView(R.id.tv_cancle)
    TextView tvCancle; //右上角取消
    @InjectView(R.id.rb_select)
    RadioButton rbSelect; //全选
    @InjectView(R.id.tv_select_delete)
    TextView tvSelectDelete; //全选删除
    @InjectView(R.id.ll_delete)
    LinearLayout llDelete; //全选容器
    private String paidUpState; //从外界面进来获取的状态  如果没有值就默认查看的是我的消息,否则就是查看系统通知
    private boolean switchConsolidatedFlag = true; // 我的消息、系统通知界面切换
    private boolean refreshClose = true;//标记是否还有数据可加载
    private ListView mListView;
    private List<NotificationBean> mDatas = new ArrayList<>();
    private CommonAdapter<NotificationBean> adapter;
    private String nextPageStr = "";

    private String flagTypeSwitch = "0"; //0表示我的消息 1表示系统通知 选项
    private float popHeight;
    private float popWidth;
    private float itemWidth;
    private MyPopWindow myPopWindowItem;
    private MyPopWindow myThreeWindow;
    private int[] location;
    private int xs;
    private int ys;
    private PopClick popClick;
    private MyPopWindow myDeletePop;
    private MyPopWindow myPopWindowAllDelete;

//    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmessage);
        ButterKnife.inject(this);
        initWidget();
    }



    private void initWidget() {
//        token = SPUtils2.getString(Message2A.this, "token");
        paidUpState = getIntent().getStringExtra("paidUpState");
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("消息中心");
        if(premissionNo(Premission.FB_INFO_VIEW)) {
            tabLayout.addTab(tabLayout.newTab().setText("我的消息"));
        }
        if (paidUpState == null) {
            flagTypeSwitch = "0";
            if(premissionNo(Premission.FB_NOTICE_VIEW)) {
                tabLayout.addTab(tabLayout.newTab().setText("系统通知"));
            }
             if(premissionNo(Premission.FB_INFO_VIEW)) {
                   requestMessage(1); //初始化进入我的消息模块
                ivThree.setVisibility(View.VISIBLE);
            }
        } else {
            flagTypeSwitch = "1";
            if(premissionNo(Premission.FB_NOTICE_VIEW)) {
                tabLayout.addTab(tabLayout.newTab().setText("系统通知"), true);   //外面如果想要进入系统通知界面会走这里
            }
            requestNotificationData(true, "1");
            ivThree.setVisibility(View.GONE);
            tvCancle.setVisibility(View.GONE);
            llDelete.setVisibility(View.GONE);
        }

        setIndicator(tabLayout, 30, 30);
        messageListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        messageListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        messageListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        messageListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = messageListView.getRefreshableView();
        tabLayout.setOnTabSelectedListener(new myOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中了tab的逻辑

                switch (tab.getPosition()) {
                    case 0:  //我的消息
                        ivThree.setVisibility(View.VISIBLE);
                        flagTypeSwitch = "0";
                        switchConsolidatedFlag = true;
                        batchTag = false;  //切换过来把单选按钮隐藏
                        requestMessage(1); //初始化进入我的消息模块
                        break;
                    case 1:  //系统通知
                        ivThree.setVisibility(View.GONE);
                        tvCancle.setVisibility(View.GONE);
                        llDelete.setVisibility(View.GONE);
                        flagTypeSwitch = "1";
                        if (mDatas != null) {
                            mDatas.clear();
                        }
                        switchConsolidatedFlag = false;
                        requestNotificationData(true, "1");
                        refreshClose = true;
                        break;
                }
            }

        });

        messageListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (flagTypeSwitch.equals("0")) { //我的消息
                    if (pageNext == -1) {
                        ToastUtils.showLongToast(Message2A.this, "没有更多数据了！");
                        refreshCompleteClose();
                        return;
                    }
                    requestMessage(pageNext); //请求下一页

                } else if (flagTypeSwitch.equals("1")) {   //系统通知
                    if (refreshClose) {  //如果有数据 继续刷新
                        try {
//						pageNumber++;
                            requestNotificationData(true, nextPageStr);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
//                    Toast.makeText(FinancialListA.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        ToastUtils.showToast(Message2A.this, "没有更多数据了");
                        ThreadCommonUtils.runonuiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageListView.onRefreshComplete();
                            }
                        });

                    }
                }
            }
        });


    }

    /**
     * 条目之上弹出框
     *
     * @param parent
     * @param view
     */
    private void showItemPopCopy(View parent, View view, final String copyContent) {
    //    int i = DensityUtil.dip2px(Message2A.this, 30);
        if (myPopWindowItem != null) { //检查屏幕上是否已经有了悬浮窗体，有的话就立即关闭
            myPopWindowItem.dissmiss();
        }
        myPopWindowItem = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                LinearLayout ll_pop_delete2 = (LinearLayout) view.findViewById(R.id.ll_pop_delete2);
               TextView tv_cp = (TextView) view.findViewById(R.id.tv_cp);
                TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete); //没有已读选项的删除
                tv_delete.setVisibility(View.GONE);
                view.measure(0, 0);
                popHeight = view.getMeasuredHeight();
                popWidth = view.getMeasuredWidth();
                tv_cp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setContentClipboard(copyContent);
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.item_pop_message;
            }

        }.popWindowTouch2(Message2A.this).showAtLocation(parent, Gravity.LEFT + Gravity.TOP, (int) (itemWidth / 2 - popWidth / 2), (int) (location[1] - popHeight ));

    }

    /**
     * 条目之上弹出框
     *  @param parent
     * @param view
     * @param position
     * @param id
     * @param text
     */
    private void showItemPop(View parent, View view, final int position, long id, final String text) {
        int i = DensityUtil.dip2px(Message2A.this, 16);
        if (myPopWindowItem != null) { //检查屏幕上是否已经有了悬浮窗体，有的话就立即关闭
            myPopWindowItem.dissmiss();
        }
        myPopWindowItem = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {


               final TextView tv_asRead = (TextView) view.findViewById(R.id.tv_asRead);// 标为已读
                TextView tv_sigle_delete = (TextView) view.findViewById(R.id.tv_sigle_delete);//删除单条
               LinearLayout ll_pop_delete = (LinearLayout) view.findViewById(R.id.ll_pop_delete);
                LinearLayout ll_pop_delete2 = (LinearLayout) view.findViewById(R.id.ll_pop_delete2);
               TextView tv_copy_t = (TextView) view.findViewById(R.id.tv_copy_t); //复制内容
                TextView tv_cp = (TextView) view.findViewById(R.id.tv_cp);
                 TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete); //没有已读选项的删除
                final MessageBean.DataBean dataBean = messageDataList.get(position);
                if(dataBean.getInfoStatus().equals("1")){  //1为已查看
                     ll_pop_delete.setVisibility(View.GONE);
                    ll_pop_delete2.setVisibility(View.VISIBLE);
                }else{    //没有查看
                      ll_pop_delete.setVisibility(View.VISIBLE);
                    ll_pop_delete2.setVisibility(View.GONE);
                }

                view.measure(0, 0);
                popHeight = view.getMeasuredHeight();
                popWidth = view.getMeasuredWidth();

                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteMessage(dataBean.getId()); //删除单条消息
                        myPopWindowItem.dissmiss();
                    }
                });

                tv_asRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        readerMessage(dataBean.getId()); //把消息置为已读
                        myPopWindowItem.dissmiss();

                    }
                });
                tv_sigle_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteMessage(dataBean.getId()); //删除单条消息
                        myPopWindowItem.dissmiss();
                    }
                });

                tv_copy_t.setOnClickListener(new View.OnClickListener() {  //复制内容到剪切板
                    @Override
                    public void onClick(View view) {
                        setContentClipboard(text);
                    }
                });
                tv_cp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setContentClipboard(text);
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.item_pop_message;
            }

        }.popWindowTouch2(Message2A.this).showAtLocation(parent, Gravity.LEFT + Gravity.TOP, (int) (itemWidth / 2 - popWidth / 2), (int) (location[1] - popHeight + i));

    }

    private void setContentClipboard(String text) {
        ClipboardManager cmb = (ClipboardManager) MyApplication.mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        myPopWindowItem.dissmiss();
    }


    private void refreshCompleteClose() {
        ThreadCommonUtils.runonuiThread(new Runnable() {
            @Override
            public void run() {
                messageListView.onRefreshComplete();
            }
        });
    }

    /**
     * 请求我的消息
     */
    private void requestMessage(int next) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNo", next);
//            jsonObject.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL + "/fb/infoList";
        requestNet(url, jsonObject, "infoList", next + "", true, true);
    }

    private boolean rbSelectTag; //选中状态
    @OnClick({R.id.iv_back, R.id.iv_three, R.id.tv_cancle,R.id.rb_select,R.id.tv_select_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(101); //返回键更新主页面数据
                finish();

                break;
            case R.id.iv_three:
                showThree(view);
                break;
            case R.id.tv_cancle:
                //取消操作 批量删除操作
                batchTag = false;
                if (messageDataList.size() > 0) {
                    for (int i = 0; i < messageDataList.size(); i++) {
                        MessageBean.DataBean dataBean = messageDataList.get(i);
                        dataBean.rbBoolean = false;
                    }
                    if (messageAdapter != null) {
                        messageAdapter.notifyDataSetChanged();
                    }
                }
                ivThree.setVisibility(View.VISIBLE);
                tvCancle.setVisibility(View.GONE);
                llDelete.setVisibility(View.GONE);
                break;
            case R.id.tv_select_delete: //全选删除
                   if(myDeletePop !=null && myDeletePop.isShow()){
                       myDeletePop.dissmiss();

                   }
                myDeletePop = new MyPopAbstract(){
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                      TextView tv_pop_delete = (TextView) view.findViewById(R.id.tv_pop_delete) ;//弹出框删除
                      TextView  tv_pop_delete_cancle = (TextView) view.findViewById(R.id.tv_pop_delete_cancle);//弹出框删除取消

                        tv_pop_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < messageDataList.size(); i++) {
                                    MessageBean.DataBean dataBean = messageDataList.get(i);
                                    if(dataBean.rbState){
                                        stringBuilder.append(dataBean.getId()+",");
                                    }

                                }
                                if(stringBuilder.toString().isEmpty()){
                                    ToastUtils.showToast(Message2A.this,"未选中任何消息，删除失败！");
                                }else {
                                    deleteMessage(stringBuilder.toString());
                                }
                                    myDeletePop.dissmiss();
                            }
                        });
                        tv_pop_delete_cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDeletePop.dissmiss();
                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.pop_delete_message;
                    }
                }.popupWindowBuilder(Message2A.this).create().showAtLocation(view, Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.rb_select: //全选点击事件
                rbSelectTag = !rbSelectTag;
                if(rbSelectTag){
                    rbSelect.setChecked(true);
                    setSelectState(true);
                }else{
                    rbSelect.setChecked(false);
                    setSelectState(false);
                }
                break;
        }
    }

    /**
     * 全选
     * @param b
     */
   public void  setSelectState(boolean b){
       if (messageDataList.size() > 0) {
           for (int i = 0; i < messageDataList.size(); i++) {
               MessageBean.DataBean dataBean = messageDataList.get(i);
               if(b) {
                   dataBean.rbState = true;
               }else{
                   dataBean.rbState = false;
               }
               }
           if (messageAdapter != null) {
               messageAdapter.notifyDataSetChanged();
           }
       }
   }

    private void showThree(View view) {
        myThreeWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                view.measure(0, 0);
                ivThree.getLocationOnScreen(arr);
                xs = arr[0] + ivThree.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + ivThree.getHeight();
                TextView tv_reader = (TextView) view.findViewById(R.id.tv_reader);//一键已读
                TextView tv_message_clear = (TextView) view.findViewById(R.id.tv_message_clear);//消息清空
                TextView tv_batch_delete = (TextView) view.findViewById(R.id.tv_batch_delete);//批量删除
                if (popClick == null) {
                    popClick = new PopClick();
                }
                tv_reader.setOnClickListener(popClick);
                tv_message_clear.setOnClickListener(popClick);
                tv_batch_delete.setOnClickListener(popClick);
            }

            @Override
            protected int layout() {
                return R.layout.pop_message_setting;
            }
        }.popupWindowBuilder(Message2A.this).create().showAtLocation(ivThree, Gravity.NO_GRAVITY, xs, ys);
    }


    private boolean  batchTag ; //批量删除tag
    public class PopClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_reader: //一键已读
                    readerMessage("");
                    break;
                case R.id.tv_message_clear: //消息清空
                    showPopAllDelete();
                    break;
                case R.id.tv_batch_delete: //批量删除
                    batchTag = true;
                    if (messageDataList.size() > 0) {
                        for (int i = 0; i < messageDataList.size(); i++) {
                            MessageBean.DataBean dataBean = messageDataList.get(i);
                            dataBean.rbBoolean = true;
                        }
                        if (messageAdapter != null) {
                            messageAdapter.notifyDataSetChanged();
                        }
                    }
                    ivThree.setVisibility(View.GONE);
                    tvCancle.setVisibility(View.VISIBLE);
                    llDelete.setVisibility(View.VISIBLE);
                    break;
            }
            if (myThreeWindow != null && myThreeWindow.isShow()) {
                myThreeWindow.dissmiss();

            }
        }
    }

    /**
     * 清除全部消息     *
     */
    private void showPopAllDelete() {
        if(myPopWindowAllDelete !=null && myPopWindowAllDelete.isShow()){
            myPopWindowAllDelete.dissmiss();
        }

        myPopWindowAllDelete = new MyPopAbstract(){
              @Override
              protected void setMapSettingViewWidget(View view) {
                  TextView cancle = (TextView) view.findViewById(R.id.tv_call); //取消
                 TextView confirm = (TextView) view.findViewById(R.id.tv_call_cancle);//确定
                  confirm.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          deleteMessage(""); //清除全部消息
                          myPopWindowAllDelete.dissmiss();
                      }
                  });
                  cancle.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          myPopWindowAllDelete.dissmiss();
                      }
                  });
              }

              @Override
              protected int layout() {
                  return R.layout.pop_message_all_delete;
              }
          }.popWindowTouch(Message2A.this).showAtLocation(ivBack, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

    }

    /**
     * 读消息
     */
    private void readerMessage(String id) {
        try {
            String url = Define.URL + "fb/readInfo";
            JSONObject jsonObject = new JSONObject();
            if("".equals(id)) {
                jsonObject.put("id", "");//全部已读传空
            }else{
                jsonObject.put("id",id); //单条已读
            }
            requestNet(url, jsonObject, "readInfo", id, true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param id   删除多个  id以逗号分隔    id为null 删除全部
     */
    private void deleteMessage(String id){
        try {
            String url = Define.URL +"fb/infoDelete";
            JSONObject jsonObject = new JSONObject();
            if(!"".equals(id)) {  //id等于空的时候 不需要传递参数  删除全部
                jsonObject.put("ids", id);
            }
            requestNet(url,jsonObject,"infoDelete",id,true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
  /**
     * tablayout下滑线设置
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
        tabStrip = null;
        llTab = null;
    }

    /**
     * 请求通知列表
     *
     * @param dataNum 传true代表请求所有列表数据，传false代表请求三条数据
     * @param pageNo  页数
     */
    private void requestNotificationData(boolean dataNum, String pageNo) {
        String url = Define.URL + "fb/getShowNoticeList";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("showList", dataNum);
            jsonObject.put("pageNo", pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "getNoticeList", pageNo, true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getNoticeList":
                setNoticeAdapterData(json, pageNumber);
                break;
            case "infoList":
                try {
                    pageNext = json.getInt("next");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refreshCompleteClose();
                setMessageAdapter(json, pageNumber);
                break;
            case "readInfo": //读消息
                postReadingProcessing(pageNumber); //读后处理显示
                break;
            case "infoDelete":
                deleteUpdateMessage(pageNumber); //这里pageNumber传过来的是id
                break;
        }
    }


    /**
     * 删除后更新消息
     * @param id    id可能为多个
     */
    private void deleteUpdateMessage(String id) {
        if("".equals(id)){
            if(messageDataList !=null && messageDataList.size()>0) {
                for (int i = 0; i < messageDataList.size(); i++) {
                    MessageBean.DataBean dataBean = messageDataList.get(i);
                    messageDataList.remove(dataBean);
                    i--;
                }
            }
        }else {
            if (id != null && id.length() > 0) {
                String[] split = id.split(",");
                for (int i = 0; i < split.length; i++) {
                    if (messageDataList != null && messageDataList.size() > 0) {
                        for (int j = 0; j < messageDataList.size(); j++) {
                            MessageBean.DataBean dataBean = messageDataList.get(j);
                            if (split[i].equals(dataBean.getId())) {
                                messageDataList.remove(dataBean);
                                j--;
                            }
                        }
                    }
                }
            }
        }

        if (messageAdapter != null) {
                    messageAdapter.notifyDataSetChanged();
         }


        noMessageUi();
    }

    /**
     * 没有任何消息 显示没有消息ui
     */
    private void noMessageUi() {
        if(messageDataList.size()<=0){
            llMessageState.setVisibility(ViewGroup.VISIBLE);

        }else{
            llMessageState.setVisibility(ViewGroup.GONE);

        }
    }

    /**
     * 读后处理显示
     * id 就是单个消息已读   没有全部已读
     */
    private void postReadingProcessing(String id) {
        if (messageDataList.size() > 0) {
            for (int i = 0; i < messageDataList.size(); i++) {
                MessageBean.DataBean dataBean = messageDataList.get(i);
                if(id !=null && id.equals(dataBean.getId())){
                    dataBean.setInfoStatus("1");
                } else if("".equals(id)){
                    dataBean.setInfoStatus("1"); //已查看
                }

            }
            if (messageAdapter != null) {
                messageAdapter.notifyDataSetChanged();
            }
        }
    }

    int pageNext;   //我的消息获取的下一页
    List<MessageBean.DataBean> messageDataList = new ArrayList<>(); //我的消息数据集合
    CommonAdapter<MessageBean.DataBean> messageAdapter; //我的消息适配器

    /**
     * 我的消息适配器
     *
     * @param json
     * @param pageNumber
     */
    private void setMessageAdapter(JSONObject json, String pageNumber) {
        if ("1".equals(pageNumber)) {
            messageDataList.clear();
            if (messageAdapter != null) {
                mListView.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();
            }

        }
        Gson gson = new Gson();
        MessageBean messageBean = gson.fromJson(json.toString(), MessageBean.class);
        List<MessageBean.DataBean> data = messageBean.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                MessageBean.DataBean dataBean = data.get(i);
                if(rbSelectTag) {  //是否全选 解决上拉加载更多全选后面的数据没有选中
                    dataBean.rbState = true;
                }else{
                    dataBean.rbState =false;
                }
            }
            messageDataList.addAll(data);
        }

        noMessageUi();

        if (messageAdapter == null) {
            messageAdapter = new CommonAdapter<MessageBean.DataBean>(Message2A.this, messageDataList, R.layout.item_notification_list2) {
                @Override
                public void convert(ViewHolder holder, MessageBean.DataBean message) {

                }

                @Override
                public void convert(ViewHolder holder, final MessageBean.DataBean message, final int position) {
                    holder.setText(R.id.tv_date, message.getInfoDate());
                    RadioButton rb_batch = holder.getView(R.id.rb_batch); //单选按钮
                    TextView tv_circle_red = holder.getView(R.id.tv_circle_red); //小红点
                    tv_circle_red.setTag(message.getId());
                    rb_batch.setTag(message.getId());
                     message.rbBoolean =batchTag; //是否批量删除操作

                    if (tv_circle_red.getTag().equals(message.getId())) {
                        if (message.getInfoStatus().equals("0")) {  //0表示未查看  1 表示查看了
                            tv_circle_red.setVisibility(View.VISIBLE);
                        } else {
                            tv_circle_red.setVisibility(View.GONE);
                        }
                    }
                    if (rb_batch.getTag().equals(message.getId())) {
                        if (message.rbBoolean) {
                            rb_batch.setVisibility(View.VISIBLE);
                        } else {
                            rb_batch.setVisibility(View.GONE);
                        }
                        if (message.rbState) {
                            rb_batch.setChecked(true);
                        } else {
                            rb_batch.setChecked(false);
                        }
                    }

                    rb_batch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            message.rbState = !message.rbState;
                            notifyDataSetChanged();
                        }
                    });

                    LinearLayout ll_item = holder.getView(R.id.ll_item);
                    final ExpandableTextView tv_content = holder.getView(R.id.tv_content);
                    tv_content.setText(message.getContent(), position);
                    ll_item.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            itemWidth = v.getMeasuredWidth();
                            location = new int[2];
                            v.getLocationInWindow(location); //显示在点击条目的上面
                            showItemPop(v, v, position, 1,tv_content.getText().toString());

                            return false;
                        }
                    });

                }
            };

            mListView.setAdapter(messageAdapter);
        }
        messageAdapter.notifyDataSetChanged();


    }


    /**
     * 设置通知数据到适配器
     *
     * @param jsonObject  服务器获取的json数据
     * @param pagerNumber 页数
     */
    private void setNoticeAdapterData(JSONObject jsonObject, String pagerNumber) {
        try {
            if("1".equals(pagerNumber)){
                if(mDatas !=null && adapter!=null){
                    mDatas.clear();
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            int size = jsonObject.getInt("size");
            if (size == 0) {

            }
            int next = jsonObject.getInt("next");
            setRefreshPager(next);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            final List<NotificationBean> notificationData = new ArrayList<NotificationBean>();
            for (int i = 0; i < jsonArray.length(); i++) {
                NotificationBean notificationBean = new NotificationBean();

                JSONObject notifiObj = (JSONObject) jsonArray.get(i);
                String id = notifiObj.optString("id");// 通知id
                String content = notifiObj.optString("content");// 通知内容
                String title = notifiObj.optString("title");// 通知标题
                String type = notifiObj.optString("type");//通知类型
                String createDate = notifiObj.optString("createDate");//通知类型

                notificationBean.setNotificationId(id);
                notificationBean.setNotificationTitle(title);
                notificationBean.setNotificationContent(content);
                notificationBean.setNotificationType(type);
                notificationBean.setCreateDate(createDate);
                notificationData.add(notificationBean);
            }

            if (next == 1) {
                mDatas.clear();
            } else if (next == -1) {
                refreshClose = false;
            }
            messageListView.onRefreshComplete();
            mDatas.addAll(notificationData);

            if(mDatas.size()<=0){
                llMessageState.setVisibility(ViewGroup.VISIBLE);

            }else{
                llMessageState.setVisibility(ViewGroup.GONE);

            }
            if (adapter == null) {
                adapter = new CommonAdapter<NotificationBean>(Message2A.this, mDatas, R.layout.item_notification_list) {
                    @Override
                    public void convert(ViewHolder holder, NotificationBean notificationBean) {

                    }

                    @Override
                    public void convert(ViewHolder holder, NotificationBean notificationBean, final int position) {
                      /*  holder.setText(R.id.tv_date, notificationBean.getCreateDate())
                                .setText(R.id.tv_title, notificationBean.getNotificationTitle());*/
                        TextView tv_date = holder.getView(R.id.tv_date);
                        TextView tv_title  = holder.getView(R.id.tv_title);
                        final View vs = holder.getView(R.id.v);
                        tv_date.setText(notificationBean.getCreateDate());
                        tv_title.setText(notificationBean.getNotificationTitle());
                        final ExpandableTextView tv_content = holder.getView(R.id.tv_content);
                        tv_content.setText(notificationBean.getNotificationContent(), position);
                        final String copyContent = notificationBean.getNotificationTitle()+"\n"+notificationBean.getNotificationContent();
                         LinearLayout ll_text=  holder.getView(R.id.ll_text);
                         ll_text.setOnLongClickListener(new View.OnLongClickListener() {
                             @Override
                             public boolean onLongClick(View v) {
                                 itemWidth = vs.getMeasuredWidth();
                                 location = new int[2];
                                 v.getLocationInWindow(location); //显示在点击条目的上面
                                 showItemPopCopy(v, v, copyContent);


                                 return false;
                             }
                         });
                    }
                };

                mListView.setAdapter(adapter);
            }
            adapter.refreshData(mDatas);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上拉加载下一页
     *
     * @param nextPage 下一页
     */
    private void setRefreshPager(final int nextPage) {
        nextPageStr = String.valueOf(nextPage);
       /* messageListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(flagTypeSwitch.equals("0")){ //我的消息

                }else {   //系统通知
                    if (refreshClose) {  //如果有数据 继续刷新
                        try {
//						pageNumber++;
                            requestNotificationData(true, nextPageStr);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
//                    Toast.makeText(FinancialListA.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        ToastUtils.showToast(Message2A.this, "没有更多数据了");
                        ThreadCommonUtils.runonuiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageListView.onRefreshComplete();
                            }
                        });

                    }
                }
            }
        });*/
    }

    //抽取权限
    public  boolean premissionNo(String premissionFlag){
        for (String premissions: premissionsList ) {
            if(premissionFlag.equals(premissions)){
                //有这个权限
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean onkeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(101);  //返回更新消息数目
        }
        return super.onkeyDown(keyCode, event);
    }



}
