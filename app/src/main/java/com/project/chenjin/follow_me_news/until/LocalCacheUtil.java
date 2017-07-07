package com.project.chenjin.follow_me_news.until;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/7   10:44.
 * 本地缓存工具类
 */

class LocalCacheUtil {
    private final MemoryCacheUtil memoryCacheUtil;

    public LocalCacheUtil(MemoryCacheUtil memoryCacheUtil) {
        this.memoryCacheUtil = memoryCacheUtil;
    }

    //根据url获取图片
    public Bitmap getBitmapFromUrl(String imageUrl) {
        //判断sd卡是否直接可用（挂载）
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //保存图片在路径xxxxxx下
            //MD5加密
            try {

                String fileName = MD5Encoder.encode(imageUrl);
                File file = new File(Environment.getExternalStorageDirectory() + "/followmenews" , fileName);
                LogUtil.e("位置：++++++++++" + Environment.getExternalStorageDirectory());
                //如果存在，以流的方式去获取
                if(file.exists()){
                    FileInputStream is = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    if(bitmap != null){
                        memoryCacheUtil.putBitmap(imageUrl, bitmap);
                        LogUtil.e("从本地保存到内存中");
                    }

                    return bitmap;

                }




            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片本地保存失败");
            }
        }


        return null;
    }

    //根据url保存图片
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        //判断sd卡是否直接可用（挂载）
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //保存图片在路径xxxxxx下
            //MD5加密
            try {

                String fileName = MD5Encoder.encode(imageUrl);
                File file = new File(Environment.getExternalStorageDirectory() + "/followmenews" , fileName);
                LogUtil.e("位置：++++++++++" + Environment.getExternalStorageDirectory());

                File parentFile =  file.getParentFile();//mnt/sdcard/beijingnews
                if(!parentFile.exists()){
                    //创建目录
                    parentFile.mkdirs();
                }

                if(!file.exists()){
                    file.createNewFile();
                }
                //保存图片
                bitmap.compress(Bitmap.CompressFormat.PNG , 100 , new FileOutputStream(file));



            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片本地保存失败");
            }
        }



    }
}
