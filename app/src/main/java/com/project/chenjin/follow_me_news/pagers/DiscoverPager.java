package com.project.chenjin.follow_me_news.pagers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.project.chenjin.follow_me_news.baseclass.BasePager;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/29   16:21.
 */

public class DiscoverPager extends BasePager {
    public DiscoverPager(Context context) {
        super(context);

    }

    @Override
    public void initData() {
        super.initData();
       //1.设置标题
        tv_title.setText("Discover");
        //2.联网请求得到数据，创建视图
        TextView textView = new TextView(context);

        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("Discover内容");
    }
}
