package com.classic.adapter.simple.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.classic.adapter.simple.R;
import com.classic.adapter.simple.bean.News;
import com.classic.adapter.simple.consts.Const;
import com.classic.adapter.simple.data.NewsDataSource;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListViewActivity extends DemoActivity {
    private MultipleLayoutAdapter mAdapter;

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
        mToolbar.setTitle(R.string.main_listview_lable);
        ListView listView = findViewById(R.id.listview);
        mAdapter = new MultipleLayoutAdapter(this, R.layout.item_none_picture,
                NewsDataSource.getNewsList());
        listView.setAdapter(mAdapter);
    }

    private final class MultipleLayoutAdapter extends CommonAdapter<News> {

        MultipleLayoutAdapter(Context context, int layoutResId, List<News> data) {
            super(context, layoutResId, data);
        }

        @Override public int getLayoutResId(News item, int position) {
            int layoutResId = -1;
            switch (item.getNewsType()) {
                case News.TYPE_NONE_PICTURE:
                    layoutResId = R.layout.item_none_picture;
                    break;
                case News.TYPE_SINGLE_PICTURE:
                    layoutResId = R.layout.item_single_picture;
                    break;
                case News.TYPE_MULTIPLE_PICTURE:
                    layoutResId = R.layout.item_multiple_picture;
                    break;
            }
            return layoutResId;
        }

        @Override public void onUpdate(BaseAdapterHelper helper, News item, int position) {
            switch (item.getNewsType()) {
                case News.TYPE_NONE_PICTURE:
                    helper.setText(R.id.item_none_picture_title, item.getTitle())
                          .setText(R.id.item_none_picture_author,
                                  String.format(Locale.CHINA, Const.FORMAT_AUTHOR,
                                                item.getAuthor()))
                          .setText(R.id.item_none_picture_date,
                                   Const.DATE_FORMAT.format(new Date(item.getReleaseTime())))
                          .setText(R.id.item_none_picture_intro, item.getIntro());
                    break;
                case News.TYPE_SINGLE_PICTURE:
                    helper.setText(R.id.item_single_picture_title, item.getTitle())
                          .setText(R.id.item_single_picture_author,
                                  String.format(Locale.CHINA, Const.FORMAT_AUTHOR,
                                                item.getAuthor()))
                          .setText(R.id.item_single_picture_date,
                                   Const.DATE_FORMAT.format(new Date(item.getReleaseTime())))
                          .setImageUrl(R.id.item_single_picture_cover, item.getCoverUrl());
                    break;
                case News.TYPE_MULTIPLE_PICTURE:
                    String[] urls = item.getCoverUrl().split(Const.URL_SEPARATOR);
                    helper.setText(R.id.item_multiple_picture_intro, item.getIntro())
                          .setImageUrl(R.id.item_multiple_picture_cover_left, urls[0])
                          .setImageUrl(R.id.item_multiple_picture_cover_right, urls[1]);
                    break;
            }
        }
    }
}
