package com.project.chenjin.follow_me_news.userdefined;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.chenjin.follow_me_news.R;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/4   11:49.
 * 自定义下拉刷新的listview
 */

public class RefreshListview extends ListView{
    //包括下拉刷新和顶部轮播图
    private LinearLayout headerView;

    private View push_down_refresh;
    private ImageView ic_arrow;
    private ProgressBar pb_status;
    private TextView tv_title_refresh;
    private TextView tv_time_refresh;
    //下拉刷新控件的高
    private int refreshHeight;
    //下拉刷新
    public static final int PULL_DOWN_REFRESH = 0;
    //手松刷新
    public static final int RELEASE_REFRESH = 1;
    //正在刷新
    public static final int REFRESHING = 2;
   //当前状态
    private int currentStatus = PULL_DOWN_REFRESH;

    public RefreshListview(Context context) {
        this(context,null);
    }

    public RefreshListview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        //初始化动画
        initAnimation();
    }
    private Animation upAnimation;
    private Animation downAnimation;

    private void initAnimation() {
        upAnimation = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                                                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180,-360,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        //注意添加headerView.
        headerView = (LinearLayout)View.inflate(context, R.layout.refresh_header,null);
        push_down_refresh = headerView.findViewById(R.id.push_down_refresh);
        ic_arrow = (ImageView)headerView.findViewById(R.id.ic_arrow);
        pb_status = (ProgressBar)headerView.findViewById(R.id.pb_status);
        tv_title_refresh = (TextView)headerView.findViewById(R.id.tv_title_refresh);
        tv_time_refresh = (TextView)headerView.findViewById(R.id.tv_time_refresh);
        //测量
        push_down_refresh.measure(0,0);
        refreshHeight = push_down_refresh.getMeasuredHeight();
        //默认隐藏下拉刷新控件
        //完全隐藏
        push_down_refresh.setPadding(0, -refreshHeight, 0,0);

        //添加listview头
        RefreshListview.this.addHeaderView(headerView);
    }
    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //1.记录起始坐标
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(startY == -1) {
                    startY = ev.getY();
                }
                //如果是正在刷新，则不让再刷新了
                if(currentStatus == REFRESHING){
                    break;
                }
                //2.来到新的坐标
                float endY = ev.getY();
                //3.记录滑动的距离
                float distanceY = endY -startY;
                if(distanceY >0) {
                    //下拉
                    int paddingTop = (int)(-refreshHeight + distanceY);
                    if(paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH){
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshViewState();
                    }
                    else if(paddingTop > 0 && currentStatus != RELEASE_REFRESH){
                        //手松刷新状态
                        currentStatus = RELEASE_REFRESH;
                        //更新状态 
                        refreshViewState();

                    }
                    push_down_refresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if(currentStatus == PULL_DOWN_REFRESH){
                     push_down_refresh.setPadding(0, - refreshHeight,0,0);
                }
                else if(currentStatus == RELEASE_REFRESH){
                    //设置状态为正在刷新
                    currentStatus = REFRESHING;
                    refreshViewState();
                    push_down_refresh.setPadding(0,0,0,0);

                    //回调接口
                }
                break;
            default:break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshViewState() {
        switch (currentStatus){
            case PULL_DOWN_REFRESH:
                //下拉刷新状态
                ic_arrow.startAnimation(downAnimation);
                tv_title_refresh.setText("下拉刷新...");
                break;
            case RELEASE_REFRESH:
                //手松刷新状态
                ic_arrow.startAnimation(upAnimation);
                tv_title_refresh.setText("手松刷新...");
                break;
            case REFRESHING:
                //正在刷新状态
                tv_title_refresh.setText("正在刷新...");
                pb_status.setVisibility(VISIBLE);
                ic_arrow.clearAnimation();
                ic_arrow.setVisibility(GONE);
                break;
        }
    }
}
