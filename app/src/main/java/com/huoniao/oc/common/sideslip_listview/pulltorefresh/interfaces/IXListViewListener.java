package com.huoniao.oc.common.sideslip_listview.pulltorefresh.interfaces;

/**
 * implements this interface to get refresh/load more event.
 */
public interface IXListViewListener {
    public void onRefresh();

    public void onLoadMore();
}