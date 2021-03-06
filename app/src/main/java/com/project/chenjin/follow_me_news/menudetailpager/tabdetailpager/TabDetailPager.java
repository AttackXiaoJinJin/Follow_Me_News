package com.project.chenjin.follow_me_news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.follow_me_news_library.refreshlistview.RefreshListview;
import com.google.gson.Gson;
import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.activity.NewsDetailActivity;
import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;
import com.project.chenjin.follow_me_news.domain.HomePagerBean;
import com.project.chenjin.follow_me_news.domain.TabDetailPagerBean;
import com.project.chenjin.follow_me_news.until.CacheUntil;
import com.project.chenjin.follow_me_news.until.Constant;
import com.project.chenjin.follow_me_news.userdefined.HorizontalScrollViewPager;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/2   23:15.
 * 页签详情页面
 */

public class TabDetailPager extends MenuDetailBasePager{
    public static final String READ_ARRAY_ID = "read_array_id";
    private final HomePagerBean.DataBean.ChildrenBean childBean;
    //private TextView textView;
    private String url;
    //顶部轮播图部分数据
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    //新闻列表数据集合
    private List<TabDetailPagerBean.DataBean.NewsBean> news;

    private TabDetailPagerListAdapter tabDetailPagerListAdapter;
    private ImageOptions imageOptions;

    private HorizontalScrollViewPager tabdetail_viewpager;
    private TextView tabdetail_tv_title;
    private LinearLayout point_group_tabdetail;
    private RefreshListview list_item_tabdetail;
    //下一页的联网路径
    private String moreUrl;
    //是否加载更多
    private boolean isLoadMOre = false;
    private MyInnerHandler innerHandler;

    private boolean isDragging = false;


    public TabDetailPager(Context context, HomePagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childBean = childrenBean;
        //手写系统识别不了
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }



