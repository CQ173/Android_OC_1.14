package com.huoniao.oc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.admin.AdminOJiTransactionDetails;
import com.huoniao.oc.bean.NotificationBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.fragment.FragmentFactory;
import com.huoniao.oc.fragment.outlets.OutletsHomepageF;
import com.huoniao.oc.fragment.outlets.OutletsShoppingF;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.Message2A;
import com.huoniao.oc.user.PerfectInformationA;
import com.huoniao.oc.user.RegisterSuccessA;
import com.huoniao.oc.useragreement.RegisterAgreeA;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.versionupdate.UpdateManager;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;
import com.huoniao.oc.wxapi.WXEntryActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UTrack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.infoNum;
import static com.huoniao.oc.R.id.layot_tab_ole;
import static com.huoniao.oc.R.id.layout_tab_homepage;
import static com.huoniao.oc.R.id.layout_tab_paysystem;
import static com.huoniao.oc.R.id.layout_tab_shoping;
import static com.huoniao.oc.R.id.layout_tab_user;
import static com.huoniao.oc.R.id.tab_homepage_img;
import static com.huoniao.oc.R.id.tab_ole_img;
import static com.huoniao.oc.R.id.tab_paysystem_img;
import static com.huoniao.oc.R.id.tv_tab_paysystem;
import static com.huoniao.oc.R.id.tv_tap_ole;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;
import static com.umeng.analytics.MobclickAgent.onProfileSignOff;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.tv_tab_title)
    TextView tvTabTitle;  //标题
    @InjectView(R.id.iv_personalCenter)
    ImageView ivPersonalCenter;
    @InjectView(R.id.tv_circle)
    TextView tvCircle;  //小红圆点消息条目
    @InjectView(R.id.rl_msgArea)
    RelativeLayout rlMsgArea;
    @InjectView(R.id.tv_financeoOJiTradeDetail)
    TextView tvFinanceoOJiTradeDetail;
    @InjectView(R.id.layout_title)
    LinearLayout layoutTitle;
    @InjectView(R.id.fl_replacement_container)
    FrameLayout flReplacementContainer; //布局加载容器
    @InjectView(tab_homepage_img)
    ImageView tabHomepageImg;  //首页图标
    @InjectView(R.id.tv_tab_homepage)
    TextView tvTabHomepage; //首页文字
    @InjectView(layout_tab_homepage)
    LinearLayout layoutTabHomepage; //首页容器
    @InjectView(tab_ole_img)
    ImageView tabOleImg;    //o计图标
    @InjectView(tv_tap_ole)
    TextView tvTapOle;  //O计 文字
    @InjectView(layot_tab_ole)
    LinearLayout layotTabOle;  //o计容器
    @InjectView(R.id.tab_shoping_img)
    ImageView tabShopingImg;  //商城图标
    @InjectView(R.id.tv_tab_shopping)
    TextView tvTabShopping;  // 商城文字
    @InjectView(layout_tab_shoping)
    LinearLayout layoutTabShoping; //商城容器
    @InjectView(R.id.tab_user_img)
    ImageView tabUserImg;  //用户图标
    @InjectView(R.id.tv_tab_user)
    TextView tvTabUser;  //用户文字
    @InjectView(layout_tab_user)
    LinearLayout layoutTabUser; //用户容器
    @InjectView(R.id.tab_mine_img)
    ImageView tabMineImg; // 我的图标
    @InjectView(R.id.tv_top_mine)
    TextView tvTopMine; //我的文字
    @InjectView(R.id.layout_tab_mine)
    LinearLayout layoutTabMine;  // 我的容器
    @InjectView(tab_paysystem_img)
    ImageView tabPaysystemImg;   //汇缴图片
    @InjectView(tv_tab_paysystem)
    TextView tvTabPaysystem; //汇缴文字
    @InjectView(R.id.layout_tab_paysystem)
    LinearLayout layoutTabPaysystem;  //汇缴容器
    List<String> listFragmentTag = new ArrayList<>();

    private FragmentFactory fragmentFactory;  //fragment工厂
    private BaseFragment baseFragment; //fragment的父类
    private Intent intent;
    private String name;
    private String phoneNumber;
    private String userType;
    private String officeType;
    private String auditState;
    private User user;
    private String roleName;
    public static String creditScore;
    private String linkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
