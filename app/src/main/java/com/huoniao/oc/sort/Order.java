package com.huoniao.oc.sort;

import com.huoniao.oc.bean.CarouseImgBean;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/6/23.
 */

public class Order implements Comparator<CarouseImgBean> {

    @Override
    public int compare(CarouseImgBean lhs, CarouseImgBean rhs) {

        return lhs.getLevel() - rhs.getLevel();
    }
}
