package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.WithholdAccountInfoBean;
import com.huoniao.oc.util.Define;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;



/**
 * Created by Administrator on 2017/11/21.
 */

public class WithholdAccountInfoA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_outlet_name)
    TextView tvOutletName;
    @InjectView(R.id.tv_outlet_windowNumber)
    TextView tvOutletWindowNumber;
    @InjectView(R.id.tv_outlet_number)
    TextView tvOutletNumber;
    @InjectView(R.id.tv_outlet_city)
    TextView tvOutletCity;
    @InjectView(R.id.tv_outlet_corpName)
    TextView tvOutletCorpName;
    @InjectView(R.id.tv_outlet_corpMobile)
    TextView tvOutletCorpMobile;
    @InjectView(R.id.tv_outlet_idNumber)
    TextView tvOutletIdNumber;
    @InjectView(R.id.tv_outlet_master)
    TextView tvOutletMaster;
    @InjectView(R.id.tv_outlet_masterPhone)
    TextView tvOutletMasterPhone;
    @InjectView(R.id.tv_outlet_state)
    TextView tvOutletState;
    @InjectView(R.id.bt_windowAnchored)
    Button btWindowAnchored;
    private Intent intent;
    private String withholdAccount;//扣款账号
    private List<WithholdAccountInfoBean.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_outletsdetails2);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        tvTitle.setText("账号信息");
        intent = getIntent();
        withholdAccount = intent.getStringExtra("withholdAccount");
        requestAgencyList();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 请求扣款账号信息
     */
    private void requestAgencyList() {
        String url = Define.URL + "fb/agencyList";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeCode", withholdAccount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "withholdAccountInfo", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "withholdAccountInfo":
//                String jsonData = json.toString();
                Gson gson = new Gson();
                WithholdAccountInfoBean withholdAccountInfoBean = gson.fromJson(json.toString(), WithholdAccountInfoBean.class);
                data = withholdAccountInfoBean.getData();
//                WithholdAccountInfoBean.DataBean dataBean = new WithholdAccountInfoBean.DataBean();
                if (data.size() > 0) {
                    WithholdAccountInfoBean.DataBean dataBean = data.get(0);
                    String outletName = dataBean.getName();
                    if (outletName != null) {
                        tvOutletName.setText(outletName);
                    }
                    String outletAccount = dataBean.getCode();
                    if (outletAccount != null) {
                        tvOutletNumber.setText(outletAccount);
                    }
                    String windowNumber = dataBean.getWinNumber();
                    if (windowNumber != null) {
                        tvOutletWindowNumber.setText(windowNumber);
                    }

                    String area = dataBean.getArea().getName();
                    if (area != null) {
                        tvOutletCity.setText(area);
                    }
                    String corpName = dataBean.getCorpName();
                    if (corpName != null) {
                        tvOutletCorpName.setText(corpName);
                    }
                    String corpMobile = dataBean.getCorpMobile();
                    if (corpMobile != null) {
                        tvOutletCorpMobile.setText(corpMobile);
                    }

                    String corpIdNum = dataBean.getCorpIdNum();
                    if (corpIdNum != null) {
                        tvOutletIdNumber.setText(corpIdNum);
                    }
                    String master = dataBean.getMaster();
                    if (master != null) {
                        tvOutletMaster.setText(master);
                    }
                    String masterPhone = dataBean.getPhone();
                    if (masterPhone != null) {
                        tvOutletMasterPhone.setText(masterPhone);
                    }

                    String outletState = dataBean.getState();
                    if (outletState != null) {
                        if (Define.OUTLETS_NORMAL.equals(outletState)) {
                            tvOutletState.setText("正常");
                        }else if (Define.OUTLETS_PENDIG_AUDIT.equals(outletState)) {
                            tvOutletState.setText("待审核");
                        }else {
                            tvOutletState.setText("审核不通过");
                        }

                    }
                }
                break;
        }
    }
}
