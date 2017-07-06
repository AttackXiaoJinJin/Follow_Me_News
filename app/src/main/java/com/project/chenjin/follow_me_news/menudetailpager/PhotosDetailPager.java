package com.project.chenjin.follow_me_news.menudetailpager;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.baseclass.MenuDetailBasePager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/7/1   22:48.
 * 图组详情页面
 */

public class PhotosDetailPager extends MenuDetailBasePager{
    @ViewInject(R.id.listview_photos)
    private ListView listView_photos;

    @ViewInject(R.id.gridview_photos)
    private GridView gridView_photos;
    public PhotosDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_detail_pager, null);
        x.view().inject(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

    }
}
