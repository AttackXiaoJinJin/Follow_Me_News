package com.project.chenjin.follow_me_news.pagers;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.project.chenjin.follow_me_news.MainActivity;
import com.project.chenjin.follow_me_news.baseclass.BasePager;
import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;
import com.project.chenjin.follow_me_news.domain.HomePagerBean;
import com.project.chenjin.follow_me_news.domain.HomePagerBean2;
import com.project.chenjin.follow_me_news.fragment.LeftMenuFragment;
import com.project.chenjin.follow_me_news.menudetailpager.InteractDetailPager;
import com.project.chenjin.follow_me_news.menudetailpager.NewsDetailPager;
import com.project.chenjin.follow_me_news.menudetailpager.PhotosDetailPager;
import com.project.chenjin.follow_me_news.menudetailpager.TopicDetailPager;
import com.project.chenjin.follow_me_news.until.CacheUntil;
import com.project.chenjin.follow_me_news.until.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/29   15:28.
 */

public class HomePager extends BasePager{
    //左侧菜单对应的数据集合
    private List<HomePagerBean.DataBean> slidingdata;
    //private List<HomePagerBean2.DetailPagerData> slidingdata;
    //详情页面的集合
    private ArrayList<MenuDetailBasePager> detailBasePagers;


    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        ic_menu.setVisibility(View.VISIBLE);
        //1.设置标题
        /*tv_title.setText("HomePager");
        //2.联网请求得到数据，创建视图
        TextView textView = new TextView(context);

        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("Home内容");*/
        //获取缓存数据
        String saveJson = CacheUntil.getString(context,Constant.NEWS_URL);

        if(!TextUtils.isEmpty(saveJson)){
            try {
                processData(saveJson);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        //联网请求数据
       getDataFromInternet();



    }

    //使用xUtils联网请求数据
    private void getDataFromInternet() {
        RequestParams requestParams = new RequestParams(Constant.NEWS_URL);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("成功 " + result);
                //缓存数据
                CacheUntil.putString(context, Constant.NEWS_URL, result);

                //适配器
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("出错 " + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //解析json数据并显示
    private void processData(String json) {


        MainActivity mainActivity = (MainActivity)context;
        //得到左侧菜单
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        HomePagerBean homePagerBean = parsedJson(json);
        //HomePagerBean homePagerBean2 = parsedJson2(json);
        //给左侧菜单传递数据
        slidingdata = homePagerBean.getData();


        //添加详情页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsDetailPager(context,slidingdata.get(0)));
        detailBasePagers.add(new TopicDetailPager(context,slidingdata.get(0)));
        detailBasePagers.add(new PhotosDetailPager(context));
        detailBasePagers.add(new InteractDetailPager(context));

        //把数据传递给左侧菜单
        leftMenuFragment.setData(slidingdata);

    }

    //使用Android系统自带的API解析json数据
    private HomePagerBean2 parsedJson2(String json2){
        HomePagerBean2 bean2 = new HomePagerBean2();
        try {
            JSONObject jsonObject = new JSONObject(json2);

            //若用Int则retcode字段不存在时会报错，使用optInt不会报错，没有内容
            int retcode = jsonObject.optInt("retcode");
            //retcode字段解析成功
            bean2.setRetcode(retcode);
            JSONArray data = jsonObject.optJSONArray("data");
            //判空
            if(data != null && data.length() >0){
                List<HomePagerBean2.DetailPagerData> detailPagerDatas = new ArrayList<>();
                //设置列表数据
                bean2.setData(detailPagerDatas);
                for(int i=0; i<data.length(); i++){
                    JSONObject jsonObject1=(JSONObject) data.get(i);

                    HomePagerBean2.DetailPagerData detailPagerData = new HomePagerBean2.DetailPagerData();
                    //添加到集合中
                    detailPagerDatas.add(detailPagerData);

                    int id = jsonObject1.optInt("id");
                    detailPagerData.setId(id);
                    int type = jsonObject1.optInt("type");
                    detailPagerData.setType(type);
                    String title = jsonObject1.optString("title");
                    detailPagerData.setTitle(title);
                    String url = jsonObject1.optString("url");
                    detailPagerData.setUrl(url);
                    String url1 = jsonObject1.optString("url1");
                    detailPagerData.setUrl1(url1);
                    String dayurl = jsonObject1.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);
                    String excurl = jsonObject1.optString("excurl");
                    detailPagerData.setExcurl(excurl);
                    String weekurl = jsonObject1.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);

                    JSONArray children = jsonObject1.optJSONArray("children");
                    if(children !=null && children.length() >0){
                        List<HomePagerBean2.DetailPagerData.ChildrenData> childrenDatas = new ArrayList<>();

                        //设置集合ChildrenData
                        detailPagerData.setChildren(childrenDatas);

                        for(int j=0;j<children.length();j++){
                         JSONObject childrenItem =(JSONObject)children.get(j);
                         //写成A.B ab = new A.B();会报错
                         HomePagerBean2.DetailPagerData detailPagerData1 = new HomePagerBean2.DetailPagerData();
                         HomePagerBean2.DetailPagerData.ChildrenData childrenData =detailPagerData1.new ChildrenData();



                         //添加到集合中
                         childrenDatas.add(childrenData);

                         int childId = childrenItem.optInt("id");
                            childrenData.setId(childId);
                         String childTitle = childrenItem.optString("title");
                            childrenData.setTitle(title);
                         String childUrl = childrenItem.optString("url");
                            childrenData.setUrl(url);
                         int childType = childrenItem.optInt("type");
                            childrenData.setType(type);
                        }
                    }

                }

            }





        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return bean2;
    }


    //解析json数据,1,使用系统api解析json 2.使用第三方框架解析json数据，如GSON,fastjson
    //private HomePagerBean2 parsedJson(String json)
    private HomePagerBean parsedJson(String json) {
        Gson gson = new Gson();
        HomePagerBean homePagerBean = gson.fromJson(json, HomePagerBean.class);
        //HomePagerBean2 homePagerBean2 = gson.fromJson(json, HomePagerBean2.class);
        return homePagerBean;
    }

    //切换详情页面
    public void switchPager(int position) {
       //1.设置标题
        tv_title.setText(slidingdata.get(position).getTitle());
        //2.移除之前的内容(视图)
        fl_content.removeAllViews();
        //3.添加新内容
        //这两行注释，则投票可以点击
       MenuDetailBasePager detailBasePager = detailBasePagers.get(position);
        View rtView =detailBasePager.rootView;
        //初始化数据
        detailBasePager.initData();
        //try {
            fl_content.addView(rtView);
       // }
       /* catch (Exception e)
        {
            e.printStackTrace();
        }*/

    }
}
