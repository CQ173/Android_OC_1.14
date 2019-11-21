package com.huoniao.oc.fragment.train_station;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huoniao.oc.R;
import com.huoniao.oc.fragment.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainStationOjiF extends BaseFragment {


    public TrainStationOjiF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
