package com.huoniao.oc.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AdminUserBean;
import com.huoniao.oc.bean.AdminWindowAnchoredListBean;
import com.huoniao.oc.bean.Document;
import com.huoniao.oc.bean.DocumentInformationBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.MyGridView;
import com.huoniao.oc.user.OjiCreditA;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.DensityUtil;
import com.huoniao.oc.util.EditFilterUtils;
import com.huoniao.oc.util.NumberFormatUtils;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

import static com.huoniao.oc.R.id.iv_details;
import static com.huoniao.oc.R.id.tv_audit_instructions;
import static com.huoniao.oc.R.id.tv_creditScore;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class AdminUserDetails extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack; //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(iv_details)
    ImageView ivDetails; //右上角弹出
    @InjectView(R.id.tv_customer_type)
    TextView tvCustomerType; //用户类型
    @InjectView(R.id.tv_user_roles)
    TextView tvUserRoles;//用户角色
    @InjectView(R.id.tv_state)
    TextView tvState;//状态
    @InjectView(R.id.tv_account_balance)
    TextView tvAccountBalance;//账户余额
    @InjectView(R.id.tv_minimum_limit)
    TextView tvMinimumLimit; //最低限额
    @InjectView(R.id.tv_minimum_remaining_recharge)
    TextView tvMinimumRemainingRecharge; //剩余最低充值额
    @InjectView(R.id.tv_organization_name)
    TextView tvOrganizationName;//机构名称
    @InjectView(R.id.tv_first_agent)
    TextView tvFirstAgent; //第一代理人
    @InjectView(R.id.tv_first_agent_type)
    TextView tvFirstAgentType; //第一代理人类型
    @InjectView(R.id.tv_subscription_window_number)
    TextView tvSubscriptionWindowNumber;//汇缴窗口号
    @InjectView(R.id.tv_superior_organization)
    TextView tvSuperiorOrganization; //上级机构
    @InjectView(R.id.tv_jurisdiction_area)
    TextView tvJurisdictionArea; //管辖区域
    @InjectView(R.id.tv_geographical_region)
    TextView tvGeographicalRegion; //地理区域
    @InjectView(R.id.tv_address)
    TextView tvAddress;//地址
    @InjectView(R.id.tv_legal_person_name)
    TextView tvLegalPersonName; //法人姓名
    @InjectView(R.id.tv_legal_person_phone)
    TextView tvLegalPersonPhone; //法人手机
    @InjectView(R.id.tv_legal_person_id)
    TextView tvLegalPersonId; //法人身份证
    @InjectView(R.id.tv_person_charge_name)
    TextView tvPersonChargeName; //负责人姓名
    @InjectView(R.id.tv_person_charge_phone)
    TextView tvPersonChargePhone; //负责人手机
    @InjectView(R.id.tv_person_charge_id)
    TextView tvPersonChargeId; //负责人身份证
    @InjectView(R.id.tv_contact_name)
    TextView tvContactName;  //联系人姓名
    @InjectView(R.id.tv_contact_phone)
    TextView tvContactPhone; //联系人电话
    @InjectView(R.id.gv_enclosure)
    MyGridView gvEnclosure;
    @InjectView(R.id.tv_register_date)
    TextView tvRegisterDate; //注册时间
    @InjectView(R.id.tv_audit_status)
    TextView tvAuditStatus; //审核状态
    /*    @InjectView(R.id.tv_audit_date)
        TextView tvAuditDate; //审核时间*/
    @InjectView(tv_audit_instructions)
    EditText tvAuditInstructions;//审核情况说明
    @InjectView(R.id.tv_return)
    TextView tvReturn;
    @InjectView(R.id.activity_admin_user_details)
    LinearLayout activityAdminUserDetails;
    @InjectView(R.id.iv_photoSrc)
    ImageView ivPhotoSrc; //头像
    @InjectView(R.id.tv_id)
    TextView tvId; //用户id
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.ll_person_charge)
    LinearLayout llPersonCharge; //负责人模块容器
    @InjectView(R.id.ll_header)
    LinearLayout llHeader;
    @InjectView(R.id.tv_no_ok)
    TextView tvNoOk;
    @InjectView(R.id.ll_user)
    LinearLayout llUser; //审核页面模块
    @InjectView(R.id.tv_id2)
    TextView tv_id2;//用户id
    @InjectView(R.id.tv_name2)
    TextView tv_name2; //用户姓名
    @InjectView(R.id.tv_register_phone)
    TextView tv_register_phone; //注册手机
    @InjectView(R.id.tv_customer_type2)
    TextView tv_customer_type2;//用户类型
    @InjectView(R.id.tv_user_roles2)
    TextView tv_user_roles2; //用户角色
    @InjectView(R.id.tv_mobile) //手机号
    TextView tv_mobile;
    @InjectView(R.id.ll_minimum)
    LinearLayout ll_minimum;  //待审核 最低限额容器
    @InjectView(R.id.et_minimum)
    TextView et_minimum; //待审核 编辑框中 最低限额
    @InjectView(R.id.ll_company)
    LinearLayout ll_company;  //公司容器
    @InjectView(R.id.tv_company)
    TextView tv_company; //公司
    @InjectView(R.id.ll_window)
    LinearLayout ll_window;
    @InjectView(R.id.ll_attached_window_number)
    LinearLayout ll_attached_window_number;  //动态添加窗口号按钮
    @InjectView(R.id.tv_superior_organization2)
    TextView tvSuperiorOrganization2;
    @InjectView(R.id.ll_finSuperOrg)
    LinearLayout llFinSuperOrg;
    @InjectView(R.id.ll_noFinLookInfo)
    LinearLayout llNoFinLookInfo;
    @InjectView(R.id.ll_noFinLookInfo2)
    LinearLayout llNoFinLookInfo2;
    @InjectView(R.id.et_barCode)
    EditText etBarCode;
    @InjectView(R.id.tv_scanning)
    TextView tvScanning;
    @InjectView(R.id.ll_barCode)
    LinearLayout llBarCode;
    @InjectView(tv_creditScore)
    TextView tvCreditScore;
    private List<DocumentInformationBean> documentInformationBeenList = new ArrayList<>();
    ;
    private String agent = "";  //第一代理人
    private String agentType = ""; ////第一代理人类型
    private CommonAdapter<DocumentInformationBean> commonAdapter;
    private List<String> listDeatils = new ArrayList<>();
    ;
    private String loginName = "";
    private String pendingAudit;
    private AdminUserBean.DataBean dataBean;
    private String roleName;//角色名
    private MyPopWindow myPopWindowPhone;

    private String posNo;
    private static final int REQUEST_CODE = 101;

    private String roleNames = "";//每条信息对应的角色名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details);
        ButterKnife.inject(this);
        initWidget();
        initData();
        initGrid();
    }

    private void initWidget() {
        User user = (User) readObject(AdminUserDetails.this, "loginResult");
        roleName = user.getRoleName();
        tvAuditInstructions.setFilters(new InputFilter[]{EditFilterUtils.inputFilter, new InputFilter.LengthFilter(100)});
        if (roleName.contains("会计") || roleName.contains("出纳")) {
            llFinSuperOrg.setVisibility(View.VISIBLE);
            llNoFinLookInfo.setVisibility(View.GONE);
            llNoFinLookInfo2.setVisibility(View.GONE);
        }
    }

    private void initGrid() {

        documentInformationBeenList.add(new DocumentInformationBean(corpLicenceSrcTag, "营业执照"));
        documentInformationBeenList.add(new DocumentInformationBean(corpCardforntSrcTag, "法人代表身份证正面"));
        documentInformationBeenList.add(new DocumentInformationBean(corpCardrearSrcTag, "法人代表身份证反面"));
        documentInformationBeenList.add(new DocumentInformationBean(staContIndexSrcTag, "车站合同首页"));
        documentInformationBeenList.add(new DocumentInformationBean(staContLastSrcTag, "车站合同末页"));
        documentInformationBeenList.add(new DocumentInformationBean(operatorCardforntSrcTag, "负责人身份证正面"));
        documentInformationBeenList.add(new DocumentInformationBean(operatorCardrearSrcTag, "负责人身份证反面"));
        documentInformationBeenList.add(new DocumentInformationBean(fareAuthorizationSrcTag, "票款汇缴授权书"));
        setGridAdapter();
    }

    private void initData() {
        String id = getIntent().getStringExtra("id");
        pendingAudit = getIntent().getStringExtra("PendingAudit");
        String loginName = getIntent().getStringExtra("loginName");

        if (pendingAudit != null) {
            //做审核页面模块操作
            ivDetails.setVisibility(View.GONE); //隐藏右上角框
            llHeader.setVisibility(View.GONE); //隐藏头布局
            tvAuditInstructions.setEnabled(true);//设置编辑框可编辑
            //  tvNoOk.setVisibility(View.VISIBLE);//设置审核不通过按钮显示
            tvReturn.setText("通过"); //把返回改为 审核通过按钮
            llUser.setVisibility(View.VISIBLE);//显示审核界面头
            tvTitle.setText("用户审核");
            ll_minimum.setVisibility(View.VISIBLE);
            InputFilter[] filters = {new CashierInputFilter()};
            et_minimum.setFilters(filters); //过滤
            ll_window.setVisibility(View.GONE);//隐藏窗口号
            setPremissionShowHideView(Premission.SYS_USER_AUDIT, tvReturn); // 通过审核 隐藏
            setPremissionShowHideView(Premission.SYS_USER_AUDIT, tvNoOk); // 不通过审核 隐藏
            etBarCode.setEnabled(true);
            tvScanning.setVisibility(View.VISIBLE);
            //daishuli
        } else {
            tvTitle.setText("用户详情");
            tvReturn.setVisibility(View.VISIBLE);
            etBarCode.setEnabled(false);
            etBarCode.setHint("");//审核通过去掉提示
            tvScanning.setVisibility(View.GONE);
        }



        String url = Define.URL + "user/queryUserInfo";
        JSONObject jsonObject = new JSONObject();
        try {
            if (loginName == null) {
                jsonObject.put("id", id);
            } else {
                jsonObject.put("loginName", loginName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "queryUserInfo", "1", true, true);
        gvEnclosure.setOnItemClickListener(this);
    }

    /**
     * 获取用户信用
     */
    private void getCredit(boolean off) {
        String url = Define.URL + "user/getCredit";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginName", loginName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getCredit", "0", off, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        Gson gson = new Gson();
        switch (tag) {
            case "queryUserInfo":
                AdminUserBean user = gson.fromJson(json.toString(), AdminUserBean.class);
                initLayoutData(user);
                if ("代售点管理员".equals(roleNames)) {
                    tvCreditScore.setVisibility(View.VISIBLE);
                    getCredit(true);
                }else {
                    tvCreditScore.setVisibility(View.GONE);
                }
                break;
            case "auditUser":
                setResult(100);
                finish();
                break;

            case "agencyConnectLists":
                AdminWindowAnchoredListBean adminWindowAnchoredListBean = gson.fromJson(json.toString(), AdminWindowAnchoredListBean.class);
                List<AdminWindowAnchoredListBean.DataBean> data = adminWindowAnchoredListBean.getData();
                if (data != null && data.size() > 0) {
                    Intent intent = new Intent(AdminUserDetails.this, AdminWindowAnchoredDetails.class);
                    intent.putExtra("anchored", data.get(0));
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;

            case "getCredit":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String creditScore = jsonObject.optString("creditScore");
                    String creditLevelName = jsonObject.optString("creditLevelName");
                    if (creditScore != null){
                        tvCreditScore.setText("信用分：" + creditScore + "分");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 初始化布局数据
     *
     * @param user
     */
    private void initLayoutData(AdminUserBean user) {

        List<AdminUserBean.DataBean> data = user.getData();
        dataBean = data.get(0);
        posNo = dataBean.getPosNo();
        if (posNo != null) {
            etBarCode.setText(posNo);
        }
        loginName = dataBean.getLoginName(); //用户名
        initHeder(dataBean);
        tvId.setText("ID:" + loginName);
        roleNames = dataBean.getRoleNames();
        tvUserRoles.setText(dataBean.getRoleNames());
        AdminUserBean.DataBean.OfficeBean office = dataBean.getOffice();
        tvName.setText(dataBean.getName());
        tvAccountBalance.setText(dataBean.getBalanceString() + "元");
        tvMinimumLimit.setText(NumberFormatUtils.formatDigits(Double.valueOf(dataBean.getMinimum())) + "元");

        double minimumRemaining = Double.valueOf(dataBean.getMinimum()) - Double.valueOf(dataBean.getBalanceString());

        double minimumRemainingRecharge = minimumRemaining <= 0 ? 0 : minimumRemaining;

        tvMinimumRemainingRecharge.setText(NumberFormatUtils.formatDigits(minimumRemainingRecharge) + "元");  // 剩余最低充值额
        tvOrganizationName.setText(office.getName());
        tvSubscriptionWindowNumber.setText(office.getWinNumber()); //汇缴窗口号

        List<AdminUserBean.AgencysBean> agencys = user.getAgencys();//获取挂靠窗口号集合
        if (agencys != null && agencys.size() > 0) {
            for (int i = 0; i < agencys.size(); i++) {   //动态生成 textview控件
                TextView textView = new TextView(AdminUserDetails.this);
                textView.setText(agencys.get(i).getWinNumber());
                textView.setTextColor(getResources().getColor(R.color.blue));
                int i1 = DensityUtil.px2dip(AdminUserDetails.this, 30);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(i1, 0, 0, 0);
                textView.setLayoutParams(layoutParams);
                textView.setTag(agencys.get(i).getId());
                textView.setOnClickListener(pChildClick);
                ll_attached_window_number.addView(textView);

            }

        } else {
            TextView textView = new TextView(AdminUserDetails.this);
            textView.setText("无");
            textView.setTextColor(getResources().getColor(R.color.blue)); //挂靠窗口号
            ll_attached_window_number.addView(textView);
        }
        AdminUserBean.DataBean.OfficeBean.ParentBeanXXXXXXXX parent = office.getParent();
        if (parent != null) {
            tvSuperiorOrganization.setText(parent.getName() == null ? "" : parent.getName());
            tvSuperiorOrganization2.setText(parent.getName() == null ? "" : parent.getName());
        }
        AdminUserBean.DataBean.OfficeBean.JurisAreaBeanXXX jurisArea = office.getJurisArea();
        AdminUserBean.DataBean.OfficeBean.AreaBeanXXXX area = office.getArea();
        if (jurisArea != null) {
            tvJurisdictionArea.setText(jurisArea.getName() == null ? "" : jurisArea.getName()); //管辖区域
        }
        if (area != null) {
            tvGeographicalRegion.setText(area.getName() == null ? "" : area.getName()); //地理区域
        }
        agentType = office.getAgentType(); //第一代理人类型
        tvAddress.setText(office.getAddress() == null ? "" : office.getAddress());

        tvLegalPersonName.setText(office.getCorpName() == null ? "" : office.getCorpName());  //法人姓名
        tvLegalPersonPhone.setText(office.getCorpMobile() == null ? "" : office.getCorpMobile());
        tvLegalPersonId.setText(office.getCorpIdNum() == null ? "" : office.getCorpIdNum());

        tvPersonChargeName.setText(office.getOperatorName() == null ? "" : office.getOperatorName());//负责人姓名
        tvPersonChargePhone.setText(office.getOperatorMobile() == null ? "" : office.getOperatorMobile());
        tvPersonChargeId.setText(office.getOperatorIdNum() == null ? "" : office.getOperatorIdNum());

        tvContactName.setText(office.getMaster() == null ? "" : office.getMaster()); //联系人姓名
        tvContactPhone.setText(office.getPhone() == null ? "" : office.getPhone()); //联系人电话

        tvRegisterDate.setText(dataBean.getCreateDate() == null ? "" : dataBean.getCreateDate()); //注册时间
        //  tvAuditStatus.setText(dataBean.getAuditState()); //审核状态
        tvAuditInstructions.setText(dataBean.getAuditReason()); //审核说明
        tv_mobile.setText("手机号:" + dataBean.getMobile()); //手机号
        if (roleName.contains("出纳")) {
            tv_mobile.setVisibility(View.GONE);
        }
        agent = office.getAgent();
        if (agentType != null) {
            switch (agentType) {
                case "0":
                    tvFirstAgentType.setText("直营");
                    break;
                case "1":
                    tvFirstAgentType.setText("承包");
                    break;
            }
        }

        if (agent != null) {
            switch (agent) {
                case "0":
                    tvFirstAgent.setText("个人");
                    break;
                case "1":
                    tvFirstAgent.setText("铁青");
                    break;
                case "2":
                    tvFirstAgent.setText("邮政");
                    break;
                case "3":
                    tvFirstAgent.setText("公司");
                    ll_company.setVisibility(View.VISIBLE);
                    String agentCompanyName = dataBean.getOffice().getAgentCompanyName() == null ? "" : dataBean.getOffice().getAgentCompanyName();
                    tv_company.setText(agentCompanyName);
                    break;
            }
        }
        String state = office.getState();
        if (state != null) {
            switch (state) {
                case "0":
                    tvState.setText("正常");
                    tvAuditStatus.setText("正常"); //审核状态
                    break;
                case "1":
                    tvState.setText("待审核");
                    tvAuditStatus.setText("待审核"); //审核状态
                    break;
                case "2":
                    tvState.setText("审核不通过");
                    tvAuditStatus.setText("审核不通过"); //审核状态
                    break;
                case "3":
                    tvState.setText("冻结");
                    tvAuditStatus.setText("冻结"); //审核状态
                    break;
            }
        }
        String userType = dataBean.getUserType();
        if (userType != null) {
            switch (userType) {
                case "1":
                    tvCustomerType.setText("系统用户");
                    tv_customer_type2.setText("系统用户");
                    break;
                case "2":
                    tvCustomerType.setText("机构用户");
                    tv_customer_type2.setText("机构用户");
                    break;
                case "3":
                    tvCustomerType.setText("个人用户");
                    tv_customer_type2.setText("个人用户");
                    break;
            }
        }


        if (pendingAudit != null) { //待审核状态界面 进行数据初始化
            tv_id2.setText(loginName);//用户id

            tv_name2.setText(dataBean.getName()); //用户姓名

            tv_register_phone.setText(dataBean.getMobile()); //注册手机

            tv_user_roles2.setText(dataBean.getRoleNames());//用户角色

            et_minimum.setText(dataBean.getMinimum() + "");

        }

        addGridLayout();

        String corpCardforntSrc = office.getCorpCardforntSrc();//法人身份证正面路径
        String corpLicenceSrc = office.getCorpLicenceSrc();//营业执照路径
        String corpCardrearSrc = office.getCorpCardrearSrc();//法人身份证反面路径
        String staContIndexSrc = office.getStaContIndexSrc(); //车站合同首页路径
        String staContLastSrc = office.getStaContLastSrc();//车站合同末页路径
        String operatorCardforntSrc = office.getOperatorCardforntSrc();//负责人身份证正面路径
        String operatorCardrearSrc = office.getOperatorCardrearSrc();//负责人身份证反面路径
        String fareAuthorizationSrc = office.getFareAuthorizationSrc();//票款汇缴授权书


        //需要和初始化一样按照先后顺序添加
        List<Document> list = new ArrayList<>();
        // 这里需要做判断 是否需要添加这么多
        if (agent != null && agentType != null) {
         /*   if (agent.equals("0") && agentType.equals("0")) {
                llPersonCharge.setVisibility(View.GONE);//隐藏负责人模块
                list.add(new Document(corpLicenceSrc, corpLicenceSrcTag));
                list.add(new Document(corpCardforntSrc, corpCardforntSrcTag));
                list.add(new Document(corpCardrearSrc, corpCardrearSrcTag));
                list.add(new Document(staContIndexSrc, staContIndexSrcTag));
                list.add(new Document(staContLastSrc, staContLastSrcTag));
            } else {*/
            list.add(new Document(corpLicenceSrc, corpLicenceSrcTag));
            list.add(new Document(corpCardforntSrc, corpCardforntSrcTag));
            list.add(new Document(corpCardrearSrc, corpCardrearSrcTag));
            list.add(new Document(staContIndexSrc, staContIndexSrcTag));
            list.add(new Document(staContLastSrc, staContLastSrcTag));
            list.add(new Document(operatorCardforntSrc, operatorCardforntSrcTag));
            list.add(new Document(operatorCardrearSrc, operatorCardrearSrcTag));
            list.add(new Document(fareAuthorizationSrc, fareAuthorizationSrcTag));
            //  }
        }

        for (int i = 0; i < list.size(); i++) {
            Document document = list.get(i);
            try {
                if (!document.srcs.isEmpty()) {
                    getDocumentImage3(document.srcs, document.srcTag, i, false, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //获取图片回调
        setImgResultLinstener(new ImgResult() {
            @Override
            public void getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr) {
                switch (tag) {
                    case photoStrTag:
                        try {
                            Glide.with(AdminUserDetails.this).load(file).asBitmap().centerCrop()
                                    .into(new BitmapImageViewTarget(ivPhotoSrc) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            ivPhotoSrc.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                if (i != -1) {
                    documentInformationBeenList.get(i).imageSrc = imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }


            }
        });
    }

    //动态窗口号的点击事件
    View.OnClickListener pChildClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!(roleName.contains("出纳") || roleName.contains("会计"))) {
                for (int i = 0; i < ll_attached_window_number.getChildCount(); i++) {
                    if (v.getTag() == ll_attached_window_number.getChildAt(i).getTag()) {
                        TextView textView = (TextView) ll_attached_window_number.getChildAt(i);
                        String trim = textView.getText().toString().trim(); //获取控件里面的窗口号
                        //请求网络 把窗口号作为参数  请求数据成功后需要跳转 挂靠详情    //挂靠窗口号点击查询详情
                        if (!trim.equals("无")) {
                            queryWindowNumberDetails((String) ll_attached_window_number.getChildAt(i).getTag());
                        }

                    }
                }
            }
        }
    };

    private final String photoStrTag = "photostr";  //头像
    private final String corpCardforntSrcTag = "corpCardforntSrc"; //法人身份证正面路径
    private final String corpLicenceSrcTag = "corpLicenceSrc";//营业执照路径
    private final String corpCardrearSrcTag = "corpCardrearSrc";//法人身份证反面路径
    private final String staContIndexSrcTag = "staContIndexSrc";//车站合同首页路径
    private final String staContLastSrcTag = "staContLastSrc";//车站合同末页路径
    private final String operatorCardforntSrcTag = "operatorCardforntSrc";//负责人身份证正面路径
    private final String operatorCardrearSrcTag = "operatorCardrearSrc";//负责人身份证反面路径
    private final String fareAuthorizationSrcTag = "fareAuthorizationSrc";//票款汇缴授权书


    /**
     * 初始化头像
     *
     * @param user
     */
    private void initHeder(AdminUserBean.DataBean user) {
        String photoSrc = user.getPhotoSrc();
        if (photoSrc == null) {
            // ivPhotoSrc.setImageDrawable(getResources().getDrawable(R.drawable.applogo));
            try {
                Glide.with(this).load(R.drawable.applogo).asBitmap().centerCrop()
                        .into(new BitmapImageViewTarget(ivPhotoSrc) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivPhotoSrc.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                getDocumentImage3(photoSrc, photoStrTag, -1, false, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加证件资料布局
     */
    private void addGridLayout() {

        // 这里需要做处理  图片部分不展示
        if (agent != null && agentType != null) {
            documentInformationBeenList.clear();
         /*   if (agent.equals("0") && agentType.equals("0")) {
                documentInformationBeenList.add(new DocumentInformationBean(corpLicenceSrcTag, "营业执照"));
                documentInformationBeenList.add(new DocumentInformationBean(corpCardforntSrcTag, "法人代表身份证正面"));
                documentInformationBeenList.add(new DocumentInformationBean(corpCardrearSrcTag, "法人代表身份证反面"));
                documentInformationBeenList.add(new DocumentInformationBean(staContIndexSrcTag, "车站合同首页"));
                documentInformationBeenList.add(new DocumentInformationBean(staContLastSrcTag, "车站合同末页"));
            } else {*/
            documentInformationBeenList.add(new DocumentInformationBean(corpLicenceSrcTag, "营业执照"));
            documentInformationBeenList.add(new DocumentInformationBean(corpCardforntSrcTag, "法人代表身份证正面"));
            documentInformationBeenList.add(new DocumentInformationBean(corpCardrearSrcTag, "法人代表身份证反面"));
            documentInformationBeenList.add(new DocumentInformationBean(staContIndexSrcTag, "车站合同首页"));
            documentInformationBeenList.add(new DocumentInformationBean(staContLastSrcTag, "车站合同末页"));
            documentInformationBeenList.add(new DocumentInformationBean(operatorCardforntSrcTag, "负责人身份证正面"));
            documentInformationBeenList.add(new DocumentInformationBean(operatorCardrearSrcTag, "负责人身份证反面"));
            documentInformationBeenList.add(new DocumentInformationBean(fareAuthorizationSrcTag, "票款汇缴授权书"));
            //  }
        }
        setGridAdapter();


    }

    /**
     * 设置资料布局
     */
    private void setGridAdapter() {
        commonAdapter = new CommonAdapter<DocumentInformationBean>(this, documentInformationBeenList, R.layout.item_document_image_layout) {
            @Override
            public void convert(ViewHolder holder, DocumentInformationBean documentInformationBean) {
                ImageView iv_image = holder.getView(R.id.iv_image);
                TextView tv_image_name = holder.getView(R.id.tv_image_name);
                TextView tv_xin = holder.getView(R.id.tv_xin);
                String imgName = documentInformationBean.imageName;
                tv_xin.setTag(imgName);
              /*  if (tv_xin.getTag().equals(imgName)) {
                    if ("负责人身份证正面".equals(imgName) || "负责人身份证反面".equals(imgName) || "票款汇缴授权书".equals(imgName)) {
                        tv_xin.setVisibility(View.GONE);
                    } else {
                        tv_xin.setVisibility(View.VISIBLE);
                    }
                }*/


                iv_image.setTag(documentInformationBean.imageFlag);
                if (iv_image.getTag().equals(documentInformationBean.imageFlag)) {
                    try {
                        Glide.with(AdminUserDetails.this).load(documentInformationBean.imageFile).into(iv_image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                tv_image_name.setText(documentInformationBean.imageName);
            }
        };

        gvEnclosure.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();

    }


    @OnClick({R.id.iv_back, R.id.tv_no_ok, iv_details, R.id.tv_legal_person_phone, R.id.tv_person_charge_phone,
            R.id.tv_contact_phone, R.id.tv_return, R.id.tv_scanning})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_no_ok:
                //  审核不通过
                audit("2");
                break;
            case iv_details:
                deatils();
                break;
            case R.id.tv_legal_person_phone:
                callPhone(tvLegalPersonPhone.getText().toString().trim());
                break;
            case R.id.tv_person_charge_phone:
                callPhone(tvPersonChargePhone.getText().toString().trim());
                break;
            case R.id.tv_contact_phone:
                callPhone(tvContactPhone.getText().toString().trim());
                break;
            case R.id.tv_return:
                if (pendingAudit != null) {
                    audit("0");// 通过
                } else {
                    finish();
                }
                ;
                break;

            case R.id.tv_scanning://调用扫描二维码

                Intent intent = new Intent(AdminUserDetails.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;


        }
    }

    // //挂靠窗口号点击查询详情
    private void queryWindowNumberDetails(String id) {
        String url = Define.URL + "fb/agencyConnectList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("jurisAreaCode", "");//归属机构
            jsonObject.put("str", "");//窗口号/归属账号
            jsonObject.put("auditState", ""); //审核状态
            jsonObject.put("pageNo", "");//页数
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "agencyConnectLists", "0", true, true);

    }


    //审核
    private void audit(String auditState) {
        if (dataBean != null) {
            String userId = dataBean.getId();  //用户id
            String userType = dataBean.getUserType(); //用户类型
            String mobile = dataBean.getMobile();//用户手机
            String loginName = dataBean.getLoginName();//用户名
            AdminUserBean.DataBean.OfficeBean office = dataBean.getOffice();
            String id = office.getId();//机构id
            String type = office.getType(); //机构类型
            // String auditState = dataBean.getAuditState();//审核状态
            double minimum = dataBean.getMinimum();//最低限额
            String auditReason = tvAuditInstructions.getText().toString().trim(); //审核通过 不通过理由
            String et_minmum = et_minimum.getText().toString().trim();//TODO
            String barCode = etBarCode.getText().toString().trim();
            if (!checkAudit(userId, "用户id不能为空！"))
                return;
            if (!checkAudit(userType, "用户类型不能为空！"))
                return;
            if (!checkAudit(mobile, "用户手机不能为空！"))
                return;
            if (!checkAudit(loginName, "用户名不能为空！"))
                return;
            if (!checkAudit(id, "机构id不能为空！"))
                return;
            if (!checkAudit(type, "机构类型不能为空！"))
                return;
            if (!checkAudit(minimum + "", "最低限额为空！"))
                return;
            if (!checkAudit(auditReason, "审核理由不能为空！"))
                return;
            if (!checkAudit(et_minmum, "最低限额不能为空！"))
                return;
            if (!checkAudit(barCode, "POS机编码！"))
                return;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userId", userId);
                jsonObject.put("userType", userType);
                jsonObject.put("mobile", mobile);
                jsonObject.put("loginName", loginName);
                jsonObject.put("officeId", id);
                jsonObject.put("officeType", type);
                jsonObject.put("auditState", auditState);
                jsonObject.put("auditReason", auditReason);
                jsonObject.put("minimum", et_minmum);
                jsonObject.put("posNo", barCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = Define.URL + "user/auditUser";
            requestNet(url, jsonObject, "auditUser", "1", true, false);  //请求
        }
    }

    /**
     * 检查字段
     *
     * @param audit
     * @param message
     * @return
     */
    public boolean checkAudit(String audit, String message) {
        if (audit.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    /**
     * 拨打电话
     */
    private void callPhone(final String phone) {
        if (phone != null && !phone.isEmpty()) {
            if (myPopWindowPhone != null && myPopWindowPhone.isShow()) {
                myPopWindowPhone.dissmiss();
            }
            myPopWindowPhone = new MyPopAbstract() {
                @Override
                protected void setMapSettingViewWidget(View view) {
                    TextView tv_pop_phone = (TextView) view.findViewById(R.id.tv_pop_phone);
                    TextView tv_call = (TextView) view.findViewById(R.id.tv_call);
                    TextView tv_call_cancle = (TextView) view.findViewById(R.id.tv_call_cancle);
                    tv_pop_phone.setText(phone);
                    tv_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phone));
                            startActivity(intent);
                            myPopWindowPhone.dissmiss();
                        }
                    });
                    tv_call_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myPopWindowPhone.dissmiss();
                        }
                    });
                }

                @Override
                protected int layout() {
                    return R.layout.item_phone;
                }
            }.popWindowTouch(this).showAtLocation(tvTitle, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            ToastUtils.showToast(this, "手机号码为空！");
        }


    }

    //弹出右上角框
    private void deatils() {
        listDeatils.clear();
        listDeatils.add("交易记录");
        listDeatils.add("汇缴记录");
        listDeatils.add("信用明细");
        showPop(ivDetails, listDeatils, "listDeatils", 1);
    }

    @Override
    protected void itemPopClick(AdapterView<?> adapterView, View view, int i, long l, String tag) {
        super.itemPopClick(adapterView, view, i, l, tag);
        String s = listDeatils.get(i);
        Intent intent;
        if (s.equals("交易记录")) {
            //跳转交易记录
            intent = new Intent(AdminUserDetails.this, AdminOJiTransactionDetails.class);
            intent.putExtra("loginName", loginName);
            startActivityIntent(intent);
        } else if (s.equals("汇缴记录")) {
            // 跳转汇缴记录
            intent = new Intent(AdminUserDetails.this, AdminConsolidatedRecord.class);
            intent.putExtra("loginName", loginName);
            startActivityIntent(intent);
        } else if (s.equals("信用明细")) {
            // 跳转汇缴记录
            intent = new Intent(AdminUserDetails.this, OjiCreditA.class);
            intent.putExtra("loginName", loginName);
            intent.putExtra("adminLookUsr", "adminLookUsr");
            startActivityIntent(intent);
        }
    }

    @Override
    protected void setDataGetView(ViewHolder holder, Object o, String tag) {
        super.setDataGetView(holder, o, tag);
        TextView tv_text = holder.getView(R.id.tv_text);
        tv_text.setText((String) o);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll("queryUserInfo");
            volleyNetCommon.getRequestQueue().cancelAll("auditUser");
            volleyNetCommon.getRequestQueue().cancelAll("agencyConnectLists");
            volleyNetCommon.getRequestQueue().cancelAll("getCredit");
        }
    }

    /*
    点击查看大图
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DocumentInformationBean documentInformationBean = documentInformationBeenList.get(position);
        final File file = documentInformationBean.imageFile;
        MyPopWindow myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                PhotoView documentImage = (PhotoView) view.findViewById(R.id.documentImage);
                if (file != null) {
                    try {
                        Glide.with(AdminUserDetails.this).load(file).into(documentImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AdminUserDetails.this, "无图片信息！", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected int layout() {
                return R.layout.documentimage_dialog;
            }
        }.popWindowTouch(AdminUserDetails.this).showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (result != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminUserDetails.this);
                        builder.setTitle("扫描结果")
                                .setMessage(result)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        etBarCode.setText(result);

                                    }
                                }).setNegativeButton("重新扫描", new DialogInterface.OnClickListener() {// 消极

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(AdminUserDetails.this, CaptureActivity.class);
                                startActivityForResult(intent, REQUEST_CODE);

                            }
                        });

                        builder.create().show();

                    }

//                    Toast.makeText(AdminUserDetails.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(AdminUserDetails.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
