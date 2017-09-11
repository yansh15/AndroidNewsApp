package com.java.group19.activity;

import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.java.group19.helper.DatabaseHelper;
import com.java.group19.helper.HttpHelper;
import com.java.group19.R;
import com.java.group19.listener.OnGetDetailListener;
import com.java.group19.listener.OnGetImagesListener;
import com.java.group19.data.News;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            HttpHelper.askDetailNews(news, new OnGetDetailListener() {
                @Override
                public void onFinish() {
                    detailNews = news;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupNews();
                        }
                    });
                    List<String> urls = detailNews.getPictures();
                    final ArrayList<String> files = new ArrayList<>();
                    HttpHelper.downloadImage(DetailActicity.this, urls, new OnGetImagesListener() {
                        @Override
                        public void onFinish(ArrayList<String> urls) {
                            detailNews.setPictures(urls);
                            detailNews.setLastVisitTime(new Date());
                            DatabaseHelper.saveNews(detailNews);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadImages();
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {}
                    });
                }

                @Override
                public void onError(Exception e) {}
            });
        } else {
            detailNews.setLastVisitTime(new Date());
            DatabaseHelper.saveNews(detailNews);
            setupNews();
            loadImages();
        }
    }

    private void setupNews() {
        ((TextView) findViewById(R.id.detail_title)).setText(detailNews.getTitle());
        ((TextView) findViewById(R.id.detail_author)).setText(detailNews.getAuthor());
        ((TextView) findViewById(R.id.detail_date)).setText(dateFormat.format(detailNews.getTime()));

        // set news content with baike links
        String content = detailNews.getContent();
        SpannableString spannableContent = new SpannableString(content);
        for (String entry : detailNews.getEntries()) {
            for (int i = content.indexOf(entry, 0); i != -1; ) {
                int j = i + entry.length();
                spannableContent.setSpan(new URLSpan("http://www.baike.com/wiki/" + entry), i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                i = content.indexOf(entry, j);
            }
        }
        ((TextView) findViewById(R.id.detail_content)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.detail_content)).setText(spannableContent);

        TextView source = (TextView) findViewById(R.id.detail_source);
        source.setText(detailNews.getSource());
        source.setOnClickListener(this);
    }

    private void loadImages() {
        File path = new File(Environment.getDataDirectory().getAbsoluteFile(), "/data/com.java.group19/newsPicture");
        if (!detailNews.getPictures().isEmpty()) {
            String name = detailNews.getPictures().get(0).replaceAll("[^A-Za-z0-9.]", "");
            File pic = new File(path, name);
            Glide.with(this).load(pic).into(imageOne);
            imageOne.setVisibility(View.VISIBLE);
        }
        if (detailNews.getPictures().size() > 1) {
            imageLayout.setVisibility(View.VISIBLE);
            for (int i = 1; i < detailNews.getPictures().size(); ++i) {
                String name = detailNews.getPictures().get(i).replaceAll("[^A-Za-z0-9.]", "");
                ImageView view = new ImageView(this);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                File pic = new File(path, name);
                Glide.with(this).load(pic).into(view);
                imageLayout.addView(view);
            }
        }
    }
}
