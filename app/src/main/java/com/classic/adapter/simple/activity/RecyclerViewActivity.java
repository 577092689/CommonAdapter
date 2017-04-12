package com.classic.adapter.simple.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.adapter.interfaces.ImageLoad;
import com.classic.adapter.simple.R;
import com.classic.adapter.simple.bean.News;
import com.classic.adapter.simple.consts.Consts;
import com.classic.adapter.simple.data.NewsDataSource;
import com.classic.adapter.simple.imageload.PicassoImageLoad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewActivity extends DemoActivity
        implements CommonRecyclerAdapter.OnItemClickListener,
                   CommonRecyclerAdapter.OnItemLongClickListener {

    private RecyclerView mRecyclerView;
    private NewsAdapter  mNewsAdapter;
    private ImageLoad    mImageLoad;

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
        final ArrayList<News> newData = NewsDataSource.getReplaceList();
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

    @Override protected void testClear() {
        mNewsAdapter.clear();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle(R.string.main_recyclerview_lable);
        //某个页面单独使用一套图片加载示例
        mImageLoad = new PicassoImageLoad();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNewsAdapter = new NewsAdapter(this, R.layout.item_none_picture,
                NewsDataSource.getNewsList());
        mNewsAdapter.setOnItemClickListener(this);
        mNewsAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    @Override public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        Toast.makeText(RecyclerViewActivity.this, "RecyclerView onItemClick,position:" + position,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        Toast.makeText(RecyclerViewActivity.this,
                "RecyclerView onItemLongClick,position:" + position, Toast.LENGTH_SHORT).show();
    }

    private class NewsAdapter extends CommonRecyclerAdapter<News> {

        NewsAdapter(Context context, int layoutResId, List<News> data) {
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

        @Override public void onUpdate(BaseAdapterHelper helper, final News item, final int position) {
            helper.setImageLoad(mImageLoad);
            switch (item.getNewsType()) {
                case News.TYPE_NONE_PICTURE:
                    helper.setText(R.id.item_none_picture_title, item.getTitle())
                          .setText(R.id.item_none_picture_author,
                                  String.format(Locale.CHINA, Consts.FORMAT_AUTHOR,
                                          item.getAuthor()))
                          .setText(R.id.item_none_picture_date,
                                  Consts.DATE_FORMAT.format(new Date(item.getReleaseTime())))
                          .setText(R.id.item_none_picture_intro, item.getIntro());
                    break;
                case News.TYPE_SINGLE_PICTURE:
                    helper.setText(R.id.item_single_picture_title, item.getTitle())
                          .setText(R.id.item_single_picture_author,
                                  String.format(Locale.CHINA, Consts.FORMAT_AUTHOR,
                                          item.getAuthor()))
                          .setText(R.id.item_single_picture_date,
                                  Consts.DATE_FORMAT.format(new Date(item.getReleaseTime())))
                          .setImageUrl(R.id.item_single_picture_cover, item.getCoverUrl())
                          .setOnClickListener(R.id.item_single_picture_cover, new View.OnClickListener() {
                              @Override public void onClick(View v) {
                                  Toast.makeText(v.getContext(), position + "," + item.getTitle(), Toast.LENGTH_SHORT)
                                       .show();
                              }
                          });
                    break;
                case News.TYPE_MULTIPLE_PICTURE:
                    String[] urls = item.getCoverUrl().split(Consts.URL_SEPARATOR);
                    helper.setText(R.id.item_multiple_picture_intro, item.getIntro())
                          .setImageUrl(R.id.item_multiple_picture_cover_left, urls[0])
                          .setImageUrl(R.id.item_multiple_picture_cover_right, urls[1])
                          .setOnClickListener(R.id.item_multiple_picture_cover_left, new View.OnClickListener() {
                              @Override public void onClick(View v) {
                                  Toast.makeText(v.getContext(), "left:" + position + "," + item.getTitle(),
                                                 Toast.LENGTH_SHORT).show();
                              }
                          })
                          .setOnClickListener(R.id.item_multiple_picture_cover_right, new View.OnClickListener() {
                              @Override public void onClick(View v) {
                                  Toast.makeText(v.getContext(), "right:" + position + "," + item.getTitle(),
                                                 Toast.LENGTH_SHORT).show();
                              }
                          });
                    break;
            }
        }
    }
}
