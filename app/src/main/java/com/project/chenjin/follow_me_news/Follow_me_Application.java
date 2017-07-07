package com.project.chenjin.follow_me_news;

import android.app.Application;

import com.project.chenjin.follow_me_news.volley.VolleyManager;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/26   20:57.
 * 该class代表整个软件
 */

public class Follow_me_Application extends Application {
    //所有组件被创建前执行
    @Override
    public void onCreate(){
        super.onCreate();
        x.Ext.setDebug(true);
        x.Ext.init(this);
        //初始化volley
        VolleyManager.init(this);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush


    }
}
