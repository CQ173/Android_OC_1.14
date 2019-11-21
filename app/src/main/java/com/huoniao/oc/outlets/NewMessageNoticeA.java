package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.ReceivieModeEvent;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SPUtils2;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.huoniao.oc.MyApplication.voiceRemindSwitch;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

/**
 * Created by Administrator on 2018/3/8.
 */

public class NewMessageNoticeA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.ll_receivingMode)
    LinearLayout llReceivingMode;
    @InjectView(R.id.iv_remindSwitch)
    ImageView ivRemindSwitch;
    @InjectView(R.id.tv_receiveMode)
    TextView tvReceiveMode;

    private String switchState = "";
    private boolean switchTag = true;
    private User user;
    private Intent intent;
    private String infoReceiveType;//接收方式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlets_newmessage);
        ButterKnife.inject(this);
        EventBus.getDefault().registerSticky(this);  //注册粘性事件  可以保证接收消息
        initData();
    }

    private void initData() {
        user = (User) readObject(NewMessageNoticeA.this, "loginResult");
        switchState = SPUtils2.getString(this, "switchState");
//        EventBus.getDefault().postSticky(new VoiceRemindEvent(switchState));
//        infoReceiveType = user.getInfoReceiveType();

        if ("open".equals(switchState)) {
            ivRemindSwitch.setImageResource(R.drawable.open);
            voiceRemindSwitch = true;//Myapplication里定义的开关状态
            switchTag = false;
        } else if ("close".equals(switchState)){
            ivRemindSwitch.setImageResource(R.drawable.off);
            voiceRemindSwitch = false;
            switchTag = true;
        }else {
            ivRemindSwitch.setImageResource(R.drawable.open);
            voiceRemindSwitch = true;//Myapplication里定义的开关状态
            switchTag = false;
        }

        getInfoReceiveType();

    }

    @OnClick({R.id.iv_back, R.id.ll_receivingMode, R.id.iv_remindSwitch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_receivingMode:
                intent = new Intent(NewMessageNoticeA.this, ReviseReceivModeA.class);
                intent.putExtra("infoReceiveType",infoReceiveType);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;

            case R.id.iv_remindSwitch:
                if (switchTag == true) {
                    ivRemindSwitch.setImageResource(R.drawable.open);
//                    XunFeiUtils.getInstance();
                    switchState = "open";
                    voiceRemindSwitch = true;
                    SPUtils2.putString(NewMessageNoticeA.this, "switchState", switchState);
                    switchTag = false;
                } else {
                    ivRemindSwitch.setImageResource(R.drawable.off);
//                    XunFeiUtils.getInstance().stop();
                    switchState = "close";
                    voiceRemindSwitch = false;
                    SPUtils2.putString(NewMessageNoticeA.this, "switchState", switchState);
                    switchTag = true;
                }
                break;
        }
    }

    /**
     * 获取用户信息接收类型
     */
    private void getInfoReceiveType() {
        String url = Define.URL + "user/getInfoReceiveType";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "getInfoReceiveType", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getInfoReceiveType":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    infoReceiveType = jsonObject.optString("infoReceiveType");
                    if ("0".equals(infoReceiveType)) {//0代表短信，1代表公众号
                        tvReceiveMode.setText("APP推送+短信");
                    }else if ("1".equals(infoReceiveType)){
                        tvReceiveMode.setText("APP推送+微信公众号");
                    }else if ("2".equals(infoReceiveType)){
                        tvReceiveMode.setText("不接收通知");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //运行在post运行所在的线程
    public void onEvent(ReceivieModeEvent event) {   //别的地方发送数据到这里  这里直接接受
        infoReceiveType = event.receivieMode;
        if ("0".equals(infoReceiveType)){
            tvReceiveMode.setText("APP推送+短信");
        }else if ("1".equals(infoReceiveType)){
            tvReceiveMode.setText("APP推送+微信公众号");
        }else if ("2".equals(infoReceiveType)){
            tvReceiveMode.setText("不接收通知");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //注销监听
    }
}
