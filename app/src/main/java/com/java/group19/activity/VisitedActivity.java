package com.java.group19.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.java.group19.R;
import com.java.group19.adapter.NewsAdapter;
import com.java.group19.helper.DatabaseHelper;
import com.java.group19.listener.OnScrollToBottomListener;

import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;

public class VisitedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private NewsAdapter adapter;

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);
        imageLoader = ImageLoaderFactory.create(this);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.favorite_toolbar);
        setSupportActionBar(toolbar);
        setupToolbar();

        //set recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.favorite_recycler_view);
        setupRecyclerView();

        adapter.setNewsList(DatabaseHelper.getLatestAllVisits());
    }

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

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(null, imageLoader);
        recyclerView.setAdapter(adapter);
    }
}
