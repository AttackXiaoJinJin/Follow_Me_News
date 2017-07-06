package com.project.chenjin.follow_me_news.menudetailpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.project.chenjin.follow_me_news.MainActivity;
import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;
import com.project.chenjin.follow_me_news.domain.HomePagerBean;
import com.project.chenjin.follow_me_news.menudetailpager.tabdetailpager.TopicTabDetailPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/1   22:48.
 * 专题详情页面
 */

public class TopicDetailPager extends MenuDetailBasePager{
   // @ViewInject(R.id.topicTabPageIndicator)
   // private TabPageIndicator topicTabPageIndicator;
    @ViewInject(R.id.topicTabLayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.topicmenu_viewpager)
    private ViewPager topicViewPager;

    @ViewInject(R.id.ic_topic_tab_next)
    private ImageButton ic_topic_tab_next;
    //页签页面数据的集合
    private List<HomePagerBean.DataBean.ChildrenBean> childrenBeanList;
    //页签页面的集合
    private ArrayList<TopicTabDetailPager> topicTabDetailPagers;

    public TopicDetailPager(Context context, HomePagerBean.DataBean dataBean) {
        super(context);
        childrenBeanList = dataBean.getChildren();

    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topicmenu_detail_pager,null);
        x.view().inject(TopicDetailPager.this, view);
        //设置图片点击事件
        ic_topic_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicViewPager.setCurrentItem(topicViewPager.getCurrentItem()+1);
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //准备专题详情页面的数据
        topicTabDetailPagers = new ArrayList<>();
        for(int i=0; i<childrenBeanList.size(); i++){
            topicTabDetailPagers.add(new TopicTabDetailPager(context, childrenBeanList.get(i)));
        }

        //设置适配器
        topicViewPager.setAdapter(new MyTopicTabDetailPagerAdapter());
        //监听器indicator一定在适配器之后
       // topicTabPageIndicator.setViewPager(topicViewPager);
        tabLayout.setupWithViewPager(topicViewPager);
        //用TabPageIndicator监听页面的变化
       // topicTabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
        topicViewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置滑动或者固定
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
         tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position == 0){
                //可以滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }
            else {
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //根据参数设置第0个页签可以侧滑菜单，其他不行
    private void isEnableSlidingMenu(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

    class MyTopicTabDetailPagerAdapter extends PagerAdapter {



        @Override
        public int getCount() {
            return topicTabDetailPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopicTabDetailPager topicTabDetailPager = topicTabDetailPagers.get(position);
            View rootView = topicTabDetailPager.rootView;
            //初始化数据
            topicTabDetailPager.initData();
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
