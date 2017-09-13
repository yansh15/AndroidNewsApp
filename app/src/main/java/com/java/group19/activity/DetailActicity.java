package com.java.group19.activity;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;

import com.java.group19.NewsApp;
import com.java.group19.component.DetailLayout;
import com.java.group19.R;
import com.java.group19.helper.DatabaseHelper;
import com.java.group19.helper.ShareHelper;
import com.java.group19.helper.SharedPreferencesHelper;
import com.java.group19.helper.SpeechHelper;
import com.java.group19.listener.OnFinishSpeakingListener;
import com.java.group19.data.News;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.srain.cube.image.CubeImageView;

public class DetailActicity extends AppCompatActivity {

    private DetailLayout detailLayout;
    private SpeechHelper speechHelper;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesHelper.getNightMode())
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_detail);

        //set toolber
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        setupToolbar();*/

        //set fab
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.detail_fab);
        fab.setOnClickListener(this);*/

        news = (News) getIntent().getSerializableExtra("news");
        News newNews = DatabaseHelper.getNews(news.getUniqueId());
        if (newNews != null) {
            news = newNews;
        } else {
            DatabaseHelper.saveNews(news);
        }
        DatabaseHelper.setNewsVisitCount(news.getUniqueId(), news.getVisitCount() + 1);
        DatabaseHelper.setNewsLastVisitTime(news.getUniqueId(), new Date());
        detailLayout = (DetailLayout) findViewById(R.id.detail_layout);
        detailLayout.setAuthor(news.getAuthor());
        detailLayout.setTitle(news.getTitle());
        detailLayout.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(news.getTime()));
        detailLayout.setSource(news.getSource());
        detailLayout.setFavoriteStatus(news.getLastFavoriteTime().getTime() != 0);
        // set news content with baike links
        String content = news.getContent();
        SpannableString spannableContent = new SpannableString(content);
        for (String entry : news.getEntries()) {
            for (int i = content.indexOf(entry, 0); i != -1; ) {
                int j = i + entry.length();
                spannableContent.setSpan(new URLSpan("http://www.baike.com/wiki/" + entry), i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                i = content.indexOf(entry, j);
            }
        }
        detailLayout.setContent(spannableContent);
        if (!SharedPreferencesHelper.getTextMode()) {
            List<String> pictures = news.getPictures();
            for (String picture : pictures) {
                CubeImageView view = (CubeImageView) getLayoutInflater().inflate(R.layout.detail_news_picture, detailLayout, false);
                view.loadImage(((NewsApp) getApplicationContext()).getImageLoader(), picture);
                detailLayout.addImage(view);
            }
        }

        // set speechHelper
        speechHelper = ((NewsApp) getApplicationContext()).getSpeechHelper();
        speechHelper.setOnFinishSpeakingListener(new OnFinishSpeakingListener() {
            @Override
            public void onFinishSpeaking() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        detailLayout.getStartVoice().setTag("toStart");
                        detailLayout.getStartVoice().setImageResource(R.drawable.ic_keyboard_voice_black);
                    }
                });
            }
        });
        detailLayout.setOnClickStartVoiceListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag().equals("toStart")) {
                    if (speechHelper.isSpeaking()) {
                        speechHelper.resume();
                    } else {
                        speechHelper.speak(detailLayout.getContent().toString());
                    }
                    view.setTag("toPause");
                    ((ImageView) view).setImageResource(R.drawable.ic_keyboard_voice_pause_black);
                } else {
                    speechHelper.pause();
                    view.setTag("toStart");
                    ((ImageView) view).setImageResource(R.drawable.ic_keyboard_voice_black);
                }
            }
        });
        detailLayout.setOnClickStopVoiceListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechHelper.stop();
            }
        });

        // set Favorite
        detailLayout.setOnClickFavoriteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag().equals("toSetFavorite")) {
                    DatabaseHelper.setNewsLastFavoriteTime(news.getUniqueId(), new Date());
                    detailLayout.setFavoriteStatus(true);
                } else {
                    DatabaseHelper.setNewsLastFavoriteTime(news.getUniqueId(), new Date(0));
                    detailLayout.setFavoriteStatus(false);
                }
            }
        });

        detailLayout.setOnClickShareListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareHelper.showShare(DetailActicity.this, news);
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_detail, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }*/

    @Override
    public void onBackPressed() {
        speechHelper.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        speechHelper.stop();
        super.onDestroy();
    }
}
