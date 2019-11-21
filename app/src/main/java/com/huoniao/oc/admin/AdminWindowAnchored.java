package com.huoniao.oc.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.bean.AdminWindowAnchoredListBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.bean.UserRoleTypeBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SpinerPopWindow;
import com.huoniao.oc.util.ThreadCommonUtils;
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
 * 窗口挂靠
 */
public class AdminWindowAnchored extends BaseActivity implements AdapterView.OnItemClickListener, MySpinerAdapter.IOnItemSelectListener {
    @InjectView(R.id.iv_back)
    ImageView ivBack;  //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.tv_add_card)
    TextView tvAddCard;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.v_backgroud)
    View vBackgroud;  //选项卡背景
    @InjectView(R.id.v_backgroud2)
    View vBackgroud2;
    @InjectView(R.id.tb_layout)
    TabLayout tbLayout; //选项卡
    @InjectView(R.id.tv_attribution_area)
    TextView tvAttributionArea;
    @InjectView(R.id.ll_ownership_institution)
    LinearLayout llOwnershipInstitution;
    @InjectView(R.id.et_variousTypes)
    EditText etVariousTypes;
    @InjectView(R.id.tv_audit_status)
    TextView tvAuditStatus;
    @InjectView(R.id.ll_audit_status)
    LinearLayout llAuditStatus;
    @InjectView(R.id.tv_query)
    TextView tvQuery;
    @InjectView(R.id.tv_recordTotal)
    TextView tvRecordTotal;
    @InjectView(R.id.userPullToRefreshListView)
    PullToRefreshListView mPullRefreshListView;
    @InjectView(R.id.activity_admin_window_anchored)
    LinearLayout activityAdminWindowAnchored;
     ListView mListView; //包裹的listview
    private MapData mapData;
    private MyPopWindow myPopWindow;
    private float xs;
    private float ys;
    private String linOfficeId = "";  //临时归属区域id
    private List<UserRoleTypeBean.ListBean> list;  //用户角色集合
    List<String> auditStateList = new ArrayList<>(); //审核状态集合
    private String linAuditStateString = ""; //临时审核状态
    private   List<AdminWindowAnchoredListBean.DataBean> allWindowAnchoredList = new ArrayList<>();
    private String pageNext="1";  //请求返回 是否还有下一页 -1表示没有
    private String count; //条数
    private CommonAdapter<AdminWindowAnchoredListBean.DataBean> commonAdapter;
    private String officeId;   //机构id
    private String auditStateString;    //状态
    private String et_variousTypes;   //窗口号/归属账号


    //归属区域--------------------------------------------------------------------
    private static final int MANAGEAREA_LIST = 4;
    private static final int MANAGEAREA2_LIST = 5;
    private static final int MANAGEAREA3_LIST = 6;
    private List<String> oneLevelCodeList = new ArrayList<String>();
    private List<String> oneLevelNameList = new ArrayList<String>();
    private List<String> twoLevelCodeList = new ArrayList<String>();
    private List<String> twoLevelNameList = new ArrayList<String>();
    private List<String> threeLevelNameList = new ArrayList<String>();
    private List<String> threeLevelCodeList = new ArrayList<String>();
    private String oneLevelCode = "";
    private String twoLevelCode = "";
    private String threeLevelCode = "";
    private String oneLevel, twoLevel, threeLevel, manageArea;
    private RelativeLayout rl_choiceOneLevel, rl_choiceTwoLevel, rl_choiceThirdLevel;
    private TextView tv_oneLevel, tv_twoLevel, tv_thirdLevel;
    private Button bt_complete;
    private String jurisAreaCode = "";//归属区域code
    private SpinerPopWindow mSpinerPopWindow;
    private String CHOICE_TAG;//
    private String  pendingAuditWindow;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MANAGEAREA_LIST:
                    @SuppressWarnings("unchecked")
                    List<User> manageAreaList = (List<User>) msg.obj;

                    for (User user : manageAreaList) {
                        oneLevelCodeList.add(user.getOneLevelCode());
                        oneLevelNameList.add(user.getOneLevelName());
                    }

                    break;

                case MANAGEAREA2_LIST: {
                    @SuppressWarnings("unchecked")
                    List<User> twoAreaList = (List<User>) msg.obj;
                    twoLevelNameList.clear();
                    twoLevelCodeList.clear();
                    for (User user : twoAreaList) {
                        twoLevelNameList.add(user.getTwoLevelName());
                        twoLevelCodeList.add(user.getTwoLevelCode());
                    }

                    break;

                }

                case MANAGEAREA3_LIST: {

                    @SuppressWarnings("unchecked")
                    List<User> twoAreaList = (List<User>) msg.obj;
                    threeLevelNameList.clear();
                    threeLevelCodeList.clear();
                    for (User user : twoAreaList) {
                        threeLevelNameList.add(user.getThreeLevelName());
                        threeLevelCodeList.add(user.getThreeLevelCode());
                    }

                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_window_anchored);
        ButterKnife.inject(this);
         pendingAuditWindow = getIntent().getStringExtra("PendingAuditWindow"); //进来如果不为null 进入待审核窗口选项选中
        initWidget();
        initData();
    }


    private void initData() {
        if(pendingAuditWindow==null) {
            requestData("", "", "", "1");  //默认进来初始化
        }
        try {
            getAllManageArea();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestData(String jurisAreaId,String str, String auditState,String pageNo ){
        String url = Define.URL +"fb/agencyConnectList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("jurisAreaCode",jurisAreaId);//归属机构
            jsonObject.put("str",str);//窗口号/归属账号
            jsonObject.put("auditState",auditState); //审核状态
            jsonObject.put("pageNo",pageNo);//页数
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url,jsonObject,"agencyConnectList",pageNo,true, true);
    }

    //控件背景颜色切换
    private void switchBackgroud(View view) {
        vBackgroud.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        vBackgroud2.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        if (view != null) {
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    //控件操作
    private void initWidget() {

        tvTitle.setText("窗口挂靠");
        tbLayout.addTab(tbLayout.newTab().setText("全部"));
        switchBackgroud(vBackgroud);
        if(pendingAuditWindow !=null) { //有的界面进来直接进入待审核界面
            tbLayout.addTab(tbLayout.newTab().setText("待审核"),true);
            switchBackgroud(vBackgroud2);
            auditStateString = "1"; //查询待审核
            linAuditStateString="1";
            requestData(officeId,et_variousTypes,auditStateString,"1"); //查询第一页
            tvAuditStatus.setText("待审核");
        }else{
            tbLayout.addTab(tbLayout.newTab().setText("待审核"));
        }
        setIndicator(tbLayout, 30, 30);

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);


        tbLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中了tab的逻辑

                switch (tab.getPosition()) {
                    case 0:  //全部
                        switchBackgroud(vBackgroud);
                        auditStateString ="";//查询全部
                        linAuditStateString="";
                        requestData(officeId,et_variousTypes,auditStateString,"1"); //查询第一页
                        tvAuditStatus.setText("审核状态");
                        break;
                    case 1:  //待审核
                        switchBackgroud(vBackgroud2);
                        auditStateString = "1"; //查询待审核
                        linAuditStateString="1";
                        requestData(officeId,et_variousTypes,auditStateString,"1"); //查询第一页
                        tvAuditStatus.setText("待审核");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//未选中tab的逻辑

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {//再次选中tab的逻辑

            }

        });
        initLinsener();
    }
    private void initLinsener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (pageNext.equals("-1")) {
                    Toast.makeText(AdminWindowAnchored.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    });
                } else {
                    requestData(officeId,et_variousTypes,auditStateString,pageNext); //查询第一页    //上拉刷新
                }
            }
        });
    }


    /**
     * 关闭刷新框
     */
    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        mPullRefreshListView.onRefreshComplete();
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
    }

    @OnClick({R.id.iv_back, R.id.ll_ownership_institution, R.id.ll_audit_status, R.id.tv_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_ownership_institution:
                oneLevelCode = null;
                twoLevelCode=null; //把第二级的code设置为null
                threeLevelCode=null;//把第三级的code设置为null
                choiceManageAreaDialog();
                break;
            case R.id.ll_audit_status:
                showAuditStatus();
                break;
            case R.id.tv_query:
                query();
                break;
        }
    }

    /**
     * 开始查询
     */
    private void query() {
        officeId = linOfficeId;      //机构id
        auditStateString = linAuditStateString;  //状态
        et_variousTypes = etVariousTypes.getText().toString().trim();//窗口号归属账号

        requestData(officeId,et_variousTypes,auditStateString,"1"); //查询第一页

    }

    /**
     * 审核状态
     */
    private void showAuditStatus() {
        setDataListView();
        showPop(llAuditStatus, auditStateList, "auditState", 2);
    }

    /**
     * 添加审核状态集合
     */
    private void setDataListView() {
        auditStateList.clear();
        auditStateList.add("全部");
        auditStateList.add("审核通过");
        auditStateList.add("待审核");
        auditStateList.add("审核不通过");
        auditStateList.add("解除绑定");
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag,String pageNumber) {
        super.dataSuccess(json, tag,pageNumber);
        switch (tag){
            case "agencyConnectList":
                 fileAdapter(json,pageNumber);
                break;
        }
    }

     /*
        填充适配器
     */
    private void fileAdapter(JSONObject json, String pageNumber) {
        Gson gson = new Gson();
        AdminWindowAnchoredListBean adminWindowAnchoredListBean = gson.fromJson(json.toString(), AdminWindowAnchoredListBean.class);
        List<AdminWindowAnchoredListBean.DataBean> data = adminWindowAnchoredListBean.getData();

        try {
            if(pageNumber.equals("1")) {
                allWindowAnchoredList.clear();
                count = String.valueOf(json.getInt("count"));
                tvRecordTotal.setText(count); //总条数
            }
            pageNext = String.valueOf(json.getInt("next"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        allWindowAnchoredList.addAll(data);

        if(allWindowAnchoredList.size()>0 && commonAdapter==null) {
            commonAdapter = new CommonAdapter<AdminWindowAnchoredListBean.DataBean>(AdminWindowAnchored.this, allWindowAnchoredList, R.layout.admin_item_window_anchored_list) {
                @Override
                public void convert(ViewHolder holder, AdminWindowAnchoredListBean.DataBean o) {
                  TextView tv_agency_name = holder.getView(R.id.tv_agency_name);//代售点名称
                  TextView  tv_win_number = holder.getView(R.id.tv_win_number);//窗口号
                  TextView tv_office_code = holder.getView(R.id.tv_office_code);//主代售点用户登录名
                  TextView tv_audit_state = holder.getView(R.id.tv_audit_state);//审核状态
                  tv_agency_name.setText(o.getAgencyName()==null ?"":o.getAgencyName());
                  tv_win_number.setText( o.getWinNumber()==null ?"":o.getWinNumber());
                   tv_office_code.setText(o.getOfficeCode()==null ?"":o.getOfficeCode());

                    String  state = o.getAuditState()==null ?"":o.getAuditState();
                    tv_audit_state.setText(switchState(state));
                    if(switchState(state).equals("不通过")){
                        tv_audit_state.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_button_pressed));
                    }else{
                        tv_audit_state.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_button_pressed));
                    }
                }
            };
            mListView.setAdapter(commonAdapter);
            commonAdapter.notifyDataSetChanged();
        }

        if(commonAdapter!=null) {
            commonAdapter.notifyDataSetChanged();
        }
    }


    /**
     *   审核状态
     *  0 通过 1 待审核 2 不通过 4 解除绑定
     *
     * @param state
     * @return
     */
    private String switchState(String state){
        switch (state){
            case "0":
              return "审核通过";
            case "1":
                return "待审核";
            case "2":
                return "不通过";
            case "4":
                return "解除绑定";
        }
        return "";
    }


    /**
     * 对弹出框里面的数据设置
     *
     * @param holder
     * @param o
     * @param tag
     */
    @Override
    protected void setDataGetView(ViewHolder holder, Object o, String tag) {
        super.setDataGetView(holder, o, tag);
        switch (tag) {
            case "auditState":
                TextView textView = holder.getView(R.id.tv_text);
                textView.setText((String) o);
                break;
        }

    }

    @Override
    protected void itemPopClick(AdapterView<?> adapterView, View view, int i, long l, String tag) {
        super.itemPopClick(adapterView, view, i, l, tag);
        switch (tag) {
            case "auditState":
                String audit = auditStateList.get(i); //获取点击的文字
                tvAuditStatus.setText(audit);
                if("全部".equals(audit)){
                    linAuditStateString = ""; //全部状态下传空
                }else if ("审核通过".equals(audit)) {
                    linAuditStateString = Define.OUTLETS_NORMAL;    //审核状态
                } else if ("待审核".equals(audit)) {
                    linAuditStateString = Define.OUTLETS_PENDIG_AUDIT;
                } else if ("审核不通过".equals(audit)) {
                    linAuditStateString = Define.OUTLETS_NOTPASS;
                }else if("解除绑定".equals(audit)){
                    linAuditStateString = Define.ANCHORED_STATE_REMOVE;
                }
                break;
        }
    }

    /**
     * listview条目点击事件
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AdminWindowAnchoredListBean.DataBean dataBean = allWindowAnchoredList.get(i-1);
        String auditState = dataBean.getAuditState()==null ?"":dataBean.getAuditState();
        Intent intent = new Intent(AdminWindowAnchored.this,AdminWindowAnchoredDetails.class);
        intent.putExtra("anchored",dataBean);
        if(!(auditState.equals("0") || auditState.equals("2") || auditState.equals("4"))){  //0 审核通过   2 不通过
            intent.putExtra("pendingAudit","pendingAudit");//待审核
              startActivityForResult(intent,101);
        }else {
            startActivity(intent);
        }
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 101:
              //重新刷新
                requestData(officeId,et_variousTypes,auditStateString,"1"); //查询第一页
                break;
        }
    }

    //归属区域 ------------------------------------------------------------------------
    private void choiceManageAreaDialog() {
        // String url = Define.URL+"acct/getCode";
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.selectmanagearea_dialog, null);

        rl_choiceOneLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceOneLevel);
        rl_choiceTwoLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceTwoLevel);
        rl_choiceThirdLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceThirdLevel);
        tv_oneLevel = (TextView) view.findViewById(R.id.tv_oneLevel);
        tv_twoLevel = (TextView) view.findViewById(R.id.tv_twoLevel);
        tv_thirdLevel = (TextView) view.findViewById(R.id.tv_thirdLevel);
        bt_complete = (Button) view.findViewById(R.id.bt_complete);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        final AlertDialog dialog = builder.create();// 获取dialog

        dialog.show();

        rl_choiceOneLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSpinerPopWindow = new SpinerPopWindow(AdminWindowAnchored.this);
                CHOICE_TAG = "4";
                mSpinerPopWindow.refreshData(oneLevelNameList, 0);
                mSpinerPopWindow.setItemListener(AdminWindowAnchored.this);
                showSpinWindow(rl_choiceOneLevel);



            }
        });

        rl_choiceTwoLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneLevelCode == null || oneLevelCode.isEmpty()) {
                    Toast.makeText(AdminWindowAnchored.this, "请先选择第一级！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSpinerPopWindow = new SpinerPopWindow(AdminWindowAnchored.this);
                CHOICE_TAG = "5";
                mSpinerPopWindow.refreshData(twoLevelNameList, 0);
                mSpinerPopWindow.setItemListener(AdminWindowAnchored.this);
                showSpinWindow(rl_choiceTwoLevel);


            }
        });

        rl_choiceThirdLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (twoLevelCode == null || twoLevelCode.isEmpty()) {
                    Toast.makeText(AdminWindowAnchored.this, "请先选择第二级！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSpinerPopWindow = new SpinerPopWindow(AdminWindowAnchored.this);
                CHOICE_TAG = "6";
                mSpinerPopWindow.refreshData(threeLevelNameList, 0);
                mSpinerPopWindow.setItemListener(AdminWindowAnchored.this);
                showSpinWindow(rl_choiceThirdLevel);

            }
        });
        bt_complete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                oneLevel = tv_oneLevel.getText().toString();
                twoLevel = tv_twoLevel.getText().toString();
                threeLevel = tv_thirdLevel.getText().toString();
            /*    if ("选择第一级".equals(oneLevel)) {
                    Toast.makeText(AdminWindowAnchored.this, "请选择第一级！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("选择第二级".equals(twoLevel)) {
                    Toast.makeText(AdminWindowAnchored.this, "请选择第二级！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("选择第三级".equals(threeLevel)) {
                    Toast.makeText(AdminWindowAnchored.this, "请选择第三级！", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(oneLevelCode!=null){
                    linOfficeId = oneLevelCode;
                    manageArea = oneLevel;
                }
                if(twoLevelCode !=null){
                    linOfficeId = twoLevelCode;
                    manageArea=oneLevel + "/" + twoLevel;
                }
                if(threeLevelCode !=null){
                    linOfficeId = threeLevelCode; //只有最后都确定完了之后 才可以把归属区域id 保存
                    manageArea = oneLevel + "/" + twoLevel + "/" + threeLevel;
                }


                tvAttributionArea.setText(manageArea);
                dialog.dismiss();
            }
        });
    }

    // 选择1级区域管理列表
    private void setManageArea(int pos) {
        if (pos >= 0 && pos <= oneLevelNameList.size()) {
            String value = oneLevelNameList.get(pos);

            tv_oneLevel.setText(value);

            twoLevelCode=null; //把第二级的code设置为null
            tv_twoLevel.setText("选择第二级");
            threeLevelCode=null;//把第三级的code设置为null
            tv_thirdLevel.setText("选择第三级");
        }
    }

    // 获取1级管理区域code
    private String getOneLevelCode(int pos) {
        if (pos >= 0 && pos <= oneLevelCodeList.size()) {
            oneLevelCode = oneLevelCodeList.get(pos);

        }
        return oneLevelCode;
    }

    // 选择2级区域管理列表
    private void setManageArea2(int pos) {
        if (pos >= 0 && pos <= twoLevelNameList.size()) {
            String value = twoLevelNameList.get(pos);

            tv_twoLevel.setText(value);
            threeLevelCode=null;//把第三级的code设置为null
            tv_thirdLevel.setText("选择第三级");
        }
    }

    // 获取2级管理区域code
    private String getTwoLevelCode(int pos) {
        if (pos >= 0 && pos <= twoLevelCodeList.size()) {
            twoLevelCode = twoLevelCodeList.get(pos);

        }
        return twoLevelCode;
    }

    // 选择3级区域管理列表
    private void setManageArea3(int pos) {
        if (pos >= 0 && pos <= threeLevelNameList.size()) {
            String value = threeLevelNameList.get(pos);

            tv_thirdLevel.setText(value);
        }
    }

    // 获取3级管理区域code
    private String getThreeLevelCode(int pos) {
        if (pos >= 0 && pos <= threeLevelCodeList.size()) {
            threeLevelCode = threeLevelCodeList.get(pos);

        }
        return threeLevelCode;
    }

    private void showSpinWindow(View v) {
        Log.e("", "showSpinWindow");
        mSpinerPopWindow.setWidth(v.getWidth());
        mSpinerPopWindow.showAsDropDown(v);
    }

    @Override
    public void onItemClick(int pos) {
   /*     if ("1".equals(CHOICE_TAG)) {
            setAuditState(pos);
        } else */

        if ("4".equals(CHOICE_TAG)) {
            setManageArea(pos);
            oneLevelCode = getOneLevelCode(pos);
            try {
                getAllManageAreaTwo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("5".equals(CHOICE_TAG)) {
            setManageArea2(pos);
            twoLevelCode = getTwoLevelCode(pos);
            try {
                getAllManageAreaThree();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("6".equals(CHOICE_TAG)) {
            setManageArea3(pos);
            threeLevelCode = getThreeLevelCode(pos);
            jurisAreaCode = threeLevelCode;

        }
    }

    /**
     * 根据1级管理区域code获取管理区域列表
     *
     * @throws Exception
     */
    private void getAllManageAreaTwo() throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", oneLevelCode);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (oneLevelCode.isEmpty() || oneLevelCode == null) {
            Toast.makeText(AdminWindowAnchored.this, "请先选择第一级菜单！", Toast.LENGTH_SHORT).show();
            return;
        }
        cpd.show();
        // String url = Define.URL + "user/getCityListByProvice";
        String url = Define.URL + "user/getChildByParent";
        final List<User> twoLeveArealList = new ArrayList<User>();
        SessionJsonObjectRequest twoLeveAreaRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    User user = new User();

                                    JSONObject twoObj = (JSONObject) jsonArray.get(i);
                                    String twoLevelCode = twoObj.optString("code");
                                    String twoLevelName = twoObj.getString("name");

                                    user.setTwoLevelCode(twoLevelCode);
                                    ;
                                    user.setTwoLevelName(twoLevelName);
                                    ;

                                    twoLeveArealList.add(user);
                                }

                                Runnable cityRunnable = new Runnable() {

                                    @Override
                                    public void run() {

                                        Message msg = new Message();
                                        msg.what = MANAGEAREA2_LIST;
                                        msg.obj = twoLeveArealList;
                                        mHandler.sendMessage(msg);
                                    }
                                };
                                cpd.dismiss();

                                Thread twoThread = new Thread(cityRunnable);
                                twoThread.start();

                            } else {
                                cpd.dismiss();
                                Toast.makeText(AdminWindowAnchored.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cpd.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(AdminWindowAnchored.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        twoLeveAreaRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        twoLeveAreaRequest.setTag("twoLeveArea");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(twoLeveAreaRequest);

    }

    /**
     * 根据2级管理区域code获取三级管理区域列表
     *
     * @throws Exception
     */
    private void getAllManageAreaThree() throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", twoLevelCode);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (twoLevelCode.isEmpty() || twoLevelCode == null) {
            Toast.makeText(AdminWindowAnchored.this, "请先选择第二级菜单！", Toast.LENGTH_SHORT).show();
            return;
        }
        cpd.show();

        // String url = Define.URL + "user/getCityListByProvice";
        String url = Define.URL + "user/getChildByParent";
        final List<User> threeLeveArealList = new ArrayList<User>();
        SessionJsonObjectRequest threeLeveAreaRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    User user = new User();

                                    JSONObject twoObj = (JSONObject) jsonArray.get(i);
                                    String threeLevelCode = twoObj.optString("code");
                                    String threeLevelName = twoObj.getString("name");

                                    user.setThreeLevelCode(threeLevelCode);
                                    user.setThreeLevelName(threeLevelName);

                                    threeLeveArealList.add(user);
                                }

                                Runnable threeRunnable = new Runnable() {

                                    @Override
                                    public void run() {

                                        Message msg = new Message();
                                        msg.what = MANAGEAREA3_LIST;
                                        msg.obj = threeLeveArealList;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                Thread twoThread = new Thread(threeRunnable);
                                twoThread.start();
                                cpd.dismiss();
                            } else {
                                cpd.dismiss();
                                Toast.makeText(AdminWindowAnchored.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cpd.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(AdminWindowAnchored.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        threeLeveAreaRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        threeLeveAreaRequest.setTag("threeLeveArea");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(threeLeveAreaRequest);

    }

    /**
     * 获取所有管理区域
     *
     * @throws Exception
     */
    private void getAllManageArea() throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        cpd.show();
        // String url = Define.URL + "user/getProvinceList";
        String url = Define.URL + "user/getGroupList";
        final List<User> manageAreaList = new ArrayList<User>();
        SessionJsonObjectRequest manageAreaListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (null == response) {
                            Toast.makeText(AdminWindowAnchored.this, "服务器状态异常，请稍后再试", Toast.LENGTH_SHORT);
                            return;
                        }
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    User user = new User();

                                    JSONObject provinceObj = (JSONObject) jsonArray.get(i);
                                    String oneLevelCode = provinceObj.optString("code");
                                    String oneLevelName = provinceObj.getString("name");

                                    user.setOneLevelName(oneLevelName);
                                    user.setOneLevelCode(oneLevelCode);
                                    ;
                                    manageAreaList.add(user);
                                }

                                Runnable proRunnable = new Runnable() {

                                    @Override
                                    public void run() {

                                        Message msg = new Message();
                                        msg.what = MANAGEAREA_LIST;
                                        msg.obj = manageAreaList;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                Thread proThread = new Thread(proRunnable);
                                proThread.start();
                                cpd.dismiss();
                            } else {
                                cpd.dismiss();
                                Toast.makeText(AdminWindowAnchored.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cpd.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(AdminWindowAnchored.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        manageAreaListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        manageAreaListRequest.setTag("manageAreaList");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(manageAreaListRequest);

    }
}
