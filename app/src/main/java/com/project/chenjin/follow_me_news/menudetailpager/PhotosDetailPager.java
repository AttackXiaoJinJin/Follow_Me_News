package com.project.chenjin.follow_me_news.menudetailpager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;
import com.project.chenjin.follow_me_news.domain.HomePagerBean;
import com.project.chenjin.follow_me_news.domain.PhotosDetailPagerBean;
import com.project.chenjin.follow_me_news.until.CacheUntil;
import com.project.chenjin.follow_me_news.until.Constant;
import com.project.chenjin.follow_me_news.volley.VolleyManager;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/1   22:48.
 * 图组详情页面
 */

public class PhotosDetailPager extends MenuDetailBasePager{
    private final HomePagerBean.DataBean dataBean;
    @ViewInject(R.id.listview_photos)
    private ListView listView_photos;

    @ViewInject(R.id.gridview_photos)
    private GridView gridView_photos;

    private String url;
    private List<PhotosDetailPagerBean.DataBean.NewsBean> news;
    private PhotosDetailPagerAdapter adapter;

    public PhotosDetailPager(Context context, HomePagerBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_detail_pager, null);
        x.view().inject(this, view);

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

        //设置适配器
        news = photosDetailPagerBean.getData().getNews();
        adapter = new PhotosDetailPagerAdapter();
        listView_photos.setAdapter(adapter);
    }

    class PhotosDetailPagerAdapter extends BaseAdapter{

        public PhotosDetailPagerAdapter() {
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
            //s使用volley设置图片
            loaderImager(viewHolder , imageUrl );

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
