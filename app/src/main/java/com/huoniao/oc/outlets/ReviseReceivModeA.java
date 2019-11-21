package com.huoniao.oc.outlets;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.BindPublicNumBean;
import com.huoniao.oc.bean.ReceivieModeEvent;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2018/3/8.
 */

public class ReviseReceivModeA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.rb_pushAddMsg)
    RadioButton rbPushAddMsg;
    @InjectView(R.id.rb_pushAddPublicNum)
    RadioButton rbPushAddPublicNum;
    @InjectView(R.id.bt_saveSetting)
    Button btSaveSetting;
    @InjectView(R.id.bt_cancel)
    Button btCancel;
    @InjectView(R.id.ll_operationButton)
    LinearLayout llOperationButton;
    @InjectView(R.id.radioGroup)
    RadioGroup radioGroup;
    @InjectView(R.id.mListView)
    ListView mListView;
    @InjectView(R.id.rb_noNotificationReceived)
    RadioButton rbNoNotificationReceived;

    private MyPopWindow myPopWindow;
    private String infoReceiveType;//接收方式
    private String infoReceiveTypeConfim;//保存设置后的接收方式
    private Intent intent;
    private CommonAdapter<BindPublicNumBean.DataBean> commonAdapter;
    private String wechatPublicNoticeId;//绑定列表的条目id
    private String operationTag;//不同情况提示框tag
    private List<BindPublicNumBean.DataBean> data;
    private List<BindPublicNumBean.DataBean> myDatas = new ArrayList<>();
    private ImageView headimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlets_revisereceivmode);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        intent = getIntent();
        infoReceiveType = intent.getStringExtra("infoReceiveType");
        infoReceiveTypeConfim = intent.getStringExtra("infoReceiveType");
        if ("0".equals(infoReceiveType)) {//0代表短信，1代表公众号
            rbPushAddMsg.setChecked(true);
            rbPushAddPublicNum.setChecked(false);
            rbNoNotificationReceived.setChecked(false);
        } else if ("1".equals(infoReceiveType)) {
            rbPushAddMsg.setChecked(false);
            rbPushAddPublicNum.setChecked(true);
            rbNoNotificationReceived.setChecked(false);
        } else if ("2".equals(infoReceiveType)) {
            rbPushAddMsg.setChecked(false);
            rbPushAddPublicNum.setChecked(false);
            rbNoNotificationReceived.setChecked(true);
        }

        getWechatPublicNoticeList();
    }

    @OnClick({R.id.iv_back, R.id.bt_saveSetting, R.id.bt_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_saveSetting:
                if (rbPushAddMsg.isChecked()) {
                    infoReceiveType = "0";
                } else if (rbPushAddPublicNum.isChecked()) {
                    infoReceiveType = "1";
                } else if (rbNoNotificationReceived.isChecked()) {
                    infoReceiveType = "2";
                }
                operationTag = "saveSetting";

                if ("1".equals(infoReceiveType) && "saveSetting".equals(operationTag) && myDatas.size() == 0) {
                    setMyPopWindowShow2();
                } else {
                    setInfoReceiveType();
                }


                break;

            case R.id.bt_cancel:
                finish();
                break;
        }
    }

    @Override
    protected boolean onkeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myPopWindow != null && myPopWindow.isShow()) {
                return false;
            }
        }
        return super.onkeyDown(keyCode, event);


    }

    /**
     * 获取绑定微信公众号列表
     */
    private void getWechatPublicNoticeList() {
        String url = Define.URL + "user/getWechatPublicNoticeList";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "getWechatPublicNoticeList", "", true, true);
    }

    /**
     * 请求设置信息接收类型
     */
    private void setInfoReceiveType() {
        String url = Define.URL + "user/setInfoReceiveType";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("infoReceiveType", infoReceiveType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "setInfoReceiveType", "", true, true);
    }

    /**
     * 请求解除绑定
     */
    private void removeWechatPublicNotice() {
        String url = Define.URL + "user/removeWechatPublicNotice";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wechatPublicNoticeId", wechatPublicNoticeId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "removeWechatPublicNotice", "", true, true);
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "setInfoReceiveType":

                EventBus.getDefault().postSticky(new ReceivieModeEvent(infoReceiveType));
                //设置成功后弹出提示框
                setMyPopWindowShow();

                break;

            case "getWechatPublicNoticeList":
                setAdapter(json);
                break;

            case "removeWechatPublicNotice":


                JSONObject jsonObject = null;
                try {
                    jsonObject = json.getJSONObject("data");
                    String result = jsonObject.getString("result");
                    if ("success".equals(result)) {
                        if (myDatas != null) {
                            myDatas.clear();
                        }
                        //解除绑定成功后刷新列表
                        getWechatPublicNoticeList();

                    } else {
                        String msg = jsonObject.optString("msg");
                        ToastUtils.showLongToast(ReviseReceivModeA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    private void setAdapter(JSONObject jsonObject) {
        Gson gson = new Gson();
        BindPublicNumBean bindPublicNumBean = gson.fromJson(jsonObject.toString(), BindPublicNumBean.class);
        data = bindPublicNumBean.getData();
        myDatas.addAll(data);

        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<BindPublicNumBean.DataBean>(ReviseReceivModeA.this, myDatas, R.layout.item_bindpublicnum_list) {
                @Override
                public void convert(ViewHolder holder, final BindPublicNumBean.DataBean dataBean) {
                    holder.setText(R.id.tv_nickName, dataBean.getNickName());
                    headimg = holder.getView(R.id.headimg);
                    String headimgUrl = dataBean.getHeadimgUrl() == null ? "" : dataBean.getHeadimgUrl();


                    if (!headimgUrl.isEmpty()) {
                        glideSetImg(headimgUrl, headimg);
//                        Glide.with(ReviseReceivModeA.this).load(headimgUrl).into(headimg);

                    } else {
                        headimg.setImageResource(R.drawable.applogo);
                    }

                    TextView tv_unBind = holder.getView(R.id.tv_unBind);
                    tv_unBind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            wechatPublicNoticeId = dataBean.getId();

                            if (myDatas.size() > 1) {//如果绑定的微信号数量大于1就请求解绑
                                removeWechatPublicNotice();
                            } else {//否则弹出解除绑定的提示
                                if ("0".equals(infoReceiveTypeConfim)) {//如果只有一个以下绑定的微信号，那除了选项是微信接收方式的不能解除绑定，其它的都能解除
                                    removeWechatPublicNotice();
                                }else if ("2".equals(infoReceiveTypeConfim)){
                                    removeWechatPublicNotice();
                                }else if ("1".equals(infoReceiveTypeConfim)){
                                    operationTag = "unBind";
                                    setMyPopWindowShow();
                                }

                            }


                        }
                    });
                }
            };

            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.refreshData(myDatas);


    }

    private void setMyPopWindowShow() {
        if (!RepeatClickUtils.repeatClick()) {
            return;
        }
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }

        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                ImageView iv_promptIcon = (ImageView) view.findViewById(R.id.iv_promptIcon);
                TextView tv_promptContent = (TextView) view.findViewById(R.id.tv_promptContent);
                Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
                if ("0".equals(infoReceiveType) && "saveSetting".equals(operationTag)) {
                    infoReceiveTypeConfim = "0";
                    iv_promptIcon.setImageResource(R.drawable.sign_success);
                    tv_promptContent.setText("设置成功");
                    btn_confirm.setText("确定");
                }

                if ("1".equals(infoReceiveType) && "saveSetting".equals(operationTag) && myDatas.size() != 0) {
                    infoReceiveTypeConfim = "1";
                    iv_promptIcon.setImageResource(R.drawable.sign_success);
                    tv_promptContent.setText("设置成功");
                    btn_confirm.setText("确定");
                }

                if ("2".equals(infoReceiveType) && "saveSetting".equals(operationTag)) {
                    infoReceiveTypeConfim = "2";
                    iv_promptIcon.setImageResource(R.drawable.sign_success);
                    tv_promptContent.setText("设置成功");
                    btn_confirm.setText("确定");
                }

                if ("unBind".equals(operationTag)) {
                    iv_promptIcon.setImageResource(R.drawable.prompt_circle);
                    tv_promptContent.setText("请至少绑定一个用于接收O计平台通知的微信账号");
                    btn_confirm.setText("我知道了");
                }

                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();

                    }
                });
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.pop_revise_receiv;
            }
        }.poPwindow(ReviseReceivModeA.this, false).showAtLocation(ivBack, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    /**
     * 要设置微信公众号接收方式却没绑定一个微信号的调用
     */
    private void setMyPopWindowShow2() {
        if (!RepeatClickUtils.repeatClick()) {
            return;
        }
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }

        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                ImageView iv_promptIcon = (ImageView) view.findViewById(R.id.iv_promptIcon);
                TextView tv_promptContent = (TextView) view.findViewById(R.id.tv_promptContent);
                Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);

                iv_promptIcon.setImageResource(R.drawable.prompt_circle);
                tv_promptContent.setText("请到O计微信公众号或网页端绑定接收信息的微信号");
                btn_confirm.setText("我知道了");


                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();

                    }
                });
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.pop_revise_receiv;
            }
        }.poPwindow(ReviseReceivModeA.this, false).showAtLocation(ivBack, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 通过Glide设置图片到控件上
     */
    private void glideSetImg(String url, final ImageView imageView) {
        //设置圆角图片
        Glide.with(ReviseReceivModeA.this).load(url).asBitmap().centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
        //设置普通图片
       /* try {
            Glide.with(ReviseReceivModeA.this).load(file).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}
