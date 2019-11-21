package com.huoniao.oc.admin;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.InstitutionalStateBean;
import com.huoniao.oc.bean.TypeOfInstitutionBean;
import com.huoniao.oc.bean.UserRoleTypeBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.ll_user_roles;
import static com.huoniao.oc.R.id.tv_customer_type;
import static com.huoniao.oc.R.id.tv_first_agent;
import static com.huoniao.oc.R.id.tv_ownership_institution;
import static com.huoniao.oc.R.id.tv_text;
import static com.huoniao.oc.R.id.tv_user_roles;

public class AdminUserDataUpdate extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;     //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;   //标题
    @InjectView(R.id.tv_add_card)
    TextView tvAddCard;  //
    @InjectView(R.id.tv_saved)
    TextView tvSaved;  //保存
    @InjectView(R.id.et_organizationName)
    EditText etOrganizationName;  //机构名称
    @InjectView(R.id.et_userid)
    EditText etUserid;  //  用户id
    @InjectView(R.id.et_name)
    EditText etName; //姓名
    @InjectView(R.id.et_register_phone)
    EditText etRegisterPhone; // 注册手机号
    @InjectView(R.id.tv_account_balance)
    TextView tvAccountBalance;  //账户余额
    @InjectView(R.id.et_account_minimum)
    EditText etAccountMinimum; //账户最低限额
    @InjectView(R.id.et_dynamic_quota)
    EditText etDynamicQuota; //动态限额
    @InjectView(R.id.et_financing_repayment_date)
    EditText etFinancingRepaymentDate; //融资还款日
    @InjectView(tv_ownership_institution)
    TextView tvOwnershipInstitution; //归属机构
    @InjectView(tv_customer_type)
    TextView tvCustomerType; // 用户类型
    @InjectView(tv_user_roles)
    TextView tvUserRoles; //用户角色
    @InjectView(R.id.tv_institutional_state)
    TextView tvInstitutionalState; // 机构状态
    @InjectView(R.id.tv_type_of_institution)
    TextView tvTypeOfInstitution; // 机构类型
    @InjectView(tv_first_agent)
    TextView tvFirstAgent; //第一代理人
    @InjectView(R.id.tv_first_agent_type)
    TextView tvFirstAgentType; // 第一代理人类型
    @InjectView(R.id.et_superior_organization)
    EditText etSuperiorOrganization;  //上级机构
    @InjectView(R.id.et_jurisdiction_area)
    EditText etJurisdictionArea; //管辖区域
    @InjectView(R.id.et_geographical_region)
    EditText etGeographicalRegion;  //地理区域
    @InjectView(R.id.et_address)
    EditText etAddress;  //地址
    @InjectView(R.id.tv_geographical_position)
    TextView tvGeographicalPosition; //地理位置
    @InjectView(R.id.et_legal_person_name)
    EditText etLegalPersonName; //法人姓名
    @InjectView(R.id.et_legal_person_phone)
    EditText etLegalPersonPhone; //法人手机
    @InjectView(R.id.et_legal_person_id)
    EditText etLegalPersonId; //法人身份证
    @InjectView(R.id.et_person_in_charge_name)
    EditText etPersonInChargeName; //负责人姓名
    @InjectView(R.id.et_person_in_charge_phone)
    EditText etPersonInChargePhone; //负责人手机
    @InjectView(R.id.et_person_in_charge_id)
    EditText etPersonInChargeId; //负责人身份证
    @InjectView(R.id.et_contact_name)
    EditText etContactName; //联系人姓名
    @InjectView(R.id.et_contact_phone)
    EditText etContactPhone; //联系人手机
    @InjectView(R.id.gv_enclosure)
    GridView gvEnclosure; //图片附件
    @InjectView(R.id.et_remarks)
    EditText etRemarks;  //备注
    @InjectView(R.id.tv_return)
    TextView tvReturn; //返回键
    @InjectView(R.id.activity_user_data_modification)
    LinearLayout activityUserDataModification;


    /// / popwindow弹在下面容器的的下面
    @InjectView(R.id.ll_ownership_institution)
    LinearLayout llOwnershipInstitution;
    @InjectView(R.id.ll_customer_type)
    LinearLayout llCustomerType;
    @InjectView(ll_user_roles)
    LinearLayout llUserRoles;
    @InjectView(R.id.ll_institutional_state)
    LinearLayout llInstitutionalState;
    @InjectView(R.id.ll_type_of_institution)
    LinearLayout llTypeOfInstitution;
    @InjectView(R.id.ll_first_agent)
    LinearLayout llFirstAgent;
    @InjectView(R.id.ll_first_agent_type)
    LinearLayout llFirstAgentType;
    private MyPopWindow myPopWindowOwnership; //归属机构pop
    private MapData mapData;
    private float xs;  //popwindow弹出的位置
    private float ys; //popwindow弹出的位置
    private String linOfficeId; //临时归属机构id
    private String linRoleIdString; //临时用户角色id
    private String linTypeOfInstitution;  //临时记录机构类型
    private String linInstitutionalState;  //机构状态
    private String linFirstAgent; //第一代理人
    private String linFirstAgentType; //临时标记第一代理人类型
    private List<TypeOfInstitutionBean.DataBean> dataTypeOfInstitution; //机构类型数据
    private List<InstitutionalStateBean> institutionalStateBeenlList;  //机构状态集合
    private final String GETDICTARY ="getDictAry"; //机构类型标记
    private final String JIGOULEIXING="jigouleixing";//popwindow 机构类型 弹出标记
    private final String JIGOUZHUANGTAI="jigouzhuangtai"; //机构状态 弹出标记
    private final String DIYIDAILIREN = "diyidailiren"; //第一代理人
    private final String DIYIDAILIRENLEIXING="diyidailirenleixing"; //第一代理人类型
    private final String YONGHULEIXING="yonghuleixing"; //用户类型
    private String linCustomerType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_modification);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_saved, R.id.tv_ownership_institution, tv_customer_type, tv_user_roles, R.id.tv_institutional_state, R.id.tv_type_of_institution, tv_first_agent, R.id.tv_first_agent_type, R.id.tv_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:  //返回键
                finish();
                break;
            case tv_ownership_institution: //归属机构
                showOwnership();
                break;
            case tv_customer_type:  //用户类型
                showCustomertype();
                break;
            case tv_user_roles://用户角色
                showRoleType();
                break;
            case R.id.tv_institutional_state: //机构状态
                showInstitutionalState();
                break;
            case R.id.tv_type_of_institution:  //机构类型
                 showInstitution();
                break;
            case tv_first_agent:  //第一代理人
                showFirstAgent();
                break;
            case R.id.tv_first_agent_type: //第一代理人类型
                showFirstAgentType();
                break;
            case R.id.tv_saved: //保存
                break;
            case R.id.tv_return: //返回
                break;
        }
    }

    /**
     * 加载用户类型数据 并弹出框
     */
    private void showCustomertype() {
        if(institutionalStateBeenlList !=null){
            institutionalStateBeenlList.clear();
        }
        institutionalStateBeenlList = new ArrayList<>();
        institutionalStateBeenlList.add(new InstitutionalStateBean("系统管理","1"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("机构用户","2"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("个人用户","3"));
        showPop(llCustomerType,institutionalStateBeenlList, YONGHULEIXING,1); //用户类型数据
    }

    //加载第一代理人类型数据并弹出框
    private void showFirstAgentType() {
        if(institutionalStateBeenlList !=null){
            institutionalStateBeenlList.clear();
        }
        institutionalStateBeenlList = new ArrayList<>();
        institutionalStateBeenlList.add(new InstitutionalStateBean("直营","0"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("承包","1"));
        showPop(llFirstAgentType,institutionalStateBeenlList, DIYIDAILIRENLEIXING,1); //第一代理人类型
    }

    private void showFirstAgent() {
        if(institutionalStateBeenlList !=null){
            institutionalStateBeenlList.clear();
        }
        institutionalStateBeenlList = new ArrayList<>();
        institutionalStateBeenlList.add(new InstitutionalStateBean("个人","0"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("铁青","1"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("邮政","2"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("企业","3"));

        showPop(llFirstAgent,institutionalStateBeenlList, DIYIDAILIREN,1); //第一代理人
    }

    private void showInstitutionalState() {
        if(institutionalStateBeenlList !=null){
            institutionalStateBeenlList.clear();
        }
        institutionalStateBeenlList = new ArrayList<>();
        institutionalStateBeenlList.add(new InstitutionalStateBean("正常","0"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("待审核","1"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("审核不通过","2"));
        institutionalStateBeenlList.add(new InstitutionalStateBean("冻结","3"));

        showPop(llInstitutionalState,institutionalStateBeenlList, JIGOUZHUANGTAI,1); //弹出机构状态
    }


    /**
     * 请求结束后获取相应数据
     * @param json
     * @param tag
     */
    @Override
    protected void dataSuccess(JSONObject json, String tag,String pageNumber) {
        super.dataSuccess(json, tag,pageNumber);
        switch (tag){
            case GETDICTARY:
                getInstitutionData(json);
                break;
        }
    }



    //获取机构类型数据
    private void getInstitutionData(JSONObject json) {
        Gson gson = new Gson();
        TypeOfInstitutionBean typeOfInstitutionBean = gson.fromJson(json.toString(), TypeOfInstitutionBean.class);
        dataTypeOfInstitution = typeOfInstitutionBean.getData();
        showInstitutionPop(dataTypeOfInstitution);
    }

    /**
     * 机构类型
     */
    private void showInstitution() {
        String url =Define.URL+"sys/getDictAry";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("dictType","sys_office_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url,jsonObject,GETDICTARY,"1",true, true);  //请求网络

    }


    //弹出机构类型
    private void showInstitutionPop(List<? extends Object> data) {
           showPop(llTypeOfInstitution,data, JIGOULEIXING,0);
    }

    //弹出气泡点击条目获取的数据
    @Override
    protected void itemPopClick(AdapterView<?> adapterView, View view, int i, long l,String tag) {
        super.itemPopClick(adapterView, view, i, l,tag);
       switch (tag){
           case JIGOULEIXING:
               TypeOfInstitutionBean.DataBean dataBean = dataTypeOfInstitution.get(i);
               linTypeOfInstitution = dataBean.getValue();  //临时记录机构类型
               tvTypeOfInstitution.setText(dataBean.getLabel());
               break;
           case JIGOUZHUANGTAI:
               InstitutionalStateBean institutionalStateBean = institutionalStateBeenlList.get(i);
               linInstitutionalState = institutionalStateBean.value_;
               tvInstitutionalState.setText(institutionalStateBean.stateName);
               break;
           case DIYIDAILIREN:
               InstitutionalStateBean institutionalStateBean2 = institutionalStateBeenlList.get(i);
               linFirstAgent = institutionalStateBean2.value_; //临时记录第一代理人
               tvFirstAgent.setText(institutionalStateBean2.stateName);
               break;
           case DIYIDAILIRENLEIXING:
               InstitutionalStateBean institutionalStateBean3 = institutionalStateBeenlList.get(i);
               linFirstAgentType = institutionalStateBean3.value_; //临时记录第一代理人类型
               tvFirstAgentType.setText(institutionalStateBean3.stateName);
               break;
           case YONGHULEIXING:
               InstitutionalStateBean institutionalStateBean4 = institutionalStateBeenlList.get(i);
               linCustomerType = institutionalStateBean4.value_;  //临时记录用户类型
               tvFirstAgentType.setText(institutionalStateBean4.stateName);
               break;
       }
    }

    /**
     * 为弹出框设置数据
     * @param holder
     * @param o
     * @param tag
     */
    @Override
    protected void setDataGetView(ViewHolder holder, Object o, String tag) {
        super.setDataGetView(holder, o, tag);
         TextView textView = holder.getView(R.id.tv_text);
        switch (tag){
            case JIGOULEIXING:
                TypeOfInstitutionBean.DataBean data = (TypeOfInstitutionBean.DataBean) o;
                textView.setText(data.getLabel());
                break;
            case JIGOUZHUANGTAI:

            case DIYIDAILIREN:

            case DIYIDAILIRENLEIXING:

            case YONGHULEIXING:
                InstitutionalStateBean institutionalStateBean2 = (InstitutionalStateBean) o;
                textView.setText(institutionalStateBean2.stateName);
                break;

        }
    }

    /**
     * 获取用户角色类型
     */
    private void showRoleType() {
        cpd.show();
        JSONObject jsonObject = new JSONObject();
        String url = Define.URL+"sys/getRoles";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(AdminUserDataUpdate.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                json.toString();
                Gson gson = new Gson();
                UserRoleTypeBean userRoleTypeBean = gson.fromJson(json.toString(), UserRoleTypeBean.class);
                List<UserRoleTypeBean.ListBean> list = userRoleTypeBean.getList();
                showPopRole(list);
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
    MyPopWindow myPopWindowRole;
    /*
     弹出用户角色
     */
    private void showPopRole(List<UserRoleTypeBean.ListBean> list) {
        final List<UserRoleTypeBean.ListBean>  roleList = list;

        if(myPopWindowRole != null){
            myPopWindowRole.dissmiss();
        }
        myPopWindowRole = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                ListView lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                llUserRoles.getLocationOnScreen(arr);
                view.measure(0, 0);
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                xs = arr[0] + llUserRoles.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + llUserRoles.getHeight();
                CommonAdapter<UserRoleTypeBean.ListBean> commonAdapter = new CommonAdapter<UserRoleTypeBean.ListBean>(AdminUserDataUpdate.this,roleList,R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, UserRoleTypeBean.ListBean listBean) {
                        holder.setText(tv_text,listBean.getName());
                    }
                };

                lv_audit_status.setAdapter(commonAdapter);
                lv_audit_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        UserRoleTypeBean.ListBean listBean = roleList.get(i);
                        linRoleIdString = listBean.getId();  //临时用户角色id
                        tvUserRoles.setText(listBean.getName());
                        myPopWindowRole.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return  R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(this).create();

        myPopWindowRole.keyCodeDismiss(true); //返回键关闭
        myPopWindowRole.showAtLocation(llUserRoles,Gravity.NO_GRAVITY,(int)xs,(int)ys);
    }

    /**
     * 归属机构弹出框
     */
    private void showOwnership() {
        mapData = new MapData(this, cpd, false) {
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();
                if (myPopWindowOwnership != null) {
                    myPopWindowOwnership.dissmiss();
                }
                myPopWindowOwnership = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        ListView lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                        mapData.setTrainOwnershipData(lv_audit_status);
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node officeId) {
                                linOfficeId = String.valueOf(officeId.getAllTreeNode().id);
                                tvOwnershipInstitution.setText(officeId.getAllTreeNode().name);
                            //    if(officeId.getAllTreeNode().type.equals("2")) {
                                    myPopWindowOwnership.dissmiss();
                              //  }
                            }
                        });
                        int[] arr = new int[2];
                        llOwnershipInstitution.getLocationOnScreen(arr);
                        view.measure(0, 0);
                        xs = arr[0] + llOwnershipInstitution.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + llOwnershipInstitution.getHeight();
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop3;
                    }
                }.popupWindowBuilder(AdminUserDataUpdate.this).create();
                myPopWindowOwnership.keyCodeDismiss(true); //返回键关闭
                myPopWindowOwnership.showAtLocation(llOwnershipInstitution, Gravity.NO_GRAVITY, (int) xs, (int) ys);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(volleyNetCommon != null){
            volleyNetCommon.getRequestQueue().cancelAll("getRoles");
            volleyNetCommon.getRequestQueue().cancelAll(GETDICTARY);
        }
    }
}
