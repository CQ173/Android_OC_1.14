package com.huoniao.oc.common;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.SimpleTreeListViewAdapter2;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.bean.NodeMap;
import com.huoniao.oc.bean.TreeBean;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.common.tree.adapter.TreeListViewAdapter2;
import com.huoniao.oc.idal.INetResultDissmissListener;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.MyApplication.treeIdList2;

/**
 * Created by Administrator on 2017/9/5.
 */

public class MapData {
    private List<TreeBean> trainOwnershipList;
    private	 VolleyNetCommon volleyNetCommon ;
    List<AllTreeNode>  allTreeNodesList = new ArrayList<>();
    private SimpleTreeListViewAdapter2 simpleTreeListViewAdapter;
    private String officeId = "";//所属机构 ID
    CustomProgressDialog cpd;
    private BaseActivity activity;
    private CommonINetResultDissmiss commonINetResultDissmiss = new CommonINetResultDissmiss();
    private boolean outletsOff;
    private String dissmissCode = "-1"; //表示如果不是成功的就直接关闭 是成功等获取数据并且处理完数据在关闭
    public MapData(BaseActivity activity ,CustomProgressDialog cpd, boolean outletsOff){
       this.cpd = cpd;
        this.activity = activity;
        this.outletsOff = outletsOff;
        getTrainMapData();
    }
    //获取火车站地图信息
    private void getTrainMapData() {
        allTreeNodesList.clear();
        cpd.show();
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

                if(!treeIdList2.contains(allTree.id)) {  // 层级树最顶层层id
                    treeIdList2.add(allTree.id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
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
                        String type = childOffices.get(i).getType() == null ? "" : childOffices.get(i).getType(); //默认请求
                    /*    if(!type.equals("3")) {
                            allTreeNodesList.add(allTreeNode);
                        }else if(type.equals("3")){
                            if(outletsOff){
                                allTreeNodesList.add(allTreeNode);
                            }
                        }*/
                        if(type.equals("3")) {
                            if(outletsOff){
                                allTreeNodesList.add(allTreeNode);
                            }
                        }else {
                            allTreeNodesList.add(allTreeNode);
                        }

                        //	diGuiChildOffices(childOffices.get(i).getChildOffices());  //递归所有的childOffices
                    }

                    String type = childOffices.get(0).getType() == null ? "" : childOffices.get(0).getType(); //默认请求
                    if(type.equals("3")) {  //代售点不要显示
                        if (!outletsOff) {
                            showTrainMapDialog(); //弹出框框
                        }else {
                            setDefaultRequest(defaultId); //获取下一级菜单
                        }
                    }else{
                        setDefaultRequest(defaultId); //获取下一级菜单
                    }
                    ;
                }else{
                    showTrainMapDialog(); //弹出框框
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
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
                cpd.dismiss();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                dissmissCode = "0";
                Gson gson = new Gson();
                NodeMap nodeMap = gson.fromJson(json.toString(), NodeMap.class);
                List<NodeMap.RootBean.ChildOfficesBeanXX> childOffices = nodeMap.getRoot().getChildOffices();
                if (childOffices != null && childOffices.size() > 0) {
                String type = childOffices.get(0).getType() == null ? "" : childOffices.get(0).getType();
                if(type.equals("3")){
                    if (!outletsOff) {
                        showTrainMapDialog();
                    }
                }else {
                    if (childOffices != null && childOffices.size() > 0) {
                        String defaultId = childOffices.get(0).getId() == null ? "" : childOffices.get(0).getId(); //默认请求

                        for (int i = 0; i < childOffices.size(); i++) {
                            AllTreeNode allTreeNode = new AllTreeNode();
                            allTreeNode.corpName = childOffices.get(i).getCorpName();
                            allTreeNode.geogPosition = childOffices.get(i).getGeogPosition();
                            allTreeNode.id = childOffices.get(i).getId();

                            if (!childOffices.get(i).getLat().isEmpty()) {
                                if (childOffices.get(i).getLat().equals("undefined")) {
                                    allTreeNode.lat = 0.0;
                                } else {
                                    allTreeNode.lat = Double.parseDouble(childOffices.get(i).getLat());
                                }
                            }
                            if (!childOffices.get(i).getLng().isEmpty()) {
                                if (childOffices.get(i).getLat().equals("undefined")) {
                                    allTreeNode.lng = 0.0;
                                } else {
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
                    } else {

				/*	//setShowMark(); //递归结束展示数据
					zoomToSpan(allTreeNodesList,null,"1"); //递归结束后展示数据  1不代表什么*/
                        showTrainMapDialog();
                    }
                }
               if(!treeIdList2.contains(id)) {
                    treeIdList2.add(id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
               }
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
        }, "getOfficeInfoDefault", true);

        volleyNetCommon.addQueue(jsonObjectRequest);

    }

    //开始弹窗并加载数据  需要重写
    protected void showTrainMapDialog() {
        cpd.dismiss();
        if(commonINetResultDissmiss.iNetResultDissmissListener !=null){
            commonINetResultDissmiss.iNetResultDissmissListener.dissmiss(true);
        }
    }

    //获取火车站归属机构数据
    public void setTrainOwnershipData(ListView lv_train_ownership) {
        getTrainOwnershipData();
        if(trainOwnershipList != null && trainOwnershipList.size()>0) {
            try {
                simpleTreeListViewAdapter = new SimpleTreeListViewAdapter2<TreeBean>(lv_train_ownership, MyApplication.mContext,
                        trainOwnershipList, 1,outletsOff);  //1表示只默认只展开2级菜单
                lv_train_ownership.setAdapter(simpleTreeListViewAdapter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            //树的箭头点击
            simpleTreeListViewAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter2.OnTreeNodeClickListener() {
                @Override
                public void onClick(final Node node, int position) {
                    if (node.isLeaf()) {
                     /*   Toast.makeText(MyApplication.mContext, node.getAllTreeNode().name,
                                Toast.LENGTH_SHORT).show();*/
                    }
                    if (node.getAllTreeNode().type.equals("2") || node.getAllTreeNode().type.equals("3")) {  //处理节点选择器
                        simpleTreeListViewAdapter.setSelectedItem(position, Long.parseLong(node.getAllTreeNode().id.substring(0,15),16));
                        simpleTreeListViewAdapter.notifyDataSetChanged();
                    }

                    if(!node.getAllTreeNode().type.equals("3") && !node.getAllTreeNode().type.equals("2")  && !treeIdList2.contains(node.getAllTreeNode().id)) { //如果包含这个id表示已经通过这个id进行搜索节点数据过了没有必要再点击再次请求网络加载数据
                        setClickLoadSingleTreeData(node.getAllTreeNode().id,position, node.getAllTreeNode().type, node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
                    }

//					if(node.getAllTreeNode().type.equals("2")){   //点击节点火车站 继续刷新  获取相应代售点 进行地图展示
//						setClickLoadSingleTreeData(node.getAllTreeNode().id,position,node.getAllTreeNode().type, node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
//					}

                  if(outletsOff && node.getAllTreeNode().type.equals("2") && !treeIdList2.contains(node.getAllTreeNode().id)){
                      setClickLoadSingleTreeData(node.getAllTreeNode().id,position, node.getAllTreeNode().type, node.getAllTreeNode()); //获取点击之后动态加载tree节点数据
                  }


                }
             //树的条目点击
                @Override
                public void listViewOnItemData(Node node, int position) {

                    if(!(node.getAllTreeNode().type.equals("3"))){
                        officeId = String.valueOf(node.getId());
                        // 写回调处理
                        if(mapTreeNodeResult!=null){
                            mapTreeNodeResult.treeNodeResult(node);
                        }
                      /*  if(!(node.getAllTreeNode().type.equals("2"))) {
                            Toast.makeText(activity, "选中了" + node.getAllTreeNode().name+",请手动关闭弹出框！", Toast.LENGTH_SHORT).show();
                        }*/
                    }else{
                        if(outletsOff){
                            if(mapTreeNodeResult!=null){
                                mapTreeNodeResult.treeNodeResult(node);
                            }
                        }
                    }
                }
            });
        }
    }




    private void getTrainOwnershipData() {
        trainOwnershipList = new ArrayList<>();
        trainOwnershipList.clear();
        if(allTreeNodesList != null && allTreeNodesList.size()>0){
            for (int i = 0; i < allTreeNodesList.size(); i++) {
                AllTreeNode allTreeNode = allTreeNodesList.get(i);
                if(allTreeNode.id.equals("1") && allTreeNode.parentId.isEmpty()){
                    allTreeNode.parentId ="2324323232131312a"; //顶级节点随便取
                    long id= 1;
                    if(allTreeNode.id.equals("1")){
                        id= Long.parseLong(allTreeNode.id);
                    }else{
                         id =Long.parseLong(allTreeNode.id.substring(0,15),16);
                    }
                  //  allTreeNode.id = "a566565656565656566";

                    long pid = Long.parseLong(allTreeNode.parentId.substring(0,15),16);
                    String name = allTreeNode.name;
                    trainOwnershipList.add(new TreeBean(id,pid,name,allTreeNode));
                    if(!treeIdList2.contains(allTreeNode.id)) {  // 层级树最顶层层id
                        treeIdList2.add(allTreeNode.id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
                    }

                }else {
                    if (allTreeNode.parentId.equals("1")) {
                     //   allTreeNode.parentId = "a566565656565656566"; //顶级节点随便取
                        long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
                       // long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
                        String name = allTreeNode.name;
                        long pid = Long.parseLong(allTreeNode.parentId);
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


    //获取点击之后动态加载tree节点数据
    private void setClickLoadSingleTreeData(final String id, final int position, final String type, final AllTreeNode allTreeNode) {
        cpd.show();
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
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                List<AllTreeNode> allTreeList = new ArrayList<>();
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
                if(!treeIdList2.contains(id)) {
                    simpleTreeListViewAdapter.addExtraNode(position,allTreeList);  //动态增加火车站层级树节点
                    allTreeNodesList.addAll(allTreeList);
                    treeIdList2.add(id);
                }

//				if (type.equals("2") || type.equals("3")) {  //表示只有火车站才可以进行更新地图
//					zoomToSpan(allTreeList,allTreeNode,type);
//				}
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

    private MapTreeNodeResult mapTreeNodeResult;
    public void setMapTreeNodeResultLinstener(MapTreeNodeResult mapTreeNodeResult){
        this.mapTreeNodeResult = mapTreeNodeResult;
    }
    public interface MapTreeNodeResult{
        void treeNodeResult(Node officeId);
    }

}
