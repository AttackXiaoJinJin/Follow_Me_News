package com.follow_me_news_library.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.icu.text.SimpleDateFormat;
import java.util.Date;
/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/4   22:01.
 */

public class RefreshListview extends ListView {
    //包括下拉刷新和顶部轮播图
    private LinearLayout headerView;

    private View push_down_refresh;
    private ImageView ic_arrow;
    private ProgressBar pb_status;
    private TextView tv_title_refresh;
    private TextView tv_time_refresh;
    //下拉刷新控件的高
    private int refreshHeight;
    //加载更多的控件
    private View footView;
    //高
    private int footerViewHeight;
    //下拉刷新
    public static final int PULL_DOWN_REFRESH = 0;
    //手松刷新
    public static final int RELEASE_REFRESH = 1;
    //正在刷新
    public static final int REFRESHING = 2;
    //当前状态
    private int currentStatus = PULL_DOWN_REFRESH;
    //是否已经加载更多
    private boolean isLoadMore = false;
    private View topNewsView;
    //listview在y轴上的坐标
    private int listViewOnScreenY = -1;

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
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footView = View.inflate(context, R.layout.refresh_footer,null);
        footView.measure(0,0);
        footerViewHeight = footView.getMeasuredHeight();
        footView.setPadding(0,-footerViewHeight,0,0);
        //listview添加footer
        addFooterView(footView);
        //监听listview的滚动
        setOnScrollListener(new MyOnScrollListener());

    }
    //添加顶部轮播图
    public void addTopNewsView(View topNewsView) {
        if(topNewsView != null) {
            this.topNewsView = topNewsView;
            headerView.addView(topNewsView);
        }
        else {

        }
    }

    class MyOnScrollListener implements OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当静止或者惯性滚动，
            if(scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING){
                // 且是最后一条可见的

                if(getLastVisiblePosition() >= getCount()-1){
                    //1.显示加载更多布局
                    footView.setPadding(8,8,8,8);
                    //2.状态改变
                    isLoadMore = true;
                    //3.回调接口
                    if(mOnRefreshListener != null){
                        mOnRefreshListener.onLoadMore();
                    }
                }
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
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

                //判断顶部轮播图是否完全显示，只有完全显示才会有下拉刷新
                boolean isDisplayTopNews = isDisplayTopNews();
                if(!isDisplayTopNews){
                    //加载更多
                    break;

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
                    //如果客户不想设置下拉刷新，则mOnRefreshListener为空，再调用会崩溃
                    if(mOnRefreshListener != null){
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
            default:break;
        }
        return super.onTouchEvent(ev);
    }
    //判断是否完全显示顶部轮播图
    //当listview在屏幕上的Y轴坐标小于或等于顶部轮播图在Y轴的坐标时，顶部轮播图完全显示
    private boolean isDisplayTopNews() {
        if(topNewsView != null){
            //1.得到listview在屏幕上的坐标
            int[] location = new int[2];
            if(listViewOnScreenY == -1) {
                getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }
            //2.得到顶部轮播图在屏幕上的坐标
            topNewsView.getLocationOnScreen(location);
            int topNewsViewOnScreenY = location[1];
      /*  if(listViewOnScreenY <= topNewsViewOnScreenY){
            return true;
        }else {
            return false;
        }*/
            return listViewOnScreenY <= topNewsViewOnScreenY;
        }else{
            return true;
        }

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
    //当联网成功和失败的时候回调该方法
    //用于刷新状态的还原
    public void onRefreshFinish(boolean success) {
        if(isLoadMore){
            //加载更多
            isLoadMore = false;
            //隐藏加载更多的布局
            footView.setPadding(0, -footerViewHeight, 0, 0);
        }else {
            //下拉刷新
            tv_title_refresh.setText("下拉刷新...");
            currentStatus = PULL_DOWN_REFRESH;
            ic_arrow.clearAnimation();
            pb_status.setVisibility(GONE);
            ic_arrow.setVisibility(VISIBLE);
            //隐藏下拉刷新控件
            push_down_refresh.setPadding(0, -refreshHeight, 0 ,0);
            if(success){
                //设置最新的更新时间
                tv_time_refresh.setText("上次更新时间: "+ getSystemTime());
            }

        }
    }

    //得到当前安卓系统的时间
    private String getSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());

    }

    //监听控件de刷新
    public interface OnRefreshListener{
        //当下拉刷新的时候回调这个方法
        public void onPullDownRefresh();
        //当加载更多的时候回调该方法
        public void onLoadMore();
    }
    private OnRefreshListener mOnRefreshListener;

    //设置监听刷新,由外界设置
    public void setOnRefreshListener(OnRefreshListener l){
        this.mOnRefreshListener = l;
    }



}

