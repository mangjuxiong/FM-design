package com.example.fm_design.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fm_design.R;
import com.example.fm_design.base.BaseFragment;

public class SubscriptionFragment  extends BaseFragment {
    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView =layoutInflater.inflate(R.layout.fragment_subscription,container,false);
        return rootView;
    }
}
