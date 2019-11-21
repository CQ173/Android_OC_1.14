package com.huoniao.oc.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2017/6/15.
 */

public class NetStateUtils {
        /**
         * 没有连接网络
         */
        private static final int NETWORK_NONE = -1;
        /**
         * 移动网络
         */
        private static final int NETWORK_MOBILE = 0;
        /**
         * 无线网络
         */
        private static final int NETWORK_WIFI = 1;
        /**
         * 2G网络
         */
        public static final int NETWORK_2G = 2;
        /**
         * 3G网络
         */
        public static final int NETWORK_3G = 3;
        /**
         * 4G网络
         */
        public static final int NETWORK_4G = 4;
        /**
         * 未知
         */
        public static final int NETWORK_UNKNOW = -2;
        /**
         * 定义电话管理器对象
         */
        public static TelephonyManager mTelephonyManager;
        /**
         * 定义连接管理器对象
         */
        public static ConnectivityManager mConnectivityManager;
        /**
         * 定义网络信息对象
         */
        public static NetworkInfo mNetworkInfo;

        /**
         * 得到网络类型
         *
         * @param context
         * @return 网络类型
         */
        public static int getNetWrokState(Context context) {
            // 得到连接管理器对象
            mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

            if (mNetworkInfo != null && mNetworkInfo.isConnected()) {

                if (mNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                    return NETWORK_WIFI;
                } else if (mNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                    return getMobileNetType(context);
                }
            }
            return NETWORK_NONE;
        }

        /**
         * 判断是否连接上
         *
         * @return
         */
        public static final boolean isNetConnected() {
            if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                return true;
            }
            return false;
        }

        /**
         * 获取移动网络的类型
         *
         * @param context
         * @return 移动网络类型
         */
        public static final int getMobileNetType(Context context) {

            mTelephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();

            return getNetworkClass(networkType);
        }

        /**
         * 判断移动网络的类型
         *
         * @param networkType
         * @return 移动网络类型
         */
        private static final int getNetworkClass(int networkType) {
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NETWORK_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NETWORK_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return NETWORK_4G;
                default:
                    return NETWORK_UNKNOW;
            }
        }
    }
