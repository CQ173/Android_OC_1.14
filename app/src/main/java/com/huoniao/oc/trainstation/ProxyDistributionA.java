package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.bean.OutletsListBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MapData2;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.custom.MyMapView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.treeIdList;
import static com.huoniao.oc.MyApplication.treeIdList2;

public class ProxyDistributionA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_map)
    TextureMapView tvMap;
    @InjectView(R.id.mymap)
    MyMapView mymap;
    @InjectView(R.id.ll_map_container)
    LinearLayout llMapContainer;
    @InjectView(R.id.tv_trainTicketName)
    TextView tvTrainTicketName;
    @InjectView(R.id.tv_trainTicket_location)
    TextView tvTrainTicketLocation;
    @InjectView(R.id.tv_trainTicket_legalPerson)
    TextView tvTrainTicketLegalPerson;
    @InjectView(R.id.tv_trainTicket_phone)
    TextView tvTrainTicketPhone;
    @InjectView(R.id.ll_tree_outlets)
    LinearLayout llTreeOutlets;
    @InjectView(R.id.tv_admin_ownership_institution)
    TextView tvAdminOwnershipInstitution;
    @InjectView(R.id.activity_proxy_distribution)
    LinearLayout activityProxyDistribution;
    @InjectView(R.id.tv_affiliation3)
    TextView tvAffiliation3;
    @InjectView(R.id.mPullToRefreshListView)
    PullToRefreshListView mPullToRefreshListView;

    private ListView agentListView;
    private List<OutletsListBean.DataBean> dataBeanList = new ArrayList<>();
    private String next = "";        //返回来的页数
    private CommonAdapter<OutletsListBean.DataBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_distribution);
        ButterKnife.inject(this);
        initWidget();
        initData();
    }

    private void initWidget() {
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        agentListView = mPullToRefreshListView.getRefreshableView();

        initPullRefreshLinstener();

    }

    private void initData() {

        requestAgencyList(true, "1");
    }


    /**
     * //上拉加载更多
     */
    private void initPullRefreshLinstener() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {  //上拉加载更多
                if (next.equals("-1")) {
//                    Toast.makeText(AdvanceInCashA.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ToastUtils.showToast(ProxyDistributionA.this, "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                } else {

                    requestAgencyList(true, next);

                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_admin_ownership_institution, R.id.tv_affiliation3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_admin_ownership_institution:
                if (RepeatClickUtils.repeatClick()) {
                    showOwnershipPop(tvAdminOwnershipInstitution);
                }
                break;

            case R.id.tv_affiliation3:
                if (RepeatClickUtils.repeatClick()) {
                    treeIdList.clear();
                    getOjiAffiliationMapData();
                }

                break;
        }
    }


    MapData2 mapData2;
    BaiduMap baiduMap2;
    private View viewTreePop = null;
    private View viewTreePopT = null;
    private TextView tv_baidu_pop_t = null;
    private TextView tv_baidu_pop_w = null;

    private void getOjiAffiliationMapData() {
        baiduMap2 = tvMap.getMap();
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        mapData2 = new MapData2(ProxyDistributionA.this, cpd) {
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();

                myPopWindow = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        ListView lv_train_ownership = (ListView) view.findViewById(R.id.lv_train_ownership);
                        mapData2.setTrainOwnershipData(lv_train_ownership, baiduMap2);
                        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myPopWindow.dissmiss();
                            }
                        });

                    }

                    @Override
                    protected int layout() {
                        return R.layout.train_ownership_institution_pop;
                    }
                }.poPwindow(ProxyDistributionA.this, true).showAtLocation(ivBack, Gravity.CENTER, 0, 0);
            }

            @Override
            protected void zoomToSpan(List<AllTreeNode> allTreeNodesList, AllTreeNode allTreeNode, String type) {
                super.zoomToSpan(allTreeNodesList, allTreeNode, type);
                setZoomToSpanMap(allTreeNodesList, allTreeNode, type);
            }

            @Override
            protected void setTreeSingleMessage(AllTreeNode allTreeNode) {
                super.setTreeSingleMessage(allTreeNode);
                setTreeSingleMessage2(allTreeNode);
            }
        };
    }


    //拿到数据进行展示管理员o计界面地图
    public void setZoomToSpanMap(List<AllTreeNode> allTreeNodesList, AllTreeNode all, String type) {
        MyApplication.type = type;
        List<Overlay> mOverlayList = new ArrayList<Overlay>();
        ;
        baiduMap2.clear();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.train_location);
        for (AllTreeNode allTreeNode : allTreeNodesList
                ) {

            AllTreeNode info = new AllTreeNode();
            info.lat = allTreeNode.lat;
            info.lng = allTreeNode.lng;
            info.name = allTreeNode.name;
            info.corpName = allTreeNode.corpName;
            info.phone = allTreeNode.phone;
            info.geogPosition = allTreeNode.geogPosition;
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            if (!(info.lat == 0.0 || info.lng == 0.0)) {
                Overlay overlay = baiduMap2.addOverlay(new MarkerOptions().position(new LatLng(allTreeNode.lat, allTreeNode.lng)).icon(bitmap));
                overlay.setExtraInfo(bundle);
                mOverlayList.add(overlay);
            }
        }

        if (mOverlayList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : mOverlayList) {
                // polyline 中的点可能太多，只按marker 缩放
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            baiduMap2.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }


        if (MyApplication.type.equals("3") && all != null) {
            if (!(all.lat == 0.0 || all.lng == 0.0)) {
                MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(new LatLng(all.lat, all.lng));
                baiduMap2.animateMapStatus(uc);

                final InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        baiduMap2.hideInfoWindow();
                        MyApplication.type = "0";
                    }
                };


                if (viewTreePop == null) {
                    viewTreePop = getLayoutInflater().inflate(R.layout.baidu_pop, null);
                    tv_baidu_pop_t = (TextView) viewTreePop.findViewById(R.id.tv_baidu_pop); //百度地图弹窗文字
                }
                tv_baidu_pop_t.setText(all.name);
                //创建InfoWindow展示的view
                InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(viewTreePop), new LatLng(all.lat, all.lng), -30, onInfoWindowClickListener);
                baiduMap2.showInfoWindow(mInfoWindow);

            } else {
                Toast.makeText(MyApplication.mContext, "你没有设置地理位置，无法再地图上找到你的地理位置！", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 地图 Marker 覆盖物点击事件监听函数
         * @param marker 被点击的 marker
         */
        baiduMap2.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                AllTreeNode info = (AllTreeNode) marker.getExtraInfo().get("info");

                // 将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                final InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        baiduMap2.hideInfoWindow();
                        MyApplication.type = "0";
                    }
                };


                if (viewTreePopT == null) {
                    viewTreePopT = getLayoutInflater().inflate(R.layout.baidu_pop, null);
                    tv_baidu_pop_w = (TextView) viewTreePopT.findViewById(R.id.tv_baidu_pop); //百度地图弹窗文字
                }
                tv_baidu_pop_w.setText(info.name);
                InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(viewTreePopT), ll, -30, onInfoWindowClickListener);
                baiduMap2.showInfoWindow(mInfoWindow);

                setTreeSingleMessage2(info);  //设置点击锚点信息展示
                return true;

            }
        });

        baiduMap2.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                baiduMap2.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void setTreeSingleMessage2(AllTreeNode info) {
        llTreeOutlets.setVisibility(View.VISIBLE);
        tvTrainTicketName.setText(info.name);
        tvTrainTicketLocation.setText(info.geogPosition);
        tvTrainTicketLegalPerson.setText(info.corpName);
        tvTrainTicketPhone.setText(info.phone);
    }


    MyPopWindow myPopWindow;
    MapData mapData;
    private float xs;
    private float ys;
    private String officeIdStr = "";
    private String officeNodeName;

    /**
     * 获取归属机构数据 弹出归属机构
     */
    private void showOwnershipPop(final TextView tvInstitution) {
        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
//得到状态栏高度
//返回键关闭
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        mapData = new MapData(ProxyDistributionA.this, cpd, false) {
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }

                myPopWindow = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        ListView lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                        mapData.setTrainOwnershipData(lv_audit_status);
                        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                        int[] arr = new int[2];
                        tvInstitution.getLocationOnScreen(arr);
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                        xs = arr[0] + tvInstitution.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + tvInstitution.getHeight();
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node officeId) {
                                officeIdStr = String.valueOf(officeId.getAllTreeNode().id); // 归属机构筛选id
                                officeNodeName = officeId.getAllTreeNode().name;
                               /* if ("todayPayment".equals(institutionTag)){
                                    getFinTodayPaysys(officeIdStr,false,true);
                                }else if ("getTotalList".equals(institutionTag)){
                                    setRequestlineChartData(true);
                                    switchBackgroud(tv_admin_week,true);
                                }*/

                                tvInstitution.setText(officeNodeName);
                                requestAgencyList(true, "1");
                                myPopWindow.dissmiss();

                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop3;
                    }
                }.popupWindowBuilder(ProxyDistributionA.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                myPopWindow.showAtLocation(tvInstitution, Gravity.NO_GRAVITY, (int) xs, (int) ys);
            }
        };
    }

    /**
     * 代售点列表
     */
    private void requestAgencyList(boolean off, String pageNo) {
        String url = Define.URL + "fb/agencyList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId", officeIdStr);
            jsonObject.put("pageNo", pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "agencyList", "1", off, false); //1没有实际意义值

    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "agencyList":
                Log.d("agencyList", json.toString());
                setAdapter(json, pageNumber);
                break;
        }
    }

    private void setAdapter(JSONObject jsonObject, String pageNo) {
        Gson gson = new Gson();
        OutletsListBean outletsListBean = gson.fromJson(jsonObject.toString(), OutletsListBean.class);


        if (pageNo.equals("1")) {
            //集成清空处理
            dataBeanList.clear();
//            if (adapter != null) {
//                agentListView.setAdapter(adapter);
//            }
        }
        List<OutletsListBean.DataBean> dataList = outletsListBean.getData();

        if (dataList != null && dataList.size() > 0) {
            dataBeanList.addAll(dataList);
        }
        next = String.valueOf(outletsListBean.getNext());


        adapter = new CommonAdapter<OutletsListBean.DataBean>(ProxyDistributionA.this, dataBeanList, R.layout.station_oltmanagelist_item) {
            @Override
            public void convert(ViewHolder holder, OutletsListBean.DataBean dataBean) {
                holder.setText(R.id.tv_name, dataBean.getName())
                        .setText(R.id.tv_windowNumber, dataBean.getWinNumber())
                        .setText(R.id.tv_city, dataBean.getArea().getName());


                final TextView tv_outletNumber = holder.getView(R.id.tv_outletNumber);
                tv_outletNumber.setText(dataBean.getCode());
                tv_outletNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String outletNumber = tv_outletNumber.getText().toString();
                        Intent intent = new Intent(ProxyDistributionA.this, WindowsAnchoredListA.class);
                        intent.putExtra("outletNumber", outletNumber);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    }
                });

                CheckBox checkBox = holder.getView(R.id.checkBox);
                checkBox.setVisibility(View.GONE);
                ImageView ivInto = holder.getView(R.id.iv_into);
                ivInto.setVisibility(View.GONE);

            }
        };


        agentListView.setAdapter(adapter);
        agentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OutletsListBean.DataBean dataBean = dataBeanList.get(i - 1);

                Intent intent = new Intent(ProxyDistributionA.this, StationOutletsDetailsA.class);
                intent.putExtra("id", dataBean.getId());
                intent.putExtra("name", dataBean.getName());
                intent.putExtra("code", dataBean.getCode());
                intent.putExtra("city", dataBean.getArea().getName());
                intent.putExtra("winNumber", dataBean.getWinNumber());
                intent.putExtra("corpName", dataBean.getCorpName());
                intent.putExtra("corpMobile", dataBean.getCorpMobile());
                intent.putExtra("corpIdNum", dataBean.getCorpIdNum());
                intent.putExtra("master", dataBean.getMaster());
                intent.putExtra("operatorName", dataBean.getOperatorName());
                intent.putExtra("operatorMobile", dataBean.getOperatorMobile());
                intent.putExtra("operatorIdNum", dataBean.getOperatorIdNum());
                intent.putExtra("phone", dataBean.getPhone());

                intent.putExtra("state", dataBean.getState());

                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
    }


    @Override
    public void onPause() {
        if (tvMap != null) {
            tvMap.onPause();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        if (tvMap != null) {
            tvMap.onResume();
        }
        super.onResume();
    }

    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        mPullToRefreshListView.onRefreshComplete();
    }

}
