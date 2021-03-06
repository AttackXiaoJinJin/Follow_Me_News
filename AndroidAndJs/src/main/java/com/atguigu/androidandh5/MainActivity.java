package com.atguigu.androidandh5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/6/29   15:28.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnJavaAndJs;
    private Button btnJsCallJava;
    private Button btnJsCallPhone;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-07-28 10:58:01 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        btnJavaAndJs = (Button)findViewById( R.id.btn_java_and_js );
        btnJsCallJava = (Button)findViewById( R.id.btn_js_call_java );
        btnJsCallPhone = (Button)findViewById( R.id.btn_js_call_phone );

        btnJavaAndJs.setOnClickListener( this );
        btnJsCallJava.setOnClickListener( this );
        btnJsCallPhone.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-07-28 10:58:01 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnJavaAndJs ) {
            // Handle clicks for btnJavaAndJs
            Intent intent = new Intent(this,JavaAndJSActivity.class);
            startActivity(intent);
        } else if ( v == btnJsCallJava ) {
            // Handle clicks for btnJsCallJava
            Intent intent = new Intent(this,JsCallJavaVideoActivity.class);
            startActivity(intent);
        } else if ( v == btnJsCallPhone ) {
            // Handle clicks for btnJsCallPhone
            Intent intent = new Intent(this,JsCallJavaCallPhoneActivity.class);
            startActivity(intent);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }
}
