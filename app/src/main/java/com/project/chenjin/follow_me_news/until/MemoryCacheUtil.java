package com.project.chenjin.follow_me_news.until;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/7   12:25.
 * Java之软引用&弱引用&虚引用
 */

public class MemoryCacheUtil {
    //集合
    private LruCache<String , Bitmap> lruCache;

    public MemoryCacheUtil(){
        //使用系统分配给应用程序的八分之一作为缓存大小
        //maxsize和value.getRowBytes() * value.getHeight();的单位一定要统一，一个除以1024，另一个必须除以1024
        int maxsize =(int) (Runtime.getRuntime().maxMemory()/1024/8);
        lruCache = new LruCache<String , Bitmap>(maxsize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight())/1024;
            }
        };
    }

    //根据url从内存中获取图片
    public Bitmap getBitmapFromUrl(String imageUrl) {
        return lruCache.get(imageUrl);
    }

    //根据url保存图片到lruCache集合中
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl, bitmap);


    }
}
