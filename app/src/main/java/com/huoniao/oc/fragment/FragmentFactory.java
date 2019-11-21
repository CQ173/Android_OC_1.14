package com.huoniao.oc.fragment;


import com.huoniao.oc.fragment.admin.AdminHomepageF;
import com.huoniao.oc.fragment.admin.AdminOjiF;
import com.huoniao.oc.fragment.admin.AdminSubscriptionManagementF;
import com.huoniao.oc.fragment.financial.FinancialHomepageF;
import com.huoniao.oc.fragment.financial.FinancialOjiF;
import com.huoniao.oc.fragment.financial.FinancialUserF;
import com.huoniao.oc.fragment.outlets.OutletsHomepageF;
import com.huoniao.oc.fragment.outlets.OutletsOjiF;
import com.huoniao.oc.fragment.outlets.OutletsShoppingF;
import com.huoniao.oc.fragment.train_station.AdministrationHomepageF;
import com.huoniao.oc.fragment.train_station.AdministrationPaymentF;
import com.huoniao.oc.fragment.train_station.TrainStationHomepageF;
import com.huoniao.oc.fragment.train_station.TrainStationOjiF;

/**
 * fragment工厂
 */

public class FragmentFactory {
    private  BaseFragment baseFragment;
    private TrainStationHomepageF trainStationHomepageF;
    private TrainStationOjiF trainStationOjiF;
    private OutletsHomepageF outletsHomepageF;
    private OutletsOjiF outletsOjiF;
    private OutletsShoppingF outletsShoppingF;
    private AdminHomepageF adminHomepageF;
    private AdminOjiF adminOjiF;
    private AdminSubscriptionManagementF adminSubscriptionManagementF;
    private FinancialHomepageF financialHomepageF;
    private FinancialOjiF financialOjiF;
    private FinancialUserF financialUserF;
    private MineF mineF;
    private AdministrationHomepageF administrationHomepageF;
    private AdministrationPaymentF administrationPaymentF;
    public FragmentFactory(){
        if(baseFragment ==null){
            baseFragment = new BaseFragment();
        }
    }

    public BaseFragment getFragment(String fragmentTag){
        switch (fragmentTag){
            case BaseFragment.TrainStationHomepageF:  //火车站首页
                if(trainStationHomepageF==null)
                    trainStationHomepageF = new TrainStationHomepageF();
                return trainStationHomepageF;

            case BaseFragment.TrainStationOjiF:  //火车站o计界面
                if(trainStationOjiF==null)
                    trainStationOjiF = new TrainStationOjiF();

                return trainStationOjiF;
            case BaseFragment.MineF:  //共用“我的”模块界面
                if(mineF == null)
                   mineF = new MineF();
                return mineF;


            case BaseFragment.OutletsHomepageF:  //代售点首页
                if(outletsHomepageF==null)
                    outletsHomepageF = new OutletsHomepageF();
                return outletsHomepageF;
            case BaseFragment.OutletsOjiF:  //代售点o计
                if(outletsOjiF==null)
                    outletsOjiF = new OutletsOjiF();
                return outletsOjiF;
            case BaseFragment.OutletsShoppingF: //代售点商城
                if(outletsShoppingF==null)
                    outletsShoppingF= new OutletsShoppingF();
                return outletsShoppingF;

            case BaseFragment.AdminHomepageF:  //管理员首页
                if(adminHomepageF==null)
                    adminHomepageF = new AdminHomepageF();
                return adminHomepageF;
            case BaseFragment.AdminOjiF:  // 管理员O计界面
                if(adminOjiF==null)
                    adminOjiF = new AdminOjiF();
                return adminOjiF;
            case BaseFragment.AdminSubscriptionManagementF: //管理员汇缴界面
                if(adminSubscriptionManagementF==null)
                    adminSubscriptionManagementF = new AdminSubscriptionManagementF();
                 return adminSubscriptionManagementF;

            case BaseFragment.FinancialHomepageF:  //会计 首页
                if(financialHomepageF==null)
                    financialHomepageF = new FinancialHomepageF();
                return financialHomepageF;
            case BaseFragment.FinancialOjiF:
               if(financialOjiF==null)
                   financialOjiF = new FinancialOjiF();  //会计o计
                return financialOjiF;

            case BaseFragment.FinancialUserF:  //会计用户
                if(financialUserF==null)
                    financialUserF = new FinancialUserF();
                return financialUserF;
            case "":  //需要复用管理员汇缴界面  //这个case 后面要参数 这里只是标记需要记录一下
                 break;

            case BaseFragment.AdministrationHomepageF:  //铁总、铁分、车务段首页
                if(administrationHomepageF==null)
                    administrationHomepageF = new AdministrationHomepageF();
                return administrationHomepageF;
            case BaseFragment.AdministrationPaymentF:
                if(administrationPaymentF==null)
                    administrationPaymentF = new AdministrationPaymentF();
                return administrationPaymentF;

        }

        return baseFragment;
    }


}
