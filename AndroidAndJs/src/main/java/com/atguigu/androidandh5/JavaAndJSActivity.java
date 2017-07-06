package com.atguigu.androidandh5;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.webkit.JavascriptInterface;
/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/29   15:28.
 */
public class JavaAndJSActivity extends Activity implements View.OnClickListener {
    private EditText etNumber;
    private EditText etPassword;
    private Button btnLogin;
    /**
     * 加载网页或者说H5页面
     */
    private WebView webView;
    private WebSettings webSettings;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-07-28 11:43:37 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_java_and_js);
        etNumber = (EditText) findViewById(R.id.et_number);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        initWebView();
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-07-28 11:43:37 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            // Handle clicks for btnLogin
            login();
        }
    }

    private void login() {
        String numberC = etNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(numberC) || TextUtils.isEmpty(password)) {
            Toast.makeText(JavaAndJSActivity.this, "账号或者密码为空", Toast.LENGTH_SHORT).show();
        } else {
            //把账号传递给html页面
            //登录
            loginC(numberC);
        }
    }

    private void loginC(String numberC){
           webView.loadUrl("javascript:javaCallJs(" + "'" + numberC + "'" + ")");
           setContentView(webView);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initWebView();
    }


    private void initWebView() {
        webView = new WebView(this);
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

            }
        });
        //添加js接口

        webView.addJavascriptInterface(new MyJavascriptInterface(),"android");

        //加载网络的页面，也可以加载应用内置的页面
        //webView.loadUrl("file:///android_asset/JavaAndJavaScriptCall.html");
        webView.loadUrl("http://192.168.1.105:8888/JavaAndJavaScriptCall.html");


       // setContentView(webView);
    }

    private class MyJavascriptInterface {
        @JavascriptInterface
        public void showToast(){
            Toast.makeText(JavaAndJSActivity.this , "java被js调用了",Toast.LENGTH_SHORT).show();
        }

    }
}
