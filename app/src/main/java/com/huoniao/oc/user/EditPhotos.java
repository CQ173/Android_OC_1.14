package com.huoniao.oc.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.custom.ClipImageLayout;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.Compres;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.DensityUtil;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class EditPhotos extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_save_header_image)
    TextView tvSaveHeaderImage;
    @InjectView(R.id.clipimage_layout)
    ClipImageLayout clipimageLayout;
    @InjectView(R.id.activity_edit_photos)
    LinearLayout activityEditPhotos;
    private String absolutePath;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photos);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        absolutePath = getIntent().getStringExtra("absolutePath");
        ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                int width = DensityUtil.px2dip(EditPhotos.this, 800);
                int height = DensityUtil.px2dip(EditPhotos.this, 500);
                Compres compres = new Compres();
               Bitmap bitmap = compres.decodeSampledBitmapFromFile(absolutePath, width, height);
                final Drawable bd =new BitmapDrawable(bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clipimageLayout.setImageDrawable(bd);
                    }
                });

            }
        });
     //   Drawable drawable=Drawable.createFromPath(absolutePath);

    }

    @OnClick({R.id.iv_back, R.id.tv_save_header_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save_header_image:
                Bitmap bitmap = clipimageLayout.clip();
                uploadSave(bitmap);
                break;
        }
    }


    private VolleyNetCommon volleyNetCommon;
    private static final int HEADER_IMAGE = 3; //上传图片成功后需要把值返回给用户界面 结果标记
    private static final String UPLOADSAVETAG = "uploadTag";
    //上传头像保存操作
    private void uploadSave(final Bitmap bitmap) {
        if(bitmap==null){
            Toast.makeText(this, "头像为空不能上传", Toast.LENGTH_SHORT).show();
            return;
        }
        cpd.show();
        ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
             String base6 = Base64ConvertBitmap.bitmapToBase64(bitmap);
                String url = Define.URL+"user/modifyPhoto";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("photoImg", base6);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(volleyNetCommon==null){
                    volleyNetCommon = new VolleyNetCommon();
                }

                JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(EditPhotos.this) {
                    @Override
                    protected void netVolleyResponese(JSONObject json) {
                        Toast.makeText(EditPhotos.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        saveSd(bitmap);
                        if(file !=null){
                            Intent intent = new Intent();
                            String clipPath=  file.getAbsolutePath();
                            intent.putExtra("clipPath",clipPath);
                            setResult(HEADER_IMAGE,intent);
                        }
                        cpd.dismiss();
                        finish();
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

                    @Override
                    public void volleyResponse(Object o) {

                    }

                    @Override
                    public void volleyError(VolleyError volleyError) {

                        Toast.makeText(EditPhotos.this, R.string.netError, Toast.LENGTH_SHORT).show();
                    }
                }, UPLOADSAVETAG, true);


                volleyNetCommon.addQueue(jsonObjectRequest);
            }
        });

    }

    private void saveSd(Bitmap bitmap) {
        String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/clip";
        File dirFile = new File(sdCardDir);  //目录转化成文件夹
        if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
            dirFile.mkdirs();
        }                          //文件夹有啦，就可以保存图片啦
        // 在SDcard的目录下创建图片文,以当前时间为其命名
        file = new File(sdCardDir, System.currentTimeMillis() + "clip.jpg");



        try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(volleyNetCommon != null){
            volleyNetCommon.getRequestQueue().cancelAll(UPLOADSAVETAG);
        }
    }
}
