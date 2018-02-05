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
import com.classic.adapter.simple.consts.Const;
import com.classic.adapter.simple.data.NewsDataSource;
import com.classic.adapter.simple.imageload.PicassoImageLoad;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewActivity extends DemoActivity
        implements CommonRecyclerAdapter.OnItemClickListener,
                   CommonRecyclerAdapter.OnItemLongClickListener {

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
        final List<News> oldData = mNewsAdapter.getData();
        final List<News> newData = NewsDataSource.getSimpleReplaceList(oldData.size());
        final DiffUtil.Callback callback = new DiffUtil.Callback() {
            @Override public int getOldListSize() {
                return oldData.size();
            }

            @Override public int getNewListSize() {
                return newData.size();
            }

            @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                //判断对象是否相等
                return oldData.get(oldItemPosition).getId() == newData.get(newItemPosition).getId();
            }

            @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                //判断对象的内容是否相等
                return oldData.get(oldItemPosition).getTitle().equals(newData.get(newItemPosition).getTitle());
            }
        };
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback, true);
        result.dispatchUpdatesTo(mNewsAdapter);
        mNewsAdapter.replaceAll(newData, false);
    }

    @Override protected void testClear() {
        mNewsAdapter.clear();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle(R.string.main_recyclerview_lable);
        //某个页面单独使用一套图片加载示例
        mImageLoad = new PicassoImageLoad();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mNewsAdapter = new NewsAdapter(this, R.layout.item_none_picture,
                NewsDataSource.getNewsList());
        recyclerView.setAdapter(mNewsAdapter);

        mNewsAdapter.setOnItemClickListener(this)
                    .setOnItemLongClickListener(this)
                    // 设置Child view点击事件(方式一)
                    .addChildViewListener(R.id.item_single_picture_cover,
                                          new com.classic.adapter.CommonRecyclerAdapter.OnChildViewClickListener() {
                                              @Override public void onItemClick(View view, int position) {
                                                  showToast("OnChildViewClickListener:" + position);
                                              }
                                          })
                    .addChildViewListener(R.id.item_multiple_picture_cover_left,
                                          new CommonRecyclerAdapter.OnChildViewLongClickListener() {
                                              @Override public boolean onItemLongClick(View view, int position) {
                                                  showToast("OnChildViewLongClickListener:" + position);
                                                  return true;
                                              }
                                          });



    }

    @Override public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        showToast("RecyclerView onItemClick:" + position);
    }

    @Override
    public boolean onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        showToast("RecyclerView onItemLongClick:" + position);
        return true;
    }

    private class NewsAdapter extends CommonRecyclerAdapter<News> {

        NewsAdapter(Context context, int layoutResId, List<News> data) {
            super(context, layoutResId, data);
        }

        @Override
        public void onCreate(RecyclerView.ViewHolder viewHolder, BaseAdapterHelper helper) {
            super.onCreate(viewHolder, helper);
            // ViewHolder创建时回调
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
                          // 设置Child view点击事件(方式二)
                          // .setOnClickListener(R.id.item_single_picture_cover, new View.OnClickListener() {
                          //     @Override public void onClick(View v) {
                          //         Toast.makeText(v.getContext(), position + "," + item.getTitle(), Toast.LENGTH_SHORT)
                          //              .show();
                          //     }
                          // });
                    break;
                case News.TYPE_MULTIPLE_PICTURE:
                    String[] urls = item.getCoverUrl().split(Const.URL_SEPARATOR);
                    helper.setText(R.id.item_multiple_picture_intro, item.getIntro())
                          .setImageUrl(R.id.item_multiple_picture_cover_left, urls[0])
                          .setImageUrl(R.id.item_multiple_picture_cover_right, urls[1]);
                          // 设置Child view点击事件(方式二)
                          // .setOnClickListener(R.id.item_multiple_picture_cover_left, new View.OnClickListener() {
                          //     @Override public void onClick(View v) {
                          //         Toast.makeText(v.getContext(), "left:" + position + "," + item.getTitle(),
                          //                        Toast.LENGTH_SHORT).show();
                          //     }
                          // })
                          // .setOnClickListener(R.id.item_multiple_picture_cover_right, new View.OnClickListener() {
                          //     @Override public void onClick(View v) {
                          //         Toast.makeText(v.getContext(), "right:" + position + "," + item.getTitle(),
                          //                        Toast.LENGTH_SHORT).show();
                          //     }
                          // });
                    break;
            }
        }
    }

    private void showToast(String content) {
        Toast.makeText(RecyclerViewActivity.this, content, Toast.LENGTH_SHORT).show();
    }
}
