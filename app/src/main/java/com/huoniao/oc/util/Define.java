package com.huoniao.oc.util;

public class Define {
    final static boolean debug = false;

    //正式环境
/*    public static final String HOST_URL = "https://oc.120368.com/";
    public static final String URL = "https://oc.120368.com/app/";
	public static final String IMG_URL = "https://oc.120368.com";//图片路径
	public static final String VERSION_URL = "http://www.120368.com/app/version";
	public static final String UNIONPAY_URL = "https://oc.120368.com/outer/chinapay/chinapayapi.jsp?WIDtotal_fee=";
	public static final String FARM_URL = "http://farm.120368.com/";
	public static final String ECO_PASSWORD = "CLh2&y9oStGp1$lh"; //生态农场密钥
    public static final String WX_APPID = "wxef5baa797f4c1197";//正式环境WX_APPID*/


    public static final String QQ_APPID = "1105896121"; //QQ_APPID

    public static final String WX_APPSECRET = "81637ee380c18cc60d434f4e3a370dd1";

    //帮助中心废弃
//    public static final String HELP_CENTER = "http://oc.120368.com/help/index";

    //测试环境
/*	public static final String URL = "https://ecy.120368.com/app/";
	public static final String IMG_URL = "https://ecy.120368.com";//图片路径-测试
	public static final String HOST_URL = "https://ecy.120368.com/";*/
    public static final String UNIONPAY_URL = "https://ecy.120368.com/outer/chinapay/chinapayapi.jsp?WIDtotal_fee=";
    public static final String VERSION_URL = "https://ecy.120368.com/app/version";//版本更新地址
    public static final String FARM_URL = "http://114.55.119.28/farm/";
    public static final String ECO_PASSWORD = "KshTmHPGpN5q8S6";//生态农场密钥-测试环境
    public static final String WX_APPID = "wxd23c09ce60d632ba";//微信测试环境WX_APPID


//	public static final String URL = "http://ecy.120368.com:8020/app/";//本地地址-开发环境
//	public static final String IMG_URL = "http://ecy.120368.com:8020";//图片路径-测试
//	public static final String HOST_URL = "http://ecy.120368.com:8020/";


    public static final String URL = "http://192.168.0.85:8181/OC/app/";//本地地址-曲
    public static final String IMG_URL = "http://192.168.0.85:8181/OC";//图片路径-曲
    public static final String HOST_URL = "http://192.168.0.85:8181/OC/";

//	public static final String URL = "http://192.168.0.85:8282/OC/app/";//本地地址-曲
//	public static final String IMG_URL = "http://192.168.0.85:8282/OC";//图片路径-曲
//	public static final String HOST_URL = "http://192.168.0.85:8282/OC/";


	/*public static final String URL = "http://192.168.0.93:8181/OC/";//本地地址-晏刚
	public static final String IMG_URL = "http://192.168.0.93:8181/OC";//图片路径-晏刚
	public static final String HOST_URL = "http://192.168.0.93:8181/OC/";*/

	/*public static final String URL = "http://192.168.0.33:8181/OC/app/";//本地地址-江
	public static final String IMG_URL = "http://192.168.0.33:8181/OC";//图片路径-江
	public static final String HOST_URL = "http://192.168.0.33:8181/OC/";*/


/*	public static final String URL = "http://192.168.0.99:8080/OC/app/"; //本地的地址-郑
	public static final String IMG_URL = "http://192.168.0.99:8080/OC";//图片路径--郑
	public static final String VERSION_URL = "http://192.168.0.99:8080/app/version";//版本更新地址--郑*/

//    public static final String URL ="http://192.168.0.24:8080/OC/app/";     // 本地   马
//    public  static final String IMG_URL = "http://192.168.0.24:8080/OC"; //本地  马


    public static final String HELP_CENTER = HOST_URL + "help/index?app=1";
    public static final String LEARN_MORE = HOST_URL + "help/index?app=1&categoryId=7ef3d255d6c04d40877d1a2901b8dcbd";

