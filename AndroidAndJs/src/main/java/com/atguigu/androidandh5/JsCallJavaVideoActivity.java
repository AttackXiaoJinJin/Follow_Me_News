package com.atguigu.androidandh5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/29   15:28.
 */
public class JsCallJavaVideoActivity extends Activity {
    private WebView webView;
    private WebSettings webSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_java_video);
        webView = (WebView)findViewById(R.id.webview);

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

            }
        });
        //添加js接口

       webView.addJavascriptInterface(new MyJavascriptInterface(),"android");

        //加载网络的页面，也可以加载应用内置的页面
        //webView.loadUrl("file:///android_asset/RealNetJSCallJavaActivity.html");
        webView.loadUrl("http://192.168.1.105:8888/RealNetJSCallJavaActivity.html");

    }

   private class MyJavascriptInterface {
        @JavascriptInterface
       public void playVideo(int id,String videoUrl,String title){
            //隐式意图
            Intent intent = new Intent();
            intent.setDataAndType(Uri.parse(videoUrl), "video/*");
            startActivity(intent);
        }
    }
}
