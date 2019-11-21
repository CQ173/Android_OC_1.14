package com.huoniao.oc.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.huoniao.oc.MainActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.ConfigureBean;
import com.huoniao.oc.bean.CreditScoreEvent;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.HomepageCommon;
import com.huoniao.oc.common.UserInfo;
import com.huoniao.oc.outlets.WalletA;
import com.huoniao.oc.user.AccountLogOffAuditingA;
import com.huoniao.oc.user.AccountLogOffNeedKnow;
import com.huoniao.oc.user.HelpCenterA;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.OjiCreditA;
import com.huoniao.oc.user.PasswordManagement;
import com.huoniao.oc.user.PerfectInformationA;
import com.huoniao.oc.user.PersonalInformationA;
import com.huoniao.oc.user.RegisterSuccessA;
import com.huoniao.oc.user.SettingA;
import com.huoniao.oc.user.UpdateHeader;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SPUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.VersonCodeUtils;
import com.huoniao.oc.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.huoniao.oc.MainActivity.creditScore;
import static com.huoniao.oc.MyApplication.serviceCode;
import static com.huoniao.oc.MyApplication.serviceVersionName;
import static com.huoniao.oc.MyApplication.treeIdList;
import static com.huoniao.oc.R.id.ll_perosn;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;
import static com.umeng.analytics.MobclickAgent.onProfileSignOff;


public class MineF extends BaseFragment implements View.OnClickListener {


    private LinearLayout ll_person;
    private LinearLayout ll_help;
    private LinearLayout ll_setting;
    private ImageView iv_logo;
    private TextView tv_outletsName;
    private TextView tv_userName;
    private LinearLayout ll_my_wallet;
    private LinearLayout ll_password_management;
    private LinearLayout ll_exit_login;
    private TextView tv_circle_red;
    private String absolutePath;
    private User user;
    private final String  headerImgTag  ="headerImg";//头像图片标识
    private String photoSrc;
    private String photoimgFlag;
    private static final int HEADERIMAG_REQUESTCODE = 100; //头像请求码

    private LinearLayout ll_logoutAccount;//代售点账户注销
    private LinearLayout ll_creditScore;//代售点信用积分
    private TextView tv_creditScore;

    private Intent intent;
    private String loginName;
//    private String creditScore;
    private String creditScoreStr;

    private List<ConfigureBean.ListEntity> entityList;//配置集合
    private String creditScoreSwitch;//积分开放开关


