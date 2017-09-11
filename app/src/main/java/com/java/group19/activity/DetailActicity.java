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
import com.java.group19.TextSpeaker;
import com.java.group19.component.DetailLayout;
import com.java.group19.helper.DatabaseHelper;
import com.java.group19.helper.HttpHelper;
import com.java.group19.R;
import com.java.group19.listener.OnFinishSpeakingListener;
import com.java.group19.listener.OnGetDetailListener;
import com.java.group19.listener.OnGetImagesListener;
import com.java.group19.data.News;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActicity extends AppCompatActivity {

    private DetailLayout detailLayout;
    private TextSpeaker textSpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //set toolber
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        setupToolbar();*/

        //set fab
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.detail_fab);
        fab.setOnClickListener(this);*/

        //set content
        detailLayout = (DetailLayout) findViewById(R.id.detail_layout);

        textSpeaker = TextSpeaker.getInstance(this);
        textSpeaker.setOnFinishSpeakingListener(new OnFinishSpeakingListener() {
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
                    if (textSpeaker.isSpeaking()) {
                        textSpeaker.resume();
                    } else {
                        textSpeaker.speak(detailLayout.getContent().toString());
                    }
                    view.setTag("toPause");
                    ((ImageView) view).setImageResource(R.drawable.ic_keyboard_voice_pause_black);
                } else {
                    textSpeaker.pause();
                    view.setTag("toStart");
                    ((ImageView) view).setImageResource(R.drawable.ic_keyboard_voice_black);
                }
            }
        });
        detailLayout.setOnClickStopVoiceListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSpeaker.stop();
                view.setTag("toStart");
                ((ImageView) view).setImageResource(R.drawable.ic_keyboard_voice_black);
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
        textSpeaker.stop();
        super.onBackPressed();
    }
}
