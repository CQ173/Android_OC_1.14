package com.huoniao.oc.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.custom.Keyboard;
import com.huoniao.oc.custom.PayEditText;
import com.huoniao.oc.user.SettingPayPwd;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/8.
 */

public class ShowPopupWindow implements View.OnClickListener {

    private View popup_showcode_view = null;
    private PopupWindow popupWindow = null;
    private LinearLayout linearLayout;  //把布局保存图片起来

    private View popSaveView = null;
    private PopupWindow popSaveWindow = null;
    private Bitmap bitmap = null;
    private TextView tvShang;
    private int  permissionFlag ;
    private String capitalPassword;//资金密码
    private EditText et_capitalPassword;
    private LinearLayout layout_capitalPassword;



    public void showSavePopupWindow(View vv, int width, int height, final LinearLayout linearLayout) {
       this.linearLayout = linearLayout;
        this.permissionFlag = permissionFlag;
        this.bitmap = bitmap;
        if (popup_showcode_view == null) {

            popup_showcode_view = LayoutInflater.from(MyApplication.mContext).inflate(R.layout.code_popwindow, null);
        }

        if (popupWindow == null) {
            popupWindow = new PopupWindow(popup_showcode_view, width, height, true);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
        popup_showcode_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(showDemonstration!=null){
                    showDemonstration.dismiss();
                }
                popupWindow.dismiss();
                return false;
            }
        });

        TextView tvSaveCode = (TextView) popup_showcode_view.findViewById(R.id.tv_save_code);


        tvSaveCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (linearLayout != null) {
                 //   saveCodeImage(bitmap);   //保存二维码到相册
                    saveCodeImage(linearLayout);
                } else {
                    toast("获取二维码失败！不能保存 ！");
                }

                popupWindow.dismiss();
            }
        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });


//      在指定控件的上面
//        int[] location = new int[2];
//        vv.getLocationOnScreen(location);
        // popupWindow.showAtLocation(vv, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());
        //popupWindow.showAtLocation(vv, Gravity.CENTER,0,0);


        //在指定控件的下面
        popupWindow.showAsDropDown(vv);

        //在指定控件的左面
//        popupWindow.showAtLocation(vv, Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth(), location[1]);


        //在指定控件的右面
//        popupWindow.showAtLocation(vv, Gravity.NO_GRAVITY, location[0]+vv.getWidth(), location[1]);


