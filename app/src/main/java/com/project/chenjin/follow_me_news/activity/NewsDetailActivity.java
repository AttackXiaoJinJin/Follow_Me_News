package com.project.chenjin.follow_me_news.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    private WebSettings webSettings;

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
             showChangeTextSizeDialog();
        } else if ( v == icShare ) {
            // Handle clicks for icShare
        }
    }
    //字体缓存
    private int tempSize = 2;
    private int realSize = tempSize;

    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = new String[]{"超大字体","大字体","正常字体","小字体","超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize = which;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize = tempSize;
                changeTextSize(realSize);
            }
        });
        builder.show();

    }

    private void changeTextSize(int realSize) {
        switch (realSize){
            //超大字体
            case 0:webSettings.setTextZoom(200);
                break;
            //大字体
            case 1:webSettings.setTextZoom(150);
                break;
            //正常字体
            case 2:webSettings.setTextZoom(100);
                break;
            //小字体
            case 3:webSettings.setTextZoom(75);
                break;
            //超小字体
            case 4:webSettings.setTextZoom(50);
                break;
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
        webSettings = webView.getSettings();
        //设置支持js
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮,页面也要支持
        webSettings.setBuiltInZoomControls(true);
        webSettings.setTextZoom(100);
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
