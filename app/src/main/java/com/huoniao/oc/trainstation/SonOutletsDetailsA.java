package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

import static com.huoniao.oc.R.id.iv_certificate;
import static com.huoniao.oc.R.id.iv_contractLast;

/**
 * Created by Administrator on 2017/5/12.
 */

public class SonOutletsDetailsA extends BaseActivity {
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
    @InjectView(R.id.tv_auditState)
    TextView tvAuditState;
    @InjectView(R.id.bt_confirmRelieve)
    Button btConfirmRelieve;
    @InjectView(R.id.layout_noRelieveContent)
    LinearLayout layoutNoRelieveContent;
    @InjectView(R.id.et_relationWindowNumber)
    EditText etRelationWindowNumber;
    @InjectView(R.id.et_newOutletsName)
    EditText etNewOutletsName;
    @InjectView(R.id.et_newFuZeName)
    EditText etNewFuZeName;
    @InjectView(R.id.et_newFuZeMobile)
    EditText etNewFuZeMobile;
    @InjectView(R.id.et_newFuZeIdNum)
    EditText etNewFuZeIdNum;
    @InjectView(iv_certificate)
    ImageView ivCertificate;
    @InjectView(R.id.iv_contractFrist)
    ImageView ivContractFrist;
    @InjectView(iv_contractLast)
    ImageView ivContractLast;
    @InjectView(R.id.iv_idCardPositive)
    ImageView ivIdCardPositive;
    @InjectView(R.id.iv_idCardOpposite)
    ImageView ivIdCardOpposite;
    @InjectView(R.id.tv_operateTime)
    TextView tvOperateTime;
    @InjectView(R.id.layout_operateTime)
    LinearLayout layoutOperateTime;
    @InjectView(R.id.tv_auditReason)
    TextView tvAuditReason;
    @InjectView(R.id.layout_auditReason)
    LinearLayout layoutAuditReason;

    private String clickTag = "";
    private Intent intent;

    private String childId;

    private String outletsName, outletsAccount, windowNumber, corpName, corpMobile,
            corpIdNum, fuZeRenName, fuZeRenMobile, fuZeRenIdNum, relationWindowNumber,
            newOutletsName, newFuZeName, newFuZeMobile, newFuZeIdNum, certificate,
            contractFrist, contractLast, idCardPositive, idCardOpposite, auditState,
            operateTime, auditReason;
//    private ProgressDialog pd;
    private Bitmap bmCertificate, bmContractFrist, bmContractLast, bmIdCardPositive, bmIdCardOpposite;
    private final String  staContIndexSrcTag  ="staContIndexSrc";//车站合同首页路径标识
    private final String  staContLastSrcTag  ="staContLastSrc";//车站合同末页路径标识
    private final String  operatorCardforntSrcTag ="operatorCardforntSrc";//负责人身份证正面路径标识
    private final String  operatorCardrearSrcTag ="operatorCardrearSrc";//负责人身份证反面路径标识
    private final String  fareAuthorizationSrcTag="fareAuthorizationSrc";//票款汇缴授权书标识
    private File staContIndexSrcFile, staContLastSrcFile, operatorCardforntSrcFile,
            operatorCardrearSrcFile, fareAuthorizationSrcFile;

    private static final int SCALE = 2;//照片放大缩小比例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonoutlets_details);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        StationWindowManageBean staWinUpdata = (StationWindowManageBean) ObjectSaveUtil.readObject(SonOutletsDetailsA.this, "windowAnchoredInfo");
        intent = getIntent();
        clickTag = intent.getStringExtra("clickTag");
        if ("relieveAnchored".equals(clickTag)) {
            layoutNoRelieveContent.setVisibility(View.GONE);
            btConfirmRelieve.setVisibility(View.VISIBLE);
            tvTitle.setText("解除挂靠");


        } else if ("seeDetails".equals(clickTag)) {
            layoutNoRelieveContent.setVisibility(View.VISIBLE);
            btConfirmRelieve.setVisibility(View.GONE);
            tvTitle.setText("窗口号挂靠详情");
//            etNewOutletsName.setKeyListener(null);
//            etRelationWindowNumber.setKeyListener(null);
//            etNewFuZeIdNum.setKeyListener(null);
//            etNewFuZeName.setKeyListener(null);
//            etNewFuZeMobile.setKeyListener(null);


            auditState = staWinUpdata.getAuditState();
            if (auditState != null && !auditState.isEmpty()) {
                if (Define.AUDIT_STATE_PASS.equals(auditState)) {
                    tvAuditState.setText("审核通过");
                    auditReason = staWinUpdata.getAuditReason();
                    if (auditReason != null && !auditReason.isEmpty()) {
                        tvAuditReason.setText(auditReason);
                    }
                } else if (Define.AUDIT_STATE_WAIT.equals(auditState)) {
                    tvAuditState.setText("待审核");
                    layoutAuditReason.setVisibility(View.GONE);
                } else if (Define.AUDIT_STATE_REFUSE.equals(auditState)) {
                    tvAuditState.setText("审核不通过");
                    auditReason = staWinUpdata.getAuditReason();
                    if (auditReason != null && !auditReason.isEmpty()) {
                        tvAuditReason.setText(auditReason);
                    }
                } else if (Define.ANCHORED_STATE_REMOVE.equals(auditState)) {
                    tvAuditState.setText("解除挂靠");
                    layoutAuditReason.setVisibility(View.GONE);
                }
            }

        }

