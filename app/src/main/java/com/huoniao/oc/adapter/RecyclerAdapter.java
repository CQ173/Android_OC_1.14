package com.huoniao.oc.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.CallBack.MyItemTouchCallback;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.ConfigureBean;
import com.huoniao.oc.bean.Item;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.outlets.CancellationNoticeA;
import com.huoniao.oc.outlets.OpWithHoldThreeA;
import com.huoniao.oc.user.AccountLogOffAuditingA;
import com.huoniao.oc.user.DataChangeApplyA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MyItemTouchCallback.ItemTouchAdapter {

    private Context context;
    private int src;
    private List<Item> results;
    private int item_foot;
    private List<String> mFootList;

    private static final int BODY_TYPE = 1;
    private static final int FOOT_TYPE = 2;
    private Intent intent;
    private BaseActivity mActivity;
    private MyPopWindow myPopWindow;


    public RecyclerAdapter(int src, int item_foot, List<Item> results, List<String> mFootList, BaseActivity activity) {
        this.results = results;
        this.src = src;
        this.item_foot = item_foot;
        this.mFootList = mFootList;
        this.mActivity = activity;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        //   根据viewType来确定加载那个布局，返回哪一个ViewHolder
        switch (viewType) {
            case BODY_TYPE:
                view = inflater.inflate(src, parent, false);
                return new MyViewHolder(view);
            case FOOT_TYPE:
                view = inflater.inflate(item_foot, parent, false);
                return new FootHolder(view, mActivity);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder bodyHolder = (MyViewHolder) holder;
            final Item item = results.get(position);
            bodyHolder.iv_bankLog.setImageResource(item.getImg());//银行图标
            bodyHolder.tv_bankName.setText(item.getBankName()); //银行名称

            final String isPublic = item.getIsPublic();   //是否对公账户0不是 1是        银行卡类型 如：储蓄卡
            if (Define.SAVINGS_CARD.equals(isPublic)) {
                bodyHolder.tv_cardType.setText("储蓄卡");
            } else if (Define.PUBLIC_ACCOUNT.equals(isPublic)) {

                bodyHolder.tv_cardType.setText("对公账户");
            }


            String cardNo = item.getCardNo();
            if (cardNo != null && !cardNo.isEmpty() && cardNo.length() > 4) {
                String newCardNumber = cardNo.substring(cardNo.length() - 4, cardNo.length());
                bodyHolder.tv_cardCode.setText(newCardNumber); //银行卡尾数
            }
            bodyHolder.tv_custname.setText(item.getCustname());
            ; //开户人姓名

            bodyHolder.tv_unbundling.setText("解约");
            bodyHolder.tv_unbundling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("提示！")
                            .setMessage("是否解约该卡")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    terminateAnAgreement(item, isPublic);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.create().show();
                }
            });
            String everyLimit = item.getEveryLimit();
            if (everyLimit != null && !everyLimit.isEmpty()) {
                bodyHolder.tv_everyLimit.setText("￥" + item.getEveryLimit());
            } else {
                bodyHolder.tv_everyLimit.setText("不限额");
            }
            String dailyLimit = item.getDailyLimit();
            if (dailyLimit != null && !dailyLimit.isEmpty()) {
                bodyHolder.tv_dailyLimit.setText("￥" + item.getDailyLimit());
            } else {
                bodyHolder.tv_dailyLimit.setText("不限额");
            }

        } else if (holder instanceof FootHolder) {
            final FootHolder footHolder = (FootHolder) holder;
            footHolder.tv_close_consolidated_withholding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(MyApplication.mContext, "关闭汇缴代扣", Toast.LENGTH_SHORT).show();
                    popWindowInit(mActivity, view);

                }
            });
            footHolder.ll_add_card_withholding_signing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(MyApplication.mContext, "新增银行卡代扣签约", Toast.LENGTH_SHORT).show();
                    List<ConfigureBean.ListEntity> entityList = (List<ConfigureBean.ListEntity>) ObjectSaveUtil.readObject(mActivity, "ListEntity"); //配置集合
                    int deductionsMaxCount = 0;
                    if (entityList != null) {
                        for (ConfigureBean.ListEntity entity : entityList) {
                            if (entity.getDeductionsMaxCount() != 0) {
                                deductionsMaxCount = entity.getDeductionsMaxCount();
                                break;
                            }
                        }
                    }
                    Log.i("entityList", "results.size()==" + results.size() + "getDeductionsMaxCount=" + entityList.get(0).getCash_min_amount());
                    if (results.size() >= deductionsMaxCount) {
                        myPopWindow = new MyPopAbstract() {
                            @Override
                            protected void setMapSettingViewWidget(View view) {
                                TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                                TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
                                TextView tv_pop_phone = (TextView) view.findViewById(R.id.tv_pop_phone);
                                LinearLayout ll_cancel = (LinearLayout) view.findViewById(R.id.ll_cancel);
                                View view_line = (View) view.findViewById(R.id.view_line);

                                ll_cancel.setVisibility(View.GONE);
                                view_line.setVisibility(View.GONE);
                                tv_pop_phone.setText("提示：您签约的银行卡数已达上限，如需签约新的银行卡可以先解约一张已签约的银行卡，再进行添加。");
                                tv_confirm.setText("确认");


                                tv_confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myPopWindow.dissmiss();

                                    }
                                });
                                tv_cancle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myPopWindow.dissmiss();
                                    }
                                });
                            }

                            @Override
                            protected int layout() {
                                return R.layout.unbound_pop;
                            }
                        }.popWindowTouch(mActivity).showAtLocation(view, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        String url = Define.URL + "acct/getUnDeductions";
                        JSONObject jsonObject = new JSONObject();

                        mActivity.requestNet(url, jsonObject, "getUnDeductions", "", true, true);
                    }


                }
            });
        }
    }

    public void terminateAnAgreement(Item item, String isPublic) {
        //                    Toast.makeText(MyApplication.mContext, "点击了解绑", Toast.LENGTH_SHORT).show();
        if (Define.SAVINGS_CARD.equals(isPublic)) {
            /**
             * 判断是中信银行卡就直接解约成功
             */
//            if (Define.CNCB.equals(item.getBankCode())) {//中信银行
//                String url = Define.URL + "acct/terminationComfirm";
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("deductionId", item.getCardId());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                mActivity.requestNet(url, jsonObject, "terminateAnAgreement", "", true, true);
//            } else {
                intent = new Intent(MyApplication.mContext, OpWithHoldThreeA.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("signState", "surrender");
                intent.putExtra("cardId", item.getCardId());
                intent.putExtra("cardName", item.getBankName());
                intent.putExtra("cardNumber", item.getCardNo());
                intent.putExtra("cardType", item.getCardType());
                MyApplication.mContext.startActivity(intent);
//            }
        } else if (Define.PUBLIC_ACCOUNT.equals(isPublic)) {
            intent = new Intent(MyApplication.mContext, CancellationNoticeA.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.mContext.startActivity(intent);

        }
    }

    @Override
    public int getItemCount() {
        return getBodyCount() + getFootCount();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition == results.size() || toPosition == results.size()) {
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(results, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(results, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        results.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_bankLog; //银行图标
        public TextView tv_bankName; //银行名称
        public TextView tv_cardType; //银行卡类型 如：储蓄卡
        public TextView tv_cardCode; //银行卡尾数
        public TextView tv_custname; //开户人姓名
        public TextView tv_unbundling; //解绑
        public TextView tv_everyLimit; //单笔限额
        public TextView tv_dailyLimit; //单日限额

        public MyViewHolder(View itemView) {
            super(itemView);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            itemView.setLayoutParams(layoutParams);
            iv_bankLog = (ImageView) itemView.findViewById(R.id.iv_bankLog);
            tv_bankName = (TextView) itemView.findViewById(R.id.tv_bankName);
            tv_cardType = (TextView) itemView.findViewById(R.id.tv_cardType);
            tv_cardCode = (TextView) itemView.findViewById(R.id.tv_cardCode);
            tv_custname = (TextView) itemView.findViewById(R.id.tv_custname);
            tv_unbundling = (TextView) itemView.findViewById(R.id.tv_unbundling);
            tv_everyLimit = (TextView) itemView.findViewById(R.id.tv_everyLimit);
            tv_dailyLimit = (TextView) itemView.findViewById(R.id.tv_dailyLimit);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = -1;
        if (position > getBodyCount() - 1) {
            //这里最后要减去1，慢慢地去数
            viewType = FOOT_TYPE;
        } else {
            viewType = BODY_TYPE;
        }

        return viewType;
    }


    private int getFootCount() {
        return mFootList.size();
    }

    private int getBodyCount() {
        return results.size();
    }

    /**
     * 脚布局Holder
     */
    public static class FootHolder extends RecyclerView.ViewHolder {

        private TextView tv_close_consolidated_withholding;
        private LinearLayout ll_add_card_withholding_signing;

        public FootHolder(final View itemView, final BaseActivity mActivity) {
            super(itemView);
            tv_close_consolidated_withholding = (TextView) itemView.findViewById(R.id.tv_close_consolidated_withholding); //关闭汇缴代扣
            ll_add_card_withholding_signing = (LinearLayout) itemView.findViewById(R.id.ll_add_card_withholding_signing); // 新增银行卡代扣签约


        }


    }

    private void popWindowInit(final BaseActivity mActivity, View view) {
        if (myPopWindow != null && myPopWindow.isShow()) {
            myPopWindow.dissmiss();
        }
        boolean isPublicFlag = false;
        for (int i = 0; i < results.size(); i++) {
            Item item = results.get(i);
            if (item.getIsPublic().contains("1")) {
                isPublicFlag = true;
                break;
            }
        }
        final boolean finalIsPublicFlag = isPublicFlag;
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
                TextView tv_pop_phone = (TextView) view.findViewById(R.id.tv_pop_phone);
                LinearLayout ll_cancel = (LinearLayout) view.findViewById(R.id.ll_cancel);
                View view_line = (View) view.findViewById(R.id.view_line);
                if (finalIsPublicFlag) {
                    ll_cancel.setVisibility(View.GONE);
                    view_line.setVisibility(View.GONE);
                    tv_pop_phone.setText("提示：您已签约对公账户代扣业务，如需关闭汇缴代扣服务，请联系客服处理。");
                    tv_confirm.setText("确认");
                } else {
                    ll_cancel.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    tv_pop_phone.setText("提示：关闭后，您需要特别留意账户信息，及时缴款；否则逾期未按时完成汇缴将产生滞纳金，给您带来不必要的损失。");
                    tv_confirm.setText("确认关闭");
                    tv_cancle.setText("暂不关闭");

                }

                tv_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalIsPublicFlag) {
                            myPopWindow.dissmiss();
                        } else {
                            myPopWindow.dissmiss();
                            String url = Define.URL + "acct/switchDeductions";
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("isDeductions", "2");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mActivity.requestNet(url, jsonObject, "switchDeductionsRequest", "", true, true);
                            /*intent = new Intent(MyApplication.mContext, OpWithHoldTwoA.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("cardState", "closeState");
                            intent.putExtras(bundle);//发送数据
                            MyApplication.mContext.startActivity(intent);//启动intent*/
                        }
                    }
                });
                tv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.unbound_pop;
            }
        }.popWindowTouch(mActivity).showAtLocation(view, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


}

