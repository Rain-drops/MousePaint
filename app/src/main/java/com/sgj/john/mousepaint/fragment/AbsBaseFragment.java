package com.sgj.john.mousepaint.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sgj.john.mousepaint.R;

/**
 *
 */
public abstract class AbsBaseFragment extends Fragment {

    protected Handler mHandler = new Handler();

    protected abstract void initActions(View paramView);
    protected abstract void initData();
    protected abstract View initView(LayoutInflater parpmLayoutInflater);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View localView = initView(inflater);
        initActions(localView);
        return localView;
    }

}
