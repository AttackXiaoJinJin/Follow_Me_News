package com.project.chenjin.follow_me_news.until;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/7   0:42.
 * 网络缓存图片
 */

public class NetCacheUtil {
    //请求图片成功
    public static final int SUCCESS = 1;

    private final Handler handler;
    public static final int FAIL = 2;
    //线程池服务类
    private ExecutorService service;
    //本地缓存工具类
    private final LocalCacheUtil localCacheUtil;

    public NetCacheUtil(Handler handler, LocalCacheUtil localCacheUtil) {
        this.handler = handler;
        service = Executors.newFixedThreadPool(10);
        this.localCacheUtil = localCacheUtil;
    }

    //联网请求得到图片
    public void getBitmapFromNet(String imageUrl, int position) {
       // new Thread(new MyRunnable(imageUrl, position)).start();

        service.execute(new MyRunnable(imageUrl, position));

    }

    class MyRunnable implements Runnable{
        private final String imageUrl;
        private final int position;

        //构造方法
        public MyRunnable(String imageUrl, int position) {
             this.imageUrl = imageUrl;
            this.position = position;

        }

        @Override
        public void run() {
            //子线程，请求网络图片
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
                //GET必须大写
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setReadTimeout(3000);
                //可写可不写
                httpURLConnection.connect();
                //得到响应码
                int code = httpURLConnection.getResponseCode();
                if(code == 200){
                    //得到输入流
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //联网请求得到图片
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    //显示到控件上,发消息把bitmap和position发出去
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    msg.arg1 = position ;
                    msg.obj = bitmap ;
                    handler.sendMessage(msg);
                    //在内存中缓存一份

                    //在本地缓存一份
                    localCacheUtil.putBitmap(imageUrl, bitmap);

                }

            } catch (IOException e) {
                e.printStackTrace();
                //失败的处理
                Message msg = Message.obtain();
                msg.what = FAIL;
                msg.arg1 = position ;

                handler.sendMessage(msg);

            }

        }
    }


}
