package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.AdminWindowAnchoredDetails;
import com.huoniao.oc.bean.AdminUserBean;
import com.huoniao.oc.bean.AdminWindowAnchoredListBean;
import com.huoniao.oc.bean.Document;
import com.huoniao.oc.bean.DocumentInformationBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.MyGridView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.DensityUtil;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

import static com.huoniao.oc.R.id.ll_company;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;


public class StationOutletsDetailsA extends BaseActivity implements AdapterView.OnItemClickListener{

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_organization_name)
    TextView tvOrganizationName;
    @InjectView(R.id.textView22)
    TextView textView22;
    @InjectView(R.id.tv_superior_organization2)
    TextView tvSuperiorOrganization2;
    @InjectView(R.id.ll_finSuperOrg)
    LinearLayout llFinSuperOrg;
    @InjectView(R.id.tv_first_agent)
    TextView tvFirstAgent;
    @InjectView(R.id.tv_company)
    TextView tvCompany;
    @InjectView(ll_company)
    LinearLayout llCompany;
    @InjectView(R.id.tv_first_agent_type)
    TextView tvFirstAgentType;
    @InjectView(R.id.ll_noFinLookInfo)
    LinearLayout llNoFinLookInfo;
    @InjectView(R.id.tv_subscription_window_number)
    TextView tvSubscriptionWindowNumber;
    @InjectView(R.id.ll_attached_window_number)
    LinearLayout llAttachedWindowNumber;
    @InjectView(R.id.ll_window)
    LinearLayout llWindow;
    @InjectView(R.id.tv_superior_organization)
    TextView tvSuperiorOrganization;
    @InjectView(R.id.tv_jurisdiction_area)
    TextView tvJurisdictionArea;
    @InjectView(R.id.tv_geographical_region)
    TextView tvGeographicalRegion;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.tv_legal_person_name)
    TextView tvLegalPersonName;
    @InjectView(R.id.tv_legal_person_phone)
    TextView tvLegalPersonPhone;
    @InjectView(R.id.tv_legal_person_id)
    TextView tvLegalPersonId;
    @InjectView(R.id.tv_person_charge_name)
    TextView tvPersonChargeName;
    @InjectView(R.id.tv_person_charge_phone)
    TextView tvPersonChargePhone;
    @InjectView(R.id.tv_person_charge_id)
    TextView tvPersonChargeId;
    @InjectView(R.id.ll_person_charge)
    LinearLayout llPersonCharge;
    @InjectView(R.id.tv_contact_name)
    TextView tvContactName;
    @InjectView(R.id.tv_contact_phone)
    TextView tvContactPhone;
    @InjectView(R.id.tv_deposit)
    TextView tvDeposit;
    @InjectView(R.id.gv_enclosure)
    MyGridView gvEnclosure;
    @InjectView(R.id.ll_noFinLookInfo2)
    LinearLayout llNoFinLookInfo2;
    @InjectView(R.id.bt_windowAnchored)
    Button btWindowAnchored;

    private List<DocumentInformationBean> documentInformationBeenList = new ArrayList<>();

    private final String corpCardforntSrcTag = "corpCardforntSrc"; //法人身份证正面路径
    private final String corpLicenceSrcTag = "corpLicenceSrc";//营业执照路径
    private final String corpCardrearSrcTag = "corpCardrearSrc";//法人身份证反面路径
    private final String staContIndexSrcTag = "staContIndexSrc";//车站合同首页路径
    private final String staContLastSrcTag = "staContLastSrc";//车站合同末页路径
    private final String operatorCardforntSrcTag = "operatorCardforntSrc";//负责人身份证正面路径
    private final String operatorCardrearSrcTag = "operatorCardrearSrc";//负责人身份证反面路径
    private final String fareAuthorizationSrcTag = "fareAuthorizationSrc";//票款汇缴授权书
    private CommonAdapter<DocumentInformationBean> commonAdapter;
    private AdminUserBean.DataBean dataBean;
    private String roleName;//角色名
    private String agent = "";  //第一代理人
    private String agentType = ""; ////第一代理人类型
    private MyPopWindow myPopWindowPhone;

    private ImageView iv_back;
    private Intent intent;
    private List<StationOutletsManageA> mData;
    private TextView tv_outlet_name, tv_outlet_windowNumber, tv_outlet_number, tv_outlet_city,
            tv_outlet_corpName, tv_outlet_corpMobile, tv_outlet_idNumber, tv_outlet_master,
            tv_outlet_masterPhone, tv_outlet_state;
    private Button bt_windowAnchored;
    private String id; //代售点ID
    private String code;//代售点机构编码
    private JSONObject area;//归属地市信息
    private String name;//代售点名称
    private String address;//联系地址
    private String master;//联系人
    private String phone;//联系人电话
    private String winNumber;//窗口号
    private String corpName; //法人姓名
    private String corpMobile;//法人手机
    private String corpIdNum;//法人身份证号
    private String state;//状态 0: 正常1: 待审核2: 审核不通过
    private String city;//用于接收'area'中的所属城市
    private String operatorName;//负责人姓名
    private String operatorMobile;//负责人手机
    private String operatorIdNum; //负责人身份证号

    private String relationDetailsTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_station_outletsdetails);
        ButterKnife.inject(this);
        initView();
        initData();
        initGrid();
