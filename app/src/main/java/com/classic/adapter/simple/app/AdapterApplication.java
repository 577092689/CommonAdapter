package com.classic.adapter.simple.app;

import android.app.Application;

import com.classic.adapter.Adapter;
import com.classic.adapter.simple.imageload.GlideImageLoad;

public class AdapterApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Adapter.config(new Adapter.Builder().setImageLoad(new GlideImageLoad()));
    }
}
