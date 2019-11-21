package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.huoniao.oc.common.luban.Luban;
import com.huoniao.oc.common.luban.OnCompressListener;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.SelectPicPopupWindow;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.iv_certificate;
import static com.huoniao.oc.R.id.tv_corpIdNum;
import static com.huoniao.oc.R.id.tv_corpMobile;
import static com.huoniao.oc.R.id.tv_corpName;
import static com.huoniao.oc.R.id.tv_fuZeRenIdNum;
import static com.huoniao.oc.R.id.tv_fuZeRenMobile;
import static com.huoniao.oc.R.id.tv_fuZeRenName;
import static com.huoniao.oc.R.id.tv_outletsAccount;
import static com.huoniao.oc.R.id.tv_outletsName;
import static com.huoniao.oc.R.id.tv_windowNumber;

/**
 * Created by Mir Lei on 2017/5/10.
 */

public class WindowsAnchoredApplyA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(tv_outletsName)
    TextView tvOutletsName;
    @InjectView(tv_outletsAccount)
    TextView tvOutletsAccount;
    @InjectView(tv_windowNumber)
    TextView tvWindowNumber;
    @InjectView(tv_corpName)
    TextView tvCorpName;
    @InjectView(tv_corpMobile)
    TextView tvCorpMobile;
    @InjectView(tv_corpIdNum)
    TextView tvCorpIdNum;
    @InjectView(tv_fuZeRenName)
    TextView tvFuZeRenName;
    @InjectView(tv_fuZeRenMobile)
    TextView tvFuZeRenMobile;
    @InjectView(tv_fuZeRenIdNum)
    TextView tvFuZeRenIdNum;
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
    @InjectView(R.id.iv_contractLast)
    ImageView ivContractLast;
    @InjectView(R.id.iv_idCardPositive)
    ImageView ivIdCardPositive;
    @InjectView(R.id.iv_idCardOpposite)
    ImageView ivIdCardOpposite;
    @InjectView(R.id.bt_submit)
    Button btSubmit;
    private String outsName = "";
    private String outsCode = "";
    private String winNumber = "";
    private String corpName = "";
    private String corpMobile = "";
    private String corpIdNum = "";
    private String fuZeRen = "";
    private String fuZeRenPhone ="";
    private String fuZeRenIdNum ="";

    private String outsId = "";
    private String newOutsName = "";
    private String newWinNumber = "";
    private String newFuZeRen = "";
    private String newFuZeRenPhone ="";
    private String newFuZeRenIdNum ="";

    //证件图片相关
    private String certificate = "";//授权书
    private String contractFrist = "";//合同首页
    private String contractLast = "";//合同盖章尾页
    private String idCardPositive = "";//身份证正面
    private String idCardOpposite = "";//身份证反面

    private String newCertificate = "";//修改后的授权书
    private String newContractFrist = "";//修改后的合同首页
    private String newContractLast = "";//修改后的合同盖章尾页
    private String newIdCardPositive = "";//修改后的身份证正面
    private String newIdCardOpposite = "";//修改后的身份证反面

    private String clickTag = "";
    private String childId;
    private Intent intent;
