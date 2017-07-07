package com.project.chenjin.follow_me_news.menudetailpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;
import com.project.chenjin.follow_me_news.domain.HomePagerBean;
import com.project.chenjin.follow_me_news.domain.PhotosDetailPagerBean;
import com.project.chenjin.follow_me_news.until.BitmapCacheUtil;
import com.project.chenjin.follow_me_news.until.CacheUntil;
import com.project.chenjin.follow_me_news.until.Constant;
import com.project.chenjin.follow_me_news.until.NetCacheUtil;
import com.project.chenjin.follow_me_news.volley.VolleyManager;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/1   22:48.
 * 互动详情页面
 */

public class InteractDetailPager extends MenuDetailBasePager{
    private final HomePagerBean.DataBean dataBean;
    @ViewInject(R.id.listview_interact)
    private ListView listView_interact;

    @ViewInject(R.id.gridview_interact)
    private GridView gridView_interact;

    private String url;
    private List<PhotosDetailPagerBean.DataBean.NewsBean> news;
    private InteractDetailPagerAdapter adapter;
    //图片三级缓存的类
    private BitmapCacheUtil bitmapCacheUtil;
    //从子线程转到主线程用Handler
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //图片请求成功
                case NetCacheUtil.SUCCESS:
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap)msg.obj;
                    if(listView_interact.isShown()) {
                        //获取图片
                        ImageView imageView = (ImageView) listView_interact.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }

                    if(gridView_interact.isShown()) {
                        //获取图片
                        ImageView imageView =(ImageView) gridView_interact.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }

                    LogUtil.e("请求图片成功++" + position);
                    break;
                //请求失败
                case NetCacheUtil.FAIL:
                    position = msg.arg1;
                    LogUtil.e("请求图片失败++" + position);
                    break;
            }


        }
    };

    public InteractDetailPager(Context context, HomePagerBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
        bitmapCacheUtil = new BitmapCacheUtil(handler);


    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.interact_detail_pager, null);
        org.xutils.x.view().inject(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constant.BASE_URL + dataBean.getUrl();
        String saveJson = CacheUntil.getString(context, url);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        getDataFromInternetByVolley();
    }
    //解析和显示数据
    private void processData(String saveJson) {
        PhotosDetailPagerBean photosDetailPagerBean = parsedJson(saveJson);
        LogUtil.e(photosDetailPagerBean.getData().getNews().get(0).getTitle());

        isShowListView = true;
        //设置适配器
        news = photosDetailPagerBean.getData().getNews();
        adapter = new InteractDetailPagerAdapter();
        listView_interact.setAdapter(adapter);
    }
    //true显示listview,隐藏gridview
    private boolean isShowListView = true;

    public void switchListAndGrid(ImageButton ic_switch_list_grid) {
        if(isShowListView){
            isShowListView = false;
            //显示gridview,隐藏listview
            gridView_interact.setVisibility(View.VISIBLE);
            adapter = new InteractDetailPagerAdapter();
            gridView_interact.setAdapter(adapter);
            //按钮显示成listview
            listView_interact.setVisibility(View.GONE);
            ic_switch_list_grid.setImageResource(R.drawable.icon_pic_list_type);
        }else{
            isShowListView = true;
            listView_interact.setVisibility(View.VISIBLE);
            adapter = new InteractDetailPagerAdapter();
            listView_interact.setAdapter(adapter);
            //按钮显示成listview
            gridView_interact.setVisibility(View.GONE);
            ic_switch_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }

    }

    class InteractDetailPagerAdapter extends BaseAdapter {

        private DisplayImageOptions options;

        //构造方法，ImageLoader
        public InteractDetailPagerAdapter(){
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.home_scroll_default)
                    .showImageForEmptyUri(R.drawable.home_scroll_default)
                    .showImageOnFail(R.drawable.home_scroll_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(20))//设置矩形圆角
                    .build();

        }


       /* public InteractDetailPagerAdapter() {
            super();
        }*/

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
                convertView = View.inflate(context , R.layout.item_photos_detail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.ic_icon_photos = (ImageView) convertView.findViewById(R.id.ic_icon_photos);
                viewHolder.tv_title_photos = (TextView)convertView.findViewById(R.id.tv_title_photos);
                //不能忘记
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到对应的数据
            PhotosDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            viewHolder.tv_title_photos.setText(newsBean.getTitle());
            String imageUrl = Constant.BASE_URL + newsBean.getSmallimage();
            //1.使用volley设置图片
           // loaderImager(viewHolder , imageUrl );
            //2.使用自定义的三级缓存请求图片
            //内存或本地,主线程不可能获得网络的图片
           /* viewHolder.ic_icon_photos.setTag(position);
            Bitmap bitmap = bitmapCacheUtil.getBitmap(imageUrl, position);
            if(bitmap != null){
                viewHolder.ic_icon_photos.setImageBitmap(bitmap);
            }*/
            //使用picasso请求列表图片
           /* Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.ic_icon_photos);*/

            //使用glide加载图片
           /* Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.ic_icon_photos);*/

           //使用ImageLoader加载图片
           com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(imageUrl, viewHolder.ic_icon_photos , options);


            return convertView;
        }
    }

    static class ViewHolder{
        ImageView ic_icon_photos;
        TextView tv_title_photos;
    }

    /**
     *
     * @param viewHolder
     * @param imageurl
     */
    private void loaderImager(final ViewHolder viewHolder, String imageurl) {

        viewHolder.ic_icon_photos.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.ic_icon_photos != null) {
                        if (imageContainer.getBitmap() != null) {
                            //设置图片
                            viewHolder.ic_icon_photos.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            //设置默认图片
                            viewHolder.ic_icon_photos.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.ic_icon_photos.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(imageurl, listener);
    }

    private PhotosDetailPagerBean parsedJson(String saveJson) {
        return new Gson().fromJson(saveJson, PhotosDetailPagerBean.class);
    }

    //使用volley联网请求数据
    private void getDataFromInternetByVolley() {
        //请求队列
        //RequestQueue requestQueue = Volley.newRequestQueue(context);
        //String请求
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                LogUtil.e("volley成功 " + s);
                //缓存数据
                CacheUntil.putString(context, url, s);

                //适配器
                processData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("volley失败 " + volleyError.getMessage());
            }
        }){
            //解决乱码问题

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        //添加到队列中
        //requestQueue.add(stringRequest);
        VolleyManager.getRequestQueue().add(stringRequest);

    }
}
