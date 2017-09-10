package com.java.group19;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.group19.data.News;
import com.java.group19.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by liena on 17/9/10.
 */

public class NewsCardView extends CardView {

    private News news;
    private TextView title;
    private LinearLayout imageLayout;
    private ImageView imageOne;
    private ImageView imageTwo;
    private ImageView imageThree;
    private TextView author;
    private TextView date;
    private TextView classTag;
    private TextView intro;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
        intro = (TextView) findViewById(R.id.news_intro);
    }

    public void setNews(News news) {
        this.news = news;
        title.setText(news.getTitle());
        List<String> pictures = news.getPictures();
        if (DatabaseHelper.isTextMode() || pictures.isEmpty())
            imageLayout.setVisibility(View.GONE);
        else {
            for (int i = 0; i < Math.min(3, pictures.size()); ++i) {
                //todo get image and set image
            }
        }
        author.setText(news.getAuthor());
        date.setText(dateFormat.format(news.getTime()));
        classTag.setText(news.getClassTag());
        intro.setText(news.getIntro());
    }

    public News getNews() {
        return news;
    }
}
