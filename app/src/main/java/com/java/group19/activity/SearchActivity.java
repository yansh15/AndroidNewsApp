package com.java.group19.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.java.group19.helper.DatabaseHelper;
import com.java.group19.R;
import com.java.group19.adapter.NewsAdapter;
import com.java.group19.data.News;
import com.java.group19.listener.OnScrollToBottomListener;

import java.util.Comparator;

import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        setipToolbar();

        //set recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        setupRecyclerView();

        //// TODO: 2017/9/12 //获取数据
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void setipToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(null);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnScrollToBottomListener() {
            @Override
            public void onScrollToBottom() {
                //// TODO: 2017/9/11 划至底部，加载新内容
                // 加载完毕后调用this.noticeLoadingEnd();
            }
        });
    }
}