    public static final String PROTOCAL_URL = HOST_URL + "static/protocol.html";
    public static final String PROTOCAL2_URL = HOST_URL + "static/wtyhdkxy.html";
    public static final String PROTOCAL3_URL = HOST_URL + "static/protocol/html/10.html";//O计汇缴平台服务协议
    public static final String PROTOCAL4_URL = HOST_URL + "static/protocol/html/11.html";//定制项目服务协议（铁路客票汇缴）
    public static final String PROTOCAL5_URL = HOST_URL + "static/protocol/html/12.html";//火鸟客户端软件许可及服务协议
    public static final String PROTOCAL6_URL = HOST_URL + "static/protocol/html/13.html";//火鸟通行证服务协议
    public static final String PROTOCAL7_URL = HOST_URL + "static/protocol/html/14.html";//火鸟隐私权政策
    public static final String PAY_WITHHOLDAGREE = HOST_URL + "static/deductionsProtocol.html";
    public static final String CNCB_PAYMENT_AGREEMENT = HOST_URL + "static/deductionsProtocolECITIC.html";//中信委托规则
    public static final String CREDIT_RULES = HOST_URL + "static/creditRules.html";//信用规则
    public static final String VIEWVIEW_EXPLAIN = HOST_URL + "/static/serviceFeeInstructions.html";//说明

    public static final String LATEFEE_COVERING_LETTER = "default/images/latefeeReturn.jpg";//滞纳金返还说明函

    //用户类型: 1代表系统管理员，2代表机构用户(包括火车站用户和代售点用户)，3代表个人用户
    public static final String SYSTEM_MANAG_USER = "1";
    public static final String ORGANIZATION_USER = "2";
    public static final String INDIVIDUAL_USER = "3";

    //机构类型: 1代表个人，2代表火车站， 3代表代售点
    public static final String PERSONAL = "1";
    public static final String TRAINSTATION = "2";
    public static final String OUTLETS = "3";

    //验证码类型: 1代表注册短信模板， 2代表找回密码， 3代表修改绑定手机， 4代表注册审核通过通知， 5代表注册审核拒绝通知
    public static final String REGISTE_VERCODE = "1";
    public static final String RETRIEVE_PASSWORD_VERCODE = "2";
    public static final String UPTATA_BINDPHONE_VERCODE = "3";
    public static final String REGISTEPASSED_VERCODE = "4";
    public static final String REGISTEREFUSE_VERCODE = "5";

    //
    public static volatile String localCookie = null;

    //代售点状态 状态 0:审核通过 	1:待审核 	2:审核不通过    3: 补充资料待审核
    public static final String OUTLETS_NORMAL = "0";
    public static final String OUTLETS_PENDIG_AUDIT = "1";
    public static final String OUTLETS_NOTPASS = "2";
    public static final String QIANDIANDAI_NOTPASS = "3";
    public static final String ANCHORED_STATE_REMOVE = "4";//解除绑定

    //代售点-汇缴数据-扣款状态(0: 成功1: 待扣款2: 失败)
    public static final String CHARG_SUCCESS = "0";
    public static final String CHARG_WAIT = "1";
    public static final String CHARG_FAIL = "2";
    public static final String CHARG_RECHARGE = "3";//待充值
    public static final String CHARG_RECHARGE_SUCCESS = "4";//补缴成功

    /*扣款状态(0:已完成1:待扣款2:异常3待充值4补缴成功)*/

    //代售点-资金流水-交易状态(TRADE_CLOSED:交易关闭   WAIT_BUYER_PAY:等待付款   TRADE_FINISHED:交易结束   TRADE_SUCCESS:交易成功)
    public static final String TRADE_STATUS1 = "TRADE_SUCCESS";
    public static final String TRADE_STATUS2 = "WAIT_BUYER_PAY";
    public static final String TRADE_STATUS3 = "TRADE_FINISHED";
    public static final String TRADE_STATUS4 = "TRADE_CLOSED";
    public static final String TRADE_STATUS5 = "ENCHASH_SUSPENSE";//等待处理
    public static final String TRADE_STATUS6 = "TRADE_PENDING";//等待收款
    public static final String TRADE_STATUS7 = "TRADE_FAIL";//交易失败

    //代售点-资金流水-交易类型(income:收入    payout:支出)
    public static final String TRADE_TYPE1 = "income";
    public static final String TRADE_TYPE2 = "payout";

