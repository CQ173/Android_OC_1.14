package com.huoniao.oc.outlets;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chinaums.commondhjt.model.Action;
import com.chinaums.commondhjt.model.ClientCallback;
import com.chinaums.commondhjt.model.DHJTCallBack;
import com.chinaums.commondhjt.service.DHJTManager;
import com.chinaums.commondhjt.utils.MyLog;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.ConfigureBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.PosSuccessA;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.NotClickableUtil;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;



public class ReceivablesA extends BaseActivity implements BDLocationListener {
    @InjectView(R.id.layout_pos)
    LinearLayout layout_pos;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.et_rechargeAmount)
    EditText etRechargeAmount;
    @InjectView(R.id.layout_pos)
    LinearLayout layoutPos;
    @InjectView(R.id.layout_receiptCode)
    LinearLayout layoutReceiptCode;
    @InjectView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @InjectView(R.id.tv_downloadCode)
    TextView tvDownloadCode;
    @InjectView(R.id.ll_QRcodeArea)
    LinearLayout llQRcodeArea;

    private String token;
    private String rechargeAmount;//收款金额
    private String posMinMount; //pos机充值最低限额
    private List<ConfigureBean.ListEntity> list ;
    private ImageRequest imageRequest;
    private Bitmap bitmap;
    private boolean codeClickTag = true;
    private LocationClient mLocationClient;
    private String country, city, province;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivables);
        ButterKnife.inject(this);
        initLocation();
        checkPermission(this);  //校验权限
        initData();

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

    private void initData() {
        InputFilter[] filters = {new CashierInputFilter()};
        etRechargeAmount.setFilters(filters);

        if(list !=null){
            list.clear();
        }
        list = (List<ConfigureBean.ListEntity>) ObjectSaveUtil.readObject(ReceivablesA.this,"ListEntity"); //配置集合
        if(list!=null) {
            for (ConfigureBean.ListEntity entity :
                    list) {
                if (entity.getEpos_min_amount() != null) {
                    posMinMount = entity.getEpos_min_amount();
                    break;
                }
            }
        }



    }
    private boolean isShowOrNot = false;
    @OnClick({R.id.iv_back, R.id.layout_pos, R.id.layout_receiptCode, R.id.tv_downloadCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(102);  //刷新余额
                finish();
                break;
            case R.id.layout_pos:
                rechargeAmount = etRechargeAmount.getText().toString();
                if (rechargeAmount.isEmpty() || rechargeAmount == null) {
                    Toast.makeText(ReceivablesA.this, "请输入收款金额", Toast.LENGTH_SHORT).show();
                    layoutPos.setEnabled(true);
                    return;
                }
                if ("0".equals(rechargeAmount) || "0.0".equals(rechargeAmount) || "0.00".equals(rechargeAmount)) {
                    Toast.makeText(ReceivablesA.this, "请输入大于0的金额", Toast.LENGTH_SHORT).show();
                    layoutPos.setEnabled(true);
                    return;
                }
                Double et_mount=Double.parseDouble(rechargeAmount);
                Double min_mount=Double.parseDouble(posMinMount);
                if(min_mount>et_mount){
                    Toast.makeText(this, "POS最低刷卡限额￥"+posMinMount, Toast.LENGTH_SHORT).show();
                    layoutPos.setEnabled(true);
                    return;
                }
                if (isLocationEnabled() == true) {
                    if (country == null || country.isEmpty()) {
                        ToastUtils.showLongToast(ReceivablesA.this, "位置信息获取失败，请检查位置服务是否打开或是否打开O计的定位权限！");
                        return;
                    } else {
                        if (!("中国".equals(country)) || "香港特别行政区".equals(city) || "澳门特别行政区".equals(city)
                                || "台湾省".equals(province)) {
                            ToastUtils.showLongToast(ReceivablesA.this, "您目前不在可使用地区，epos只限在中国大陆地区使用！");
                            return;
                        }
                        if(isShowOrNot == false){
                            layoutPos.setEnabled(false);
                            getToken();
                            isShowOrNot = true;
                            layout_pos.setBackgroundColor(getResources().getColor(R.color.gray));//设置按钮变成灰色
                            layout_pos.setOnClickListener(null);//设置不可点击
                        }else {
                            isShowOrNot  = false;
                        }
                    }
                }else {
                    ToastUtils.showToast(ReceivablesA.this, "需打开手机定位服务才能使用EPOS功能！");
                }

                break;
            case R.id.layout_receiptCode:
//                if (NotClickableUtil.isFastClick()) {
                    rechargeAmount = etRechargeAmount.getText().toString();
                    if (codeClickTag) {
                        llQRcodeArea.setVisibility(View.VISIBLE);
                        codeClickTag = false;
                        requestReceivableCode("acct/getPayCode1");
                    } else {
                        llQRcodeArea.setVisibility(View.GONE);
                        codeClickTag = true;
                    }
//                }
                break;
            case R.id.tv_downloadCode:
                requestReceivableCode2("acct/downLoadReceivableQr");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutPos.setEnabled(true);
    }

    //pos刷卡
    private void posPay() {
//        rechargeAmount = etRechargeAmount.getText().toString();
//
//        if (rechargeAmount.isEmpty() || rechargeAmount == null) {
//            Toast.makeText(ReceivablesA.this, "请输入收款金额", Toast.LENGTH_SHORT).show();
//            layoutPos.setEnabled(true);
//            return;
//        }
//
//        if ("0".equals(rechargeAmount) || "0.0".equals(rechargeAmount) || "0.00".equals(rechargeAmount)) {
//            Toast.makeText(ReceivablesA.this, "请输入大于0的金额", Toast.LENGTH_SHORT).show();
//            layoutPos.setEnabled(true);
//            return;
//        }
//
//        Double et_mount=Double.parseDouble(rechargeAmount);
//        Double min_mount=Double.parseDouble(posMinMount);
//
//        if(min_mount>et_mount){
//            Toast.makeText(this, "POS最低刷卡限额￥"+posMinMount, Toast.LENGTH_SHORT).show();
//            layoutPos.setEnabled(true);
//            return;
//        }

        cpd.show();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tradeName","Epos收款");  //交易名称
            jsonObject.put("tradeFee",rechargeAmount); // 交易金额
            jsonObject.put("tradeType","income"); //交易类型
            jsonObject.put("channelType","EPOSE");
            jsonObject.put("preventRepeatToken",token);//防重复请求token
            JsonObjectRequest jsonPOS = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "acct/createTrade ", jsonObject, new VolleyAbstract(this) {



                @Override
                public void volleyResponse(Object o) {

                }

                @Override
                public void volleyError(VolleyError volleyError) {
//					tv_firmRecharge.setEnabled(true);
                    Toast.makeText(ReceivablesA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected void netVolleyResponese(JSONObject json) {
                    //处理成功响应结果
                    //订单编号
                    String flowId = json.optString("flowId");
                    String cshnepos = json.optString("epos_mch_alias");
                    User loginResult = (User) ObjectSaveUtil.readObject(ReceivablesA.this, "loginResult");
                    String loginName = loginResult.getUserCode();	//用户名

                    ContentValues cv = new ContentValues();
                    cv.put("shopname", cshnepos);   //jhe     123456789012 生产   ,      app2       emp2  测试  ,       cshn      cshnepos
                    cv.put("account",  loginName);  //这里需要改成自己的 员工编号（用户substringId） TODO  Pos
                    loginSZ(cv,rechargeAmount, flowId);
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
            }, "receivablePosPay", false);

            volleyNetCommon.addQueue(jsonPOS);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void loginSZ(ContentValues cv, final String rechargeAmount , final String flowId) {
        cpd.show();
        MyLog.d("dhjt", "登录参数" + cv);
        DHJTManager.getInstance().loginSZ(cv, new ClientCallback() {

            @Override
            public void success(String result) {
                // TODO Auto-generated method stub
                //	Toast.makeText(RechargeA.this, "登录成功", Toast.LENGTH_SHORT).show();
                cpd.dismiss();
                //再加一个 运单号
                DHJTManager.getInstance().payInQuick(Action.PAYType_Card, flowId,rechargeAmount, new DHJTCallBack() {
                    public void querySuccess(String result) {
                        // TODO Auto-generated method stub
                        Log.i("lg", "查询成功返回值：" + result.toString());

                    }

                    public void queryFail(String msg) {
                        // TODO Auto-generated method stub
                        Log.i("lg", "查询失败返回值：" + msg);
//					tv_show.setText(msg);

                    }

                    String payStatus = "success";  //交易状态
                    @Override
                    public void onSuccess(Bundle bundle) {
//						tv_firmRecharge.setEnabled(true);
                        // TODO Auto-generated method stub
                        Log.i("lg", "查询成功返回值：" + bundle.toString());
                        payStatus = bundle.getString("payStatus");
                        if (payStatus != null) {
                            if (payStatus == "success") {
                                Intent intent = new Intent(ReceivablesA.this, PosSuccessA.class);
                                intent.putExtra("transactionStatus", "success");
                                intent.putExtra("rechargeAmount", rechargeAmount);
                                intent.putExtra("receivable","receivable");
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onNetError() {
//						tv_firmRecharge.setEnabled(true);
                        Toast.makeText(ReceivablesA.this, R.string.netError, Toast.LENGTH_LONG).show();
                    }

                    public void onFail(final Bundle bundle) {
//						tv_firmRecharge.setEnabled(true);
                        Log.i("aa",bundle.toString());
                        String error =	bundle.getString("msg");
                        if(error!=null) {
                            Intent intent = new Intent(ReceivablesA.this, PosSuccessA.class);
                            intent.putExtra("transactionStatus", "error");
                            intent.putExtra("rechargeAmount", rechargeAmount);
                            intent.putExtra("er",error);
                            intent.putExtra("flowId",flowId);
                            intent.putExtra("receivable","receivable");
                            startActivity(intent);
                            return;

                        }
                        //Bundle[{msg=用户取消交易, code=2}]
                    }
                });

            }

            @Override
            public void onNetError() {
//			tv_firmRecharge.setEnabled(true);
                cpd.dismiss();
                Toast.makeText(ReceivablesA.this, R.string.netError, Toast.LENGTH_LONG).show();
            }

            @Override
            public void fail(final String result) {
//				tv_firmRecharge.setEnabled(true);
                cpd.dismiss();
                Toast.makeText(ReceivablesA.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 获取Token
     */
    private void getToken() {
        String url = Define.URL + "fb/preventRepeatSubmit";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "receivablesToken", "0", true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "receivablesToken":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    token = jsonObject.getString("preventRepeatToken");
                    if (token != null){
                        SPUtils2.putString(ReceivablesA.this, "preventRepeatToken", token);

                        posPay();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void requestReceivableCode(String location){

        cpd.show();
        volleyNetCommon = new VolleyNetCommon();

        Map<String, String> map = new HashMap<>();
        map.put("fee", rechargeAmount);

        //当前线程是主线程
//获取图片
//错误处理
        //当前线程是主线程
//获取图片
//错误处理
        imageRequest = volleyNetCommon.imageRequest(Request.Method.POST, Define.URL + location, new VolleyAbstract(this) {
            @Override
            protected void netVolleyResponese(JSONObject json) {

            }

            @Override
            protected void PdDismiss() {

            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void volleyResponse(final Object o) {
                //当前线程是主线程

                bitmap = (Bitmap) o;
                //获取图片

                ivQrcode.setImageBitmap(bitmap);

                cpd.dismiss();
            }

            @Override
            public void volleyError(VolleyError volleyError) {

                //错误处理
                Toast.makeText(ReceivablesA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                cpd.dismiss();
            }
        }, 0, 0, "receivableCode", map);

        volleyNetCommon.addQueue(imageRequest);  //添加到队列
    }


    private void requestReceivableCode2(String location){

        cpd.show();
        volleyNetCommon = new VolleyNetCommon();

        //当前线程是主线程
//获取图片
//错误处理
        //当前线程是主线程
//获取图片
//错误处理
        imageRequest = volleyNetCommon.imageRequest(Define.URL + location, new VolleyAbstract(this) {
            @Override
            protected void netVolleyResponese(JSONObject json) {

            }

            @Override
            protected void PdDismiss() {

            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void volleyResponse(final Object o) {
                //当前线程是主线程

                bitmap = (Bitmap) o;
                //获取图片

                saveCodeImage(bitmap);


                cpd.dismiss();
            }

            @Override
            public void volleyError(VolleyError volleyError) {

                //错误处理
                Toast.makeText(ReceivablesA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                cpd.dismiss();
            }
        }, 0, 0, "receivableCode2");

        volleyNetCommon.addQueue(imageRequest);  //添加到队列
    }

    private void saveCodeImage(Bitmap bitmap) {

        FileOutputStream out = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {    // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            File file = new File(sdCardDir, System.currentTimeMillis() + ".jpg");// 在SDcard的目录下创建图片文,以当前时间为其命名

            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            MyApplication.mContext.sendBroadcast(intent);  //通知相册 有图片更新
            Toast.makeText(MyApplication.mContext, "二维码已经保存至" + Environment.getExternalStorageDirectory() + "/OC/" + "目录文件夹下", Toast.LENGTH_LONG).show();
            bitmap.recycle();
        } else {
            Toast.makeText(MyApplication.mContext, "未检测到sd卡", Toast.LENGTH_SHORT).show();
        }
    }


    /*
    权限校验
     */
    public void checkPermission(Activity activity) {

        if (Build.VERSION.SDK_INT >= 23) {

            int readSdcard = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int writeSdcard = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int mountUnmount = activity.checkSelfPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<>();

            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode = 1;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (writeSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode = 2;
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                activity.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }

        }


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

    public int permissionsFlag = 2;   //成功

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsFlag = 1;
                    Toast.makeText(this, "读取设备外部存储权限未授权！", Toast.LENGTH_SHORT).show();
                } else {
                    permissionsFlag = 2;
                    Toast.makeText(this, "恭喜！读取设备外部存储权限授权成功！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsFlag = 1;
                    Toast.makeText(this, "外部存储权限未授权！", Toast.LENGTH_SHORT).show();
                } else {
                    permissionsFlag = 2;
                    Toast.makeText(this, "恭喜！外部存储权限授权成功！", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    protected void onStart() {
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
        RequestQueue requestQueue = volleyNetCommon.getRequestQueue();
        if (requestQueue != null) {
            requestQueue.cancelAll("receivableCode");
            requestQueue.cancelAll("receivablePosPay");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    @Override
    protected boolean onkeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(102);  //返回更新余额数目
            finish();
        }
        return super.onkeyDown(keyCode, event);
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
}
