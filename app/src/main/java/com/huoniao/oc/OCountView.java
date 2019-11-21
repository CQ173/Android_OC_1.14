package com.huoniao.oc;

import java.lang.ref.WeakReference;

import com.huoniao.oc.user.RegisterA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class OCountView implements OCPageView{

	WeakReference<Activity> mParentActivity;
	View view;
	public OCountView(Activity act) {
		mParentActivity = new WeakReference<Activity>(act);
		view = mParentActivity.get().getLayoutInflater().inflate(R.layout.outlets_view_newoji, null);
		view.findViewById(R.id.iv_ticket).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mParentActivity.get(), RegisterA.class);
				mParentActivity.get().startActivity(intent);
			}
		});
	}

	@Override
	public View getView() {
		return view;
	}

	
}
