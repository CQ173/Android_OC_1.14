package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.ChoiceDatesEvent;
import com.huoniao.oc.bean.LateFeeBean;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.luban.Luban;
import com.huoniao.oc.common.luban.OnCompressListener;
import com.huoniao.oc.user.SelectPicPopupWindow;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import uk.co.senab.photoview.PhotoView;

import static com.huoniao.oc.R.id.et_returnReason;

public class AddLateFeeSubmitA extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.ll_choiceDate)
    LinearLayout llChoiceDate;
    @InjectView(R.id.et_outletsAccount)
    EditText etOutletsAccount;
    @InjectView(R.id.et_returnAmount)
    EditText etReturnAmount;
    @InjectView(R.id.rb_importDataError)
    RadioButton rbImportDataError;
    @InjectView(R.id.rb_other)
    RadioButton rbOther;
    @InjectView(et_returnReason)
    EditText etReturnReason;
    @InjectView(R.id.ll_returnReason)
    LinearLayout llReturnReason;
    @InjectView(R.id.tv_uploadPicture)
    TextView tvUploadPicture;
    @InjectView(R.id.tv_pictureTemplate)
    TextView tvPictureTemplate;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.tv_confirmApply)
    TextView tvConfirmApply;
    @InjectView(R.id.radioButton)
    RadioGroup radioButton;
    @InjectView(R.id.iv_previewImage)
    ImageView ivPreviewImage;
    private String applyReason = "";
    private String previewImage = "";
    private String dates;
    private String id = "";
    private String outletsAccount;
    private String returnAmount;
    private Intent intent;
    private String operationTag;
    private String needId;//从其它地方传过来的是否需要传id标识
    private LateFeeBean.DataBean dataBean;
    private String imageUrl;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_late_fee_submit);
        ButterKnife.inject(this);
        EventBus.getDefault().registerSticky(this);  //注册粘性事件  可以保证接收消息

        initData();
    }


    private void initData() {
        InputFilter[] filters = {new CashierInputFilter()};
        etReturnAmount.setFilters(filters);
        radioButton.setOnCheckedChangeListener(this);
        intent = getIntent();
        needId = intent.getStringExtra("needId");
//        dataBean = (LateFeeBean.DataBean) ObjectSaveUtil.readObject(AddLateFeeSubmitA.this, "lateFeeList");
            id = intent.getStringExtra("id");
            tvDate.setText(intent.getStringExtra("days"));
            etOutletsAccount.setText(intent.getStringExtra("agencyCode"));
            etReturnAmount.setText(intent.getStringExtra("feeString"));
            applyReason  = intent.getStringExtra("applyReason");

            if (applyReason != null && !applyReason.isEmpty()) {
                if (applyReason.contains("导入数据出错")) {
                    rbImportDataError.setChecked(true);
                    rbOther.setChecked(false);
                    llReturnReason.setVisibility(View.GONE);
                } else {
                    rbImportDataError.setChecked(false);
                    rbOther.setChecked(true);
                    llReturnReason.setVisibility(View.VISIBLE);
                    etReturnReason.setText(applyReason);
                }
            }else {
                rbImportDataError.setChecked(false);
                rbOther.setChecked(true);
                llReturnReason.setVisibility(View.VISIBLE);
            }
            imageUrl = intent.getStringExtra("instructionSrc");
            try {
                if (imageUrl != null && !imageUrl.isEmpty()){
//                getDocumentImage2(idCardOpposite, "operatorCardrear", 0);
                    getDocumentImage3(imageUrl, "coveringLetter", 0, false, "");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



        setImgResultLinstener(new ImgResult() {
            @Override
            public void getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr) {
                switch (tag){
                    case "coveringLetter":
                        imageFile = file;
                        previewImage = Base64ConvertBitmap.fileToBase64(file);
                        glideSetImg(file, ivPreviewImage);

                        break;

                }


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
            Glide.with(AddLateFeeSubmitA.this).load(file).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
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
                    Glide.with(AddLateFeeSubmitA.this).load(file).into(iv_enlarge);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            protected int layout() {
                return R.layout.pop_lookimg;
            }
        }.popWindowTouch(AddLateFeeSubmitA.this).showAtLocation(ivBack, Gravity.CENTER,0,0);
    }


    /**
     * 放大查看图片
     * @param
     */
    private void myEnlargeImage2(final int dwableId){
//        final Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() * SCALE, bitmap.getHeight() * SCALE);
        MyPopWindow myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                PhotoView iv_enlarge = (PhotoView) view.findViewById(R.id.iv_enlarge);
//                iv_enlarge.setImageBitmap(newBitmap);

                iv_enlarge.setImageResource(dwableId);

            }
            @Override
            protected int layout() {
                return R.layout.pop_lookimg;
            }
        }.popWindowTouch(AddLateFeeSubmitA.this).showAtLocation(ivBack, Gravity.CENTER,0,0);
    }

    @OnClick({R.id.iv_back, R.id.ll_choiceDate, R.id.tv_uploadPicture, R.id.tv_pictureTemplate, R.id.tv_save,
            R.id.tv_confirmApply, R.id.iv_previewImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_choiceDate:
                intent = new Intent(AddLateFeeSubmitA.this, ChoiceDateA.class);
                startActivity(intent);
                break;
            case R.id.tv_uploadPicture:
                startActivityForResult(new Intent(AddLateFeeSubmitA.this, SelectPicPopupWindow.class), 1);
                break;
            case R.id.tv_pictureTemplate:
//                myEnlargeImage2(R.drawable.erweima);
                enlargeImage(AddLateFeeSubmitA.this, Define.LATEFEE_COVERING_LETTER, ivBack);
                break;
            case R.id.tv_save:
                operationTag = "0";
                latefeeReturnSave();
                break;
            case R.id.tv_confirmApply:
                operationTag = "1";
                latefeeReturnSave();
                break;
            case R.id.iv_previewImage:
                if (imageFile != null) {
                    myEnlargeImage(imageFile);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_importDataError:
                llReturnReason.setVisibility(View.GONE);
                applyReason = rbImportDataError.getText().toString();
                break;

            case R.id.rb_other:
//                applyReason = "";
                llReturnReason.setVisibility(View.VISIBLE);

                break;
        }
    }

    //运行在post运行所在的线程
    public void onEvent(ChoiceDatesEvent event) {   //别的地方发送数据到这里  这里直接接受
        dates = event.dates;
        tvDate.setText(dates);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        String imagePath = data.getStringExtra("absolutePath");

        //	final String imagePath = getExternalStorageDirectory() + "/OC/register/image.jpg";
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
                        switch (resultCode) {
                            case 1:
                                imageFile = file;
                                previewImage = Base64ConvertBitmap.fileToBase64(file);
                                try {
                                    Glide.with(AddLateFeeSubmitA.this).load(file).into(ivPreviewImage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO 当压缩过去出现问题时调用
                    }
                }).launch();    //启动压缩

    }

    private void latefeeReturnSave(){
        dates = tvDate.getText().toString();
        outletsAccount = etOutletsAccount.getText().toString();
        returnAmount = etReturnAmount.getText().toString();
        if (dates == null || dates.isEmpty()){
            ToastUtils.showToast(AddLateFeeSubmitA.this, "请选择日期！");
            return;
        }

        if (outletsAccount == null || outletsAccount.isEmpty()){
            ToastUtils.showToast(AddLateFeeSubmitA.this, "请输入代售点账号！");
            return;
        }

        if (returnAmount == null || returnAmount.isEmpty()){
            ToastUtils.showToast(AddLateFeeSubmitA.this, "请输入返还金额！");
            return;
        }

        if ("0".equals(returnAmount) || "0.0".equals(returnAmount) || "0.00".equals(returnAmount)){
            ToastUtils.showToast(AddLateFeeSubmitA.this, "请输入大于0的返还金额！");
            return;
        }

        if (rbOther.isChecked()){
            applyReason = etReturnReason.getText().toString().trim();
            if (applyReason == null || applyReason.isEmpty()) {
                ToastUtils.showToast(AddLateFeeSubmitA.this, "请输入返还原因！");
                return;
            }

        }

        if (previewImage == null || previewImage.isEmpty()){
            ToastUtils.showToast(AddLateFeeSubmitA.this, "请上传说明函！");
            return;
        }


        String url = Define.URL + "fb/latefeeReturnSave";
        JSONObject jsonObject = new JSONObject();
        try {
            if (needId != null) {
                jsonObject.put("id", id);
            }
            jsonObject.put("fee", returnAmount);
            jsonObject.put("agencyCode", outletsAccount);
            jsonObject.put("instruction", previewImage);
            jsonObject.put("state", operationTag);
            jsonObject.put("applyReason", applyReason);
            jsonObject.put("days", dates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "latefeeReturnSave", "0", true, false); //0 不代表什么
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "latefeeReturnSave":
                if ("0".equals(operationTag)){
                    ToastUtils.showToast(AddLateFeeSubmitA.this, "保存成功！");
                }else if ("1".equals(operationTag)){
                    ToastUtils.showToast(AddLateFeeSubmitA.this, "申请成功！");
                }
                setResult(10);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //注销监听
        dates = "";
    }
}
