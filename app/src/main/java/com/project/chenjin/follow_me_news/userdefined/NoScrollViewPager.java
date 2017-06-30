package com.project.chenjin.follow_me_news.userdefined;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/30   18:01.
 * 自定义不可滑动的viewpager
 */

public class NoScrollViewPager extends ViewPager{
    //在代码中实例化时使用该方法
    public NoScrollViewPager(Context context) {
        super(context);
    }

    //在布局文件使用该类的时候，实例化此类用此构造方法，不能少，否则崩溃
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写触摸事件，消耗掉
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
