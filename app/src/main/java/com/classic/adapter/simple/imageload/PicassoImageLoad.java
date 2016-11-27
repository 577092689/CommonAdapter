package com.classic.adapter.simple.imageload;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.classic.adapter.interfaces.ImageLoad;
import com.squareup.picasso.Picasso;

public class PicassoImageLoad implements ImageLoad {

    @Override public void load(@NonNull Context context,
                               @NonNull ImageView imageView,
                               @NonNull String imageUrl) {
        Picasso.with(context).load(imageUrl).fit().centerCrop().into(imageView);
    }
}
