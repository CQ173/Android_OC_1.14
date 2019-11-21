package com.huoniao.oc.admin;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.adapter.SimpleTreeListViewAdapter;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.bean.NodeMap;
import com.huoniao.oc.bean.TreeBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.common.tree.adapter.TreeListViewAdapter;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SpinerPopWindow;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.MyApplication.treeIdList;


public class UserManageA extends BaseActivity implements OnClickListener, MySpinerAdapter.IOnItemSelectListener{
	private ImageView iv_back;
	private ListView userListView;
	private PullToRefreshListView mPullRefreshListView;
	private List<User> mDatas = new ArrayList<>();
	CommonAdapter<User> adapter;
	private boolean refreshClose = true;//标记是否还有数据可加载
	private String auditState = "";//审核状态
	private String searchContent = "";//查询内容:用户名/角色/姓名
	private String officeId = "";//所属机构 ID
	private	 VolleyNetCommon volleyNetCommon ;
//	private CommonAdapter<User> adapter;
	private TextView search_area;
	private EditText et_searchContent;
	private TextView tv_searchStatus;

	private TextView tv_select;
	private SpinerPopWindow mSpinerPopWindow;
	private List<TreeBean> trainOwnershipList;
	private SimpleTreeListViewAdapter simpleTreeListViewAdapter;
	private ListView lv_train_ownership;
	private List<String> auditStateList = new ArrayList<String>();
	private String CHOICE_TAG;//
	private MyPopWindow myPopWindow;  //火车站归属机构
	private Intent intent;
//	private ProgressDialog pd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_usermanage);
		initView();
		initData();
	}
	
	@SuppressWarnings("unchecked")
	private void initData() {
//		mDatas = new ArrayList<User>();
		auditStateList.add("审核通过");
//		identityTypeList.add("火车站");
		auditStateList.add("待审核");
		auditStateList.add("审核不通过");

//		auditStateList.add("补充资料待审核");


		try {
			getUserManageList(officeId, searchContent, auditState, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		intent = getIntent();
//		mDatas = (List<User>) intent.getSerializableExtra("userList");
//		adapter = new CommonAdapter<User>(this, mDatas, R.activity_windowanchored.admin_usermanage_item) {
//			
//			@Override
//			public void convert(ViewHolder holder, User user) {
//				holder.setText(R.id.tv_homeInstitution, user.getOrgName())
//				  	  .setText(R.id.tv_name, user.getName())
//					  .setText(R.id.tv_role, user.getRoleNames())
//					  .setText(R.id.tv_auditingStatus, user.getRoleNames());
//					  
//				
//				Log.d("user", "name =" + user.getName());
//				
//			}
//		};
//		
//		userListView.setAdapter(adapter);
		getTrainMapData();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		search_area = (TextView) findViewById(R.id.search_area);
		et_searchContent = (EditText) findViewById(R.id.et_searchContent);
		tv_searchStatus = (TextView) findViewById(R.id.tv_searchStatus);
		tv_select = (TextView) findViewById(R.id.tv_select);
//		userListView = (ListView) findViewById(R.id.userList);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.userList);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
		userListView = mPullRefreshListView.getRefreshableView();
		iv_back.setOnClickListener(this);
		search_area.setOnClickListener(this);
		tv_searchStatus.setOnClickListener(this);
		tv_select.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		case R.id.search_area:
			myPopWindow = new MyPopAbstract() {
				@Override
				protected void setMapSettingViewWidget(View view) {
					lv_train_ownership = (ListView) view.findViewById(R.id.lv_train_ownership);
					setTrainOwnershipData();
					ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
					iv_close.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							myPopWindow.dissmiss();
						}
					});

				}
				@Override
				protected int layout() {
					return R.layout.train_ownership_institution_pop;
				}
			}.poPwindow(UserManageA.this,true).showAtLocation(iv_back, Gravity.CENTER,0,0);
			break;
		case R.id.tv_searchStatus:

			mSpinerPopWindow = new SpinerPopWindow(this);
			CHOICE_TAG = "1";
			mSpinerPopWindow.refreshData(auditStateList, 0);
			mSpinerPopWindow.setItemListener(this);
			showSpinWindow(tv_searchStatus);
			break;

		case R.id.tv_select:
			searchContent = et_searchContent.getText().toString();
			try {
				if(mDatas != null){
					mDatas.clear();
				}
				getUserManageList(officeId, searchContent, auditState, 1);
//						pageNumber = 1;
				refreshClose = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		
	}


	private void showSpinWindow(View v) {
		Log.e("", "showSpinWindow");
		mSpinerPopWindow.setWidth(v.getWidth());
		mSpinerPopWindow.showAsDropDown(v);
	}

	
	/**
	 * 获取用户列表信息
	 * 
	 * @throws Exception
	 */
	private void getUserManageList(String officeId, String content, String auditState, final int pageNo) throws Exception {
//		pd = new CustomProgressDialog(UserManageA.this, "正在加载中...", R.anim.frame_anim);
		cpd.show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("officeId", officeId);
			jsonObject.put("str", content);
			jsonObject.put("auditState", auditState);
			jsonObject.put("pageNo", pageNo);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String url = Define.URL + "user/userList";
		final List<User> userList = new ArrayList<User>();
		SessionJsonObjectRequest userListRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("userList", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							int nextPage = response.getInt("next");
							setRefreshPager(nextPage);
							if ("0".equals(responseCode)) {
								JSONArray jsonArray = response.getJSONArray("data");
								
								for (int i = 0; i < jsonArray.length(); i++) {
									User user = new User();
									JSONObject userObj = (JSONObject) jsonArray.get(i);
									String id = userObj.optString("id");// 用户ID
									String loginName = userObj.optString("loginName");// 用户名
									String name = userObj.optString("name");// 用户姓名
									String orgName = userObj.optString("officeName");// 机构名称
//									String mobile = userObj.optString("mobile");// 手机号码
//									String balance = userObj.optString("balanceString");// 余额
//									String minimum = userObj.optString("minimum");// 最低限额
									String userType = userObj.optString("userType");// 用户类型
									String auditState = userObj.optString("auditState");// 审核状态
//									String auditReason = userObj.optString("auditReason");// 审核理由
									String roleNames = userObj.optString("roleNames");// 用户角色名
//									String email = userObj.optString("email");// 邮箱
//									String remarks = userObj.optString("remarks");// 备注
//									String creatTime = userObj.optString("createDate");// 创建时间
									
/*									JSONObject office = userObj.optJSONObject("office");
									String officeId = office.optString("id");// 机构ID
									String officeType = office.optString("type");// 机构类型
									String userCode = office.optString("code");// 用户编号
									String area_name = office.optJSONObject("area").getString("name");// 归属区域名称
									String orgName = office.optString("name");// 机构名称
									String corpName = office.optString("corpName");// 法人姓名
									String corpMobile = office.optString("corpMobile");// 法人手机
									String corpIdNum = office.optString("corpIdNum");// 法人身份证号
									String address = office.optString("address");// 联系地址
									String master = office.optString("master");// 联系人
									String contactPhone = office.optString("phone");// 联系人电话
									String winNumber = office.optString("winNumber");// 窗口号

									String corp_licence = office.optString("corpLicenceSrc");// 营业执照图片
									String corp_card_fornt = office.optString("corpCardforntSrc");// 身份证正面图片
									String corp_card_rear = office.optString("corpCardrearSrc");// 身份证反面图片*/

									user.setId(id);
//									user.setCreatTime(creatTime);
									user.setLoginName(loginName);
									user.setName(name);
//									user.setMobile(mobile);
//									user.setBalance(balance);
//									user.setMinimum(minimum);
									user.setUserType(userType);
									user.setAuditState(auditState);
//									user.setAuditReason(auditReason);
									user.setRoleNames(roleNames);
									user.setOrgName(orgName);
//									user.setEmail(email);
//									user.setRemarks(remarks);
									/*user.setUserCode(userCode);
									user.setArea_name(area_name);
									user.setOrgName(orgName);
//									user.setCorpName(corpName);
									user.setCorpMobile(corpMobile);
									user.setCorpIdNum(corpIdNum);
									user.setAddress(address);
									user.setMaster(master);
									user.setContactPhone(contactPhone);
									user.setWinNumber(winNumber);
									user.setOfficeId(officeId);
									user.setOfficeType(officeType);
									user.setCorp_licence(corp_licence);
									user.setCorp_card_fornt(corp_card_fornt);
									user.setCorp_card_rear(corp_card_rear);*/
									userList.add(user);
								}
								
								Log.d("debug", "用户列表："+ userList);

								if(nextPage == 1){
									mDatas.clear();
								}else if (nextPage == -1){
									refreshClose = false;
								}
								mPullRefreshListView.onRefreshComplete();
//								int dataSize = mDatas.size();
								mDatas.addAll(userList);

								/*userListView.setAdapter(new CommonAdapter<User>(UserManageA.this, mDatas, R.layout.admin_usermanage_item) {

									@Override
									public void convert(ViewHolder holder, User user) {
										holder.setText(R.id.tv_homeInstitution, user.getOrgName())
									  	  .setText(R.id.tv_name, user.getName())
										  .setText(R.id.tv_role, user.getRoleNames())
										  .setText(R.id.tv_userName, user.getLoginName());
										TextView tv_auditingStatus = holder.getView(R.id.tv_auditStatus);
										if (Define.OUTLETS_NORMAL.equals(user.getAuditState())) {
											tv_auditingStatus.setText("审核通过");
										}else if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {
											tv_auditingStatus.setText("待审核");
										}else {
											tv_auditingStatus.setText("审核不通过");
										}
										
									}
								});*/
								if (adapter == null) {
									adapter = new CommonAdapter<User>(UserManageA.this, mDatas, R.layout.admin_usermanage_item) {
										@Override
										public void convert(ViewHolder holder, User user) {
											holder.setText(R.id.tv_homeInstitution, user.getOrgName())
													.setText(R.id.tv_name, user.getName())
													.setText(R.id.tv_role, user.getRoleNames())
													.setText(R.id.tv_userName, user.getLoginName());
											TextView tv_auditingStatus = holder.getView(R.id.tv_auditStatus);
											if (Define.OUTLETS_NORMAL.equals(user.getAuditState())) {
												tv_auditingStatus.setText("审核通过");
											} else if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {
												tv_auditingStatus.setText("待审核");
											} else {
												tv_auditingStatus.setText("审核不通过");
											}
										}
									};
									userListView.setAdapter(adapter);
								}

								if(pageNo == 1){
									if(mDatas.size()>0){
										userListView.setAdapter(adapter);
										userListView.setSelection(1);
									}
								}
								adapter.refreshData(mDatas);
//								userListView.setSelection(dataSize);

								cpd.dismiss();
								userListView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										User user = mDatas.get(position - 1);
										intent = new Intent(UserManageA.this, UserListDetailsA.class);
										intent.putExtra("userId", user.getId());
//										intent.putExtra("creatTime", user.getCreatTime());
//										intent.putExtra("officeId", user.getOfficeId());
//										intent.putExtra("officeType", user.getOfficeType());
										intent.putExtra("loginName", user.getLoginName());
										intent.putExtra("name", user.getName());
//										intent.putExtra("mobile", user.getMobile());
//										intent.putExtra("email", user.getEmail());
										intent.putExtra("userType", user.getUserType());
										intent.putExtra("roleName", user.getRoleNames());
										intent.putExtra("orgName", user.getOrgName());
//										intent.putExtra("area", user.getArea_name());
//										intent.putExtra("address", user.getAddress());
//										intent.putExtra("corpName", user.getCorpName());
//										intent.putExtra("corpMobile", user.getCorpMobile());
//										intent.putExtra("corpIdNum", user.getCorpIdNum());
//										intent.putExtra("master", user.getMaster());
//										intent.putExtra("contactPhone", user.getContactPhone());
//										intent.putExtra("corp_licence", user.getCorp_licence());
//										intent.putExtra("corp_card_fornt", user.getCorp_card_fornt());
//										intent.putExtra("corp_card_rear", user.getCorp_card_rear());
										intent.putExtra("auditState", user.getAuditState());
//										intent.putExtra("auditReason", user.getAuditReason());
//										intent.putExtra("minimum", user.getMinimum());
										startActivity(intent);
										overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
									}
								});
								
							} else if ("46000".equals(responseCode)) {
								cpd.dismiss();
								Toast.makeText(UserManageA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UserManageA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(UserManageA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							cpd.dismiss();
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						cpd.dismiss();
						Toast.makeText(UserManageA.this, R.string.netError, Toast.LENGTH_SHORT).show();
//						Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

					}
				});
		// 解决重复请求后台的问题
		userListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		userListRequest.setTag("adminUserList");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(userListRequest);

	}

	private void setRefreshPager(final int nextPage) {
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshClose) {  //如果有数据 继续刷新
					try {
//						pageNumber++;
						getUserManageList(officeId, searchContent, auditState, nextPage);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Toast.makeText(UserManageA.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
					ThreadCommonUtils.runonuiThread(new Runnable() {
						@Override
						public void run() {
							mPullRefreshListView.onRefreshComplete();
						}
					});

				}
			}
		});
	}


	List<AllTreeNode>  allTreeNodesList = new ArrayList<>();

	//获取火车站地图信息
	private void getTrainMapData() {
		allTreeNodesList.clear();
		cpd.show();
		volleyNetCommon = new VolleyNetCommon();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("officeId","");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = Define.URL+"fb/getOfficeInfo";
		JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Method.POST, url, jsonObject, new VolleyAbstract(this) {
			@Override
			public void volleyResponse(Object o) {
				Log.i("aa","");
			}

			@Override
			public void volleyError(VolleyError volleyError) {
				Toast.makeText(UserManageA.this, R.string.netError, Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void netVolleyResponese(JSONObject json) {
				Gson gson = new Gson();
				NodeMap nodeMap = gson.fromJson(json.toString(), NodeMap.class);
				AllTreeNode allTree = new AllTreeNode();
				allTree.corpName = nodeMap.getRoot().getCorpName();
				allTree.geogPosition = nodeMap.getRoot().getGeogPosition();
				allTree.id = nodeMap.getRoot().getId();
				allTree.parentId = nodeMap.getRoot().getParentId();
				allTree.winNumber = nodeMap.getRoot().getWinNumber();
				allTree.type = nodeMap.getRoot().getType();
				allTree.name = nodeMap.getRoot().getName();
				allTree.phone = nodeMap.getRoot().getPhone();
				if(!nodeMap.getRoot().getLat().isEmpty()){
					allTree.lat =Double.parseDouble(nodeMap.getRoot().getLat());
				}
				if(!nodeMap.getRoot().getLng().isEmpty()){
					allTree.lng  = Double.parseDouble(nodeMap.getRoot().getLng());
				}
				allTreeNodesList.add(allTree);   //添加根节点

				if(!treeIdList.contains(allTree.id)) {  // 层级树最顶层层id
					treeIdList.add(allTree.id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
				}
				List<NodeMap.RootBean.ChildOfficesBeanXX> childOffices = nodeMap.getRoot().getChildOffices();
				if(childOffices != null && childOffices.size()>0) {

					String defaultId = childOffices.get(0).getId() == null ? "" : childOffices.get(0).getId(); //默认请求
					for (int i = 0; i < childOffices.size(); i++) {
						AllTreeNode allTreeNode = new AllTreeNode();
						allTreeNode.corpName = childOffices.get(i).getCorpName();
						allTreeNode.geogPosition = childOffices.get(i).getGeogPosition();
						allTreeNode.id = childOffices.get(i).getId();
						if (!childOffices.get(i).getLat().isEmpty()) {
							if(childOffices.get(i).getLat().equals("undefined")){
								allTreeNode.lat = 0.0;
							}else {
								allTreeNode.lat = Double.parseDouble(childOffices.get(i).getLat());
							}
						}
						if (!childOffices.get(i).getLng().isEmpty()) {
							if(childOffices.get(i).getLat().equals("undefined")) {
								allTreeNode.lng = 0.0;
							}else {
								allTreeNode.lng = Double.parseDouble(childOffices.get(i).getLng());
							}
						}
						allTreeNode.name = childOffices.get(i).getName();
						allTreeNode.parentId = childOffices.get(i).getParentId();
						allTreeNode.phone = childOffices.get(i).getPhone();
						allTreeNode.type = childOffices.get(i).getType();
						allTreeNode.winNumber = childOffices.get(i).getWinNumber();
						allTreeNodesList.add(allTreeNode);
						//	diGuiChildOffices(childOffices.get(i).getChildOffices());  //递归所有的childOffices
					}


					setDefaultRequest(defaultId); //获取下一级菜单

					;
				}
			}

			@Override
			protected void PdDismiss() {
				cpd.dismiss();
			}
			@Override
			protected void errorMessages(String message) {
				super.errorMessages(message);
				Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
			}
		}, "getOfficeInfo", true);
		volleyNetCommon.addQueue(jsonObjectRequest);
	}

	//火车站 归属机构默认请求下一级
	private void setDefaultRequest(final String id) {
		cpd.show();
		volleyNetCommon = new VolleyNetCommon();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("officeId",id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Method.POST, Define.URL + "fb/getOfficeInfo", jsonObject, new VolleyAbstract(this) {
			@Override
			public void volleyResponse(Object o) {

			}

			@Override
			public void volleyError(VolleyError volleyError) {
				Toast.makeText(UserManageA.this, R.string.netError, Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void netVolleyResponese(JSONObject json) {
				Gson gson = new Gson();
				NodeMap nodeMap = gson.fromJson(json.toString(), NodeMap.class);
				List<NodeMap.RootBean.ChildOfficesBeanXX> childOffices = nodeMap.getRoot().getChildOffices();
				if(childOffices != null && childOffices.size()>0) {
					String defaultId = childOffices.get(0).getId() == null ? "" : childOffices.get(0).getId(); //默认请求

					for (int i = 0; i < childOffices.size(); i++) {
						AllTreeNode allTreeNode = new AllTreeNode();
						allTreeNode.corpName = childOffices.get(i).getCorpName();
						allTreeNode.geogPosition = childOffices.get(i).getGeogPosition();
						allTreeNode.id = childOffices.get(i).getId();

						if (!childOffices.get(i).getLat().isEmpty()) {
							if(childOffices.get(i).getLat().equals("undefined")){
								allTreeNode.lat = 0.0;
							}else {
								allTreeNode.lat = Double.parseDouble(childOffices.get(i).getLat());
							}
						}
						if (!childOffices.get(i).getLng().isEmpty()) {
							if(childOffices.get(i).getLat().equals("undefined")) {
								allTreeNode.lng = 0.0;
							}else {
								allTreeNode.lng = Double.parseDouble(childOffices.get(i).getLng());
							}
						}
						allTreeNode.name = childOffices.get(i).getName();
						allTreeNode.parentId = childOffices.get(i).getParentId();
						allTreeNode.phone = childOffices.get(i).getPhone();
						allTreeNode.type = childOffices.get(i).getType();
						allTreeNode.winNumber = childOffices.get(i).getWinNumber();
						allTreeNodesList.add(allTreeNode);
					}
					setDefaultRequest(defaultId);  //递归
				}
//				else{
//
//					//setShowMark(); //递归结束展示数据
//					zoomToSpan(allTreeNodesList,null,"1"); //递归结束后展示数据  1不代表什么
//				}
				if(!treeIdList.contains(id)) {
					treeIdList.add(id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
				}
			}

			@Override
			protected void PdDismiss() {
				cpd.dismiss();
			}
			@Override
			protected void errorMessages(String message) {
				super.errorMessages(message);
				Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
			}
		}, "getOfficeInfoDefault", true);

		volleyNetCommon.addQueue(jsonObjectRequest);

	}

	//获取火车站归属机构数据
	private void setTrainOwnershipData() {
		getTrainOwnershipData();
		if(trainOwnershipList != null && trainOwnershipList.size()>0) {
			try {
				simpleTreeListViewAdapter = new SimpleTreeListViewAdapter<TreeBean>(lv_train_ownership, UserManageA.this,
						trainOwnershipList, 1);  //1表示只默认只展开2级菜单
				lv_train_ownership.setAdapter(simpleTreeListViewAdapter);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			//树点击
			simpleTreeListViewAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
				@Override
				public void onClick(final Node node, int position) {
					if (node.isLeaf()) {
					/*	Toast.makeText(UserManageA.this, node.getAllTreeNode().name,
								Toast.LENGTH_SHORT).show();*/
					}
					if (node.getAllTreeNode().type.equals("2") || node.getAllTreeNode().type.equals("3")) {  //处理节点选择器
						simpleTreeListViewAdapter.setSelectedItem(position, Long.parseLong(node.getAllTreeNode().id.substring(0,15),16));
						simpleTreeListViewAdapter.notifyDataSetChanged();
					}

					if(!node.getAllTreeNode().type.equals("3") && !treeIdList.contains(node.getAllTreeNode().id)) { //如果包含这个id表示已经通过这个id进行搜索节点数据过了没有必要再点击再次请求网络加载数据
						setClickLoadSingleTreeData(node.getAllTreeNode().id,position, node.getAllTreeNode().type, node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
					}

//					if(node.getAllTreeNode().type.equals("2")){   //点击节点火车站 继续刷新  获取相应代售点 进行地图展示
//						setClickLoadSingleTreeData(node.getAllTreeNode().id,position,node.getAllTreeNode().type, node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
//					}

					if(node.getAllTreeNode().type.equals("3")){
						officeId = String.valueOf(node.getId());
						search_area.setText(node.getName());
					//	setClickLoadSingleTreeData(node.getAllTreeNode().parentId,position,node.getAllTreeNode().type,node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
//						ThreadCommonUtils.runonuiThread(new Runnable() {
//							@Override
//							public void run() {
//								setTreeSingleMessage(node.getAllTreeNode());
//							}
//						});
//
//						if(baiduView == null){
//							baiduView = getLayoutInflater().inflate(R.layout.baidu_pop,null);
//							tv_baidu_pop = (TextView) baiduView.findViewById(tv_baidu_pop); //百度地图弹窗文字
//						}
//
//						tv_baidu_pop.setText(node.getAllTreeNode().name);
//						InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
//							@Override
//							public void onInfoWindowClick() {
//								baiduMap.hideInfoWindow();
//							}
//						};
//						InfoWindow	mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(baiduView), new LatLng(node.getAllTreeNode().lat,node.getAllTreeNode().lng), -30, onInfoWindowClickListener);
//						baiduMap.showInfoWindow(mInfoWindow);
//
					}




				}
			});
		}
	}

	private void getTrainOwnershipData() {
		trainOwnershipList = new ArrayList<>();
		trainOwnershipList.clear();
		if(allTreeNodesList != null && allTreeNodesList.size()>0){
			for (int i = 0; i < allTreeNodesList.size(); i++) {
				AllTreeNode allTreeNode = allTreeNodesList.get(i);
				if(allTreeNode.id.equals("1") && allTreeNode.parentId.isEmpty()){
					allTreeNode.parentId ="2324323232131312a"; //顶级节点随便取
					allTreeNode.id = "a566565656565656566";
					long id =Long.parseLong(allTreeNode.id.substring(0,15),16);
					long pid = Long.parseLong(allTreeNode.parentId.substring(0,15),16);
					String name = allTreeNode.name;
					trainOwnershipList.add(new TreeBean(id,pid,name,allTreeNode));
					if(!treeIdList.contains(allTreeNode.id)) {  // 层级树最顶层层id
						treeIdList.add(allTreeNode.id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
					}

				}else {
					if (allTreeNode.parentId.equals("1")) {
						allTreeNode.parentId = "a566565656565656566"; //顶级节点随便取
						long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
						long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
						String name = allTreeNode.name;
						trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
					} else {
						if (allTreeNode.id.length() > 16 && allTreeNode.parentId.length() > 16) {
							long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
							long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
							String name = allTreeNode.name;
							trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
						}
					}
					Log.e("yyyyy", "id>>>>>>>" + allTreeNode.id + "--------parentId>>>>>" + allTreeNode.parentId + "\n");
				}
			}

			/*for (AllTreeNode allTreeNode: allTreeNodesList
				 ) {
				AllTreeNode allTreeNodes = new AllTreeNode();
				Integer.parseInt("FF",16);
				int id =Integer.parseInt(allTreeNode.id.substring(0,6),16);

				int pid = Integer.parseInt(allTreeNode.parentId.substring(0,6),16);
				String name = allTreeNode.name;


				trainOwnershipList.add(new TreeBean(id,pid,name,allTreeNodes));

			}*/
		}
	}

	//获取点击之后动态加载tree节点数据
	private void setClickLoadSingleTreeData(final String id, final int position, final String type, final AllTreeNode allTreeNode) {
		cpd.show();
		volleyNetCommon = new VolleyNetCommon();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("officeId",id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = Define.URL+"fb/getOfficeInfo";
		final JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Method.POST, url, jsonObject, new VolleyAbstract(this) {
			@Override
			public void volleyResponse(Object o) {

			}

			@Override
			public void volleyError(VolleyError volleyError) {
				Toast.makeText(UserManageA.this, R.string.netError, Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void netVolleyResponese(JSONObject json) {

				List<AllTreeNode>  allTreeList = new ArrayList<>();
				Gson gson = new Gson();
				NodeMap nodeMap = gson.fromJson(json.toString(), NodeMap.class);
				List<NodeMap.RootBean.ChildOfficesBeanXX> childOffices = nodeMap.getRoot().getChildOffices();
				if(childOffices != null && childOffices.size()>0) {
					for (int i = 0; i < childOffices.size(); i++) {
						AllTreeNode allTreeNode = new AllTreeNode();
						allTreeNode.corpName = childOffices.get(i).getCorpName();
						allTreeNode.geogPosition = childOffices.get(i).getGeogPosition();
						allTreeNode.id = childOffices.get(i).getId();
						if (!childOffices.get(i).getLat().isEmpty()) {
							allTreeNode.lat = Double.parseDouble(childOffices.get(i).getLat());
						}
						if (!childOffices.get(i).getLng().isEmpty()) {
							allTreeNode.lng = Double.parseDouble(childOffices.get(i).getLng());
						}
						allTreeNode.name = childOffices.get(i).getName();
						allTreeNode.parentId = childOffices.get(i).getParentId();
						allTreeNode.phone = childOffices.get(i).getPhone();
						allTreeNode.type = childOffices.get(i).getType();
						allTreeNode.winNumber = childOffices.get(i).getWinNumber();
						allTreeList.add(allTreeNode);
					}
				}
				if(MyApplication.type.equals("2")){
					if(allTreeNodesList != null && allTreeList.size()<=0) {
						Toast.makeText(UserManageA.this, "该层级你还没有代售点！", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				// 下面这两个判断   解决重复加载 并且可以刷新地图
				if(!treeIdList.contains(id)) {
					simpleTreeListViewAdapter.addExtraNode(position,allTreeList);  //动态增加火车站层级树节点
					allTreeNodesList.addAll(allTreeList);
					treeIdList.add(id);
				}

//				if (type.equals("2") || type.equals("3")) {  //表示只有火车站才可以进行更新地图
//					zoomToSpan(allTreeList,allTreeNode,type);
//				}
			}

			@Override
			protected void PdDismiss() {
				cpd.dismiss();
			}
			@Override
			protected void errorMessages(String message) {
				super.errorMessages(message);
				Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
			}
		}, "setClickLoadSingleTreeData", true);

		volleyNetCommon.addQueue(jsonObjectRequest);

	}


	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("adminUserList");

	}


	@Override
	public void onItemClick(int pos) {
		if ("1".equals(CHOICE_TAG)) {
			setIdentity(pos);
		}
	}

	// 选择用户类型
	private void setIdentity(int pos) {
		if (pos >= 0 && pos <= auditStateList.size()) {
			String value = auditStateList.get(pos);

			tv_searchStatus.setText(value);

			auditState = tv_searchStatus.getText().toString();
			// if ("管理员".equals(identity)) {
			// identity = Define.SYSTEM_MANAG_USER;
			// et_windowNumber.setVisibility(View.GONE);
			// et_userName.setVisibility(View.VISIBLE);
			// }else
			if ("审核通过".equals(auditState)) {
				auditState = Define.OUTLETS_NORMAL;
			} else if ("待审核".equals(auditState)) {
				auditState = Define.OUTLETS_PENDIG_AUDIT;
			}else if ("审核不通过".equals(auditState)) {
				auditState = Define.OUTLETS_NOTPASS;
			}
		}
	}
}
