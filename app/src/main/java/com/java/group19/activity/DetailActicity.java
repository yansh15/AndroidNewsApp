package com.java.group19.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.java.group19.NewsApp;
import com.java.group19.component.DetailLayout;
import com.java.group19.R;
import com.java.group19.helper.SharedPreferencesHelper;
import com.java.group19.helper.SpeechHelper;
import com.java.group19.listener.OnFinishSpeakingListener;
import com.java.group19.data.News;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.srain.cube.image.CubeImageView;

public class DetailActicity extends AppCompatActivity {

    private DetailLayout detailLayout;
    private SpeechHelper speechHelper;
    private News news;

    private WbShareHandler shareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //set weibo
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();

        //set toolber
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        setupToolbar();*/

        //set fab
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.detail_fab);
        fab.setOnClickListener(this);*/

        news = (News) getIntent().getSerializableExtra("news");
        news.setVisitCount(news.getVisitCount() + 1);
        news.setLastVisitTime(new Date());
        news.save();
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
                CubeImageView view = new CubeImageView(this);
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
                    news.setLastFavoriteTime(new Date());
                    news.save();
                    detailLayout.setFavoriteStatus(true);
                } else {
                    news.setLastFavoriteTime(new Date(0));
                    news.save();
                    detailLayout.setFavoriteStatus(false);
                }
            }
        });

        //set weibo
        detailLayout.setOnClickWeiboListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
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

    //set weibo

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, new WbShareCallback() {
            @Override
            public void onWbShareSuccess() {
                Toast.makeText(DetailActicity.this, "分享成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onWbShareCancel() {
                Toast.makeText(DetailActicity.this, "分享取消", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onWbShareFail() {
                Toast.makeText(DetailActicity.this, "分享失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendMessage() {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = new TextObject();
        weiboMultiMessage.textObject.text = "Monkey的新闻应用";
        weiboMultiMessage.textObject.title = "Monkey";
        weiboMultiMessage.textObject.actionUrl = "https://github.com/yansh15";
        shareHandler.shareMessage(weiboMultiMessage, false);
    }
}
