package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.NewRechargeB;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

/**
 * Created by Administrator on 2019/4/28.
 */

public class NewAposPayA extends BaseActivity implements BDLocationListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.et_rechargeAmount)
    EditText etRechargeAmount;
    @InjectView(R.id.tv_mode)
    TextView tvMode;
    @InjectView(R.id.layout_payByCard)
    RelativeLayout layoutPayByCard;
    @InjectView(R.id.layout_scan)
    LinearLayout layoutScan;
    @InjectView(R.id.rl_Service_Expansion_a)
    RelativeLayout rl_Service_Expansion_a;
    @InjectView(R.id.iv_Pull_pull_a)
    ImageView iv_Pull_pull_a;
    @InjectView(R.id.ll_Service_charge_a)
    LinearLayout ll_Service_charge_a;

    @InjectView(R.id.rl_Service_Expansion_b)
    RelativeLayout rl_Service_Expansion_b;
    @InjectView(R.id.iv_Pull_pull_b)
    ImageView iv_Pull_pull_b;
    @InjectView(R.id.ll_Service_charge_b)
    LinearLayout ll_Service_charge_b;
    @InjectView(R.id.tv_money_titile)
    TextView tv_money_titile;
    private LocationClient mLocationClient;
    private String country, city, province;
    private String merchantId; //商户id
    private String orderNo;//订单号
    private String transMoney , codetMoney;//金额
    private String rechargeAmount;//输入金额
    private String channelType;//支付通道类型 0022刷卡， 0074扫码
    private String channelTypeOji;//XEF_POSE：信E付刷卡
    private JSONObject jo;
    private String keySign;//签名
    private static final String PLUGIN_PKGNAME = "cn.com.qdone.android.payment.xinefuplugin";
    private static final String PLUGIN_CLASSNAME = "cn.com.qdone.android.payment.activity.emalls2d.NewWebViewHostActivity";
    private static final String TESTKEY = "12345678901234561234567890123456";
    private Intent intent;
    private String moneyType;
    private User user;
    private String tradeName;
    private String tradeType = "income";
    private String keyTime;

    @InjectView(R.id.tv_pos_m)
    TextView tv_pos_m;  //刷卡服务费金额
    @InjectView(R.id.tv_pos_service_m)
    TextView tv_pos_service_m;  //刷卡服务费
    @InjectView(R.id.tv_pos_Collection_m)
    TextView tv_pos_Collection_m;   //刷卡第三方代收
    @InjectView(R.id.tv_pos_Totalsum)
    TextView tv_pos_Totalsum;   //刷卡总金额

    @InjectView(R.id.tv_Sweep_code_m)
    TextView tv_Sweep_code_m;   //扫码服务费金额
    @InjectView(R.id.tv_code_service_m)
    TextView tv_code_service_m; //扫码服务费
    @InjectView(R.id.tv_code_Collection_m)
    TextView tv_code_Collection_m;   //扫码第三方代收
    @InjectView(R.id.tv_code_code_Totalsum)
    TextView tv_code_code_Totalsum; //扫码总金额

    private String loginName;

    private BigDecimal totalSum = new BigDecimal("0") , codetotalSum = new BigDecimal("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newapospay);
        ButterKnife.inject(this);
        initLocation();
        initWidget();
        initData();
    }

    private void initWidget() {

        InputFilter[] filters = {new CashierInputFilter()};
        etRechargeAmount.setFilters(filters);
        etRechargeAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(delayRun);
                //800毫秒没有输入认为输入完毕
                handler.postDelayed(delayRun, 1000);
                layoutPayByCard.setEnabled(false);
                layoutScan.setEnabled(false);
            }
        });
    }

    private Handler handler = new Handler();
    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            //清空etRechargeAmount后把下面的值都设为空
            if (etRechargeAmount.getText().toString().isEmpty()) {
                rechargeAmount = "0";
            }else {
                rechargeAmount = etRechargeAmount.getText().toString();
            }
            //在这里调用服务器的接口，获取数据
            getServicecharge();
            getServicechargea();
        }
    };

    /**
     * 获取E信付刷卡服务费和代收费
     */
    private void getServicecharge(){
        String url = Define.URL + "acct/getServiceFeeAndPoundageAmount";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tradeType" , "xefPos");
            jsonObject.put("tradeFee" , rechargeAmount);
            jsonObject.put("loginName" , loginName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getServiceFeeAndPoundagepos", "0", true, false);
    }

    /**
     * 获取E信付扫码服务费和代收费
     */
    private void getServicechargea(){
        String url = Define.URL + "acct/getServiceFeeAndPoundageAmount";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tradeType" , "xefQrPay");
            jsonObject.put("tradeFee" , rechargeAmount);
            jsonObject.put("loginName" , loginName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getServiceFeeAndcode", "0", true, false);
    }

    private void initData() {
        user = (User) readObject(NewAposPayA.this, "loginResult");
        intent = getIntent();
//        User loginResult = (User) ObjectSaveUtil.readObject(this, "loginResult");
        //用户名
        loginName = user.getLoginName();
        moneyType = intent.getStringExtra("moneyType");
        if ("缴款".equals(moneyType)){
            tvTitle.setText("缴款");
            tv_money_titile.setText("缴款金额");
            tvMode.setText("选择缴款方式");
//            tradeName = "信E付缴费";

        }else {
            tvTitle.setText("收款");
            tv_money_titile.setText("票款金额");
            tvMode.setText("选择收款方式");
//            tradeName = "信E付收款";

        }
    }

    /**
     * 定位
     */
    private void initLocation() {

        // 定位初始化
        // 声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(this);// 注册定位监听接口

        /**
         * 设置定位参数
         */
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setScanSpan(2000);// 设置发起定位请求的间隔时间,ms
        option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
        option.setOpenGps(true);// 打开gps
//		option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private boolean PayByCard = false;  //刷卡打開
    private boolean SweepCode = false;  //掃碼打開
    @OnClick({R.id.iv_back, R.id.layout_payByCard, R.id.layout_scan , R.id.rl_Service_Expansion_a , R.id.rl_Service_Expansion_b})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(102);  //刷新余额
                finish();
                break;
            case R.id.layout_payByCard:
                if (tv_pos_Totalsum.getText().toString().equals("¥0")) {
                    Toast.makeText(NewAposPayA.this, "请输入大于0的金额！", Toast.LENGTH_SHORT).show();
                    layoutPayByCard.setEnabled(true);
                    return;
                }
                channelType = "0022";
                channelTypeOji = "XEF_POSE";
                if ("缴款".equals(moneyType)){
                    tradeName = "信E付刷卡缴款";
                }else {
                    tradeName = "信E付刷卡收款";
                }
                if (isLocationEnabled() == true) {
                    if (country == null || country.isEmpty()) {
                        ToastUtils.showLongToast(NewAposPayA.this, "位置信息获取失败，请检查位置服务是否打开或是否打开O计的定位权限！");
                        return;
                    } else {
                        if (!("中国".equals(country)) || "香港特别行政区".equals(city) || "澳门特别行政区".equals(city)
                                || "台湾省".equals(province)) {
                            ToastUtils.showLongToast(NewAposPayA.this, "您目前不在可使用地区，APOS只限在中国大陆地区使用！");
                            return;
                        }
                        layoutPayByCard.setEnabled(false);
//                        getToken();
                        createXefTrade();
                    }
                }else {
                    ToastUtils.showToast(NewAposPayA.this, "需打开手机定位服务才能使用APOS功能！");
                }

                break;
            case R.id.layout_scan:
                if (tv_code_code_Totalsum.getText().toString().equals("¥0")){
                    Toast.makeText(this, "请输入大于0的金额！", Toast.LENGTH_SHORT).show();
                    layoutScan.setEnabled(true);
                    return;
                }
                channelType = "0074";
                channelTypeOji = "XEF_QR_PAY";
                if ("缴款".equals(moneyType)){
                    tradeName = "信E付扫码缴款";
                }else {
                    tradeName = "信E付扫码收款";
                }
                if (isLocationEnabled() == true) {
                    if (country == null || country.isEmpty()) {
                        ToastUtils.showLongToast(NewAposPayA.this, "位置信息获取失败，请检查位置服务是否打开或是否打开O计的定位权限！");
                        return;
                    } else {
                        if (!("中国".equals(country)) || "香港特别行政区".equals(city) || "澳门特别行政区".equals(city)
                                || "台湾省".equals(province)) {
                            ToastUtils.showLongToast(NewAposPayA.this, "您目前不在可使用地区，APOS只限在中国大陆地区使用！");
                            return;
                        }
                        layoutScan.setEnabled(false);
//                        getToken();
                        createXefTrade();
                    }
                }else {
                    ToastUtils.showToast(NewAposPayA.this, "需打开手机定位服务才能使用APOS功能！");
                }
                break;
            case R.id.rl_Service_Expansion_a:
                if (PayByCard == false){
                    iv_Pull_pull_a.setImageDrawable(getResources().getDrawable(R.drawable.icon_upward));
                    ll_Service_charge_a.setVisibility(View.VISIBLE);
                    PayByCard =true;
                }else if (PayByCard == true){
                    iv_Pull_pull_a.setImageDrawable(getResources().getDrawable(R.drawable.icon_down));
                    ll_Service_charge_a.setVisibility(View.GONE);
                    PayByCard = false;
                }
                break;
            case R.id.rl_Service_Expansion_b:
                if (SweepCode == false){
                    iv_Pull_pull_b.setImageDrawable(getResources().getDrawable(R.drawable.icon_upward));
                    ll_Service_charge_b.setVisibility(View.VISIBLE);
                    SweepCode =true;
                }else if (SweepCode == true){
                    iv_Pull_pull_b.setImageDrawable(getResources().getDrawable(R.drawable.icon_down));
                    ll_Service_charge_b.setVisibility(View.GONE);
                    SweepCode = false;
                }
                break;
        }
    }

    /**
     * 创建订单
     */
    private void createXefTrade(){
        rechargeAmount = etRechargeAmount.getText().toString();
        if (rechargeAmount.isEmpty() || rechargeAmount == null) {
            if ("缴款".equals(moneyType)) {
                ToastUtils.showToast(NewAposPayA.this, "请输入缴款金额");
            }else {
                ToastUtils.showToast(NewAposPayA.this, "请输入收款金额");
            }
            layoutPayByCard.setEnabled(true);
            layoutScan.setEnabled(true);
            return;
        }

        if ("0".equals(rechargeAmount) || "0.0".equals(rechargeAmount) || "0.00".equals(rechargeAmount)) {
            if ("缴款".equals(moneyType)) {
                ToastUtils.showToast(NewAposPayA.this, "请输入缴款金额");
            }else {
                ToastUtils.showToast(NewAposPayA.this, "请输入收款金额");
            }
            layoutPayByCard.setEnabled(true);
            layoutScan.setEnabled(true);
            return;
        }
        double codetotaD = Double.parseDouble(totalSum.toString()) * 100;//入参的单位是分，所以这里转换一下
        int codetotaI = (new Double(codetotaD)).intValue();
        codetMoney = String.valueOf(codetotaI);

        double rechargeAmountD = Double.parseDouble(codetotalSum.toString()) * 100;//入参的单位是分，所以这里转换一下
        int rechargeAmountI = (new Double(rechargeAmountD)).intValue();
        transMoney = String.valueOf(rechargeAmountI);

        requestCreateXefTrade();
    }

    /**
     * 调用信e付那边
     */
    private void intentParamsXef(){
        Intent intent = new Intent();
        String packageName = PLUGIN_PKGNAME;
        String className = PLUGIN_CLASSNAME;
        intent.setClassName(packageName, className);
        // 【1】操作类型，1为交易
        intent.putExtra("pluginOperateType", 1);
        // 【2】交易金额，"1"为1分钱
        if ("XEF_POSE" == channelTypeOji){
            intent.putExtra("transMoney", codetMoney);
        }else if ("XEF_QR_PAY" == channelTypeOji){
            intent.putExtra("transMoney", transMoney);
        }
        // 【3】第三方订单号
        intent.putExtra("thirdOrderNo", orderNo);
//        intent.putExtra("thirdOrderNo", "123456");
        // 【4】用户姓名
        intent.putExtra("userRealName", user.getOperatorName());
        // 【5】用户手机号
        intent.putExtra("userPhone", user.getOperatorMobile());
        // 【6】用户ID
        intent.putExtra("userId", "16032517120931100001");
        // 【7】商户ID
        intent.putExtra("merchantId", merchantId);//火鸟商户id
//                intent.putExtra("merchantId", "800062016033482");
//				intent.putExtra("merchantId", "17031409430904500001");//镜像环境
        // 【8】商城ID
        intent.putExtra("mallId", "D10212");//2.7
        // 【9】插件退出时是否弹出提示
        intent.putExtra("isExitDialog", true);

        // 【10】自定义字段      用户自定义json中key和value字段示例如下：
        JSONObject mObject = new JSONObject();
        try{
            mObject.put("key1", "value1");
            mObject.put("key2", "value2");
            mObject.put("key3", "value3");
            mObject.put("key4", "value4");
        }catch(Exception e){
            e.printStackTrace();
        }
        intent.putExtra("custom", mObject.toString());
        JSONArray deviceTypeList = new JSONArray();
        try {
            // 设备型号： ME30为"1004"，ME31为"1006"，M35为"1007"，M36为"1008"
            jo = new JSONObject();
            jo.put("deviceType", "1004");
            deviceTypeList.put(jo);
            jo = new JSONObject();
            jo.put("deviceType", "1006");
            deviceTypeList.put(jo);
            jo = new JSONObject();
            jo.put("deviceType", "1007");
            deviceTypeList.put(jo);
            jo = new JSONObject();
            jo.put("deviceType", "1008");
            deviceTypeList.put(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 【11】设备列表
        intent.putExtra("deviceTypeList", deviceTypeList.toString());
        // 【12】可选参数。0019收款 0031微信支付(商户扫码支付)
        intent.putExtra("channelType", channelType);
        // 【13】支付通知地址
        intent.putExtra("callBackUrl", "");
        // 【14】设置商户类型 0 表示绑卡支付商户； 1 表示刷卡支付商户
        intent.putExtra("payType", "1");
        // 【15】收款:0003    付款:0002
        intent.putExtra("orderType", "0003");
        // 【16】手续费承担 0：商户承担   1：用户承担
        intent.putExtra("IS_USER_PAY_FEE", "0");
        // 【17】是否区分借贷记卡      非0：不区分     0：区分
        intent.putExtra("DEBIT_OR_CREDIT_CARD", "1");
//                String keyTime = System.currentTimeMillis() + "";
        intent.putExtra("keyTime", keyTime);
        intent.putExtra("keySign", keySign);
        //微信、支付宝支付扫是否采用扫码枪扫码
        intent.putExtra("IS_SCAN_GUN", false);
        startActivityForResult(intent, 11);
    }

    /**
     * 判断是否打开了系统定位服务
     * @return
     */
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     *  创建信E付订单
     *
     */
    private void requestCreateXefTrade(){
        String url = Define.URL+"acct/createXefTrade";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tradeName", tradeName);
            if ("XEF_POSE" == channelTypeOji){
                jsonObject.put("tradeFee", totalSum.setScale(2 , RoundingMode.HALF_UP).toString() );
            }else if ("XEF_QR_PAY" == channelTypeOji){
                jsonObject.put("tradeFee", codetotalSum.setScale(2 , RoundingMode.HALF_UP).toString() );
            }
            jsonObject.put("tradeType", tradeType);
            jsonObject.put("channelType", channelTypeOji);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "createXefTrade", "", true, false);
    }

    /**
     * 通知后台交易关闭
     */
    private void requestCloseTradeStatus() {
        String url = Define.URL + "acct/closeTradeStatus";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("flowId", orderNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "closeTradeStatusXef", "", true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "createXefTrade":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    keyTime = jsonObject.optString("keyTime");
                    merchantId = jsonObject.optString("merchantId");
                    orderNo = jsonObject.optString("orderId");
                    keySign = jsonObject.optString("sign");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intentParamsXef();

                break;

            case "closeTradeStatusXef":
                //TODO 通知后台交易关闭后的处理
                break;

            case "getServiceFeeAndPoundagepos":
                Gson gson = new Gson();
                NewRechargeB newRechargeB = gson.fromJson(json.toString(), NewRechargeB.class);
                NewRechargeB.RechargeBean rechargeBeanList = (NewRechargeB.RechargeBean) newRechargeB.getData();
                BigDecimal posPoundageAmount = new BigDecimal(rechargeBeanList.getPoundageAmount());
                BigDecimal posServiceFeeAmount = new BigDecimal(rechargeBeanList.getServiceFeeAmount());
                BigDecimal posTotalservicecharge = posPoundageAmount.add(posServiceFeeAmount);//服务费总
                BigDecimal posrechargeAmount = new BigDecimal(rechargeAmount);
                totalSum = posTotalservicecharge.add(posrechargeAmount);//合计付款金额
                tv_pos_service_m.setText("¥"+rechargeBeanList.getServiceFeeAmount());
                tv_pos_Collection_m.setText("¥"+rechargeBeanList.getPoundageAmount());
                tv_pos_m.setText("¥"+ posTotalservicecharge.setScale(2 , RoundingMode.HALF_UP));
                tv_pos_Totalsum.setText("¥"+ totalSum.setScale(2 , RoundingMode.HALF_UP));
                layoutPayByCard.setEnabled(true);
                break;
            case "getServiceFeeAndcode":
                Gson gsons = new Gson();
                NewRechargeB newRechargeBa = gsons.fromJson(json.toString(), NewRechargeB.class);
                NewRechargeB.RechargeBean rechargeBeanLista = (NewRechargeB.RechargeBean) newRechargeBa.getData();
                BigDecimal codePoundageAmount = new BigDecimal(rechargeBeanLista.getPoundageAmount());
                BigDecimal codeServiceFeeAmount = new BigDecimal(rechargeBeanLista.getServiceFeeAmount());
                BigDecimal codeTotalservicecharge = codePoundageAmount.add(codeServiceFeeAmount);//服务费总和
                BigDecimal coderechargeAmount = new BigDecimal(rechargeAmount);
                codetotalSum = codeTotalservicecharge.add(coderechargeAmount);//合计付款金额
                tv_code_service_m.setText("¥"+rechargeBeanLista.getServiceFeeAmount());
                tv_code_Collection_m.setText("¥"+rechargeBeanLista.getPoundageAmount());
                tv_Sweep_code_m.setText("¥"+ codeTotalservicecharge.setScale(2 , RoundingMode.HALF_UP));//保留两位小数
                tv_code_code_Totalsum.setText("¥"+ codetotalSum.setScale(2 , RoundingMode.HALF_UP));
                layoutScan.setEnabled(true);
                break;
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        }
        country = bdLocation.getCountry();
        city = bdLocation.getCity();
        province = bdLocation.getProvince();
    }

    @Override
    protected void onStart() {
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutPayByCard.setEnabled(true);
        layoutScan.setEnabled(true);
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    /*
   * 支付结果码。查余额没有结果码。
   */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.v(TAG, "requestCode:" + requestCode);
//        Log.v(TAG, "resultCode:" + resultCode);
        String errorMsg = "";
        if(data != null && !TextUtils.isEmpty(data.getStringExtra("errorMsg"))){
            errorMsg = data.getStringExtra("errorMsg");
//            errorMsgText.setText(errorMsg);
        }

//        Log.v(TAG, "errorMsg:" + errorMsg);
        if (requestCode == 11) {
            switch (resultCode) {
                case 21:
                    ToastUtils.showLongToast(NewAposPayA.this, "交易成功");
                    break;
                case 22:
                    ToastUtils.showLongToast(NewAposPayA.this, "交易失败");
                    requestCloseTradeStatus();
                    break;
                case 23:
                    ToastUtils.showLongToast(NewAposPayA.this, "请以支付成功通知为准");
                    break;
                case 24:
                    ToastUtils.showLongToast(NewAposPayA.this, "网络连接失败，请重试");
                    break;
                case 25:
                    //交易中断 包含用户主动取消
                    ToastUtils.showLongToast(NewAposPayA.this, errorMsg);
                    break;
            }
        }
    }

}