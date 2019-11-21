package com.huoniao.oc.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.StationWindowManageBean;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

import static com.huoniao.oc.R.id.layout_auditedContent;

/**
 * Created by Administrator on 2017/7/13.
 */

public class AnchoredAuditDetailsA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_outletsName)
    TextView tvOutletsName;
    @InjectView(R.id.tv_outletsAccount)
    TextView tvOutletsAccount;
    @InjectView(R.id.tv_windowNumber)
    TextView tvWindowNumber;
    @InjectView(R.id.tv_corpName)
    TextView tvCorpName;
    @InjectView(R.id.tv_corpMobile)
    TextView tvCorpMobile;
    @InjectView(R.id.tv_corpIdNum)
    TextView tvCorpIdNum;
    @InjectView(R.id.tv_fuZeRenName)
    TextView tvFuZeRenName;
    @InjectView(R.id.tv_fuZeRenMobile)
    TextView tvFuZeRenMobile;
    @InjectView(R.id.tv_fuZeRenIdNum)
    TextView tvFuZeRenIdNum;
    @InjectView(R.id.tv_relationWindowNumber)
    TextView tvRelationWindowNumber;
    @InjectView(R.id.tv_newOutletsName)
    TextView tvNewOutletsName;
    @InjectView(R.id.tv_newFuZeName)
    TextView tvNewFuZeName;
    @InjectView(R.id.tv_newFuZeMobile)
    TextView tvNewFuZeMobile;
    @InjectView(R.id.tv_newFuZeIdNum)
    TextView tvNewFuZeIdNum;
    @InjectView(R.id.iv_certificate)
    ImageView ivCertificate;
    @InjectView(R.id.iv_contractFrist)
    ImageView ivContractFrist;
    @InjectView(R.id.iv_contractLast)
    ImageView ivContractLast;
    @InjectView(R.id.iv_idCardPositive)
    ImageView ivIdCardPositive;
    @InjectView(R.id.iv_idCardOpposite)
    ImageView ivIdCardOpposite;
    @InjectView(R.id.et_auditReason)
    EditText etAuditReason;
    @InjectView(R.id.layout_noRelieveContent)
    LinearLayout layoutNoRelieveContent;
    @InjectView(R.id.tv_auditReason)
    TextView tvAuditReason;
    @InjectView(R.id.layout_auditReason)
    LinearLayout layoutAuditReason;
    @InjectView(R.id.tv_auditState)
    TextView tvAuditState;
    @InjectView(layout_auditedContent)
    LinearLayout layoutAuditedContent;
    @InjectView(R.id.tv_pass)
    TextView tvPass;
    @InjectView(R.id.layout_pass)
    LinearLayout layoutPass;
    @InjectView(R.id.view_shuXian)
    View viewShuXian;
    @InjectView(R.id.tv_noPass)
    TextView tvNoPass;
    @InjectView(R.id.layout_noPass)
    LinearLayout layoutNoPass;
    @InjectView(R.id.layout_button)
    LinearLayout layoutButton;

    private Intent intent;
    private String outletsName, outletsAccount, windowNumber, corpName, corpMobile,
            corpIdNum, fuZeRenName, fuZeRenMobile, fuZeRenIdNum, relationWindowNumber,
            newOutletsName, newFuZeName, newFuZeMobile, newFuZeIdNum, certificate,
            contractFrist, contractLast, idCardPositive, idCardOpposite, auditState,
            operateTime, auditReason, childId;
    private Bitmap bmCertificate, bmContractFrist, bmContractLast, bmIdCardPositive,
            bmIdCardOpposite;

    private static final int SCALE = 2;//照片放大缩小比例

    private final String  staContIndexSrcTag  ="staContIndexSrc";//车站合同首页路径标识
    private final String  staContLastSrcTag  ="staContLastSrc";//车站合同末页路径标识
    private final String  operatorCardforntSrcTag ="operatorCardforntSrc";//负责人身份证正面路径标识
    private final String  operatorCardrearSrcTag ="operatorCardrearSrc";//负责人身份证反面路径标识
    private final String  fareAuthorizationSrcTag="fareAuthorizationSrc";//票款汇缴授权书标识
    private File staContIndexSrcFile, staContLastSrcFile, operatorCardforntSrcFile,
            operatorCardrearSrcFile, fareAuthorizationSrcFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_anchored);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        StationWindowManageBean windowManage = (StationWindowManageBean) ObjectSaveUtil.readObject(AnchoredAuditDetailsA.this, "windowAnchoredInfo");
        auditState = windowManage.getAuditState();
        if (auditState != null && !auditState.isEmpty()) {
            if (Define.AUDIT_STATE_PASS.equals(auditState)) {
                tvAuditState.setText("审核通过");
                layoutNoRelieveContent.setVisibility(View.GONE);
                layoutButton.setVisibility(View.GONE);
                layoutAuditedContent.setVisibility(View.VISIBLE);
                auditReason = windowManage.getAuditReason();
                if (auditReason != null && !auditReason.isEmpty()) {
                    tvAuditReason.setText(auditReason);
                }
            } else if (Define.AUDIT_STATE_WAIT.equals(auditState)) {
                tvAuditState.setText("待审核");
//                layoutAuditedContent.setVisibility(View.GONE);
            } else if (Define.AUDIT_STATE_REFUSE.equals(auditState)) {
                tvAuditState.setText("审核不通过");
                layoutNoRelieveContent.setVisibility(View.GONE);
                layoutButton.setVisibility(View.GONE);
                layoutAuditedContent.setVisibility(View.VISIBLE);
                auditReason = windowManage.getAuditReason();
                if (auditReason != null && !auditReason.isEmpty()) {
                    tvAuditReason.setText(auditReason);
                }
            } else if (Define.ANCHORED_STATE_REMOVE.equals(auditState)) {
                tvAuditState.setText("解除挂靠");
//              layoutAuditReason.setVisibility(View.GONE);
                layoutNoRelieveContent.setVisibility(View.GONE);
                layoutButton.setVisibility(View.GONE);
                layoutAuditedContent.setVisibility(View.VISIBLE);
                if (auditReason != null && !auditReason.isEmpty()) {
                    tvAuditReason.setText(auditReason);
                }
            }
        }

        outletsName = windowManage.getOfficeName();
        if (outletsName != null && !outletsName.isEmpty()) {
            tvOutletsName.setText(outletsName);
        }else {
            tvOutletsName.setText("");
        }
        childId = windowManage.getId();

        outletsAccount = windowManage.getOfficeCode();
        if (outletsAccount != null && !outletsAccount.isEmpty()) {
            tvOutletsAccount.setText(outletsAccount);
        }else {
            tvOutletsAccount.setText("");
        }

        windowNumber = windowManage.getOfficeWinNumber();
        if (windowNumber != null && !windowNumber.isEmpty()) {
            tvWindowNumber.setText(windowNumber);
        }else {
            tvWindowNumber.setText("");
        }
        corpName = windowManage.getOfficeCorpName();
        if (corpName != null && !corpName.isEmpty()) {
            tvCorpName.setText(corpName);
        }else {
            tvCorpName.setText("");
        }
        corpMobile = windowManage.getOfficeCorpMobile();
        if (corpMobile != null && !corpMobile.isEmpty()) {
            tvCorpMobile.setText(corpMobile);
        }else {
            tvCorpMobile.setText("");
        }
        corpIdNum = windowManage.getOfficeCorpIdNum();
        if (corpIdNum != null && !corpIdNum.isEmpty()) {
            tvCorpIdNum.setText(corpIdNum);
        }else {
            tvCorpIdNum.setText("");
        }
        fuZeRenName = windowManage.getOfficeOperatorName();
        if (fuZeRenName != null && !fuZeRenName.isEmpty()) {
            tvFuZeRenName.setText(fuZeRenName);
        }

        fuZeRenMobile = windowManage.getOfficeOperatorMoblie();
        if (fuZeRenMobile != null && !fuZeRenMobile.isEmpty()) {
            tvFuZeRenMobile.setText(fuZeRenMobile);
        }else {
            tvFuZeRenMobile.setText("");
        }

        fuZeRenIdNum = windowManage.getOfficeOperatorIdNum();
        if (fuZeRenIdNum != null && !fuZeRenIdNum.isEmpty()) {
            tvFuZeRenIdNum.setText(fuZeRenIdNum);
        }else {
            tvFuZeRenIdNum.setText("");
        }

        newOutletsName = windowManage.getAgencyName();
        if (newOutletsName != null && !newOutletsName.isEmpty()) {
            tvNewOutletsName.setText(newOutletsName);
        }else {
            tvNewOutletsName.setText("");
        }

        newFuZeMobile = windowManage.getOperatorMobile();
        if (newFuZeMobile != null && !newFuZeMobile.isEmpty()) {
            tvNewFuZeMobile.setText(newFuZeMobile);
        }else {
            tvNewFuZeMobile.setText("");
        }

        newFuZeName = windowManage.getOperatorName();
        if (newFuZeName != null && !newFuZeName.isEmpty()) {
            tvNewFuZeName.setText(newFuZeName);
        }else {
            tvNewFuZeName.setText("");
        }

        newFuZeIdNum = windowManage.getOperatorIdNum();
        if (newFuZeIdNum != null && !newFuZeIdNum.isEmpty()) {
            tvNewFuZeIdNum.setText(newFuZeIdNum);
        }else {
            tvNewFuZeIdNum.setText("");
        }

        relationWindowNumber = windowManage.getWinNumber();
        if (relationWindowNumber != null && !relationWindowNumber.isEmpty()) {
            tvRelationWindowNumber.setText(relationWindowNumber);
        }else {
            tvRelationWindowNumber.setText("");
        }
        certificate = windowManage.getFareAuthorizationSrc();
        try {
            if (certificate != null && !certificate.isEmpty()){
//                getDocumentImage2(certificate, "fareAuthorization", 0);
                getDocumentImage3(certificate, fareAuthorizationSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        contractFrist = windowManage.getStaContIndexSrc();
        try {
            if (contractFrist != null && !contractFrist.isEmpty()){
//                getDocumentImage2(contractFrist, "staContIndex", 0);
                getDocumentImage3(contractFrist, staContIndexSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        contractLast = windowManage.getStaContLastSrc();
        try {
            if (contractLast != null && !contractLast.isEmpty()){
//                getDocumentImage2(contractLast, "staContLast", 0);
                getDocumentImage3(contractLast, staContLastSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        idCardPositive = windowManage.getOperatorCardforntSrc();
        try {
            if (idCardPositive != null && !idCardPositive.isEmpty()){
//                getDocumentImage2(idCardPositive, "operatorCardfornt", 0);
                getDocumentImage3(idCardPositive, operatorCardforntSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        idCardOpposite = windowManage.getOperatorCardrearSrc();
        try {
            if (idCardOpposite != null && !idCardOpposite.isEmpty()){
//                getDocumentImage2(idCardOpposite, "operatorCardrear", 0);
                getDocumentImage3(idCardOpposite, operatorCardrearSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取图片回调
        setImgResultLinstener(new ImgResult() {
            @Override
            public void getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr) {
                switch (tag){
                    case staContIndexSrcTag:
                        staContIndexSrcFile = file;
                        glideSetImg(file, ivContractFrist);

                        break;
                    case staContLastSrcTag:
                        staContLastSrcFile = file;
                        glideSetImg(file, ivContractLast);
                        break;

                    case operatorCardforntSrcTag:
                        operatorCardforntSrcFile = file;
                        glideSetImg(file, ivIdCardPositive);
                        break;

                    case operatorCardrearSrcTag:
                        operatorCardrearSrcFile = file;
                        glideSetImg(file, ivIdCardOpposite);
                        break;

                    case fareAuthorizationSrcTag:
                        fareAuthorizationSrcFile = file;
                        glideSetImg(file, ivCertificate);
                        break;
                }
               /* if(i!=-1){
                    documentInformationBeenList.get(i).imageSrc =imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }*/


            }
        });

    }

    /**
     * 通过Glide设置图片到控件上
     */
    private void glideSetImg(File file, final ImageView imageView){
        //设置圆角图片
       /* Glide.with(UpdatePersonalinformation.this).load(file).asBitmap().centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });*/
        //设置普通图片
        try {
            Glide.with(AnchoredAuditDetailsA.this).load(file).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   /* @Override
    protected void getImageBitmap(Bitmap licenceBitmap, String tag,int postion) {
        if (licenceBitmap != null) {
            if ("fareAuthorization".equals(tag)) {
                ivCertificate.setImageBitmap(licenceBitmap);
                bmCertificate = licenceBitmap;

            }else if ("staContIndex".equals(tag)){
                ivContractFrist.setImageBitmap(licenceBitmap);
                bmContractFrist = licenceBitmap;
            }else if ("staContLast".equals(tag)){
                ivContractLast.setImageBitmap(licenceBitmap);
                bmContractLast = licenceBitmap;
            }else if ("operatorCardfornt".equals(tag)){
                ivIdCardPositive.setImageBitmap(licenceBitmap);
                bmIdCardPositive = licenceBitmap;
            }else if ("operatorCardrear".equals(tag)){
                ivIdCardOpposite.setImageBitmap(licenceBitmap);
                bmIdCardOpposite = licenceBitmap;
            }
        }
    }*/

    @OnClick({R.id.iv_back, R.id.iv_certificate, R.id.iv_contractFrist, R.id.iv_contractLast, R.id.iv_idCardPositive, R.id.iv_idCardOpposite, R.id.layout_pass, R.id.layout_noPass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_certificate:
                if (fareAuthorizationSrcFile != null) {
                    myEnlargeImage(fareAuthorizationSrcFile);
                }
                break;
            case R.id.iv_contractFrist:
                if (staContIndexSrcFile != null) {
                    myEnlargeImage(staContIndexSrcFile);
                }
                break;
            case R.id.iv_contractLast:
                if (staContLastSrcFile != null) {
                    myEnlargeImage(staContLastSrcFile);
                }
                break;
            case R.id.iv_idCardPositive:
                if (operatorCardforntSrcFile != null) {
                    myEnlargeImage(operatorCardforntSrcFile);
                }
                break;
            case R.id.iv_idCardOpposite:
                if (operatorCardrearSrcFile != null) {
                    myEnlargeImage(operatorCardrearSrcFile);
                }
                break;
            case R.id.layout_pass:
                anchoredReview(Define.AUDIT_STATE_PASS);
                break;
            case R.id.layout_noPass:
                anchoredReview(Define.AUDIT_STATE_REFUSE);
                break;
        }
    }

    /**
     * 放大查看图片
     * @param
     */
    private void myEnlargeImage(final File file){
//        final Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() * SCALE, bitmap.getHeight() * SCALE);
        MyPopWindow myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                PhotoView iv_enlarge = (PhotoView) view.findViewById(R.id.iv_enlarge);
//                iv_enlarge.setImageBitmap(newBitmap);
                try {
                    Glide.with(AnchoredAuditDetailsA.this).load(file).into(iv_enlarge);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            protected int layout() {
                return R.layout.pop_lookimg;
            }
        }.popWindowTouch(AnchoredAuditDetailsA.this).showAtLocation(ivBack, Gravity.CENTER,0,0);
    }

    /**
     * 窗口号挂靠审核
     *
     * @param reviewStatus
     */

    private void anchoredReview(String reviewStatus) {

        auditReason = etAuditReason.getText().toString().trim();
        if (auditReason == null || auditReason.isEmpty()) {
            Toast.makeText(AnchoredAuditDetailsA.this, "请输入审核理由", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject auditPrtObj = new JSONObject();
        try {
            auditPrtObj.put("id", childId);
            auditPrtObj.put("auditState", reviewStatus);
            auditPrtObj.put("auditReason", auditReason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cpd.show();


        SessionJsonObjectRequest reviewUserRequest = new SessionJsonObjectRequest(Request.Method.POST,
                Define.URL + "fb/auditAgencyConnect", auditPrtObj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("data", "response =" + response.toString());
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
                        cpd.dismiss();
                        Toast.makeText(AnchoredAuditDetailsA.this, "审核成功!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(AnchoredAuditDetailsA.this, AnchoredAuditListA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    } else if ("46000".equals(responseCode)) {
                        cpd.dismiss();
                        Toast.makeText(AnchoredAuditDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(AnchoredAuditDetailsA.this, LoginA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    } else {
                        cpd.dismiss();
                        Toast.makeText(AnchoredAuditDetailsA.this, "系统错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(AnchoredAuditDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

            }
        });
        // 解决重复请求后台的问题
        reviewUserRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        reviewUserRequest.setTag("reviewAnchored");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(reviewUserRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("reviewAnchored");

    }
}