    //用户协议类型 1:O计服务协议  2:电子钱包服务协议 3:证券服务协议   4：伙伴商城服务协议   5：借款服务协议
    public static final String O_SERVICE_AGREEMENT = "1";
    public static final String ELECTRONIC_WALLET = "2";
    public static final String PARTNER_MALL = "4";
    public static final String LOAN_AGREEMENT = "5";

    //反馈信息类型:1咨询,2建议,3投诉,4其它
    public static final String SELECT = "0";
    public static final String CONSULTATION = "1";
    public static final String PROPOSAL = "2";
    public static final String COMPLAINT = "3";
    public static final String OTHER = "4";

    //反馈信息处理状态:0待处理,1已处理
    public static final String UNTREATED = "0";
    public static final String ALREADY_PROCESSED = "1";

    //系统通知类型:0一般,1重要
    public static final String COMMONLY_NOTIFICATION = "0";
    public static final String IMPORTANT_NOTIFICATION = "1";

    //银行卡操作类型
    public static final String BANKCARD_SELECT = "1";//查询
    public static final String BANKCARD_ADD = "2";//添加
    public static final String BANKCARD_UPDATA = "3";//修改
    public static final String BANKCARD_DELETE = "4";//删除

    //银行卡类型
    public static final String DEBIT_CARD = "1";//借记卡
    public static final String CREDIT_CARD = "2";//信用卡

    //银行卡类型
    public static final String SAVINGS_CARD = "0"; //储蓄卡（非对公账户）
    public static final String PUBLIC_ACCOUNT = "1";//对公账户

    //银行卡签约状态
    public static final String NOT_SIGNED = "0"; //未签约
    public static final String SIGNED = "1";//已签约

    //窗口号挂靠审核状态
    public static final String AUDIT_STATE_PASS = "0"; //审核通过
    public static final String AUDIT_STATE_WAIT = "1"; //待审核
    public static final String AUDIT_STATE_REFUSE = "2"; //审核不通过
    public static final String AUDIT_STATE_REMOVE = "4"; //解除绑定


    //退款   测试环境
//	public static final String REFUND = "http://192.168.0.152:8080/OC/app/acct/aliRefund";  //临时用
    public static final String ALIREFUND = HOST_URL + "/app/acct/aliRefund";     //支付宝扫码退款微信扫码退款

    public static final String WECHATREFUND = HOST_URL + "app/acct/wechatRefund"; //

    //交易状态  字段名:tradeStatus
    public static final String ACCT_TRADE_STATUS_WAIT = "WAIT_BUYER_PAY";//等待付款      如果超出时间一个小时   交易关闭
    public static final String ACCT_TRADE_STATUS_SUSPENSE = "ENCHASH_SUSPENSE";//等待处理
    public static final String ACCT_TRADE_STATUS_FAIL = "TRADE_FAIL";//交易失败
    public static final String ACCT_TRADE_STATUS_CLOSED = "TRADE_CLOSED";//交易关闭
    public static final String ACCT_TRADE_STATUS_PENDING = "TRADE_PENDING";//等待收款
    public static final String ACCT_TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";//交易成功        如果超过一个月 不能退款  交易关闭
    public static final String ACCT_TRADE_STATUS_FINISHED = "TRADE_FINISHED";//交易结束
    public static final String ACCT_TRADE_STATUS_REFUNDING = "TRADE_REFUNDING";//退款中
    public static final String ACCT_TRADE_STATUS_REFUND = "TRADE_REFUND";//交易已退款
    public static final String ACC_TRADE_STATUS_REFUND_FAIL = "TRADE_REFUND_FAIL";//退款失败"   //新增  TODO 需要处理


    public static final String ACCT_TRADE_CHAN_TYPE_ALI_QR = "ALI_QR_PAY";//支付宝扫码支付
    public static final String ACCT_TRADE_CHAN_TYPE_WE_QR = "WE_QR_PAY";//微信扫码支付
    public static final String ACCT_TRADE_CHAN_TYPE_ALI_XFT = "XFT_ALI_QR_PAY";//星付通支付宝扫码支付
    public static final String ACCT_TRADE_CHAN_TYPE_WE_XFT = "XFT_WE_QR_PAY";//星付通微信扫码支付
    public static final String ACCT_TRADE_CHAN_TYPE_ALI_PAY = "ALI_PAY";//支付宝充值
    public static final String ACCT_TRADE_CHAN_TYPE_WE_PAY = "WE_PAY";//微信充值
    public static final String ACCT_TRADE_CHAN_TYPE_BANK_PAY = "BANK_PAY";//银行充值
    public static final String EPOSE = "EPOSE";//EPOSE充值

