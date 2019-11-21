package com.huoniao.oc.outlets;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PaymentCodeA extends BaseActivity {

    @InjectView(R.id.qrcode_iv_back)
    ImageView qrcodeIvBack;
    @InjectView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @InjectView(R.id.bt_downloadCode)
    Button btDownloadCode;

    private ImageRequest imageRequest;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_code);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        checkPermission(this);
        requestPaymentCode("acct/getPayCode2", "showCode");
    }

    @OnClick({R.id.qrcode_iv_back, R.id.bt_downloadCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qrcode_iv_back:
                finish();
                break;
            case R.id.bt_downloadCode:
                requestPaymentCode("acct/downLoadPayQr", "downloadCode");
                break;
        }
    }


    private void requestPaymentCode(String location, final String codeTag){

        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        Map<String, String> map = new HashMap<>();

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
                if ("showCode".equals(codeTag)) {
                    ivQrcode.setImageBitmap(bitmap);
                }else if ("downloadCode".equals(codeTag)) {
                    saveCodeImage(bitmap);
                }

                cpd.dismiss();
            }

            @Override
            public void volleyError(VolleyError volleyError) {

                //错误处理
                Toast.makeText(PaymentCodeA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                cpd.dismiss();
            }
        }, 0, 0, "paymentCode");

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
    protected void onStop() {
        super.onStop();
        RequestQueue requestQueue = volleyNetCommon.getRequestQueue();
        if (requestQueue != null) {
            requestQueue.cancelAll("paymentCode");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
