package com.project.chenjin.follow_me_news.until;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/7   0:23.
 * 图片三级缓存工具类
 */

public class BitmapCacheUtil {
    //网路缓存工具类（网络请求）
    private NetCacheUtil netCacheUtil;

    public BitmapCacheUtil(Handler handler) {
       netCacheUtil =new NetCacheUtil(handler);
    }


    public Bitmap getBitmap(String imageUrl, int position) {
        //1.从内存中获取图片
        //2.从本地中获取图片
        //3.从网络中获取图片
        netCacheUtil.getBitmapFromNet(imageUrl, position);
        return  null;


    }
}
