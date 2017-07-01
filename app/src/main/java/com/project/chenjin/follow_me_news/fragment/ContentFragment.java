package com.project.chenjin.follow_me_news.fragment;


import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.project.chenjin.follow_me_news.MainActivity;
import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.adapter.ContentFragmentAdapter;
import com.project.chenjin.follow_me_news.baseclass.BaseFragment;
import com.project.chenjin.follow_me_news.baseclass.BasePager;
import com.project.chenjin.follow_me_news.pagers.DiscoverPager;
import com.project.chenjin.follow_me_news.pagers.HomePager;
import com.project.chenjin.follow_me_news.pagers.LetterPager;
import com.project.chenjin.follow_me_news.pagers.MessagePager;
import com.project.chenjin.follow_me_news.pagers.MinePager;
import com.project.chenjin.follow_me_news.userdefined.NoScrollViewPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/5/26   23:04.
 */

public class ContentFragment extends BaseFragment{

    //2.使用xUntils3来初始化控件
    @ViewInject(R.id.content_viewpager)
    //private ViewPager viewPager;
    private NoScrollViewPager viewPager;
    @ViewInject(R.id.radiogroup)
    private RadioGroup radioGroup;

    //5个页面集合
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.content_fragment, null);
        //viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        //radioGroup = (RadioGroup)view.findViewById(R.id.radiogroup);

        //1.把视图注入到框架中，让ContentFragment.this和view关联起来
        x.view().inject(ContentFragment.this,view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //初始化5个页面，并放入集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));
        basePagers.add(new DiscoverPager(context));
        basePagers.add(new LetterPager(context));
        basePagers.add(new MessagePager(context));
        basePagers.add(new MinePager(context));

        //设置viewPager的适配器
       viewPager.setAdapter(new ContentFragmentAdapter(basePagers));
       //设置按钮的监听
        radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //监听某个页面被选中，初始化对应的页面的数据
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        //默认首页
        radioGroup.check(R.id.rb_one);
        //默认初始化主页界面数据
        basePagers.get(0).initData();
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch(checkedId){
                //首页
                case R.id.rb_one:viewPager.setCurrentItem(0,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                //发现
                case R.id.rb_two:viewPager.setCurrentItem(1,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                //写信
                case R.id.rb_three:viewPager.setCurrentItem(2,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                //消息
                case R.id.rb_four:viewPager.setCurrentItem(3,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                //我的
                case R.id.rb_five:viewPager.setCurrentItem(4,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    //根据参数设置是否侧滑菜单
    private void isEnableSlidingMenu(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }


}
