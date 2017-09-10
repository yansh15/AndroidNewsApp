package com.java.group19;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by liena on 17/9/8.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mContext;
    private List<News> mNewsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        NewsCardView newsCardView;

        public ViewHolder(View view) {
            super(view);
            newsCardView = (NewsCardView) view;
        }
    }

    public NewsAdapter() {
        mNewsList = new Vector<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        holder.newsCardView.setNews(news);
        holder.newsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActicity.class);
                intent.putExtra("news", ((NewsCardView)view).getNews());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void addNewsListRondom(List<News> newsList) {
        for (News news : newsList)
            mNewsList.add(news);
        Collections.sort(mNewsList, new Comparator<News>() {
            @Override
            public int compare(News news, News t1) {
                long diff = news.getTime().getTime() - t1.getTime().getTime();
                if (diff < 0) return -1;
                if (diff == 0) return 0;
                return 1;
            }
        });
        notifyDataSetChanged();
    }
}
