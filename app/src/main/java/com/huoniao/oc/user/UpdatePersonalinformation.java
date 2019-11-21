package com.huoniao.oc.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.adapter.MyTextWatcher;
import com.huoniao.oc.bean.EventBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.UserInfo;
import com.huoniao.oc.common.luban.Luban;
import com.huoniao.oc.common.luban.OnCompressListener;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ImageCompressUtil;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SpinerPopWindow;
import com.huoniao.oc.util.TransformationImageUtils;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.huoniao.oc.R.id.layout_companyName;
import static com.huoniao.oc.R.id.tv_agentaType;
import static com.huoniao.oc.R.id.tv_area_name;
import static com.huoniao.oc.R.id.tv_firstAgent;
import static com.huoniao.oc.R.id.tv_jurisArea;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class UpdatePersonalinformation extends BaseActivity implements MySpinerAdapter.IOnItemSelectListener {


    @InjectView(R.id.iv_back)
    ImageView ivBack;       //返回键
    @InjectView(R.id.tv_name)
    EditText tvName;    // 姓名
    @InjectView(R.id.tv_mobile)
    TextView tvMobile;   //手机号
    @InjectView(R.id.layout_updataMobile)
    LinearLayout layoutUpdataMobile;   // 更改手机号
    @InjectView(R.id.tv_remarks)
    EditText etRemarks; //备注
    @InjectView(tv_jurisArea)
    TextView tvJurisArea;   //管辖区域
    @InjectView(R.id.layout_jurisArea)
    LinearLayout layoutJurisArea;   //作为点击容器
    @InjectView(tv_area_name)
    TextView tvAreaName;  // 地理区域
    @InjectView(R.id.tv_orgName)
    EditText tvOrgName; //机构名称
    @InjectView(tv_firstAgent)
    TextView tvFirstAgent; //第一代理人
    @InjectView(R.id.tv_companyName)
    TextView tvCompanyName;  //企业名称（公司）
    @InjectView(layout_companyName)
    LinearLayout layoutCompanyName;
    @InjectView(tv_agentaType)
    TextView tvAgentaType; //代理人类型
    @InjectView(R.id.layout_outsAgentArea)
    LinearLayout layoutOutsAgentArea;
    @InjectView(R.id.tv_corpName)
    EditText tvCorpName;//法人姓名
    @InjectView(R.id.tv_corpMobile)
    EditText tvCorpMobile;//法人手机
    @InjectView(R.id.tv_corpIdNum)
    EditText tvCorpIdNum;//法人身份证
    @InjectView(R.id.tv_person_charge_name)
    TextView tvPersonChargeName; //负责人姓名
    @InjectView(R.id.tv_person_charge_phone)
    TextView tvPersonChargePhone; //负责人手机号
    @InjectView(R.id.textView11)
    TextView textView11;
    @InjectView(R.id.tv_person_charge_idNum)
    TextView tvPersonChargeIdNum; //负责人身份证号
    @InjectView(R.id.tv_address)
    TextView tvAddress;  //地址
    @InjectView(R.id.tv_master)
    TextView tvMaster;//联系人
    @InjectView(R.id.layout_updateMaster)
    LinearLayout layoutUpdateMaster; //修改联系人
    @InjectView(R.id.tv_contactPhone)
    TextView tvContactPhone;   //联系电话
    @InjectView(R.id.iv_idNum_Positive)
    ImageView ivIdNumPositive;  //负责人身份证正面
    @InjectView(R.id.iv_idNum_otherSide)
    ImageView ivIdNumOtherSide;  //负责人身份证反面
    @InjectView(R.id.iv_payment_authorization)
    ImageView ivPaymentAuthorization; //票款汇缴授权书
    @InjectView(R.id.ll_idNum_Positive)
    LinearLayout llIdNumPositive; //负责人身份证正面
    @InjectView(R.id.iv_person_registration_certificate)
    ImageView ivPersonRegistrationCertificate;  //营业执照/法人登记证书
    @InjectView(R.id.iv_legal_person_idNumPositive)
    ImageView ivLegalPersonIdNumPositive; //法人代表身份证正面
    @InjectView(R.id.iv_legal_person_other_side)
    ImageView ivLegalPersonOtherSide;  //法人代表身份证反面
    @InjectView(R.id.iv_station_contract_home_page)
    ImageView ivStationContractHomePage;  //车站合同首页
    @InjectView(R.id.iv_the_station_contract)
    ImageView ivTheStationContract;  //车站合同尾页
    @InjectView(R.id.iv_station_bar)
    ImageView ivStationBar; //车站压金条
    @InjectView(R.id.ll_station_contract_home_page)
    LinearLayout llStationContractHomePage;
    @InjectView(R.id.iv_business_license)
    ImageView ivBusinessLicense;   //车站押金年检证书
    @InjectView(R.id.ll_business_license)
    LinearLayout llBusinessLicense;
    @InjectView(R.id.layout_noAdminContent)
    LinearLayout layoutNoAdminContent;
    @InjectView(R.id.ll_jurisArea)
    LinearLayout llJurisArea;
    @InjectView(R.id.ll_area_name)
    LinearLayout llAreaName;
    @InjectView(R.id.ll_firstAgent)
    LinearLayout llFirstAgent;    //第一代理人容器
    @InjectView(R.id.ll_agentaType)
    LinearLayout llAgentaType;  //代理人类型
    @InjectView(R.id.layout_outsFuZeRenArea)
    LinearLayout layoutOutsFuZeRenArea;  //负责人容器
    @InjectView(R.id.tv_submit)
    TextView tvSubmit;
    @InjectView(R.id.layout_payment_authorization)
    LinearLayout layoutPaymentAuthorization;
    @InjectView(R.id.ll_fuZeRen_certificates)
    LinearLayout llFuZeRenCertificates;

    private User user;
    private SpinerPopWindow mSpinerPopWindow;
    private SpinerPopWindow mSpinerPopWindow1;
    private RelativeLayout rl_choiceOneLevel;
    private RelativeLayout rl_choiceTwoLevel;
    private RelativeLayout rl_choiceThirdLevel;
    private TextView tv_oneLevel;
    private TextView tv_twoLevel;
    private TextView tv_thirdLevel;
    private Button bt_complete;
    private String twoLevel;
    private String threeLevel;
    private String manageArea;
    private String CHOICE_TAG;//
    private List<String> oneLevelCodeList = new ArrayList<String>();
    private List<String> oneLevelNameList = new ArrayList<String>();
    private List<String> twoLevelCodeList = new ArrayList<String>();
    private List<String> twoLevelNameList = new ArrayList<String>();
    private List<String> threeLevelNameList = new ArrayList<String>();
    private List<String> threeLevelCodeList = new ArrayList<String>();

    private List<String> fristAgentList = new ArrayList<String>();
    private List<String> agentTypeList = new ArrayList<String>();

    private List<String> provinceNameList = new ArrayList<String>();
    private List<String> provinceCodeList = new ArrayList<String>();

    private List<String> cityNameList = new ArrayList<String>();
    private List<String> cityCodeList = new ArrayList<String>();

    private String oneLevelCode = "";
    private String twoLevelCode = "";
    private String threeLevelCode = "";
    private String provinceCode = "";
    private static final int PROVINCE_LIST = 1;
    private static final int CITY_LIST = 2;
    private static final int USERNAME_VRI = 3;
    private static final int MANAGEAREA_LIST = 4;
    private static final int MANAGEAREA2_LIST = 5;
    private static final int MANAGEAREA3_LIST = 6;

    private String firstAgent, agentType, oneLevel;

    private static final int CAMERA_REQUESTCODE = 1; //相机拍照
    private static final int PHOTO_REQUESTCODE = 2;//相册选择
    private String ivIdNumPositivePath;//负责人身份证正面
    private String ivIdNumOtherSidePath;  //负责人身份证反面
    private String ivPaymentAuthorizationPath; //票款汇缴授权书
    private String ivPersonRegistrationCertificatePath;  //营业执照/法人登记证书
    private String ivLegalPersonIdNumPositivePath; //法人代表身份证正面
    private String ivLegalPersonOtherSidePath;  //法人代表身份证反面
    private String ivStationContractHomePagePath;  //车站合同首页
    private String ivTheStationContractPath;  //车站合同尾页
    private String ivStationBarPath; //车站压金条
    private String ivBusinessLicensePath;   //车站押金年检证书

    private String ivIdNumPositiveBase;//负责人身份证正面
    private String ivIdNumOtherSideBase;  //负责人身份证反面
    private String ivPaymentAuthorizationBase; //票款汇缴授权书
    private String ivPersonRegistrationCertificateBase;  //营业执照/法人登记证书
    private String ivLegalPersonIdNumPositiveBase; //法人代表身份证正面
    private String ivLegalPersonOtherSideBase;  //法人代表身份证反面
    private String ivStationContractHomePageBase;  //车站合同首页
    private String ivTheStationContractBase;  //车站合同尾页
    private String ivStationBarBase; //车站压金条
    private String ivBusinessLicenseBase;   //车站押金年检证书

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROVINCE_LIST: {
                    @SuppressWarnings("unchecked")
                    List<User> proList = (List<User>) msg.obj;

                    for (User user : proList) {
                        provinceNameList.add(user.getProvinceName());
                        provinceCodeList.add(user.getProvinceCode());
                    }

                    break;

                }
                case CITY_LIST: {
                    @SuppressWarnings("unchecked")
                    List<User> cityList = (List<User>) msg.obj;
                    cityNameList.clear();
                    cityCodeList.clear();
                    for (User user : cityList) {
                        cityNameList.add(user.getCityName());
                        cityCodeList.add(user.getCityCode());
                    }

                    break;

                }
                case USERNAME_VRI:

                    break;

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
    private RelativeLayout rl_choiceProvinces;
    private RelativeLayout rl_choiceCitys;
    private TextView tv_provinces;
    private TextView tv_citys;
    private String provinces;
    private String citys;
    private String location;
    private String cityCode = "";
    private MyPopWindow myPopWindow;
    private String tvNameS;
    private String tvMobileS;
    private String tvRemarksS;
    private String tvCompanyNameS;
    private String tvOrgNameS;
    private String tvCorpNameS;
    private String tvCorpMobileS;
    private String tvCorpIdNumS;
    private String tvPersonChargeNameS;
    private String tvPersonChargePhoneS;
    private String tvPersonChargeIdNumS;
    private String tvAddressS;
    private String tvMasterS;
    private String tvContactPhoneS;
    private VolleyNetCommon volleyNetCommon;
    private ImageCompressUtil imageCompressUtil;
    private Bitmap bmOperatorCardfornt;
    private Bitmap bmOperatorCardrear;
    private Bitmap bmFareAuthorization;
    private Bitmap bmCorpLicence;
    private Bitmap bmCorpCardFornt;
    private Bitmap bmCorpCardRear;
    private Bitmap bmStaContIndex;
    private Bitmap bmStaContLast;

    private final String  corpLicenceSrcTag ="corpLicenceSrc";//营业执照路径标识
    private final String  corpCardforntSrcTag ="corpCardforntSrc"; //法人身份证正面路径标识
    private final String  corpCardrearSrcTag  ="corpCardrearSrc";//法人身份证反面路径标识
    private final String  staContIndexSrcTag  ="staContIndexSrc";//车站合同首页路径标识
    private final String  staContLastSrcTag  ="staContLastSrc";//车站合同末页路径标识
    private final String  operatorCardforntSrcTag ="operatorCardforntSrc";//负责人身份证正面路径标识
    private final String  operatorCardrearSrcTag ="operatorCardrearSrc";//负责人身份证反面路径标识
    private final String  fareAuthorizationSrcTag="fareAuthorizationSrc";//票款汇缴授权书标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personalinformation);
        ButterKnife.inject(this);
        EventBus.getDefault().registerSticky(this);  //注册粘性事件  可以保证接收消息
        initData();
        initWidget();
    }

    private void initWidget() {
        tvName.setText(user.getName());
        tvMobile.setText(user.getMobile());
        tvOrgName.setText(user.getOrgName());
        tvCorpName.setText(user.getCorpName());
        tvCorpMobile.setText(user.getCorpMobile());
        tvCorpIdNum.setText(user.getCorpIdNum());
        tvPersonChargeName.setText(user.getOperatorName());
        tvPersonChargePhone.setText(user.getOperatorMobile());
        tvPersonChargeIdNum.setText(user.getOperatorIdNum());
        tvJurisArea.setText(user.getJurisArea());
        tvAreaName.setText(user.getArea_name());
        tvFirstAgent.setText(getAgent(user.getAgent()));
        tvAgentaType.setText(getAgentType(user.getAgentType()));
        tvAddress.setText(user.getAddress());
        tvMaster.setText(user.getMaster());
        tvContactPhone.setText(user.getContactPhone());
        agentType = getAgentType(user.getAgentType());
        firstAgent = getAgent(user.getAgent());
        tvMobileS = tvMobile.getText().toString().trim();  //手机
        setShowHideIdNumFuZeRen(agentType, firstAgent);  //设置显示隐藏 第一代理人 和代理类型 相关
        try {
            //负责人身份证正面
//            getDocumentImage2(user.getOperatorCardforntSrc(),"operatorCardfornt", 0);
            if (user.getOperatorCardforntSrc() != null && !user.getOperatorCardforntSrc().isEmpty()) {
                getDocumentImage3(user.getOperatorCardforntSrc(), operatorCardforntSrcTag, 0, false, "");
            }
            //负责人身份证反面
//            getDocumentImage2(user.getOperatorCardrearSrc(), "operatorCardrear", 0);
            if (user.getOperatorCardrearSrc() != null && !user.getOperatorCardrearSrc().isEmpty()) {
                getDocumentImage3(user.getOperatorCardrearSrc(), operatorCardrearSrcTag, 0, false, "");
            }
            //票款汇缴授权书
//            getDocumentImage2(user.getFareAuthorizationSrc(), "fareAuthorization", 0);
            if (user.getFareAuthorizationSrc() != null && !user.getFareAuthorizationSrc().isEmpty()) {
                getDocumentImage3(user.getFareAuthorizationSrc(), fareAuthorizationSrcTag, 0, false, "");
            }
            //营业执照/法人登记证书
//            getDocumentImage2(user.getCorp_licence(), "corp_licence", 0);
            if (user.getCorp_licence() != null && !user.getCorp_licence().isEmpty()) {
                getDocumentImage3(user.getCorp_licence(), corpLicenceSrcTag, 0, false, "");
            }
           //法人代表身份证正面
//            getDocumentImage2(user.getCorp_card_fornt(), "corp_card_fornt", 0);
            if (user.getCorp_card_fornt() != null && !user.getCorp_card_fornt().isEmpty()) {
                getDocumentImage3(user.getCorp_card_fornt(), corpCardforntSrcTag, 0, false, "");
            }
            //法人代表身份证反面
//            getDocumentImage2(user.getCorp_card_rear(), "corp_card_rear", 0);
            if (user.getCorp_card_rear() != null && !user.getCorp_card_rear().isEmpty()) {
                getDocumentImage3(user.getCorp_card_rear(), corpCardrearSrcTag, 0, false, "");
            }
            //车站合同首页
//            getDocumentImage2(user.getStaContIndexSrc(), "staContIndex", 0);
            if (user.getStaContIndexSrc() != null && !user.getStaContIndexSrc().isEmpty()) {
                getDocumentImage3(user.getStaContIndexSrc(), staContIndexSrcTag, 0, false, "");
            }
           //车站合同尾页
//            getDocumentImage2(user.getStaContLastSrc(), "staContLast", 0);
            if (user.getStaContLastSrc() != null && !user.getStaContLastSrc().isEmpty()) {
                getDocumentImage3(user.getStaContLastSrc(), staContLastSrcTag, 0, false, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取图片回调
        setImgResultLinstener(new ImgResult() {
            @Override
            public void getImageFile(File file,String imgUrl, String tag, int i, String linkUrlStr) {
                switch (tag){
                    case corpLicenceSrcTag:
                        glideSetImg(file, ivPersonRegistrationCertificate);
                        break;

                    case corpCardforntSrcTag:
                        glideSetImg(file, ivLegalPersonIdNumPositive);
                        break;
                    case corpCardrearSrcTag:
                        glideSetImg(file, ivLegalPersonOtherSide);
                        break;

                    case staContIndexSrcTag:
                        glideSetImg(file, ivStationContractHomePage);
                        break;
                    case staContLastSrcTag:
                        glideSetImg(file, ivTheStationContract);
                        break;

                    case operatorCardforntSrcTag:
                        glideSetImg(file, ivIdNumPositive);
                        break;

                    case operatorCardrearSrcTag:
                        glideSetImg(file, ivIdNumOtherSide);
                        break;

                    case fareAuthorizationSrcTag:
                        glideSetImg(file, ivPaymentAuthorization);
                        break;
                }
               /* if(i!=-1){
                    documentInformationBeenList.get(i).imageSrc =imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }*/


            }
        });

        if ("公司".equals(firstAgent)) {   //如果第一代理人为公司
            layoutCompanyName.setVisibility(View.VISIBLE);
            tvCompanyName.setText(user.getAgentCompanyName() == null ? "" : user.getAgentCompanyName());
        }

    }

    /**
     * 通过Glide设置图片到控件上
     */
    private void glideSetImg(File file, final ImageView imageView){
        //设置圆角图片
       /* Glide.with(UpdatePersonalinformation.this).load(file).asBitmap().centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });*/
        //设置普通图片
        try {
            Glide.with(UpdatePersonalinformation.this).load(file).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

  /*  @Override
    protected void getImageBitmap(Bitmap licenceBitmap, String tag,int p) {
        if (licenceBitmap != null) {
            if ("operatorCardfornt".equals(tag)) {

                ivIdNumPositive.setImageBitmap(licenceBitmap);
                bmOperatorCardfornt = licenceBitmap;
            }else if ("operatorCardrear".equals(tag)){
//				Glide.with(UserListDetailsA.this).load(licenceBitmap).into(iv_fuzerenIdOpposite);
                ivIdNumOtherSide.setImageBitmap(licenceBitmap);
                bmOperatorCardrear = licenceBitmap;
            }else if ("corp_licence".equals(tag)){
//				Glide.with(UserListDetailsA.this).load(licenceBitmap).into(iv_businessLicense);
                ivPersonRegistrationCertificate.setImageBitmap(licenceBitmap);
                bmCorpLicence = licenceBitmap;
            }else if ("corp_card_fornt".equals(tag)){
//				Glide.with(UserListDetailsA.this).load(licenceBitmap).into(iv_identificationFront);
                ivLegalPersonIdNumPositive.setImageBitmap(licenceBitmap);
                bmCorpCardFornt = licenceBitmap;
            }else if ("corp_card_rear".equals(tag)){
//				Glide.with(UserListDetailsA.this).load(licenceBitmap).into(iv_identificationOpposite);
                ivLegalPersonOtherSide.setImageBitmap(licenceBitmap);
                bmCorpCardRear = licenceBitmap;
            }else if ("fareAuthorization".equals(tag)){
//				Glide.with(UserListDetailsA.this).load(licenceBitmap).into(iv_paySystemCertificate);
                ivPaymentAuthorization.setImageBitmap(licenceBitmap);
                bmFareAuthorization = licenceBitmap;
            }else if ("staContIndex".equals(tag)){
//				Glide.with(UserListDetailsA.this).load(licenceBitmap).into(iv_stationContactFrist);
                ivStationContractHomePage.setImageBitmap(licenceBitmap);
                bmStaContIndex = licenceBitmap;
            }else if ("staContLast".equals(tag)){
//				Glide.with(UserListDetailsA.this).load(licenceBitmap).into(iv_stationContactEnd);
                ivTheStationContract.setImageBitmap(licenceBitmap);
                bmStaContLast = licenceBitmap;
            }

        }
    }*/


    private void initData() {
        MyApplication.getInstence().addActivity(this);
        user = (User) readObject(UpdatePersonalinformation.this, "usetInfo");
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("poneNumber");
        String openId = intent.getStringExtra("openId");
        String loginType = intent.getStringExtra("loginType");
        // Log.d("debug", phoneNumber);

        //		identityTypeList.add("火车站");
        fristAgentList.add("个体户");
        fristAgentList.add("铁青");
        fristAgentList.add("邮政");
        fristAgentList.add("公司");
        agentTypeList.add("直营");
        agentTypeList.add("承包");
        // identityTypeList.add("管理员");

        try {
            getAllProvince();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getAllManageArea();
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private String imagePathTag = "-1";

    @OnClick({R.id.iv_back, R.id.layout_updataMobile, R.id.tv_jurisArea, R.id.tv_area_name, R.id.tv_firstAgent,
            R.id.tv_agentaType, R.id.iv_idNum_otherSide, R.id.iv_payment_authorization,
            R.id.iv_person_registration_certificate, R.id.iv_legal_person_idNumPositive,
            R.id.iv_legal_person_other_side, R.id.iv_station_contract_home_page,
            R.id.iv_the_station_contract, R.id.iv_station_bar, R.id.iv_business_license, R.id.ll_area_name, R.id.ll_jurisArea
            , R.id.ll_firstAgent, R.id.ll_agentaType, R.id.iv_idNum_Positive, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_updataMobile:
                MyApplication.updatePersonMessagePhone = true; //修改个人信息 进入 修改手机
                Intent intent = new Intent(UpdatePersonalinformation.this, UpdataMobileA.class);
                intent.putExtra("mobile", user.getMobile());
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case  R.id.tv_jurisArea:
                break;
            case  R.id.tv_area_name:
                break;
            case  R.id.tv_firstAgent:
                break;
            case  R.id.tv_agentaType:
                break;
            case R.id.iv_idNum_otherSide:
                ;
                imagePathTag = "0";
                selectCamera(imagePathTag);  //负责人身份证反面
                break;
            case R.id.iv_payment_authorization: //票款汇缴授权书
                imagePathTag = "1";
                selectCamera(imagePathTag);
                break;
            case R.id.iv_person_registration_certificate: //营业执照/法人登记证书
                imagePathTag = "2";
                selectCamera(imagePathTag);
                break;
            case R.id.iv_legal_person_idNumPositive:   //法人代表身份证正面
                imagePathTag = "3";
                selectCamera(imagePathTag);
                break;
            case R.id.iv_legal_person_other_side:  //法人代表身份证反面
                imagePathTag = "4";
                selectCamera(imagePathTag);
                break;
            case R.id.iv_station_contract_home_page: //车站合同首页
                imagePathTag = "5";
                selectCamera(imagePathTag);
                break;
            case R.id.iv_the_station_contract:  //车站合同尾页
                imagePathTag = "6";
                selectCamera(imagePathTag);
                break;
            case R.id.iv_station_bar://车站压金条
                imagePathTag = "7";
                selectCamera(imagePathTag);
                break;
            case R.id.iv_business_license: //营业执照/法人登记证书
                imagePathTag = "8";
                selectCamera(imagePathTag);
                break;
            case R.id.iv_idNum_Positive://负责人身份证正面
                imagePathTag = "9";
                selectCamera(imagePathTag);
                break;
            case R.id.ll_area_name:
                choiceLoctionDialog();      //地理区域
                break;
            case R.id.ll_jurisArea:
                choiceManageAreaDialog();  //管辖区域
                break;
            case R.id.ll_firstAgent:  //第一代理人
                mSpinerPopWindow = new SpinerPopWindow(this);
                CHOICE_TAG = "7";
                mSpinerPopWindow.refreshData(fristAgentList, 0);
                mSpinerPopWindow.setItemListener(this);
                showSpinWindow(llFirstAgent);
                break;
            case R.id.ll_agentaType:  //代理人类型
                mSpinerPopWindow = new SpinerPopWindow(this);
                CHOICE_TAG = "8";
                mSpinerPopWindow.refreshData(agentTypeList, 0);
                mSpinerPopWindow.setItemListener(this);
                showSpinWindow(llAgentaType);
                break;
            case R.id.tv_submit:
                checkSubmit();
                break;
        }
    }


    //运行在post运行所在的线程
    public void onEvent(EventBean event) {   //别的地方发送数据到这里  这里直接接受
        tvMobile.setText(event.phone);
//        if (!("直营".equals(agentType) && "个体户".equals(firstAgent))) {
            tvPersonChargePhone.setText(event.phone);
//        } else {
//            tvCorpMobile.setText(event.phone);
//        }

        user.setMobile(event.phone);
        tvMobileS = event.phone; //手机
    }

    private void checkSubmit() {
        tvNameS = tvName.getText().toString().trim(); //姓名
        tvMobileS = tvMobile.getText().toString().trim();  //手机
        tvRemarksS = etRemarks.getText().toString().trim();   //备注
        tvCompanyNameS = tvCompanyName.getText().toString().trim();  //公司
        tvOrgNameS = tvOrgName.getText().toString().trim(); //机构名称
        tvCorpNameS = tvCorpName.getText().toString().trim(); //法人姓名
        tvCorpMobileS = tvCorpMobile.getText().toString().trim(); //  //法人手机
        tvCorpIdNumS = tvCorpIdNum.getText().toString().trim();  //         法人身份证
        tvPersonChargeNameS = tvName.getText().toString().trim();  //     负责人姓名
        tvPersonChargePhoneS = tvPersonChargePhone.getText().toString().trim(); //       负责人手机
        tvPersonChargeIdNumS = tvPersonChargeIdNum.getText().toString().trim();//           负责人身份证号
        tvAddressS = tvAddress.getText().toString().trim(); //      地址
        tvMasterS = tvMaster.getText().toString().trim();//         联系人
        tvContactPhoneS = tvContactPhone.getText().toString().trim();//  联系人电话

        if (tvNameS.isEmpty()) {
            Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvMobileS.isEmpty()) {
            Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvOrgNameS.isEmpty()) {
            Toast.makeText(this, "机构名称不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvCorpNameS.isEmpty()) {
            Toast.makeText(this, "法人姓名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvCorpMobileS.isEmpty()) {
            Toast.makeText(this, "法人手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvCorpIdNumS.isEmpty()) {
            Toast.makeText(this, "法人身份证不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvAddressS.isEmpty()) {
            Toast.makeText(this, "地址不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvMasterS.isEmpty()) {
            Toast.makeText(this, "联系人不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvContactPhoneS.isEmpty()) {
            Toast.makeText(this, "联系人电话不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (RegexUtils.isMobileNO(tvMobileS) == false) {
            Toast.makeText(UpdatePersonalinformation.this, "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
            return;
        } else if (RegexUtils.isMobileNO(tvCorpMobileS) == false) {
            Toast.makeText(UpdatePersonalinformation.this, "请输入正确的法人手机号码!", Toast.LENGTH_SHORT).show();
            return;
        } else if (RegexUtils.isMobileNO(tvContactPhoneS) == false) {
            Toast.makeText(this, "请输入正确的联系人电话！", Toast.LENGTH_SHORT).show();
            return;
        } else if (RegexUtils.checkIdCard(tvCorpIdNumS) == false) {
            Toast.makeText(this, "请输入正确的法人身份证！", Toast.LENGTH_SHORT).show();
            return;
        }


//        if (!("直营".equals(agentType) && "个体户".equals(firstAgent))) {
            if (tvPersonChargeNameS.isEmpty()) {
                Toast.makeText(this, "负责人姓名不能为空！", Toast.LENGTH_SHORT).show();
                return;
            } else if (tvPersonChargePhoneS.isEmpty()) {
                Toast.makeText(this, "负责人手机不能为空！", Toast.LENGTH_SHORT).show();
                return;
            } else if (tvPersonChargeIdNumS.isEmpty()) {
                Toast.makeText(this, "负责人身份证不能为空！", Toast.LENGTH_SHORT).show();
                return;
            } else if (RegexUtils.checkIdCard(tvPersonChargeIdNumS) == false) {
                Toast.makeText(this, "请输入正确的负责人身份证！", Toast.LENGTH_SHORT).show();
                return;
            } else if (RegexUtils.isMobileNO(tvPersonChargePhoneS) == false) {
                Toast.makeText(UpdatePersonalinformation.this, "请输入正确的负责人手机号码!", Toast.LENGTH_SHORT).show();
                return;
            }
//        }

        if ("公司".equals(firstAgent)) {   //如果第一代理人为公司
            if (tvCompanyNameS.isEmpty()) {
                Toast.makeText(this, "公司不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
        }



        /*if(ivIdNumPositiveBase ==null || ivIdNumPositiveBase.isEmpty()) {//负责人身份证正面
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
          return;
        }else  if(ivIdNumOtherSideBase==null || ivIdNumOtherSideBase.isEmpty()){ //负责人身份证反面
        }
        else if(ivPaymentAuthorizationBase==null ||ivPaymentAuthorizationBase.isEmpty()) { //票款汇缴授权书
        }
        else  if(ivPersonRegistrationCertificateBase==null ||ivPersonRegistrationCertificateBase.isEmpty()) {  //营业执照/法人登记证书

        }else  if(ivLegalPersonIdNumPositiveBase==null || ivLegalPersonIdNumPositiveBase.isEmpty()) { //法人代表身份证正面
        }else if(ivLegalPersonOtherSideBase==null || ivLegalPersonOtherSideBase.isEmpty()) {  //法人代表身份证反面
        }else if(ivStationContractHomePageBase==null || ivStationContractHomePageBase.isEmpty()) {  //车站合同首页
        }else if(ivTheStationContractBase==null || ivTheStationContractBase.isEmpty()) {  //车站合同尾页
        }else if(ivStationBarBase==null || ivStationBarBase.isEmpty()) { //车站压金条
        }else if(ivBusinessLicenseBase==null || ivBusinessLicenseBase.isEmpty()){   //车站押金年检证书*/

        submit(); //开始修改个人信息
    }

    private void submit() {
        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        String url = Define.URL + "user/modifyUser";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", user.getId());
            jsonObject.put("name", tvNameS);
            jsonObject.put("remark", tvRemarksS);
            jsonObject.put("jurAreaCode", threeLevelCode);
            jsonObject.put("areaCode", cityCode);
            jsonObject.put("officeName", tvOrgNameS);
            jsonObject.put("agentType", getAgentTypeCode(agentType));  //代理人类型
            jsonObject.put("agent", getAgentCode(firstAgent)); //代理人
            if ("公司".equals(firstAgent)) {   //如果第一代理人为公司
                jsonObject.put("agentCompanyName", tvCompanyNameS); //公司
            }
            jsonObject.put("corpName", tvCorpNameS); //法人姓名
            jsonObject.put("corpMobile", tvCorpMobileS); //法人手机
            jsonObject.put("corpIdNum", tvCorpIdNumS); //法人身份证
//            if (!("直营".equals(agentType) && "个体户".equals(firstAgent))) {
                jsonObject.put("operatorName", tvPersonChargeNameS);//负责人姓名
                jsonObject.put("operatorIdNum", tvPersonChargeIdNumS);//负责人手身份证号
                jsonObject.put("operatorMobile", tvPersonChargePhoneS);// 负责人手机号
//            }
            jsonObject.put("address", tvAddressS);//地址
            jsonObject.put("master", tvMasterS);//联系人
            jsonObject.put("phone", tvContactPhoneS); //联系人电话
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(UpdatePersonalinformation.this,R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Log.i("a", json.toString());
                imageConmit();  //图片上传
            }

            @Override
            protected void PdDismiss() {
                cpd.dismiss();
            }
        }, "modifyUser", true);
        volleyNetCommon.addQueue(jsonObjectRequest);


    }

    private void imageConmit() {
        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        String url = Define.URL + "user/supImg";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", user.getId());
            jsonObject.put("corpLicenceImg", ivPersonRegistrationCertificateBase);//营业执照
            jsonObject.put("corpCardForntImg", ivLegalPersonIdNumPositiveBase);//法人身份证正面路径
            jsonObject.put("corpCardRearImg", ivLegalPersonOtherSideBase);//法人身份证反面路径
            jsonObject.put("staContIndexImg", ivStationContractHomePageBase);//车站合同首页路径
            jsonObject.put("staContLastImg", ivTheStationContractBase);//车站合同末页路径
            jsonObject.put("staDepositImg", ivStationBarBase);//车站押金条路径
            jsonObject.put("staDepInspImg", ivBusinessLicenseBase);//车站押金年检证书路径
//            if (!("直营".equals(agentType) && "个体户".equals(firstAgent))) {
                jsonObject.put("operatorCardforntImg", ivIdNumPositiveBase);//负责人身份证正面路径
                jsonObject.put("operatorCardrearImg", ivIdNumOtherSideBase);//负责人身份证反面路径
                jsonObject.put("fareAuthorizationImg", ivPaymentAuthorizationBase);//票款汇缴授权书
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(UpdatePersonalinformation.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Log.i("a", json.toString());
                Toast.makeText(UpdatePersonalinformation.this, "个人信息修改成功！", Toast.LENGTH_SHORT).show();
//                finish();

                try {
                    UserInfo userInfo = new UserInfo();
                    userInfo.getUserInfo(UpdatePersonalinformation.this, cpd, PersonalInformationA.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void PdDismiss() {
                cpd.dismiss();
            }
        }, "supImg", true);
        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    /**
     * 选择相片
     *
     * @param s
     */
    private void selectCamera(String s) {
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                Button btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);  //相机拍照
                Button btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo); //相册选择
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);  //取消
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cameraClick();
                        myPopWindow.dissmiss();
                    }
                });
                btn_pick_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        photoClick();
                        myPopWindow.dissmiss();
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.alert_dialog;
            }
        }.poPwindow(this,true).showAtLocation(ivBack, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    File file;

    /**
     * 拍照
     */
    public void cameraClick() {

        try {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/camera/certificate";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            // 在SDcard的目录下创建图片文,以当前时间为其命名
            file = new File(sdCardDir, System.currentTimeMillis() + ".jpg");

            //拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, CAMERA_REQUESTCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 从相册中选择
     */
    public void photoClick() {
        //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
        //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 2);
    }


    private String getAgentType(String firstAgent2) {
        String agentType = "";
        switch (firstAgent2) {
            case "0":
                agentType = "直营";
                break;
            case "1":
                agentType = "承包";
                break;
        }
        return agentType;


    }


    private String getAgent(String agentType) {
        String agent = "";
        switch (agentType) {
            case "0":
                agent = "个体户";
                break;
            case "1":
                agent = "铁青";
                break;
            case "2":
                agent = "邮政";
                break;
            case "3":
                agent = "公司";
                break;
        }
        return agent;
    }

    private String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

    private void showSpinWindow(View v) {
        Log.e("", "showSpinWindow");
        mSpinerPopWindow.setWidth(v.getWidth());
        mSpinerPopWindow.showAsDropDown(v);
    }

    // 选择用户类型
    private void setIdentity(int pos) {
       /* if (pos >= 0 && pos <= identityTypeList.size()) {
            String value = identityTypeList.get(pos);

            tv_identity.setText(value);

            identity = tv_identity.getText().toString();
            // if ("管理员".equals(identity)) {
            // identity = Define.SYSTEM_MANAG_USER;
            // et_windowNumber.setVisibility(View.GONE);
            // et_userName.setVisibility(View.VISIBLE);
            // }else
            if ("火车站".equals(identity)) {
                identity = Define.TRAINSTATION;
                et_windowNumber.setVisibility(View.GONE);
                et_userName.setVisibility(View.VISIBLE);
            } else if ("代售点".equals(identity)) {
                identity = Define.OUTLETS;
                et_windowNumber.setVisibility(View.VISIBLE);
                et_userName.setVisibility(View.GONE);
            }
        }*/
    }

    // 选择第一代理人
    private void setFristAgent(int pos) {
        if (pos >= 0 && pos <= fristAgentList.size()) {
            String value = fristAgentList.get(pos);

            tvFirstAgent.setText(value);

            firstAgent = tvFirstAgent.getText().toString();

            if ("公司".equals(firstAgent)) {
                // identity = Define.TRAINSTATION;
                layoutCompanyName.setVisibility(View.VISIBLE);
            } else {
                layoutCompanyName.setVisibility(View.GONE);
            }
            setShowHideIdNumFuZeRen(agentType, firstAgent);  //设置显示
        }
    }

    private String getAgentTypeCode(String firstAgent2) {
        if (agentType.equals("直营")) {
            return "0";
        } else if (agentType.equals("承包")) {
            return "1";
        }
        return "";
    }


    private String getAgentCode(String agentType) {
        if (firstAgent.equals("个体户"))
            return "0";
        else if (firstAgent.equals("铁青"))
            return "1";
        else if (firstAgent.equals("邮政"))
            return "2";
        else if (firstAgent.equals("公司"))
            return "3";

        return "";
    }

    // 选择第一代理人类型
    private void setAgentType(int pos) {
        if (pos >= 0 && pos <= agentTypeList.size()) {
            String value = agentTypeList.get(pos);
            tvAgentaType.setText(value);
            agentType = tvAgentaType.getText().toString();

            setShowHideIdNumFuZeRen(agentType, firstAgent);  //设置显示
        }
    }

    private void setShowHideIdNumFuZeRen(final String agentType, final String firstAgent) {
       /* if ("直营".equals(agentType) && "个体户".equals(firstAgent)) {
            layoutOutsFuZeRenArea.setVisibility(View.GONE); //负责人身份证信息容器
            llIdNumPositive.setVisibility(View.INVISIBLE);  //负责人身份证正面
            llFuZeRenCertificates.setVisibility(View.GONE); //负责人身份证反面和票款汇缴授权书
            tvCorpName.setText(tvName.getText().toString().trim());
            tvCorpName.setEnabled(false); //法人禁用不可更改
            tvCorpMobile.setEnabled(false); //法人手机号禁用 不可以更改
            tvName.addTextChangedListener(tvCorpNameWatcher); //姓名改变法人姓名也改变
            tvCorpMobile.setText(tvMobile.getText().toString().trim()); //法人手机号改变
            tvName.removeTextChangedListener(tvPersonChargeNameWatcher);

            tvPersonChargeName.setEnabled(true);
            tvPersonChargePhone.setEnabled(true);
        } else {*/
            layoutOutsFuZeRenArea.setVisibility(View.VISIBLE);
            llIdNumPositive.setVisibility(View.VISIBLE);
            llFuZeRenCertificates.setVisibility(View.VISIBLE); //负责人身份证反面和票款汇缴授权书
//          layoutPaymentAuthorization.setVisibility(View.VISIBLE);
            tvCorpName.setEnabled(true);
            tvCorpMobile.setEnabled(true);
//            tvName.removeTextChangedListener(tvCorpNameWatcher);  //解除监听
            tvPersonChargePhone.setText(tvMobile.getText().toString()); //负责人手机号改变
            tvName.addTextChangedListener(tvPersonChargeNameWatcher); //姓名改变 负责人姓名也改变
//            tvPersonChargeName.setText(tvName.getText().toString().trim());
            tvPersonChargeName.setEnabled(false);
            tvPersonChargePhone.setEnabled(false);
//        }
    }

   /* MyTextWatcher tvCorpNameWatcher = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            if ("直营".equals(agentType) && "个体户".equals(firstAgent)) {
                if (tvCorpName != null) {
                    tvCorpName.setText(editable.toString());
                }

            }
        }
    };*/


    MyTextWatcher tvPersonChargeNameWatcher = new MyTextWatcher() {    //负责人姓名
        @Override
        public void afterTextChanged(Editable editable) {
//            if (!("直营".equals(agentType) && "个体户".equals(firstAgent))) {
                if (tvPersonChargeName != null) {
                    tvPersonChargeName.setText(editable.toString());
                }
//            }
        }
    };


    private void choiceLoctionDialog() {
        // String url = Define.URL+"acct/getCode";
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.selectlocation_dialog, null);

        rl_choiceProvinces = (RelativeLayout) view.findViewById(R.id.rl_choiceProvinces);
        rl_choiceCitys = (RelativeLayout) view.findViewById(R.id.rl_choiceCitys);
        tv_provinces = (TextView) view.findViewById(R.id.tv_provinces);
        tv_citys = (TextView) view.findViewById(R.id.tv_citys);
        bt_complete = (Button) view.findViewById(R.id.bt_complete);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        final AlertDialog dialog = builder.create();// 获取dialog

        dialog.show();

        rl_choiceProvinces.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSpinerPopWindow = new SpinerPopWindow(UpdatePersonalinformation.this);
                CHOICE_TAG = "2";
                mSpinerPopWindow.refreshData(provinceNameList, 0);
                mSpinerPopWindow.setItemListener(UpdatePersonalinformation.this);
                showSpinWindow(rl_choiceProvinces);

            }
        });

        rl_choiceCitys.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (provinceCode.isEmpty() || provinceCode == null) {
                    Toast.makeText(UpdatePersonalinformation.this, "请先选择省份！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSpinerPopWindow = new SpinerPopWindow(UpdatePersonalinformation.this);
                CHOICE_TAG = "3";
                mSpinerPopWindow.refreshData(cityNameList, 0);
                mSpinerPopWindow.setItemListener(UpdatePersonalinformation.this);
                showSpinWindow(rl_choiceCitys);

            }
        });

        bt_complete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                provinces = tv_provinces.getText().toString();
                citys = tv_citys.getText().toString();
                if ("选择省".equals(provinces)) {
                    Toast.makeText(UpdatePersonalinformation.this, "请选择省！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("选择市".equals(citys)) {
                    Toast.makeText(UpdatePersonalinformation.this, "请选择市！", Toast.LENGTH_SHORT).show();
                    return;
                }

                location = provinces + "/" + citys;
                tvAreaName.setText(location);
                dialog.dismiss();
            }
        });
    }

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
                mSpinerPopWindow = new SpinerPopWindow(UpdatePersonalinformation.this);
                CHOICE_TAG = "4";
                mSpinerPopWindow.refreshData(oneLevelNameList, 0);
                mSpinerPopWindow.setItemListener(UpdatePersonalinformation.this);
                showSpinWindow(rl_choiceOneLevel);

            }
        });

        rl_choiceTwoLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneLevelCode == null || oneLevelCode.isEmpty()) {
                    Toast.makeText(UpdatePersonalinformation.this, "请先选择第一级！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSpinerPopWindow = new SpinerPopWindow(UpdatePersonalinformation.this);
                CHOICE_TAG = "5";
                mSpinerPopWindow.refreshData(twoLevelNameList, 0);
                mSpinerPopWindow.setItemListener(UpdatePersonalinformation.this);
                showSpinWindow(rl_choiceTwoLevel);

            }
        });

        rl_choiceThirdLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (twoLevelCode == null || twoLevelCode.isEmpty()) {
                    Toast.makeText(UpdatePersonalinformation.this, "请先选择第二级！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSpinerPopWindow = new SpinerPopWindow(UpdatePersonalinformation.this);
                CHOICE_TAG = "6";
                mSpinerPopWindow.refreshData(threeLevelNameList, 0);
                mSpinerPopWindow.setItemListener(UpdatePersonalinformation.this);
                showSpinWindow(rl_choiceThirdLevel);

            }
        });
        bt_complete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                oneLevel = tv_oneLevel.getText().toString();
                twoLevel = tv_twoLevel.getText().toString();
                threeLevel = tv_thirdLevel.getText().toString();
                if ("选择第一级".equals(oneLevel)) {
                    Toast.makeText(UpdatePersonalinformation.this, "请选择第一级！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("选择第二级".equals(twoLevel)) {
                    Toast.makeText(UpdatePersonalinformation.this, "请选择第二级！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("选择第三级".equals(threeLevel)) {
                    Toast.makeText(UpdatePersonalinformation.this, "请选择第三级！", Toast.LENGTH_SHORT).show();
                    return;
                }

                manageArea = oneLevel + "/" + twoLevel + "/" + threeLevel;
                tvJurisArea.setText(manageArea);
                dialog.dismiss();
            }
        });
    }

    // 选择1级区域管理列表
    private void setManageArea(int pos) {
        if (pos >= 0 && pos <= oneLevelNameList.size()) {
            String value = oneLevelNameList.get(pos);

            tv_oneLevel.setText(value);
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

    // 选择省份
    private void setProvince(int pos) {
        if (pos >= 0 && pos <= provinceNameList.size()) {
            String value = provinceNameList.get(pos);

            tv_provinces.setText(value);
        }
    }

    // 获取省份code
    private String getProvinceCode(int pos) {
        if (pos >= 0 && pos <= provinceCodeList.size()) {
            provinceCode = provinceCodeList.get(pos);

        }
        return provinceCode;
    }

    // 选择城市
    private void setCitys(int pos) {
        if (pos >= 0 && pos <= cityNameList.size()) {
            String value = cityNameList.get(pos);

            tv_citys.setText(value);
        }
    }

    // 获取城市code
    private String getCityCode(int pos) {
        if (pos >= 0 && pos <= cityNameList.size()) {
            cityCode = cityCodeList.get(pos);

        }
        return cityCode;
    }

    @Override
    public void onItemClick(int pos) {
        if ("1".equals(CHOICE_TAG)) {
            setIdentity(pos);
        } else if ("2".equals(CHOICE_TAG)) {
            setProvince(pos);
            provinceCode = getProvinceCode(pos);
            try {
                getAllCitys();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("3".equals(CHOICE_TAG)) {
            setCitys(pos);
            cityCode = getCityCode(pos);
        } else if ("4".equals(CHOICE_TAG)) {
            setManageArea(pos);
            oneLevelCode = getOneLevelCode(pos);
            try {
                getAllManageAreaTwo();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("5".equals(CHOICE_TAG)) {
            setManageArea2(pos);
            twoLevelCode = getTwoLevelCode(pos);
            try {
                getAllManageAreaThree();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("6".equals(CHOICE_TAG)) {
            setManageArea3(pos);
            threeLevelCode = getThreeLevelCode(pos);
        } else if ("7".equals(CHOICE_TAG)) {
            setFristAgent(pos);
        } else if ("8".equals(CHOICE_TAG)) {
            setAgentType(pos);
        }

    }

    /**
     * 获取所有省份
     *
     * @throws Exception
     */
    private void getAllProvince() throws Exception {
        cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Define.URL + "user/getProvinceList";
        final List<User> provinceList = new ArrayList<User>();
        SessionJsonObjectRequest provinceListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
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

                                    JSONObject provinceObj = (JSONObject) jsonArray.get(i);
                                    String provinceCode = provinceObj.optString("code");
                                    String provinceName = provinceObj.getString("name");

                                    user.setProvinceCode(provinceCode);
                                    user.setProvinceName(provinceName);

                                    provinceList.add(user);
                                }

                                Runnable proRunnable = new Runnable() {

                                    @Override
                                    public void run() {

                                        Message msg = new Message();
                                        msg.what = PROVINCE_LIST;
                                        msg.obj = provinceList;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                Thread proThread = new Thread(proRunnable);
                                proThread.start();

                                cpd.dismiss();
                            } else {
                                cpd.dismiss();
                                Toast.makeText(UpdatePersonalinformation.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            cpd.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(UpdatePersonalinformation.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        provinceListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        provinceListRequest.setTag("provinceListRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(provinceListRequest);

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
        // String url = Define.URL + "user/getProvinceList";
        String url = Define.URL + "user/getGroupList";
        final List<User> manageAreaList = new ArrayList<User>();
        SessionJsonObjectRequest manageAreaListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (null == response) {
                            Toast.makeText(UpdatePersonalinformation.this, "服务器状态异常，请稍后再试", Toast.LENGTH_SHORT);
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

                            } else {
                                // pd.dismiss();
                                Toast.makeText(UpdatePersonalinformation.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // pd.dismiss();
                Toast.makeText(UpdatePersonalinformation.this,R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        manageAreaListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        manageAreaListRequest.setTag("manageAreaListRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(manageAreaListRequest);

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
            Toast.makeText(UpdatePersonalinformation.this, "请先选择第一级菜单！", Toast.LENGTH_SHORT).show();
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

                                Thread twoThread = new Thread(cityRunnable);
                                twoThread.start();
                                cpd.dismiss();

                            } else {
                                cpd.dismiss();
                                Toast.makeText(UpdatePersonalinformation.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(UpdatePersonalinformation.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        twoLeveAreaRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        twoLeveAreaRequest.setTag("twoLeveAreaRequest");
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
            Toast.makeText(UpdatePersonalinformation.this, "请先选择第二级菜单！", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(UpdatePersonalinformation.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(UpdatePersonalinformation.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        threeLeveAreaRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        threeLeveAreaRequest.setTag("threeLeveAreaRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(threeLeveAreaRequest);

    }

    /**
     * 根据选择的省份code获取对应的城市列表
     *
     * @throws Exception
     */
    private void getAllCitys() throws Exception {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", provinceCode);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (provinceCode.isEmpty() || provinceCode == null) {
            Toast.makeText(UpdatePersonalinformation.this, "请先选择省份！", Toast.LENGTH_SHORT).show();
            return;
        }
        cpd.show();
        String url = Define.URL + "user/getCityListByProvice";
        final List<User> cityList = new ArrayList<User>();
        SessionJsonObjectRequest cityListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
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

                                    JSONObject cityObj = (JSONObject) jsonArray.get(i);
                                    String cityCode = cityObj.optString("code");
                                    String cityName = cityObj.getString("name");

                                    user.setCityCode(cityCode);
                                    user.setCityName(cityName);

                                    cityList.add(user);
                                }

                                Runnable cityRunnable = new Runnable() {

                                    @Override
                                    public void run() {

                                        Message msg = new Message();
                                        msg.what = CITY_LIST;
                                        msg.obj = cityList;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                Thread cityThread = new Thread(cityRunnable);
                                cityThread.start();

                                cpd.dismiss();
                            } else {
                                cpd.dismiss();
                                Toast.makeText(UpdatePersonalinformation.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(UpdatePersonalinformation.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        cityListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        cityListRequest.setTag("cityListRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(cityListRequest);

    }


    /**
     * 删除字符串空格
     *
     * @param str
     * @return
     */
    public String removeAllSpace(String str) {
        String tmpstr = str.replace(" ", "");
        return tmpstr;
    }

    public String absolutePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUESTCODE:  //相机拍照
                if (resultCode == RESULT_OK) {
                    if (file != null) {
                        absolutePath = file.getAbsolutePath();
                        setImagPath(imagePathTag, absolutePath);
                    }
                }
                break;
            case PHOTO_REQUESTCODE:   //相册
                if (data != null && resultCode == RESULT_OK) {
                    cpd.show();
                    final Uri mImageCaptureUri = data.getData();
                    //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {
                        absolutePath = TransformationImageUtils.getRealFilePath(this, mImageCaptureUri);
                        setImagPath(imagePathTag, absolutePath);
                        cpd.dismiss();
                    }
                }
                break;

        }

    }

    private void setImagPath(String imagePathTag, String absolutePath) {
        switch (imagePathTag) {
            case "0":
                ivIdNumOtherSidePath = absolutePath;//负责人身份证反面
                imageCompres(ivIdNumOtherSidePath, ivIdNumOtherSide, imagePathTag);
                break;
            case "1":
                ivPaymentAuthorizationPath = absolutePath; //票款汇缴授权书
                imageCompres(ivPaymentAuthorizationPath, ivPaymentAuthorization, imagePathTag);
                break;
            case "2":
                ivPersonRegistrationCertificatePath = absolutePath;  //营业执照/法人登记证书
                imageCompres(ivPersonRegistrationCertificatePath, ivPersonRegistrationCertificate, imagePathTag);
                break;
            case "3":
                ivLegalPersonIdNumPositivePath = absolutePath; //法人代表身份证正面
                imageCompres(ivLegalPersonIdNumPositivePath, ivLegalPersonIdNumPositive, imagePathTag);
                break;
            case "4":
                ivLegalPersonOtherSidePath = absolutePath;  //法人代表身份证反面
                imageCompres(ivLegalPersonOtherSidePath, ivLegalPersonOtherSide, imagePathTag);
                break;
            case "5":
                ivStationContractHomePagePath = absolutePath;  //车站合同首页
                imageCompres(ivStationContractHomePagePath, ivStationContractHomePage, imagePathTag);
                break;
            case "6":
                ivTheStationContractPath = absolutePath;  //车站合同尾页
                imageCompres(ivTheStationContractPath, ivTheStationContract, imagePathTag);
                break;
            case "7":
                ivStationBarPath = absolutePath; //车站压金条
                imageCompres(ivStationBarPath, ivStationBar, imagePathTag);
                break;
            case "8":
                ivBusinessLicensePath = absolutePath;   //营业执照/法人登记证书
                imageCompres(ivBusinessLicensePath, ivBusinessLicense, imagePathTag);
                break;
            case "9":
                ivIdNumPositivePath = absolutePath;//负责人身份证正面
                imageCompres(ivIdNumPositivePath, ivIdNumPositive, imagePathTag);
                break;
        }
    }

    Bitmap bitmap = null;

    public  void imageCompres(final String absolutePath, final ImageView imageView, final String imagePathTag) {

        File file = new File(absolutePath);
        if (file.exists()) {
            file.length();
        }

        Luban.with(this)
                .load(new File(absolutePath))                     //传人要压缩的图片
                .ignoreBy(300)
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }
                    @Override
                    public void onSuccess(final File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        switch (imagePathTag) {
                            case "0":
                                     ivIdNumOtherSideBase =Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivIdNumOtherSide);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "1":
                                ivPaymentAuthorizationBase =Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivPaymentAuthorization);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "2":
                                ivPersonRegistrationCertificateBase =Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivPersonRegistrationCertificate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "3":
                                ivLegalPersonIdNumPositiveBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivLegalPersonIdNumPositive);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "4":
                                ivLegalPersonOtherSideBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivLegalPersonOtherSide);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "5":
                                ivStationContractHomePageBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivStationContractHomePage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "6":
                                ivTheStationContractBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivTheStationContract);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "7":
                                ivStationBarBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivStationBar);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "8":
                                ivBusinessLicenseBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivBusinessLicense);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "9":
                                ivIdNumPositiveBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(UpdatePersonalinformation.this).load(absolutePath).into(ivIdNumPositive);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩




    /*    Glide.with(UpdatePersonalinformation.this).load(absolutePath).asBitmap().override(480, 640).listener(new RequestListener<String, Bitmap>() {
             @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(final Bitmap bitmap, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                bitmap.getByteCount();
                ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
                    @Override
                    public void run() {
                        Compres compres = new Compres();
                        try {
                            // final Bitmap bitmap = compres.revitionImageSize(absolutePath, 500);


                          //  final Bitmap bitmap = ImageCompres.getCompressImage(absolutePath);

                            switch (imagePathTag) {
                                case "0":
                                    ivIdNumOtherSideBase = Base64ConvertBitmap.bitmapToBase64(bitmap);
                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivIdNumOtherSide.setImageBitmap(bitmap);
                                        }
                                    });

                                    break;
                                case "1":
                                    ivPaymentAuthorizationBase = Base64ConvertBitmap.bitmapToBase64(bitmap);
                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivPaymentAuthorization.setImageBitmap(bitmap);
                                        }
                                    });

                                    break;
                                case "2":
                                    ivPersonRegistrationCertificateBase = Base64ConvertBitmap.bitmapToBase64(bitmap);

                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivPersonRegistrationCertificate.setImageBitmap(bitmap);
                                        }
                                    });
                                    break;
                                case "3":
                                    ivLegalPersonIdNumPositiveBase = Base64ConvertBitmap.bitmapToBase64(bitmap);

                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivLegalPersonIdNumPositive.setImageBitmap(bitmap);
                                        }
                                    });
                                    break;
                                case "4":
                                    ivLegalPersonOtherSideBase = Base64ConvertBitmap.bitmapToBase64(bitmap);

                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivLegalPersonOtherSide.setImageBitmap(bitmap);
                                        }
                                    });
                                    break;
                                case "5":
                                    ivStationContractHomePageBase = Base64ConvertBitmap.bitmapToBase64(bitmap);

                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivStationContractHomePage.setImageBitmap(bitmap);
                                        }
                                    });
                                    break;
                                case "6":
                                    ivTheStationContractBase = Base64ConvertBitmap.bitmapToBase64(bitmap);

                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivTheStationContract.setImageBitmap(bitmap);
                                        }
                                    });
                                    break;
                                case "7":
                                    ivStationBarBase = Base64ConvertBitmap.bitmapToBase64(bitmap);

                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivStationBar.setImageBitmap(bitmap);
                                        }
                                    });
                                    break;
                                case "8":
                                    ivBusinessLicenseBase = Base64ConvertBitmap.bitmapToBase64(bitmap);

                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivBusinessLicense.setImageBitmap(bitmap);
                                        }
                                    });
                                    break;
                                case "9":
                                    ivIdNumPositiveBase = Base64ConvertBitmap.bitmapToBase64(bitmap);

                                    ThreadCommonUtils.runonuiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivIdNumPositive.setImageBitmap(bitmap);
                                        }
                                    });
                                    break;
                            }
                            Looper.prepare();
                            Toast.makeText(UpdatePersonalinformation.this, ""+bitmap.getByteCount(), Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                return false;
            }
        }).into(imageView);*/


    }


    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("provinceListRequest");
        MyApplication.getHttpQueues().cancelAll("cityListRequest");
        MyApplication.getHttpQueues().cancelAll("veriUserNameRequest");
        MyApplication.getHttpQueues().cancelAll("manageAreaListRequest");
        MyApplication.getHttpQueues().cancelAll("twoLeveAreaRequest");
        MyApplication.getHttpQueues().cancelAll("threeLeveAreaRequest");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //注销监听
        if (bitmap != null) {
            bitmap.recycle(); //释放资源
            bitmap = null;
        }
        if (bmOperatorCardfornt != null){
            bmOperatorCardfornt.recycle();
            bmOperatorCardfornt = null;
        }
        if (bmOperatorCardrear != null){
            bmOperatorCardrear.recycle();
            bmOperatorCardrear = null;
        }
        if (bmFareAuthorization != null){
            bmFareAuthorization.recycle();
            bmFareAuthorization = null;
        }
        if(bmCorpLicence != null){
            bmCorpLicence.recycle();
            bmCorpLicence = null;
        }
        if(bmCorpCardFornt != null){
            bmCorpCardFornt.recycle();
            bmCorpCardFornt = null;
        }
        if(bmCorpCardRear != null){
            bmCorpCardRear.recycle();
            bmCorpCardRear = null;
        }
        if(bmStaContIndex != null){
            bmStaContIndex.recycle();
            bmStaContIndex = null;
        }
        if(bmStaContLast != null){
            bmStaContLast.recycle();
            bmStaContLast = null;
        }
        Glide.get(this).clearMemory();  //清除内存
        ivIdNumPositiveBase=null;
        ivIdNumOtherSideBase =null;
        ivPaymentAuthorizationBase =null;
        ivPersonRegistrationCertificateBase =null;
        ivLegalPersonIdNumPositiveBase =null;
        ivLegalPersonOtherSideBase =null;
        ivStationContractHomePageBase =null;
        ivTheStationContractBase =null;
        ivStationBarBase =null;
        ivBusinessLicenseBase = null;
        System.gc();  //通知垃圾回收站回收垃圾
    }
}
