package com.project.chenjin.follow_me_news.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.project.chenjin.follow_me_news.baseclass.BasePager;

import java.util.ArrayList;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/30   0:06.
 */

public class ContentFragmentAdapter  extends PagerAdapter {
    private final ArrayList<BasePager> basePagers;

    public ContentFragmentAdapter(ArrayList<BasePager> basePagers){
        this.basePagers = basePagers;
    }

    @Override
    public int getCount() {
        return basePagers.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //各个页面的实例
        BasePager basePager = basePagers.get(position);
        //各个子页面
        View rootView = basePager.rootView;
        //调用各个页面的initData()
        basePager.initData();
        container.addView(rootView);
        return rootView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
