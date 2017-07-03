package com.project.chenjin.follow_me_news;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.project.chenjin.follow_me_news.fragment.ContentFragment;
import com.project.chenjin.follow_me_news.fragment.LeftMenuFragment;
import com.project.chenjin.follow_me_news.until.DensityUtil;

public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFT_CONTENT_TAG = "left_content_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //去掉顶部标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initSlidingMenu();


        //初始化fragment
        initFragment();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //让底部虚拟导航栏适应app
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    private void initSlidingMenu() {
        //1.设置主页面
        setContentView(R.layout.activity_main);

        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_left_menu);

        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        //设置右侧菜单
        // slidingMenu.setSecondaryMenu(R.layout.activity_right_menu);

        //4.设置显示模式：左+主 主+右 左+主+右
        slidingMenu.setMode(SlidingMenu.LEFT);

        //5.设置滑动模式：滑动边缘 全屏滑动 部分滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //6.设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this, 260));
    }

    private void initFragment() {
        /*
        //1.得到fragmentManger
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //3.替换
        //抽取tag,因为还要供其他地方使用
        ft.replace(R.id.main_frame, new ContentFragment(), MAIN_CONTENT_TAG);
        ft.replace(R.id.leftmenu_frame, new LeftMenuFragment(), LEFT_CONTENT_TAG);
        //4.提交
        ft.commit();
         */

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ContentFragment(), MAIN_CONTENT_TAG)
                .replace(R.id.leftmenu_frame, new LeftMenuFragment(), LEFT_CONTENT_TAG).commit();

    }

    //得到左侧菜单fragment
    public LeftMenuFragment getLeftMenuFragment() {
       /* FragmentManager fragmentManager = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fragmentManager.findFragmentByTag(LEFT_CONTENT_TAG);
        return  leftMenuFragment;*/
       return (LeftMenuFragment)getSupportFragmentManager().findFragmentByTag(LEFT_CONTENT_TAG);

    }

    //得到正文fragment
    public ContentFragment getContentFragment() {
        return (ContentFragment)getSupportFragmentManager().findFragmentByTag(MAIN_CONTENT_TAG);
    }
}
