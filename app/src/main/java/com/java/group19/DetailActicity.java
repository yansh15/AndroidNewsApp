package com.java.group19;

import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DetailActicity extends AppCompatActivity implements View.OnClickListener {

    private News detailNews;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ImageView imageOne;
    private LinearLayout imageLayout;
    private LinearLayout classTagLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //set toolber
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        setupToolbar();

        //set fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.detail_fab);
        fab.setOnClickListener(this);

        //set Visibility
        setupLayoutVisibility();

        //set news
        getDetailNews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_detail, menu);
        return true;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_fab:
                //// TODO: 17/9/10  snackbar
                break;
            case R.id.detail_source:
                //// TODO: 17/9/10 Uri
                break;
            default:
                break;
        }
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);
        }
    }

    private void setupLayoutVisibility() {
        imageOne = (ImageView) findViewById(R.id.detail_image);
        imageOne.setVisibility(View.GONE);
        imageLayout = (LinearLayout) findViewById(R.id.detail_image_layout);
        imageLayout.setVisibility(View.GONE);
        classTagLayout = (LinearLayout) findViewById(R.id.detail_classtag_layout);
        classTagLayout.setVisibility(View.GONE);
    }

    private void getDetailNews() {
        final News news = (News) getIntent().getSerializableExtra("news");
        detailNews = DatabaseHelper.getNews(news.getUniqueId());
        if (detailNews == null) {
            HttpHelper.askDetailNews(news, new CallBack() {
                @Override
                public void onFinishNewsList(List<News> newsList) {

                }

                @Override
                public void onFinishDetail() {
                    detailNews = news;
                    DatabaseHelper.saveNews(detailNews);
                    setupNews();
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            detailNews.setLastVisitTime(new Date());
            DatabaseHelper.saveNews(detailNews);
            setupNews();
        }
    }

    private void setupNews() {
        ((TextView) findViewById(R.id.detail_title)).setText(detailNews.getTitle());
        ((TextView) findViewById(R.id.detail_author)).setText(detailNews.getAuthor());
        ((TextView) findViewById(R.id.detail_date)).setText(dateFormat.format(detailNews.getTime()));
        ((TextView) findViewById(R.id.detail_content)).setText(detailNews.getContent());
        TextView source = (TextView) findViewById(R.id.detail_source);
        source.setText(detailNews.getSource());
        source.setOnClickListener(this);

        List<String> urls = detailNews.getPictures();
        List<String> files = new ArrayList<>();
        for (int i = 0; i < urls.size(); ++i) {
            files.add(urls.get(i).replaceAll("[^A-Za-z0-9.]", ""));
        }
        if (!files.isEmpty()) {
            //// TODO: 17/9/10
        }
        if (files.size() > 1) {
            //// TODO: 17/9/10
        }
    }
}
