package com.project.chenjin.follow_me_news;

import android.app.Application;

import org.xutils.x;

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

    }
}
