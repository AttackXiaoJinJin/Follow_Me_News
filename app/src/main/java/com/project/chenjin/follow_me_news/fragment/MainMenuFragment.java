package com.project.chenjin.follow_me_news.fragment;


import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.project.chenjin.follow_me_news.baseclass.BaseFragment;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/5/26   23:04.
 */

public class MainMenuFragment extends BaseFragment{
    private TextView textView;

    @Override
    public View initView() {
        textView = new TextView(getContext());
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("你妈嗨");


    }


}
