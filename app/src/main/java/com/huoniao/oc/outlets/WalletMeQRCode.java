package com.huoniao.oc.outlets;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.custom.GlideRoundTransform;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.ShowPopupWindow;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/8.
 * 我的二维码
 */

public class WalletMeQRCode extends BaseActivity implements View.OnLongClickListener {


    @InjectView(R.id.qrcode_iv_back)
    ImageView qrcodeIvBack;   //返回键
    @InjectView(R.id.iv_three)
    ImageView ivThree;   //三点图形
    @InjectView(R.id.iv_qrcode)
    ImageView ivQrcode;  //二维码图片
    @InjectView(R.id.ll_layout)
    LinearLayout llLayout;
    @InjectView(R.id.iv_logo)
    ImageView ivLogo; //公司logo
    @InjectView(R.id.tv_outletsName)
    TextView tvOutletsName;   //代售点名
    /*    @InjectView(R.id.tv_userName)
        TextView tvUserName;  //用户名*/
    @InjectView(R.id.ll_save_code)
    LinearLayout llSaveCode;  //把布局保存图片
    @InjectView(R.id.iv_imgo)
    TextView ivImgo;   //只是作为显示隐藏
    @InjectView(R.id.iv_imgt)
    TextView ivImgt; //只是作为显示隐藏
    @InjectView(R.id.iv_imgr)
    TextView ivImgr; //只是作为显示隐藏
    private ShowPopupWindow showPopupWindow = null;   //显示   二维码页面的弹出框
    private Bitmap bitmap;


    private final int RQUESTCODE = 1;
    private ImageRequest imageRequest;
    private VolleyNetCommon volleyNetCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wallet_qrcode);
        ButterKnife.inject(this);

        initWight();
        checkPermission(this);  //校验权限
        initData();

    }

    private void initData() {

        cpd.show();
        getUserData();   //获取我的二维码 用户信息

        volleyNetCommon = new VolleyNetCommon();
        Map<String, String> map = new HashMap<>();
        //当前线程是主线程
//获取图片
//错误处理
        imageRequest = volleyNetCommon.imageRequest(Define.URL + "acct/getPayCode", new VolleyAbstract(this) {
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
                Toast.makeText(WalletMeQRCode.this, R.string.netError, Toast.LENGTH_SHORT).show();
                cpd.dismiss();
            }
        }, 0, 0, "qrcode");

        volleyNetCommon.addQueue(imageRequest);  //添加到队列


    }

    /**
     * 获取我的二维码用户信息
     */
    private void getUserData() {
        Object loginResult = ObjectSaveUtil.readObject(this, "loginResult");
        User user = (User) loginResult;
        tvOutletsName.setText(user.getOrgName());  //售票点
        // tvUserName.setText("用户名：" + user.getUserCode()); //用户名
    }


    /**
     * 控件操作
     */
    private void initWight() {
        ivQrcode.setOnLongClickListener(this);

        try {
            Glide.with(this).load(R.drawable.applogo).error(R.drawable.ic_launcher).transform(new GlideRoundTransform(this, 10)).into(ivLogo);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @OnClick({R.id.qrcode_iv_back, R.id.iv_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qrcode_iv_back:
                setResult(102); //返回更新账户余额
                finish();
                break;
            case R.id.iv_three:
                showSavePopwindow();
                break;
        }
    }

    private void showSavePopwindow() {

        if (showPopupWindow == null) {
            showPopupWindow = new ShowPopupWindow();
        }
        if (permissionsFlag == 2) {
           // showPopupWindow.showSavePopupWindow(ivThree, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, bitmap);
            showPopupWindow.showSavePopupWindow(ivThree, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, llSaveCode);
            setDemonstration(1);
        } else {
            setDemonstration(2);
            Toast.makeText(this, "不能保存图片，你把权限禁用了！", Toast.LENGTH_SHORT).show();
        }

        showPopupWindow.setShowDemonstrationListener(new ShowPopupWindow.ShowDemonstration() {
            @Override
            public void dismiss() {
                setDemonstration(2);
            }
        });
    }

    //是否显示操作演示进行保存图片
  public void  setDemonstration(int i){
   if(i==1) {
       ivImgo.setVisibility(View.VISIBLE);
       ivImgt.setVisibility(View.VISIBLE);
       ivImgr.setVisibility(View.VISIBLE);
   }else{
       ivImgo.setVisibility(View.GONE);
       ivImgt.setVisibility(View.GONE);
       ivImgr.setVisibility(View.GONE);
   }
  }


    //二维码长按保存到相册
    @Override
    public boolean onLongClick(View view) {

        //弹出popwindow 确认保存图片
        if (showPopupWindow == null) {
            showPopupWindow = new ShowPopupWindow();
        }
        if (bitmap != null) {
            if (permissionsFlag == 2) {
                // showPopupWindow.showPopuWindowcode(llLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, bitmap );
                showPopupWindow.showPopuWindowcode(llLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, llSaveCode);
                setDemonstration(1);
            } else {
                setDemonstration(2);
                Toast.makeText(this, "不能保存图片，你把权限禁用了！", Toast.LENGTH_SHORT).show();
            }
        }

        showPopupWindow.setShowDemonstrationListener(new ShowPopupWindow.ShowDemonstration() {
            @Override
            public void dismiss() {
                setDemonstration(2);
            }
        });
        return true;
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
            requestQueue.cancelAll("qrcode");
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
}
