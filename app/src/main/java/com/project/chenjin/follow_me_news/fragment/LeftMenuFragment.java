package com.project.chenjin.follow_me_news.fragment;


import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.chenjin.follow_me_news.MainActivity;
import com.project.chenjin.follow_me_news.R;
import com.project.chenjin.follow_me_news.baseclass.BaseFragment;
import com.project.chenjin.follow_me_news.domain.HomePagerBean;
import com.project.chenjin.follow_me_news.pagers.HomePager;
import com.project.chenjin.follow_me_news.until.DensityUtil;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 项目名称： Follow_Me_News
 * 创建人  ： chenjin
 * 创建时间： 2017/5/26   23:04.
 */

public class LeftMenuFragment extends BaseFragment{

    private List<HomePagerBean.DataBean> slidingdata;
    //private List<HomePagerBean2.DetailPagerData> slidingdata;
    private ListView listView;
    private LeftMenuFragmentAdapter leftMenuFragmentAdapter;
    //点击的位置
    private int prePosition;


    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context, 40),0,0);
        //设置分割线高度为0
        listView.setDividerHeight(0);
        //兼容2.3及以下版本，按下listview会变灰，故改成透明
        listView.setCacheColorHint(Color.TRANSPARENT);
        //设置按下listview的item不变色
        listView.setSelector(android.R.color.transparent);
        //设置item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.记录点击的位置，点击后变成红色
                prePosition = position;
                //getCount()-->getView
                leftMenuFragmentAdapter.notifyDataSetChanged();
                //2.把左侧菜单关闭
                //若注释掉，则侧面菜单点击后不会关闭
               MainActivity mainActivity = (MainActivity)context;
                //开，关，取反
                mainActivity.getSlidingMenu().toggle();
                //3.切换到对应的详情页面,新闻，专题，组图，互动
                switchPager(position);


            }
        });

        return listView;
    }

    //根据位置切换不同详情页面
    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity)context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        HomePager homePager = contentFragment.getHomePager();
        homePager.switchPager(position);
    }

    @Override
    public void initData() {
        super.initData();



    }

    //接收数据
    //public void setData(List<HomePagerBean2.DetailPagerData> slidingdata)
    public void setData(List<HomePagerBean.DataBean> slidingdata) {
        this.slidingdata = slidingdata;
        for(int i=0;i<slidingdata.size();i++){
            LogUtil.e("标题 " + slidingdata.get(i).getTitle());
        }

        //设置适配器
        leftMenuFragmentAdapter = new LeftMenuFragmentAdapter();
        listView.setAdapter(leftMenuFragmentAdapter);
        //设置默认页面
        switchPager(prePosition);



    }

    class LeftMenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return slidingdata.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView)View.inflate(context, R.layout.item_leftmenu,null);
            textView.setText(slidingdata.get(position).getTitle());
          /*  if(position == prePosition)
            {
                //设置红色
                textView.setEnabled(true);
            }
            else
            {
                textView.setEnabled(false);
            }*/
            textView.setEnabled(position == prePosition);
            return textView;
        }
    }

}