//		ExitApplication.getInstance().addActivity(this);
    }

    private void initData() {
        intent = getIntent();
        relationDetailsTag = intent.getStringExtra("relationTag");
        if ("relationDetails".equals(relationDetailsTag)) {
            btWindowAnchored.setVisibility(View.GONE);
        }
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        code = intent.getStringExtra("code");
        city = intent.getStringExtra("city");
        winNumber = intent.getStringExtra("winNumber");
        corpName = intent.getStringExtra("corpName");
        corpMobile = intent.getStringExtra("corpMobile");
        corpIdNum = intent.getStringExtra("corpIdNum");
        master = intent.getStringExtra("master");
        phone = intent.getStringExtra("phone");
        state = intent.getStringExtra("state");
        operatorName = intent.getStringExtra("operatorName");
        operatorMobile = intent.getStringExtra("operatorMobile");
        operatorIdNum = intent.getStringExtra("operatorIdNum");

//        tv_outlet_name.setText(name);
//        tv_outlet_windowNumber.setText(winNumber);
//        tv_outlet_number.setText(code);
//        tv_outlet_city.setText(city);
//        tv_outlet_corpName.setText(corpName);
//        tv_outlet_corpMobile.setText(corpMobile);
//        tv_outlet_idNumber.setText(corpIdNum);
//        tv_outlet_master.setText(master);
//        tv_outlet_masterPhone.setText(phone);
//        if (Define.OUTLETS_NORMAL.equals(state)) {
//            tv_outlet_state.setText("正常");
//        } else if (Define.OUTLETS_PENDIG_AUDIT.equals(state)) {
//            tv_outlet_state.setText("待审核");
//        } else {
//            tv_outlet_state.setText("审核不通过");
//        }


        String url = Define.URL + "user/queryUserInfo";
        JSONObject jsonObject = new JSONObject();
        try {
//            if (loginName == null) {
            jsonObject.put("loginName", code);
//            } else {
//                jsonObject.put("loginName", loginName);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "queryUserInfo", "1", true, true);
        gvEnclosure.setOnItemClickListener(this);

    }

    private void initView() {

        User user = (User) readObject(StationOutletsDetailsA.this, "loginResult");
        roleName = user.getRoleName();
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        tv_outlet_name = (TextView) findViewById(R.id.tv_outlet_name);
//        tv_outlet_windowNumber = (TextView) findViewById(R.id.tv_outlet_windowNumber);
//        tv_outlet_number = (TextView) findViewById(R.id.tv_outlet_number);
//        tv_outlet_city = (TextView) findViewById(R.id.tv_outlet_city);
//        tv_outlet_corpName = (TextView) findViewById(R.id.tv_outlet_corpName);
//        tv_outlet_corpMobile = (TextView) findViewById(R.id.tv_outlet_corpMobile);
//        tv_outlet_idNumber = (TextView) findViewById(R.id.tv_outlet_idNumber);
//        tv_outlet_master = (TextView) findViewById(R.id.tv_outlet_master);
//        tv_outlet_masterPhone = (TextView) findViewById(R.id.tv_outlet_masterPhone);
//        tv_outlet_state = (TextView) findViewById(R.id.tv_outlet_state);
//        bt_windowAnchored = (Button) findViewById(R.id.bt_windowAnchored);
//        iv_back.setOnClickListener(this);
//        bt_windowAnchored.setOnClickListener(this);

        setPremissionShowHideView(Premission.FB_AGENCYCONNECT_SAVE, btWindowAnchored);  //是否有申请窗口号挂靠的权限

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
     * 初始化布局数据
     *
     * @param user
     */
    private void initLayoutData(AdminUserBean user) {

        List<AdminUserBean.DataBean> data = user.getData();
        dataBean = data.get(0);
//        posNo = dataBean.getPosNo();
//        if (posNo != null) {
//            etBarCode.setText(posNo);
//        }
//        loginName = dataBean.getLoginName(); //用户名
//        initHeder(dataBean);
//        tvId.setText("ID:" + loginName);
//        tvUserRoles.setText(dataBean.getRoleNames());
        AdminUserBean.DataBean.OfficeBean office = dataBean.getOffice();
//        tvName.setText(dataBean.getName());
//        tvAccountBalance.setText(dataBean.getBalanceString() + "元");
//        tvMinimumLimit.setText(NumberFormatUtils.formatDigits(Double.valueOf(dataBean.getMinimum())) + "元");
//
//        double minimumRemaining = Double.valueOf(dataBean.getMinimum()) - Double.valueOf(dataBean.getBalanceString());
//
//        double minimumRemainingRecharge = minimumRemaining <= 0 ? 0 : minimumRemaining;
//
//        tvMinimumRemainingRecharge.setText(NumberFormatUtils.formatDigits(minimumRemainingRecharge) + "元");  // 剩余最低充值额
        tvOrganizationName.setText(office.getName());
        tvSubscriptionWindowNumber.setText(office.getWinNumber()); //汇缴窗口号

        List<AdminUserBean.AgencysBean> agencys = user.getAgencys();//获取挂靠窗口号集合
        if (agencys != null && agencys.size() > 0) {
            for (int i = 0; i < agencys.size(); i++) {   //动态生成 textview控件
                TextView textView = new TextView(StationOutletsDetailsA.this);
                textView.setText(agencys.get(i).getWinNumber());
                textView.setTextColor(getResources().getColor(R.color.blue));
                int i1 = DensityUtil.px2dip(StationOutletsDetailsA.this, 30);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(i1, 0, 0, 0);
                textView.setLayoutParams(layoutParams);
                textView.setTag(agencys.get(i).getId());
//                textView.setOnClickListener(pChildClick);
                llAttachedWindowNumber.addView(textView);

            }

        } else {
            TextView textView = new TextView(StationOutletsDetailsA.this);
            textView.setText("无");
            textView.setTextColor(getResources().getColor(R.color.blue)); //挂靠窗口号
            llAttachedWindowNumber.addView(textView);
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
        tvDeposit.setText(office.getDepositString());

//        tvRegisterDate.setText(dataBean.getCreateDate() == null ? "" : dataBean.getCreateDate()); //注册时间
        //  tvAuditStatus.setText(dataBean.getAuditState()); //审核状态
//        tvAuditInstructions.setText(dataBean.getAuditReason()); //审核说明
//        tv_mobile.setText("手机号:" + dataBean.getMobile()); //手机号
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
                    llCompany.setVisibility(View.VISIBLE);
                    String agentCompanyName = dataBean.getOffice().getAgentCompanyName() == null ? "" : dataBean.getOffice().getAgentCompanyName();
                    tvCompany.setText(agentCompanyName);
                    break;
            }
        }
      /*  String state = office.getState();
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

        }*/

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
//                switch (tag) {
//                    case photoStrTag:
//                        try {
//                            Glide.with(StationOutletsDetailsA.this).load(file).asBitmap().centerCrop()
//                                    .into(new BitmapImageViewTarget(ivPhotoSrc) {
//                                        @Override
//                                        protected void setResource(Bitmap resource) {
//                                            RoundedBitmapDrawable circularBitmapDrawable =
//                                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
//                                            circularBitmapDrawable.setCircular(true);
//                                            ivPhotoSrc.setImageDrawable(circularBitmapDrawable);
//                                        }
//                                    });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                }
                if (i != -1) {
                    documentInformationBeenList.get(i).imageSrc = imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }


            }
        });
    }


    //动态窗口号的点击事件
   /* View.OnClickListener pChildClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!(roleName.contains("出纳") || roleName.contains("会计"))) {
                for (int i = 0; i < llAttachedWindowNumber.getChildCount(); i++) {
                    if (v.getTag() == llAttachedWindowNumber.getChildAt(i).getTag()) {
                        TextView textView = (TextView) llAttachedWindowNumber.getChildAt(i);
                        String trim = textView.getText().toString().trim(); //获取控件里面的窗口号
                        //请求网络 把窗口号作为参数  请求数据成功后需要跳转 挂靠详情    //挂靠窗口号点击查询详情
                        if (!trim.equals("无")) {
                            queryWindowNumberDetails((String) llAttachedWindowNumber.getChildAt(i).getTag());
                        }

                    }
                }
            }
        }
    };*/


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
                        Glide.with(StationOutletsDetailsA.this).load(documentInformationBean.imageFile).into(iv_image);
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

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.bt_windowAnchored:
                String clickTag = "windowAnchoredApply";
                intent = new Intent(StationOutletsDetailsA.this, WindowsAnchoredApplyA.class);
                intent.putExtra("clickTag", clickTag);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("code", code);
                intent.putExtra("winNumber", winNumber);
                intent.putExtra("corpName", corpName);
                intent.putExtra("corpMobile", corpMobile);
                intent.putExtra("corpIdNum", corpIdNum);
                intent.putExtra("master", master);
                intent.putExtra("phone", phone);
                intent.putExtra("operatorName", operatorName);
                intent.putExtra("operatorMobile", operatorMobile);
                intent.putExtra("operatorIdNum", operatorIdNum);

                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            default:
                break;
        }

    }*/



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

    @OnClick({R.id.iv_back, R.id.tv_legal_person_phone, R.id.tv_person_charge_phone, R.id.tv_contact_phone, R.id.bt_windowAnchored})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
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
            case R.id.bt_windowAnchored:

                String clickTag = "windowAnchoredApply";
                intent = new Intent(StationOutletsDetailsA.this, WindowsAnchoredApplyA.class);
                intent.putExtra("clickTag", clickTag);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("code", code);
                intent.putExtra("winNumber", winNumber);
                intent.putExtra("corpName", corpName);
                intent.putExtra("corpMobile", corpMobile);
                intent.putExtra("corpIdNum", corpIdNum);
                intent.putExtra("master", master);
                intent.putExtra("phone", phone);
                intent.putExtra("operatorName", operatorName);
                intent.putExtra("operatorMobile", operatorMobile);
                intent.putExtra("operatorIdNum", operatorIdNum);

                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        DocumentInformationBean documentInformationBean = documentInformationBeenList.get(position);
        final File file = documentInformationBean.imageFile;
        MyPopWindow myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                PhotoView documentImage = (PhotoView) view.findViewById(R.id.documentImage);
                if (file != null) {
                    try {
                        Glide.with(StationOutletsDetailsA.this).load(file).into(documentImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(StationOutletsDetailsA.this, "无图片信息！", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected int layout() {
                return R.layout.documentimage_dialog;
            }
        }.popWindowTouch(StationOutletsDetailsA.this).showAtLocation(view, Gravity.CENTER, 0, 0);
    }



    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        Gson gson = new Gson();
        switch (tag) {
            case "queryUserInfo":
                AdminUserBean user = gson.fromJson(json.toString(), AdminUserBean.class);
                initLayoutData(user);
                break;

            case "agencyConnectLists":
                AdminWindowAnchoredListBean adminWindowAnchoredListBean = gson.fromJson(json.toString(), AdminWindowAnchoredListBean.class);
                List<AdminWindowAnchoredListBean.DataBean> data = adminWindowAnchoredListBean.getData();
                if (data != null && data.size() > 0) {
                    Intent intent = new Intent(StationOutletsDetailsA.this, AdminWindowAnchoredDetails.class);
                    intent.putExtra("anchored", data.get(0));
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll("queryUserInfo");
            volleyNetCommon.getRequestQueue().cancelAll("agencyConnectLists");

        }
    }
}
