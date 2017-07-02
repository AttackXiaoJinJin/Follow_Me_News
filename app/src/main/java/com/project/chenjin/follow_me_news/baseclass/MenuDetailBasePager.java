package com.project.chenjin.follow_me_news.baseclass;

import android.content.Context;
import android.view.View;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/1   22:39.
 * 详情页面的基类
 */

public abstract class MenuDetailBasePager {
    public final Context context;
    //代表各个详情页面的视图
    public View rootView;

    public MenuDetailBasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    //抽象方法强制子类实现该方法，每个页面实现不同的效果
    public abstract View initView();
    //子页面绑定数据，联网请求数据的时候，重写该方法
    public void initData(){

    }

}
