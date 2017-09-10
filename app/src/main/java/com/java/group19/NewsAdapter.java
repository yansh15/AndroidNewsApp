package com.java.group19;

import android.content.Context;
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

    private SimpleDateFormat dateFormat;

    static class ViewHolder extends RecyclerView.ViewHolder {
        NewsCardView newsCardView;

        public ViewHolder(View view) {
            super(view);
            newsCardView = (NewsCardView) view;
        }
    }

    public NewsAdapter() {
        mNewsList = new Vector<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        holder.newsCardView.setTitle(news.getTitle());
        Vector<String> pictures = news.getPictures();
        if (DatabaseHelper.isTextMode() || pictures.isEmpty())
            holder.newsCardView.setImageViewVisibility(View.GONE);
        else {
            for (int i = 0; i < Math.min(3, pictures.size()); ++i) {
                //todo get image and set image
            }
        }
        holder.newsCardView.setAuthor(news.getAuthor());
        holder.newsCardView.setDate(dateFormat.format(news.getTime()));
        holder.newsCardView.setClassTag(news.getClassTag());
        holder.newsCardView.setIntro(news.getIntro());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void addNewsList(List<News> newsList) {
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
