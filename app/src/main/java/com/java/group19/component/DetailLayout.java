package com.java.group19.component;

import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.group19.R;

import java.util.List;

/**
 * Created by 阎世宏 on 2017/9/11.
 */

public class DetailLayout extends LinearLayout {

    private TextView title;
    private TextView author;
    private TextView date;
    private ImageView imageOne;
    private TextView content;
    private LinearLayout imageLayout;
    private LinearLayout classTagLayout;
    private TextView source;
    private ImageView wxContact;
    private ImageView wxMoment;
    private ImageView weibo;
    private ImageView favorite;
    private ImageView startVoice;
    private ImageView stopVoice;

    public DetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.detail_item, this);
        title = (TextView) findViewById(R.id.detail_title);
        author = (TextView) findViewById(R.id.detail_author);
        date = (TextView) findViewById(R.id.detail_date);
        imageOne = (ImageView) findViewById(R.id.detail_image);
        imageOne.setVisibility(GONE);
        content = (TextView) findViewById(R.id.detail_content);
        imageLayout = (LinearLayout) findViewById(R.id.detail_image_layout);
        imageLayout.setVisibility(GONE);
        classTagLayout = (LinearLayout) findViewById(R.id.detail_classtag_layout);
        classTagLayout.setVisibility(GONE);
        source = (TextView) findViewById(R.id.detail_source);
        wxContact = (ImageView) findViewById(R.id.weixin_contacts_share);
        wxMoment = (ImageView) findViewById(R.id.weixin_moment_share);
        weibo = (ImageView) findViewById(R.id.weibo_share);
        favorite = (ImageView) findViewById(R.id.detail_favorite);
        startVoice = (ImageView) findViewById(R.id.detail_voice_start);
        stopVoice = (ImageView) findViewById(R.id.detail_voice_stop);
    }

    public ImageView getStartVoice() {
        return startVoice;
    }

    public CharSequence getTitle() {
        return title.getText();
    }
    
    public void setTitle(CharSequence charSequence) {
        title.setText(charSequence);
    }
    
    public CharSequence getAuthor() {
        return author.getText();
    }
    
    public void setAuthor(CharSequence charSequence) {
        author.setText(charSequence);
    }
    
    public CharSequence getDate() {
        return date.getText();
    }
    
    public void setDate(CharSequence charSequence) {
        date.setText(charSequence);
    }
    
    public CharSequence getContent() {
        return content.getText();
    }
    
    public void setContent(CharSequence charSequence) {
        content.setText(charSequence);
    }
    
    public CharSequence getSource() {
        return source.getText();
    }
    
    public void setSource(CharSequence charSequence) {
        source.setText(charSequence);
    }
    
    public void addImage(ImageView view) {
        //// TODO: 2017/9/11
    }

    public void setOnClickWxContactListener(View.OnClickListener listener) {
        wxContact.setOnClickListener(listener);
    }

    public void setOnClickWxMomentListener(View.OnClickListener listener) {
        wxMoment.setOnClickListener(listener);
    }

    public void setOnClickWeiboListener(View.OnClickListener listener) {
        weibo.setOnClickListener(listener);
    }

    public void setOnClickFavoriteListener(View.OnClickListener listener) {
        favorite.setOnClickListener(listener);
    }

    public void setOnClickStartVoiceListener(View.OnClickListener listener) {
        startVoice.setOnClickListener(listener);
    }

    public void setOnClickStopVoiceListener(View.OnClickListener listener) {
        stopVoice.setOnClickListener(listener);
    }
}