    //类型   字段名:type
    public static final String INCOME = "income";//收入
    public static final String PAYOUT = "payout";//支出

    //商城
    public static final String SHOPPING = HOST_URL + "appembed/partnerMall.jsp";

    //查询银行卡信息  管理银行卡接口
    public static final String CRAD = URL + "acct/manageBankCard";

    //获取银行卡
    public static final String Cash = URL + "sys/getDictAry";


    //用户增加银行卡
    public static final String UserCash = URL + "acct/manageBankCard";

    //提现接口  https://ecy.120368.com/app/acct/enchashSave
    public static final String WITHDRAWCASH = URL + "acct/enchashSave";

    //第一代理人
    public static final String AGENT_PERSONAL = "0";//个人
    public static final String AGENT_LIVID = "1";//铁青
    public static final String AGENT_POST = "2";//邮政
    public static final String AGENT_BUSINESS = "3";//企业

    //第一代理人类型
    public static final String agentType_ZHIYIN = "0";//直营
    public static final String agentType_CONTRACT = "1";//承包


    //财务申请类型
    public static final String finApplyType1 = "payment";//汇缴扣款
    public static final String finApplyType2 = "enchash";//提现
    public static final String finApplyType3 = "depositary";//代存
    public static final String finApplyType4 = "helpbuy";//代付
    public static final String finApplyType5 = "deductions";//代扣
    public static final String finApplyType6 = "enchash";//提现
    public static final String finApplyType7 = "paymentPay";//汇缴扣款2


    //财务审核状态
    public static final String finApplyState1 = "1";//待审核
    public static final String finApplyState2 = "2";//审核通过
    public static final String finApplyState3 = "3";//审核不通过
    public static final String finApplyState4 = "4";//待付款
    public static final String finApplyState5 = "5";//转账成功
    public static final String finApplyState6 = "6";//转账失败
    public static final String finApplyState7 = "7";//待确认
    public static final String finApplyState8 = "8";//付款中
    public static final String finApplyState9 = "9";//拒绝付款
    public static final String finApplyState10 = "10";//失败重新付款
    public static final String finApplyState11 = "11";//线下付款
    public static final String finApplyState12 = "12";//待出纳审核

    //是否开通汇缴代扣状态
    public static final String NOT_OPENED = "0";//未开通
    public static final String OPEN = "1";//已开通
    public static final String CLOSE = "2";//手动关闭

    //汇缴预警处理状态
    public static final String WARN_EXCIPTION = "0";//异常
    public static final String WARN_NORMOL = "1";//正常
    public static final String WARN_WAITDO = "2";//代处理

    //滞纳金返还状态
    public static final String NUMBER_ZERO = "0";//递交中
    public static final String NUMBER_ONE = "1";//处理中
    public static final String NUMBER_TWO = "2";//处理成功
    public static final String NUMBER_THREE = "3";//处理失败
    public static final String NUMBER_FOUR = "4";//已完结

    //垫款状态
    public static final String PAYMENT_PAYSTATE_1 = "1";//待垫款
    public static final String PAYMENT_PAYSTATE_2 = "2";//垫款失败
    public static final String PAYMENT_PAYSTATE_3 = "3";//待还款申请
    public static final String PAYMENT_PAYSTATE_4 = "4";//待还款审核
    public static final String PAYMENT_PAYSTATE_5 = "5";//已还款
    public static final String PAYMENT_PAYSTATE_6 = "6";//还款失败
    public static final String PAYMENT_PAYSTATE_7 = "7";//待付款审核
    public static final String PAYMENT_PAYSTATE_8 = "8";//已付款
    public static final String PAYMENT_PAYSTATE_9 = "9";//付款失败
    public static final String PAYMENT_PAYSTATE_10 = "10";//付款中
    public static final String PAYMENT_PAYSTATE_11 = "11";//已退款

    //银行卡类型
    public static final String CNCB = "ECITIC";//中信银行
}