//    private ProgressDialog cpd;

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
        setContentView(R.layout.activity_windowanchored);
        ButterKnife.inject(this);
        initData();

    }

    private void initData() {

        intent = getIntent();
        clickTag = intent.getStringExtra("clickTag");
        if ("windowAnchoredApply".equals(clickTag)) {
            outsId = intent.getStringExtra("id");

            outsName = intent.getStringExtra("name");
            if (outsName != null && !outsName.isEmpty()) {
                tvOutletsName.setText(outsName);
            }
            outsCode = intent.getStringExtra("code");
            if (outsCode != null && !outsCode.isEmpty()) {
                tvOutletsAccount.setText(outsCode);
            }
            winNumber = intent.getStringExtra("winNumber");
            if (winNumber != null && !winNumber.isEmpty()) {
                tvWindowNumber.setText(winNumber);
            }
            corpName = intent.getStringExtra("corpName");
            if (corpName != null && !corpName.isEmpty()) {
                tvCorpName.setText(corpName);
            }
            corpMobile = intent.getStringExtra("corpMobile");
            if (corpMobile != null && !corpMobile.isEmpty()) {
                tvCorpMobile.setText(corpMobile);
            }
            corpIdNum = intent.getStringExtra("corpIdNum");
            if (corpIdNum != null && !corpIdNum.isEmpty()) {
                tvCorpIdNum.setText(corpIdNum);
            }
            fuZeRen = intent.getStringExtra("operatorName");
            if (fuZeRen != null && !fuZeRen.isEmpty()) {
                tvFuZeRenName.setText(fuZeRen);
            }

            fuZeRenPhone = intent.getStringExtra("operatorMobile");
            if (fuZeRenPhone != null && !fuZeRenPhone.isEmpty()) {
                tvFuZeRenMobile.setText(fuZeRenPhone);
            }

            fuZeRenIdNum = intent.getStringExtra("operatorIdNum");
            if (fuZeRenIdNum != null && !fuZeRenIdNum.isEmpty()) {
                tvFuZeRenIdNum.setText(fuZeRenIdNum);
            }
        }else if("updataInfo".equals(clickTag)){
           StationWindowManageBean staWinUpdata = (StationWindowManageBean) ObjectSaveUtil.readObject(WindowsAnchoredApplyA.this, "windowAnchoredInfo");
           outsName = staWinUpdata.getOfficeName();
           if (outsName != null && !outsName.isEmpty()) {
                tvOutletsName.setText(outsName);
            }

            outsCode = staWinUpdata.getOfficeCode();
            if (outsCode != null && !outsCode.isEmpty()) {
                tvOutletsAccount.setText(outsCode);
            }

            winNumber = staWinUpdata.getOfficeWinNumber();
            if (winNumber != null && !winNumber.isEmpty()) {
                tvWindowNumber.setText(winNumber);
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
            fuZeRen = staWinUpdata.getOfficeOperatorName();
            if (fuZeRen != null && !fuZeRen.isEmpty()) {
                tvFuZeRenName.setText(fuZeRen);
            }

            fuZeRenPhone = staWinUpdata.getOfficeOperatorMoblie();
            if (fuZeRenPhone != null && !fuZeRenPhone.isEmpty()) {
                tvFuZeRenMobile.setText(fuZeRenPhone);
            }

            fuZeRenIdNum = staWinUpdata.getOfficeOperatorIdNum();
            if (fuZeRenIdNum != null && !fuZeRenIdNum.isEmpty()) {
                tvFuZeRenIdNum.setText(fuZeRenIdNum);
            }
            newOutsName = staWinUpdata.getAgencyName();
            if (newOutsName != null && !newOutsName.isEmpty()) {
               etNewOutletsName.setText(newOutsName);
            }

            newFuZeRenPhone = staWinUpdata.getOperatorMobile();
            if (newFuZeRenPhone != null && !newFuZeRenPhone.isEmpty()) {
                etNewFuZeMobile.setText(newFuZeRenPhone);
            }

            newFuZeRen = staWinUpdata.getOperatorName();
            if (newFuZeRen != null && !newFuZeRen.isEmpty()) {
                etNewFuZeName.setText(newFuZeRen);
            }

            newFuZeRenIdNum = staWinUpdata.getOperatorIdNum();
            if (newFuZeRenIdNum != null && !newFuZeRenIdNum.isEmpty()) {
                etNewFuZeIdNum.setText(newFuZeRenIdNum);
            }

            newWinNumber = staWinUpdata.getWinNumber();
            if (newWinNumber != null && !newWinNumber.isEmpty()) {
                etRelationWindowNumber.setText(newWinNumber);
            }

            childId = staWinUpdata.getId();

            certificate = staWinUpdata.getFareAuthorizationSrc();

            try {
                if (certificate != null && !certificate.isEmpty()){
//                    getDocumentImage2(certificate, "fareAuthorization", 0);
                    getDocumentImage3(certificate, fareAuthorizationSrcTag, 0, false, "");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            contractFrist = staWinUpdata.getStaContIndexSrc();
            try {
                if (contractFrist != null && !contractFrist.isEmpty()){
//                    getDocumentImage2(contractFrist, "staContIndex", 0);
                    getDocumentImage3(contractFrist, staContIndexSrcTag, 0, false, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            contractLast = staWinUpdata.getStaContLastSrc();
            try {
                if (contractLast != null && !contractLast.isEmpty()){
//                    getDocumentImage2(contractLast, "staContLast", 0);
                    getDocumentImage3(contractLast, staContLastSrcTag, 0, false, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            idCardPositive = staWinUpdata.getOperatorCardforntSrc();
            try {
                if (idCardPositive != null && !idCardPositive.isEmpty()){
//                    getDocumentImage2(idCardPositive, "operatorCardfornt", 0);
                    getDocumentImage3(idCardPositive, operatorCardforntSrcTag, 0, false, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            idCardOpposite = staWinUpdata.getOperatorCardrearSrc();
            try {
                if (idCardOpposite != null && !idCardOpposite.isEmpty()){
//                    getDocumentImage2(idCardOpposite, "operatorCardrear", 0);
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
            Glide.with(WindowsAnchoredApplyA.this).load(file).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   /* @Override
    protected void getImageBitmap(Bitmap licenceBitmap, String tag,int po) {
        if (licenceBitmap != null) {
            if ("fareAuthorization".equals(tag)) {
                ivCertificate.setImageBitmap(licenceBitmap);

            }else if ("staContIndex".equals(tag)){
                ivContractFrist.setImageBitmap(licenceBitmap);

            }else if ("staContLast".equals(tag)){
                ivContractLast.setImageBitmap(licenceBitmap);

            }else if ("operatorCardfornt".equals(tag)){

                ivIdCardPositive.setImageBitmap(licenceBitmap);
            }else if ("operatorCardrear".equals(tag)){

                ivIdCardOpposite.setImageBitmap(licenceBitmap);

            }
        }
    }*/


    @OnClick({R.id.iv_back, iv_certificate, R.id.iv_contractFrist, R.id.iv_contractLast, R.id.iv_idCardPositive, R.id.iv_idCardOpposite, R.id.bt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case iv_certificate:
                startActivityForResult(new Intent(WindowsAnchoredApplyA.this, SelectPicPopupWindow.class), 1);
                break;
            case R.id.iv_contractFrist:
                startActivityForResult(new Intent(WindowsAnchoredApplyA.this, SelectPicPopupWindow.class), 2);
                break;
            case R.id.iv_contractLast:
                startActivityForResult(new Intent(WindowsAnchoredApplyA.this, SelectPicPopupWindow.class), 3);
                break;
            case R.id.iv_idCardPositive:
                startActivityForResult(new Intent(WindowsAnchoredApplyA.this, SelectPicPopupWindow.class), 4);
                break;
            case R.id.iv_idCardOpposite:
                startActivityForResult(new Intent(WindowsAnchoredApplyA.this, SelectPicPopupWindow.class), 5);
                break;
            case R.id.bt_submit:
                newOutsName = etNewOutletsName.getText().toString();
                if (newOutsName.isEmpty()){
                    Toast.makeText(WindowsAnchoredApplyA.this, "代售点名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                newWinNumber = etRelationWindowNumber.getText().toString();
                if (newWinNumber.isEmpty()){
                    Toast.makeText(WindowsAnchoredApplyA.this, "窗口号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else if (newWinNumber.length() < 4){
                    {
                        newWinNumber = addZeroForNum(newWinNumber, 4);
                    }
                }
                newFuZeRen = etNewFuZeName.getText().toString();
                if (newFuZeRen.isEmpty()){
                    Toast.makeText(WindowsAnchoredApplyA.this, "负责人姓名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                newFuZeRenPhone = etNewFuZeMobile.getText().toString();
                if (newFuZeRenPhone.isEmpty()){
                    Toast.makeText(WindowsAnchoredApplyA.this, "负责人电话不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else if (RegexUtils.isMobileNO(newFuZeRenPhone) == false) {
                    Toast.makeText(WindowsAnchoredApplyA.this, "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
                    return;


                }

                newFuZeRenIdNum = etNewFuZeIdNum.getText().toString();
                if (newFuZeRenIdNum.isEmpty()){
                    Toast.makeText(WindowsAnchoredApplyA.this, "负责人身份证号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else if (RegexUtils.checkIdCard(newFuZeRenIdNum) == false) {
                    Toast.makeText(WindowsAnchoredApplyA.this, "请输入正确的负责人身份证号码 !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("windowAnchoredApply".equals(clickTag)) {
                    if (newCertificate.isEmpty()) {
                        Toast.makeText(WindowsAnchoredApplyA.this, "请上传授权书!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newContractFrist.isEmpty()) {
                        Toast.makeText(WindowsAnchoredApplyA.this, "请上传合同首页!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (newContractLast.isEmpty()) {
                        Toast.makeText(WindowsAnchoredApplyA.this, "请上传合同盖章尾页!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (newIdCardPositive.isEmpty()) {
                        Toast.makeText(WindowsAnchoredApplyA.this, "请上传身份证正面!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newIdCardOpposite.isEmpty()) {
                        Toast.makeText(WindowsAnchoredApplyA.this, "请上传身份证反面!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                try {
                    applyAnchored();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }
    private String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }




    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {

       // final String imagePath = getExternalStorageDirectory() + "/OC/register/image.jpg";
        if(data==null){
            return;
        }
        String imagePath =	data.getStringExtra("absolutePath");

        Luban.with(this)
                .load(new File(imagePath))                     //传人要压缩的图片
                .ignoreBy(300)
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        //TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }
                    @Override
                    public void onSuccess(File file) {

                      /*  operatorCardrearImg = Base64ConvertBitmap.fileToBase64(new File(imagePath));
                        Glide.with(RealNameAuthenticationA.this).load(file).into(iv_fuzerenIdOpposite);*/

//                        switch (resultCode) {
//                            case 1:
                                switch (requestCode) {
                                    case 1:
                                        newCertificate = Base64ConvertBitmap.fileToBase64(file);
                                        try {
                                            Glide.with(WindowsAnchoredApplyA.this).load(file).into(ivCertificate);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 2:
                                        newContractFrist = Base64ConvertBitmap.fileToBase64(file);
                                        try {
                                            Glide.with(WindowsAnchoredApplyA.this).load(file).into(ivContractFrist);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 3:
                                        newContractLast = Base64ConvertBitmap.fileToBase64(file);
                                        try {
                                            Glide.with(WindowsAnchoredApplyA.this).load(file).into(ivContractLast);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 4:
                                        newIdCardPositive = Base64ConvertBitmap.fileToBase64(file);
                                        try {
                                            Glide.with(WindowsAnchoredApplyA.this).load(file).into(ivIdCardPositive);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 5:
                                        newIdCardOpposite = Base64ConvertBitmap.fileToBase64(file);
                                        try {
                                            Glide.with(WindowsAnchoredApplyA.this).load(file).into(ivIdCardOpposite);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;


                                }

//                            break;


//                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO 当压缩过去出现问题时调用
                    }
                }).launch();    //启动压缩

    }



    /**
     * 申请窗口号挂靠
     * @throws Exception
     */
    private void applyAnchored() throws Exception {
//        cpd = new CustomProgressDialog(WindowsAnchoredApplyA.this, "正在处理中...", R.anim.frame_anim);
//        cpd.setCancelable(false);//设置进度条不可以按退回键取消
//        cpd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
        cpd.show();
        cpd.setCustomPd("正在处理中...");

        JSONObject jsonObject = new JSONObject();
        try {
            // operateType: 1 查看  2 添加  3 修改  4 取消申请  5 解除绑定
            if ("windowAnchoredApply".equals(clickTag)) {
                jsonObject.put("operateType", "2");
//                jsonObject.put("operatorCardforntImg", newIdCardPositive); // 身份证正面
//                jsonObject.put("operatorCardRearImg", newIdCardOpposite); // 身份证反面
//                jsonObject.put("staContIndexImg", newContractFrist); // 合同首页
//                jsonObject.put("staContLastImg", newContractLast); // 合同盖章尾页
//                jsonObject.put("fareAuthorizationImg", newCertificate); // 授权书照
            }else if ("updataInfo".equals(clickTag)){
                jsonObject.put("operateType", "3");
                jsonObject.put("id", childId);
//                jsonObject.put("operatorCardforntImg", newIdCardPositive); // 身份证正面
//                jsonObject.put("operatorCardRearImg", newIdCardOpposite); // 身份证反面
//                jsonObject.put("staContIndexImg", newContractFrist); // 合同首页
//                jsonObject.put("staContLastImg", newContractLast); // 合同盖章尾页
//                jsonObject.put("fareAuthorizationImg", newCertificate); // 授权书照
            }
            jsonObject.put("operatorCardforntImg", newIdCardPositive); // 身份证正面
            jsonObject.put("operatorCardRearImg", newIdCardOpposite); // 身份证反面
            jsonObject.put("staContIndexImg", newContractFrist); // 合同首页
            jsonObject.put("staContLastImg", newContractLast); // 合同盖章尾页
            jsonObject.put("fareAuthorizationImg", newCertificate); // 授权书照

            jsonObject.put("agencyId", outsId);
            jsonObject.put("winNumber", newWinNumber);
            jsonObject.put("agencyName", newOutsName);
            jsonObject.put("operatorName", newFuZeRen);
            jsonObject.put("operatorMobile", newFuZeRenPhone);
            jsonObject.put("operatorIdNum", newFuZeRenIdNum); // 负责人身份证号


        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        String url = Define.URL + "fb/manageAgencyConnect";
        SessionJsonObjectRequest applyAnchoredRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            cpd.dismiss();
                            Toast.makeText(WindowsAnchoredApplyA.this, "服务器异常！", Toast.LENGTH_SHORT);
                            return;
                        }

                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                cpd.dismiss();
                                if ("windowAnchoredApply".equals(clickTag)) {
                                    Toast.makeText(WindowsAnchoredApplyA.this,  "窗口号挂靠申请成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else if ("updataInfo".equals(clickTag)){
                                    Toast.makeText(WindowsAnchoredApplyA.this,  "修改成功！", Toast.LENGTH_SHORT).show();
                                    intent = new Intent(WindowsAnchoredApplyA.this, WindowsAnchoredListA.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                }

                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(WindowsAnchoredApplyA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(WindowsAnchoredApplyA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                cpd.dismiss();
                                Toast.makeText(WindowsAnchoredApplyA.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(WindowsAnchoredApplyA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);


            }
        });
        // 解决重复请求后台的问题
        applyAnchoredRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        applyAnchoredRequest.setTag("applyAnchoredRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(applyAnchoredRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("applyAnchoredRequest");
        MyApplication.getHttpQueues().cancelAll("documentImage2");
    }
}
