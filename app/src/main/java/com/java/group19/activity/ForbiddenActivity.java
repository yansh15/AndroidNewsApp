package com.java.group19.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.java.group19.R;
import com.java.group19.adapter.ForbiddenWordAdapter;
import com.java.group19.adapter.NewsAdapter;
import com.java.group19.helper.SharedPreferencesHelper;

public class ForbiddenActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ForbiddenWordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesHelper.getNightMode())
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_forbidden);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.forbidden_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);
        }

        //set RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.forbidden_word_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ForbiddenWordAdapter();
        recyclerView.setAdapter(adapter);

        //set button
        Button button = (Button) findViewById(R.id.forbidden_add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
