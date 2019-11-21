package com.huoniao.oc.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AdminWindowAnchoredListBean;
import com.huoniao.oc.bean.Document;
import com.huoniao.oc.bean.DocumentInformationBean;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.MyGridView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.EditFilterUtils;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

import static com.huoniao.oc.R.id.tv_no_ok;

public class AdminWindowAnchoredDetails extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;  //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.tv_eebit_account)
    TextView tvEebitAccount;   //扣款账号
    @InjectView(R.id.tv_username)
    TextView tvUsername;  //用户姓名
    @InjectView(R.id.tv_outlets_window_number)
    TextView tvOutletsWindowNumber; //代售点窗口号
    @InjectView(R.id.tv_consignee_name)
    TextView tvConsigneeName; // 代售点名称
    @InjectView(R.id.tv_legal_person_name)
    TextView tvLegalPersonName; //法人姓名
    @InjectView(R.id.tv_region)
    TextView tvRegion;  //区域
    @InjectView(R.id.tv_outlets_window_number2)
    TextView tvOutletsWindowNumber2; //代售点窗口号2
    @InjectView(R.id.tv_consignee_name2)
    TextView tvConsigneeName2; //代售点名称
    @InjectView(R.id.tv_person_in_charge)
    TextView tvPersonInCharge; //负责人
    @InjectView(R.id.tv_name_of_person_in_charge_phone)
    TextView tvNameOfPersonInCharge;  //负责人手机
    @InjectView(R.id.tv_person_in_charge_id)
    TextView tvPersonInChargeId; //负责人身份证
    @InjectView(R.id.gv_enclosure)
    MyGridView gvEnclosure;  //证件资料
    @InjectView(R.id.tv_audit_status)
    TextView tvAuditStatus;  //审核状态
    @InjectView(R.id.tv_audit_instructions)
    EditText tvAuditInstructions;   //审核状态说明
    @InjectView(R.id.tv_return)
    TextView tvReturn;  //返回   或者 通过
    @InjectView(tv_no_ok)
    TextView tvNoOk;    //不通过
    @InjectView(R.id.activity_admin_window_anchored_details)
    LinearLayout activityAdminWindowAnchoredDetails;

    @InjectView(R.id.ll_click_account_details)
    LinearLayout ll_click_account_details;
    private String pendingAudit;

    private List<DocumentInformationBean> documentInformationBeenList = new ArrayList<>(); //证件集合

    private final String  photoStrTag ="photostr";  //头像
    private final String  corpCardforntSrcTag ="corpCardforntSrc"; //法人身份证正面路径
    private final String  corpLicenceSrcTag ="corpLicenceSrc";//营业执照路径
    private final String  corpCardrearSrcTag  ="corpCardrearSrc";//法人身份证反面路径
    private final String  staContIndexSrcTag  ="staContIndexSrc";//车站合同首页路径
    private final String  staContLastSrcTag  ="staContLastSrc";//合同盖章末页路径
    private final String  operatorCardforntSrcTag ="operatorCardforntSrc";//负责人身份证正面路径
    private final String  operatorCardrearSrcTag ="operatorCardrearSrc";//负责人身份证反面路径
    private final String  fareAuthorizationSrcTag="fareAuthorizationSrc";//授权协议
    private CommonAdapter commonAdapter;
    private AdminWindowAnchoredListBean.DataBean dataDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_window_anchored_details);
        ButterKnife.inject(this);
        initWidget();
        initData();
        initGrid();
    }

    private void initWidget() {
        tvTitle.setText("窗口挂靠详情");
        gvEnclosure.setOnItemClickListener(this);
        tvAuditInstructions.setFilters(new InputFilter[]{EditFilterUtils.inputFilter,new InputFilter.LengthFilter(100)});
    }

    /**
     * 初始化数据
     */
    private void initData() {
         pendingAudit = getIntent().getStringExtra("pendingAudit"); //待审核进来
         if(pendingAudit !=null){
             tvReturn.setText("通过");
             //tvNoOk.setVisibility(View.VISIBLE);
             tvAuditInstructions.setEnabled(true);
             setPremissionShowHideView(Premission.FB_AGENCYCONNECT_AUDIT, tvReturn); //判断当前用户是否拥有审核通过不通过权限
             setPremissionShowHideView(Premission.FB_AGENCYCONNECT_AUDIT, tvNoOk); //判断当前用户是否拥有审核通过不通过权限
         }else{
             tvReturn.setVisibility(View.VISIBLE);
         }

        //-----------------------------------------挂靠账号信息
        //窗口挂靠数据
         dataDetails = (AdminWindowAnchoredListBean.DataBean) getIntent().getSerializableExtra("anchored");
         String officeCode =  dataDetails.getOfficeCode()==null ? "": dataDetails.getOfficeCode();  //扣款账号
         tvEebitAccount.setText(officeCode);
         String operatorName =  dataDetails.getOfficeOperatorName()==null ? "": dataDetails.getOfficeOperatorName();
         tvUsername.setText(operatorName);//用户姓名  负责人姓名
          String corpName = dataDetails.getOfficeCorpName() == null ?"": dataDetails.getOfficeCorpName();
         tvLegalPersonName.setText(corpName); //法人
         String winNumber =  dataDetails.getOfficeWinNumber() ==null ?"": dataDetails.getOfficeWinNumber();
         tvOutletsWindowNumber.setText(winNumber);//代售点窗口号
          String name =  dataDetails.getOfficeName() ==null ? "": dataDetails.getOfficeName();
         tvConsigneeName.setText(name); ////代售点名称


        //-------------------------------------- 本窗口号信息
        String winNumber2 =  dataDetails.getWinNumber()==null ?"": dataDetails.getWinNumber();
        tvOutletsWindowNumber2.setText(winNumber2); // 本窗口号信息  代售点窗口号
        String agencyName = dataDetails.getAgencyName() == null ? "": dataDetails.getAgencyName(); //代售点名称
        tvConsigneeName2.setText(agencyName);
        String operatorName2 =  dataDetails.getOperatorName() == null ?"": dataDetails.getOperatorName(); //负责人
        tvPersonInCharge.setText(operatorName2);
       String operatorMobile = dataDetails.getOperatorMobile() == null ?"": dataDetails.getOperatorMobile();//负责人手机
        tvNameOfPersonInCharge.setText(operatorMobile);
        String operatoIdNum = dataDetails.getOperatorIdNum()==null ?"": dataDetails.getOperatorIdNum();    //负责人身份证号
        tvPersonInChargeId.setText(operatoIdNum);
        AdminWindowAnchoredListBean.DataBean.JurisAreaBean jurisArea = dataDetails.getJurisArea();
        if(jurisArea !=null){
            String name1 = jurisArea.getName()==null ?"":jurisArea.getName();//区域
            tvRegion.setText(name1);
        }

        String auditState = dataDetails.getAuditState()==null ? "": dataDetails.getAuditState();//状态
        tvAuditStatus.setText(switchState(auditState));
        String auditReason  = dataDetails.getAuditReason() == null ?"": dataDetails.getAuditReason(); //审核理由
        tvAuditInstructions.setText(auditReason);

        showImage();

        //获取图片回调
        setImgResultLinstener(new ImgResult() {
            @Override
            public void getImageFile(File file,String imgUrl, String tag, int i, String linkUrlStr) {
                if(i!=-1){
                    documentInformationBeenList.get(i).imageSrc =imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    /**
     *   审核状态
     *  0 通过 1 待审核 2 不通过 4 解除绑定
     *
     * @param state
     * @return
     */
    private String switchState(String state){
        switch (state){
            case "0":
                return "审核通过";
            case "1":
                return "待审核";
            case "2":
                return "不通过";
            case "3":
                return "解除绑定";
        }
        return "";
    }

    @OnClick({R.id.iv_back, R.id.tv_eebit_account, R.id.tv_return, tv_no_ok,R.id.ll_click_account_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_eebit_account:
                break;
            case R.id.tv_return: //通过 和返回
                if(pendingAudit !=null){
                    //做网络请求
                    String id = dataDetails.getId();
                    String auditInstructions = tvAuditInstructions.getText().toString().trim();
                    if(auditInstructions.isEmpty()){
                        Toast.makeText(this, "审核情况说明不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    audit(id,"0",auditInstructions);
                }else{
                    finish();
                }
                break;
            case tv_no_ok:  //不通过
                String id = dataDetails.getId();
                String auditInstructions = tvAuditInstructions.getText().toString().trim();
                if(auditInstructions.isEmpty()){
                    Toast.makeText(this, "审核情况说明不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                audit(id,"2",auditInstructions);
                break;
            case R.id.ll_click_account_details:
                 //点击查看该账号 详细资料
                Intent intent = new Intent(AdminWindowAnchoredDetails.this,AdminUserDetails.class);
                intent.putExtra("loginName",dataDetails.getOfficeCode());  // 扣款账号
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }

    //审核
    private void audit(String id,String auditState,String auditReason) {
        String url = Define.URL+ "fb/auditAgencyConnect";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("auditState",auditState);
            jsonObject.put("auditReason",auditReason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url,jsonObject,"auditAgencyConnect","0",true, true); //最后一个参数随便 没有作用
    }


    //请求结果处理
    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
           case "auditAgencyConnect":
               setResult(101);
               finish();
               break;
       }

    }

    /**
     * 初始化证件布局
     */
    private void initGrid() {

        documentInformationBeenList.add(new DocumentInformationBean(fareAuthorizationSrcTag, "授权协议"));
        documentInformationBeenList.add(new DocumentInformationBean(staContIndexSrcTag, "车站合同首页"));
        documentInformationBeenList.add(new DocumentInformationBean(staContLastSrcTag, "合同盖章末页"));
        documentInformationBeenList.add(new DocumentInformationBean(operatorCardforntSrcTag, "身份证正面"));
        documentInformationBeenList.add(new DocumentInformationBean(operatorCardrearSrcTag, "身份证反面"));
        setGridAdapter();


    }

    //显示图片
    private void showImage() {
        String fareAuthorizationSrc = dataDetails.getFareAuthorizationSrc();//授权协议
        String staContIndexSrc = dataDetails.getStaContIndexSrc();          //车站合同首页路径
        String staContLastSrc = dataDetails.getStaContLastSrc();            //合同盖章末页路径
        String operatorCardforntSrc = dataDetails.getOperatorCardforntSrc();//负责人身份证正面路径
        String operatorCardrearSrc = dataDetails.getOperatorCardrearSrc();  //负责人身份证反面路径


        //需要和初始化一样按照先后顺序添加
        List<Document> list = new ArrayList<>();
        list.add(new Document(fareAuthorizationSrc, fareAuthorizationSrcTag));
        list.add(new Document(staContIndexSrc, staContIndexSrcTag));
        list.add(new Document(staContLastSrc, staContLastSrcTag));
        list.add(new Document(operatorCardforntSrc, operatorCardforntSrcTag));
        list.add(new Document(operatorCardrearSrc, operatorCardrearSrcTag));


        for (int i = 0; i < list.size(); i++) {
            Document document = list.get(i);
            try {
                if(!document.srcs.isEmpty()) {
                    getDocumentImage3(document.srcs, document.srcTag, i, false, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 设置资料布局
     */
    private void setGridAdapter() {
        commonAdapter = new CommonAdapter<DocumentInformationBean>(this,documentInformationBeenList, R.layout.item_document_image_layout) {
                @Override
                public void convert(ViewHolder holder, DocumentInformationBean documentInformationBean) {
                    ImageView iv_image = holder.getView(R.id.iv_image);
                    TextView tv_image_name= holder.getView(R.id.tv_image_name);
                    TextView tv_xin = holder.getView(R.id.tv_xin);
                    String imgName =documentInformationBean.imageName;


                    iv_image.setTag(documentInformationBean.imageFlag);
                    if(iv_image.getTag().equals(documentInformationBean.imageFlag)){
                        try {
                            Glide.with(AdminWindowAnchoredDetails.this).load(documentInformationBean.imageFile).into(iv_image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_image_name.setText(documentInformationBean.imageName);
                }
            };
        gvEnclosure.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();

    }

    //点击查看大图
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DocumentInformationBean documentInformationBean = documentInformationBeenList.get(i);
        final File file= documentInformationBean.imageFile;
        MyPopWindow myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                PhotoView documentImage = (PhotoView) view.findViewById(R.id.documentImage);
                if (file != null){
                    try {
                        Glide.with(AdminWindowAnchoredDetails.this).load(file).into(documentImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(AdminWindowAnchoredDetails.this, "无图片信息！", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            protected int layout() {
                return R.layout.documentimage_dialog;
            }
        }.popWindowTouch(AdminWindowAnchoredDetails.this).showAtLocation(view, Gravity.CENTER,0,0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(volleyNetCommon !=null){
            volleyNetCommon.getRequestQueue().cancelAll("auditAgencyConnect");
        }
    }
}