//        popupWindow.showAtLocation(vv, Gravity.CENTER,0,0);
    }


    public void showPopuWindowcode(View view, int width, int height, Bitmap bitmap) {
        this.bitmap = bitmap;
        setPopWindowCodeImage(view, width, height);

    }

    //把布局保存为图片
     public void showPopuWindowcode(View view,int width,int height,LinearLayout linearLayout){
         this.linearLayout = linearLayout;
         setPopWindowCodeImage(view, width, height);
     }
    //设置显示是否弹出保存popwindow弹出框
    private void setPopWindowCodeImage(View view, int width, int height) {
        if (popSaveView == null) {
            popSaveView = LayoutInflater.from(MyApplication.mContext).inflate(R.layout.pop_save_view, null);
        }

        if (popSaveWindow == null) {
            popSaveWindow = new PopupWindow(popSaveView, width, height, true);
            popSaveWindow.setFocusable(true);
            popSaveWindow.setOutsideTouchable(true);
            popSaveWindow.setClippingEnabled(false);  //设置覆盖状态栏
        }

        popSaveView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(showDemonstration!=null){
                    showDemonstration.dismiss();
                }
                popSaveWindow.dismiss();
                return false;
            }
        });


        popSaveWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popSaveWindow.dismiss();
            }
        });


        TextView tvSaveCodePhoto = (TextView) popSaveView.findViewById(R.id.tv_save_codephoto);
        TextView tvCancleCodePhoto = (TextView) popSaveView.findViewById(R.id.tv_cancle_codephoto);

        tvSaveCodePhoto.setOnClickListener(this);

        tvCancleCodePhoto.setOnClickListener(this);


        //在指定控件的下面
        popSaveWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private int flag = 1;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save_codephoto:
                if (linearLayout != null) {
                    //   做保存二维码图片处理
                  //  saveCodeImage(bitmap);
                    saveCodeImage(linearLayout);
                }

                if(bitmap != null){
                    saveCodeImage(bitmap);
                }

                popSaveWindow.dismiss();
                break;
            case R.id.tv_cancle_codephoto:
               if( showDemonstration!=null){
                   showDemonstration.dismiss();
               }
               popSaveWindow.dismiss();
                break;
            case R.id.tv_confirm:     // 退款 确定按钮操作
               if(flag==1) {
                   if (popRefund != null) {
                       capitalPassword = et_capitalPassword.getText().toString();
                       if (capitalPassword != null && !capitalPassword.isEmpty()){
                           popRefund.confirm(capitalPassword);  //null表示不传参
                       }else {
                           Toast.makeText(MyApplication.mContext, "请输入支付密码！", Toast.LENGTH_SHORT).show();

                       }

                   }
               }
                if(flag==2){
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            flag = 1;
                            refundPopWindow.dismiss();
                        }
                    });

                }
                 flag = 2;
                 //是否退款确认
                break;

            case R.id.tv_cancle:      //退款 返回  按钮
                if(popRefund !=null){
                    flag = 1;
                    popRefund.cancle(null);
                }
                refundPopWindow.dismiss();
                //取消
                break;
        }
    }

    //点击确定按钮后  提交申请 给用户提示文字

    public void setTvShang(String s){
        tvShang.setText(s);
        layout_capitalPassword.setVisibility(View.GONE);
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
                Toast.makeText(MyApplication.mContext, "保存已经至" + Environment.getExternalStorageDirectory() + "/OC/" + "目录文件夹下", Toast.LENGTH_LONG).show();
                bitmap.recycle();
            } else {
                Toast.makeText(MyApplication.mContext, "未检测到sd卡", Toast.LENGTH_SHORT).show();
            }
        }



    private void saveCodeImage(final LinearLayout linearLayout){


            // 获取布局图片
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.buildDrawingCache();

        ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                // 要在运行在子线程中
                final Bitmap bmp = linearLayout.getDrawingCache(); // 获取图片
                Random random = new Random();
                int i = random.nextInt();
               Compres compres = new Compres();
                Bitmap bitmap = compres.decodeSampledBitmapFromBitmap(bmp, 302, 457);
                saveImage(bmp, "code"+i+".jpg");// 保存图片
                linearLayout.destroyDrawingCache(); // 保存过后释放资源
            }
          });
        }

    //把布局保存为图片
        public void saveImage(Bitmap bm, String fileName) {
            if (null == bm) {
                return;
            }

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
            {
                File foder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/OC");
                if (!foder.exists()) {
                    foder.mkdirs();
                }
                File myCaptureFile = new File(foder, fileName);
                try {
                    if (!myCaptureFile.exists()) {
                        myCaptureFile.createNewFile();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    //压缩保存到本地
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    toast("图片保存失败");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(showDemonstration!=null){
                                showDemonstration.dismiss();
                            }
                        }
                    });

                }
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(myCaptureFile);
                intent.setData(uri);
                MyApplication.mContext.sendBroadcast(intent);  //通知相册 有图片更新
                toast( "保存已经至" + Environment.getExternalStorageDirectory() + "/OC/" + "目录文件夹下");
           //     Toast.makeText(MyApplication.mContext, "保存已经至" + Environment.getExternalStorageDirectory() + "/OC/" + "目录文件夹下", Toast.LENGTH_LONG).show();

            } else{
                toast("未检测到sd卡");
            }
            ThreadCommonUtils.runonuiThread(new Runnable() {
                @Override
                public void run() {
                    if(showDemonstration!=null){
                        showDemonstration.dismiss();
                    }
                }
            });

        }


    private   PopupWindow  refundPopWindow = null;
    private   View refundPopView = null;
   //弹退款框
    public void showPopuWindowRefund(View view, int width, int height) {

        if (refundPopView == null) {
            refundPopView = LayoutInflater.from(MyApplication.mContext).inflate(R.layout.refund_pop_layout, null);
        }

        if (refundPopWindow == null) {
            refundPopWindow = new PopupWindow(refundPopView, width, height, true);
            refundPopWindow.setFocusable(true);
            refundPopWindow.setOutsideTouchable(false);
            refundPopWindow.setClippingEnabled(false);  //设置覆盖状态栏


        }

       /* refundPopView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                refundPopWindow.dismiss();
                return false;
            }
        });*/


        refundPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                refundPopWindow.dismiss();
            }
        });


        tvShang = (TextView) refundPopView.findViewById(R.id.tv_shang);
        et_capitalPassword = (EditText) refundPopView.findViewById(R.id.et_capitalPassword); //输入资金密码
        layout_capitalPassword = (LinearLayout) refundPopView.findViewById(R.id.layout_capitalPassword);

        TextView tvConfirm = (TextView) refundPopView.findViewById(R.id.tv_confirm); //确认退款
        TextView tvCancle = (TextView) refundPopView.findViewById(R.id.tv_cancle); //返回
         TextView tv_paypwd =  (TextView) refundPopView.findViewById(R.id.tv_paypwd);  //设置支付密码 在没有设置的情况下设置

        tvConfirm.setOnClickListener(this);

        tvCancle.setOnClickListener(this);


        //在指定控件的下面
        refundPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }



    public PopupWindow getRefundPopWindow(){
        if(refundPopWindow != null){
            return refundPopWindow;
        }
        return null;
    }


    // 支付密码框
    public void  showPopWindowPwd(View view, int width, int height,String momery){
         final String[] KEY = new String[] {
                "1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "<<", "0", "完成"
        };

       final PayEditText payEditText;
       Keyboard keyboard;

            refundPopView = LayoutInflater.from(MyApplication.mContext).inflate(R.layout.textpwd_pop, null);
            refundPopWindow = new PopupWindow(refundPopView, width, height, true);
            refundPopWindow.setFocusable(true);
            refundPopWindow.setOutsideTouchable(false);
            refundPopWindow.setClippingEnabled(false);  //设置覆盖状态栏




        refundPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                refundPopWindow.dismiss();
            }
        });


          payEditText = (PayEditText)refundPopView.findViewById(R.id.PayEditText_pay);
          keyboard = (Keyboard)refundPopView.findViewById(R.id.KeyboardView_pay);
          ImageView  ivFinish = (ImageView) refundPopView.findViewById(R.id.iv_deletepwd); //关闭弹出页
          TextView tvMomery = (TextView) refundPopView.findViewById(R.id.tv_memory);  //要支付的金额

         TextView tv_paypwd =(TextView)refundPopView.findViewById(R.id.tv_paypwd); //设置支付密码  如果设置了就隐藏
          if(MyApplication.payPasswordIsEmpty){ //如果等于true 表示没有设置过密码这个时候就要显示出来
               tv_paypwd.setVisibility(View.VISIBLE);
             tv_paypwd.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     ThreadCommonUtils.runonuiThread(new Runnable() {
                         @Override
                         public void run() {
                             MyApplication.mContext.startActivity(new Intent(MyApplication.mContext, SettingPayPwd.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //跳转设置支付密码
                             refundPopWindow.dismiss();
                         }
                     });
                 }
             });
          }

        //显示要支付的金额
         tvMomery.setText("￥"+momery);

        //设置键盘
        keyboard.setKeyboardKeys(KEY);

        keyboard.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    payEditText.add(value);
                } else if (position == 9) {
                    payEditText.remove();
                }else if (position == 11) {
                    //当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
                   // Toast.makeText(MyApplication.mContext, "您的密码是：" + payEditText.getText(), Toast.LENGTH_SHORT).show();
                   if(payEditText.getText().toString().trim().length()<6){
                       Toast.makeText(MyApplication.mContext, "密码长度不够，请继续输入。", Toast.LENGTH_SHORT).show();
                   return;
                   }
                    if(popRefund != null) {
                        popRefund.confirm(payEditText.getText());
                    }
                 //   finish();
                    refundPopWindow.dismiss();
                }
            }
        });

        /**
         * 当密码输入完成时的回调
         */
        payEditText.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
               // Toast.makeText(MyApplication.mContext, "您的密码是：" + password, Toast.LENGTH_SHORT).show();
               if(popRefund != null) {
                   popRefund.confirm(password);
               }
                refundPopWindow.dismiss();
            }
        });



        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refundPopWindow.dismiss();
            }
        });

        //在指定控件的下面
        refundPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }



    public void toast(final String text) {
        ThreadCommonUtils.runonuiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.mContext, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //回调是否点击确定按钮或者取消按钮  做相应的操作
    private  PopRefund popRefund;
    public void setPopRefundListener(PopRefund popRefund){
        this.popRefund = popRefund;
    }
    public interface PopRefund{
        void confirm(Object object);
        void cancle(Object object);
    }


    private ShowDemonstration showDemonstration;
    public void setShowDemonstrationListener(ShowDemonstration showDemonstrationListener){
        this.showDemonstration = showDemonstrationListener;
    }
    public interface ShowDemonstration{
        void dismiss();
    }
}
