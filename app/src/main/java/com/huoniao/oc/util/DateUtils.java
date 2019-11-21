package com.huoniao.oc.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public DateUtils() {
    }

    public static DateUtils getInstance() {
        return dateUtils;
    }

    /**
     * 将日期型转换为字符型.转换后的日期格式为yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public String formatString(Date date) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    private String format(Date date, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    private Date parse(String date, String format)
            throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.parse(date);
    }

    /**
     * 得到当前日期字符串
     *
     * @return
     */
    public String getToday() {
        String result = "";
        Date date = new Date();
        result = formatString(date);
        return result;
    }

    /**
     * 获得当前时间.显示格式如：2008-07-18
     *
     * @return
     */
    public static  String getTime() {
        String result = "";
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        result = fmt.format(date);
        return result;
    }
    /**
     * 获得当前时间.显示格式如：2008-07-18
     *
     * @return
     */
    public static  String getTime11() {
        String result = "";
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM");
        result = fmt.format(date);
        return result;
    }

    /**
     * 获得当前时间.显示格式如：2008-07-18
     *
     * @return
     */
    public static  String getTime12() {
        String result = "";
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
        result = fmt.format(date);
        return result;
    }
    /**
     * 获得当前时间.显示格式如：2008-07-18
     *
     * @return
     */
    public static  String getTime2() {
        String result = "";
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
        result = fmt.format(date);
        return result;
    }

    /**
     * 获得当前时间.显示格式如：2008-07-18
     *
     * @return
     */
    public static  String getTime21() {
        String result = "";
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy年MM月");
        result = fmt.format(date);
        return result;
    }

    /**
     * 获得当前时间.显示格式如：2008-07-18
     *
     * @return
     */
    public static  String getTime22() {
        String result = "";
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy年");
        result = fmt.format(date);
        return result;
    }

    /**
     * 获得当月第一天
     *
     * @return
     */
    public static String getFirstdayofThisMonth()  {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date date = cal.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        result = fmt.format(date);
        return result;
    }


    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dft.format(endDate);
    }




    /**
     * 获得当前时间.显示格式如：20080826102109一般用于作为新建文件夹或者文件名的一部分
     *
     * @return
     */
    public String getTimeNoSeparate() {
        String result = "";
        Date date = new Date();
        result = format(date, "yyyyMMddHHmmss");
        return result;
    }

    /**
     * 得到当前日期,格式如:20080825
     *
     * @return
     */
    public String getNow() {
        String result = "";
        Date date = new Date();
        Calendar.getInstance(Locale.getDefault());
        int year = 1;
        Calendar.getInstance(Locale.getDefault());
        int month = 2;
        Calendar.getInstance(Locale.getDefault());
        int day = 5;
        Calendar.getInstance(Locale.getDefault());
        int week = 7;
        result = format(date, "yyyyMMdd");
        return result;
    }

    /**
     * 将字符串转换成Date型如:2008-08-25 to Wed Aug 24 00:00:00 CST 2005
     *
     * @param str
     * @return
     */
    public  Date formatDate(String str) {
        Date result = null;
        try {
            str = str + " ";
            int endStr = str.indexOf(" ");
            String dateString = str.substring(0, endStr);
            result = parse(dateString, "yyyy-MM-dd");
        } catch (Exception ex) {
        }
        return result;
    }

    private static final DateUtils dateUtils = new DateUtils();


    /**
     * 将字符串数据转化为毫秒数
     *
     * @param dateTime 传的格式
     * @throws ParseException
     */
    public static long dataFormentHaoMiao(String dateTime) throws ParseException {
        Calendar c = Calendar.getInstance();
        try {
            // 2017-05-16 09:31:18
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));


            System.out.println("时间转化后的毫秒数为：" + c.getTimeInMillis());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return c.getTimeInMillis();
    }



    public static String dataFormentHaoMiaos(String dateTime)  {
        Calendar c = Calendar.getInstance();
        try {
            // 2017-05-16 09:31:18
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));


            System.out.println("时间转化后的毫秒数为：" + c.getTimeInMillis());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return c.getTimeInMillis()+"";
    }

    /**
     *
     * @param sstime   格式 1339033320000  废弃
     * @return
     */
    public static String haoMiaoForment(String sstime) {
        try {
            /**  * 将毫秒数转化为时间
             */
            Date date = new Date(sstime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("毫秒数转化后的时间为：" + sdf.format(date));
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * * 将毫秒数转化为时间
     */
    public static String getFormatedDateTime(long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sDateFormat.format(new Date(dateTime + 0));
    }




    //获取系统时间

    public static long getSystemDataHaoMiao(){
      return System.currentTimeMillis();
    }

    //获取网络时间
    public static long getWebNetDatetime(){
        final long[] ld = {0};
        ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.baidu.com");// 取得资源对象
                    URLConnection uc = url.openConnection();// 生成连接对象
                    uc.connect();// 发出连接
                    ld[0] = uc.getDate();// 读取网站日期时间

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        return  ld[0] ;
    }

    //获取网络时间
    public static long getNetDataHaoHiao(){
        URL url = null;//取得资源对象
        long lg =0;
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            lg = uc.getDate(); //取得网站日期时间

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lg;
    }

  public   static String nowTime=null;
    //获取网络时间
    public static void getNetDate(){

        ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                URL url = null;//取得资源对象
                long lg =0;
                try {
                    url = new URL("http://www.baidu.com");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    lg = uc.getDate(); //取得网站日期时间
                    Date date=new Date(lg);
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                    nowTime=format.format(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


}