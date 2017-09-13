package com.java.group19.component;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.group19.R;

import in.srain.cube.image.CubeImageView;

public class DetailLayout extends LinearLayout {

    private TextView title;
    private TextView author;
    private TextView date;
    private TextView content;
    private LinearLayout detailItem;
    private LinearLayout imageLayout;
    private LinearLayout classTagLayout;
    private TextView source;
    private ImageView share;
    private ImageView favorite;
    private ImageView startVoice;
    private ImageView stopVoice;
    private int imageCount = 0;

    public DetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.detail_item, this);
        title = (TextView) findViewById(R.id.detail_title);
        author = (TextView) findViewById(R.id.detail_author);
        date = (TextView) findViewById(R.id.detail_date);
        content = (TextView) findViewById(R.id.detail_content);
        content.setMovementMethod(LinkMovementMethod.getInstance());
        detailItem = (LinearLayout) findViewById(R.id.detail_item);
        imageLayout = (LinearLayout) findViewById(R.id.detail_image_layout);
        imageLayout.setVisibility(GONE);
        classTagLayout = (LinearLayout) findViewById(R.id.detail_classtag_layout);
        classTagLayout.setVisibility(GONE);
        source = (TextView) findViewById(R.id.detail_source);
        share = (ImageView) findViewById(R.id.weibo_share);
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
    
    public void addImage(CubeImageView view) {
        if (++imageCount == 1) {
            detailItem.addView(view, 2);
        } else {
            imageLayout.setVisibility(VISIBLE);
            imageLayout.addView(view);
        }
    }

    public void setOnClickShareListener(View.OnClickListener listener) {
        share.setOnClickListener(listener);
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

    public void setFavoriteStatus(boolean flag) {
        if (flag) {
            favorite.setTag("toCancelFavorite");
            favorite.setImageResource(R.drawable.ic_favorite_black);
        } else {
            favorite.setTag("toSetFavorite");
            favorite.setImageResource(R.drawable.ic_favorite_border_black);
        }
    }
}
