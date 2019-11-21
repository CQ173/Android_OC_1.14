package com.huoniao.oc.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;
import com.huoniao.oc.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;


public class PersonalInformationA extends BaseActivity implements OnClickListener {
    private ImageView iv_back;
    private TextView tv_userName, tv_name, tv_mobile, tv_master, tv_userState, tv_corpName,
            tv_corpMobile, tv_corpIdNum, tv_area_name;
    private LinearLayout layout_updateMaster;//修改联系人
    private TextView tv_remarks;
    private TextView tv_email;
    private TextView tv_orgName;
    private TextView tv_address;
    private TextView tv_contactPhone;
    private TextView tv_jurisArea;
    private String jurisArea;
    private LinearLayout layout_updataMobile;

    private String corp_licence;//营业执照
    private String corp_card_fornt;//身份证正面
    private String corp_card_rear;//身份证反面

    private String isBindQQ;//是否绑定QQ
    private String isBindWx;//是否绑定微信
    private String relieveBindType = "";//取绑类型

    private TextView tv_see_licence, tv_see_cardFornt, tv_see_cardRear;
    private TextView tv_bindQQ, tv_bindWeiXin;
    private Intent intent;

    private LinearLayout layout_relieveQQBind;
    private LinearLayout layout_relieveWeiXinBind;
    private LinearLayout layout_noAdminContent;
    User user;
    private ProgressDialog pd;
    private TextView tv_firstAgent, tv_agentaType;
    private String firstAgent, agentaType;
    private LinearLayout layout_outsAgentArea, layout_outsFuZeRenArea, layout_outsNecessaryArea, layout_companyName;
    private LinearLayout layout_outsFuZeRenPaperArea;
    private TextView tv_fuZeRenName, tv_fuZeRenPhone, tv_fuZeRenIdNum, tv_fuZeren_cardFornt,
            tv_fuZeren_cardRear, tv_paySystemCertificate, tv_staContIndexSrc, tv_staContLastSrc,
            tv_staDepositSrc, tv_staDepInspSrc, tv_companyName;
    private String staContIndexSrc, staContLastSrc, staDepositSrc, staDepInspSrc, operatorCardforntSrc,
            operatorCardrearSrc, fareAuthorizationSrc, companyName;
    private ImageView iv_bindQQ, iv_bindWeiXin;
    private LinearLayout layout_jurisArea;

