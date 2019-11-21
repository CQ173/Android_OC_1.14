package com.huoniao.oc.fragment.outlets;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.outlets.PaySystemA;
import com.huoniao.oc.user.HelpCenterA;
import com.huoniao.oc.util.Premission;


/**
 * A simple {@link Fragment} subclass.
 */
public class OutletsOjiF extends BaseFragment implements View.OnClickListener {


    private LinearLayout layout_oJiPaySys;
    private LinearLayout layout_contacts;
    private RelativeLayout rl_paySysSpecial;
    private RelativeLayout rl_contactsSpecial;
   private Intent intent;
    public OutletsOjiF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.outlets_view_newoji,null);
        layout_oJiPaySys = (LinearLayout) view.findViewById(R.id.layout_oJiPaySys); //汇缴
        layout_contacts = (LinearLayout) view.findViewById(R.id.layout_contacts); //往来
        rl_paySysSpecial = (RelativeLayout) view.findViewById(R.id.rl_paySysSpecial); //汇缴专题
        rl_contactsSpecial = (RelativeLayout) view.findViewById(R.id.rl_contactsSpecial);  //往来专题
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
    }

    private void initWidget() {
        layout_oJiPaySys.setOnClickListener(this);
        layout_contacts.setOnClickListener(this);
        rl_paySysSpecial.setOnClickListener(this);
        rl_contactsSpecial.setOnClickListener(this);
        setPremissionShowHideView(Premission.FB_PAYMENT_VIEW,layout_oJiPaySys);      //是否有显示汇缴数据菜单权限
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.layout_oJiPaySys:		  //代售点-O计-汇缴
                intent = new Intent(MyApplication.mContext, PaySystemA.class);
                intent.putExtra("type", "1");
                baseActivity.startActivityIntent(intent);
                break;
            //代售点-O计-往来
            case R.id.layout_contacts:
                Toast.makeText(MyApplication.mContext, "正在建设中!", Toast.LENGTH_SHORT).show();
                break;
            //代售点-O计-汇缴专题
            case R.id.rl_paySysSpecial:
                startActivityMethod(HelpCenterA.class);
                break;
            //代售点-O计-汇缴专题
            case R.id.rl_contactsSpecial:
                startActivityMethod(HelpCenterA.class);
                break;
        }
    }
}
