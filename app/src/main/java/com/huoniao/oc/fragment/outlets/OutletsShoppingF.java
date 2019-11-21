package com.huoniao.oc.fragment.outlets;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.custom.WebViewScollview;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.WebviewSetting;
import com.umeng.analytics.MobclickAgent;

import static com.umeng.analytics.MobclickAgent.onProfileSignOff;


/**
 * A simple {@link Fragment} subclass.
 */
public class OutletsShoppingF extends BaseFragment {


    private ProgressBar pbShopping;
    private WebViewScollview wvShopping;

    public OutletsShoppingF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view  = inflater.inflate(R.layout.outlets_view_shopping,null);
        pbShopping = (ProgressBar) view.findViewById(R.id.pb_shopping);
        wvShopping = (WebViewScollview) view.findViewById(R.id.wv_shopping);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //伙伴商城
        WebviewSetting.setShopping(wvShopping,pbShopping, Define.SHOPPING);
        wvShopping.setOnScrollChangeListener(new WebViewScollview.OnScrollChangeListener() {
            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {
                Log.e("已经到达底端",""+t);
            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {
                Log.e("已经到达顶端",""+t);
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                Log.e("一直在变化",t+"");

            }
        });
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private long mExitTime;
    public  boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if(wvShopping.canGoBack()) {
                wvShopping.goBack();
            }else{
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(MyApplication.mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                }else {
                    ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
                        @Override
                        public void run() {
                            onProfileSignOff();  //友盟账号退出登录
                            MobclickAgent.onKillProcess(MyApplication.mContext); //友盟退出登录用来保存统计数据。
                        }
                    });

                    MyApplication.exit(0);
                }
            }
        }
        return true ;
    }




}
