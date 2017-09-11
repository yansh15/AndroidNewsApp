package com.java.group19.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java.group19.activity.DetailActicity;
import com.java.group19.component.NewsCardView;
import com.java.group19.R;
import com.java.group19.data.News;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by liena on 17/9/8.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mContext;
    private List<News> mNewsList;
    private Comparator<News> comparator;

    static class ViewHolder extends RecyclerView.ViewHolder {
        NewsCardView newsCardView;

        public ViewHolder(View view) {
            super(view);
            newsCardView = (NewsCardView) view;
        }
    }

    public NewsAdapter(Comparator<News> comparator) {
        mNewsList = new ArrayList<>();
        this.comparator = comparator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_news_item, parent, false);
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
        Collections.sort(mNewsList, comparator);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return mNewsList.isEmpty();
    }
}
