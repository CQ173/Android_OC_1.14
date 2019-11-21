package com.huoniao.oc.outlets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.PaySystemBean;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.ViewHolder;

import android.Manifest.permission;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ExciptionPaySystemA extends BaseActivity implements OnClickListener{
	private ImageView iv_back, iv_dataChoice;
	private ListView oltPaySys_listView;
//	private TextView tv_data, tv_paysysOnMonth;
	private Intent intent;
	private List<PaySystemBean> mDatas;
	private PaySystemBean paySysBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_outlets_exciption_paysystem);
		initView();
		initData();
//		ExitApplication.getInstance().addActivity(this);
	}
	
	private void initData() {
		intent = getIntent();
		paySysBean = new PaySystemBean();
		mDatas = new ArrayList<PaySystemBean>();
		paySysBean.setDate(intent.getStringExtra("excDate"));
		paySysBean.setShouldAmount(intent.getStringExtra("excShouldAmount"));
		paySysBean.setAlreadyAmount(intent.getStringExtra("excAlreadyAmount"));
		paySysBean.setWithholdStatus(intent.getStringExtra("excWithholdStatus"));	
		mDatas.add(paySysBean);
		
		oltPaySys_listView.setAdapter(new CommonAdapter<PaySystemBean>(this, mDatas, R.layout.outlets_paysysdata_item) {

			@Override
			public void convert(ViewHolder holder, PaySystemBean paySystemBean) {
				if (!"".equals(paySystemBean) || paySystemBean != null) {
					holder.setText(R.id.tv_date, paySystemBean.getDate())
					  .setText(R.id.tv_shouldAmount, paySystemBean.getShouldAmount())
					  .setText(R.id.tv_alreadyAmount, paySystemBean.getAlreadyAmount())
					  .setText(R.id.tv_withholdStatus, paySystemBean.getWithholdStatus());
				
				}
				
			}
		});
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_dataChoice = (ImageView) findViewById(R.id.iv_dataChoice);
		oltPaySys_listView = (ListView) findViewById(R.id.oltPaySys_listView);
//		tv_data = (TextView) findViewById(R.id.tv_data);
//		tv_paysysOnMonth = (TextView) findViewById(R.id.tv_paysysOnMonth);
		iv_back.setOnClickListener(this);
//		iv_dataChoice.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		
//		case R.id.iv_dataChoice:
//			Calendar cal = Calendar.getInstance();
//			
//			DatePickerDialog mDialog = new DatePickerDialog(ExciptionPaySystemA.this, new OnDateSetListener() {
//				
//				@Override
//				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//					tv_data.setText(year + "-" + (monthOfYear + 1));
//					tv_paysysOnMonth.setText((monthOfYear + 1) + "月");
//					
//					
//				}
//			}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//
//				mDialog.show();  
//				//隐藏不想显示的子控件，这里隐藏日控件
//				DatePicker dp = findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());  
//				if (dp != null) {  
//				    ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);  
//				}   
//				/**
//				 * 设置可选日期范围
//				 * 
//				 */
////				dp = mDialog.getDatePicker();
////				//限制显示6个月的
////				dp.setMinDate(new Date().getTime() - (long)150 * 24 * 60 * 60 * 1000);
////
////				dp.setMaxDate(new Date().getTime());
//				
//			// 直接创建一个DatePickerDialog对话框实例，并将它显示出来
////			new DatePickerDialog(PaySystemA.this,
////					// 绑定监听器
////					new DatePickerDialog.OnDateSetListener() {
////
////						@Override
////						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
////
////							tv_data.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
////						}
////					}
////					// 设置初始日期
////					, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
//			break;
//			
//		default:
//			break;
//		}
//
//	}	
//	/**
//	 * 通过遍历方法查找DatePicker里的子控件：年、月、日
//	 * @param group
//	 * @return
//	 */
//	private DatePicker findDatePicker(ViewGroup group) {
//		if (group != null) {
//			for (int i = 0, j = group.getChildCount(); i < j; i++) {
//				View child = group.getChildAt(i);
//				if (child instanceof DatePicker) {
//					return (DatePicker) child;
//				} else if (child instanceof ViewGroup) {
//					DatePicker result = findDatePicker((ViewGroup) child);
//					if (result != null)
//						return result;
//				}
//			}
//		}
//		return null;
//	}
		                                                                                                                                           
		}
	}	
	
	
}
