package com.classic.adapter.simple.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.classic.adapter.simple.R;
import com.classic.adapter.simple.bean.News;
import com.classic.adapter.simple.consts.Const;
import com.classic.adapter.simple.data.NewsDataSource;

import java.util.Date;
import java.util.Locale;

public class ListViewSimpleActivity extends DemoActivity {

    @Override protected boolean canBack() {
        return true;
    }

    @Override protected int getLayoutResId() {
        return R.layout.activity_listview;
    }

    @Override protected void testAdd() {
        mAdapter.add(NewsDataSource.randomData());
    }

    @Override protected void testAddAll() {
        mAdapter.addAll(NewsDataSource.getAddList(5));
    }

    @Override protected void testSetByIndex() {
        mAdapter.set(0, NewsDataSource.randomData());
    }

    @Override protected void testRemoveByIndex() {
        mAdapter.remove(0);
    }

    @Override protected void testReplaceAll() {
        mAdapter.replaceAll(NewsDataSource.getReplaceList());
    }

    @Override protected void testClear() {
        mAdapter.clear();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle(R.string.main_listview_simple_lable);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
    }

    private CommonAdapter<News> mAdapter = new CommonAdapter<News>(this, R.layout.item_none_picture,
            NewsDataSource.getNewsList()) {
        @Override public void onUpdate(BaseAdapterHelper helper, News item, int position) {
            helper.setText(R.id.item_none_picture_title, item.getTitle())
                  .setText(R.id.item_none_picture_author,
                          String.format(Locale.CHINA, Const.FORMAT_AUTHOR, item.getAuthor()))
                  .setText(R.id.item_none_picture_date,
                           Const.DATE_FORMAT.format(new Date(item.getReleaseTime())))
                  .setText(R.id.item_none_picture_intro, item.getIntro());
        }
    };
}
