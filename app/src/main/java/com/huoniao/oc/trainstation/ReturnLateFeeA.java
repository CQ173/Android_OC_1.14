package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.DictAryBean;
import com.huoniao.oc.bean.LateFeeBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.financial.LaunchApplyA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class ReturnLateFeeA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_addSubmit)
    TextView tvAddSubmit;
    @InjectView(R.id.tv_start_date)
    TextView tvStartDate;
    @InjectView(R.id.ll_start_date)
    LinearLayout llStartDate;
    @InjectView(R.id.tv_end_date)
    TextView tvEndDate;
    @InjectView(R.id.ll_end_date)
    LinearLayout llEndDate;
    @InjectView(R.id.ll_date)
    LinearLayout llDate;
    @InjectView(R.id.tv_choiceState)
    TextView tvChoiceState;
    @InjectView(R.id.et_outletsAccount)
    EditText etOutletsAccount;
    @InjectView(R.id.tv_query)
    TextView tvQuery;
    @InjectView(R.id.tv_count)
    TextView tvCount;
    @InjectView(R.id.mPullRefreshListView)
    PullToRefreshListView mPullRefreshListView;

    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private String startDate = "";
    private String endDate = "";
    private List<DictAryBean.DataBean> dictAryBean = new ArrayList<>();
    private List<LateFeeBean.DataBean> lateFeeList = new ArrayList<>();
    private ListView lv_returnState;  //弹出框滞纳金返还状态列表
    private MyPopWindow myPopWindow;
    private String stateValue = "";//选择的状态值
    private float xs;
    private float ys;
    private ListView mListView;
    private CommonAdapter<LateFeeBean.DataBean> commonAdapter;
    private String next = "";        //返回来的页数
    private String roleName;
    private User user;
    private Intent intent;
    private String name;//登录时的机构名称
    private String createByName;//创建的机构名
    private boolean isCanHandle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_late_fee);
        ButterKnife.inject(this);
        initWidget();
        initData();
    }

    private void initWidget() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullRefreshListView.getRefreshableView();
        setPremissionShowHideView(Premission.FB_LATEFEERETURN_SAVE, tvAddSubmit); //添加递交权限
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        DateUtils dateUtils = new DateUtils();
//        startDate = dateUtils.getTime();  //获取今天日期
//        endDate = dateUtils.getTime();  //获取今天日期
//        tvStartDate.setText(startDate);
//        tvEndDate.setText(endDate);
        if (myDatePickerDialog == null) {
            myDatePickerDialog = new MyDatePickerDialog();
        }

        user = (User) readObject(ReturnLateFeeA.this, "loginResult");
        roleName = user.getRoleName();  //获取角色名
        name = user.getName();//获取机构名称
        initPullRefreshLinstener();
        queryLateFeeList("1");
    }

    @OnClick({R.id.iv_back, R.id.tv_addSubmit, R.id.ll_start_date, R.id.ll_end_date, R.id.tv_choiceState, R.id.tv_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_addSubmit:
                intent = new Intent(ReturnLateFeeA.this, AddLateFeeSubmitA.class);
                startActivityForResult(intent, 10);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.ll_start_date:
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        startDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(ReturnLateFeeA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvStartDate.setText(date);
                        queryLateFeeList("1");
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });

                break;
            case R.id.ll_end_date:

                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        endDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(ReturnLateFeeA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvEndDate.setText(date);

                        queryLateFeeList("1");
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });

                break;
            case R.id.tv_choiceState:
                queryDictAry("fb_latefee_state");

                break;
            case R.id.tv_query:
                queryLateFeeList("1");
                break;
        }
    }

    private void queryDictAry(String dictType){
        String url = Define.URL + "sys/getDictAry";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dictType", dictType);//通过字典接口查询滞纳金返还状态
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getDictAry", "0", true, false); //0 不代表什么
    }

    private void queryLateFeeList(String pageNumber){
        String outletsAccount = etOutletsAccount.getText().toString().trim();

        String url = Define.URL + "fb/latefeeReturnList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("agencyCode", outletsAccount);
            jsonObject.put("states", stateValue);
            jsonObject.put("pageNo", pageNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "latefeeReturnList", pageNumber, true, false); //0 不代表什么
    }


    private void auditingLatefee(String id){

        String url = Define.URL + "fb/latefeeReturnHandle";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("state", "2");//2代表审核通过

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "latefeeReturnHandlePass", "0", true, false); //0 不代表什么
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "getDictAry":
                dictAryBean.clear();
                Gson gson = new Gson();
                DictAryBean dataBean = gson.fromJson(json.toString(), DictAryBean.class);
                List<DictAryBean.DataBean> dictAryList =  dataBean.getData();
                DictAryBean.DataBean dataBean1 = new DictAryBean.DataBean();
                dataBean1.setLabel("全部");
                dataBean1.setValue("");
                dictAryBean.add(dataBean1);
                if (dictAryList != null && dictAryList.size() > 0){
                    dictAryBean.addAll(dictAryList);
                    showReturnState();
                }
                Log.d("dictAryBean", dictAryBean.toString());
                break;
            case "latefeeReturnList":
                setLateFeeAdpter(json, pageNumber);
                break;

            case "latefeeReturnHandlePass":
                ToastUtils.showToast(ReturnLateFeeA.this, "处理成功！");
                queryLateFeeList("1");
                break;
        }
    }

    private void setLateFeeAdpter(JSONObject jsonObject, String pageNumber) {

        Gson latefeeGson = new Gson();
        LateFeeBean lateFeeBean = latefeeGson.fromJson(jsonObject.toString(), LateFeeBean.class);
        if (pageNumber.equals("1")) {
            //集成清空处理
            lateFeeList.clear();
            if (commonAdapter != null) {
                mListView.setAdapter(commonAdapter);
            }
        }
        List<LateFeeBean.DataBean> lateFeeBeanList = lateFeeBean.getData();
        if (lateFeeBeanList != null && lateFeeBeanList.size() > 0) {
            lateFeeList.addAll(lateFeeBeanList);
        }
        next = String.valueOf(lateFeeBean.getNext());
        tvCount.setText(lateFeeBean.getSize() + "");

        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<LateFeeBean.DataBean>(ReturnLateFeeA.this, lateFeeList, R.layout.item_returnlatefee_list) {
                @Override
                public void convert(ViewHolder holder, final LateFeeBean.DataBean dataBean) {
                    holder.setText(R.id.tv_outletsAccount, dataBean.getAgencyCode())
                          .setText(R.id.tv_returnAmount, dataBean.getFeeString())
                          .setText(R.id.tv_returnDays, dataBean.getDaysCount() + "天")
                          .setText(R.id.tv_applyTime, dataBean.getCreateDateString())
                          .setText(R.id.tv_state, dataBean.getStateName());

                    TextView tv_lookImage = holder.getView(R.id.tv_lookImage);
                    final String imaUrl = dataBean.getInstructionSrc();
                    tv_lookImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            enlargeImage(ReturnLateFeeA.this, imaUrl, ivBack);
                        }
                    });

                    isCanHandle = dataBean.isIsCanHandle();
                    TextView tv_states = holder.getView(R.id.tv_states);
                    final String state = dataBean.getState();
                    createByName = dataBean.getCreateByName();
                    if (Define.NUMBER_ZERO.equals(state)){
                        tv_states.setVisibility(View.VISIBLE);
                        tv_states.setText("编辑");

                    }else if (Define.NUMBER_TWO.equals(state) && roleName.contains("出纳")){
                        tv_states.setText("确定代存");
                        setPremissionShowHideView(Premission.ACCT_FINANCEAPPLY_FORM, tv_states); //财务申请权限

                    }else if (Define.NUMBER_ONE.equals(state) && isCanHandle == true){
                        tv_states.setText("处理成功");
                        setPremissionShowHideView(Premission.FB_LATEFEERETURN_HANDLE, tv_states); //添加滞纳金返还处理权限
                    }else {
                        tv_states.setVisibility(View.GONE);
                    }



                    tv_states.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Define.NUMBER_ZERO.equals(state)){
//                                    ObjectSaveUtil.saveObject(ReturnLateFeeA.this, "lateFeeList", dataBean);
                                    intent = new Intent(ReturnLateFeeA.this, AddLateFeeSubmitA.class);
                                    intent.putExtra("needId", "needId");
                                    intent.putExtra("id", dataBean.getId());
                                    intent.putExtra("days", dataBean.getDays());
                                    intent.putExtra("agencyCode", dataBean.getAgencyCode());
                                    intent.putExtra("feeString", dataBean.getFeeString());
                                    intent.putExtra("applyReason", dataBean.getApplyReason());
                                    intent.putExtra("instructionSrc", dataBean.getInstructionSrc());
                                    startActivityForResult(intent, 10);
                                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                            }else if (Define.NUMBER_ONE.equals(state)){

                                auditingLatefee(dataBean.getId());

                            }else if (Define.NUMBER_TWO.equals(state)){
                                if (roleName.contains("出纳")){//出纳确定代存
                                    intent = new Intent(ReturnLateFeeA.this, LaunchApplyA.class);
                                    intent.putExtra("confirmDaiCun", "confirmDaiCun");
                                    intent.putExtra("latefeeReturnId", dataBean.getId());
                                    intent.putExtra("agencyAccount", dataBean.getAgencyCode());
                                    intent.putExtra("name", dataBean.getOperatorName());
                                    intent.putExtra("amount", dataBean.getFeeString());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                }
                            }

                           /* else if (Define.NUMBER_THREE.equals(state)){
//                                    ObjectSaveUtil.saveObject(ReturnLateFeeA.this, "lateFeeList", dataBean);
                                    intent = new Intent(ReturnLateFeeA.this, AddLateFeeSubmitA.class);
                                    intent.putExtra("days", dataBean.getDays());
                                    intent.putExtra("agencyCode", dataBean.getAgencyCode());
                                    intent.putExtra("feeString", dataBean.getFeeString());
                                    intent.putExtra("applyReason", dataBean.getApplyReason());
                                    intent.putExtra("instructionSrc", dataBean.getInstructionSrc());
                                    startActivityForResult(intent, 10);
                                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);


                            }*/

                        }
                    });



                }
            };
            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LateFeeBean.DataBean dataBean = lateFeeList.get(position - 1);
                intent = new Intent(ReturnLateFeeA.this, LateFeeSubmitDetailsA.class);
                intent.putExtra("id", dataBean.getId());
