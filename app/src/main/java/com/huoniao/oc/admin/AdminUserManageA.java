package com.huoniao.oc.admin;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AdminUserListBean;
import com.huoniao.oc.bean.UserRoleTypeBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.NumberFormatUtils;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.treeIdList2;
import static com.huoniao.oc.R.id.userPullToRefreshListView;

public class AdminUserManageA extends BaseActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.iv_back)
    ImageView ivBack;  //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle; //标题
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;  //归属机构
     @InjectView(R.id.ll_ownership_institution)
    LinearLayout llOwnershipInstitution;  //归属机构容器
     @InjectView(R.id.tv_user_role_type)
    TextView tvUserRoleType;   //用户角色类型
    @InjectView(R.id.et_variousTypes)
    EditText etVariousTypes; //用户名/角色/姓名
    @InjectView(R.id.tv_audit_status)
    TextView tvAuditStatus;   //审核状态
    @InjectView(R.id.tv_query)
    TextView tvQuery;   //查询
    @InjectView(R.id.tv_recordTotal)
    TextView tvRecordTotal;  //总条数记录
    @InjectView(R.id.ll_audit_status)    //审核状态容器
    LinearLayout ll_audit_status;
    @InjectView(R.id.ll_user_role_type) //用户角色容器
    LinearLayout ll_user_role_type;
    @InjectView(userPullToRefreshListView)
    PullToRefreshListView mPullRefreshListView;
    private ListView lv_audit_status;  //弹出框审核状态列表

    List<String> auditStateList = new ArrayList<>(); //审核状态集合
    private int xOffAuditset;
    private int xOffRoleset;
    private int cow;
    private MyPopWindow myPopWindow;
    private String linAuditStateString=""; //审核状态  记录点击pop审核里列表的值
    private String linRoleIdString =""; //临时记录用户角色id
    private String linOfficeId="";  //临时记录 选中归属机构id  只有查询后生效
    private VolleyNetCommon volleyNetCommon;
    private List<AdminUserListBean.DataBean> userDataList = new ArrayList<>();  //用户列表数据
    private String pageNext="";  //记录上拉加载更多返回的 页数
    private int sizeNumber=0; //记录总条数
    private ListView mListView;  //包裹的listview

    private String auditState ="" ; //审核状态(0:正常1:待审核 2:审核不通过)
    private  String str ="";         // 用户名/姓名/机构名称
    private  String officeId ="";    //所属机构Id
    private  String roleId ="";      //角色Id

    private int[] windowAuditPos;
    private int[] windowRolePos;
    private float xs;          //计算popwindow的位置
    private float ys;//计算popwindow的位置
    private float xsRole;//计算popwindow的位置
    private float ysRole;//计算popwindow的位置
    private CommonAdapter<AdminUserListBean.DataBean> dataBeanCommonAdapter;
    private ImageView iv_photoSrc;
    private MapData mapData;
    private String pending;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_manage);
        ButterKnife.inject(this);

        initWidget();
        pending = getIntent().getStringExtra("pending");
        if(pending !=null && pending.equals("1")){ //审核状态
            ll_audit_status.setVisibility(View.GONE);
            tvTitle.setText("待审核");
            auditState = "1";
            userData(auditState,"","","","1");
        }else{
            tvTitle.setText("用户管理");
            userData("","","","","1");
        }

        treeIdList2.clear();  //清空归属机构记录的数据

    }

    /**
     * 控件操作
     */
    private void initWidget() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullRefreshListView.getRefreshableView();
         mListView.setOnItemClickListener(this);
        initLinsener();




    }

    private void initLinsener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (pageNext.equals("-1")) {
                    Toast.makeText(AdminUserManageA.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    });
                } else {
                    //userData();  TODO 还差归属机构  ""
                    userData(auditState,str,officeId,roleId,pageNext);
                }
            }
        });
    }


    /**
     *   获取用户列表数据
     * @param auditState  审核状态(0:正常1:待审核 2:审核不通过)
     * @param str          用户名/姓名/机构名称
     * @param officeId    所属机构Id
     * @param roleId      角色Id
     * @param pageNo      页数
     */
    private void userData(String auditState , String str , String officeId, String roleId, final String pageNo) {
       if(volleyNetCommon ==null) {
           volleyNetCommon = new VolleyNetCommon();
       }
        cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("auditState",auditState);
            jsonObject.put("str",str);
            jsonObject.put("officeId",officeId);
            jsonObject.put("roleId",roleId);
            jsonObject.put("pageNo",pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL + "user/userList";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(AdminUserManageA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
               if(pageNo.equals("1")){
                   if(userDataList != null){
                       userDataList.clear();
                   }
                   if(dataBeanCommonAdapter!=null){
                       mListView.setAdapter(dataBeanCommonAdapter);
                       dataBeanCommonAdapter.notifyDataSetChanged();
                   }
               }

                try {
                    pageNext = String.valueOf(json.getInt("next"));
                    if(pageNo.equals("1")) {
                        sizeNumber = json.getInt("count"); //记录总条数
                        tvRecordTotal.setText(sizeNumber+"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPullRefreshListView.onRefreshComplete();
                Gson gson = new Gson();
                final AdminUserListBean adminUserListBean = gson.fromJson(json.toString(), AdminUserListBean.class);
                if(adminUserListBean.getData().size()>0) {
                    for (int i = 0; i < adminUserListBean.getData().size(); i++) {


                        final String tags = "img"+i;
                        try {
                         String img =  adminUserListBean.getData().get(i).getPhotoSrc()==null ?"":adminUserListBean.getData().get(i).getPhotoSrc();
                            if(!img.isEmpty()) {
                                getDocumentImage3(adminUserListBean.getData().get(i).getPhotoSrc(), "img" + i, i,true, "");
                            }
                            setImgResultLinstener(new ImgResult() {
                                @Override
                                public void getImageFile(File file,String imgUrl, String tag, int i, String linkUrlStr) {

                                    adminUserListBean.getData().get(i).setFile(file);
                                    if(dataBeanCommonAdapter!=null) {
                                        dataBeanCommonAdapter.notifyDataSetChanged();
                                    }

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                userDataList.addAll(adminUserListBean.getData());
                setAdapter();

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
        }, "userList", true);

        volleyNetCommon.addQueue(jsonObjectRequest);

    }

    //给用户列表设置设置适配器
    private void setAdapter() {
        if (dataBeanCommonAdapter == null ) {
            dataBeanCommonAdapter = new CommonAdapter<AdminUserListBean.DataBean>(AdminUserManageA.this, userDataList, R.layout.item_user_list) {
                @Override
                public void convert(final ViewHolder holder, AdminUserListBean.DataBean dataBean) {
                    //头像
                    iv_photoSrc = holder.getView(R.id.iv_photoSrc);
                    TextView tv_officeName = holder.getView(R.id.tv_officeName);//机构名称
                    TextView tv_loginName = holder.getView(R.id.tv_loginName);//用户名
                    TextView tv_name = holder.getView(R.id.tv_name);//姓名
                    TextView tv_roleNames = holder.getView(R.id.tv_roleNames);//角色名称
                    TextView tv_auditState = holder.getView(R.id.tv_auditState);//审核状态
                    TextView tv_accountBalance = holder.getView(R.id.tv_accountBalance);//账户余额
                    TextView tv_minimum = holder.getView(R.id.tv_minimum);//最低限额
                    LinearLayout  ll_officeName = holder.getView(R.id.ll_officeName);  //进入资料修改页面
                    ll_officeName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          //TODO 进入用户修改界面
                            //startActivityMethod(AdminUserDataUpdate.class);
                        }
                    });
                    tv_officeName.setText(dataBean.getOfficeName());
                    tv_loginName.setText(dataBean.getLoginName());
                    tv_name.setText(dataBean.getName());
                    tv_roleNames.setText(dataBean.getRoleNames());
                   final String src =  dataBean.getPhotoSrc()==null ? "":dataBean.getPhotoSrc();
                    iv_photoSrc.setTag(src);
                    if(iv_photoSrc.getTag().equals(src)) {
                        if (src.isEmpty()) {
                            iv_photoSrc.setImageDrawable(getResources().getDrawable(R.drawable.applogo));
                        } else {
                            try {
                                Glide.with(AdminUserManageA.this).load(dataBean.getFile()).into(iv_photoSrc);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                   // iv_photoSrc.setTag();
                    switch (dataBean.getAuditState()){
                        case Define.OUTLETS_NORMAL:  // 0:审核通过
                            tv_auditState.setText("审核通过");
                            tv_auditState.setTextColor(getResources().getColor(R.color.sgrenn));
                            break;
                        case Define.OUTLETS_PENDIG_AUDIT:  //	1:待审核
                            tv_auditState.setText("待审核");
                            tv_auditState.setTextColor(getResources().getColor(R.color.blues));
                            break;
                        case Define.OUTLETS_NOTPASS:  //	2:审核不通过
                            tv_auditState.setText("审核不通过");
                            tv_auditState.setTextColor(getResources().getColor(R.color.colorAccent));
                            break;
                    }

                    tv_accountBalance.setText(dataBean.getBalanceString());
                    tv_minimum.setText(NumberFormatUtils.formatDigits(dataBean.getMinimum())+"");
                }
            };
            mListView.setAdapter(dataBeanCommonAdapter);
        }

        dataBeanCommonAdapter.notifyDataSetChanged();
    }



    @OnClick({R.id.iv_back, R.id.tv_ownership_institution, R.id.tv_user_role_type, R.id.tv_audit_status, R.id.tv_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_ownership_institution: //归属机构
                showOwnershipPop();

                break;
            case R.id.tv_user_role_type:
                showRoleType(); //获取用户角色类型
                break;
            case R.id.tv_audit_status:
                showAuditStatus();
                break;
            case R.id.tv_query:  //查询
                  getTextData();
                break;
        }
    }

    /**
     * 弹出归属机构
     */
    private void showOwnershipPop() {
        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
//得到状态栏高度
//返回键关闭
        mapData = new MapData(AdminUserManageA.this,cpd, false){
           @Override
           protected void showTrainMapDialog() {
               super.showTrainMapDialog();
               if(myPopWindow !=null){
                   myPopWindow.dissmiss();
               }

               myPopWindow =  new MyPopAbstract() {
                   @Override
                   protected void setMapSettingViewWidget(View view) {
                       lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                       mapData.setTrainOwnershipData(lv_audit_status);
                       //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                       int[] arr = new int[2];
                       llOwnershipInstitution.getLocationOnScreen(arr);
                       view.measure(0, 0);
                       Rect frame = new Rect();
                       getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                       xs = arr[0] + llOwnershipInstitution.getWidth() - view.getMeasuredWidth();
                       ys = arr[1] + llOwnershipInstitution.getHeight();
                    mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                        @Override
                        public void treeNodeResult(Node officeId) {
                            linOfficeId= String.valueOf(officeId.getAllTreeNode().id);
                            tvOwnershipInstitution.setText(officeId.getAllTreeNode().name);
                           // if(officeId.getAllTreeNode().type.equals("2")) {
                                myPopWindow.dissmiss();
                            //}
                        }
                    });
                   }

                   @Override
                   protected int layout() {
                       return R.layout.admin_audit_status_pop2;
                   }
               }.popupWindowBuilder(AdminUserManageA.this).create();
               myPopWindow.keyCodeDismiss(true); //返回键关闭
               myPopWindow.showAtLocation(llOwnershipInstitution, Gravity.NO_GRAVITY,(int)xs,(int)ys);
           }
       };
      //  mapData.setTrainOwnershipData();


    }

    //获取文本框数据 文本框的数据点击查询后生效
    private void getTextData() {
        str = etVariousTypes.getText().toString().trim();//用户名，角色，姓名
        if(pending==null){
            auditState = linAuditStateString;  //临时的赋值给正式的 审核状态
        }
        roleId = linRoleIdString; //用户角色id
        officeId = linOfficeId ;//归属机构id
        //TODO 所属机构id
         query();

    }

    /**
     * 开始查询
     */
    private void query() {
        userData(auditState,str,officeId,roleId,"1");   //默认都查询第一页
    }

    private void showAuditStatus() {


       if(myPopWindow !=null){
           myPopWindow.dissmiss();
       }
        myPopWindow = new MyPopAbstract() {
             @Override
             protected void setMapSettingViewWidget(View view) {
                 lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                 //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                 int[] arr = new int[2];
                 ll_audit_status.getLocationOnScreen(arr);
                 view.measure(0, 0);
                 Rect frame = new Rect();
                 getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                 xs = arr[0] + ll_audit_status.getWidth() - view.getMeasuredWidth();
                 ys = arr[1] + ll_audit_status.getHeight();
                 setDataListView();
                 CommonAdapter commonAdapter = new CommonAdapter(AdminUserManageA.this,auditStateList,R.layout.admin_item_audit_status_pop) {
                     @Override
                     public void convert(ViewHolder holder, Object o) {
                           holder.setText(R.id.tv_text,(String)o);

                     }
                 };

                 lv_audit_status.setAdapter(commonAdapter);
                 lv_audit_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       String  audit = auditStateList.get(i); //获取点击的文字
                         tvAuditStatus.setText(audit);
                         if ("审核通过".equals(audit)) {
                             linAuditStateString = Define.OUTLETS_NORMAL;    //审核状态
                         } else if ("待审核".equals(audit)) {
                             linAuditStateString = Define.OUTLETS_PENDIG_AUDIT;
                         }else if ("审核不通过".equals(audit)) {
                             linAuditStateString = Define.OUTLETS_NOTPASS;
                         }else if("全部".equals(audit)){
                             linAuditStateString = "";
                         }
                         myPopWindow.dissmiss();
                     }
                 });
             }

             @Override
             protected int layout() {
                 return R.layout.admin_audit_status_pop;
             }
         }.popupWindowBuilder(this).create();
        myPopWindow.keyCodeDismiss(true); //返回键关闭
        myPopWindow.showAtLocation(ll_audit_status, Gravity.NO_GRAVITY,(int)xs,(int)ys);
    }



    /**
     * 添加审核状态集合
     */
    private void setDataListView() {
         auditStateList.clear();
         auditStateList.add("全部");
         auditStateList.add("审核通过");
         auditStateList.add("待审核");
         auditStateList.add("审核不通过");
    }

    /**
     * 获取用户角色类型
     */
    private void showRoleType() {
        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        String url =Define.URL+"sys/getRoles";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(AdminUserManageA.this,R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                json.toString();
                Gson gson = new Gson();
                UserRoleTypeBean userRoleTypeBean = gson.fromJson(json.toString(), UserRoleTypeBean.class);
                List<UserRoleTypeBean.ListBean> list = userRoleTypeBean.getList();
                showPop(list);
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
        }, "getRoles", true);

        volleyNetCommon.addQueue(jsonObjectRequest);


        //

    }

     List<UserRoleTypeBean.ListBean>  roleList = new ArrayList<>();
    /**
     * 弹用户角色
     * @param list
     */
    public void showPop(List<UserRoleTypeBean.ListBean> list){
          roleList.clear();
         UserRoleTypeBean.ListBean  bean =  new UserRoleTypeBean.ListBean();
          bean.setId("");
          bean.setName("全部");
           roleList.add(bean);
           roleList.addAll(list);

        if(myPopWindow != null){
            myPopWindow.dissmiss();
        }
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                ll_user_role_type.getLocationOnScreen(arr);
                view.measure(0, 0);
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                xsRole = arr[0] + ll_user_role_type.getWidth() - view.getMeasuredWidth();
                ysRole = arr[1] + ll_user_role_type.getHeight();
                CommonAdapter<UserRoleTypeBean.ListBean> commonAdapter = new CommonAdapter<UserRoleTypeBean.ListBean>(AdminUserManageA.this,roleList,R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, UserRoleTypeBean.ListBean listBean) {
                        holder.setText(R.id.tv_text,listBean.getName());
                    }
                };

                lv_audit_status.setAdapter(commonAdapter);
                lv_audit_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        UserRoleTypeBean.ListBean listBean = roleList.get(i);
                        linRoleIdString = listBean.getId(); //用户角色id
                        tvUserRoleType.setText(listBean.getName());
                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return  R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(this).create();

        myPopWindow.keyCodeDismiss(true); //返回键关闭
        myPopWindow.showAtLocation(ll_user_role_type,Gravity.NO_GRAVITY,(int)xsRole,(int)ysRole);
    }

    //listivew条目点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AdminUserListBean.DataBean dataBean = userDataList.get(i-1);
        Intent intent = new Intent(AdminUserManageA.this,AdminUserDetails.class);
        intent.putExtra("id",dataBean.getId());
        switch (dataBean.getAuditState()){
            case Define.OUTLETS_NORMAL:  // 0:审核通过

            case Define.OUTLETS_NOTPASS:  //	2:审核不通过

                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case Define.OUTLETS_PENDIG_AUDIT:  //	1:待审核

                intent.putExtra("PendingAudit","PendingAudit"); //带审核参数
                startActivityForResult(intent,0);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 100:
                //再次请求刷新
                userData(auditState,str,officeId,roleId,"1");   //默认都查询第一页
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(volleyNetCommon !=null){
            volleyNetCommon.getRequestQueue().cancelAll("getRoles");
            volleyNetCommon.getRequestQueue().cancelAll("userList");
        }
    }


}
