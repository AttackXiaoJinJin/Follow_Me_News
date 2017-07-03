package com.project.chenjin.follow_me_news.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;
import com.project.chenjin.follow_me_news.domain.HomePagerBean;
import com.project.chenjin.follow_me_news.menudetailpager.tabdetailpager.TabDetailPager;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/1   22:48.
 * 新闻详情页面
 */

public class NewsDetailPager extends MenuDetailBasePager{
    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator tabPageIndicator;

    @ViewInject(R.id.newsmenu_viewpager)
    private ViewPager viewPager;
    //页签页面数据的集合
    private List<HomePagerBean.DataBean.ChildrenBean> childrenBeanList;
    //页签页面的集合
    private ArrayList<TabDetailPager> tabDetailPagers;

    public NewsDetailPager(Context context, HomePagerBean.DataBean dataBean) {
        super(context);
        childrenBeanList = dataBean.getChildren();

    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.newsmenu_detail_pager,null);
        x.view().inject(NewsDetailPager.this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //准备新闻详情页面的数据
        tabDetailPagers = new ArrayList<>();
        for(int i=0; i<childrenBeanList.size(); i++){
            tabDetailPagers.add(new TabDetailPager(context, childrenBeanList.get(i)));
        }

        //设置适配器
        viewPager.setAdapter(new MyNewsDetailPagerAdapter());
        //监听器indicator一定在适配器之后
        tabPageIndicator.setViewPager(viewPager);

    }

    class MyNewsDetailPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            //初始化数据
            tabDetailPager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return childrenBeanList.get(position).getTitle();
        }
    }

}
