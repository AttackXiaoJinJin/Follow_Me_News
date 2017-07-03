package com.project.chenjin.follow_me_news.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/1   22:48.
 * 互动详情页面
 */

public class InteractDetailPager extends MenuDetailBasePager{
    private TextView textView;


    public InteractDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(25);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("设置互动内容");
    }
}