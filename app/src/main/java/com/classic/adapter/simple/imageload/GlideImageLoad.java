package com.classic.adapter.simple.imageload;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.classic.adapter.interfaces.ImageLoad;

/**
 * 项目名称：CommonAdapter
 * 包名称：com.classic.adapter.simple.imageload
 * 类描述：
 * 创建人： 刘宾
 * 创建时间 2016/1/27 16:44.
 */
public class GlideImageLoad implements ImageLoad {
    @Override public void load(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context).load(imageUrl).centerCrop().crossFade().into(imageView);
    }
}
