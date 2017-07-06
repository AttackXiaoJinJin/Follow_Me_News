package com.atguigu.androidandh5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/29   15:28.
 */
public class JsCallJavaCallPhoneActivity extends Activity {


    private WebView webview;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_java_video);
        webview = (WebView) findViewById(R.id.webview);

        webSettings = webview.getSettings();
        /*//设置支持js
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮,页面也要支持
        webSettings.setBuiltInZoomControls(true);
        webSettings.setTextZoom(100);
        //不让从当前网页跳转到系统浏览器中
        webview.setWebViewClient(new WebViewClient() );


        //添加js接口

        webview.addJavascriptInterface(new MyJavascriptInterface(), "Android");


        //加载网络的页面，也可以加载应用内置的页面
        webview.loadUrl("file:///android_asset/JsCallJavaCallPhone.html");*/
        //设置支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);

        //支持双击-前提是页面要支持才显示
//        webSettings.setUseWideViewPort(true);
        webview.setWebChromeClient(new WebChromeClient());

        //支持缩放按钮-前提是页面要支持才显示
        webSettings.setBuiltInZoomControls(true);

        //设置客户端-不跳转到默认浏览器中
        webview.setWebViewClient(new WebViewClient());

        //设置支持js调用java
        webview.addJavascriptInterface(new AndroidAndJSInterface(), "Android");

        //加载本地资源

        //webview.loadUrl("file:///android_asset/JsCallJavaCallPhone.html");
        webview.loadUrl("http://192.168.1.105:8888/JsCallJavaCallPhone.html");

    }

    private class AndroidAndJSInterface {
        //拨打电话
       /* @JavascriptInterface
        public void call(String phone) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone ));
            Toast.makeText(JsCallJavaCallPhoneActivity.this, phone, Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(JsCallJavaCallPhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            startActivity(intent);
            }*/

        @JavascriptInterface
        public void call(String phone) {
           Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri uri = Uri.parse("tel:" + phone );
           intent.setData(uri);
            startActivity(intent);
            }

         /*   @JavascriptInterface
        public void call(String phone) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri uri = Uri.parse("tel:" + phone);
            intent.setData(uri);
            if (ActivityCompat.checkSelfPermission(JsCallJavaCallPhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
            }*/





        //加载联系人
        @JavascriptInterface
        public void showcontacts() {
            //String json = "[{\"name\":\"大脸怪\",\"phone\":\"12345678910\"}]";
           // webview.loadUrl("javascript:show("+"'"+json+"'"+")");
           /* runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String json = "[{\"name\":\"大脸怪\",\"phone\":\"12345678910\"}]";
                    webview.loadUrl("javascript:show('"+json+"')");
                }
            });*/

            webview.postDelayed(new Runnable() {

                @Override
                public void run() {
                    String json = "[{\"name\":\"大脸怪\",\"phone\":\"12345678910\"}]";
                    webview.loadUrl("javascript:show('"+json+"')");
                    Toast.makeText(JsCallJavaCallPhoneActivity.this, json, Toast.LENGTH_SHORT).show();
                }
            }, 500);






        }
    }

}







