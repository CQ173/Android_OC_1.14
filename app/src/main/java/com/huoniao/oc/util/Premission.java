package com.huoniao.oc.util;

/**
 * Created by Administrator on 2017/6/5.
 */

public class Premission {
    public static  final String SYS_USER_INDEX ="sys:user:index" ;  //显示"首页"菜单权限
    public static  final String SYS_USER_INFO = "sys:user:info" ;   //显示"个人信息"菜单权限
    public static  final String SYS_USER_MODIFYPWD = "sys:user:modifyPwd" ;//显示"修改密码"菜单权限
    public static  final String FB_FEEDBACK_ADD = "fb:feedback:add";  //	显示"我要反馈"菜单权限
    public static  final String SYS_SUBUSER_VIEW = "sys:subUser:view"; //显示"子账号管理"菜单权限
    public static  final String SYS_SUBUSER_EDIT ="sys:subUser:edit" ; //添加,修改,删除子账号权限
    public static  final String ACCT_TRADEFLOW_MYWALLET = "acct:tradeFlow:myWallet";//	显示"我的钱包"菜单权限
    public static  final String ACCT_USERCARD_VIEW ="acct:userCard:view";      // acct:userCard:view	显示"银行卡"菜单权限
    public static  final String ACCT_USERCARD_EDIT="acct:userCard:edit";	 //添加,修改,删除银卡权限
    public static  final String ACCT_TRADEFLOW_VIEW="acct:tradeFlow:view";//	显示"交易流水"菜单权限
    public static  final String  FB_PAYMENT_VIEW = "fb:payment:view";	//显示"汇缴数据"菜单权限
    public static  final String FB_PAYMENT_IMPORT="fb:payment:import";// 	导入汇缴数据权限
    public static  final String FB_PAYMENT_EXPORT = "fb:payment:export";//	汇缴数据导出权限
    public static  final String FB_PAYMENT_UNUSUALLIST = "fb:payment:unusualList";//	显示"异常汇缴"菜单权限
    public static  final String FB_PAYMENT_UNUSUALEXPORT = "fb:payment:unusualExport";// 	异常汇缴数据导出权限
    public static  final String FB_PAYMENT_SUMLIST="fb:payment:sumList";//	显示"汇缴汇总"菜单
    public static  final String FB_PAYMENT_SUMEXPORT = "fb:payment:sumExport";//	汇缴汇总数据导出权限
    public static  final String FB_AGENCY_VIEW = "fb:agency:view";//	显示"代点售"菜单权限
    public static  final String FB_AGENCY_EDIT = "fb:agency:edit";//	代售点关联,删除关联权限
    public static  final String FB_INFO_VIEW = "fb:info:view";//	显示"消息记录"菜单权限
    public static  final String FB_INFO_TAG  ="fb:info:view" ;//	标记消息为已读的权限
    public static  final String FB_INFO_EXPORT = "fb:info:export";//	消息导出权限
    public static  final String FB_INFO_DELETE = "fb:info:delete";//	消息删除权限
    public static  final String FB_FEEDBACK_VIEW = "fb:feedback:view";//	显示"反馈信息"菜单权限(主要管理员用到)

