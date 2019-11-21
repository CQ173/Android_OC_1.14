package com.huoniao.oc.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.R;




/**
 * 
 * @author 雷鹏杰
 * 2016-11-30
 *	
 */
public class CustomProgressDialog extends ProgressDialog
{

	private AnimationDrawable mAnimation;
	// private Context mContext;
	private ImageView mImageView;
	private String mLoadingTip;
	private TextView mLoadingTv;
	private int mResid;

	public CustomProgressDialog(Context context, String content, int id)
	{
		super(context);
		// this.mContext = context;
		this.mLoadingTip = content;
		this.mResid = id;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData()
	{
		
		mImageView.setBackgroundResource(mResid);
		//设置逐帧动画的背景图
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// 防止只显示一帧的情况
		mImageView.post(new Runnable()
		{
			@Override
			public void run()
			{
				mAnimation.start();

			}
		});
		mLoadingTv.setText(mLoadingTip);

	}

	//设置加载文字
	public void setCustomPd(String message){
		if(mLoadingTv != null) {
			mLoadingTv.setText(message);
		}
	}



	private void initView()
	{
		setContentView(R.layout.progress_dialog);
		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		mImageView = (ImageView) findViewById(R.id.loadingIv);
	}

}
