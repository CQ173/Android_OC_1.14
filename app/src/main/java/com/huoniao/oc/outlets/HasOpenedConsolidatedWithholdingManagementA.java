package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.CallBack.MyItemTouchCallback;
import com.huoniao.oc.CallBack.OnRecyclerItemClickListener;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.RecyclerAdapter;
import com.huoniao.oc.bean.BankCardBean;
import com.huoniao.oc.bean.Item;
import com.huoniao.oc.common.BankIcon;
import com.huoniao.oc.user.HelpCenterA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.VibratorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HasOpenedConsolidatedWithholdingManagementA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;      //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.tv_save)
    TextView tvSave; //保存
    @InjectView(R.id.rv_withholding_drag)
    RecyclerView rvWithholdingDrag;

    List<Item> dragResulstDataList = new ArrayList<>();
    private List<BankCardBean> bankCardBeenList;  //已开通汇缴代扣 数据
    private Intent intent;

    private String id;
    private String bankName;
    private String isPublic;
    private String cardNo;
    private String bankCode;
    private String cardType;
    private List<BankCardBean> bankCardList = new ArrayList<BankCardBean>();
    private String custName;
    private String everyLimit;//单笔限额
    private String dailyLimit;//单日限额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_opened_consolidated_withholding_management);
        ButterKnife.inject(this);
        initWidget();
        if(bankCardBeenList !=null){
            bankCardBeenList.clear();
        }
        bankCardBeenList = SPUtils2.getList(HasOpenedConsolidatedWithholdingManagementA.this,"cardList");
        initData();
        initAdaper();

    }

    private void initData() {
        if(bankCardBeenList!=null && bankCardBeenList.size()>0){
            for (int i = 0; i < bankCardBeenList.size(); i++) {
                BankCardBean bankCardBean = bankCardBeenList.get(i);
                Item item = new Item();
                item.setId(i); //排序id
                item.setCardId(bankCardBean.getId()); //银行卡id
                item.setIsPublic(bankCardBean.getIsPublic()); //是否对公账户0不是 1是
                item.setBankCode(bankCardBean.getBankCode()); //银行code
                item.setBankName(bankCardBean.getCardName()); //银行名称
                item.setCardNo(bankCardBean.getCardnumber()); //卡号
                item.setCardType(bankCardBean.getCardType()); //1储蓄卡2信用卡  不用
                item.setImg(BankIcon.getBankIcon(bankCardBean.getBankCode()));
                item.setCustname(bankCardBean.getCustname()); //开户人姓名
                item.setEveryLimit(bankCardBean.getEveryLimit());//单笔限额
                item.setDailyLimit(bankCardBean.getDailyLimit());//单日限额
                dragResulstDataList.add(item);
            }
        }

    }

    private void initAdaper() {
        List<String> list = new ArrayList<>();
        list.add("1");
        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_drag_list,R.layout.item_footer,dragResulstDataList,list, this);
        rvWithholdingDrag.setHasFixedSize(true);
        rvWithholdingDrag.setAdapter(adapter);
        rvWithholdingDrag.setLayoutManager(new LinearLayoutManager(this));

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter));
        itemTouchHelper.attachToRecyclerView(rvWithholdingDrag);

        rvWithholdingDrag.addOnItemTouchListener(new OnRecyclerItemClickListener(rvWithholdingDrag) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition()!=dragResulstDataList.size()) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(HasOpenedConsolidatedWithholdingManagementA.this, 70);   //震动70ms
                }
            }

            @Override
            public void onItemClick(final RecyclerView.ViewHolder vh) {
                super.onItemClick(vh);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                      /*  if (vh instanceof RecyclerAdapter.FootHolder) {  //脚布局
                            final RecyclerAdapter.FootHolder footHolder = (RecyclerAdapter.FootHolder) vh;

                        }else if(vh instanceof RecyclerAdapter.MyViewHolder){
                            Item item = dragResulstDataList.get(vh.getLayoutPosition());
                            Toast.makeText(HasOpenedConsolidatedWithholdingManagementA.this,item.getId()+" "+item.getName(),Toast.LENGTH_SHORT).show();

                        }
*/
                    }
                });

            }
        });
    }

    private void initWidget() {
        MyApplication.getInstence().addActivity(this);
        tvTitle.setText("汇缴代扣管理");
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText("了解更多");
    }

    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:  //返回键
                sortConmit();
                // SPUtils2.putList(HasOpenedConsolidatedWithholdingManagementA.this, "cardList", new ArrayList<BankCardBean>());//将数据保存起来
                finish();
                break;
            case R.id.tv_save: //了解更多
                Intent intent = new Intent(HasOpenedConsolidatedWithholdingManagementA.this,HelpCenterA.class);
                intent.putExtra("learn_more","learn_more");
                startActivityIntent(intent);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sortConmit();
            SPUtils2.putList(HasOpenedConsolidatedWithholdingManagementA.this, "cardList", new ArrayList<BankCardBean>());//将数据保存起来
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    public void sortConmit(){
        try {
            String url = Define.URL+"acct/sort";
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            StringBuilder sb = new StringBuilder();
            if(dragResulstDataList.size()>0) {
                for (int i = 0; i < dragResulstDataList.size(); i++) {

                    jsonObject2.put("id",dragResulstDataList.get(i).getCardId());
                    jsonObject2.put("sort",i); //排序后的
                    sb.append(jsonObject2.toString()+",");
                }
            }
            StringBuilder replace = new StringBuilder();
            if(sb.length()>2) {
                replace = sb.replace(sb.length() - 1, sb.length(), "");
            }
            jsonObject.put("sortString","["+replace.toString()+"]");
            requestNet(url,jsonObject,"sort","0",true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *  请求汇缴代扣管理入口(关闭代扣后,需要调一次刷新数据)
     *
     */
    private void requestPayWhMangerEntrance(){
        String url = Define.URL+"acct/deductionsIndex";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "whMangerEntranceClose", "", true, true);
    }
    /**
     * 数据解析
     * @param object 数据源
     */
    private void dataAnalysis(JSONObject object){
        try {
            JSONObject jsonObject = object.getJSONObject("data");
            String result = jsonObject.getString("result");
            if ("success".equals(result)) {
                intent = new Intent(HasOpenedConsolidatedWithholdingManagementA.this, OpWithHoldFiveA.class);
                intent.putExtra("signState", "surrender");
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            } else {
                String msg = jsonObject.optString("msg");
                ToastUtils.showLongToast(HasOpenedConsolidatedWithholdingManagementA.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "terminateAnAgreement":
//                dataAnalysis(json);
//                ToastUtils.showToast(this ,"點擊了解除按鈕");
                break;
            case "sort":
                //   ToastUtils.showToast(HasOpenedConsolidatedWithholdingManagementA.this,"银行卡排序成功！");
                break;
            case  "switchDeductionsRequest":
                requestPayWhMangerEntrance();//关闭汇缴代扣后再调一次数据

                break;
            case "getUnDeductions":
//                json.toString();
                analysisUnDeductionsData(json);
                break;
            case  "whMangerEntranceClose":
                if(bankCardBeenList !=null){
                    bankCardBeenList.clear();
                }
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String isDeductions = jsonObject.getString("isDeductions");//是否开通汇缴代扣标识
                    if (Define.CLOSE.equals(isDeductions)){
                        JSONArray deductionsArr = jsonObject.getJSONArray("deductions");
                        for (int i = 0; i < deductionsArr.length() ; i++) {
                            BankCardBean deductionsCard = new BankCardBean();
                            JSONObject obj = (JSONObject) deductionsArr.get(i);
                            analysisCardList(deductionsCard, obj);
                        }

                        SPUtils2.putList(HasOpenedConsolidatedWithholdingManagementA.this, "closeCardList", bankCardBeenList);//将数据保存起来
                        SPUtils2.putList(HasOpenedConsolidatedWithholdingManagementA.this, "cardList", bankCardBeenList);//将数据保存起来
                        intent = new Intent(HasOpenedConsolidatedWithholdingManagementA.this, OpWithHoldTwoA.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("cardState", "closeState");
                        intent.putExtras(bundle);//发送数据
                        startActivity(intent);//启动intent
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    private void analysisUnDeductionsData(JSONObject jsonObject){
        if (bankCardBeenList != null){
            bankCardBeenList.clear();
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length() ; i++) {
                BankCardBean unDeductionsCard = new BankCardBean();
                JSONObject obj = (JSONObject) jsonArray.get(i);
                analysisCardList(unDeductionsCard, obj);
            }


            SPUtils2.putList(HasOpenedConsolidatedWithholdingManagementA.this, "unCardList", bankCardBeenList);//将数据保存起来

            intent = new Intent(HasOpenedConsolidatedWithholdingManagementA.this, OpWithHoldTwoA.class);

            startActivity(intent);//启动intent
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void analysisCardList(BankCardBean bankCardBean, JSONObject jsonObject){
        //银行卡id
        id = jsonObject.optString("id");
        //银行卡名称
        bankName = jsonObject.optString("bankName");
        //是否为对公账户
        isPublic = jsonObject.optString("isPublic");
        //银行卡号
        cardNo = jsonObject.optString("cardNo");
        //银行标识
        bankCode = jsonObject.optString("bankCode");
        //银行卡类型
        cardType = jsonObject.optString("cardType");
        //开户人姓名
        custName = jsonObject.optString("custName");
        //单笔限额
        everyLimit = jsonObject.optString("everyLimit");
        //单日限额
        dailyLimit = jsonObject.optString("dailyLimit");

        bankCardBean.setId(id);
        bankCardBean.setCardName(bankName);
        bankCardBean.setIsPublic(isPublic);
        bankCardBean.setCardnumber(cardNo);
        bankCardBean.setBankCode(bankCode);
        bankCardBean.setCardType(cardType);
        bankCardBean.setCustname(custName);
        bankCardBean.setEveryLimit(everyLimit);
        bankCardBean.setDailyLimit(dailyLimit);
        bankCardBeenList.add(bankCardBean);

    }

}
