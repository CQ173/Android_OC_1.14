package com.huoniao.oc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.FileUtils;


public class StartPagerA extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 2000; //延迟2秒
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppBaseTheme);
		super.onCreate(savedInstanceState);
		setConfigure();
		FileUtils.deleteFileCacle();//清除缓存图片
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_startpager);
		 new Handler().postDelayed(new Runnable(){   
			    
	         @Override   
	         public void run() {   
	             Intent mainIntent = new Intent(StartPagerA.this,LoginA.class);   
	             StartPagerA.this.startActivity(mainIntent);   
	             overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	             StartPagerA.this.finish();   
	         }   
	              
	        }, SPLASH_DISPLAY_LENGHT);   
		 
		 ExitApplication.getInstance().addActivity(this);

	}

	private void setConfigure() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题
		setRequestedOrientation(ActivityInfo
				.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
	}
}
