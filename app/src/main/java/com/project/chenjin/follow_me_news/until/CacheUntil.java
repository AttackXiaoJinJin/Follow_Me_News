package com.project.chenjin.follow_me_news.until;

/*
 * Created by chenjin on 2017/5/24.
 * 缓存数据
 */

import android.content.Context;
import android.content.SharedPreferences;

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
}
