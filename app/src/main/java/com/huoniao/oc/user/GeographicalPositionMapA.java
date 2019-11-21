package com.huoniao.oc.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyOrientationListener;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GeographicalPositionMapA extends BaseActivity implements OnGetGeoCoderResultListener, BDLocationListener, OnGetDistricSearchResultListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_add_card)
    TextView tvAddCard;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.bmapView)
    MapView mMapView;
    @InjectView(R.id.activity_geographical_position_map)
    LinearLayout activityGeographicalPositionMap;
    @InjectView(R.id.tv_address_name)
    TextView tvAddressName;
    @InjectView(R.id.tv_absolute_address)
    TextView tvAbsoluteAddress;


    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private int locType;
    private double longitude;// 经度
    private double latitude;// 纬度
    private float radius;// 定位精度半径，单位是米
    private String addrStr;// 反地理编码
    private String province;// 省份信息
    private String city;// 城市信息
    private String district;// 区县信息
    private float direction;// 手机方向信息

    private Marker mCurrentMarker;// 当前标志
    private LatLng lalo;
    private MyOrientationListener myOrientationListener;
    private double mCurrentLantitude;
    private double mCurrentLongitude;

    private float mCurrentAccracy;  // 当前的精度

    private int mXDirection; // 方向传感器X方向的值
    private GeoCoder mSearch;
    private double lats;
    private double lngs;
    private String mainFlag;
    private String addressMap;
    private VolleyNetCommon volleyNetCommon;
    private User.AgencysBean agencysBean;
    private int listItemNumber;
    private DistrictSearch mDistrictSearch;
    private List<List<LatLng>> polyLines;
    private Overlay overlay;

    private    boolean range = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geographical_position_map);
        ButterKnife.inject(this);
        initLocation();
        initWidget();
        initOritationListener();

    }

    private void initWidget() {
        tvSave.setVisibility(View.VISIBLE);
        tvTitle.setText("地理位置");
        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(this);
        agencysBean = (User.AgencysBean) getIntent().getSerializableExtra("AgencysBean");
        User   user = (User) ObjectSaveUtil.readObject(GeographicalPositionMapA.this, "loginResult");
        if(user != null){
          String provinceName= user.getProvinceName()==null ? "北京":user.getProvinceName();
            mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(provinceName));
        }
        listItemNumber = getIntent().getIntExtra("listItemNumber", 0); //获取用户点击第几个条目进来的

        mainFlag = agencysBean.getMainAddressFlag()== null ? "": agencysBean.getMainAddressFlag();
                String lat =  agencysBean.getLat() == null ? "0" : agencysBean.getLat() ;
                String lng = agencysBean.getLng() == null ? "0" : agencysBean.getLng();
             String agencyName = agencysBean.getAgencyName()==null ? "" : agencysBean.getAgencyName();
        lats = Double.parseDouble(lat);
        lngs = Double.parseDouble(lng);
        tvAddressName.setText(agencyName);


        //点击地图
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                mBaiduMap.hideInfoWindow();
              //  mBaiduMap.clear();
               if(polyLines != null && polyLines.size()>0) {
                   for (List<LatLng> latLngList : polyLines
                           ) {
                       boolean polygonContainsPoint = SpatialRelationUtil.isPolygonContainsPoint(latLngList, latLng);
                       if (polygonContainsPoint) {
                           range = true;
                           break;
                       } else {
                           range = false;
                       }
                   }
               }
               if(!range){
                   Toast.makeText(GeographicalPositionMapA.this, "请在范围内添加地理位置", Toast.LENGTH_SHORT).show();
               }

                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.location);
                MarkerOptions ooA = new MarkerOptions().position(latLng).icon(bitmap).draggable(true);
                ;

                if(overlay!=null){
                    overlay.remove();
                }
                overlay = mBaiduMap.addOverlay(ooA);

                double longitude = latLng.longitude;  //经度
                double latitude = latLng.latitude; //纬度

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        searchLocation(latLng);
                    }
                }.start();


            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });


       /*
         长按百度地图
       mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                mBaiduMap.clear();
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_launcher);
                MarkerOptions ooA = new MarkerOptions().position(latLng).icon(bitmap).draggable(true);
                ;


                Overlay overlay = mBaiduMap.addOverlay(ooA);

                double longitude = latLng.longitude;  //经度
                double latitude = latLng.latitude; //纬度

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        searchLocation(latLng);
                    }
                }.start();

            }
        });*/
    }

    private void search() {
        // 反Geo搜索
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mSearch.geocode(new GeoCodeOption().city("长沙").address("金南家园"));

    }

    private void searchLocation(LatLng locations) {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(locations));
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
            return;
        }
        //获取地理编码结果
        lalo = geoCodeResult.getLocation();

        //需要改变图标
        OverlayOptions ooA = new MarkerOptions().position(lalo).icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        overlay = (Marker) mBaiduMap.addOverlay(ooA);
        tvAbsoluteAddress.setText(geoCodeResult.getAddress());
        LatLng latLng = new LatLng(geoCodeResult.getLocation().latitude,geoCodeResult.getLocation().longitude);
        updateMapLocation(latLng);

    }

    //更新地图位置
    private void updateMapLocation(LatLng latLng) {
        MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(latLng);
        if(uc != null && mBaiduMap != null) {
            try {
                mBaiduMap.animateMapStatus(uc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }


    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有找到检索结果
            return;
        }

        lats = reverseGeoCodeResult.getLocation().latitude;
        lngs  = reverseGeoCodeResult.getLocation().longitude;
        addressMap = reverseGeoCodeResult.getAddress();
        agencysBean.setLng(String.valueOf(lngs));
        agencysBean.setLat(String.valueOf(lats));
        agencysBean.setGeogPosition(reverseGeoCodeResult.getAddress());

        updateMapLocation(reverseGeoCodeResult.getLocation());
        //获取反向地理编码结果
        tvAbsoluteAddress.setText(reverseGeoCodeResult.getAddress());


    }


    /**
     * 定位
     */
    private void initLocation() {


        mBaiduMap = mMapView.getMap();
        mBaiduMap.clear();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));   // 设置级别

        // 定位初始化
        // 声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(this);// 注册定位监听接口

        /**
         * 设置定位参数
         */
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
        option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
        option.setOpenGps(true);// 打开gps
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);

    }


    private boolean isFristLocation = true;

    //定位结果
    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null || mMapView == null) {
            return;
        }

        locType = location.getLocType();
        Log.i("mybaidumap", "当前定位的返回值是：" + locType);

        longitude = location.getLongitude();
        latitude = location.getLatitude();
        if (location.hasRadius()) {// 判断是否有定位精度半径
            radius = location.getRadius();
        }

        if (locType == BDLocation.TypeNetWorkLocation) {
            addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
            Log.i("mybaidumap", "当前定位的地址是：" + addrStr);
        }

        direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
        province = location.getProvince();// 省份
        city = location.getCity();// 城市
        district = location.getDistrict();// 区县

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());


        Log.i("mybaidumap", "province是：" + province + " city是" + city + " 区县是: " + district);


        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                //.accuracy(location.getRadius())  //设置蓝色圈圈
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mXDirection).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        mCurrentAccracy = location.getRadius();
        mCurrentLantitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();

  /*      //画标志
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(ll);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();*/
  /*       mBaiduMap.clear();
        //需要改变图标
        OverlayOptions ooA = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        mCurrentMarker = (Marker) mBaiduMap.addOverlay(ooA);*/

        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mBaiduMap.setMyLocationConfigeration(config);


        mMapView.showZoomControls(false);

        if (isFristLocation) {
            isFristLocation = false;
            //更新当前地图位置
         /*   MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(uc);*/
          //  search();
            //需要改变图标
            OverlayOptions ooA = new MarkerOptions().position(new LatLng(lats,lngs)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
            overlay = (Marker) mBaiduMap.addOverlay(ooA);
            searchLocation(new LatLng(lats,lngs));
           // searchLocation(ll);
        /*    mSearch = GeoCoder.newInstance();
            mSearch.setOnGetGeoCodeResultListener(this);
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));*/
        }


    }


    /**
     * 初始化方向传感器
     */
    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    @Override
                    public void onOrientationChanged(float x) {
                        mXDirection = (int) x;
                        // 构造定位数据
                        MyLocationData locData = new MyLocationData.Builder()
                            //    .accuracy(mCurrentAccracy)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(mXDirection)
                                .latitude(mCurrentLantitude)
                                .longitude(mCurrentLongitude).build();

                        // 设置定位数据
                        mBaiduMap.setMyLocationData(locData);

                        MyLocationConfiguration config = new MyLocationConfiguration(
                                MyLocationConfiguration.LocationMode.NORMAL, true, null);
                        mBaiduMap.setMyLocationConfigeration(config);

                    }
                });
    }


    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:

                if(range){
                    noLoadSave();  //这个方法保存不上传服务器
                    //mapConfigSettingSave();  //这个方法是单独的保存上传到服务器
                }else{
                    Toast.makeText(this, "未在范围内选择地理位置，无法保存！", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private void noLoadSave() {
        Toast.makeText(GeographicalPositionMapA.this, "更改位置成功，请点击确定保存。", Toast.LENGTH_SHORT).show();

            /*    if(mainFlag.isEmpty()){ //表示是点击主账号进来设置地图配置的
                    // 子账号
            }else{
                //我是主账号
                setResult(21,);
            }*/
        Intent intent = new Intent();
        intent.putExtra("itemNumber",listItemNumber);
        intent.putExtra("agencysBean",agencysBean);
        setResult(21,intent);
        finish();
    }


    private void mapConfigSettingSave() {
       cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isDefault",1);
            JSONObject json = new JSONObject();
            //我不是主账号
            json.put("geogPosition",addressMap);
            json.put("lng",String.valueOf(lngs));
            json.put("lat",String.valueOf(lats));

            if(mainFlag.isEmpty()){ //表示是点击主账号进来设置地图配置的
                // 子账号
                 json.put("agencyId",agencysBean.getIdX());
                 jsonObject.put("agencyList","["+json.toString()+"]");
            }else{
                //我是主账号
                jsonObject.put("mainAgency",json.toString());
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL+"fb/setGeogPostion";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {

            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Toast.makeText(GeographicalPositionMapA.this, "保存位置成功！", Toast.LENGTH_SHORT).show();

            /*    if(mainFlag.isEmpty()){ //表示是点击主账号进来设置地图配置的
                    // 子账号
            }else{
                //我是主账号
                setResult(21,);
            }*/
                    Intent intent = new Intent();
                    intent.putExtra("itemNumber",listItemNumber);
                    intent.putExtra("agencysBean",agencysBean);
                   setResult(21,intent);

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
        }, "mapSaveTag", true);

        volleyNetCommon.addQueue(jsonObjectRequest);

    }
    private final int color = 0xAA00FF00;

    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        if (districtResult == null) {
            return;  
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            polyLines = districtResult.getPolylines();
            if (polyLines == null) {
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {
                OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                        .points(polyline).dottedLine(true).color(color);
                mBaiduMap.addOverlay(ooPolyline11);
                OverlayOptions ooPolygon = new PolygonOptions().points(polyline)
                        .stroke(new Stroke(5, 0xAA00FF88)).fillColor(getResources().getColor(android.R.color.transparent));
                mBaiduMap.addOverlay(ooPolygon);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
          /*  mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
*/
        }
    }


    @Override
    protected void onStart() {
        // 开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        // 开启方向传感器
        myOrientationListener.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();

        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        mMapView = null;
        if(volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll("mapSaveTag");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }



}


