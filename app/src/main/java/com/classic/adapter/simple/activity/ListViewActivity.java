package com.classic.adapter.simple.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.classic.adapter.simple.R;
import com.classic.adapter.simple.bean.News;
import com.classic.adapter.simple.consts.Consts;
import com.classic.adapter.simple.data.NewsDataSource;
import com.classic.adapter.simple.imageload.GlideImageLoad;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListViewActivity extends AppCompatActivity {
    private ListView listView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        //这里偷懒，使用默认的。实际项目中建议使用ToolBar
        getSupportActionBar().setTitle(R.string.main_listview_lable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new MultipleLayoutAdapter(this, R.layout.item_none_picture,
            NewsDataSource.getNewsList()));
    }

    private final class MultipleLayoutAdapter extends CommonAdapter<News>{

        public MultipleLayoutAdapter(Context context, int layoutResId, List<News> data) {
            super(context, layoutResId, data);
        }

        @Override public int getLayoutResId(News item) {
            int layoutResId = -1;
            switch (item.getNewsType()){
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

        @Override public void onUpdate(BaseAdapterHelper helper, News item) {
            switch (item.getNewsType()){
                case News.TYPE_NONE_PICTURE:
                    helper.setText(R.id.item_none_picture_title, item.getTitle())
                        .setText(R.id.item_none_picture_author,
                            String.format(Locale.CHINA, Consts.FORMAT_AUTHOR, item.getAuthor()))
                        .setText(R.id.item_none_picture_date,
                            Consts.DATE_FORMAT.format(new Date(item.getReleaseTime())))
                        .setText(R.id.item_none_picture_intro, item.getIntro());
                    break;
                case News.TYPE_SINGLE_PICTURE:
                    helper.setText(R.id.item_single_picture_title, item.getTitle())
                        .setText(R.id.item_single_picture_author,
                            String.format(Locale.CHINA, Consts.FORMAT_AUTHOR, item.getAuthor()))
                        .setText(R.id.item_single_picture_date,
                            Consts.DATE_FORMAT.format(new Date(item.getReleaseTime())))
                        .setImageLoad(new GlideImageLoad())
                        .setImageUrl(R.id.item_single_picture_cover,item.getCoverUrl());
                    break;
                case News.TYPE_MULTIPLE_PICTURE:
                    String[] urls = item.getCoverUrl().split(Consts.URL_SEPARATOR);
                    helper.setText(R.id.item_multiple_picture_intro, item.getIntro())
                        .setImageLoad(new GlideImageLoad())
                        .setImageUrl(R.id.item_multiple_picture_cover_left,urls[0])
                        .setImageUrl(R.id.item_multiple_picture_cover_right, urls[1]);
                    break;
            }
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
