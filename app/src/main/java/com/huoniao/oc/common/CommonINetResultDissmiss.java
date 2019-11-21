package com.huoniao.oc.common;

import com.huoniao.oc.idal.INetResultDissmissListener;

/**
 *  回调类
 */

public class CommonINetResultDissmiss {
    public INetResultDissmissListener iNetResultDissmissListener;
    public void setiNetResultDissmissListener(INetResultDissmissListener iNetResultDissmissListener){
        this.iNetResultDissmissListener = iNetResultDissmissListener;
    }
}
