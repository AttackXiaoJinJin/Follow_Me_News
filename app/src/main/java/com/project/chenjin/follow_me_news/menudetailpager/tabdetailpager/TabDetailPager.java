package com.project.chenjin.follow_me_news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;
import com.project.chenjin.follow_me_news.domain.HomePagerBean;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/2   23:15.
 * 页签详情页面
 */

public class TabDetailPager extends MenuDetailBasePager{
    private final HomePagerBean.DataBean.ChildrenBean childBean;
    private TextView textView;

    public TabDetailPager(Context context, HomePagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childBean = childrenBean;
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
        textView.setText(childBean.getTitle());
    }
}