    public MineF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);  //注册粘性事件  可以保证接收消息
       View view = inflater.inflate(R.layout.new_outlets_view_mine,null);

			/*我的  界面*/
        ll_person = (LinearLayout) view.findViewById(R.id.ll_perosn);  //我的    新界面的个人中心
        ll_help = (LinearLayout) view.findViewById(R.id.ll_help);  // 我的    新界面帮助中心
        ll_setting = (LinearLayout)view.findViewById(R.id.ll_setting); // 我的  新界面 设置
        iv_logo = (ImageView)view.findViewById(R.id.iv_logo);   //我的   logo
        tv_outletsName = (TextView) view.findViewById(R.id.tv_outletsName);  //我的  代售点
        tv_userName = (TextView) view.findViewById(R.id.tv_userName); //我的   用户名
        ll_my_wallet = (LinearLayout) view.findViewById(R.id.ll_my_wallet);  //我的 我的钱包
        ll_password_management = (LinearLayout) view.findViewById(R.id.ll_password_management);  //我的模块  密码管理
        ll_exit_login = (LinearLayout) view.findViewById(R.id.ll_exit_login);  //退出登录
        tv_circle_red = (TextView)view.findViewById(R.id.tv_circle_red);  //小红点 提示是否有最新版本更新
        ll_logoutAccount = (LinearLayout) view.findViewById(R.id.ll_logoutAccount);  //代售点账户注销
        ll_creditScore = (LinearLayout) view.findViewById(R.id.ll_creditScore);  //代售点信用积分
        tv_creditScore = (TextView)view.findViewById(R.id.tv_creditScore);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();
    }



    private void initWidget() {
        if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
            ll_logoutAccount.setVisibility(View.VISIBLE);
            setPremissionShowHideView(Premission.FB_USERCHANGE_CANCELLATION,ll_logoutAccount);	// 是否有用户注销权限
            ll_creditScore.setVisibility(View.VISIBLE);
            setPremissionShowHideView(Premission.FB_CREDITSCORE_VIEW,ll_creditScore);	// 是否有信用积分权限

        }
        ll_person.setOnClickListener(this);
        ll_help.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        iv_logo.setOnClickListener(this);
        ll_password_management.setOnClickListener(this);
        ll_exit_login.setOnClickListener(this);
        ll_logoutAccount.setOnClickListener(this);
        ll_creditScore.setOnClickListener(this);
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity.roleDifferentiationLoadingInterface()==R.string.outlets) {
            setPremissionShowHideView(Premission.ACCT_TRADEFLOW_MYWALLET,ll_my_wallet); //钱包菜单权限 //我的模块  我的钱包只有代售点有
            ll_my_wallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyApplication.mContext, WalletA.class);
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out); //跳转我的钱包
                }
            });
        }
        setPremissionShowHideView(Premission.SYS_USER_INFO,ll_person);	// 是否有查看个人资料这个权限
        setPremissionShowHideView(Premission.SYS_USER_MODIFYPWD,ll_password_management);	// 是否有修改密码这个权限
    }


    private void initData() {
        Object loginResult = readObject(MyApplication.mContext, "loginResult");
        user = (User) loginResult;
        tv_outletsName.setText(user.getOrgName());  //售票点
        loginName = user.getLoginName();
        if (user.getParentId().isEmpty()) {  //getParentId 父账号ID  weik
            //是主账号
            tv_userName.setText("用户名：" + loginName); //用户名
        } else {
            ll_logoutAccount.setVisibility(View.GONE);//子账号隐藏账户注销
            //子账号
            tv_userName.setText("用户名：" + user.getMobile()); //用户名
        }

            photoSrc = user.getPhotoSrc();
            //photoSrc = "http://192.168.0.152:8080/OC/" + user.getPhotoSrc();
            //返回来的图片路径是否为空  为空表示没有上传过图片
            photoimgFlag = user.getPhotoSrc();
            //	Glide.with(this).load(R.drawable.applogo).transform(new GlideRoundTransform(this)).into(iv_logo);
            HomepageCommon homepageCommon = new HomepageCommon(baseActivity);
            homepageCommon.transferControlHeaderImage(iv_logo);
            homepageCommon.setImageHeader(photoimgFlag);
            homepageCommon.create();


        if(serviceCode!=null &&serviceVersionName !=null) {
            //	serviceCode = serviceCode;  //获取服务器apk版本并保存
            if (Integer.parseInt(MyApplication.serviceCode) > VersonCodeUtils.getVersionCode(MyApplication.mContext)) {
                tv_circle_red.setVisibility(View.VISIBLE);
            } else {
                tv_circle_red.setVisibility(View.GONE); //隐藏我的 模块版本更新小红点
            }
        }else{
            tv_circle_red.setVisibility(View.GONE);
        }
//        creditScore = SPUtils2.getString(MyApplication.mContext, "creditScore");

       /* if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {

            getCredit(true);
        }*/

        if (creditScoreStr != null){
//            tv_creditScore.setText(creditScore );//第一次进首页，“我的”fragment还没加载所以接受不到值
            tv_creditScore.setText(Html.fromHtml( "<font color='#4D90E7'>" +creditScore  + "分" + "</font>"));
        }

        entityList = (List<ConfigureBean.ListEntity>) ObjectSaveUtil.readObject(MyApplication.mContext,"ListEntity"); //配置集合
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==5){ //表示上传头像成功后返回 一个路径
            absolutePath = data.getStringExtra("clipPath");
            try {
                Glide.with(this).load(absolutePath).asBitmap().centerCrop()
                        .into(new BitmapImageViewTarget(iv_logo) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                iv_logo.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            // 个人信息
            case ll_perosn:
                try {
                    UserInfo userInfo = new UserInfo();
                    userInfo.getUserInfo(baseActivity, cpd,PersonalInformationA.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            //帮助中心
            case R.id.ll_help:
                startActivity(new Intent(MyApplication.mContext, HelpCenterA.class));
                break;

            //设置界面
            case R.id.ll_setting:
                baseActivity.startActivityMethod(SettingA.class);
                break;

            //头像点击
            case R.id.iv_logo:
                Intent intent = new Intent(MyApplication.mContext,UpdateHeader.class);
                //	String headeUrl = Define.URL+photoSrc; //头像
                if(absolutePath==null) {
                    if (photoimgFlag.isEmpty()) {
                        //如果后台没有图片就去默认本地图片
                        Resources res = getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.applogo);
                        Bundle b = new Bundle();
                        b.putParcelable("bitmap", bmp);
                        intent.putExtras(b);
                    } else {
                        intent.putExtra("headeUrl", photoSrc);
                    }
                }else{
                    intent.putExtra("absolutePath", absolutePath);
                }

                startActivityForResult(intent,HEADERIMAG_REQUESTCODE);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;

            case R.id.ll_password_management:  //我的 密码管理
                baseActivity.startActivityMethod(PasswordManagement.class);
                break;

            case R.id.ll_exit_login: //退出登录


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示！")
                        .setMessage("退出登录，是否注销当前用户？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                treeIdList.clear();  //清除火车站层级树节点
                                //清除用户保存的用户名密码
                                SPUtils.put(MyApplication.mContext, "password", "");
                                startActivity(new Intent(MyApplication.mContext, LoginA.class));
                                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                onProfileSignOff();  //友盟账号退出登录
                                baseActivity.finish();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();

                break;

            case R.id.ll_logoutAccount://代售点账户注销
                findUnComfirmUserchange();
                break;
            case R.id.ll_creditScore://代售点信用积分
                if(entityList !=null){
                    for (ConfigureBean.ListEntity entity :
                            entityList) {
                        if(entity.getCreditScoreSwitch() != null) {
                            creditScoreSwitch = entity.getCreditScoreSwitch();
                            break;
                        }
                    }
                }

                if ("close".equals(creditScoreSwitch)){
                    ToastUtils.showToast(MyApplication.mContext, "积分功能尚未开放！");
                }else {
                    intent = new Intent(MyApplication.mContext, OjiCreditA.class);
//                intent.putExtra("creditScore", creditScore);
                    baseActivity.startActivityIntent(intent);
                }
                break;

        }
    }


    /**
     *查找未完成变更申请或账户注销申请
     */
    private void findUnComfirmUserchange(){
        String url = Define.URL+"user/findUnComfirmUserchange";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "findUnComfirmUserchangeLogout", "", true, true);
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
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag){
            case "findUnComfirmUserchangeLogout":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String type = jsonObject.optString("type");
                    if ("0".equals(type)){
                        intent = new Intent(getActivity(), AccountLogOffAuditingA.class);
                        intent.putExtra("operationTag", "logout");
                        baseActivity.startActivityIntent(intent);

                    }else if ("1".equals(type)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("提示！")
                                .setMessage("您的账户已经提交了资料变更申请，审核期间无法进行账户注销操作")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                    }
                                });

                        builder.create().show();
                    }else {

                        baseActivity.startActivityMethod(AccountLogOffNeedKnow.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "getCredit":

                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    creditScore = jsonObject.optString("creditScore");
                    String creditLevelName = jsonObject.optString("creditLevelName");
                    if (creditScore != null){
//                        tv_creditScore.setText(creditScore );
                        tv_creditScore.setText(Html.fromHtml( "<font color='#4D90E7'>" +creditScore  + "分" + "</font>"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void closeDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.closeDismiss(cpd, tag, controlOff);
        switch (tag){
            case "getCredit":
                cpd.dismiss();
                break;
        }
    }

    //运行在post运行所在的线程
    public void onEvent(CreditScoreEvent event) {   //别的地方发送数据到这里  这里直接接受
        creditScoreStr = event.integral;
        if (creditScoreStr != null){
//            tv_creditScore.setText(creditScoreStr );//第一次进首页，“我的”fragment还没加载所以接受不到值
            tv_creditScore.setText(Html.fromHtml( "<font color='#4D90E7'>" +creditScore + "分"  + "</font>"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //注销监听
    }
}
