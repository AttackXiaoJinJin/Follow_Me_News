package com.project.chenjin.follow_me_news.until;

/*
 * Created by chenjin on 2017/5/24.
 * 缓存数据
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class CacheUntil {
    public static boolean getBoolean(Context context, String key) {
        //使用sharepreference来保存数据
        SharedPreferences sp= context.getSharedPreferences("chenjin" , context.MODE_PRIVATE);
        //sp.getBoolean(key, false);

     //默认返回false，即没有进入过主界面
     return  sp.getBoolean(key, false);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        //使用sharepreference来保存数据
        SharedPreferences sp= context.getSharedPreferences("chenjin" , context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }
    //缓存文本数据
    public static void putString(Context context, String key, String value) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            try {

                String fileName = MD5Encoder.encode(key);
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
                //保存文本数据
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(value.getBytes());
                fileOutputStream.close();


            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本数据保存失败");
            }
        }else{
            SharedPreferences sp= context.getSharedPreferences("chenjin" , context.MODE_PRIVATE);
            sp.edit().putString(key, value).commit();
        }



    }

    //获取缓存文本信息
    public static String getString(Context context, String key) {
        String result = "" ;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //保存图片在路径xxxxxx下
            //MD5加密
            try {

                String fileName = MD5Encoder.encode(key);
                File file = new File(Environment.getExternalStorageDirectory() + "/followmenews" , fileName);
                LogUtil.e("位置：++++++++++" + Environment.getExternalStorageDirectory());
                //如果存在，以流的方式去获取
                if(file.exists()){
                    FileInputStream is = new FileInputStream(file);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1 ){
                        stream.write(buffer, 0 ,length);
                    }

                    is.close();
                    stream.close();

                    result = stream.toString();




                }




            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("获取缓存文本失败");
            }
        }else {
            SharedPreferences sp= context.getSharedPreferences("chenjin" , context.MODE_PRIVATE);
            //return  sp.getString(key, none);会崩溃
            result = sp.getString(key, "");


        }

      return result;


    }
}
