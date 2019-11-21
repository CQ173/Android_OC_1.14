package com.huoniao.oc.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.luban.Luban;
import com.huoniao.oc.common.luban.OnCompressListener;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.TransformationImageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class WriteDataChangeInfoA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_institutionName)
    EditText tvInstitutionName;
    @InjectView(R.id.tv_address)
    EditText tvAddress;
    @InjectView(R.id.tv_corpName)
    EditText tvCorpName;
    @InjectView(R.id.tv_corpMobile)
    EditText tvCorpMobile;
    @InjectView(R.id.tv_corpIdNum)
    EditText tvCorpIdNum;
    @InjectView(R.id.tv_person_charge_name)
    EditText tvPersonChargeName;
    @InjectView(R.id.tv_person_charge_phone)
    EditText tvPersonChargePhone;
    @InjectView(R.id.tv_person_charge_idNum)
    EditText tvPersonChargeIdNum;
    @InjectView(R.id.layout_outsFuZeRenArea)
    LinearLayout layoutOutsFuZeRenArea;
    @InjectView(R.id.iv_person_registration_certificate)
    ImageView ivPersonRegistrationCertificate;
    @InjectView(R.id.iv_legal_person_idNumPositive)
    ImageView ivLegalPersonIdNumPositive;
    @InjectView(R.id.iv_legal_person_other_side)
    ImageView ivLegalPersonOtherSide;
    @InjectView(R.id.iv_station_contract_home_page)
    ImageView ivStationContractHomePage;
    @InjectView(R.id.iv_the_station_contract)
    ImageView ivTheStationContract;
    @InjectView(R.id.iv_idNum_Positive)
    ImageView ivIdNumPositive;
    @InjectView(R.id.ll_idNum_Positive)
    LinearLayout llIdNumPositive;
    @InjectView(R.id.ll_station_contract_home_page)
    LinearLayout llStationContractHomePage;
    @InjectView(R.id.iv_idNum_otherSide)
    ImageView ivIdNumOtherSide;
    @InjectView(R.id.iv_payment_authorization)
    ImageView ivPaymentAuthorization;
    @InjectView(R.id.layout_payment_authorization)
    LinearLayout layoutPaymentAuthorization;
    @InjectView(R.id.iv_station_bar)
    ImageView ivStationBar;
    @InjectView(R.id.ll_fuZeRen_certificates)
    LinearLayout llFuZeRenCertificates;
    @InjectView(R.id.iv_business_license)
    ImageView ivBusinessLicense;
    @InjectView(R.id.ll_business_license)
    LinearLayout llBusinessLicense;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    @InjectView(R.id.bt_cancel)
    Button btCancel;

    private static final int CAMERA_REQUESTCODE = 1; //相机拍照
    private static final int PHOTO_REQUESTCODE = 2;//相册选择
    private String ivIdNumPositivePath;//负责人身份证正面
    private String ivIdNumOtherSidePath;  //负责人身份证反面
    private String ivPaymentAuthorizationPath; //票款汇缴授权书
    private String ivPersonRegistrationCertificatePath;  //营业执照/法人登记证书
    private String ivLegalPersonIdNumPositivePath; //法人代表身份证正面
    private String ivLegalPersonOtherSidePath;  //法人代表身份证反面
    private String ivStationContractHomePagePath;  //车站合同首页
    private String ivTheStationContractPath;  //车站合同尾页
    private String ivStationBarPath; //车站压金条
    private String ivBusinessLicensePath;   //车站押金年检证书

    private String ivIdNumPositiveBase;//负责人身份证正面
    private String ivIdNumOtherSideBase;  //负责人身份证反面
    private String ivPaymentAuthorizationBase; //票款汇缴授权书
    private String ivPersonRegistrationCertificateBase;  //营业执照/法人登记证书
    private String ivLegalPersonIdNumPositiveBase; //法人代表身份证正面
    private String ivLegalPersonOtherSideBase;  //法人代表身份证反面
    private String ivStationContractHomePageBase;  //车站合同首页
    private String ivTheStationContractBase;  //车站合同尾页
    private String ivStationBarBase; //车站压金条
    private String ivBusinessLicenseBase;   //车站押金年检证书

    private Bitmap bmOperatorCardfornt;
    private Bitmap bmOperatorCardrear;
    private Bitmap bmFareAuthorization;
    private Bitmap bmCorpLicence;
    private Bitmap bmCorpCardFornt;
    private Bitmap bmCorpCardRear;
    private Bitmap bmStaContIndex;
    private Bitmap bmStaContLast;

    private final String  corpLicenceSrcTag ="corpLicenceSrc";//营业执照路径标识
    private final String  corpCardforntSrcTag ="corpCardforntSrc"; //法人身份证正面路径标识
    private final String  corpCardrearSrcTag  ="corpCardrearSrc";//法人身份证反面路径标识
    private final String  staContIndexSrcTag  ="staContIndexSrc";//车站合同首页路径标识
    private final String  staContLastSrcTag  ="staContLastSrc";//车站合同末页路径标识
    private final String  operatorCardforntSrcTag ="operatorCardforntSrc";//负责人身份证正面路径标识
    private final String  operatorCardrearSrcTag ="operatorCardrearSrc";//负责人身份证反面路径标识
    private final String  fareAuthorizationSrcTag="fareAuthorizationSrc";//票款汇缴授权书标识

    private String tvOrgNameS;
    private String tvCorpNameS;
    private String tvCorpMobileS;
    private String tvCorpIdNumS;
    private String tvPersonChargeNameS;
    private String tvPersonChargePhoneS;
    private String tvPersonChargeIdNumS;
    private String tvAddressS;

    private MyPopWindow myPopWindow;
    private String imagePathTag = "-1";

    private User user;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_data_change_info);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        user = (User) readObject(WriteDataChangeInfoA.this, "usetInfo");
        if ((user.getOrgName()) != null) {
            tvInstitutionName.setText(user.getOrgName());
        }
        if ((user.getAddress()) != null) {
            tvAddress.setText(user.getAddress());
        }
        if ((user.getCorpName()) != null) {
            tvCorpName.setText(user.getCorpName());
        }
        if ((user.getCorpMobile()) != null) {
            tvCorpMobile.setText(user.getCorpMobile());
        }
        if ((user.getCorpIdNum()) != null) {
            tvCorpIdNum.setText(user.getCorpIdNum());
        }
        if ((user.getOperatorName()) != null) {
            tvPersonChargeName.setText(user.getOperatorName());
        }
        if ((user.getOperatorMobile()) != null) {
            tvPersonChargePhone.setText(user.getOperatorMobile());
        }
        if ((user.getOperatorIdNum()) != null) {
            tvPersonChargeIdNum.setText(user.getOperatorIdNum());
        }


        try {
            //负责人身份证正面
            if (user.getOperatorCardforntSrc() != null && !user.getOperatorCardforntSrc().isEmpty()) {
                getDocumentImage3(user.getOperatorCardforntSrc(), operatorCardforntSrcTag, 0, false, "");
            }
            //负责人身份证反面
            if (user.getOperatorCardrearSrc() != null && !user.getOperatorCardrearSrc().isEmpty()) {
                getDocumentImage3(user.getOperatorCardrearSrc(), operatorCardrearSrcTag, 0, false, "");
            }
            //票款汇缴授权书
            if (user.getFareAuthorizationSrc() != null && !user.getFareAuthorizationSrc().isEmpty()) {
                getDocumentImage3(user.getFareAuthorizationSrc(), fareAuthorizationSrcTag, 0, false, "");
            }
            //营业执照/法人登记证书
            if (user.getCorp_licence() != null && !user.getCorp_licence().isEmpty()) {
                getDocumentImage3(user.getCorp_licence(), corpLicenceSrcTag, 0, false, "");
            }
            //法人代表身份证正面
            if (user.getCorp_card_fornt() != null && !user.getCorp_card_fornt().isEmpty()) {
                getDocumentImage3(user.getCorp_card_fornt(), corpCardforntSrcTag, 0, false, "");
            }
            //法人代表身份证反面
            if (user.getCorp_card_rear() != null && !user.getCorp_card_rear().isEmpty()) {
                getDocumentImage3(user.getCorp_card_rear(), corpCardrearSrcTag, 0, false, "");
            }
            //车站合同首页

            if (user.getStaContIndexSrc() != null && !user.getStaContIndexSrc().isEmpty()) {
                getDocumentImage3(user.getStaContIndexSrc(), staContIndexSrcTag, 0, false, "");
            }
            //车站合同尾页
            if (user.getStaContLastSrc() != null && !user.getStaContLastSrc().isEmpty()) {
                getDocumentImage3(user.getStaContLastSrc(), staContLastSrcTag, 0, false, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取图片回调
        setImgResultLinstener(new ImgResult() {
            @Override
            public void getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr) {
                switch (tag){
                    case corpLicenceSrcTag:
                        glideSetImg(file, ivPersonRegistrationCertificate);
                        break;

                    case corpCardforntSrcTag:
                        glideSetImg(file, ivLegalPersonIdNumPositive);
                        break;
                    case corpCardrearSrcTag:
                        glideSetImg(file, ivLegalPersonOtherSide);
                        break;

                    case staContIndexSrcTag:
                        glideSetImg(file, ivStationContractHomePage);
                        break;
                    case staContLastSrcTag:
                        glideSetImg(file, ivTheStationContract);
                        break;

                    case operatorCardforntSrcTag:
                        glideSetImg(file, ivIdNumPositive);
                        break;

                    case operatorCardrearSrcTag:
                        glideSetImg(file, ivIdNumOtherSide);
                        break;

                    case fareAuthorizationSrcTag:
                        glideSetImg(file, ivPaymentAuthorization);
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
            Glide.with(WriteDataChangeInfoA.this).load(file).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_person_registration_certificate, R.id.iv_legal_person_idNumPositive, R.id.iv_legal_person_other_side, R.id.iv_station_contract_home_page, R.id.iv_the_station_contract, R.id.iv_idNum_Positive, R.id.iv_idNum_otherSide, R.id.iv_payment_authorization, R.id.iv_station_bar, R.id.iv_business_license, R.id.bt_confirm, R.id.bt_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_idNum_otherSide:
                imagePathTag = "0";
                selectCamera(imagePathTag);  //负责人身份证反面
                break;
            case R.id.iv_payment_authorization:
                imagePathTag = "1";
                selectCamera(imagePathTag);//票款汇缴授权书
                break;
            case R.id.iv_person_registration_certificate:
                imagePathTag = "2";
                selectCamera(imagePathTag);//营业执照/法人登记证书
                break;
            case R.id.iv_legal_person_idNumPositive:
                imagePathTag = "3";
                selectCamera(imagePathTag);//法人代表身份证正面
                break;
            case R.id.iv_legal_person_other_side:
                imagePathTag = "4";
                selectCamera(imagePathTag);//法人代表身份证反面
                break;
            case R.id.iv_station_contract_home_page:
                imagePathTag = "5";
                selectCamera(imagePathTag);//车站合同首页
                break;
            case R.id.iv_the_station_contract:
                imagePathTag = "6";
                selectCamera(imagePathTag);//车站合同尾页
                break;
            case R.id.iv_station_bar:
                imagePathTag = "7";
                selectCamera(imagePathTag);//车站压金条
                break;
            case R.id.iv_business_license:
                imagePathTag = "8";
                selectCamera(imagePathTag);//营业执照/法人登记证书
                break;
            case R.id.iv_idNum_Positive:
                imagePathTag = "9";
                selectCamera(imagePathTag);//负责人身份证反面
                break;
            case R.id.bt_confirm:

                checkSubmit();
                break;
            case R.id.bt_cancel:
                MyApplication.getInstence().activityFinish();
                break;
        }
    }


    /**
     * 选择相片
     *
     * @param s
     */
    private void selectCamera(String s) {
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                Button btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);  //相机拍照
                Button btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo); //相册选择
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);  //取消
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cameraClick();
                        myPopWindow.dissmiss();
                    }
                });
                btn_pick_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        photoClick();
                        myPopWindow.dissmiss();
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.alert_dialog;
            }
        }.poPwindow(this,true).showAtLocation(ivBack, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    File file;

    /**
     * 拍照
     */
    public void cameraClick() {

        try {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/camera/certificate";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            // 在SDcard的目录下创建图片文,以当前时间为其命名
            file = new File(sdCardDir, System.currentTimeMillis() + ".jpg");

            //拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, CAMERA_REQUESTCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 从相册中选择
     */
    public void photoClick() {
        //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
        //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 2);
    }


    private void setImagPath(String imagePathTag, String absolutePath) {
        switch (imagePathTag) {
            case "0":
                ivIdNumOtherSidePath = absolutePath;//负责人身份证反面
                imageCompres(ivIdNumOtherSidePath, ivIdNumOtherSide, imagePathTag);
                break;
            case "1":
                ivPaymentAuthorizationPath = absolutePath; //票款汇缴授权书
                imageCompres(ivPaymentAuthorizationPath, ivPaymentAuthorization, imagePathTag);
                break;
            case "2":
                ivPersonRegistrationCertificatePath = absolutePath;  //营业执照/法人登记证书
                imageCompres(ivPersonRegistrationCertificatePath, ivPersonRegistrationCertificate, imagePathTag);
                break;
            case "3":
                ivLegalPersonIdNumPositivePath = absolutePath; //法人代表身份证正面
                imageCompres(ivLegalPersonIdNumPositivePath, ivLegalPersonIdNumPositive, imagePathTag);
                break;
            case "4":
                ivLegalPersonOtherSidePath = absolutePath;  //法人代表身份证反面
                imageCompres(ivLegalPersonOtherSidePath, ivLegalPersonOtherSide, imagePathTag);
                break;
            case "5":
                ivStationContractHomePagePath = absolutePath;  //车站合同首页
                imageCompres(ivStationContractHomePagePath, ivStationContractHomePage, imagePathTag);
                break;
            case "6":
                ivTheStationContractPath = absolutePath;  //车站合同尾页
                imageCompres(ivTheStationContractPath, ivTheStationContract, imagePathTag);
                break;
            case "7":
                ivStationBarPath = absolutePath; //车站压金条
                imageCompres(ivStationBarPath, ivStationBar, imagePathTag);
                break;
            case "8":
                ivBusinessLicensePath = absolutePath;   //营业执照/法人登记证书
                imageCompres(ivBusinessLicensePath, ivBusinessLicense, imagePathTag);
                break;
            case "9":
                ivIdNumPositivePath = absolutePath;//负责人身份证正面
                imageCompres(ivIdNumPositivePath, ivIdNumPositive, imagePathTag);
                break;
        }
    }

    Bitmap bitmap = null;

    public  void imageCompres(final String absolutePath, final ImageView imageView, final String imagePathTag) {

        File file = new File(absolutePath);
        if (file.exists()) {
            file.length();
        }

        Luban.with(this)
                .load(new File(absolutePath))                     //传人要压缩的图片
                .ignoreBy(300)
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }
                    @Override
                    public void onSuccess(final File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        switch (imagePathTag) {
                            case "0":
                                ivIdNumOtherSideBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivIdNumOtherSide);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "1":
                                ivPaymentAuthorizationBase =Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivPaymentAuthorization);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "2":
                                ivPersonRegistrationCertificateBase =Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivPersonRegistrationCertificate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "3":
                                ivLegalPersonIdNumPositiveBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivLegalPersonIdNumPositive);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "4":
                                ivLegalPersonOtherSideBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivLegalPersonOtherSide);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "5":
                                ivStationContractHomePageBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivStationContractHomePage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "6":
                                ivTheStationContractBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivTheStationContract);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "7":
                                ivStationBarBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivStationBar);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "8":
                                ivBusinessLicenseBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivBusinessLicense);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "9":
                                ivIdNumPositiveBase = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(WriteDataChangeInfoA.this).load(absolutePath).into(ivIdNumPositive);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩

    }


    public String absolutePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUESTCODE:  //相机拍照
                if (resultCode == RESULT_OK) {
                    if (file != null) {
                        absolutePath = file.getAbsolutePath();
                        setImagPath(imagePathTag, absolutePath);
                    }
                }
                break;
            case PHOTO_REQUESTCODE:   //相册
                if (data != null && resultCode == RESULT_OK) {
                    cpd.show();
                    final Uri mImageCaptureUri = data.getData();
                    //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {
                        absolutePath = TransformationImageUtils.getRealFilePath(this, mImageCaptureUri);
                        setImagPath(imagePathTag, absolutePath);
                        cpd.dismiss();
                    }
                }
                break;

        }

    }


    private void checkSubmit() {

        tvOrgNameS = tvInstitutionName.getText().toString().trim(); //机构名称
        tvCorpNameS = tvCorpName.getText().toString().trim(); //法人姓名
        tvCorpMobileS = tvCorpMobile.getText().toString().trim(); //  //法人手机
        tvCorpIdNumS = tvCorpIdNum.getText().toString().trim();  //         法人身份证
        tvPersonChargeNameS = tvPersonChargeName.getText().toString().trim();  //     负责人姓名
        tvPersonChargePhoneS = tvPersonChargePhone.getText().toString().trim(); //       负责人手机
        tvPersonChargeIdNumS = tvPersonChargeIdNum.getText().toString().trim();//           负责人身份证号
        tvAddressS = tvAddress.getText().toString().trim(); //      地址


        if (tvOrgNameS.isEmpty()) {
            Toast.makeText(this, "机构名称不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvCorpNameS.isEmpty()) {
            Toast.makeText(this, "法人姓名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvCorpMobileS.isEmpty()) {
            Toast.makeText(this, "法人手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvCorpIdNumS.isEmpty()) {
            Toast.makeText(this, "法人身份证不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvAddressS.isEmpty()) {
            Toast.makeText(this, "地址不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (RegexUtils.isMobileNO(tvCorpMobileS) == false) {
            Toast.makeText(WriteDataChangeInfoA.this, "请输入正确的法人手机号码!", Toast.LENGTH_SHORT).show();
            return;
        }else if (RegexUtils.checkIdCard(tvCorpIdNumS) == false) {
            Toast.makeText(this, "请输入正确的法人身份证！", Toast.LENGTH_SHORT).show();
            return;
        }


        if (tvPersonChargeNameS.isEmpty()) {
            Toast.makeText(this, "负责人姓名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvPersonChargePhoneS.isEmpty()) {
            Toast.makeText(this, "负责人手机不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (tvPersonChargeIdNumS.isEmpty()) {
            Toast.makeText(this, "负责人身份证不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (RegexUtils.checkIdCard(tvPersonChargeIdNumS) == false) {
            Toast.makeText(this, "请输入正确的负责人身份证！", Toast.LENGTH_SHORT).show();
            return;
        } else if (RegexUtils.isMobileNO(tvPersonChargePhoneS) == false) {
            Toast.makeText(WriteDataChangeInfoA.this, "请输入正确的负责人手机号码!", Toast.LENGTH_SHORT).show();
            return;
        }

        User changeUser = new User();
        changeUser.setOrgName(tvOrgNameS);
        changeUser.setAddress(tvAddressS);
        changeUser.setCorpName(tvCorpNameS);
        changeUser.setCorpMobile(tvCorpMobileS);
        changeUser.setCorpIdNum(tvCorpIdNumS);
        changeUser.setOperatorName(tvPersonChargeNameS);
        changeUser.setOperatorMobile(tvPersonChargePhoneS);
        changeUser.setOperatorIdNum(tvPersonChargeIdNumS);
        changeUser.setCorp_licence(ivPersonRegistrationCertificateBase);
        changeUser.setCorp_card_fornt(ivLegalPersonIdNumPositiveBase);
        changeUser.setCorp_card_rear(ivLegalPersonOtherSideBase);
        changeUser.setOperatorCardforntSrc(ivIdNumPositiveBase);
        changeUser.setOperatorCardrearSrc(ivIdNumOtherSideBase);
        changeUser.setStaContIndexSrc(ivStationContractHomePageBase);
        changeUser.setStaContLastSrc(ivTheStationContractBase);
        changeUser.setFareAuthorizationSrc(ivPaymentAuthorizationBase);
        ObjectSaveUtil.saveObject(WriteDataChangeInfoA.this, "dataChangeImg", changeUser);

        if ((user.getOperatorMobile()).equals(tvPersonChargePhoneS)) {
            if (user.getOperatorName().equals(tvPersonChargeNameS)){
                dataChangeSubmit(); //开始修改个人信息
            }else {
                showDialog("因您修改了负责人姓名，审核通过后将删除原负责人名下所有银行卡，确认提交？");
            }

        }else {
            validateMobile();//如果修改了负责人手机号就先验证一下新的手机号是否被使用
        }



    }


    /**
     *  请求汇缴代扣管理入口(签约成功后,需要调一次刷新数据)
     *
     */
    private void dataChangeSubmit(){
        String url = Define.URL+"user/dataChangeComfirm";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", user.getId());
            jsonObject.put("officeName", tvOrgNameS);
            jsonObject.put("corpName", tvCorpNameS); //法人姓名
            jsonObject.put("corpMobile", tvCorpMobileS); //法人手机
            jsonObject.put("corpIdNum", tvCorpIdNumS); //法人身份证
            jsonObject.put("operatorName", tvPersonChargeNameS);//负责人姓名
            jsonObject.put("operatorIdNum", tvPersonChargeIdNumS);//负责人手身份证号
            jsonObject.put("operatorMobile", tvPersonChargePhoneS);// 负责人手机号
            jsonObject.put("address", tvAddressS);//地址

            jsonObject.put("corpLicenceImg", ivPersonRegistrationCertificateBase);//营业执照
            jsonObject.put("corpCardForntImg", ivLegalPersonIdNumPositiveBase);//法人身份证正面路径
            jsonObject.put("corpCardRearImg", ivLegalPersonOtherSideBase);//法人身份证反面路径
            jsonObject.put("staContIndexImg", ivStationContractHomePageBase);//车站合同首页路径
            jsonObject.put("staContLastImg", ivTheStationContractBase);//车站合同末页路径
            jsonObject.put("operatorCardforntImg", ivIdNumPositiveBase);//负责人身份证正面路径
            jsonObject.put("operatorCardrearImg", ivIdNumOtherSideBase);//负责人身份证反面路径
            jsonObject.put("fareAuthorizationImg", ivPaymentAuthorizationBase);//票款汇缴授权书
           //jsonObject.put("staDepositImg", ivStationBarBase);//车站押金条路径
//          jsonObject.put("staDepInspImg", ivBusinessLicenseBase);//车站押金年检证书路径

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "dataChangeSubmit", "", true, true);
    }


    /**
     * 验证手机号码是否被使用
     */
    private void validateMobile() {


      /*  String url = Define.URL + "user/validateMobile";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("mobile", tvPersonChargePhone);
            jsonObject.put("iconCode", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestNet(url, jsonObject, "validateMobile", "", true);*/
        cpd.show();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("mobile", tvPersonChargePhoneS);
//            jsonObject.put("iconCode", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SessionJsonObjectRequest request = new SessionJsonObjectRequest(Request.Method.POST, Define.URL+"user/validateMobile", jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("debug", "response: "+ response.toString());
                try {
                    String code = response.getString("code");
                    String message = response.getString("msg");
                    if ("46004".equals(code)) {
                        cpd.dismiss();
                        intent = new Intent(WriteDataChangeInfoA.this, DataChangeApplyA.class);
                        intent.putExtra("operationTag", "updateMobile");
                        intent.putExtra("newOperatorMobile", tvPersonChargePhoneS);
                        if (!(user.getOperatorName().equals(tvPersonChargeNameS))){
                            intent.putExtra("updateOperatorName", "updateOperatorName");
                        }
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    }else if ("61451".equals(code)) {
                        cpd.dismiss();
                        ToastUtils.showToast(WriteDataChangeInfoA.this, "图形验证码输入错误！");
                    }else {
                        cpd.dismiss();
                        ToastUtils.showToast(WriteDataChangeInfoA.this, "手机号已被注册！");
                        Log.d("cofinm", message);
                    }
                } catch (JSONException e) {
                    cpd.dismiss();
                    e.printStackTrace();
                }
                Log.d("debug", "vercode = " + response.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(WriteDataChangeInfoA.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });

        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("validateDataChangeMobile");
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "dataChangeSubmit":
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = json.getJSONObject("data");
//                    String result = jsonObject.getString("result");
//                    if ("success".equals(result)){
                        intent = new Intent(WriteDataChangeInfoA.this, AccountLogOffSuccessA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

//                    }else {
//                        String message = json.optString("msg");
//                        ToastUtils.showToast(WriteDataChangeInfoA.this, message);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;
           /* case "validateMobile":
                intent = new Intent(WriteDataChangeInfoA.this, DataChangeApplyA.class);
                intent.putExtra("operationTag", "updateMobile");
                intent.putExtra("newOperatorMobile", tvPersonChargePhoneS);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;*/
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("validateDataChangeMobile");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();  //清除内存
        ivIdNumPositiveBase=null;
        ivIdNumOtherSideBase =null;
        ivPaymentAuthorizationBase =null;
        ivPersonRegistrationCertificateBase =null;
        ivLegalPersonIdNumPositiveBase =null;
        ivLegalPersonOtherSideBase =null;
        ivStationContractHomePageBase =null;
        ivTheStationContractBase =null;
        ivStationBarBase =null;
        ivBusinessLicenseBase = null;
        System.gc();  //通知垃圾回收站回收垃圾
    }

    /**
     * 确认和拒绝操作提示弹窗
     *
     * @param msg 提示信息
     */
    private void showDialog(String msg) {
        new AlertDialog.Builder(WriteDataChangeInfoA.this)
                .setTitle("提示！")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //根据不同请求弹出不同提示的弹窗

                        dataChangeSubmit(); //开始修改个人信息


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setCancelable(false)
                .show();

    }
}
