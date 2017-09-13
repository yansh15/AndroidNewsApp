package com.java.group19.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.java.group19.R;
import com.java.group19.adapter.NewsAdapter;
import com.java.group19.helper.DatabaseHelper;
import com.java.group19.helper.SharedPreferencesHelper;

/**
 * Created by 阎世宏 on 2017/9/13.
 */

public class VoiceSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesHelper.getNightMode())
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_voice_set);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.visited_toolbar);
        setSupportActionBar(toolbar);
        setupToolbar();

        // set seekbars
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.iflytek_prefer_name), MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final SeekBar speedSeekbar = (SeekBar) findViewById(R.id.voice_set_speed_seekbar);
        speedSeekbar.setProgress(Integer.parseInt(sharedPreferences.getString("speed_preference", "50")));
        speedSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress(i);
                editor.putString("speed_preference", "" + i);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar volumeSeekbar = (SeekBar) findViewById(R.id.voice_set_volume_seekbar);
        volumeSeekbar.setProgress(Integer.parseInt(sharedPreferences.getString("volume_preference", "50")));
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress(i);
                editor.putString("volume_preference", "" + i);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar pitchSeekbar = (SeekBar) findViewById(R.id.voice_set_pitch_seekbar);
        pitchSeekbar.setProgress(Integer.parseInt(sharedPreferences.getString("pitch_preference", "50")));
        pitchSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress(i);
                editor.putString("pitch_preference", "" + i);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
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
}
