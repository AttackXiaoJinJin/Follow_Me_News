package com.project.chenjin.follow_me_news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    private RelativeLayout welcome_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcome_layout=(RelativeLayout)findViewById(R.id.welcome_layout);


        //渐变动画，缩放动画，旋转动画
        //渐变动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        //持续播放时间
       // alphaAnimation.setDuration(1000);
        //停留在播放后的状态
        //alphaAnimation.setFillAfter(true);

        //缩放动画
        ScaleAnimation scaleAnimation= new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
       // scaleAnimation.setDuration(1000);
        //scaleAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation=new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        //rotateAnimation.setDuration(1000);
        //rotateAnimation.setFillAfter(true);

        //让三个动画同时播放
        AnimationSet animationSet=new AnimationSet(false);
        //添加三个动画没有先后顺序,目的是同时播放动画
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setDuration(1000);
        animationSet.setFillAfter(true);

        welcome_layout.startAnimation(animationSet);
        animationSet.setAnimationListener(new MyAnimationListener());

    }


    //用alt+ins 来导入抽象方法
    //笔记本是先将数字键取消，然后0就成为了ins键
    class MyAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {
            Toast.makeText(WelcomeActivity.this, "动画播放完成", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}
