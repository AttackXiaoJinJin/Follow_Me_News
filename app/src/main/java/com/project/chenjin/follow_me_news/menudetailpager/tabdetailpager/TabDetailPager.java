package com.project.chenjin.follow_me_news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.project.chenjin.follow_me_news.R;
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
    private ListView list_item_tabdetail;



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
        list_item_tabdetail = (ListView)view.findViewById(R.id.list_item_tabdetail);

        View topNewsView = View.inflate(context, R.layout.topnews,null);
        //以后顶部和listview要分开，用xUtil会报错,故不用
        tabdetail_viewpager = (HorizontalScrollViewPager)topNewsView.findViewById(R.id.tabdetail_viewpager);
        tabdetail_tv_title = (TextView)topNewsView.findViewById(R.id.tabdetail_tv_title);
        point_group_tabdetail = (LinearLayout)topNewsView.findViewById(R.id.point_group_tabdetail);
        //把顶部轮播图部分视图以头的方式添加到Listview中
        list_item_tabdetail.addHeaderView(topNewsView);
        return view;
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
            Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.ic_icon);

            //设置标题
            viewHolder.tv_title.setText(newsBean.getTitle());
            //设置更新时间
            viewHolder.tv_time.setText(newsBean.getPubdate());

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
        RequestParams requestParams = new RequestParams(url);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网成功"+childBean.getTitle() + result);
                //缓存数据
                CacheUntil.putString(context, url, result);
                //解析和处理显示数据
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败"+childBean.getTitle() + ex.getMessage());
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
