package com.classic.adapter.simple.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.classic.adapter.simple.R;

public abstract class DemoActivity extends AppCompatActivity implements
                                                                Toolbar.OnMenuItemClickListener{
    protected Toolbar mToolbar;
    protected Context mAppContext;

    protected boolean canBack() {
        return false;
    }

    protected abstract int getLayoutResId();

    protected abstract void testAdd();
    protected abstract void testAddAll();
    protected abstract void testSetByIndex();
    protected abstract void testRemoveByIndex();
    protected abstract void testReplaceAll();
    protected abstract void testClear();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mAppContext = getApplicationContext();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("No Toolbar");
        }
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);

        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_menu, menu);
        return true;
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                testAdd();
                break;
            case R.id.add_all:
                testAddAll();
                break;
            case R.id.set_index:
                testSetByIndex();
                break;
            case R.id.remove_index:
                testRemoveByIndex();
                break;
            case R.id.replace_all:
                testReplaceAll();
                break;
            case R.id.clear:
                testClear();
                break;
            default:
                return false;
        }
        return true;
    }
}
