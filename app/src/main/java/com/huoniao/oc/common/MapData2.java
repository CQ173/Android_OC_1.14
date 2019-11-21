package com.huoniao.oc.common;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.SimpleTreeListViewAdapter;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.bean.NodeMap;
import com.huoniao.oc.bean.TreeBean;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.common.tree.adapter.TreeListViewAdapter;
import com.huoniao.oc.idal.INetResultDissmissListener;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.MyApplication.treeIdList;

/**
 * Created by admin on 2017/9/20.
 */

public class MapData2 {
    private List<TreeBean> trainOwnershipList;
    private	 VolleyNetCommon volleyNetCommon ;
    List<AllTreeNode>  allTreeNodesList = new ArrayList<>();
    private SimpleTreeListViewAdapter simpleTreeListViewAdapter;
    private String officeId = "";//所属机构 ID
    CustomProgressDialog cpd;
    private BaseActivity activity;
    private CommonINetResultDissmiss commonINetResultDissmiss = new CommonINetResultDissmiss();
    private String dissmissCode = "-1"; //表示如果不是成功的就直接关闭 是成功等获取数据并且处理完数据在关闭

    public MapData2(BaseActivity activity ,CustomProgressDialog cpd){
        this.cpd = cpd;
        this.activity = activity;
        getTrainMapData();
    }
    //获取火车站地图信息
    private void getTrainMapData() {
        allTreeNodesList.clear();
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL+"fb/getOfficeInfo";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(activity) {
            @Override
            public void volleyResponse(Object o) {
                Log.i("aa","");
            }

            @Override
            public void volleyError(VolleyError volleyError) {
                cpd.dismiss();
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                dissmissCode = "0";
                Gson gson = new Gson();
                NodeMap nodeMap = gson.fromJson(json.toString(), NodeMap.class);
                AllTreeNode allTree = new AllTreeNode();
                allTree.corpName = nodeMap.getRoot().getCorpName();
                allTree.geogPosition = nodeMap.getRoot().getGeogPosition();
                allTree.id = nodeMap.getRoot().getId();
                allTree.parentId = nodeMap.getRoot().getParentId();
                allTree.winNumber = nodeMap.getRoot().getWinNumber();
                allTree.type = nodeMap.getRoot().getType();
                allTree.name = nodeMap.getRoot().getName();
                allTree.phone = nodeMap.getRoot().getPhone();
                if(!nodeMap.getRoot().getLat().isEmpty()){
                    allTree.lat =Double.parseDouble(nodeMap.getRoot().getLat());
                }
                if(!nodeMap.getRoot().getLng().isEmpty()){
                    allTree.lng  = Double.parseDouble(nodeMap.getRoot().getLng());
                }
                allTreeNodesList.add(allTree);   //添加根节点

                if(!treeIdList.contains(allTree.id)) {  // 层级树最顶层层id
                    treeIdList.add(allTree.id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
                }
                List<NodeMap.RootBean.ChildOfficesBeanXX> childOffices = nodeMap.getRoot().getChildOffices();
                if(childOffices != null && childOffices.size()>0) {

                    String defaultId = childOffices.get(0).getId() == null ? "" : childOffices.get(0).getId(); //默认请求
                    for (int i = 0; i < childOffices.size(); i++) {
                        AllTreeNode allTreeNode = new AllTreeNode();
                        allTreeNode.corpName = childOffices.get(i).getCorpName();
                        allTreeNode.geogPosition = childOffices.get(i).getGeogPosition();
                        allTreeNode.id = childOffices.get(i).getId();
                        if (!childOffices.get(i).getLat().isEmpty()) {
                            if(childOffices.get(i).getLat().equals("undefined")){
                                allTreeNode.lat = 0.0;
                            }else {
                                allTreeNode.lat = Double.parseDouble(childOffices.get(i).getLat());
                            }
                        }
                        if (!childOffices.get(i).getLng().isEmpty()) {
                            if(childOffices.get(i).getLat().equals("undefined")) {
                                allTreeNode.lng = 0.0;
                            }else {
                                allTreeNode.lng = Double.parseDouble(childOffices.get(i).getLng());
                            }
                        }
                        allTreeNode.name = childOffices.get(i).getName();
                        allTreeNode.parentId = childOffices.get(i).getParentId();
                        allTreeNode.phone = childOffices.get(i).getPhone();
                        allTreeNode.type = childOffices.get(i).getType();
                        allTreeNode.winNumber = childOffices.get(i).getWinNumber();
                        allTreeNodesList.add(allTreeNode);
                        //	diGuiChildOffices(childOffices.get(i).getChildOffices());  //递归所有的childOffices
                    }


                    setDefaultRequest(defaultId); //获取下一级菜单

                    ;
                }else{

                        showTrainMapDialog();

                }
            }

            @Override
            protected void PdDismiss() {
                if(dissmissCode.equals("0")){
                    commonINetResultDissmiss.setiNetResultDissmissListener(new INetResultDissmissListener() {
                        @Override
                        public void dissmiss(boolean dissmiss) {
                            if(dissmiss){
                                cpd.dismiss();
                                dissmissCode = "-1";
                            }
                        }
                    });
                }else{
                    cpd.dismiss();
                }
            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
        }, "getOfficeInfo", true);
        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    //火车站 归属机构默认请求下一级
    private void setDefaultRequest(final String id) {
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "fb/getOfficeInfo", jsonObject, new VolleyAbstract(activity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                cpd.dismiss();
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                dissmissCode = "0";
                Gson gson = new Gson();
                NodeMap nodeMap = gson.fromJson(json.toString(), NodeMap.class);
                List<NodeMap.RootBean.ChildOfficesBeanXX> childOffices = nodeMap.getRoot().getChildOffices();
                if(childOffices != null && childOffices.size()>0) {
                    String defaultId = childOffices.get(0).getId() == null ? "" : childOffices.get(0).getId(); //默认请求

                    for (int i = 0; i < childOffices.size(); i++) {
                        AllTreeNode allTreeNode = new AllTreeNode();
                        allTreeNode.corpName = childOffices.get(i).getCorpName();
                        allTreeNode.geogPosition = childOffices.get(i).getGeogPosition();
                        allTreeNode.id = childOffices.get(i).getId();

                        if (!childOffices.get(i).getLat().isEmpty()) {
                            if(childOffices.get(i).getLat().equals("undefined")){
                                allTreeNode.lat = 0.0;
                            }else {
                                allTreeNode.lat = Double.parseDouble(childOffices.get(i).getLat());
                            }
                        }
                        if (!childOffices.get(i).getLng().isEmpty()) {
                            if(childOffices.get(i).getLat().equals("undefined")) {
                                allTreeNode.lng = 0.0;
                            }else {
                                allTreeNode.lng = Double.parseDouble(childOffices.get(i).getLng());
                            }
                        }
                        allTreeNode.name = childOffices.get(i).getName();
                        allTreeNode.parentId = childOffices.get(i).getParentId();
                        allTreeNode.phone = childOffices.get(i).getPhone();
                        allTreeNode.type = childOffices.get(i).getType();
                        allTreeNode.winNumber = childOffices.get(i).getWinNumber();
                        allTreeNodesList.add(allTreeNode);
                    }
                    setDefaultRequest(defaultId);  //递归
                }else{

                    //setShowMark(); //递归结束展示数据
                    zoomToSpan(allTreeNodesList,null,"1"); //递归结束后展示数据  1不代表什么

                        showTrainMapDialog();

                }
                if(!treeIdList.contains(id)) {
                    treeIdList.add(id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
                }
            }

            @Override
            protected void PdDismiss() {
                if(dissmissCode.equals("0")){
                    commonINetResultDissmiss.setiNetResultDissmissListener(new INetResultDissmissListener() {
                        @Override
                        public void dissmiss(boolean dissmiss) {
                            if(dissmiss){
                                cpd.dismiss();
                                dissmissCode = "-1";
                            }
                        }
                    });
                }else{
                    cpd.dismiss();
                }
            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
        }, "getOfficeInfoDefault", true);

        volleyNetCommon.addQueue(jsonObjectRequest);

    }

    //弹出popwindow
    protected void showTrainMapDialog() {
        cpd.dismiss();
        if(commonINetResultDissmiss.iNetResultDissmissListener !=null){
            commonINetResultDissmiss.iNetResultDissmissListener.dissmiss(true);
        }
    }

    View baiduView;
    TextView tv_baidu_pop;
    //获取火车站归属机构数据
    public void setTrainOwnershipData(ListView lv_train_ownership, final BaiduMap baiduMap) {
        getTrainOwnershipData();
        if(trainOwnershipList != null && trainOwnershipList.size()>0) {
            try {
                simpleTreeListViewAdapter = new SimpleTreeListViewAdapter<TreeBean>(lv_train_ownership, MyApplication.mContext,
                        trainOwnershipList, 1);  //1表示只默认只展开2级菜单
                lv_train_ownership.setAdapter(simpleTreeListViewAdapter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            //树点击
            simpleTreeListViewAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(final Node node, int position) {
                    if (node.isLeaf()) {
                      /*  Toast.makeText(MyApplication.mContext, node.getAllTreeNode().name,
                                Toast.LENGTH_SHORT).show();*/
                    }
                    if (node.getAllTreeNode().type.equals("2") || node.getAllTreeNode().type.equals("3")) {  //处理节点选择器
                        simpleTreeListViewAdapter.setSelectedItem(position, Long.parseLong(node.getAllTreeNode().id.substring(0,15),16));
                        simpleTreeListViewAdapter.notifyDataSetChanged();
                    }

                    if(!node.getAllTreeNode().type.equals("3") && !treeIdList.contains(node.getAllTreeNode().id)) { //如果包含这个id表示已经通过这个id进行搜索节点数据过了没有必要再点击再次请求网络加载数据
                        setClickLoadSingleTreeData(node.getAllTreeNode().id,position, node.getAllTreeNode().type, node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
                    }

                    if(node.getAllTreeNode().type.equals("2")){   //点击节点火车站 继续刷新  获取相应代售点 进行地图展示
                        setClickLoadSingleTreeData(node.getAllTreeNode().id,position,node.getAllTreeNode().type, node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
                    }

                    if(node.getAllTreeNode().type.equals("3")){
                        setClickLoadSingleTreeData(node.getAllTreeNode().parentId,position,node.getAllTreeNode().type,node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
                        ThreadCommonUtils.runonuiThread(new Runnable() {
                            @Override
                            public void run() {
                                setTreeSingleMessage(node.getAllTreeNode());
                            }
                        });

                        if(baiduView == null){
                            baiduView = activity.getLayoutInflater().inflate(R.layout.baidu_pop,null);
                            tv_baidu_pop = (TextView) baiduView.findViewById(R.id.tv_baidu_pop); //百度地图弹窗文字
                        }

                        tv_baidu_pop.setText(node.getAllTreeNode().name);
                        InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick() {
                                baiduMap.hideInfoWindow();
                            }
                        };
                        InfoWindow	mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(baiduView), new LatLng(node.getAllTreeNode().lat,node.getAllTreeNode().lng), -30, onInfoWindowClickListener);
                        baiduMap.showInfoWindow(mInfoWindow);

                    }




                }
            });
        }
    }

    //获取点击之后动态加载tree节点数据
    private void setClickLoadSingleTreeData(final String id, final int position, final String type, final AllTreeNode allTreeNode) {
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL+"fb/getOfficeInfo";
        final JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(activity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(MyApplication.mContext,R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {

                List<AllTreeNode>  allTreeList = new ArrayList<>();
                Gson gson = new Gson();
                NodeMap nodeMap = gson.fromJson(json.toString(), NodeMap.class);
                List<NodeMap.RootBean.ChildOfficesBeanXX> childOffices = nodeMap.getRoot().getChildOffices();
                if(childOffices != null && childOffices.size()>0) {
                    for (int i = 0; i < childOffices.size(); i++) {
                        AllTreeNode allTreeNode = new AllTreeNode();
                        allTreeNode.corpName = childOffices.get(i).getCorpName();
                        allTreeNode.geogPosition = childOffices.get(i).getGeogPosition();
                        allTreeNode.id = childOffices.get(i).getId();
                        if (!childOffices.get(i).getLat().isEmpty()) {
                            allTreeNode.lat = Double.parseDouble(childOffices.get(i).getLat());
                        }
                        if (!childOffices.get(i).getLng().isEmpty()) {
                            allTreeNode.lng = Double.parseDouble(childOffices.get(i).getLng());
                        }
                        allTreeNode.name = childOffices.get(i).getName();
                        allTreeNode.parentId = childOffices.get(i).getParentId();
                        allTreeNode.phone = childOffices.get(i).getPhone();
                        allTreeNode.type = childOffices.get(i).getType();
                        allTreeNode.winNumber = childOffices.get(i).getWinNumber();
                        allTreeList.add(allTreeNode);
                    }
                }
                if(MyApplication.type.equals("2")){
                    if(allTreeNodesList != null && allTreeList.size()<=0) {
                        Toast.makeText(MyApplication.mContext, "该层级你还没有代售点！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 下面这两个判断   解决重复加载 并且可以刷新地图
                if(!treeIdList.contains(id)) {
                    simpleTreeListViewAdapter.addExtraNode(position,allTreeList);  //动态增加火车站层级树节点
                    allTreeNodesList.addAll(allTreeList);
                    treeIdList.add(id);
                }

                if (type.equals("2") || type.equals("3")) {  //表示只有火车站才可以进行更新地图
                    zoomToSpan(allTreeList,allTreeNode,type);
                }
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
        }, "setClickLoadSingleTreeData", true);

        volleyNetCommon.addQueue(jsonObjectRequest);

    }

    private void getTrainOwnershipData() {
        trainOwnershipList = new ArrayList<>();
        trainOwnershipList.clear();
        if(allTreeNodesList != null && allTreeNodesList.size()>0){
            for (int i = 0; i < allTreeNodesList.size(); i++) {
                AllTreeNode allTreeNode = allTreeNodesList.get(i);
                if(allTreeNode.id.equals("1") && allTreeNode.parentId.isEmpty()){
                    allTreeNode.parentId ="2324323232131312a"; //顶级节点随便取
                    allTreeNode.id = "a566565656565656566";
                    long id =Long.parseLong(allTreeNode.id.substring(0,15),16);
                    long pid = Long.parseLong(allTreeNode.parentId.substring(0,15),16);
                    String name = allTreeNode.name;
                    trainOwnershipList.add(new TreeBean(id,pid,name,allTreeNode));
                    if(!treeIdList.contains(allTreeNode.id)) {  // 层级树最顶层层id
                        treeIdList.add(allTreeNode.id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
                    }

                }else {
                    if (allTreeNode.parentId.equals("1")) {
                        allTreeNode.parentId = "a566565656565656566"; //顶级节点随便取
                        long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
                        long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
                        String name = allTreeNode.name;
                        trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
                    } else {
                        if (allTreeNode.id.length() > 16 && allTreeNode.parentId.length() > 16) {
                            long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
                            long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
                            String name = allTreeNode.name;
                            trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
                        }
                    }
                    Log.e("yyyyy", "id>>>>>>>" + allTreeNode.id + "--------parentId>>>>>" + allTreeNode.parentId + "\n");
                }
            }

			/*for (AllTreeNode allTreeNode: allTreeNodesList
				 ) {
				AllTreeNode allTreeNodes = new AllTreeNode();
				Integer.parseInt("FF",16);
				int id =Integer.parseInt(allTreeNode.id.substring(0,6),16);

				int pid = Integer.parseInt(allTreeNode.parentId.substring(0,6),16);
				String name = allTreeNode.name;


				trainOwnershipList.add(new TreeBean(id,pid,name,allTreeNodes));

			}*/
        }
    }

    //对地图进行展示
    protected void  zoomToSpan(List<AllTreeNode> allTreeNodesList, AllTreeNode allTreeNode, String type){

    }

     //设置锚点展示信息
    protected  void setTreeSingleMessage(AllTreeNode allTreeNode){

    }

}
