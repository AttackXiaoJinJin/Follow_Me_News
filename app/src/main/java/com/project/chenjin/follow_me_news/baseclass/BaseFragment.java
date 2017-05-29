package com.project.chenjin.follow_me_news.baseclass;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/5/26   23:04.
 */

public abstract class BaseFragment extends Fragment{

    public Activity mainActivity;

    //fragment被创建时回调该方法
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=getActivity();

    }


    /**
     * 当视图被创建时回调
     * 让孩子实现自己的视图，并实现自己的效果
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    public abstract View initView() ;


    /**
     * 当activity创建后回调
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    /**
     * 1、如果没有数据，则联网请求数据，并把数据绑定到initView()的视图上
     *
     */
    public void initData() {
    }
}
