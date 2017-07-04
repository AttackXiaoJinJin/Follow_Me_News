package com.project.chenjin.follow_me_news.userdefined;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/4   10:57.
 * 水平方向滑动的viewpager
 */

public class HorizontalScrollViewPager extends ViewPager{
    public HorizontalScrollViewPager(Context context) {
        super(context);
    }
    //起始坐标
    private float startX;
    private float startY;

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //请求父层视图不拦截当前控件的事件
                //把事件传给当前控件 HorizontalScrollViewPager
                getParent().requestDisallowInterceptTouchEvent(true);
                //1.记录起始坐标
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2.来到新的坐标
                float endX = ev.getX();
                float endY = ev.getY();
                //3.计算偏移量
                float distanceX = endX - startX;
                float distanceY = endY - startY;
                //4.判断滑动方向
                //abs()绝对值
                if(Math.abs(distanceX) > Math.abs(distanceY)){
                    //水平方向滑动
                    //2.1当滑动到viewpager的第0个页面，是从左到右滑动
                    //getParent().requestDisallowInterceptTouchEvent(false);
                    if(getCurrentItem()==0 && distanceX >0)
                    {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //2.2当滑动到viewpager的最后一个页面，是从右到左滑动
                    //getParent().requestDisallowInterceptTouchEvent(false);
                    else if((getCurrentItem()==(getAdapter().getCount()-1))&& (distanceX < 0))
                    {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //2.3其他
                    //getParent().requestDisallowInterceptTouchEvent(true);
                    else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else {
                    //垂直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:break;

        }

        return super.dispatchTouchEvent(ev);
    }


}