        etNewOutletsName.setEnabled(false);
        etRelationWindowNumber.setEnabled(false);
        etNewFuZeIdNum.setEnabled(false);
        etNewFuZeName.setEnabled(false);
        etNewFuZeMobile.setEnabled(false);

        outletsName = staWinUpdata.getOfficeName();
        if (outletsName != null && !outletsName.isEmpty()) {
            tvOutletsName.setText(outletsName);
        }

        childId = staWinUpdata.getId();

        outletsAccount = staWinUpdata.getOfficeCode();
        if (outletsAccount != null && !outletsAccount.isEmpty()) {
            tvOutletsAccount.setText(outletsAccount);
        }

        windowNumber = staWinUpdata.getOfficeWinNumber();
        if (windowNumber != null && !windowNumber.isEmpty()) {
            tvWindowNumber.setText(windowNumber);
        }
        corpName = staWinUpdata.getOfficeCorpName();
        if (corpName != null && !corpName.isEmpty()) {
            tvCorpName.setText(corpName);
        }
        corpMobile = staWinUpdata.getOfficeCorpMobile();
        if (corpMobile != null && !corpMobile.isEmpty()) {
            tvCorpMobile.setText(corpMobile);
        }
        corpIdNum = staWinUpdata.getOfficeCorpIdNum();
        if (corpIdNum != null && !corpIdNum.isEmpty()) {
            tvCorpIdNum.setText(corpIdNum);
        }
        fuZeRenName = staWinUpdata.getOfficeOperatorName();
        if (fuZeRenName != null && !fuZeRenName.isEmpty()) {
            tvFuZeRenName.setText(fuZeRenName);
        }

        fuZeRenMobile = staWinUpdata.getOfficeOperatorMoblie();
        if (fuZeRenMobile != null && !fuZeRenMobile.isEmpty()) {
            tvFuZeRenMobile.setText(fuZeRenMobile);
        }

        fuZeRenIdNum = staWinUpdata.getOfficeOperatorIdNum();
        if (fuZeRenIdNum != null && !fuZeRenIdNum.isEmpty()) {
            tvFuZeRenIdNum.setText(fuZeRenIdNum);
        }
        newOutletsName = staWinUpdata.getAgencyName();
        if (newOutletsName != null && !newOutletsName.isEmpty()) {
            etNewOutletsName.setText(newOutletsName);
        }

        newFuZeMobile = staWinUpdata.getOperatorMobile();
        if (newFuZeMobile != null && !newFuZeMobile.isEmpty()) {
            etNewFuZeMobile.setText(newFuZeMobile);
        }

        newFuZeName = staWinUpdata.getOperatorName();
        if (newFuZeName != null && !newFuZeName.isEmpty()) {
            etNewFuZeName.setText(newFuZeName);
        }

        newFuZeIdNum = staWinUpdata.getOperatorIdNum();
        if (newFuZeIdNum != null && !newFuZeIdNum.isEmpty()) {
            etNewFuZeIdNum.setText(newFuZeIdNum);
        }

        relationWindowNumber = staWinUpdata.getWinNumber();
        if (relationWindowNumber != null && !relationWindowNumber.isEmpty()) {
            etRelationWindowNumber.setText(relationWindowNumber);
        }
        certificate = staWinUpdata.getFareAuthorizationSrc();

