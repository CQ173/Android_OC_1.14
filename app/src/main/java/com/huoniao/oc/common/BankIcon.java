package com.huoniao.oc.common;

import com.huoniao.oc.R;

/**
 * Created by Administrator on 2017/10/13.
 */

public class BankIcon {
    public static int getBankIcon(String bankCode) {
        if ("BCOM".equals(bankCode)) { //交通银行
            return R.drawable.jiaotong;
        } else if ("BOC".equals(bankCode)) { //中国银行
            return R.drawable.zhongguo_w;
        } else if ("CEB".equals(bankCode)) { //中国光大银行
            return R.drawable.guangdayh;
        } else if ("CMB".equals(bankCode)) { //招商银行
            return R.drawable.zhaoshang_w;
        } else if ("HXB".equals(bankCode)) { //华夏银行
            return R.drawable.huaxiayh;
        } else if ("BOB".equals(bankCode)) { //北京银行
            return R.drawable.beijingyh;
        } else if ("ICBC".equals(bankCode)) { //中国工商银行										 );
            return R.drawable.gongshang_w;
        } else if ("CMBC".equals(bankCode)) { //中国民生银行
            return R.drawable.mingshengyh;
        } else if ("POST".equals(bankCode)) { //中国邮政储蓄银行
            return R.drawable.youzhengyh;
        } else if ("SPDB".equals(bankCode)) { //浦发银行
            return R.drawable.pufa_w;
        } else if ("CCB".equals(bankCode)) {  // 中国建设银行
            return R.drawable.jianshe_w;
        } else if ("NJCB".equals(bankCode)) { //南京银行
            return R.drawable.nanjingyh;
        } else if ("ABC".equals(bankCode)) { //中国农业银行
            return R.drawable.nongyeyh;
        } else if ("ECITIC".equals(bankCode)) { //中信银行
            return R.drawable.zhongxin_w;
        }else if("CGB".equals(bankCode)){  //广发银行
          return R.drawable.guangfayh;
        }else if("CIB".equals(bankCode)){
         return R.drawable.xinyeyh;  //兴业银行
        }else if("PINGAN".equals(bankCode)){
        return R.drawable.pinganyh;  //平安银行
        } else if(bankCode==null){
            return R.drawable.zhongxin_w; //没有返回中信银行
        }
        return R.drawable.zhongxin_w; //没有返回中信银行
    }

//灰色
    public static int getBankGrayIcon(String bankCode) {
        if ("BCOM".equals(bankCode)) { //交通银行
            return R.drawable.jiaotongbank_graylogo;
        } else if ("BOC".equals(bankCode)) { //中国银行
            return R.drawable.zhongxinbank_graylogo;
        } else if ("CEB".equals(bankCode)) { //中国光大银行
            return R.drawable.guangdabank_graylogo;
        } else if ("CMB".equals(bankCode)) { //招商银行
            return R.drawable.zsbank_graylogo;
        } else if ("HXB".equals(bankCode)) { //华夏银行
            return R.drawable.huaxiabank_graylogo;
        } else if ("BOB".equals(bankCode)) { //北京银行
            return R.drawable.beijinbank_graylogo;
        } else if ("ICBC".equals(bankCode)) { //中国工商银行										 );
            return R.drawable.gongshangbank_graylogo;
        } else if ("CMBC".equals(bankCode)) { //中国民生银行
            return R.drawable.minshengbank_graylogo;
        } else if ("POST".equals(bankCode)) { //中国邮政储蓄银行
            return R.drawable.youzhengbank_graylogo;
        } else if ("SPDB".equals(bankCode)) { //浦发银行
            return R.drawable.pufabank_graylogo;
        } else if ("CCB".equals(bankCode)) {  // 中国建设银行
            return R.drawable.jsbank_graylogo;
        } else if ("NJCB".equals(bankCode)) { //南京银行
            return R.drawable.nanjingbank_graylogo;
        } else if ("ABC".equals(bankCode)) { //中国农业银行
            return R.drawable.nongyebank_graylogo;
        } else if ("ECITIC".equals(bankCode)) { //中信银行
            return R.drawable.zhongxinbank_graylogo;
        }else if("CGB".equals(bankCode)){  //广发银行
            return R.drawable.guangfabank_graylogo;
        }else if("CIB".equals(bankCode)){
            return R.drawable.xingyebank_graylogo;  //兴业银行
        }else if("PINGAN".equals(bankCode)){
            return R.drawable.pinganbank_graylogo;  //平安银行
        } else if(bankCode==null){
            return R.drawable.zhongxinbank_graylogo; //没有返回中信银行
        }
        return R.drawable.zhongxinbank_graylogo; //没有返回中信银行
    }
}
