package com.project.chenjin.follow_me_news.baseclass;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.chenjin.follow_me_news.MainActivity;
import com.project.chenjin.follow_me_news.R;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/29   13:39.
 * childern:HomePager, DiscoverPager, LetterPager, MessagePager, MinePager
 */

public class BasePager {
    //context = MainActivity
    public final Context context;

    public View rootView;
    //标题
    public TextView tv_title;
    //侧滑菜单
    public ImageButton ic_menu;
    //加载各个子页面
    public FrameLayout fl_content;

    public BasePager( Context context){
        this.context = context;
        rootView = initView();
       }

    private View initView() {
        View view = View.inflate(context, R.layout.base_pager, null);
        tv_title=(TextView)view.findViewById(R.id.tv_title);
        ic_menu=(ImageButton)view.findViewById(R.id.ic_menu);
        fl_content=(FrameLayout)view.findViewById(R.id.fl_content);
        ic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //若注释掉，则按左上按钮不会弹出侧面菜单栏
               MainActivity mainActivity = (MainActivity)context;
                //开，关，取反
                mainActivity.getSlidingMenu().toggle();

            }
        });
        return view;
    }

    public void initData(){

    }


}