        try {
            if (certificate != null && !certificate.isEmpty()){
//                getDocumentImage2(certificate, "fareAuthorization", 0);
                getDocumentImage3(certificate, fareAuthorizationSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        contractFrist = staWinUpdata.getStaContIndexSrc();
        try {
            if (contractFrist != null && !contractFrist.isEmpty()){
//                getDocumentImage2(contractFrist, "staContIndex", 0);
                getDocumentImage3(contractFrist, staContIndexSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        contractLast = staWinUpdata.getStaContLastSrc();
        try {
            if (contractLast != null && !contractLast.isEmpty()){
//                getDocumentImage2(contractLast, "staContLast", 0);
                getDocumentImage3(contractLast, staContLastSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        idCardPositive = staWinUpdata.getOperatorCardforntSrc();
        try {
            if (idCardPositive != null && !idCardPositive.isEmpty()){
//                getDocumentImage2(idCardPositive, "operatorCardfornt", 0);
                getDocumentImage3(idCardPositive, operatorCardforntSrcTag, 0, false, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        idCardOpposite = staWinUpdata.getOperatorCardrearSrc();
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

        operateTime = staWinUpdata.getUpdateDate();
        if (operateTime != null && !operateTime.isEmpty()) {
            tvOperateTime.setText(operateTime);
        } else {
            layoutOperateTime.setVisibility(View.GONE);
        }
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
            Glide.with(SonOutletsDetailsA.this).load(file).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

  /*  @Override
    protected void getImageBitmap(Bitmap licenceBitmap, String tag,int p) {
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

    @OnClick({R.id.iv_back, R.id.bt_confirmRelieve, iv_certificate, R.id.iv_contractFrist,
            iv_contractLast, R.id.iv_idCardPositive, R.id.iv_idCardOpposite})
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

            case iv_contractLast:
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

            case R.id.bt_confirmRelieve:

                try {
                    relieveAnchored();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                    Glide.with(SonOutletsDetailsA.this).load(file).into(iv_enlarge);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            protected int layout() {
                return R.layout.pop_lookimg;
            }
        }.popWindowTouch(SonOutletsDetailsA.this).showAtLocation(ivBack, Gravity.CENTER,0,0);
    }
//    private String addZeroForNum(String str, int strLength) {
//        int strLen = str.length();
//        if (strLen < strLength) {
//            while (strLen < strLength) {
//                StringBuffer sb = new StringBuffer();
//                sb.append("0").append(str);// 左补0
//                // sb.append(str).append("0");//右补0
//                str = sb.toString();
//                strLen = str.length();
//            }
//        }
//
//        return str;
//    }

    /**
     * 解除挂靠
     *
     * @throws Exception
     */
    private void relieveAnchored() throws Exception {
//        pd = new CustomProgressDialog(SonOutletsDetailsA.this, "正在加载中...", R.anim.frame_anim);
        cpd.show();

        JSONObject jsonObject = new JSONObject();
        try {
            // operateType: 1 查看  2 添加  3 修改  4 取消申请  5 解除绑定

            jsonObject.put("operateType", "5");
            jsonObject.put("id", childId);


        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        String url = Define.URL + "fb/manageAgencyConnect";
        SessionJsonObjectRequest relieveAnchoredRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            cpd.dismiss();
                            Toast.makeText(SonOutletsDetailsA.this, "服务器异常！", Toast.LENGTH_SHORT);
                            return;
                        }

                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(SonOutletsDetailsA.this, "解除挂靠成功！", Toast.LENGTH_SHORT).show();
                                intent = new Intent(SonOutletsDetailsA.this, WindowsAnchoredListA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(SonOutletsDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(SonOutletsDetailsA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                cpd.dismiss();
                                Toast.makeText(SonOutletsDetailsA.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            cpd.dismiss();
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(SonOutletsDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);


            }
        });
        // 解决重复请求后台的问题
        relieveAnchoredRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        relieveAnchoredRequest.setTag("relieveAnchoredRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(relieveAnchoredRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("relieveAnchoredRequest");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bmContractFrist != null){
            bmContractFrist.recycle();
            bmContractFrist = null;
        }
        if (bmContractLast != null){
            bmContractLast.recycle();
            bmContractLast = null;
        }

        if(bmIdCardPositive != null){
            bmIdCardPositive.recycle();
            bmIdCardPositive = null;
        }
        if(bmIdCardOpposite != null){
            bmIdCardOpposite.recycle();
            bmIdCardOpposite = null;
        }

        if(bmCertificate != null){
            bmCertificate.recycle();
            bmCertificate = null;
        }

        System.gc();  //通知垃圾回收站回收垃圾
    }
}