//                intent.putExtra("date", dataBean.getDays());
//                intent.putExtra("outletsAccount", dataBean.getAgencyCode());
//                intent.putExtra("returnAmount", dataBean.getFeeString());
//                intent.putExtra("returnReson", dataBean.getApplyReason());
//                intent.putExtra("imageUrl", dataBean.getInstructionSrc());
//                intent.putExtra("refuseReson", dataBean.getRefuseReason());
//                intent.putExtra("state", dataBean.getState());
                startActivityForResult(intent, 10);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });

    }


    /**
     * 展示申滞纳金返还状态下拉框
     */
    private void showReturnState() {
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                lv_returnState = (ListView) view.findViewById(R.id.lv_audit_status);
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //重新测量
//                int w =  lv_applyType.getMeasuredWidth();
//                cow = Math.abs(w - xOffset);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                tvChoiceState.getLocationOnScreen(arr);
                view.measure(0, 0);
                xs = arr[0] + tvChoiceState.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + tvChoiceState.getHeight();

                CommonAdapter<DictAryBean.DataBean> commonAdapter = new CommonAdapter<DictAryBean.DataBean>(ReturnLateFeeA.this, dictAryBean, R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, DictAryBean.DataBean data) {
                        holder.setText(R.id.tv_text, data.getLabel());

                    }
                };

                lv_returnState.setAdapter(commonAdapter);
                lv_returnState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String applyTypeStr = dictAryBean.get(i).getLabel(); //获取点击的文字
                        tvChoiceState.setText(applyTypeStr);
                        //获取点击的類型對應的代號
                        stateValue = dictAryBean.get(i).getValue();
                        myPopWindow.dissmiss();
                        queryLateFeeList("1");
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(this).create();
        myPopWindow.keyCodeDismiss(true); //返回键关闭
        myPopWindow.showAtLocation(tvChoiceState, Gravity.NO_GRAVITY, (int) xs, (int) ys);
    }


    /**
     * 查看图片(可放大)
     * @param context 上下文
     * @param imgUrl 图片相对路径
     * @param view 控件
     *//*
    protected void enlargeImage(final Context context, final String imgUrl, View view){
//		final Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() * SCALE, bitmap.getHeight() * SCALE);
        MyPopWindow myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                PhotoView documentImage = (PhotoView) view.findViewById(R.id.documentImage);
                if (imgUrl != null && !imgUrl.isEmpty()){

					*//*Glide.with(context).load(Define.IMG_URL + imgUrl).listener(new RequestListener<String, GlideDrawable>() {
						@Override
						public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
							Toast.makeText(MyApplication.mContext, "图片链接错误！", Toast.LENGTH_SHORT).show();
							return false;
						}

						@Override
						public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
							return false;
						}
					}).into(documentImage);*//*
                    try {
                        getDocumentImage(imgUrl, documentImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }else {
                    Toast.makeText(ReturnLateFeeA.this, "无图片信息！", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            protected int layout() {
                return R.layout.documentimage_dialog;
            }
        }.popWindowTouch(ReturnLateFeeA.this).showAtLocation(view, Gravity.CENTER,0,0);
    }*/

    /**
     *
     * @param tv_states 状态按钮控件
     * @param trainTag 火车站角色在某个状态时是否隐藏状态按钮
     * @param adminTag 管理员角色在某个状态时是否隐藏状态按钮
     * @param finTag 财务角色在某个状态时是否隐藏状态按钮
     * @param states 状态
     */
    /*private void RoleDifferentiation(TextView tv_states, boolean trainTag, boolean adminTag, boolean finTag, String states){


            if (Define.NUMBER_ZERO.equals(states)){
                tv_states.setVisibility(View.VISIBLE);
                tv_states.setText("编辑");
            }else if (Define.NUMBER_ONE.equals(states) && !trainTag){
                tv_states.setVisibility(View.GONE);
            }else if (Define.NUMBER_TWO.equals(states) && !trainTag){
                tv_states.setVisibility(View.GONE);
            }else if (Define.NUMBER_THREE.equals(states) && name.equals(createByName)){
                tv_states.setText("重新提交");
                tv_states.setVisibility(View.VISIBLE);
            }else if (Define.NUMBER_FOUR.equals(states) && !trainTag){
                tv_states.setVisibility(View.GONE);
            }

        }
            if (Define.NUMBER_ZERO.equals(states) && adminTag){
                tv_states.setText("编辑");
                tv_states.setVisibility(View.VISIBLE);
            }else if (Define.NUMBER_ONE.equals(states) && adminTag){
                tv_states.setText("处理成功");
                if (isCanHandle == true) {
                    setPremissionShowHideView(Premission.FB_LATEFEERETURN_HANDLE, tv_states); //添加递交权限
                }else {
                    tv_states.setVisibility(View.GONE);
                }
            }else if (Define.NUMBER_TWO.equals(states) && !adminTag){
                tv_states.setVisibility(View.GONE);
            }else if (Define.NUMBER_THREE.equals(states) && !adminTag){
                tv_states.setVisibility(View.GONE);
            }else if (Define.NUMBER_FOUR.equals(states) && !adminTag){
                tv_states.setVisibility(View.GONE);
            }

        }

        if (roleName.contains("出纳")) {
            if (Define.NUMBER_ZERO.equals(states) && !finTag){
                tv_states.setVisibility(View.GONE);
            }else if (Define.NUMBER_ONE.equals(states) && !finTag){
                tv_states.setVisibility(View.GONE);
            }else if (Define.NUMBER_TWO.equals(states) && finTag){
                tv_states.setText("确定代存");
                tv_states.setVisibility(View.VISIBLE);
            }else if (Define.NUMBER_THREE.equals(states) && !finTag){
                tv_states.setVisibility(View.GONE);
            }else if (Define.NUMBER_FOUR.equals(states) && !finTag){
                tv_states.setVisibility(View.GONE);
            }


    }*/



    /**
     * //上拉加载更多
     */
    private void initPullRefreshLinstener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {  //上拉加载更多
                if (next.equals("-1")) {
                    Toast.makeText(ReturnLateFeeA.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    });
                } else {
                    queryLateFeeList(next);
                }
            }
        });
    }

    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        mPullRefreshListView.onRefreshComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 10:
                queryLateFeeList("1");
                break;
        }
    }
}
