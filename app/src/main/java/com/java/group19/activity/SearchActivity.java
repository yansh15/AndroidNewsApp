package com.java.group19.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.java.group19.R;
import com.java.group19.adapter.NewsAdapter;
import com.java.group19.data.News;
import com.java.group19.helper.HttpHelper;
import com.java.group19.helper.SharedPreferencesHelper;
import com.java.group19.listener.OnGetNewsListener;
import com.java.group19.listener.OnScrollToBottomListener;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private NewsAdapter adapter;

    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesHelper.getNightMode())
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_search);

        query = getIntent().getStringExtra("query");

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        setipToolbar();

        //set recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        setupRecyclerView();

        HttpHelper.askKeywordNews(query, new OnGetNewsListener() {
            @Override
            public void onFinish(final List<News> newsList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addToLastNewsList(newsList);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });

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

                HttpHelper.askMoreKeywordNews(query, new OnGetNewsListener() {
                    @Override
                    public void onFinish(final List<News> newsList) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addToLastNewsList(newsList);
                                noticeLoadingEnd();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
    }
}
