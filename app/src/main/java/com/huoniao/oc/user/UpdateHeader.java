package com.huoniao.oc.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.util.Compres;
import com.huoniao.oc.util.TransformationImageUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UpdateHeader extends BaseActivity {
    private final String  headerImgTag  ="headerImg2";//头像图片标识
    private static final int CAMERA_REQUESTCODE = 1; //相机拍照
    private static final int PHOTO_REQUESTCODE = 2;//相册选择
    private static final int HEADER_IMAGE = 3; //上传图片成功后需要把值返回给用户界面 结果标记
    private static final int CAMERA_IMAGEPATH = 4;//拍照后图片调到裁剪页面tag
    private static final int CLIPPATH_RESULTCODE = 5; //返回键退出设置头像页面 返回我的模块界面 需要把路径传递出去
    @InjectView(R.id.iv_header_image)
    ImageView ivHeaderImage;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    private File file;
    private Compres compres;
    private Bitmap bitmap;
    private String absolutePath;
    private String clipPath;
    public int permissionsFlag = 0;   //成功  用户是否拒绝了权限    1拒绝  2接受 权限
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_header);
        ButterKnife.inject(this);
        initData();
        checkPermission(this);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        Bitmap bmp = (Bitmap) b.getParcelable("bitmap");
        String imageheaderPath = intent.getStringExtra("absolutePath");
      if(imageheaderPath == null) {  //如果截取新图片并且保存后 且关闭当前页面  再次点击头像进入当前页面 把路径传过来 作为显示 由于后台给的图片地址是每次都变化的 所需要做这种操作
          if (bmp == null) {
              //表示网络有图片  本地图片没有传过来

              String headeUrl = intent.getStringExtra("headeUrl");
             /* Glide.with(this).load(headeUrl).listener(new RequestListener<String, GlideDrawable>() {
                  @Override
                  public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                      Toast.makeText(UpdateHeader.this, "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
                      return false;
                  }

                  @Override
                  public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                      return false;
                  }
              }).into(ivHeaderImage);*/
              try {
//                  getDocumentImage2(headeUrl, "headerImg2", 0);
                  getDocumentImage3(headeUrl, "headerImg2", 0, false, "");
              } catch (Exception e) {
                  e.printStackTrace();
              }

              //获取图片回调
              setImgResultLinstener(new ImgResult() {
                  @Override
                  public void getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr) {
                      switch (tag){
                          case headerImgTag:

                              glideSetImg(file, ivHeaderImage, headerImgTag);

                              break;

                      }
               /* if(i!=-1){
                    documentInformationBeenList.get(i).imageSrc =imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }*/


                  }
              });

          } else {

              ivHeaderImage.setImageBitmap(bmp);
          }
      }else{
          try {
              Glide.with(this).load(imageheaderPath).into(ivHeaderImage);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
    }

    private void glideSetImg(File file, final ImageView imageView, String tag){
        if ("headerImg2".equals(tag)){

//            //设置圆角图片
//            Glide.with(UpdateHeader.this).load(file).asBitmap().centerCrop()
//                    .into(new BitmapImageViewTarget(imageView) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable =
//                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            imageView.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//        }else {
            //设置普通图片
            try {
                Glide.with(UpdateHeader.this).load(file).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    /*@Override
    protected void getImageBitmap(Bitmap licenceBitmap, String tag,int p) {
        if (licenceBitmap != null) {
            if ("headerImg2".equals(tag)) {
                ivHeaderImage.setImageBitmap(licenceBitmap);

            }
        }
    }*/

    /**
     * 拍照
     *
     * @param view
     */
    public void cameraClick(View view) {

        try {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/camera";
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
     *
     * @param view
     */
    public void photoClick(View view) {
        //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
        //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUESTCODE:  //相机拍照
                if (resultCode == RESULT_OK) {
                    if (file != null) {
                        cpd.show();
                        absolutePath = file.getAbsolutePath();
                        Intent intent = new Intent(UpdateHeader.this,EditPhotos.class);
                        intent.putExtra("absolutePath",absolutePath);
                        startActivityForResult(intent,CAMERA_IMAGEPATH);
                        //ivHeaderImage.setImageBitmap(bitmap);
                        cpd.dismiss();

                       /* if (compres == null) {
                            compres = new Compres();
                        }

                        ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
                            @Override
                            public void run() {
                                int width = DensityUtil.px2dip(UpdateHeader.this, 800);
                                int height = DensityUtil.px2dip(UpdateHeader.this, 500);
                                bitmap = compres.decodeSampledBitmapFromFile(absolutePath, width, height);
                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      threeTypes = 3;

                                      Intent intent = new Intent(UpdateHeader.this,EditPhotos.class);
                                       intent.putExtra("absolutePath",absolutePath);
                                      startActivityForResult(intent,CAMERA_IMAGEPATH);
                                      //ivHeaderImage.setImageBitmap(bitmap);
                                      cpd.dismiss();
                                  }
                              });
                            }
                        });*/


                    }
                }
                break;
            case PHOTO_REQUESTCODE:   //相册
                if (data != null && resultCode == RESULT_OK ) {
                    cpd.show();
                    final Uri mImageCaptureUri = data.getData();
                    //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {

                        if (compres == null) {
                            compres = new Compres();
                        }
                    absolutePath = TransformationImageUtils.getRealFilePath(this,mImageCaptureUri);
                      /*  File file = new File(new URI(mImageCaptureUri.toString()));
                        absolutePath = file.getAbsolutePath();*/

                      /*  String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        Cursor cursor = getContentResolver().query(mImageCaptureUri,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                          absolutePath = cursor.getString(columnIndex);
                        cursor.close();
*/


                        Intent intent = new Intent(UpdateHeader.this,EditPhotos.class);
                        intent.putExtra("absolutePath",absolutePath);
                        startActivityForResult(intent,CAMERA_IMAGEPATH);
                        //   ivHeaderImage.setImageBitmap(bitmap);
                        cpd.dismiss();

                      /*  ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
                            @Override
                            public void run() {

                                    int width = DensityUtil.px2dip(UpdateHeader.this, 800);
                                    int height = DensityUtil.px2dip(UpdateHeader.this, 500);
                                   // final Bitmap bitmap = compres.decodeSampledBitmapFromBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri), width, height);
                                bitmap = compres.decodeSampledBitmapFromFile(absolutePath, width, height);
                               runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            threeTypes = 3;

                                            Intent intent = new Intent(UpdateHeader.this,EditPhotos.class);
                                            intent.putExtra("absolutePath",absolutePath);
                                            startActivityForResult(intent,CAMERA_IMAGEPATH);
                                         //   ivHeaderImage.setImageBitmap(bitmap);
                                            cpd.dismiss();
                                        }
                                    });

                            }

                        });*/


                    }
                }
                break;
        }
        if(HEADER_IMAGE ==resultCode){
            clipPath = data.getStringExtra("clipPath");
            try {
                Glide.with(this).load(clipPath).into(ivHeaderImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if(clipPath !=null) {
                    Intent intent = new Intent();
                    intent.putExtra("clipPath",clipPath);
                    setResult(CLIPPATH_RESULTCODE,intent);
                }
                finish();
                break;

        }
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
          if(clipPath !=null) {
              Intent intent = new Intent();
              intent.putExtra("clipPath",clipPath);
              setResult(CLIPPATH_RESULTCODE,intent);

          }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    /*
  权限校验
   */
    public void checkPermission(Activity activity) {

        if (Build.VERSION.SDK_INT >= 23) {

            int readSdcard = activity.checkSelfPermission(Manifest.permission.CAMERA);

            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<>();

            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode = 1;
                permissions.add(Manifest.permission.CAMERA);
            }

            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                activity.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }

        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拍照权限未授权！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "恭喜！拍照权限授权成功！", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
