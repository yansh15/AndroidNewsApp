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
import java.util.List;
import java.util.Vector;

/**
 * Created by liena on 17/9/8.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mContext;

    private List<News> mNewsList;

    private boolean textMode;

    private SimpleDateFormat dateFormat;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        LinearLayout imageLayout;
        ImageView imageOne;
        ImageView imageTwo;
        ImageView imageThree;
        TextView author;
        TextView date;
        TextView classTag;
        ImageView hideIntro;
        ImageView displayIntro;
        TextView intro;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            title = (TextView) cardView.findViewById(R.id.news_title);
            imageLayout = (LinearLayout) cardView.findViewById(R.id.news_images);
            imageOne = (ImageView) imageLayout.findViewById(R.id.news_image_one);
            imageTwo = (ImageView) imageLayout.findViewById(R.id.news_image_two);
            imageThree = (ImageView) imageLayout.findViewById(R.id.news_image_three);
            author = (TextView) cardView.findViewById(R.id.news_author);
            date = (TextView) cardView.findViewById(R.id.news_date);
            classTag = (TextView) cardView.findViewById(R.id.news_classtag);
            hideIntro = (ImageView) cardView.findViewById(R.id.hide_intro);
            displayIntro = (ImageView) cardView.findViewById(R.id.display_intro);
            intro = (TextView) cardView.findViewById(R.id.news_intro);
        }
    }

    public NewsAdapter(List<News> newsList, boolean textMode) {
        mNewsList = newsList;
        this.textMode = textMode;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        holder.title.setText(news.getTitle());
        Vector<String> pictures = news.getPictures();
        if (textMode || pictures.isEmpty())
            holder.imageLayout.setVisibility(View.GONE);
        else {
            for (int i = 0; i < Math.min(3, pictures.size()); ++i) {
                //todo get image and set image
            }
        }
        holder.author.setText(news.getAuthor());
        holder.date.setText(dateFormat.format(news.getTime()));
        holder.classTag.setText(news.getClassTag());
        holder.intro.setText(news.getIntro());
        holder.intro.setVisibility(View.GONE);
        holder.hideIntro.setVisibility(View.GONE);
        holder.hideIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.intro.setVisibility(View.GONE);
                holder.hideIntro.setVisibility(View.GONE);
                holder.displayIntro.setVisibility(View.VISIBLE);
            }
        });
        holder.displayIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.intro.setVisibility(View.VISIBLE);
                holder.hideIntro.setVisibility(View.VISIBLE);
                holder.displayIntro.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
