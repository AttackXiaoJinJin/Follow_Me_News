package com.project.chenjin.follow_me_news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.project.chenjin.follow_me_news.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ShowImageActivity extends AppCompatActivity {
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        url = getIntent().getStringExtra("url");

        final PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        Picasso.with(this)
                .load(url)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                         attacher.update();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
