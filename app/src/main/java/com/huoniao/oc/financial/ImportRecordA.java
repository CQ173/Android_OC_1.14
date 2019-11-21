package com.huoniao.oc.financial;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.ImportRecordBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.UserInfo;
import com.huoniao.oc.common.myOnTabSelectedListener;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.user.PersonalInformationA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.treeIdList2;
import static com.huoniao.oc.R.id.ll_ownership_institution;

public class ImportRecordA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.v_backgroud)
    View vBackgroud;
    @InjectView(R.id.v_backgroud2)
    View vBackgroud2;
    @InjectView(R.id.tb_layout)
    TabLayout tbLayout;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;
    @InjectView(ll_ownership_institution)
    LinearLayout llOwnershipInstitution;
    @InjectView(R.id.et_variousTypes)
    EditText etVariousTypes;
    @InjectView(R.id.tv_query)
    TextView tvQuery;
    @InjectView(R.id.tv_recordTotal)
    TextView tvRecordTotal;
    @InjectView(R.id.mListView)
    ListView mListView;

    private MapData mapData;
    private MyPopWindow myPopWindow;
    private ListView lv_audit_status; //归属机构弹出框里面的listview
    private boolean switchConsolidatedFlag = true; // 已导入/未导入汇缴界面切换
    private float xs;
    private float ys;
    private String linOfficeId = "";  //临时的归属机构id
    private String stationName = "";
    private String type = "0";
    private CommonAdapter<ImportRecordBean.DataBean> commonAdapter;
    List<ImportRecordBean.DataBean> myListData = new ArrayList<>(); //listview列表集合
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_record);
        ButterKnife.inject(this);
        initWidget();
        initData();
    }

    private void initData() {
        requestPaymentInfo();
    }

    private void initWidget() {
        tvTitle.setText("导入记录");
        tbLayout.addTab(tbLayout.newTab().setText("今日已导入"));
        switchBackgroud(vBackgroud);
        tbLayout.addTab(tbLayout.newTab().setText("今日未导入"));
        setIndicator(tbLayout, 30, 30);

        tbLayout.setOnTabSelectedListener(new myOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中了tab的逻辑
                stationName = etVariousTypes.getText().toString().trim();
                switch (tab.getPosition()) {
                    case 0:  //今日已导入
                        switchBackgroud(vBackgroud);
                        type = "0";
//                        if (myListData != null){
//                            myListData.clear();
//                        }
                        requestPaymentInfo();
                        break;
                    case 1:  //今日未导入
                        switchBackgroud(vBackgroud2);
                        type = "1";
//                        if (myListData != null){
//                            myListData.clear();
//                        }
                        requestPaymentInfo();
                        break;
                }
            }

        });


    }

    @OnClick({R.id.iv_back, R.id.tv_query, R.id.tv_ownership_institution})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_query:
                stationName = etVariousTypes.getText().toString().trim();
//                if (myListData != null){
//                    myListData.clear();
//                }
                requestPaymentInfo();
                break;
            case R.id.tv_ownership_institution:
                showOwnershipPop();
                break;
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

    //控件背景颜色切换
    private void switchBackgroud(View view) {
        vBackgroud.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        vBackgroud2.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        if (view != null) {
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * 弹出归属机构
     */
    private void showOwnershipPop() {

        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
        //得到状态栏高度
        //返回键关闭
        mapData = new MapData(ImportRecordA.this, cpd, false) {
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }

                myPopWindow = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                        mapData.setTrainOwnershipData(lv_audit_status);
                        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                        int[] arr = new int[2];
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
//                        if (switchConsolidatedFlag) {  //true 表示汇缴记录界面  false 未完成汇缴界面
                        llOwnershipInstitution.getLocationOnScreen(arr);
                        xs = arr[0] + llOwnershipInstitution.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + llOwnershipInstitution.getHeight();
//                        }
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node officeId) {


//                                if (switchConsolidatedFlag) {
                                linOfficeId = String.valueOf(officeId.getAllTreeNode().id);  //临时的 汇缴记录页面
                                tvOwnershipInstitution.setText(officeId.getAllTreeNode().name);
//                                } else {
//                                    tvOwnershipInstitution2.setText(officeId.getAllTreeNode().name);
//                                    linOfficeIdNotComplete = String.valueOf(officeId.getAllTreeNode().id);  //临时的 未完成汇缴界面
//                                }

                                //  if (officeId.getAllTreeNode().type.equals("2")) {
                                myPopWindow.dissmiss();
                                //   }
                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop2;
                    }
                }.popupWindowBuilder(ImportRecordA.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
//                if (switchConsolidatedFlag){
                myPopWindow.showAsDropDown(llOwnershipInstitution);
//                }else{
//                    myPopWindow.showAsDropDown(ll_OutstandingContributions);
//                }
            }
        };
    }

    /**
     * 请求导入列表
     */
    public void requestPaymentInfo() {
        String url = Define.URL + "fb/paymentInfo";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("officeId", linOfficeId);
            jsonObject.put("stationName", stationName);
            jsonObject.put("type", type);
//            jsonObject.put("pageNo",pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "paymentInfo", "1", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "paymentInfo":
                setAdapter(json);
                break;
        }
    }

    private void setAdapter(JSONObject jsonObject) {
        Gson gson = new Gson();
        ImportRecordBean importRecordBean = gson.fromJson(jsonObject.toString(), ImportRecordBean.class);
        List<ImportRecordBean.DataBean> dataBeen = importRecordBean.getData();
        if (myListData != null){
            myListData.clear();
        }
        if (dataBeen != null) {
            tvRecordTotal.setText("" + dataBeen.size());

                myListData.addAll(dataBeen);
//                if (commonAdapter == null) {
                    commonAdapter = new CommonAdapter<ImportRecordBean.DataBean>(ImportRecordA.this, myListData, R.layout.item_import_record) {
                        @Override
                        public void convert(ViewHolder holder, ImportRecordBean.DataBean data) {
                            holder.setText(R.id.tv_stationName, "车站名称："+ data.getStandby2())
                                    .setText(R.id.tv_stationAccount, "车站账号："+ data.getStandby3())
                                    .setText(R.id.tv_ownershipOrgan, "归属机构："+ data.getString1())
                                    .setText(R.id.tv_dataNumber, data.getCount() + "")
                                    .setText(R.id.tv_ticketsNumber, data.getStandby4() + "")
                                    .setText(R.id.tv_ticketAmountSum, data.getSumString())
                                    .setText(R.id.tv_importTime, "导入时间："+ data.getTime());
                            LinearLayout ll_importArea = holder.getView(R.id.ll_importArea);
                            if ("1".equals(type)){//未导入把不需要的部分隐藏掉
                                ll_importArea.setVisibility(View.GONE);
                            }


                        }
                    };
                    mListView.setAdapter(commonAdapter);
//                }
//                commonAdapter.notifyDataSetChanged();




            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ImportRecordBean.DataBean bean = myListData.get(i);
                    intent = new Intent();
                    intent.putExtra("importRecordTag", "importRecordTag");
                    try {
                        UserInfo userInfo = new UserInfo();
                        userInfo.getUserInfo(ImportRecordA.this, cpd, PersonalInformationA.class, intent, bean.getStandby3());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
