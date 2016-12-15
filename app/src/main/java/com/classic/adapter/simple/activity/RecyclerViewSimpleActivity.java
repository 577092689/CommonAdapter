package com.classic.adapter.simple.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.adapter.simple.R;
import com.classic.adapter.simple.bean.News;
import com.classic.adapter.simple.consts.Consts;
import com.classic.adapter.simple.data.NewsDataSource;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewSimpleActivity extends DemoActivity
        implements CommonRecyclerAdapter.OnItemClickListener,
                   CommonRecyclerAdapter.OnItemLongClickListener {
    private RecyclerView mRecyclerView;
    private NewsAdapter  mNewsAdapter;

    @Override protected boolean canBack() {
        return true;
    }

    @Override protected int getLayoutResId() {
        return R.layout.activity_recyclerview;
    }

    @Override protected void testAdd() {
        mNewsAdapter.add(NewsDataSource.randomData());
    }

    @Override protected void testAddAll() {
        mNewsAdapter.addAll(NewsDataSource.getAddList(5));
    }

    @Override protected void testSetByIndex() {
        mNewsAdapter.set(0, NewsDataSource.randomData());
    }

    @Override protected void testRemoveByIndex() {
        mNewsAdapter.remove(0);
    }

    @Override protected void testReplaceAll() {
        mNewsAdapter.replaceAll(NewsDataSource.getReplaceList());
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle(R.string.main_recyclerview_simple_lable);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNewsAdapter = new NewsAdapter(this, R.layout.item_none_picture,
                                       NewsDataSource.getNewsList());
        mNewsAdapter.setOnItemClickListener(this);
        mNewsAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    @Override public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        Toast.makeText(RecyclerViewSimpleActivity.this,
                "RecyclerView onItemClick,position:" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        Toast.makeText(RecyclerViewSimpleActivity.this,
                "RecyclerView onItemLongClick,position:" + position, Toast.LENGTH_SHORT).show();
    }

    private class NewsAdapter extends CommonRecyclerAdapter<News> {

        NewsAdapter(Context context, int layoutResId, List<News> data) {
            super(context, layoutResId, data);
        }

        @Override public void onUpdate(BaseAdapterHelper helper, News item, int position) {
            helper.setText(R.id.item_none_picture_title, item.getTitle())
                  .setText(R.id.item_none_picture_author,
                          String.format(Locale.CHINA, Consts.FORMAT_AUTHOR, item.getAuthor()))
                  .setText(R.id.item_none_picture_date,
                          Consts.DATE_FORMAT.format(new Date(item.getReleaseTime())))
                  .setText(R.id.item_none_picture_intro, item.getIntro());
        }
    }
}
