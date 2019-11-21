package com.huoniao.oc.common.sideslip_listview.swipemenu.interfaces;


import com.huoniao.oc.common.sideslip_listview.swipemenu.bean.SwipeMenu;
import com.huoniao.oc.common.sideslip_listview.swipemenu.view.SwipeMenuView;

public interface OnSwipeItemClickListener {
    void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
}