    @Override
    public View initView() {
           /* textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(25);
        return textView;*/
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        list_item_tabdetail = (RefreshListview) view.findViewById(R.id.list_item_tabdetail);

        View topNewsView = View.inflate(context, R.layout.topnews,null);
        //以后顶部和listview要分开，用xUtil会报错,故不用
        tabdetail_viewpager = (HorizontalScrollViewPager)topNewsView.findViewById(R.id.tabdetail_viewpager);
        tabdetail_tv_title = (TextView)topNewsView.findViewById(R.id.tabdetail_tv_title);
        point_group_tabdetail = (LinearLayout)topNewsView.findViewById(R.id.point_group_tabdetail);
        //把顶部轮播图部分视图以头的方式添加到Listview中
       // list_item_tabdetail.addHeaderView(topNewsView);
        list_item_tabdetail.addTopNewsView(topNewsView);
        //设置监听下拉刷新
        list_item_tabdetail.setOnRefreshListener(new MyOnRefreshListener());
        //设置listView的item的点击监听
        list_item_tabdetail.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }
    class MyOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              int realPosition = position-1;
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(realPosition);
            //1.取出保存的id集合
            String idArray = CacheUntil.getString(context, READ_ARRAY_ID);
            //2.判断是否存在，若不存在则保存，并且刷新适配器
            if(!idArray.contains(newsBean.getId()+"")){
                CacheUntil.putString(context, READ_ARRAY_ID, idArray+newsBean.getId()+", ");
                //刷新适配器
                //getCount-->getView
                tabDetailPagerListAdapter.notifyDataSetChanged();

            }
            //跳转到新闻浏览页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url", Constant.BASE_URL + newsBean.getUrl());
            context.startActivity(intent);

        }
    }

    class MyOnRefreshListener implements RefreshListview.OnRefreshListener{

        @Override
        public void onPullDownRefresh() {
             getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            if(TextUtils.isEmpty(moreUrl)) {
                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                list_item_tabdetail.onRefreshFinish(false);
            }else {
                getMoreDataFromNet();
            }
        }
    }

    private void getMoreDataFromNet() {
        RequestParams requestParams = new RequestParams(moreUrl);
        requestParams.setConnectTimeout(6000);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("加载更多联网成功 ++"+result);
                list_item_tabdetail.onRefreshFinish(false);
                //为true,才去解析数据.要放在前面
                isLoadMOre = true;
                //解析数据
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("加载更多联网失败 ++" +ex.getMessage());
                list_item_tabdetail.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
       // textView.setText(childBean.getTitle());
        url = Constant.BASE_URL + childBean.getUrl();
        //取缓存数据
        String saveJson = CacheUntil.getString(context, url);
        if(!TextUtils.isEmpty(saveJson)){
            try {
                //解析数据
                processData(saveJson);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        LogUtil.e("标题：" + childBean.getTitle() + "地址：" + url);
        //联网请求
        getDataFromNet();
    }
    //之前点的位置
    private  int prePosition;

    private void processData(String saveJson) {
        TabDetailPagerBean tabDetailPagerBean = parsedJson(saveJson);
        moreUrl= "" ;
        if(TextUtils.isEmpty(tabDetailPagerBean.getData().getMore())){
            moreUrl = "";
        }else{
            moreUrl = Constant.BASE_URL + tabDetailPagerBean.getData().getMore() ;
        }
        //默认和加载更多
        if(!isLoadMOre){
            //默认
            // tabDetailPagerBean.getData().getNews().get(0).getTitle();
            //顶部轮播图数据
            topnews = tabDetailPagerBean.getData().getTopnews();
            //设置ViewPager的适配器
            tabdetail_viewpager.setAdapter(new TabDetailPagerTopNewsAdapter());
            //添加红点
            addPonit();

            //监听页面改变，设置红点的变化
            tabdetail_viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
            tabdetail_tv_title.setText(topnews.get(prePosition).getTitle());
            //准备listview对应的集合数据
            news = tabDetailPagerBean.getData().getNews();
            //设置listview的适配器
            tabDetailPagerListAdapter = new TabDetailPagerListAdapter();
            list_item_tabdetail.setAdapter(tabDetailPagerListAdapter);
        }else{
            //加载更多
            isLoadMOre =false;
            tabDetailPagerBean.getData().getNews();
            List<TabDetailPagerBean.DataBean.NewsBean> moreNews = tabDetailPagerBean.getData().getNews();
            //添加到原来的集合中
            news.addAll(moreNews);
            //刷新适配器
            tabDetailPagerListAdapter.notifyDataSetChanged();

        }
        //发消息每隔4000ms切换一次viewPager页面
        if(innerHandler == null) {
            innerHandler = new MyInnerHandler();
        }
        //把消息队列所有的消息和回调移除
        innerHandler.removeCallbacksAndMessages(null);
        innerHandler.postDelayed(new MyRunnable(),3000);

    }
    class MyInnerHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //切换viewpager的下一个页面
            int item = (tabdetail_viewpager.getCurrentItem()+1)%(topnews.size());
            tabdetail_viewpager.setCurrentItem(item);
            innerHandler.postDelayed(new MyRunnable(),3000);
        }
    }
    //runnable不是子线程，而是可以发任务，只是一个方法
    class MyRunnable implements Runnable{

        @Override
        public void run() {
            innerHandler.sendEmptyMessage(0);
        }
    }


    class TabDetailPagerListAdapter extends BaseAdapter{

        public TabDetailPagerListAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(context, R.layout.item_tabdetail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.ic_icon = (ImageView)convertView.findViewById(R.id.ic_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
                //不设置这个则无法显示
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder =(ViewHolder)convertView.getTag();
            }
            //根据位置得到数据
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            String imageUrl = Constant.BASE_URL + newsBean.getListimage();
            //请求图片 xUntil
            //x.image().bind(viewHolder.ic_icon, imageUrl, imageOptions);
            //使用glide
            Glide.with(context).load(imageUrl).placeholder(R.drawable.news_pic_default).
            diskCacheStrategy(DiskCacheStrategy.ALL).
                    error(R.drawable.news_pic_default).into(viewHolder.ic_icon);

            //设置标题
            viewHolder.tv_title.setText(newsBean.getTitle());
            //设置更新时间
            viewHolder.tv_time.setText(newsBean.getPubdate());
           //设置点击的颜色
            String idArray2 =CacheUntil.getString(context, READ_ARRAY_ID);
            if(idArray2.contains(newsBean.getId()+"")){
                //设置灰色
                viewHolder.tv_title.setTextColor(Color.GRAY);
            }else {
                //设置黑色
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView ic_icon;
        TextView tv_title;
        TextView tv_time;

    }

    private void addPonit() {
        //移除之前的红点
        point_group_tabdetail.removeAllViews();
        for(int i=0;i<topnews.size();i++){
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.point_selector);
            //红点的屏幕适配
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(8),DensityUtil.dip2px(8));
            if(i==0){
                imageView.setEnabled(true);
            }
            else {
                imageView.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(8);
            }

            imageView.setLayoutParams(params);
            point_group_tabdetail.addView(imageView);
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        public MyOnPageChangeListener() {
            super();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //1.设置文本
            tabdetail_tv_title.setText(topnews.get(position).getTitle());
            //2.对应页面的点高亮-红
            //2.1把之前的变灰
            point_group_tabdetail.getChildAt(prePosition).setEnabled(false);
            //2.2把当前的变红
            point_group_tabdetail.getChildAt(position).setEnabled(true);
            //不写这步，则值无变化
            prePosition = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //拖拽
            if(state == ViewPager.SCROLL_STATE_DRAGGING){
                //拖拽要移除消息
                isDragging =true;
                innerHandler.removeCallbacksAndMessages(null);

            }//惯性
            else if(state == ViewPager.SCROLL_STATE_SETTLING && isDragging){
                //发消息
                isDragging = false;
                innerHandler.removeCallbacksAndMessages(null);
                innerHandler.postDelayed(new MyRunnable(), 3000);
            }//禁止
            else if(state == ViewPager.SCROLL_STATE_IDLE && isDragging){
                //发消息
                isDragging = false;
                innerHandler.removeCallbacksAndMessages(null);
                innerHandler.postDelayed(new MyRunnable(), 3000);
            }
        }
    }

    class TabDetailPagerTopNewsAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            //设置图片默认背景
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
           //拉伸图片
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            TabDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
            String imageUrl = Constant.BASE_URL + topnewsBean.getTopimage();
            //联网请求图片
            x.image().bind(imageView, imageUrl);
            //闪退是图片过大,这么改图片模糊
            //x.image().bind(imageView, imageUrl,imageOptions);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //触摸事件返回true,既有触摸又有点击返回false
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN://按下
                            //把消息队列所有的消息和回调移除
                            innerHandler.removeCallbacksAndMessages(null);

                            break;
                        case MotionEvent.ACTION_UP://松开
                            innerHandler.removeCallbacksAndMessages(null);
                            innerHandler.postDelayed(new MyRunnable(),3000);
                            break;
                        /*case MotionEvent.ACTION_CANCEL://取消
                            innerHandler.removeCallbacksAndMessages(null);
                            innerHandler.postDelayed(new MyRunnable(),3000);
                            break;*/
                    }
                    return true;
                }
            });


            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private TabDetailPagerBean parsedJson(String saveJson) {
        return new Gson().fromJson(saveJson, TabDetailPagerBean.class);
    }

    private void getDataFromNet() {
        prePosition = 0;
        RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(6000);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网成功"+childBean.getTitle() + result);
                //缓存数据
                CacheUntil.putString(context, url, result);
                //解析和处理显示数据
                processData(result);
                //隐藏下拉刷新控件,并更新时间.数据
                list_item_tabdetail.onRefreshFinish(true);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败"+childBean.getTitle() + ex.getMessage());
                Toast.makeText(context, "数据更新失败", Toast.LENGTH_SHORT).show();
                //隐藏下拉刷新控件，不更新时间
                list_item_tabdetail.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
