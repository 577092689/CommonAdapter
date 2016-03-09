package com.classic.adapter.simple.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.classic.adapter.simple.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_listview_simple_tv).setOnClickListener(this);
        findViewById(R.id.main_listview_tv).setOnClickListener(this);
        findViewById(R.id.main_recyclerview_simple_tv).setOnClickListener(this);
        findViewById(R.id.main_recyclerview_tv).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_listview_simple_tv:
                startActivity(new Intent(this,ListViewSimpleActivity.class));
                break;
            case R.id.main_listview_tv:
                startActivity(new Intent(this,ListViewActivity.class));
                break;
            case R.id.main_recyclerview_simple_tv:
                startActivity(new Intent(this,RecyclerViewSimpleActivity.class));
                break;
            case R.id.main_recyclerview_tv:
                startActivity(new Intent(this,RecyclerViewActivity.class));
                break;
        }
    }
}
