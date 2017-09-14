package com.java.group19.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.group19.R;
import com.java.group19.data.News;
import com.java.group19.helper.DatabaseHelper;
import com.java.group19.helper.SharedPreferencesHelper;

import java.text.SimpleDateFormat;
import java.util.List;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;

public class NewsItem extends LinearLayout {

    private Context context;
    private News news;
    private TextView title;
    private LinearLayout newsItem;
    private CubeImageView image;
    private TextView author;
    private TextView date;
    private TextView classTag;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public NewsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.news_item, this);
        title = (TextView) findViewById(R.id.news_title);
        newsItem = (LinearLayout) findViewById(R.id.news_item);
        image = (CubeImageView) findViewById(R.id.news_image);
        author = (TextView) findViewById(R.id.news_author);
        date = (TextView) findViewById(R.id.news_date);
        classTag = (TextView) findViewById(R.id.news_classtag);
    }

    public void setNews(News news, ImageLoader imageLoader) {
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[] {
                R.attr.textColorSecondary,
                R.attr.textColorPrimary,
        });
        final int secondColor = array.getColor(0, 0xFF00FF);
        final int textColor = array.getColor(1, 0xFF00FF);
        this.news = news;
        title.setText(news.getTitle());
        if (SharedPreferencesHelper.getTextMode() || news.getPictures().size() == 0) {
            image.setVisibility(View.GONE);
        }
        else {
            image.setVisibility(View.VISIBLE);
            image.loadImage(imageLoader, news.getPictures().get(0));
        }
        if (news.getAuthor().isEmpty())
            author.setText("佚名");
        else
            author.setText(news.getAuthor());
        date.setText(dateFormat.format(news.getTime()));
        classTag.setText(news.getClassTag());
        News datanews = DatabaseHelper.getNews(news.getUniqueId());
        if (datanews != null && datanews.getLastVisitTime().getTime() > 0) {
            title.setTextColor(secondColor);
            author.setTextColor(secondColor);
            date.setTextColor(secondColor);
            classTag.setTextColor(secondColor);
        } else {
            title.setTextColor(textColor);
            author.setTextColor(textColor);
            date.setTextColor(textColor);
            classTag.setTextColor(textColor);
        }
    }

    public News getNews() {
        return news;
    }
}
