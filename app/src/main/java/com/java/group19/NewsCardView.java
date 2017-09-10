package com.java.group19;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by liena on 17/9/10.
 */

public class NewsCardView extends CardView {

    private TextView title;
    private LinearLayout imageLayout;
    private ImageView imageOne;
    private ImageView imageTwo;
    private ImageView imageThree;
    private TextView author;
    private TextView date;
    private TextView classTag;
    private ImageView hideIntro;
    private ImageView displayIntro;
    private TextView intro;

    public NewsCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.news_item, this);
        title = (TextView) findViewById(R.id.news_title);
        imageLayout = (LinearLayout) findViewById(R.id.news_images);
        imageOne = (ImageView) imageLayout.findViewById(R.id.news_image_one);
        imageTwo = (ImageView) imageLayout.findViewById(R.id.news_image_two);
        imageThree = (ImageView) imageLayout.findViewById(R.id.news_image_three);
        author = (TextView) findViewById(R.id.news_author);
        date = (TextView) findViewById(R.id.news_date);
        classTag = (TextView) findViewById(R.id.news_classtag);
        hideIntro = (ImageView) findViewById(R.id.hide_intro);
        displayIntro = (ImageView) findViewById(R.id.display_intro);
        intro = (TextView) findViewById(R.id.news_intro);

        intro.setVisibility(View.GONE);
        hideIntro.setVisibility(View.GONE);
        hideIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intro.setVisibility(View.GONE);
                hideIntro.setVisibility(View.GONE);
                displayIntro.setVisibility(View.VISIBLE);
            }
        });
        displayIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intro.setVisibility(View.VISIBLE);
                hideIntro.setVisibility(View.VISIBLE);
                displayIntro.setVisibility(View.GONE);
            }
        });
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setImageViewVisibility(int visibility) {
        imageLayout.setVisibility(visibility);
    }

    public void setImage(int index) {
        //// TODO: 17/9/10
    }

    public void setAuthor(String text) {
        author.setText(text);
    }

    public void setDate(String text) {
        date.setText(text);
    }

    public void setClassTag(String text) {
        classTag.setText(text);
    }

    public void setIntro(String text) {
        intro.setText(text);
    }
}
