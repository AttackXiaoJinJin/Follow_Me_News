package com.project.chenjin.follow_me_news.until;

import android.graphics.Bitmap;
import android.os.Handler;

import org.xutils.common.util.LogUtil;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/7   0:23.
 * 图片三级缓存工具类
 */

public class BitmapCacheUtil {
    //网路缓存工具类（网络请求）
    private NetCacheUtil netCacheUtil;

    //本地缓存的工具类
    private LocalCacheUtil localCacheUtil;
    //内存缓存的工具类
    private MemoryCacheUtil memoryCacheUtil;

    public BitmapCacheUtil(Handler handler) {
        memoryCacheUtil = new MemoryCacheUtil();
        localCacheUtil = new LocalCacheUtil(memoryCacheUtil);
       netCacheUtil =new NetCacheUtil(handler, localCacheUtil, memoryCacheUtil);
    }


    public Bitmap getBitmap(String imageUrl, int position) {
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


    }
}
