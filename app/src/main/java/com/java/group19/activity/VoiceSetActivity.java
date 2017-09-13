package com.java.group19.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.java.group19.R;
import com.java.group19.adapter.NewsAdapter;
import com.java.group19.helper.DatabaseHelper;
import com.java.group19.helper.SharedPreferencesHelper;

import java.util.Arrays;

/**
 * Created by 阎世宏 on 2017/9/13.
 */

public class VoiceSetActivity extends AppCompatActivity {
    private String[] mCloudVoicersEntries;
    private String[] mCloudVoicersValue;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int selectedNum;
    Button voicerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesHelper.getNightMode())
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_voice_set);

        sharedPreferences = getSharedPreferences(getString(R.string.iflytek_prefer_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mCloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        mCloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);
        selectedNum = Arrays.asList(mCloudVoicersValue).indexOf(sharedPreferences.getString("voicer_preference", "xiaoyan"));

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.voice_toolbar);
        setSupportActionBar(toolbar);
        setupToolbar();

        // set seekbars
        SeekBar speedSeekbar = (SeekBar) findViewById(R.id.voice_set_speed_seekbar);
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
        SeekBar volumeSeekbar = (SeekBar) findViewById(R.id.voice_set_volume_seekbar);
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
        SeekBar pitchSeekbar = (SeekBar) findViewById(R.id.voice_set_pitch_seekbar);
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
        voicerButton = (Button) findViewById(R.id.voice_set_voicer_button);
        voicerButton.setText(mCloudVoicersEntries[selectedNum]);
        voicerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPersonSelectDialog();

            }
        });
    }

    /**
     * 发音人选择。
     */
    private void showPersonSelectDialog() {
        new AlertDialog.Builder(this).setTitle("选择发音人")
                .setSingleChoiceItems(mCloudVoicersEntries, // 单选框有几项，各是什么名字
                        selectedNum, // 默认的选项
                        new DialogInterface.OnClickListener() { // 点击单选框后的处理
                            public void onClick(DialogInterface dialog,
                                                int which) { // 点击了哪一项
                                editor.putString("voicer_preference", mCloudVoicersValue[which]);
                                editor.commit();
                                selectedNum = which;
                                voicerButton.setText(mCloudVoicersEntries[selectedNum]);
                                dialog.dismiss();
                            }
                        }).show();
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
