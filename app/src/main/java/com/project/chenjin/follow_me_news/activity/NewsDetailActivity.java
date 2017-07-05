package com.project.chenjin.follow_me_news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.chenjin.follow_me_news.R;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageButton icMenu;
    private ImageButton icBack;
    private ImageButton icTextsize;
    private ImageButton icShare;
    private WebView webView;
    private ProgressBar pbLoading;
    private String url;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-07-05 14:22:01 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        tvTitle = (TextView)findViewById( R.id.tv_title );
        icMenu = (ImageButton)findViewById( R.id.ic_menu );
        icBack = (ImageButton)findViewById( R.id.ic_back );
        icTextsize = (ImageButton)findViewById( R.id.ic_textsize );
        icShare = (ImageButton)findViewById( R.id.ic_share );
        webView = (WebView)findViewById(R.id.webview);
        pbLoading = (ProgressBar)findViewById(R.id.pb_loading);

        tvTitle.setVisibility(View.GONE);
        icMenu.setVisibility(View.GONE);
        icBack.setVisibility(View.VISIBLE);
        icTextsize.setVisibility(View.VISIBLE);
        icShare.setVisibility(View.VISIBLE);



        icBack.setOnClickListener( this);
        icTextsize.setOnClickListener( this);
        icShare.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-07-05 14:22:01 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */

    @Override
    public void onClick(View v) {
         if ( v == icBack ) {
             finish();
            // Handle clicks for icBack
        } else if ( v == icTextsize ) {
            // Handle clicks for icTextsize
        } else if ( v == icShare ) {
            // Handle clicks for icShare
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findViews();
        getData();
    }

    private void getData() {
      url = getIntent().getStringExtra("url");
        //设置支持js
        WebSettings webSettings = webView.getSettings();
        //设置支持js
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮,页面也要支持
        webSettings.setBuiltInZoomControls(true);
        //不让从当前网页跳转到系统浏览器中
        webView.setWebViewClient(new WebViewClient(){
            //当加载页面完成时回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
      //加载页面
        webView.loadUrl(url);
    }
}