    private String roleName;//角色名
    private LinearLayout ll_attached_number;
    private MyPopWindow myPopWindow;
    private ListView lv_affiliated_details;  //窗口号列表
    private User allUser;
    private TextView tv_affiliated_details;
    private LinearLayout ll_geographical_position;
    private List<User.AgencysBean> allAgencysList = new ArrayList<>();
    private MyPopWindow myPopWindowSetting;
    private ListView lv_setting_map;
    private VolleyNetCommon volleyNetCommon;
    private List<User.AgencysBean> agencys;
    private TextView tv_update;
    private TextView tv_mainLocation;
    private LinearLayout ll_update;
    private ImageView iv_arrow_updataMobile;
    private ImageView iv_arrow_updateMaster;
    private static final int SCALE = 2;//照片放大缩小比例
    private User user2;
    private TextView tv_dataChange;
    private LinearLayout layout_legalManLicence; //法人证件区域
    private String importRecordTag;//汇缴导入列表传过来的标识
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinformation);
        MyApplication.getInstence().addActivity(this);
        initView();
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);

        intent = getIntent();
        user = (User) readObject(PersonalInformationA.this, "usetInfo");
        allUser = (User) ObjectSaveUtil.readObject(PersonalInformationA.this, "allUser");
        List<User.AgencysBean> agencys = allUser.getAgencys();
        StringBuilder sb = new StringBuilder();
        if (agencys != null) {
            for (int i = 0; i < agencys.size(); i++) {
                User.AgencysBean agencysBean = agencys.get(i);
                sb.append(agencysBean.getWinNumberX() + ";");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        tv_affiliated_details.setText(sb); //显示多个挂靠号
        tv_userName.setText(user.getLoginName());
        if (Define.OUTLETS_NORMAL.equals(user.getAuditState())) {
            tv_userState.setText("审核通过");
        } else if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {
            tv_userState.setText("待审核");
        } else if (Define.OUTLETS_NOTPASS.equals(user.getAuditState())) {
            tv_userState.setText("审核不通过");
        } else if (Define.QIANDIANDAI_NOTPASS.equals(user.getAuditState())) {
            tv_userState.setText("补充资料待审核");
        }
        tv_corpName.setText(user.getCorpName());
        tv_corpMobile.setText(user.getCorpMobile());
        if (user.getCorpIdNum() != null && user.getCorpIdNum().length() > 3) {
            //	String corpIdNumber = user.getCorpIdNum().substring(user.getCorpIdNum().length() - 1, user.getCorpIdNum().length());
            String corpIdNumber = user.getCorpIdNum().replaceAll("(?<=\\d{1})\\d(?=.{1})", "*");
            tv_corpIdNum.setText(corpIdNumber);
        }
        tv_area_name.setText(user.getArea_name());
        tv_name.setText(user.getName());
        tv_mobile.setText(user.getMobile());
        tv_orgName.setText(user.getOrgName());
        tv_address.setText(user.getAddress());
        tv_master.setText(user.getMaster());
        tv_contactPhone.setText(user.getContactPhone());
        tv_email.setText(user.getEmail());
        tv_remarks.setText(user.getRemarks());
        corp_licence = user.getCorp_licence();
        corp_card_fornt = user.getCorp_card_fornt();
        corp_card_rear = user.getCorp_card_rear();

        isBindQQ = intent.getStringExtra("isBindQQ");
        isBindWx = intent.getStringExtra("isBindWx");

        if ("true".equals(isBindQQ)) {
            tv_bindQQ.setText("是");
        } else {
            tv_bindQQ.setText("否");
            iv_bindQQ.setVisibility(View.INVISIBLE);
        }

        if ("true".equals(isBindWx)) {
            tv_bindWeiXin.setText("是");
        } else {
            tv_bindWeiXin.setText("否");
            iv_bindWeiXin.setVisibility(View.INVISIBLE);
        }


        //待审核状态  隐藏  修改手机 ，联系人 箭头
        if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {
            iv_arrow_updateMaster.setVisibility(View.GONE);
            iv_arrow_updataMobile.setVisibility(View.GONE);
        } else {
            iv_arrow_updateMaster.setVisibility(View.VISIBLE);
            iv_arrow_updataMobile.setVisibility(View.VISIBLE);
        }


        if (roleName.contains("系统管理员") || roleName.contains("出纳") || roleName.contains("会计")) {
            if (importRecordTag != null){//汇缴导入那边跳过来处理
                tv_title.setText("车站信息");
                layout_noAdminContent.setVisibility(View.VISIBLE);
                ll_geographical_position.setVisibility(View.GONE);
                ll_attached_number.setVisibility(View.GONE);
                layout_legalManLicence.setVisibility(View.GONE);
                layout_updataMobile.setEnabled(false);//汇缴票款导入进来不可修改信息
                iv_arrow_updataMobile.setVisibility(View.GONE);
                layout_relieveQQBind.setEnabled(false);
                iv_bindQQ.setVisibility(View.GONE);
                layout_relieveWeiXinBind.setEnabled(false);
                iv_bindWeiXin.setVisibility(View.GONE);
                layout_updateMaster.setEnabled(false);
                iv_arrow_updateMaster.setVisibility(View.GONE);
            }
        }


        if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
            firstAgent = user.getAgent();
            agentaType = user.getAgentType();

            if ("0".equals(firstAgent)) {
                tv_firstAgent.setText("个体户");
            } else if ("1".equals(firstAgent)) {
                tv_firstAgent.setText("铁青");
            } else if ("2".equals(firstAgent)) {
                tv_firstAgent.setText("邮政");
            } else if ("3".equals(firstAgent)) {
                tv_firstAgent.setText("公司");
            }

            if ("0".equals(agentaType)) {
                tv_agentaType.setText("直营");
            } else if ("1".equals(agentaType)) {
                tv_agentaType.setText("承包");
            }
            staContIndexSrc = user.getStaContIndexSrc();
            staContLastSrc = user.getStaContLastSrc();
            staDepositSrc = user.getStaDepositSrc();
            staDepInspSrc = user.getStaDepInspSrc();

			/*if ("0".equals(firstAgent) && "0".equals(agentaType)){
                layout_outsFuZeRenArea.setVisibility(View.GONE);
				layout_outsFuZeRenPaperArea.setVisibility(View.GONE);
			}else {*/
            tv_fuZeRenName.setText(user.getOperatorName());
            tv_fuZeRenPhone.setText(user.getOperatorMobile());
            if (user.getOperatorIdNum() != null && user.getOperatorIdNum().length() > 3) {
                String fuZeRenIdNum = user.getOperatorIdNum().replaceAll("(?<=\\d{1})\\d(?=.{1})", "*");

                tv_fuZeRenIdNum.setText(fuZeRenIdNum);
            }
            operatorCardforntSrc = user.getOperatorCardforntSrc();
            operatorCardrearSrc = user.getOperatorCardrearSrc();
            fareAuthorizationSrc = user.getFareAuthorizationSrc();
//			}

            if ("3".equals(firstAgent)) {
                layout_companyName.setVisibility(View.VISIBLE);
                companyName = user.getAgentCompanyName();
                tv_companyName.setText(companyName);
            }
            jurisArea = user.getJurisArea();
            if (jurisArea != null && !jurisArea.isEmpty()) {
                tv_jurisArea.setText(jurisArea);
            }


            //待审核状态
            if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState()) || Define.OUTLETS_NOTPASS.equals(user.getAuditState())) {
                //layout_outsFuZeRenArea.setVisibility(View.VISIBLE);
                ll_geographical_position.setVisibility(View.GONE); //如果是待审核或审核未通过状态  不显示地理位置
                ll_attached_number.setVisibility(View.GONE); //待审核或审核未通过状态不显示  挂靠窗口号
                ll_update.setVisibility(View.VISIBLE);  //待审核或审核未通过状态下显示修改按钮

            } else {
                ll_update.setVisibility(View.GONE);

            }

        }

        tv_fuZeren_cardFornt.setOnClickListener(this);
        tv_fuZeren_cardRear.setOnClickListener(this);
        tv_paySystemCertificate.setOnClickListener(this);
        tv_staContIndexSrc.setOnClickListener(this);
        tv_staContLastSrc.setOnClickListener(this);
        tv_staDepositSrc.setOnClickListener(this);
        tv_staDepInspSrc.setOnClickListener(this);


        //获取地图设置列表信息
        if (allAgencysList != null) {
            allAgencysList.clear();
        }
        getConfigurationMapData(); //获取主账号地图信息


    }

    //获取主账号地图数据
    private void getConfigurationMapData() {
        List<User.DataBean> data = allUser.getData();
        if (data != null) {
            User.DataBean.OfficeBean office = data.get(0).getOffice();
            if (office != null) {
                User.AgencysBean agencysBean = new User.AgencysBean();
                agencysBean.setWinNumberX(office.getWinNumberX()); //主账号挂靠窗口号
                agencysBean.setOperatorNameX(office.getOperatorNameX()); //负责人姓名
                agencysBean.setOperatorMobileX(office.getOperatorMobileX()); //负责人手机
                agencysBean.setOperatorIdNumX(office.getOperatorIdNumX()); //负责人身份证
                agencysBean.setLat(office.getLat() == null ? "0.0" : office.getLat());// 纬度
                agencysBean.setLng(office.getLng() == null ? "0.0" : office.getLng());//经度
                agencysBean.setGeogPosition(office.getGeogPosition()); //地理位置名称
                agencysBean.setIdX(office.getIdX());  //挂靠代售点id
                agencysBean.setMainAddress(office.getAddressX());  //主账号地址     默认第一次用 mainAddrress 搜索  设置过后用 经纬度搜索
                agencysBean.setMainAddressFlag("mainFlag"); //标记表示是主账号
                mainCountId = office.getIdX(); //获取主账号id 用户判断是否 是不是挂靠窗口号
                allAgencysList.add(agencysBean);
                //显示定位地址
                tv_mainLocation.setText(office.getGeogPosition());
            }

            if (allUser.getAgencys() != null && allUser.getAgencys().size() > 0) {
                this.allAgencysList.addAll(allUser.getAgencys()); //添加挂靠窗口号地址列表
            }
        }


    }

    private void initView() {
        intent = getIntent();
        importRecordTag = intent.getStringExtra("importRecordTag");
        user2 = (User) readObject(PersonalInformationA.this, "loginResult");
        roleName = user2.getRoleName();
        pd = new CustomProgressDialog(PersonalInformationA.this, "正在加载中...", R.drawable.frame_anim);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_userName = (TextView) findViewById(R.id.tv_userName);
        tv_userState = (TextView) findViewById(R.id.tv_userState);
        tv_corpName = (TextView) findViewById(R.id.tv_corpName);
        tv_corpMobile = (TextView) findViewById(R.id.tv_corpMobile);
        tv_corpIdNum = (TextView) findViewById(R.id.tv_corpIdNum);
        ll_attached_number = (LinearLayout) findViewById(R.id.ll_attached_number); //挂靠窗口号容器
        ll_geographical_position = (LinearLayout) findViewById(R.id.ll_geographical_position);    //地理位置容器
        tv_mainLocation = (TextView) findViewById(R.id.tv_mainLocation);  //地理位置主账号地址
        tv_affiliated_details = (TextView) findViewById(R.id.tv_affiliated_details);
        tv_see_licence = (TextView) findViewById(R.id.tv_see_licence);
        tv_see_cardFornt = (TextView) findViewById(R.id.tv_see_cardFornt);
        tv_see_cardRear = (TextView) findViewById(R.id.tv_see_cardRear);

        tv_area_name = (TextView) findViewById(R.id.tv_area_name);
        tv_jurisArea = (TextView) findViewById(R.id.tv_jurisArea);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        layout_updataMobile = (LinearLayout) findViewById(R.id.layout_updataMobile);
        iv_arrow_updataMobile = (ImageView) findViewById(R.id.iv_arrow_updataMobile);
        tv_master = (TextView) findViewById(R.id.tv_master);
        layout_updateMaster = (LinearLayout) findViewById(R.id.layout_updateMaster);
        iv_arrow_updateMaster = (ImageView) findViewById(R.id.iv_arrow_updateMaster);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_remarks = (TextView) findViewById(R.id.tv_remarks);
        tv_orgName = (TextView) findViewById(R.id.tv_orgName);
        tv_address = (TextView) findViewById(R.id.tv_address);

        tv_contactPhone = (TextView) findViewById(R.id.tv_contactPhone);
        layout_noAdminContent = (LinearLayout) findViewById(R.id.layout_noAdminContent);
        tv_bindQQ = (TextView) findViewById(R.id.tv_bindQQ);
        layout_relieveQQBind = (LinearLayout) findViewById(R.id.layout_relieveQQBind);
        tv_bindWeiXin = (TextView) findViewById(R.id.tv_bindWeiXin);
        layout_relieveWeiXinBind = (LinearLayout) findViewById(R.id.layout_relieveWeiXinBind);
        tv_firstAgent = (TextView) findViewById(R.id.tv_firstAgent);
        tv_agentaType = (TextView) findViewById(R.id.tv_agentaType);
        tv_fuZeRenName = (TextView) findViewById(R.id.tv_fuZeRenName);
        tv_fuZeRenPhone = (TextView) findViewById(R.id.tv_fuZeRenPhone);
        tv_fuZeRenIdNum = (TextView) findViewById(R.id.tv_fuZeRenIdNum);
        tv_fuZeren_cardFornt = (TextView) findViewById(R.id.tv_fuZeren_cardFornt);
        tv_fuZeren_cardRear = (TextView) findViewById(R.id.tv_fuZeren_cardRear);
        tv_paySystemCertificate = (TextView) findViewById(R.id.tv_paySystemCertificate);
        tv_staContIndexSrc = (TextView) findViewById(R.id.tv_staContIndexSrc);
        tv_staContLastSrc = (TextView) findViewById(R.id.tv_staContLastSrc);
        tv_staDepositSrc = (TextView) findViewById(R.id.tv_staDepositSrc);
        tv_staDepInspSrc = (TextView) findViewById(R.id.tv_staDepInspSrc);
        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        layout_outsAgentArea = (LinearLayout) findViewById(R.id.layout_outsAgentArea);
        layout_outsFuZeRenArea = (LinearLayout) findViewById(R.id.layout_outsFuZeRenArea);
        layout_outsFuZeRenPaperArea = (LinearLayout) findViewById(R.id.layout_outsFuZeRenPaperArea);
        layout_outsNecessaryArea = (LinearLayout) findViewById(R.id.layout_outsNecessaryArea);
        layout_companyName = (LinearLayout) findViewById(R.id.layout_companyName);
        iv_bindQQ = (ImageView) findViewById(R.id.iv_bindQQ);
        iv_bindWeiXin = (ImageView) findViewById(R.id.iv_bindWeiXin);
        layout_jurisArea = (LinearLayout) findViewById(R.id.layout_jurisArea);
        tv_update = (TextView) findViewById(R.id.tv_update); //修改
        ll_update = (LinearLayout) findViewById(R.id.ll_update);    // 修改 容器
        tv_dataChange = (TextView) findViewById(R.id.tv_dataChange); //代售点资料变更
        layout_legalManLicence = (LinearLayout) findViewById(R.id.layout_legalManLicence);  // 法人证件 容器
        tv_title = (TextView) findViewById(R.id.tv_title); //标题

        setPremissionShowHideView(Premission.FB_USERCHANGE_CHANGE,tv_dataChange);	// 是否有资料变更权限
        if (user2.getParentId() != null && !user2.getParentId().isEmpty()){//子账号隐藏资料变更
            tv_dataChange.setVisibility(View.GONE);
        }

        if (roleName.contains("系统管理员") || roleName.contains("出纳") || roleName.contains("会计")) {
            layout_noAdminContent.setVisibility(View.GONE);
            layout_outsAgentArea.setVisibility(View.GONE);
            layout_outsFuZeRenArea.setVisibility(View.GONE);
            layout_outsFuZeRenPaperArea.setVisibility(View.GONE);
            layout_outsNecessaryArea.setVisibility(View.GONE);
            layout_jurisArea.setVisibility(View.GONE);
            ll_update.setVisibility(View.GONE);//个人信息修改 隐藏
            tv_dataChange.setVisibility(View.GONE);

        } else if (Define.TRAINSTATION.equals(LoginA.IDENTITY_TAG) || Define.TRAINSTATION.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.TRAINSTATION.equals(WXEntryActivity.IDENTITY_TAG) || Define.TRAINSTATION.equals(RegisterSuccessA.IDENTITY_TAG)
                || "铁路总局".equals(roleName) || "铁路分局".equals(roleName)) {
            layout_outsAgentArea.setVisibility(View.GONE);
            layout_outsFuZeRenArea.setVisibility(View.GONE);
            layout_outsFuZeRenPaperArea.setVisibility(View.GONE);
            layout_outsNecessaryArea.setVisibility(View.GONE);
            layout_jurisArea.setVisibility(View.GONE);
            ll_geographical_position.setVisibility(View.GONE);
            ll_attached_number.setVisibility(View.GONE);
            ll_update.setVisibility(View.GONE);  //个人信息修改 隐藏
            tv_dataChange.setVisibility(View.GONE);
        }


        iv_back.setOnClickListener(this);
        tv_see_licence.setOnClickListener(this);
        tv_see_cardFornt.setOnClickListener(this);
        tv_see_cardRear.setOnClickListener(this);
        layout_relieveQQBind.setOnClickListener(this);
        layout_relieveWeiXinBind.setOnClickListener(this);
        layout_updataMobile.setOnClickListener(this);
        layout_updateMaster.setOnClickListener(this);
        ll_attached_number.setOnClickListener(this);
        ll_geographical_position.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        tv_dataChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //法人身份证正面
            case R.id.tv_see_licence:
                enlargeImage(PersonalInformationA.this, corp_licence, iv_back);

                break;
            //法人身份证反面
            case R.id.tv_see_cardFornt:
                enlargeImage(PersonalInformationA.this, corp_card_fornt, iv_back);
                break;
            //法人营业执照
            case R.id.tv_see_cardRear:
                enlargeImage(PersonalInformationA.this, corp_card_rear, iv_back);
                break;
            //负责人身份证正面
            case R.id.tv_fuZeren_cardFornt:
//                if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {    //待审核状态
                    enlargeImage(PersonalInformationA.this, operatorCardforntSrc, iv_back);
//                } else if (!("0".equals(firstAgent) && "0".equals(agentaType))) {
//                    enlargeImage(PersonalInformationA.this, operatorCardforntSrc, iv_back);
//                }
                break;
            //负责人身份证反面
            case R.id.tv_fuZeren_cardRear:

//                if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {
                    enlargeImage(PersonalInformationA.this, operatorCardrearSrc, iv_back);//待审核状态
//                } else if (!("0".equals(firstAgent) && "0".equals(agentaType))) {
//                    enlargeImage(PersonalInformationA.this, operatorCardrearSrc, iv_back);
//                }
                break;
            //票款汇缴授权书
            case R.id.tv_paySystemCertificate:
//                if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {//待审核状态
                    enlargeImage(PersonalInformationA.this, fareAuthorizationSrc, iv_back);
//                } else if (!("0".equals(firstAgent) && "0".equals(agentaType))) {
//                    enlargeImage(PersonalInformationA.this, fareAuthorizationSrc, iv_back);
//                }
                break;

            //车站合同首页
            case R.id.tv_staContIndexSrc:
                enlargeImage(PersonalInformationA.this, staContIndexSrc, iv_back);
                break;
            //车站合同尾页
            case R.id.tv_staContLastSrc:
                enlargeImage(PersonalInformationA.this, staContLastSrc, iv_back);
                break;
            //车站押金条
            case R.id.tv_staDepositSrc:
                enlargeImage(PersonalInformationA.this, staDepositSrc, iv_back);
                break;
            //车站押金年检证书
            case R.id.tv_staDepInspSrc:
                enlargeImage(PersonalInformationA.this, staDepInspSrc, iv_back);
                break;

            case R.id.layout_relieveQQBind:
                if ("true".equals(isBindQQ)) {
                    relieveBindType = "qq";
                    relieveBindDialog(relieveBindType);
                }

                break;
            case R.id.layout_relieveWeiXinBind:
                if ("true".equals(isBindWx)) {
                    relieveBindType = "weixin";
                    relieveBindDialog(relieveBindType);
                }

                break;

            case R.id.layout_updataMobile:
                //非待审核状态 手机可以修改
                if (!Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {
                    MyApplication.updatePersonMessagePhone = false;//个人信息 进入修改 手机号
                    intent = new Intent(PersonalInformationA.this, UpdataMobileA.class);
                    intent.putExtra("mobile", user.getMobile());
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;

            case R.id.layout_updateMaster:
                //非待审核状态下可以修改联系人
                if (!Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {
                    intent = new Intent(PersonalInformationA.this, UpdataContactA.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;
            case R.id.ll_attached_number:    // 挂靠窗口号
                if (RepeatClickUtils.repeatClick()) {
                    if (allUser != null) {
                        agencys = allUser.getAgencys();
                        if (agencys != null && agencys.size() > 0) {
                            if (myPopWindow != null) {
                                myPopWindow.dissmiss();
                            }
                            myPopWindow = new MyPopAbstract() {
                                @Override
                                protected void setMapSettingViewWidget(View view) {
                                    lv_affiliated_details = (ListView) view.findViewById(R.id.lv_affiliated_details);
                                    lvData();
                                    ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                                    iv_close.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myPopWindow.dissmiss();
                                        }
                                    });

                                }

                                @Override
                                protected int layout() {
                                    return R.layout.map_affiliated_details_pop;
                                }
                            }.poPwindow(this, true).showAtLocation(iv_back, Gravity.CENTER, 0, 0);
                        } else {
                            Toast.makeText(this, "没有挂靠窗口号信息！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.ll_geographical_position:
                if (user2 != null && user2.getParentId().isEmpty()) {  //getParentId 父账号ID为空说明是父账号  如果不为空说明是子账号 子账号不弹地图设置
                    if (RepeatClickUtils.repeatClick()) { //防止重复点击
                        setMapPop();
                    }
                }
                break;
            case R.id.tv_update:
                startActivityForResult(new Intent(PersonalInformationA.this, UpdatePersonalinformation.class), 22);  //修改个人信息
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                finish();
                break;

            case R.id.tv_dataChange://代售点资料变更
//                intent = new Intent(PersonalInformationA.this, DataChangeApplyA.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                findUnComfirmUserchange();
                break;
            default:
                break;
        }

    }

    String mainAddress = "";  //记录主账号地理位置

    //TODO 地图
    private void setMapPop() {
        if (myPopWindowSetting != null) {       //防止用户点击多次  关闭不了窗口
            myPopWindowSetting.dissmiss();
        }
        myPopWindowSetting = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                lv_setting_map = (ListView) view.findViewById(R.id.lv_setting_map);
                setConfigurationMapDate();
                iv_close.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindowSetting.dissmiss();
                    }
                });

                tv_confirm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (allAgencysList != null && allAgencysList.size() > 0) {
                            cpd.show();
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("isDefault", 1);
                                StringBuilder sb = new StringBuilder();
                                for (User.AgencysBean agency : allAgencysList
                                        ) {
                                    JSONObject json = new JSONObject();  //二级json
                                    json.put("geogPosition", agency.getGeogPosition());
                                    json.put("lng", agency.getLng());
                                    json.put("lat", agency.getLat());
                                    String mainFlag = agency.getMainAddressFlag() == null ? "" : agency.getMainAddressFlag();
                                    if (mainFlag.equals("mainFlag")) { //表示我是主账号
                                        jsonObject.put("mainAgency", json.toString());
                                        mainAddress = agency.getGeogPosition();
                                    } else {
                                        //我是挂靠窗口号
                                        json.put("agencyId", agency.getIdX());
                                        sb.append(json.toString() + ",");
                                    }
                                }
                                StringBuilder replace = new StringBuilder();
                                if (sb.length() > 2) {
                                    replace = sb.replace(sb.length() - 1, sb.length(), "");
                                }
                                replace.toString();
                                jsonObject.put("agencyList", "[" + replace.toString() + "]");
                                String url = Define.URL + "fb/setGeogPostion";
                                volleyNetCommon = new VolleyNetCommon();
                                JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Method.POST, url, jsonObject, new VolleyAbstract(PersonalInformationA.this) {
                                    @Override
                                    public void volleyResponse(Object o) {

                                    }

                                    @Override
                                    public void volleyError(VolleyError volleyError) {
                                        Toast.makeText(PersonalInformationA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    protected void netVolleyResponese(JSONObject json) {
                                        Toast.makeText(PersonalInformationA.this, "地图位置设置成功！", Toast.LENGTH_SHORT).show();
                                        myPopWindowSetting.dissmiss();
                                        tv_mainLocation.setText(mainAddress);
                                    }

                                    @Override
                                    protected void PdDismiss() {
                                        cpd.dismiss();

                                    }
                                }, "setSavaMap", true);

                                volleyNetCommon.addQueue(jsonObjectRequest);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(PersonalInformationA.this, "没有数据不能保存！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            @Override
            protected int layout() {
                return R.layout.map_setting_pop;
            }
        }.poPwindow(this, true).showAtLocation(iv_back, Gravity.CENTER, 0, 0);


    }

    private String mainCountId; // 主账号id
    private CommonAdapter<User.AgencysBean> agencysBeanCommonAdapter;

    /**
     * 地图设置列表数据
     */
    private void setConfigurationMapDate() {
        if (allAgencysList != null && allAgencysList.size() > 0) {
            agencysBeanCommonAdapter = new CommonAdapter<User.AgencysBean>(PersonalInformationA.this, allAgencysList, R.layout.map_setting_pop_item) {
                @Override
                public void convert(ViewHolder holder, User.AgencysBean agencysBean) {
                    TextView tv_windowNumbe_nick = holder.getView(R.id.tv_windowNumbe_nick);
                    tv_windowNumbe_nick.setTag(agencysBean.getIdX());
                    TextView tv_windowNumbe = holder.getView(R.id.tv_windowNumbe);//挂靠窗口号
                    TextView tv_setting_consignee_name = holder.getView(R.id.tv_setting_consignee_name); //代售点名称
                    TextView tv_setting_location = holder.getView(R.id.tv_setting_location); //地理位置
                    if (mainCountId == agencysBean.getIdX() || agencysBean.getAgencyName() == null) {  //如果代售点主账号id一样就是主账号
                        if (tv_windowNumbe_nick.getTag() == agencysBean.getIdX()) {
                            tv_windowNumbe_nick.setText("主账号");
                        }
                        tv_windowNumbe.setText(agencysBean.getWinNumberX() == null ? "" : agencysBean.getWinNumberX());
                        tv_setting_consignee_name.setText("");
                        tv_setting_location.setText(agencysBean.getGeogPosition() == null ? "" : agencysBean.getGeogPosition());

                    } else {
                        if (tv_windowNumbe_nick.getTag() == agencysBean.getIdX()) {
                            tv_windowNumbe_nick.setText("挂靠窗口号");
                        }
                        tv_windowNumbe.setText(agencysBean.getWinNumberX() == null ? "" : agencysBean.getWinNumberX());
                        tv_setting_consignee_name.setText(agencysBean.getAgencyName());
                        tv_setting_location.setText(agencysBean.getGeogPosition() == null ? "" : agencysBean.getGeogPosition());

                    }
                }
            };

            lv_setting_map.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            User.AgencysBean agencysBean = allAgencysList.get(i);
                            Intent intent = new Intent(PersonalInformationA.this, GeographicalPositionMapA.class);
                            intent.putExtra("AgencysBean", agencysBean);
                            intent.putExtra("listItemNumber", i);
                            startActivityForResult(intent, 21);
                            //	startActivity(intent);
                        }
                    });
                }
            });

            lv_setting_map.setAdapter(agencysBeanCommonAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 21) {   //表示地图位置保存 数据返回  TODO 21
            int itemNumber = data.getIntExtra("itemNumber", 0);
            User.AgencysBean agencysBean = (User.AgencysBean) data.getSerializableExtra("agencysBean");
            User.AgencysBean agencys = allAgencysList.get(itemNumber);
            agencys.setLat(agencysBean.getLat());
            agencys.setLng(agencysBean.getLng());
            agencys.setGeogPosition(agencysBean.getGeogPosition());
            agencysBeanCommonAdapter.notifyDataSetChanged();
        }
    }

    //加载窗口号详情数据
    private void lvData() {

        lv_affiliated_details.setAdapter(new CommonAdapter<User.AgencysBean>(PersonalInformationA.this, agencys, R.layout.map_affiliated_details_pop_listview_item) {
            @Override
            public void convert(ViewHolder holder, User.AgencysBean agencysBean) {
                holder.setText(R.id.tv_attached_number, agencysBean.getWinNumberX());// 挂靠窗口号
                holder.setText(R.id.tv_consignee_name, agencysBean.getAgencyName());//代售点名称
                holder.setText(R.id.tv_person_name, agencysBean.getOperatorNameX());//负责人姓名
                holder.setText(R.id.tv_phone_number, agencysBean.getOperatorMobileX());//负责人手机号
                holder.setText(R.id.tv_identity_id, agencysBean.getOperatorIdNumX());//负责人身份证
            }
        });

    }


    private void relieveBindDialog(final String relieveBindType) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.relievebind_dialog, null);

        final EditText et_password = (EditText) view.findViewById(R.id.et_password);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("解除绑定");
        builder.setView(view);

        builder.setPositiveButton("确定解除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = et_password.getText().toString();
                if (password == null) {
                    ToastUtils.showToast(PersonalInformationA.this, "请输入登录密码！");
                    return;
                }
                try {
                    relieveBind(relieveBindType, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }

        });
        AlertDialog dialog = builder.create();//创建dialog

        dialog.show();


    }

    private void relieveBind(final String type, String password) throws Exception {
        pd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
            jsonObject.put("type", type);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        SessionJsonObjectRequest relieveBindRequest = new SessionJsonObjectRequest(Method.POST,
                Define.URL + "user/removeBinding", jsonObject, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("debug", "response =" + response.toString());
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
                        pd.dismiss();
                        if ("qq".equals(type)) {
                            tv_bindQQ.setText("否");
//									tv_relieveQQBind.setVisibility(View.GONE);
                        } else if ("weixin".equals(type)) {
                            tv_bindWeiXin.setText("否");
//									tv_relieveWeiXinBind.setVisibility(View.GONE);
                        }
                        Toast.makeText(PersonalInformationA.this, "解除绑定成功！", Toast.LENGTH_SHORT).show();


                    } else if ("46000".equals(responseCode)) {
                        pd.dismiss();
                        Toast.makeText(PersonalInformationA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(PersonalInformationA.this, LoginA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    } else if ("46006".equals(responseCode)) {
                        pd.dismiss();
                        Toast.makeText(PersonalInformationA.this, "密码输入错误！", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(PersonalInformationA.this, "取消绑定失败！", Toast.LENGTH_SHORT).show();
                        Log.d("debug", message);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(PersonalInformationA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

            }
        });
        // 解决重复请求后台的问题
        relieveBindRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        relieveBindRequest.setTag("relieveBind");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(relieveBindRequest);

    }

    /**
     *查找未完成变更申请或账户注销申请
     */
    private void findUnComfirmUserchange(){
        String url = Define.URL+"user/findUnComfirmUserchange";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "findUnComfirmUserchange", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "findUnComfirmUserchange":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String type = jsonObject.optString("type");
                    if ("0".equals(type)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationA.this);
                        builder.setTitle("提示！")
                                .setMessage("您的账户已经提交了账户注销申请，审核期间无法进行资料变更操作")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                    }
                                });

                        builder.create().show();
                    }else if ("1".equals(type)){
                        intent = new Intent(PersonalInformationA.this, AccountLogOffAuditingA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }else {
                        intent = new Intent(PersonalInformationA.this, DataChangeApplyA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//		MyApplication.getHttpQueues().cancelAll("documentImage");
        MyApplication.getHttpQueues().cancelAll("relieveBind");

        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll("setSavaMap");
        }
    }

}
