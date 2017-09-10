package com.java.group19;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.Comparator;

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
        adapter = new NewsAdapter(new Comparator<News>() {
            @Override
            public int compare(News news, News t1) {
                long diff = news.getLastVisitTime().getTime() - t1.getLastVisitTime().getTime();
                if (diff < 0) return -1;
                if (diff == 0) return 0;
                return 1;
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.addNewsListRondom(DatabaseHelper.getLatestVisits(100));
    }
}
