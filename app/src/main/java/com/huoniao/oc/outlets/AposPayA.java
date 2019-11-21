package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class AposPayA extends BaseActivity implements BDLocationListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.et_rechargeAmount)
    EditText etRechargeAmount;
    @InjectView(R.id.tv_mode)
    TextView tvMode;
    @InjectView(R.id.layout_payByCard)
    LinearLayout layoutPayByCard;
    @InjectView(R.id.layout_scan)
    LinearLayout layoutScan;
    private LocationClient mLocationClient;
    private String country, city, province;
    private String merchantId; //商户id
    private String orderNo;//订单号
    private String transMoney;//金额
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apos_pay);
        ButterKnife.inject(this);
        initLocation();
        initWidget();
        initData();
    }


    private void initWidget() {
        InputFilter[] filters = {new CashierInputFilter()};
        etRechargeAmount.setFilters(filters);
    }

    private void initData() {
        user = (User) readObject(AposPayA.this, "loginResult");
        intent = getIntent();
        moneyType = intent.getStringExtra("moneyType");
        if ("缴费".equals(moneyType)){
            tvTitle.setText("缴费");
            tvMode.setText("选择缴费方式");
//            tradeName = "信E付缴费";

        }else {
            tvTitle.setText("收款");
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




    @OnClick({R.id.iv_back, R.id.layout_payByCard, R.id.layout_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(102);  //刷新余额
                finish();
                break;
            case R.id.layout_payByCard:
                channelType = "0022";
                channelTypeOji = "XEF_POSE";
                if ("缴费".equals(moneyType)){
                     tradeName = "信E付刷卡缴费";
                }else {
                     tradeName = "信E付刷卡收款";
                }
                if (isLocationEnabled() == true) {
                    if (country == null || country.isEmpty()) {
                        ToastUtils.showLongToast(AposPayA.this, "位置信息获取失败，请检查位置服务是否打开或是否打开O计的定位权限！");
                        return;
                    } else {
                        if (!("中国".equals(country)) || "香港特别行政区".equals(city) || "澳门特别行政区".equals(city)
                                || "台湾省".equals(province)) {
                            ToastUtils.showLongToast(AposPayA.this, "您目前不在可使用地区，APOS只限在中国大陆地区使用！");
                            return;
                        }
                        layoutPayByCard.setEnabled(false);
//                        getToken();
                        createXefTrade();
                    }
                }else {
                    ToastUtils.showToast(AposPayA.this, "需打开手机定位服务才能使用APOS功能！");
                }

                break;
            case R.id.layout_scan:
                channelType = "0074";
                channelTypeOji = "XEF_QR_PAY";
                if ("缴费".equals(moneyType)){
                    tradeName = "信E付扫码缴费";
                }else {
                    tradeName = "信E付扫码收款";
                }
                if (isLocationEnabled() == true) {
                    if (country == null || country.isEmpty()) {
                        ToastUtils.showLongToast(AposPayA.this, "位置信息获取失败，请检查位置服务是否打开或是否打开O计的定位权限！");
                        return;
                    } else {
                        if (!("中国".equals(country)) || "香港特别行政区".equals(city) || "澳门特别行政区".equals(city)
                                || "台湾省".equals(province)) {
                            ToastUtils.showLongToast(AposPayA.this, "您目前不在可使用地区，APOS只限在中国大陆地区使用！");
                            return;
                        }
                        layoutScan.setEnabled(false);
//                        getToken();
                        createXefTrade();
                    }
                }else {
                    ToastUtils.showToast(AposPayA.this, "需打开手机定位服务才能使用APOS功能！");
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
            if ("缴费".equals(moneyType)) {
                ToastUtils.showToast(AposPayA.this, "请输入缴费金额");
            }else {
                ToastUtils.showToast(AposPayA.this, "请输入收款金额");
            }
            layoutPayByCard.setEnabled(true);
            layoutScan.setEnabled(true);
            return;
        }

        if ("0".equals(rechargeAmount) || "0.0".equals(rechargeAmount) || "0.00".equals(rechargeAmount)) {
            if ("缴费".equals(moneyType)) {
                ToastUtils.showToast(AposPayA.this, "请输入缴费金额");
            }else {
                ToastUtils.showToast(AposPayA.this, "请输入收款金额");
            }
            layoutPayByCard.setEnabled(true);
            layoutScan.setEnabled(true);
            return;
        }

        double rechargeAmountD = Double.parseDouble(rechargeAmount) * 100;//入参的单位是分，所以这里转换一下
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
        intent.putExtra("transMoney", transMoney);
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
            jsonObject.put("tradeFee", rechargeAmount);
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
//		ToastUtils.showLongToast(RechargeA.this, country);
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
                    ToastUtils.showLongToast(AposPayA.this, "交易成功");
                    break;
                case 22:
                    ToastUtils.showLongToast(AposPayA.this, "交易失败");
                    requestCloseTradeStatus();
                    break;
                case 23:
                    ToastUtils.showLongToast(AposPayA.this, "请以支付成功通知为准");
                    break;
                case 24:
                    ToastUtils.showLongToast(AposPayA.this, "网络连接失败，请重试");
                    break;
                case 25:
                    //交易中断 包含用户主动取消
                    ToastUtils.showLongToast(AposPayA.this, errorMsg);
                    break;
            }
        }

    }



}
