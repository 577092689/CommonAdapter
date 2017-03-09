package com.classic.adapter.simple.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewSimpleActivity extends DemoActivity
        implements CommonRecyclerAdapter.OnItemClickListener,
                   CommonRecyclerAdapter.OnItemLongClickListener {

    private static final String KEY_TITLE     = "title";
    private static final String KEY_INTRO     = "intro";
    private static final String KEY_AUTHOR    = "author";

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
        final ArrayList<News> newData = NewsDataSource.getSimpleReplaceList(10);
        final DiffUtil.Callback callback = new DiffUtil.Callback() {
            @Override public int getOldListSize() {
                return mNewsAdapter.getData().size();
            }

            @Override public int getNewListSize() {
                return newData.size();
            }

            @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                //判断对象是否相等
                return mNewsAdapter.getItem(oldItemPosition).getId() ==
                       newData.get(newItemPosition).getId();
            }

            @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                //判断对象的内容是否相等
                return mNewsAdapter.getItem(oldItemPosition).getTitle().equals(
                        newData.get(newItemPosition).getTitle()) &&
                       mNewsAdapter.getItem(oldItemPosition).getIntro().equals(
                               newData.get(newItemPosition).getIntro());
            }

            // 更小粒度的更新，比如某个对象的某个属性值改变了，只改变此属性
            // 此方法为可选，这里只是提供一个使用示例
            @Nullable @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                final News oldItem = mNewsAdapter.getItem(oldItemPosition);
                final News newItem = newData.get(newItemPosition);
                Bundle bundle = new Bundle();
                if(!oldItem.getTitle().equals(newItem.getTitle())){
                    bundle.putString(KEY_TITLE, newItem.getTitle());
                }
                if(!oldItem.getAuthor().equals(newItem.getAuthor())){
                    bundle.putString(KEY_AUTHOR, newItem.getAuthor());
                }
                if(!oldItem.getIntro().equals(newItem.getIntro())){
                    bundle.putString(KEY_INTRO, newItem.getIntro());
                }
                return bundle.size() == 0 ? null : bundle;
            }
        };
        new Thread(new Runnable() {
            @Override public void run() {
                final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback, true);
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mNewsAdapter.replaceAll(newData, false);
                        result.dispatchUpdatesTo(mNewsAdapter);
                    }
                });
            }
        }).start();
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

        /**
         * 更小粒度的更新，比如某个对象的某个属性值改变了，只改变此属性
         * 如果你重写了DiffUtil.Callback的getChangePayload方法，此回调才会执行
         *
         * @param helper
         * @param payloads
         */
        @Override public void onItemContentChanged(
                @NonNull BaseAdapterHelper helper, @NonNull List<Object> payloads) {
            Bundle bundle = (Bundle) payloads.get(0);
            for(String key : bundle.keySet()){
                final String value = bundle.getString(key);
                switch (key) {
                    case KEY_TITLE:
                        helper.setText(R.id.item_none_picture_title, value);
                        break;
                    case KEY_AUTHOR:
                        helper.setText(R.id.item_none_picture_author, value);
                        break;
                    case KEY_INTRO:
                        helper.setText(R.id.item_none_picture_intro, value);
                        break;
                }
            }
        }
    }
}
