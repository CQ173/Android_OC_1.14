package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.user.HelpCenterA;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_learnMore;


/**
 * Created by Administrator on 2017/10/12.
 */

public class OpWithHoldA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(tv_learnMore)
    TextView tvLearnMore;
    @InjectView(R.id.bt_runOpening)
    Button btRunOpening;
    private Intent intent;
//    private List<BankCardBean> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openwithhold);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
//        intent = getIntent();
//        mList = (List<BankCardBean>) intent.getSerializableExtra("unCardList");//获取未签约银行卡列表

    }

    @OnClick({R.id.iv_back, tv_learnMore, R.id.bt_runOpening})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_learnMore:
              //  startActivityMethod(HelpCenterA.class);
                Intent intent = new Intent(OpWithHoldA.this,HelpCenterA.class);
                intent.putExtra("learn_more","learn_more");
                startActivityIntent(intent);
                break;
            case R.id.bt_runOpening:
                intent = new Intent(OpWithHoldA.this, OpWithHoldTwoA.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("unCardList",(Serializable)mList);//序列化,要注意转化(Serializable)
//                intent.putExtras(bundle);//发送数据
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }
}