//        kqwSpeechCompound.speaking("Hello word!");

        initIntent();
        RestartDataRecovery(savedInstanceState);
        initWidget();
        initData();
    }



    /**
     * 获取别的的界面传过来的数据
     */
    private void initIntent() {
        intent = getIntent();
        name = intent.getStringExtra("name");
        phoneNumber = intent.getStringExtra("mobile");
        userType = intent.getStringExtra("userType");
        officeType = intent.getStringExtra("officeType");
        auditState = intent.getStringExtra("auditState");
        user = (User) readObject(MainActivity.this, "loginResult");
        String userid = user.getId();
        roleName = user.getRoleName();  //获取角色名
        if (userid != null && !userid.isEmpty()) {  //友盟推送的
            MyApplication.mPushAgent.addAlias(userid, "userid", new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean isSuccess, String message) {
                    if (isSuccess) {
                        Log.d("uPushMsg", "successPushMsg = " + message);
                    } else {
                        Log.d("uPushMsg", "failPushMsg = " + message);
                    }
                }
            });
        }

        layoutTitle = (LinearLayout) findViewById(R.id.layout_title);
    }


    private void initData() {
        String mtype = android.os.Build.MODEL; // 手机型号
        Log.d("mtype", mtype);
        initMessage();
        initFragmentTag();
        for (String fragmentTag : listFragmentTag
                ) {
            removeSolveFragmentGhost(fragmentTag);
        }

        roleNotAuditedStatePage(); //加载页面
        if (!mtype.contains("APOS")) {
            versionInspectUpdate(); //版本更新检查
        }
        requestHomeNotifycationList();//通知
//        requestPreventRepeatSubmit();//获取防重复提交token
    }

    private void versionInspectUpdate() {
        final UpdateManager manager = new UpdateManager(this);
        final ProgressDialog cd = new CustomProgressDialog(this, "正在检查更新....", R.drawable.frame_anim);
        cd.show();

        manager.getServiceVersionInfo(new UpdateManager.versionUpdateNotifyListenner() {
            @Override
            public void UpdateCheckResult(boolean result, boolean bNeedUpdate) {

                if (!result) {
                    cd.dismiss();
//					Toast.makeText(Main.this, "服务器版本更新状态异常，请稍候重试", Toast.LENGTH_SHORT).show();
//					finish();
                    return;
                }


                if (!bNeedUpdate) {
                    cd.dismiss();
//					Toast.makeText(Main.this, R.string.soft_update_no, Toast.LENGTH_SHORT).show();
                    return;
                }
                cd.dismiss();
                manager.checkUpdate();
            }

            @Override
            public void version(String versionCode, String versonName) {
                MyApplication.serviceCode = versionCode;
                MyApplication.serviceVersionName = versonName;
            }
        });
    }

    /**
     * 未审核界面加载 和审核通过界面
     */
    private void roleNotAuditedStatePage() {
        if (roleNotAuditedState() == 1) {  //“未审核”或“审核未通过”状态
            rlMsgArea.setVisibility(View.GONE); //未审核状态消息不显示
            switch (roleDifferentiationLoadingInterface()) {
                case R.string.trainstation: //火车站
                case R.string.outlets: //代售点
                case R.string.admin: //管理员
                case R.string.accounting_and_cashier: //会计出纳
                case R.string.railway_administration: //铁路总局，铁路分局，车务段
                    homeRole("4");
                    //禁用别的选项卡
                    layoutTabHomepage.setEnabled(false);
                    layotTabOle.setEnabled(false);
                    layoutTabShoping.setEnabled(false);
                    layoutTabPaysystem.setEnabled(false);
                    layoutTabUser.setEnabled(false);
                    tvTopMine.setTextColor(Color.rgb(77, 144, 231));
                    tabMineImg.setImageResource(R.drawable.newmine_true);
                    tvTabHomepage.setTextColor(Color.rgb(169, 169, 169));
                    tabHomepageImg.setImageResource(R.drawable.newhomepage_false);
                    break;
            }
        } else {
            homePagePremission();  //判断是否有首页这个权限  有的话就加载
        }

    }

    private static final String tabo = "1";
    private static final String tabw = "2";
    private static final String tabt = "3";
    private static final String tabf = "4";

    /**
     * 角色切换
     *
     * @param tabSwitch 1 首页  2 O计  3 汇缴  4 我的
     */
    private void homeRole(String tabSwitch) {
        //角色进来待处理
        switch (roleDifferentiationLoadingInterface()) {
            case R.string.trainstation: //火车站
                roleLoadSwitch(tabSwitch, BaseFragment.TrainStationHomepageF, null, BaseFragment.AdminSubscriptionManagementF, BaseFragment.MineF);
                break;
            case R.string.admin:  //管理员
                roleLoadSwitch(tabSwitch, BaseFragment.AdminHomepageF, BaseFragment.AdminOjiF, BaseFragment.AdminSubscriptionManagementF, BaseFragment.MineF);
                break;
            case R.string.outlets://代售点    //代售点没有汇缴
                roleLoadSwitch(tabSwitch, BaseFragment.OutletsHomepageF, BaseFragment.OutletsOjiF, null, BaseFragment.MineF);
                break;
            case R.string.accounting_and_cashier: //财务 会计
                roleLoadSwitch(tabSwitch, BaseFragment.FinancialHomepageF, BaseFragment.FinancialOjiF, BaseFragment.AdminSubscriptionManagementF, BaseFragment.MineF);
                if ("2".equals(tabSwitch)){
                    rlMsgArea.setVisibility(View.GONE);
                    setPremissionShowHideView(Premission.ACCT_TRADEFLOW_VIEW, tvFinanceoOJiTradeDetail);  //是否有交易明细权限
                }else {
                    rlMsgArea.setVisibility(View.VISIBLE);
                    tvFinanceoOJiTradeDetail.setVisibility(View.GONE);
                }
                break;

            case R.string.railway_administration: //铁路总局，铁路分局，车务段
                roleLoadSwitch(tabSwitch, BaseFragment.AdministrationHomepageF, null, BaseFragment.AdministrationPaymentF, BaseFragment.MineF);
                break;
        }
    }

    /**
     * 底部选项卡切换 加载fragment
     *
     * @param tabSwitch     第几个选项卡
     * @param homePageF     //首页
     * @param ojF           o计
     * @param subscriptionF 汇缴管理
     * @param mineF         我的模块
     */
    private void roleLoadSwitch(String tabSwitch, String homePageF, String ojF, String subscriptionF, String mineF) {
        switch (tabSwitch) {
            case tabo:
                if (homePageF != null) {
                    addFragment(homePageF);
                }
                break;
            case tabw:
                if (ojF != null) {
                    addFragment(ojF);
                }
                break;
            case tabt:
                if (subscriptionF != null) {
                    addFragment(subscriptionF);
                }
                break;
            case tabf:
                if (mineF != null) {
                    addFragment(BaseFragment.MineF);
                }
                break;
        }
    }


    /**
     *  消息
     */

    private void initMessage() {
        if (infoNum > 99) {
            tvCircle.setVisibility(View.VISIBLE);
            tvCircle.setText(99 + "+");
        } else if (infoNum <= 0) {
            tvCircle.setVisibility(View.GONE);
        } else {
            tvCircle.setVisibility(View.VISIBLE);
            tvCircle.setText(infoNum + "");
        }
    }

    /**
     * 初始化fragment标记
     */
    private void initFragmentTag() {
        listFragmentTag.add(BaseFragment.AdminHomepageF);  //管理员首页
        listFragmentTag.add(BaseFragment.AdminOjiF);  //oji
        listFragmentTag.add(BaseFragment.AdminSubscriptionManagementF); //汇缴
        listFragmentTag.add(BaseFragment.FinancialHomepageF);  //会计首页
        listFragmentTag.add(BaseFragment.FinancialOjiF);  //oji
        listFragmentTag.add(BaseFragment.FinancialUserF); //用户
        listFragmentTag.add(BaseFragment.OutletsHomepageF); //代售点首页
        listFragmentTag.add(BaseFragment.OutletsOjiF);  //oji
        listFragmentTag.add(BaseFragment.OutletsShoppingF); //商城
        listFragmentTag.add(BaseFragment.TrainStationHomepageF);  //火车站首页
        listFragmentTag.add(BaseFragment.TrainStationOjiF);
        listFragmentTag.add(BaseFragment.MineF);  //共用我的模块
        listFragmentTag.add(BaseFragment.AdministrationHomepageF);  //铁总，铁分，车务段首页
        listFragmentTag.add(BaseFragment.AdministrationPaymentF);  //铁总，铁分，车务段汇缴页
    }

    private void addFragment(String fragmentName) {
        hideFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        baseFragment = fragmentFactory.getFragment(fragmentName);

        if (baseFragment != null && !baseFragment.isAdded()) {  //判断是否添加过fragment
            fragmentTransaction.add(R.id.fl_replacement_container, baseFragment, fragmentName);
        }
        fragmentTransaction.show(baseFragment).commit();
    }

    /**
     * 隐藏fragment
     */
    private void hideFragment() {
        for (String fragmentTag : listFragmentTag
                ) {
            boolean b = checkIsAdded(fragmentTag);
            if (b) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                baseFragment = fragmentFactory.getFragment(fragmentTag);
                fragmentTransaction.hide(baseFragment).commit();
            }
        }
    }


    /**
     * 检查是否添加过fragment到activity
     *
     * @param fragmentNameId
     * @return
     */
    private boolean checkIsAdded(String fragmentNameId) {
        return fragmentFactory.getFragment(fragmentNameId).isAdded();
    }

    /**
     * 去除fragment重影
     *
     * @param fragmentName
     */
    private void removeSolveFragmentGhost(String fragmentName) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(fragmentName);
        if (fragmentByTag != null) {
            fragmentTransaction.remove(fragmentByTag);
            fragmentTransaction.commit();
        }
    }

    /**
     * 控件操作
     */
    private void initWidget() {
        fragmentFactory = new FragmentFactory();
        roleSwitchingTab();
        //是否有主页这个权限
        //setPremissionShowHideView(Premission.SYS_USER_INDEX, layoutTabHomepage);  //判断当前用户是否拥有首页这个权限
        setPremissionShowHideView(Premission.FB_INFO_VIEW, rlMsgArea);    // 是否有消息记录这个权限

    }

    /**
     * 首页是否有显示权限
     */
    private void homePagePremission() {
        boolean flag = true;
        for (String premissions : premissionsList) {
            if (Premission.SYS_USER_INDEX.equals(premissions)) { //判断当前用户是否拥有首页这个权限
                //如果有这个权限就显示   默认显示
                homeRole("1");  //首页各种角色切换
                flag = true;
                break;
            } else {
                flag = false;
            }
        }
        if(premissionsList.size()==0){  //如果什么权限都没有赋予  说明首页权限也没有了
            flag = false;
        }
        if (!flag) {
            layoutTabHomepage.setVisibility(View.GONE);  //没有首页权限就隐藏
            if (roleDifferentiationLoadingInterface() == R.string.trainstation) {
                homeRole("3");    //因为火车站没有O计
                tvTabTitle.setText("汇缴");
                tabPaysystemImg.setImageResource(R.drawable.management_true);
                tvTabPaysystem.setTextColor(Color.rgb(77, 144, 231));
            } else {
                homeRole("2");  //默认显示o计
                tabOleImg.setImageResource(R.drawable.newoji_true);
                tvTabTitle.setText("O计");
                tvTapOle.setTextColor(Color.rgb(77, 144, 231));
            }
        }
    }


    /**
     * 角色切换
     */
    private void roleSwitchingTab() {
        switch (roleDifferentiationLoadingInterface()) {
            case R.string.trainstation:
                hideTab("1");  //隐藏选项卡  火车站
                break;
            case R.string.outlets:
                hideTab("2");  //代售点
                break;
            case R.string.admin: //管理员
                hideTab("0");
                break;
            case R.string.accounting_and_cashier:
                hideTab("3");  //财务  出纳
                break;

            case R.string.railway_administration:
                hideTab("4");  //铁路总局，铁路分局，车务段
                break;
        }
    }

    /**
     * 角色进来进行区分加载界面判断
     *
     * @return
     */
    public int roleDifferentiationLoadingInterface() {
        if (Define.TRAINSTATION.equals(LoginA.IDENTITY_TAG) || Define.TRAINSTATION.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.TRAINSTATION.equals(WXEntryActivity.IDENTITY_TAG) || Define.TRAINSTATION.equals(RegisterSuccessA.IDENTITY_TAG)
                ) {
            return R.string.trainstation;
        } else if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
            return R.string.outlets;
        } else if (roleName.contains("系统管理员")) {
            return R.string.admin;
        } else if (roleName.contains("会计") || roleName.contains("出纳")) {
            return R.string.accounting_and_cashier;
        }else if ( "铁路总局".equals(roleName) || "铁路分局".equals(roleName) || "车务段管理员".equals(roleName)) {
            return R.string.railway_administration;
        }
        return 0;
    }


    /**
     * 角色未审核状态 当前账号为“未审核”或“审核未通过”状态
     *
     * @return
     */
    public int roleNotAuditedState() {
        if (Define.OUTLETS_PENDIG_AUDIT.equals(auditState) || Define.OUTLETS_NOTPASS.equals(auditState)) {
            return 1;
        }
        return -1;
    }


    /**
     * 隐藏底部导航其中项
     */
    private void hideTab(String tabPostion) {
        switch (tabPostion) {
            case "0":  //代表管理员
                layoutTabUser.setVisibility(View.GONE);
                layoutTabShoping.setVisibility(View.GONE);

                break;
            case "1":  //代表火车站
                layoutTabUser.setVisibility(View.GONE);
                layoutTabShoping.setVisibility(View.GONE);
                layotTabOle.setVisibility(View.GONE);
                break;
            case "2": //代表 代售点
                layoutTabUser.setVisibility(View.GONE);
                layoutTabPaysystem.setVisibility(View.GONE);
                setPremissionShowHideView(Premission.PARTNER_PARTNERMALL_VIEW, layoutTabShoping);//代售点如果没有查看商城的权限就隐藏
                break;
            case "3":   //代表会计  出纳
                layoutTabShoping.setVisibility(View.GONE);
                layoutTabUser.setVisibility(View.GONE);
                break;

            case "4":   //代表铁路总局，铁路分局，车务段
                layoutTabUser.setVisibility(View.GONE);
                layoutTabShoping.setVisibility(View.GONE);
                layotTabOle.setVisibility(View.GONE);
                break;
        }
        if(!"2".equals(tabPostion)) {  //代售点没有汇缴管理选项不需要权限控制
            setPremissionShowHideView(Premission.FB_PAYMENT_VIEW, layoutTabPaysystem); //选项卡汇缴管理权限
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //这个页面只负责写公用的
            case 101:
                messageNumber();
                break;
        }
    }

    /**
     *     更新消息数目
     */
    public void messageNumber() {
        cpd.show();
        //更新消息数目
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        String url = Define.URL + "fb/infoCount ";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                try {
                    MyApplication.infoNum = json.getInt("infoNum");
                    if (infoNum > 99) {
                        tvCircle.setVisibility(View.VISIBLE);
                        tvCircle.setText(99 + "+");
                    } else if (infoNum <= 0) {
                        tvCircle.setVisibility(View.GONE);
                    } else {
                        tvCircle.setVisibility(View.VISIBLE);
                        tvCircle.setText(infoNum + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

        }, "infoCount", true);
        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    /**
     * 选中状态\
     *
     * @param linearLayout     点击一次后失效 切换后生效
     * @param textView         //选中状态的文字控件
     * @param imageView        选中状态的图片控件
     * @param positionLocation 选中的选项卡的位置 从0开始
     */
    private void bottomSwitchSelected(LinearLayout linearLayout, TextView textView, ImageView imageView, int positionLocation, String titleName) {
        tvTabHomepage.setTextColor(Color.rgb(169, 169, 169));
        tvTapOle.setTextColor(Color.rgb(169, 169, 169));
        tvTabShopping.setTextColor(Color.rgb(169, 169, 169));
        tvTabUser.setTextColor(Color.rgb(169, 169, 169));
        tvTopMine.setTextColor(Color.rgb(169, 169, 169));
        tvTabPaysystem.setTextColor(Color.rgb(169, 169, 169));
        tabHomepageImg.setImageResource(R.drawable.newhomepage_false);
        tabOleImg.setImageResource(R.drawable.newoji_false);
        tabShopingImg.setImageResource(R.drawable.newshoping_false);
        tabUserImg.setImageResource(R.drawable.newuser_false);
        tabMineImg.setImageResource(R.drawable.newmine_false);
        tabPaysystemImg.setImageResource(R.drawable.management);
        if (roleNotAuditedState() != 1) {  //用户为待审核状态 不走
            layoutTabHomepage.setEnabled(true);
            layotTabOle.setEnabled(true);
            layoutTabShoping.setEnabled(true);
            layoutTabUser.setEnabled(true);
            layoutTabMine.setEnabled(true);
            layoutTabPaysystem.setEnabled(true);
        }
        tvTabTitle.setText(titleName);

        if (textView != null && imageView != null && linearLayout != null) {
            textView.setTextColor(Color.rgb(77, 144, 231));
            linearLayout.setEnabled(false);
            switch (positionLocation) {
                case 0:
                    imageView.setImageResource(R.drawable.newhomepage_true);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.newoji_true);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.newshoping_true);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.management_true);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.newuser_true);
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.newmine_true);
                    break;
            }

        }

    }

    @OnClick({R.id.iv_personalCenter, layout_tab_homepage, R.id.layot_tab_ole,
            layout_tab_shoping, layout_tab_paysystem, R.id.layout_tab_user, R.id.layout_tab_mine,
            R.id.tv_financeoOJiTradeDetail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case layout_tab_homepage:  //首页
                bottomSwitchSelected(layoutTabHomepage, tvTabHomepage, tabHomepageImg, 0, "首页");
                homeRole("1");
                break;
            case layot_tab_ole: //O计
                bottomSwitchSelected(layotTabOle, tvTapOle, tabOleImg, 1, "O计");
                homeRole("2");

                break;
            case layout_tab_shoping://
                bottomSwitchSelected(layoutTabShoping, tvTabShopping, tabShopingImg, 2, "商城");  //无需做判断 只有代售点有
                addFragment(BaseFragment.OutletsShoppingF);
                break;
            case layout_tab_paysystem: //汇缴
                bottomSwitchSelected(layoutTabPaysystem, tvTabPaysystem, tabPaysystemImg, 3, "汇缴");
                homeRole("3");
                break;
            case layout_tab_user:
                bottomSwitchSelected(layoutTabUser, tvTabUser, tabUserImg, 4, "用户");
                break;
            case R.id.layout_tab_mine:
                bottomSwitchSelected(layoutTabMine, tvTopMine, tabMineImg, 5, "我的");
                homeRole("4");
                break;
            case R.id.iv_personalCenter:  //消息点击
                intent = new Intent(MainActivity.this, Message2A.class);
                startActivityForResult(intent, 101);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.tv_financeoOJiTradeDetail:  //交易明细
                startActivityMethod(AdminOJiTransactionDetails.class);
                break;
        }
    }

    boolean firstOnce = true;  //表示只能启动app进入一次

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (user!=null && user.getParentId().isEmpty()) {  //getParentId 父账号ID为空说明是父账号  如果不为空说明是子账号 子账号不弹地图引导
            if (roleDifferentiationLoadingInterface() == R.string.outlets && roleNotAuditedState() != 1) { //代售点  账户待审核状态不弹地图引导
                if (firstOnce) {
                    firstOnce = false;
                    baseFragment = fragmentFactory.getFragment(BaseFragment.OutletsHomepageF);
                    if (baseFragment.isVisible() && baseFragment instanceof OutletsHomepageF) {
                        ((OutletsHomepageF) baseFragment).onWindowFocusChanged();
                    }
                }
            }
        }
    }


   /* *//**
     * 请求防重复提交token
     *
     * @throws Exception
     *//*
    private void requestPreventRepeatSubmit() {
        String url = Define.URL + "fb/preventRepeatSubmit";
        requestNet(url, new JSONObject(), "preventRepeatSubmit", "0", true);
    }*/


    /**
     * 请求获取首页重要通知列表
     *
     * @throws Exception
     */
    private void requestHomeNotifycationList() {
        String url = Define.URL + "fb/importantNoticeList";
        requestNet(url, new JSONObject(), "importantNoticeList", "0", true, true);
    }

    /**
     * 重要通知确认请求
     *
     * @param idArray
     */
    private void requestImportNotify(ArrayList<String> idArray) {
        String ids = "";
        for (String id : idArray) {
            if (!ids.isEmpty()) ids += ",";
            ids += id;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ids", ids);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = Define.URL + "fb/noticeConfirm";
        requestNet(url, jsonObject, "noticeConfirm", "0", true, true);
    }

    /**
     * 请求响应成功的数据
     * @param json
     * @param tag
     * @param pageNumber
     */
    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "importantNoticeList":
                ejectImportantNotificationList(json);
                break;
            case "noticeConfirm":
                ToastUtils.showToast(MyApplication.mContext, "您已读取所有重要通知!");
                break;

           /* case "preventRepeatSubmit":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String preventRepeatToken = jsonObject.optString("preventRepeatToken");
                    if (preventRepeatToken != null){
                        SPUtils2.putString(MainActivity.this, "preventRepeatToken", preventRepeatToken);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;*/
        }
    }

    /**
     * 弹出首页重要通知
     *
     * @param json
     */
    private void ejectImportantNotificationList(JSONObject json) {
        try {
            JSONArray jsonArray = json.getJSONArray("data");

            final List<NotificationBean> notificationData = new ArrayList<NotificationBean>();

            for (int i = 0; i < jsonArray.length(); i++) {
                NotificationBean notificationBean = new NotificationBean();
                JSONObject notifiObj = (JSONObject) jsonArray.get(i);
                String content = notifiObj.optString("content");// 通知内容
                String id = notifiObj.optString("id");// 通知id
                String title = notifiObj.optString("title");// 通知标题
                String url = notifiObj.optString("url");// 通知链接

                notificationBean.setUrl(url);

                notificationBean.setNotificationId(id);
                notificationBean.setNotificationTitle(title);
                notificationBean.setNotificationContent(content);
                notificationData.add(notificationBean);
            }
            importantNotificationDialog(notificationData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int index = 0;//首页重要通知下标
    private int notfiCount = 0;//首页重要通知条数
    private NotificationBean importantNotification;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_lastNotifi;
    private TextView tv_nextNotifi;
    private TextView tv_linkUrl;
    private View view_shuXian;

    /**
     * 重要通知弹窗
     */

    private void importantNotificationDialog(final List<NotificationBean> notificationData) {
        if (!notificationData.isEmpty() && notificationData != null) {
            notfiCount = notificationData.size();
            importantNotification = notificationData.get(index);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.important_notification_dialog, null);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_lastNotifi = (TextView) view.findViewById(R.id.tv_lastNotifi);
            tv_nextNotifi = (TextView) view.findViewById(R.id.tv_nextNotifi);
            tv_linkUrl = (TextView) view.findViewById(R.id.tv_linkUrl);
            view_shuXian = view.findViewById(R.id.view_shuXian);
            linkUrl = "";
            try {
                linkUrl = importantNotification.getUrl();
                tv_title.setText(importantNotification.getNotificationTitle());
                tv_content.setText(importantNotification.getNotificationContent());
                checkUrl();
            } catch (Exception e) {
                e.printStackTrace();
            }
//			loadQRcodeImg(url);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (notfiCount == 1) {
                tv_lastNotifi.setVisibility(View.GONE);
                view_shuXian.setVisibility(View.GONE);
                tv_nextNotifi.setText("知道了");
                builder.setView(view);
                final AlertDialog dialog = builder.create();//获取dialog
                dialog.show();
                tv_nextNotifi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        ArrayList<String> idString = new ArrayList<String>();
                        for (NotificationBean notificationBean : notificationData) {
                            idString.add(notificationBean.getNotificationId());
                        }
                        try {
                            requestImportNotify(idString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                builder.setView(view);
                final AlertDialog dialog = builder.create();//获取dialog
                dialog.show();
                tv_lastNotifi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (index > 0 && index < notfiCount) {
                            importantNotification = notificationData.get(--index);
                            linkUrl = importantNotification.getUrl();
                            tv_title.setText(importantNotification.getNotificationTitle());
                            tv_content.setText(importantNotification.getNotificationContent());
                            checkUrl();
                            tv_nextNotifi.setText("下一条");
                        } else if (index <= 0) {
                            index = 0;
                            ToastUtils.showToast(MyApplication.mContext, "已经是第一条通知了！");
                        }
                    }
                });
                tv_nextNotifi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (index >= 0 && index < notfiCount - 1) {
                            importantNotification = notificationData.get(++index);
                            linkUrl = importantNotification.getUrl();
                            tv_title.setText(importantNotification.getNotificationTitle());
                            tv_content.setText(importantNotification.getNotificationContent());
                            checkUrl();
                            if (index > notfiCount - 2) {
                                tv_nextNotifi.setText("知道了");
                            }
                        } else if (index >= notfiCount - 1) {
                            index = notfiCount - 1;
                            dialog.dismiss();
                            ArrayList<String> idStr = new ArrayList<String>();
                            for (NotificationBean notificationBean : notificationData) {
                                idStr.add(notificationBean.getNotificationId());
                            }
                            try {
                                requestImportNotify(idStr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


            }

            tv_linkUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String loginName = user.getLoginName();
                    Intent intent = new Intent(MainActivity.this, RegisterAgreeA.class);
                    intent.putExtra("url", linkUrl + "?loginName=" + loginName);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 检查通知有没有链接
     */
    private void checkUrl(){
        if (linkUrl != null && !linkUrl.isEmpty()){
            tv_linkUrl.setVisibility(View.VISIBLE);
        }else {
            tv_linkUrl.setVisibility(View.GONE);
        }
    }

    /**
     * 程序崩溃数据恢复
     * @param savedInstanceState
     */
    private void RestartDataRecovery(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            LoginA.IDENTITY_TAG = savedInstanceState.getString("LoginA_IDENTITY_TAG");
            PerfectInformationA.IDENTITY_TAG = savedInstanceState.getString("PerfectInformationA_IDENTITY_TAG");
            WXEntryActivity.IDENTITY_TAG = savedInstanceState.getString("WXEntryActivity_IDENTITY_TAG");
            RegisterSuccessA.IDENTITY_TAG = savedInstanceState.getString("RegisterSuccessA_IDENTITY_TAG");
            roleName = savedInstanceState.getString("roleName");
            infoNum =savedInstanceState.getInt("infoNum");
        }
    }

    /**
     * 保存一些角色信息 如果静态的数据不保存 当app程序崩溃数据就为null了
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("LoginA_IDENTITY_TAG", LoginA.IDENTITY_TAG);
        outState.putString("PerfectInformationA_IDENTITY_TAG", PerfectInformationA.IDENTITY_TAG);
        outState.putString("WXEntryActivity_IDENTITY_TAG", WXEntryActivity.IDENTITY_TAG);
        outState.putString("RegisterSuccessA_IDENTITY_TAG", RegisterSuccessA.IDENTITY_TAG);
        outState.putString("roleName", roleName);
        outState.putInt("infoNum",infoNum);
    }

    private long mExitTime;

    @Override
    protected boolean onkeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            baseFragment = fragmentFactory.getFragment(BaseFragment.OutletsShoppingF);
            if (baseFragment.isVisible() && baseFragment instanceof OutletsShoppingF) {
                ((OutletsShoppingF) baseFragment).onKeyDown(keyCode, event);
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
                        @Override
                        public void run() {
                            onProfileSignOff();  //友盟账号退出登录
                            MobclickAgent.onKillProcess(MyApplication.mContext); //友盟退出登录用来保存统计数据。

                        }
                    });

                    MyApplication.exit(0); //退出虚拟机

                }
            }
            return true;
        }
        return super.onkeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        creditScore = null;//静态变量置空
    }
}