    public static  final String FB_FEEDBACK_CHECK = "fb:feedback:check";//	反馈信息处理权限
    public static  final String FB_NOTICE_VIEW = "fb:notice:view";//	显示"通知列表"菜单权限
    public static  final String FB_NOTICE_DELETE = "fb:notice:delete";//	通知删除权限
    public static  final String FB_NOTICE_SAVE = "fb:notice:save";//	显示"通知添加"菜单权限
    public static  final String FB_USERLOG_VIEW  = "fb:userLog:view";//	显示"我的日志"菜单权限
    public static  final String SYS_LOG_VIEW = "sys:log:view";//	显示"操作日志"菜单权限
    public static  final String FB_QUESTCATEG_VIEW = "fb:questCateg:view";//	显示"问答类型列表"菜单权限
    public static  final String FB_QUESTCATEG_EDIT = "fb:questCateg:edit";//	添加,修改,删除问签类型权限
    public static  final String FB_QUESTION_VIEW = "fb:question:view" ; //	显示"问答列表"菜单权限
    public static  final String FB_QUESTION_EDIT = "fb:question:edit";//	添加,修改,删除问答权限
    public static  final String PARTNER_PARTNERMALL_VIEW = "partner:partnerMall:view";//	显示伙伴商城"首页"菜单权限
    public static  final String ACCT_ACCOUNT_EDIT = "acct:account:edit";//	显示"新增对账"菜单权限
    public static  final String ACCT_ACCOUNT_VIEW = "acct:account:view" ;//	显示"对账数据"菜单权限
    public static  final String ACCT_ACCOUNT_IMPORT = "acct:account:import" ;//	对账数据导入权限
    public static  final String ACCT_ACCOUNT_EXPORT = "acct:account:export";//	对账数据导出权限
    public static  final String ACCT_FINANCEAPPLY_VIEW = "acct:financeApply:view";// 	财务明细
    public static final String ACCT_FINANCEAPPLY_FORM="acct:financeApply:form"; //财务申请
    public static  final String ACCT_FINANCEAPPLY_ACCOUNTING = "acct:financeApply:accounting";//	会计
    public static  final String ACCT_FINANCEAPPLY_CASHIER = "acct:financeApply:cashier";// 出纳相关权限(转账,财务申请添加, 导入)
   public static  final String SYS_USER_VIEW ="sys:user:view";//	显示"用户管理"菜单权限
   public static  final String SYS_USER_EDIT = "sys:user:edit";//	添加,修改,删除用户权限
  // public static  final String SYS_USER_AUDIT	="sys:user:audit";//审核用户权限
   public static  final String SYS_OFFICE_VIEW	 = "sys:office:view";//显示"机构管理"菜单权限
   public static  final String SYS_OFFICE_EDIT ="sys:office:edit";//	添加,修改,删除机构权限
   public static  final String SYS_AREA_VIEW = "sys:area:view" ;//	显示"区域管理"菜单权限
   public static  final String SYS_AREA_EDIT = "sys:area:edit";//	添加,修改,删除区域权限
   public static  final String SYS_MENU_VIEW = "sys:menu:view";//	显示"菜单管理"菜单权限
   public static  final String SYS_MENU_EDIT = "sys:menu:edit";//	添加,修改,删除菜单权限
   public static  final String SYS_ROLE_VIEW = "sys:role:view";//	显示"角色管理"菜单权限
   public static  final String SYS_ROLE_EDIT = "sys:role:edit";//	添加,修改,删除角色权限
   public static  final String SYS_DICT_VIEW ="sys:dict:view";//	 显示"字典管理"菜单权限
   public static  final String SYS_DICT_EDIT  = "sys:dict:edit";//添加,修改,删除字典权限
   public static  final String SYS_KEYSTORE_VIEW = "sys:keyStore:view" ;//	显示"密钥管理"菜单权限
   public static  final String SYS_KEYSTORE_EDIT = "sys:keyStore:edit";//	添加,修改,删除秘钥权限
   public static  final String FB_PAYMENTPLAN_VIEW = "fb:paymentPlan:view";//	显示"扣款任务"菜单权限
   public static  final String FB_PAYMENTPLAN_CHECK = "fb:paymentPlan:check";//	扣款计划审核权限
   public static final String FB_AGENCYCONNECT_VIEW = "fb:agencyConnect:view"; //窗口号挂靠查看
   public static final String FB_AGENCYCONNECT_SAVE = "fb:agencyConnect:save"; //窗口号挂靠申请权限   //窗口挂靠保存权限
    public static final String   SYS_USER_AUDIT ="sys:user:audit";  //用户审核
    public static final String  FB_AGENCYCONNECT_AUDIT = "fb:agencyConnect:audit"; //  窗口挂靠审核
    public static final  String FB_PAYMENTWARN_VIEW="fb:paymentWarn:view"; //汇缴预警
   public static final String ACCT_TRADEFLOW_EPOSSYNC ="acct:tradeFlow:eposSync";//eposSync 同步权限
    public static final String 	ACCT_DEDUCTIONS_INDEX =	"acct:deductions:index";//汇缴代扣权限
    public  static final String ACCT_TRADEFLOW_ENCHASHMENT ="acct:tradeFlow:enchashment"; //提现权限
    public  static final String FB_USERCHANGE_CANCELLATION ="fb:userChange:cancellation"; // 用户注销权限
    public  static final String FB_USERCHANGE_CHANGE ="fb:userChange:change"; // 资料变更权限
    public  static final String FB_WECHATPUBLICNOTICE_INDEX ="fb:wechatPublicNotice:index"; // 新消息通知权限
    public  static final String FB_CREDITSCORE_VIEW ="fb:creditScore:view"; // 信用积分权限
    public  static final String ACCT_FINANCEAPPLY_REFUND ="acct:financeApply:refund"; // 财务退款权限
    public  static final String FB_PAYMENT_PAYMENTINFO ="fb:payment:paymentInfo"; // 汇缴导入记录权限
    public  static final String FB_LATEFEERETURN_VIEW ="fb:latefeeReturn:view"; // 滞纳金返还权限
    public  static final String FB_LATEFEERETURN_SAVE ="fb:latefeeReturn:save"; // 滞纳金返还添加递交权限
    public  static final String FB_ALARM_VIEW ="fb:alarm:view"; // 新汇缴预警权限
    public  static final String FB_ALARM_HANDLE  = "fb:alarm:handle";// 新汇缴预警处理内容权限
    public  static final String FB_TRAINPAYMENT_VIEW  = "fb:trainPayment:view";//车站汇缴信息首页显示权限
    public  static final String FB_TRAINPAYMENT_REFUND  =  "fb:trainPayment:refund";//车站汇缴明细退款按钮
    public  static final String FB_LATEFEERETURN_HANDLE  =  "fb:latefeeReturn:handle";//滞纳金返还处理按钮权限




}
