package com.huoniao.oc;

import android.os.Build;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;


/**
 * Created by Administrator on 2017/6/22.
 */

 class MyExceptionHandler implements UncaughtExceptionHandler{
    //当发现了未捕获异常的时候调用的方法
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println("发生了异常，捕获了。。。");
        StringBuffer sb = new StringBuffer();
        User user = (User)ObjectSaveUtil.readObject(MyApplication.mContext, "loginResult");
        sb.append("userName："+user.getUserCode()+"\n");
        sb.append("time:");
        String s=  DateUtils.getFormatedDateTime(System.currentTimeMillis());
        sb.append(s+"\n");
        Field[] fields = Build.class.getDeclaredFields();
        for(Field field:fields){
            try {
                String value = (String) field.get(null);
                String name = field.getName();
                sb.append(name+"="+value+"\n");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        sb.append(sw.toString());
        try {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存
            File file = new File(sdCardDir,  "error.log");// 在SDcard的目录下创建图片文

            FileOutputStream fos = new FileOutputStream(file,true);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            uploadError(sb);  //上传错误app奔溃日志
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void uploadError(StringBuffer sb) throws Exception {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errInfo",sb.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Define.URL + "user/uploadErrLog";


        VolleyNetCommon volleyNetCommon = new VolleyNetCommon();
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest2(Request.Method.POST, url, jsonObject, "aa");
        volleyNetCommon.addQueue(jsonObjectRequest);

     /*   Intent intent = new Intent(MyApplication.mContext.getApplicationContext(), LoginA.class);
        AlarmManager mAlarmManager = (AlarmManager) MyApplication.mContext.getSystemService(Context.ALARM_SERVICE);
        //重启应用，得使用PendingIntent
        PendingIntent restartIntent = PendingIntent.getActivity(
                MyApplication.mContext.getApplicationContext(), 0, intent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis(),
                restartIntent); // 重启应用*/

        MyApplication.exit(1);
      /*  //自杀的方法
        ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                  SystemClock.sleep(5000);
           //       System.exit(1);  //异常退出传1

            }
        });*/

    }
}
