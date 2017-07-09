# 随我新闻application
[![build](https://img.shields.io/badge/build-gradle-green.svg)](https://github.com/AttackXiaoJinJin/Follow_Me_News/blob/master/app/build.gradle)                   [![Android|Tools](https://img.shields.io/badge/android-tools-blue.svg)](http://www.android-studio.org/)
 [![License](https://img.shields.io/badge/license-MIT-red.svg)](https://choosealicense.com/licenses/mit/#)

##### 联系方式
* Email: wwwcenjin1314@163.com

## 功能和特性:
##### GuideActivity<br>
  ![follow1](https://github.com/AttackXiaoJinJin/AndroidExample/blob/master/app/src/main/res/drawable-hdpi/follow1.gif)
  <br>
##### HomePager<br>
![follow4](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow4.PNG)<br>

##### 1.侧滑栏SlidingMenu<br>
  ![follow5](https://github.com/AttackXiaoJinJin/AndroidExample/blob/master/app/src/main/res/drawable-hdpi/follow5.gif) <br>
1.1 初始化和使用<br>
```

        //1.设置主页面<br>
        setContentView(R.layout.activity_main);
        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);
        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);//设置右侧菜单
        //4.设置显示的模式：左侧菜单+主页，左侧菜单+主页面+右侧菜单；主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);
        //5.设置滑动模式：滑动边缘，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //6.设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this,200));
        ......
        ......
```


##### 2.联网请求<br>

2.1 xUntils3<br>
2.1.1在Application中初始化XUntils3<br>
```
x.Ext.setDebug(true);
```<br>
```
x.Ext.init(this);
```<br>

2.1.2 使用xUntils3做联网请求<br>
```

      private void getDataFromInternet() {

        RequestParams requestParams = new RequestParams(Constant.NEWS_URL);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                long endTime = SystemClock.uptimeMillis();
                long passTime = endTime - startTime ;
                LogUtil.e("xUntil3的请求时间 " + passTime);
                LogUtil.e("成功 " + result);
                //缓存数据
                CacheUntil.putString(context, Constant.NEWS_URL, result);
                //适配器
                processData(result);
            }
           ......
           ......

```

2.2使用volley做联网请求<br>
```

       private void getDataFromInternetByVolley() {

        //请求队列
        //RequestQueue requestQueue = Volley.newRequestQueue(context);
        //String请求
        StringRequest  stringRequest = new StringRequest(Request.Method.GET, Constant.NEWS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                long endTime = SystemClock.uptimeMillis();
                long passTime = endTime - startTime ;
                LogUtil.e("volley的请求时间 " + passTime);
                LogUtil.e("volley成功 " + s);
                //缓存数据
                CacheUntil.putString(context, Constant.NEWS_URL, s);
                //适配器
                processData(s);
            }
        }, new Response.ErrorListener() {
        ......
        ......

```<br>

##### 3.使用Gson解析jsons数据<br>

```

       private HomePagerBean parsedJson(String json) {

        Gson gson = new Gson();
        HomePagerBean homePagerBean = gson.fromJson(json, HomePagerBean.class);
        //HomePagerBean2 homePagerBean2 = gson.fromJson(json, HomePagerBean2.class);
        return homePagerBean;
    }
     ......
     ......
```


##### 4.ViewPageIndicator<br>
 ![follow6](https://github.com/AttackXiaoJinJin/AndroidExample/blob/master/app/src/main/res/drawable-hdpi/follow6.gif) 
 <br>
```

       //ViewPager和TabPageIndicator关联
      tabPageIndicator.setViewPager(viewPager);

```


##### 5.数据缓存<br>
![follow7](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow7.gif)<br>

* 缓存文本数据
```

     SharedPreferences sp= context.getSharedPreferences("chenjin" , context.MODE_PRIVATE);
     sp.edit().putString(key, value).commit();
```

* 图片三级缓存<br>

```

       //1.从内存中获取图片
        if(memoryCacheUtil != null){
            Bitmap bitmap = memoryCacheUtil.getBitmapFromUrl(imageUrl);
            if(bitmap != null){
                LogUtil.e("内存图片加载成功++" + position);
                return bitmap;
            }
        }


        //2.从本地中获取图片
        if(localCacheUtil != null){
            Bitmap bitmap = localCacheUtil.getBitmapFromUrl(imageUrl);
            if(bitmap != null){
                LogUtil.e("本地图片加载成功++" + position);
                return bitmap;
            }
        }

        //3.从网络中获取图片
        netCacheUtil.getBitmapFromNet(imageUrl, position);
        return  null;
        ......
        ......
```


##### 6.下拉刷新，上拉加载<br>

6.1 自定义下拉刷新<br>
![follow8](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow8.gif)<br>
```

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
           ......
           ......
```

6.2自定义上拉加载<br>
![follow13](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow13.gif)<br>
```

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
            ......
            ......
```


##### 7.进入详情界面&&设置字体大小<br>
![follow9](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow9.gif)
```

          //跳转到新闻浏览页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url", Constant.BASE_URL + newsBean.getUrl());
            context.startActivity(intent);
          ......
          ......
```

```

        private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = new String[]{"超大字体","大字体","正常字体","小字体","超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize = which;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
```

##### 8.gridview和listview相互切换<br>
![follow10](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow10.gif)<br>
```

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //根据位置得到对应的数据
            PhotosDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            String imageUrl = Constant.BASE_URL + newsBean.getLargeimage();
            Intent intent = new Intent(context, ShowImageActivity.class);
            intent.putExtra("url",imageUrl);
            context.startActivity(intent);


        }
```

##### 9.photoview的使用<br>
![follow11](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow11.gif)<br>
```

      final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        Picasso.with(this)
                .load(url)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                         attacher.update();
                    }

                    @Override
                    public void onError() {

                    }
                });

```

##### 10.Android与H5互调
![follow12](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow12.gif)<br>
![follow14](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow14.gif)<br>
![follow15](https://raw.githubusercontent.com/AttackXiaoJinJin/AndroidExample/master/app/src/main/res/drawable-hdpi/follow15.gif)<br>



## 待解决的问题:
* 图片三级缓存中的本地缓存 失败
* 用drawerlayout替换slidingmenu
* 点击底部小鸡图片使其能跃出RadioButton
* 屏幕适配
  follow2 follow3
* android版本适配
* 分享功能

## 如何建立服务器并使用:
* (windows)在cmd中运行 ipconfig 查看ip地址并在Constant类中修改
* [点这里](https://pan.baidu.com/s/1dFb5Qy1)

## 使用到的第三方库和服务:
* SlidingMenu：
[https://github.com/jfeinstein10/SlidingMenu](https://github.com/jfeinstein10/SlidingMenu)
* xUtils：
[https://github.com/wyouflf/xUtils3](https://github.com/wyouflf/xUtils3)
* gson：[https://github.com/google/gson](https://github.com/google/gson)
* ViewPageIndicator：[https://github.com/JakeWharton/ViewPagerIndicator](https://github.com/JakeWharton/ViewPagerIndicator)
* Android-PullToRefresh：[https://github.com/chrisbanes/Android-PullToRefresh](https://github.com/chrisbanes/Android-PullToRefresh)
* Volley：[https://android.googlesource.com/platform/frameworks/volley](https://android.googlesource.com/platform/frameworks/volley)
* glide：[https://github.com/bumptech/glide](https://github.com/bumptech/glide)
* picasso：[https://github.com/square/picasso](https://github.com/square/picasso)
* 极光推送：[https://www.jiguang.cn/](https://www.jiguang.cn/)
* Android-Universal-Image-Loader：[https://github.com/nostra13/Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader)
* PhotoView：[https://github.com/chrisbanes/PhotoView](https://github.com/chrisbanes/PhotoView)
* okhttp：[https://github.com/square/okhttp](https://github.com/square/okhttp)
* okhttputils：[https://github.com/hongyangAndroid/okhttputils](https://github.com/hongyangAndroid/okhttputils)
