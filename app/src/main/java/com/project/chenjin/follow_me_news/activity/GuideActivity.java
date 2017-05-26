package com.project.chenjin.follow_me_news.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.chenjin.follow_me_news.MainActivity;
import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.WelcomeActivity;
import com.project.chenjin.follow_me_news.until.CacheUntil;

import java.util.ArrayList;

/**
 *
 */
public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Button button;
    private LinearLayout linearLayout;
    private ImageView point_red;

    private ArrayList<ImageView> arrayimgs;
    private int point_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);



        viewPager=(ViewPager)findViewById(R.id.viewpager);
        button=(Button)findViewById(R.id.btn_startmain);
        linearLayout=(LinearLayout)findViewById(R.id.point_gray);
        point_red=(ImageView)findViewById(R.id.point_red);

        int [] img=new int[]{
                R.drawable.guide1,
                R.drawable.guide2,
               R.drawable.guide3
        };

        arrayimgs=new ArrayList<>();
        for(int i=0;i<img.length;i++){
            ImageView imageView=new ImageView(this);
            //设置背景
            imageView.setBackgroundResource(img[i]);
            //添加到集合中
            arrayimgs.add(imageView);

            ImageView grayPoint= new ImageView(this);
            grayPoint.setBackgroundResource(R.drawable.gray_point);
            //35px,像素,要做屏幕适配
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(35 , 35);

            if( i != 0){
                params.leftMargin=20;

            }

            //对grayPonit的父视图传递信息
            grayPoint.setLayoutParams(params);

            linearLayout.addView(grayPoint);

        }

        viewPager.setAdapter(new MyPagerAdapter());
        //根据view的生命周期，在onDraw和onLayout时，已有数据
        //添加红点移动功能
        point_red.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //得到屏幕的百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.保存guide页面
                CacheUntil.saveBoolean(GuideActivity.this, WelcomeActivity.START_MAIN_ACTIVITY, true);
                //2.开启主界面
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                //3.关闭guide界面
                finish();
            }
        });


    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 页面滚动回调该方法
         * @param position 当前页面的位置 百分比
         * @param positionOffset 页面滑动的百分比（match_parent 即与屏幕等价）
         * @param positionOffsetPixels 页面滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //定义两点间滑动的距离
            //重要position*point_length
            int slide_point_length=position*point_length + (int) (positionOffset * point_length );

            //设置页面滑动时，红点移动

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)point_red.getLayoutParams();
            params.leftMargin=slide_point_length;
            point_red.setLayoutParams(params);

        }

        /**
         * 选中的页面
         * @param position 返回位置
         */
        @Override
        public void onPageSelected(int position) {
            if(position == arrayimgs.size() - 1){
                button.setVisibility(View.VISIBLE);

            }
            else{
                button.setVisibility(View.GONE);

            }

        }

        /**
         * 页面滚动的状态改变的时候回调该方法
         * @param state 状态
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }


    }


    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
             //只需执行一次，故要关闭
            //默认执行2次，可打印日志观察
            if(Build.VERSION.SDK_INT>=16)
            //this == MyOnGlobalLayoutListener.this
            { point_red.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            else
            {
                point_red.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }

            point_length=linearLayout.getChildAt(1).getLeft() - linearLayout.getChildAt(0).getLeft() ;
        }
    }



    class MyPagerAdapter extends PagerAdapter{

        /**
         * 返回数据的总个数
         * @return
         */
        @Override
        public int getCount() {
            return arrayimgs.size();
        }



        /**
         * 销毁页面
         * @param container ViewPager
         * @param position 要销毁的页面位置
         * @param object 要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         *
         * @param container ViewPager
         * @param position 创建页面的位置
         * @return 返回相应页面的位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView=arrayimgs.get(position);
            //添加到容器中
            container.addView(imageView);

            // 1. return position;
            return imageView;
        }

        /**
         * 判断
         * @param view 当前创建的视图
         * @param object 上面instantiateItem返回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            //1. return view==arrayimgs.get(Integer.parseInt((String)object));
            return view==object;
        }




    }

}
